/* drop all tables and functions */
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

/* Users table */
CREATE TABLE IF NOT EXISTS users (
  user_id BIGSERIAL    NOT NULL,
  net_id  VARCHAR(255) NOT NULL,
  CONSTRAINT PK_users PRIMARY KEY (user_id),
  CONSTRAINT UQ_users_net_id UNIQUE (net_id)
);

/* Contests table */
CREATE TABLE IF NOT EXISTS contests (
  contest_id VARCHAR(255) NOT NULL,
  name       VARCHAR(255) NOT NULL,
  start_date TIMESTAMP    NOT NULL,
  duration   INT          NOT NULL,
  CONSTRAINT PK_contests PRIMARY KEY (contest_id)
);

/* Groups */
create table if not exists groups (
  group_id bigserial not null,
  name varchar(255) not null,
  secret varchar(255) not null,
  contest_id varchar(255) not null,
  constraint fk_groups_contest_id foreign key (contest_id) references contests (contest_id)
);

/* Group membership */
create table if not exists group_membership (
  group_id bigint not null,
  user_id bigint not null,
  constraint fk_group_membership_group_id foreign key (group_id) references groups (group_id),
  constraint fk_group_membership_user_id foreign key (user_id) references users (user_id)
);