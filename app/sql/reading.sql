CREATE TABLE reading(
    id UUID,
    comment VARCHAR(32),
    /*customer id INT,*/
    dateOfReading DATETIME,
    kindOfMeter INT,
    meterCount FLOAT,
    meterId VARCHAR(32),
    substitute BOOLEAN,

    /*
    FOREIGN KEY(id) REFERENCES(customer.id)
    FOREIGN KEY(kindOfMeter) REFERENCES(meter.id)
    */
);
