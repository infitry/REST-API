insert into tb_authority(ROLE, DESCRIPTION, UPDATE_AT, CREATE_AT) values ('ROLE_USER', '사용자 기능을 수행', NOW(), NOW());
insert into tb_authority(ROLE, DESCRIPTION, UPDATE_AT, CREATE_AT) values ('ROLE_ADMIN', '어드민 기능을 수행', NOW(), NOW());
insert into tb_user(ID, NAME, PASSWORD, UPDATE_AT, CREATE_AT) values ('member', '회원1', '$2a$10$YfUT/U7Qdcbkpdr2y6o5ZegUFHfw6liVFQjMYfX79KckHNfFlCFfa', NOW(), NOW());
insert into tb_user_authorities(TB_USER_USER_ID, AUTHORITIES_AUTHORITY_ID) values (1, 1);