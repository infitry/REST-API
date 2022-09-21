-- 사용자 관련
insert into tb_authority(ROLE, DESCRIPTION, UPDATE_AT, CREATE_AT) values ('ROLE_USER', '사용자 기능을 수행', NOW(), NOW());
insert into tb_authority(ROLE, DESCRIPTION, UPDATE_AT, CREATE_AT) values ('ROLE_ADMIN', '어드민 기능을 수행', NOW(), NOW());
insert into tb_user(ID, PASSWORD, UPDATE_AT, CREATE_AT) values ('user', '$2a$10$YfUT/U7Qdcbkpdr2y6o5ZegUFHfw6liVFQjMYfX79KckHNfFlCFfa', NOW(), NOW());
insert into tb_user(ID, PASSWORD, UPDATE_AT, CREATE_AT) values ('manager', '$2a$10$YfUT/U7Qdcbkpdr2y6o5ZegUFHfw6liVFQjMYfX79KckHNfFlCFfa', NOW(), NOW());
insert into tb_user_authorities(TB_USER_USER_ID, AUTHORITIES_AUTHORITY_ID) values (1, 1);
insert into tb_user_authorities(TB_USER_USER_ID, AUTHORITIES_AUTHORITY_ID) values (2, 2);

-- 상품 관련
insert into tb_item(NAME, PRICE, STOCK_QUANTITY, UPDATE_AT, CREATE_AT) values ('상품1', 3000, 20, NOW(), NOW());
insert into tb_item(NAME, PRICE, STOCK_QUANTITY, UPDATE_AT, CREATE_AT) values ('상품2', 5000, 10, NOW(), NOW());