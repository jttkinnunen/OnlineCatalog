CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  password TEXT DEFAULT NULL,
  email TEXT NOT NULL UNIQUE,
  admin INTEGER DEFAULT 0 NOT NULL,
  token TEXT UNIQUE,
  reset_token TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS articles (
  id SERIAL PRIMARY KEY,
  article_name TEXT NOT NULL,
  image TEXT DEFAULT NULL,
  last_change TIMESTAMP,
  description TEXT
);

CREATE TABLE IF NOT EXISTS location (
  id SERIAL PRIMARY KEY,
  location_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS article_locations (
  location_id INTEGER NOT NULL,
  article_id INTEGER NOT NULL,
  quantity INTEGER DEFAULT 0 NOT NULL,
  FOREIGN KEY(article_id) REFERENCES articles(id),
  FOREIGN KEY(location_id) REFERENCES location(id),
  PRIMARY KEY (location_id, article_id)
);