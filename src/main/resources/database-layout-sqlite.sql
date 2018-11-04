CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  password TEXT DEFAULT NULL,
  email TEXT NOT NULL UNIQUE,
  admin INTEGER DEFAULT 0 NOT NULL,
  token TEXT UNIQUE,
  reset_token TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS articles (
  id INTEGER PRIMARY KEY,
  article_name TEXT NOT NULL,
  image TEXT,
  last_change TIMESTAMP,
  description TEXT
);

CREATE TABLE IF NOT EXISTS location (
  id INTEGER PRIMARY KEY,
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

/*
INSERt INTO articles(article_name) VALUES ("moo");
INSERt INTO articles(article_name) VALUES ("moo2");
INSERt INTO articles(article_name) VALUES ("moo3");

INSERt INTO location(location_name) VALUES ("lccation");
INSERt INTO location(location_name) VALUES ("lccation2");
INSERt INTO location(location_name) VALUES ("lccation3");

INSERT INTO article_locations(location_id, article_id, quantity) VALUES (1, 1, 2);
INSERT INTO article_locations(location_id, article_id, quantity) VALUES (2, 1, 3);
INSERT INTO article_locations(location_id, article_id, quantity) VALUES (1, 2, 4);

SELECT location_id, article_id, location_name, quantity, article_name, image, last_change, description FROM articles INNER JOIN article_locations ON articles.id = article_locations.article_id INNER JOIN location ON location.id = article_locations.location_id
 */