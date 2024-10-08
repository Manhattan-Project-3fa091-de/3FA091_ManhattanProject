CREATE TABLE reading(
    id BINARY(16) PRIMARY KEY,
    comment VARCHAR(32),
    /*customer id INT,*/
    dateOfReading DATETIME,
    kindOfMeter INT,
    meterCount DOUBLE(4,2),
    meterId VARCHAR(32),
    substitute BOOLEAN

    /*
    FOREIGN KEY(id) REFERENCES(customer.id)
    FOREIGN KEY(kindOfMeter) REFERENCES(meter.id)
    */
);
