CREATE TABLE customer(
    id UUID PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(32),
    lastName VARCHAR(32),
    birthDate DATETIME,
    gender INT
);
