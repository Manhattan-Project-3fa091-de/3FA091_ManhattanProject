CREATE TABLE reading(
    id BINARY(16) PRIMARY KEY,
    comment VARCHAR(32),
    customer_id BINARY(16),
    dateOfReading DATE,
    kindOfMeter INT,
    meterCount DOUBLE(4,2),
    meterId VARCHAR(32),
    substitute BOOLEAN,

    FOREIGN KEY(customer_id) REFERENCES customer(id)
    /*FOREIGN KEY(kindOfMeter) REFERENCES(meter.id)*/
);
