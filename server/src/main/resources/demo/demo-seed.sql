-- demo seed (embedded Postgres, 매 부팅 fresh)
-- board/faq/qna: create_table에 없어서 추가. demo 계정: demo / demo1234

create table if not exists board (
  board_no serial primary key,
  title    varchar(200) not null,
  content  text not null,
  id       varchar(50) references member(id),
  reg_date timestamp default now(),
  view_cnt int default 0
);

create table if not exists faq (
  faq_no      serial primary key,
  faq_title   varchar(200) not null,
  faq_content text not null,
  faq_date    timestamp default now(),
  id          varchar(50),
  faq_del     int default 0,
  faq_read    int default 0
);

create table if not exists qna (
  seq       serial primary key,
  id        varchar(50),
  ref       int default 0,
  step      int default 0,
  depth     int default 0,
  title     varchar(200) not null,
  content   text not null,
  wdate     timestamp default now(),
  del       int default 0,
  readcount int default 0
);

-- demo member (pw = demo1234, BCrypt)
insert into member (id, pw, name, discnt_time, phone, email, auth, car_num, car_type) values
('demo', '$2b$10$0DnIcqJik1I1RfCdfulrzuKtJim14pF7zKl1eL8aKvk4eNsQwKTGO', '데모사용자', 0, '010-0000-0000', 'demo@example.com', 1, '12가 3456', 0)
on conflict (id) do nothing;

-- sample cars (demo 회원 주차 중 2대)
insert into car (id, car_stat, spc_no, cost) values
('demo', 1, 'A1', 0),
('demo', 1, 'B5', 0);

-- sample posts
insert into board (title, content, id) values
('데모 공지사항입니다', '체험용 데모 데이터입니다. 글쓰기·댓글을 자유롭게 테스트하세요.', 'demo'),
('주차장 이용 안내', 'B1~B3, 총 90면. 전기차·장애인 구역이 있습니다.', 'demo');

insert into faq (faq_title, faq_content, id) values
('데모 계정은 무엇인가요?', 'demo / demo1234 로 로그인하면 전 기능을 체험할 수 있습니다.', 'demo'),
('작성한 글은 어떻게 되나요?', '체험용 데모라서 서버 재시작 시 초기화됩니다.', 'demo');

insert into qna (id, ref, step, depth, title, content) values
('demo', 1, 0, 0, '데모 질문입니다', 'QnA 작성 테스트용 글입니다.');
