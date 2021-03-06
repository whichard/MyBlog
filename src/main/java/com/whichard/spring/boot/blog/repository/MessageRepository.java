package com.whichard.spring.boot.blog.repository;

import com.whichard.spring.boot.blog.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wq
 * @date 2019/5/5
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedDateDesc(String conversationId);

    //对话中消息总条数
    int countByConversationId(String conversationId);

    //对话列表
      @Query(
            value = "select from_id, to_id, content, has_read, conversation_id, created_date ,count(id) as id from ( select * from message where to_id= ?1 order by id desc) tt group by conversation_id order by created_date desc",
            nativeQuery = true)
    List<Message> getConversationList(int userId);

      //某个对话中未读消息
    int countByHasReadAndToIdAndConversationId(int hasRead, int userId, String conversationId);

    //全部未读消息
    int countByHasReadAndToId(int hasRead, int toId);

    int deleteByToIdAndConversationId(int toId, String conversationId);

    int deleteById(int msgId);
}
