CREATE DATABASE springapp;

GRANT ALL ON springapp.* TO springappuser@'%' IDENTIFIED BY 'pspringappuser';
GRANT ALL ON springapp.* TO springappuser@localhost IDENTIFIED BY 'pspringappuser';

USE springapp;

CREATE TABLE loan (
  id INTEGER PRIMARY KEY,
  money DOUBLE,
  interest DOUBLE,
  numFees INTEGER
);
CREATE INDEX loan_money ON loan(money);