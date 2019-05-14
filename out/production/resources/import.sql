INSERT INTO user (id, username, password, name, email) VALUES (1, 'wqe', '0a0484413c1f2968933d7fa2bfa2b429', 'wq', 'whichard@wq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (2, 'admin', '0a0484413c1f2968933d7fa2bfa2b429', 'admin', 'whichard@wq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (3, 'zll', '0a0484413c1f2968933d7fa2bfa2b429', 'zll', 'zlu40@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (4, 'zl2', '0a0484413c1f2968933d7fa2bfa2b429', 'zl2', 'zlu402@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (5, 'zl3', '0a0484413c1f2968933d7fa2bfa2b429', 'zll3', 'zlu403@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (6, 'zll4', '0a0484413c1f2968933d7fa2bfa2b429', 'zll4', 'zlu404@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (7, 'zll5', '0a0484413c1f2968933d7fa2bfa2b429', 'zll5', 'zlu405@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (8, 'zll6', '0a0484413c1f2968933d7fa2bfa2b429', 'zll6', 'zlu406@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (9, 'zll7', '0a0484413c1f2968933d7fa2bfa2b429', 'zll7', 'zlu407@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (10, 'zll8', '0a0484413c1f2968933d7fa2bfa2b429', 'zll8', 'zlu408@qq.com');

INSERT INTO authority (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (3, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (4, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (5, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (6, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (7, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (8, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (9, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (10, 'ROLE_USER');

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (4, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (5, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (6, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (7, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (8, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (9, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (10, 2);