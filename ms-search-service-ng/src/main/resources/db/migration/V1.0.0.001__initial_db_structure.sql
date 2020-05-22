CREATE TABLE index_column (
  id     BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
  column_code        VARCHAR(255)   NOT NULL,
  column_name        VARCHAR(255)   NOT NULL,
  active boolean not null default 1,
  unique key `column_code` (`column_code`)
);

CREATE TABLE user_column (
  id     BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
  column_id     BIGINT(20),
  user_id     BIGINT(20),
  foreign key (column_id) references index_column(id) on delete cascade,
  unique key `id_user_relation` (`column_id`,`user_id`)
);

CREATE TABLE user_search (
  id     BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
  dto        VARCHAR(255)   NOT NULL,
  search_name        VARCHAR(15000)   NOT NULL,
  user_id     BIGINT(20)
);
