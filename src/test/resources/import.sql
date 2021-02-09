INSERT INTO role (id,code,name) VALUES (1, 'ROLE_SUPER_MANAGER', '超級管理者');
INSERT INTO role (id,code,name) VALUES (2, 'ROLE_MANAGER', '一般管理者');

INSERT INTO floor(id, name) VALUES (1, '3F');
INSERT INTO floor(id, name) VALUES (2, '24F');

insert into user (id,email,english_name,name,password,username,floor_id) values(1,'ROOT@tgfc.tw','admin','超級管理員','{bcrypt}$2a$10$BcKLKCNcduU4.mfF/NXE5elTMeExQd1jRjn0xC8QB2iDqE3GGXQZu','admin',1);
insert into user (id,email,english_name,name,password,username,floor_id) values(2,'ROOT@tgfc.tw','manager','一般管理員','{bcrypt}$2a$10$BcKLKCNcduU4.mfF/NXE5elTMeExQd1jRjn0xC8QB2iDqE3GGXQZu','manager',1);
insert into user (id,email,english_name,name,password,username,floor_id) values(3,'ROOT@tgfc.tw','tt','tt','{bcrypt}$2a$10$BcKLKCNcduU4.mfF/NXE5elTMeExQd1jRjn0xC8QB2iDqE3GGXQZu','tt',1);

INSERT INTO user_role (user_id,role_id) VALUES (1, 1);
INSERT INTO user_role (user_id,role_id) VALUES (2, 2);
INSERT INTO user_role (user_id,role_id) VALUES (1, 2);

INSERT INTO tag (id,name) VALUES (3, '早餐');
INSERT INTO tag (id,name) VALUES (4, '午餐');
INSERT INTO tag (id,name) VALUES (1, '午後甜點');
INSERT INTO tag (id,name) VALUES (2, '下午茶');

INSERT INTO store (id, remark, address, name, tel) VALUES (166, '我吃光光!!!', '高屏地區', '我誰!!!我修改店家!!!!', '香港3345678');
INSERT INTO store (id, remark, address, name, tel) VALUES (167, 'asdfs', 'asdfa', '帕莎蒂娜', '123123');
INSERT INTO store (id, remark, address, name, tel) VALUES (168, 'asdfs', 'asdfa', '帕莎蒂娜', '123123');

INSERT INTO store_tag (store_id,tag_id) VALUES (166, 1);
INSERT INTO store_tag (store_id,tag_id) VALUES (166, 2);
INSERT INTO store_tag (store_id,tag_id) VALUES (166, 3);
INSERT INTO store_tag(store_id,tag_id) VALUES (168, 3);
INSERT INTO store_tag(store_id,tag_id) VALUES (167, 3);
INSERT INTO store_tag(store_id,tag_id) VALUES (167, 1);

INSERT INTO store_picture (id, name, url, store_id) VALUES (11, '1574650143166.jpg', '/api/images/1574650143166.jpg', 166);
INSERT INTO store_picture (id, name, url, store_id) VALUES (12, '1574650146064.jpg', '/api/images/1574650146064.jpg', 166);
INSERT INTO store_picture (id, name, url, store_id) VALUES (13, '1574650146064.jpg', '/api/images/1574650146064.jpg', 167);

INSERT INTO store_comment (id, message, store_id, user_id) VALUES (1, '1111111', 166, 2);
INSERT INTO store_comment (id, message, store_id, user_id) VALUES (2, '1111111', 167, 2);

INSERT INTO product (id, name, price, remark, store_id) VALUES (111, '原味沙拉1', 70, NULL, 166);
INSERT INTO product (id, name, price, remark, store_id) VALUES (112, '原味沙拉2', 60, NULL, 166);
INSERT INTO product (id, name, price, remark, store_id) VALUES (113, '原味沙拉3', 50, NULL, 166);
INSERT INTO product (id, name, price, remark, store_id) VALUES (116, 'DeleteOrderItemTest(Manager)', 500, NULL, 166);

INSERT INTO product (id, name, price, remark, store_id) VALUES (114, '原味沙拉3', 50, NULL, 167);
-- INSERT INTO product (id, name, price, remark, store_id) VALUES (115, '原味沙拉3', 50, NULL, 168);


INSERT INTO option_ (id, name, price, product_id) VALUES (1, '不加醬', 10, 111);
INSERT INTO option_ (id, name, price, product_id) VALUES (2, '', 10, 112);
INSERT INTO option_ (id, name, price, product_id) VALUES (3, '', 10, 114);
INSERT INTO option_ (id, name, price, product_id) VALUES (4, '無', 0, 116);

INSERT INTO groups(id, end_time, is_deleted, is_locked, name, start_time) VALUES (100, '2030-01-29 13:10:20.000000', false, false, '皮卡丘嗨起來', '2020-01-09 10:35:22.000000');
INSERT INTO group_floor(group_id, floor_id) VALUES (100, 1);
INSERT INTO group_store(group_id, store_id) VALUES (100, 166);
INSERT INTO group_user(group_id, user_id) VALUES (100, 1);
INSERT INTO group_user (group_id, user_id) VALUES (100, 2);

INSERT INTO order_ (id, `date`, group_id, option_id, store_id) VALUES (51, '2019-10-18 10:47:54.000000', 100, 1, 166);
INSERT INTO order_ (id, `date`, group_id, option_id, store_id) VALUES (52, '2019-10-18 10:47:54.000000', 100, 2, 166);
INSERT INTO order_ (id, `date`, group_id, option_id, store_id) VALUES (53, '2019-10-18 10:47:55.000000', 100, 4, 166);

INSERT INTO order_user (order_id,user_id) VALUES (51, 3);
INSERT INTO order_user (order_id,user_id) VALUES (53, 2);
INSERT INTO order_user (order_id,user_id) VALUES (53, 2);