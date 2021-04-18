CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY
  , username VARCHAR(50)
  , password VARCHAR(50)
  , firstname VARCHAR(50)
  , lastname VARCHAR(50)
);

INSERT INTO users VALUES ('9b8f11c0-9f62-11eb-bbfc-97e59ba63655', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Admin', 'Full Admin');
INSERT INTO users VALUES ('9fd8fe6c-9f62-11eb-bd62-137d23d58974', 'user', 'ee11cbb19052e40b07aac0ca060c23ee', 'User', 'Normal User');

CREATE TABLE IF NOT EXISTS daily (
  msisdn BIGINT PRIMARY KEY
  , year INT
  , month INT
  , day INT
  , call BIGINT
  , text BIGINT
  , web BIGINT
);

CREATE OR REPLACE VIEW monthly AS
  SELECT
    msisdn
    , year
    , month
    , sum(call) AS call
    , sum(text) AS test
    , sum(web) AS web
  FROM daily
  GROUP BY msisdn, year, month;

CREATE OR REPLACE VIEW annually AS
  SELECT
    msisdn
    , year
    , sum(call) AS call
    , sum(text) AS test
    , sum(web) AS web
  FROM daily
  GROUP BY msisdn, year;

