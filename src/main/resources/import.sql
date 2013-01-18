-- You can use this file to load seed data into the database using SQL statements
-- insert into Member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')
-- CREATE TABLE PRINCIPLES ( principal_id VARCHAR(64) primary key,password VARCHAR(64));
-- CREATE TABLE ROLES ( principal_id VARCHAR(64),user_role VARCHAR(64),role_group VARCHAR(64));

BEGIN
INSERT INTO PRINCIPLES VALUES('ralf.sigmund@gmail.com', 'K7gNU3sdo+OL0wNhqoVWhr3g6s1xYv72ol/pe/Unols=', 1);
-- Insert into PRINCIPLES values('santa@claus.no','secret');
-- Insert into PRINCIPLES values('maren.soetebier@googlemail.com','secret');

INSERT INTO ROLES VALUES ('ralf.sigmund@gmail.com', 'known', 'known');
-- Insert into ROLES values('santa@claus.no','known','known');
-- Insert into ROLES values('maren.soetebier@googlemail.com','known','known');
INSERT INTO MEMBER (ID, EMAIL, NAME, PASSWORD, PHONE_NUMBER) VALUES (1, 'ralf.sigmund@gmail.com', 'K7gNU3sdo+OL0wNhqoVWhr3g6s1xYv72ol/pe/Unols=', 'Ralf Sigmund', '123455667890');
COMMIT
