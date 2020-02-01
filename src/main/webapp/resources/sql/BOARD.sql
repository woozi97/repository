drop table board;

create table board(
board_num number, --글번호
board_name varchar2(30), --작성자
board_pass varchar2(30), --비밀번호
board_subject varchar2(300), --제목
board_content varchar2(4000), --내용
board_file varchar2(50), --첨부될 파일명
board_original varchar2(50), --첨부될 파일명(가공), 랜덤으로 붙여서 DB저장명
board_re_ref number, --답변 글 작성시 참조되는 글의 번호
board_re_lev number, --답변 글의 깊이
board_re_seq number, --답변 글의 순서
board_readcount number, --글의 조회수
board_date date, --글의 작성 날짜
primary key(board_num)
)
select * from BOARD
alter table board
rename column board_data to board_date;

delete from board where board_name='admin';
delete from BOARD where BOARD_NAME='admin';

--테이블 전체삭제
truncate table board
truncate table board_copys

--board 테이블 복사본 만들기
create table board_copys
as
select * from board;

--복사 테이블에서 데이터 삽입
insert into board_copys
select * from board

select * from board_copys


--삭제할 파일 테이블 만듦
create table delete_File(
	BOARD_FILE VARCHAR2(50) primary key
)
select * from delete_File



