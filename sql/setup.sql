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
CREATE TABLE IF NOT EXISTS groups (
  group_id   BIGSERIAL    NOT NULL,
  name       VARCHAR(255) NOT NULL,
  secret     VARCHAR(255) NOT NULL,
  contest_id VARCHAR(255) NOT NULL,
  CONSTRAINT fk_groups_contest_id FOREIGN KEY (contest_id) REFERENCES contests (contest_id)
);

/* Group membership */
CREATE TABLE IF NOT EXISTS group_membership (
  group_id BIGINT NOT NULL,
  user_id  BIGINT NOT NULL,
  CONSTRAINT fk_group_membership_group_id FOREIGN KEY (group_id) REFERENCES groups (group_id),
  CONSTRAINT fk_group_membership_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);