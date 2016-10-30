/* drop all tables and functions */
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

/* Users table */
CREATE TABLE IF NOT EXISTS users (
  user_id         BIGSERIAL    NOT NULL,
  email           VARCHAR(255) NOT NULL,
  name            VARCHAR(255) NOT NULL,
  passhash        VARCHAR(255) NOT NULL,
  school          VARCHAR(255) NOT NULL,
  class_in_school VARCHAR(255) NOT NULL,
  CONSTRAINT PK_users PRIMARY KEY (user_id),
  CONSTRAINT UQ_users_email UNIQUE (email)
);

/* Contests table */
CREATE TABLE IF NOT EXISTS contests (
  contest_id   VARCHAR(255) NOT NULL,
  contest_name VARCHAR(255) NOT NULL,
  start_time   TIMESTAMP    NOT NULL,
  end_time     TIMESTAMP    NOT NULL,
  CONSTRAINT PK_contests PRIMARY KEY (contest_id)
);

/* Groups */
CREATE TABLE IF NOT EXISTS groups (
  group_id   BIGSERIAL    NOT NULL,
  group_name VARCHAR(255) NOT NULL,
  secret     VARCHAR(255) NOT NULL,
  contest_id VARCHAR(255) NOT NULL,
  CONSTRAINT PK_groups PRIMARY KEY (group_id),
  CONSTRAINT FK_groups_contest_id FOREIGN KEY (contest_id) REFERENCES contests (contest_id)
);

/* Group membership */
CREATE TABLE IF NOT EXISTS group_membership (
  group_id BIGINT NOT NULL,
  user_id  BIGINT NOT NULL,
  CONSTRAINT UQ_group_membership UNIQUE (group_id, user_id),
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

/* Password recovery */
CREATE TABLE IF NOT EXISTS password_recovery (
  email VARCHAR(255) NOT NULL,
  token VARCHAR(255) NOT NULL,
  CONSTRAINT FK_password_recovery_email FOREIGN KEY (email) REFERENCES users (email),
  CONSTRAINT UQ_password_recovery_token UNIQUE (token)
);

/* Email queue */
CREATE TABLE IF NOT EXISTS email_queue (
  email_id BIGSERIAL     NOT NULL,
  to_email     VARCHAR(255)  NOT NULL,
  subject  VARCHAR(255)  NOT NULL,
  content  VARCHAR(4000) NOT NULL
);