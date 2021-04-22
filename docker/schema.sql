CREATE TABLE IF NOT EXISTS daily (
  msisdn VARCHAR(11)
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
    , sum(text) AS text
    , sum(web) AS web
  FROM daily
  GROUP BY msisdn, year, month;

CREATE OR REPLACE VIEW annually AS
  SELECT
    msisdn
    , year
    , sum(call) AS call
    , sum(text) AS text
    , sum(web) AS web
  FROM daily
  GROUP BY msisdn, year;

