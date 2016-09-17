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