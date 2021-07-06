-- create database

CREATE DATABASE unit_techno;

-- \c unit_techno

CREATE SCHEMA device_registration_service;

CREATE USER device_registration_admin WITH password 'squd';

ALTER USER device_registration_admin WITH SUPERUSER;

GRANT USAGE ON SCHEMA device_registration_service TO device_registration_admin;

ALTER SCHEMA device_registration_service OWNER TO device_registration_admin;
