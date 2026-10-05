-- MedicalTower parking: Supabase Postgres DDL + seed
-- Dashboard > SQL Editor > New query, block by block Run.
-- 1) tables -> 2) parking/space seed -> 3) signup 'demo' in UI -> 4) demo cars

-- ============ 1. member ============
create table if not exists public.member (
  id       varchar(50)  primary key,
  pw       varchar(255) not null,
  name     varchar(50)  not null,
  discnt_time int,
  phone    varchar(20)  not null,
  email    varchar(100),
  auth     int          not null,
  car_num  varchar(20)  not null,
  car_type int          not null
);

-- ============ 2. parking (per-floor summary) ============
create table if not exists public.parking (
  floor      int primary key,
  total_spc  int not null,
  client_spc int not null,
  elec_spc   int not null,
  dis_spc    int not null
);

-- ============ 3. space (individual spots) ============
create table if not exists public.space (
  spc_no   varchar(20) primary key,
  floor    int not null references public.parking(floor),
  spc_type int not null,
  spc_stat int not null
);

-- ============ 4. car (in/out history) ============
create table if not exists public.car (
  car_id    serial primary key,
  ent_time  timestamp default now() not null,
  id        varchar(50) not null references public.member(id) on update cascade,
  car_stat  int not null,
  ex_time   timestamp,
  cost      int,
  spc_no    varchar(20) references public.space(spc_no),
  discnt_at timestamp,
  discnt_owner_id varchar(50)
);

-- ============ 5. board ============
create table if not exists public.board (
  board_no serial primary key,
  title    varchar(200) not null,
  content  text not null,
  id       varchar(50) references public.member(id),
  reg_date timestamp default now(),
  view_cnt int default 0
);

-- ============ 6. faq ============
create table if not exists public.faq (
  faq_no      serial primary key,
  faq_title   varchar(200) not null,
  faq_content text not null,
  faq_date    timestamp default now(),
  id          varchar(50),
  faq_del     int default 0,
  faq_read    int default 0
);

-- ============ 7. qna ============
create table if not exists public.qna (
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

-- ============ 2. seed: parking + space (3 floors x 30) ============
insert into public.parking (floor, total_spc, client_spc, elec_spc, dis_spc) values
(1, 30, 24, 4, 2),
(2, 30, 24, 4, 2),
(3, 30, 24, 4, 2)
on conflict (floor) do nothing;

insert into public.space (spc_no, floor, spc_type, spc_stat)
select
  case f.floor when 1 then 'A' || n when 2 then 'B' || n else 'C' || n end,
  f.floor,
  case when n <= 24 then 0 when n <= 28 then 1 else 2 end,
  0
from (values (1), (2), (3)) as f(floor)
cross join generate_series(1, 30) as n
on conflict (spc_no) do nothing;

-- ============ 4. demo cars (UI에서 'demo' 회원가입 먼저, 그 다음 실행) ============
-- insert into public.car (id, car_stat, spc_no, cost) values
-- ('demo', 1, 'A1', 0),
-- ('demo', 1, 'B5', 0);
