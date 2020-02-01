drop table comments;

create table comments(
num number primary key,
id varchar2(30) references member(id),
content varchar2(200),
reg_date date,
BOARD_RE_REF number references board(BOARD_NUM) on delete cascade
);

-- on delete cascade : ������ �����ϸ� �� �������� �����ϴ� ��۵� �����մϴ�.
create sequence com_seq;

select * from comments;

select* from member;
select * from board;