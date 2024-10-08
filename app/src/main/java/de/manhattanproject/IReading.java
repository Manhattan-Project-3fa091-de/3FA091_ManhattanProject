package de.manhattanproject;

interface IReading {
    public void setComment(String comment);
    public void setCustomer(ICustomer customer);
    public void setDateOfReading(LoclaDate dateOfReading);
    public void setKindOfMeter(KindOfMeter kindOfMeter);
    public void setMeterCount(double meterCount);
    public void setMeterId(String meterId);
    public void setSubstitute(Boolean substitute);
    public String getComment();
    public ICustomer getCustomer();
    public LocalDate getDateOfReading();
    public KindOfMeter getKindOfMeter();
    public Double getMeterCount();
    public String getMeterId();
    public Boolean getSubstitude();
    public String printDateOfReading();
}