CREATE ROLE delivery_app WITH LOGIN PASSWORD 'delivery_app_pw';
CREATE DATABASE delivery_platform OWNER delivery_app;
GRANT ALL PRIVILEGES ON DATABASE delivery_platform TO delivery_app;