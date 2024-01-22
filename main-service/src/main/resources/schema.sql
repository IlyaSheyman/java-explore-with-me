-- categories

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_categories PRIMARY KEY (id)
);

-- users

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(300) NOT NULL,
  email VARCHAR NOT NULL UNIQUE,
  CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE "users" ADD CONSTRAINT unique_email UNIQUE (email);

-- events

CREATE TABLE event (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   title VARCHAR(120) NOT NULL,
   annotation VARCHAR(1500) NOT NULL,
   description VARCHAR(6000),
   participant_limit INTEGER NOT NULL,
   category_id BIGINT NOT NULL,
   initiator_id BIGINT NOT NULL,
   paid BOOLEAN NOT NULL,
   created_on TIMESTAMP WITHOUT TIME ZONE,
   published_on TIMESTAMP WITHOUT TIME ZONE,
   event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   request_moderation BOOLEAN NOT NULL,
   state INTEGER,
   views BIGINT NOT NULL,
   CONSTRAINT pk_events PRIMARY KEY (id)
);

ALTER TABLE events ADD CONSTRAINT EVENT_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE events ADD CONSTRAINT EVENT_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id);

-- requests

CREATE TABLE event_requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT,
    organizer_id BIGINT,
    visitor_id BIGINT,
    request BOOLEAN NOT NULL,
    CONSTRAINT pk_eventrequest PRIMARY KEY (id)
);

ALTER TABLE event_requests ADD CONSTRAINT EVENTREQUEST_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES users (id);

ALTER TABLE event_requests ADD CONSTRAINT EVENTREQUEST_ON_VISITOR FOREIGN KEY (visitor_id) REFERENCES users (id);

ALTER TABLE event_requests ADD CONSTRAINT EVENTREQUEST_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);