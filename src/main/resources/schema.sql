CREATE TABLE IF NOT EXISTS categories (
  id BIGINT primary key
  name varchar(128) NOT NULL,
  left_category INT NOT NULL,
  right_category INT NOT NULL
)

insert into categories values (1, 'TV and Audio', 1, 2)