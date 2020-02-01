drop table member;
--1. index.jsp에서 시작합니다.
--2. 관리자 계정 admin, 비번 1234를 만듭니다.
--3. 사용자 계정을 3개 만듭니다.

create table member(
id varchar2(15),
password varchar2(10),
name varchar2(15),
age Number,
gender varchar2(5),
email varchar2(30),
PRIMARY KEY(id)
);

insert into member values('admin','1234','admin',30,'male','admin@gmail.com');
select * from member;

select * from member where id='admin';
--검색쿼리 만들어라, 문자열은 조작 가능, 페이지 limit에 맞게 가져와야함

--이름이 김하나인사람을 찾아라
select * from member where name like '%김하나%'
--나이가 21세인 사람을 찾아라
select * from member where age like '%21%';
select * from member where id like '%jsp%;

select * from member where age like '%+{param:searchword}+%';