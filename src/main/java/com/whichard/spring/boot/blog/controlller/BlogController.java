package com.whichard.spring.boot.blog.controlller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whichard.spring.boot.blog.async.EventModel;
import com.whichard.spring.boot.blog.async.EventProducer;
import com.whichard.spring.boot.blog.async.EventType;
import com.whichard.spring.boot.blog.domain.Blog;
import com.whichard.spring.boot.blog.service.BadIpLoginService;
import com.whichard.spring.boot.blog.service.BlogService;
import com.whichard.spring.boot.blog.service.MessageService;
import com.whichard.spring.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.domain.es.EsBlog;
import com.whichard.spring.boot.blog.service.EsBlogService;
import com.whichard.spring.boot.blog.vo.TagVO;
import org.springframework.web.servlet.ModelAndView;

/**
 * Blog 控制器.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月28日
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    @Autowired
    private BlogService blogService;

    @Autowired
    MessageService messageService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    BadIpLoginService badIpLoginService;

    /**
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param title
     * @param model
     * @return
     */
    @GetMapping("/all")
    public ModelAndView list(@RequestParam(value = "async", required = false) boolean async,
                             @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                             @RequestParam(value = "title", required = false, defaultValue = "") String title,
                             Model model) {

        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<Blog> page = blogService.listBlogsByTitle(title, pageable);
        List<Blog> list = page.getContent();    // 当前所在页面数据列表

        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return new ModelAndView(async == true ? "users/listBlogs :: #mainContainerRepleace" : "users/listBlogs", "blogModel", model);
    }


    @GetMapping
    public String listEsBlogs(
            @RequestParam(value = "order", required = false, defaultValue = "new") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            Model model) {

        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User user = null;
            try {
                user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            } catch (Exception e1) {
            }
            //System.out.println(user == null ? null : user.getUsername());
            //System.out.println(badIpLoginService.getCurrentAddress());
            String addr = badIpLoginService.getCurrentAddress();
            if(user.getAddress() == null)
                user.setAddress(addr);
            else if(addr != user.getAddress()) {
                System.out.println("异地登录：" + addr);
                eventProducer.fireEvent(new EventModel(EventType.MAIL)
                        .setActorId(user.getId().intValue()).setExts("username", user.getUsername()).setExts("addr", addr)
                );
            }

        }

        Page<EsBlog> page = null;

        List<EsBlog> list = null;
        boolean isEmpty = true; // 系统初始化时，没有博客数据
        try {
            if (order.equals("hot")) { // 最热查询
                //              Sort sort = new Sort(Direction.DESC,"priority","readSize","commentSize","voteSize","createTime");
                Pageable pageable = new PageRequest(pageIndex, pageSize);
                page = esBlogService.listHotestEsBlogs(keyword, pageable);
                //page = blogService.listHotestBlogs(pageable);
            } else if (order.equals("new")) { // 最新查询
                //             Sort sort = new Sort(Direction.DESC,"priority","createTime");
                Pageable pageable = new PageRequest(pageIndex, pageSize);
                page = esBlogService.listNewestEsBlogs(keyword, pageable);
                //page = blogService.listNewestBlogs(pageable);
            }

            isEmpty = false;
        } catch (Exception e) {
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            //page = blogService.listAllBlogs(pageable);
        }

        list = page.getContent();   // 当前所在页面数据列表

        //获取未读消息数，用于首页展示
        User curr = null;
        try {
            curr  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {

        }
        int unread = 0;
        if(curr != null)
            unread = messageService.getTotalUnread(curr.getId().intValue());

        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        model.addAttribute("unread", unread);

        // 首次访问页面才加载，在翻页下一页搜索博客等等，不需要再加载这些内容（此时已经注入）
        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest", newest);
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest", hotest);
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("users", users);
        }

        return (async == true ? "/index :: #mainContainerRepleace" : "/index");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteBlog(@PathVariable("id") Long id) {
        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "删除博客成功"));
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> refreshEs() {
        try {
            blogService.refreshES();
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "同步数据到ES成功"));
    }
}
