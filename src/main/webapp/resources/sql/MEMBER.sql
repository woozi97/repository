drop table member;
--1. index.jsp���� �����մϴ�.
--2. ������ ���� admin, ��� 1234�� ����ϴ�.
--3. ����� ������ 3�� ����ϴ�.

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
--�˻����� ������, ���ڿ��� ���� ����, ������ limit�� �°� �����;���

--�̸��� ���ϳ��λ���� ã�ƶ�
select * from member where name like '%���ϳ�%'
--���̰� 21���� ����� ã�ƶ�
select * from member where age like '%21%';
select * from member where id like '%jsp%;

select * from member where age like '%+{param:searchword}+%';