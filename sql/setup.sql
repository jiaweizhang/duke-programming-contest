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
  start_time TIMESTAMP    NOT NULL,
  end_time   TIMESTAMP    NOT NULL,
  CONSTRAINT PK_contests PRIMARY KEY (contest_id)
);

/* Groups */
CREATE TABLE IF NOT EXISTS groups (
  group_id   BIGSERIAL    NOT NULL,
  name       VARCHAR(255) NOT NULL,
  secret     VARCHAR(255) NOT NULL,
  contest_id VARCHAR(255) NOT NULL,
  CONSTRAINT UQ_groups_name UNIQUE (name),
  CONSTRAINT PK_groups PRIMARY KEY (group_id),
  CONSTRAINT FK_groups_contest_id FOREIGN KEY (contest_id) REFERENCES contests (contest_id)
);

/* Group membership */
CREATE TABLE IF NOT EXISTS group_membership (
  group_id BIGINT NOT NULL,
  user_id  BIGINT NOT NULL,
  CONSTRAINT UQ_users UNIQUE (group_id, user_id),
  CONSTRAINT FK_group_membership_group_id FOREIGN KEY (group_id) REFERENCES groups (group_id),
  CONSTRAINT FK_group_membership_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);

/* Submissions */
CREATE TABLE IF NOT EXISTS submissions (
  group_id       BIGINT       NOT NULL,
  contest_id     VARCHAR(255) NOT NULL,
  problem_number INT          NOT NULL,
  is_correct     INT          NOT NULL,
  submit_time    TIMESTAMP DEFAULT current_timestamp,
  CONSTRAINT FK_submissions_group_id FOREIGN KEY (group_id) REFERENCES groups (group_id),
  CONSTRAINT FK_submissions_contest_id FOREIGN KEY (contest_id) REFERENCES contests (contest_id)
);