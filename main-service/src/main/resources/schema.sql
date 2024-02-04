-- categories
CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

-- users
CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  email VARCHAR(254) NOT NULL UNIQUE
);

-- location
CREATE TABLE  IF NOT EXISTS location (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

-- events
CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  title VARCHAR(120) NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  description VARCHAR(7000),
  participant_limit INTEGER NOT NULL,
  category_id BIGINT NOT NULL,
  initiator_id BIGINT NOT NULL,
  paid BOOLEAN NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  request_moderation BOOLEAN NOT NULL,
  state VARCHAR(50),
  views BIGINT NOT NULL,
  location_id BIGINT NOT NULL,
  confirmed_requests INTEGER NOT NULL,
  FOREIGN KEY (category_id) REFERENCES categories (id),
  FOREIGN KEY (initiator_id) REFERENCES users (id),
  FOREIGN KEY (location_id) REFERENCES location (id)
);


-- requests
CREATE TABLE IF NOT EXISTS event_requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT,
    requester_id  BIGINT,
    status VARCHAR(50),
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (requester_id) REFERENCES users (id)
);

-- compilations (many2many with events)

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(50),
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    FOREIGN KEY (event_id) REFERENCES events (id)
);