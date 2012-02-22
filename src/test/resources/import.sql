-- You can use this file to load seed data into the database using SQL statements
-- insert into Member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')
-- CREATE TABLE PRINCIPLES ( principal_id VARCHAR(64) primary key,password VARCHAR(64));
-- CREATE TABLE ROLES ( principal_id VARCHAR(64),user_role VARCHAR(64),role_group VARCHAR(64));


Insert into PRINCIPLES values('testuser','secret');
Insert into PRINCIPLES values('santa@claus.no','secret');
Insert into PRINCIPLES values('maren.soetebier@googlemail.com','secret');

Insert into ROLES values('testuser','known','known');
Insert into ROLES values('santa@claus.no','known','known');
Insert into ROLES values('maren.soetebier@googlemail.com','known','known');
