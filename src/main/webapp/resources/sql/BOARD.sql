drop table board;

create table board(
board_num number, --�۹�ȣ
board_name varchar2(30), --�ۼ���
board_pass varchar2(30), --��й�ȣ
board_subject varchar2(300), --����
board_content varchar2(4000), --����
board_file varchar2(50), --÷�ε� ���ϸ�
board_original varchar2(50), --÷�ε� ���ϸ�(����), �������� �ٿ��� DB�����
board_re_ref number, --�亯 �� �ۼ��� �����Ǵ� ���� ��ȣ
board_re_lev number, --�亯 ���� ����
board_re_seq number, --�亯 ���� ����
board_readcount number, --���� ��ȸ��
board_date date, --���� �ۼ� ��¥
primary key(board_num)
)
select * from BOARD
alter table board
rename column board_data to board_date;

delete from board where board_name='admin';
delete from BOARD where BOARD_NAME='admin';

--���̺� ��ü����
truncate table board
truncate table board_copys

--board ���̺� ���纻 �����
create table board_copys
as
select * from board;

--���� ���̺��� ������ ����
insert into board_copys
select * from board

select * from board_copys


--������ ���� ���̺� ����
create table delete_File(
	BOARD_FILE VARCHAR2(50) primary key
)
select * from delete_File



