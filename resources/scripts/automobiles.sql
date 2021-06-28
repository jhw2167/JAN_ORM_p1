/* new sql Script to assist with ORM development for automobile schema */

--Create schema
DROP SCHEMA IF EXISTS automobiles CASCADE;
CREATE SCHEMA automobiles;


--Drop tables
DROP TABLE IF EXISTS cars CASCADE;
DROP TABLE IF EXISTS producer_country CASCADE;
DROP TABLE IF EXISTS brand CASCADE;

SELECT * FROM cars;