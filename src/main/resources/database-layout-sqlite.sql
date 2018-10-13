CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  password TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE,
  admin INTEGER DEFAULT 0 NOT NULL,
  token TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS article (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  brand TEXT,
  quantity INTEGER DEFAULT 0 NOT NULL,
  lastchange DATE,
  shelf TEXT
);