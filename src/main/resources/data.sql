MERGE INTO USERS(name, email)
KEY(name, email)
SELECT * FROM CSVREAD('classpath:users.csv');
