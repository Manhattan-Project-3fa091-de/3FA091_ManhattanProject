package de.manhattanproject.model;

import java.time.LocalDate;
import java.util.UUID;

public class Reading implements IReading {
    //Setter
    @Override
    public void setId(UUID id) {
        this._id = id;
    }
    @Override
    public void setComment(String comment) {
        this._comment = comment;
	}
    @Override
    public void setCustomer(ICustomer customer) {
        this._customer = customer;
	}
    @Override
    public void setDateOfReading(LocalDate dateOfReading) {
        this._dateOfReading = dateOfReading;
	}
    @Override
    public void setKindOfMeter(KindOfMeter kindOfMeter) {
        this._kindOfMeter = kindOfMeter;
	}
    @Override
    public void setMeterCount(double meterCount) {
        this._meterCount = meterCount;
	}
    @Override
    public void setMeterId(String meterId) {
        this._meterId = meterId;
	}
    @Override
    public void setSubstitute(Boolean substitute) {
        this._substitute = substitute;
	}
    //Getter
    @Override
    public UUID getId() {
        return this._id;
    }
    @Override
    public String getComment() {
        return this._comment;
	}
    @Override
    public ICustomer getCustomer() {
        return this._customer;
	}
    @Override
    public LocalDate getDateOfReading() {
        return this._dateOfReading;
	}
    @Override
    public KindOfMeter getKindOfMeter() {
        return this._kindOfMeter;
	}
    @Override
    public Double getMeterCount() {
        return this._meterCount;
	}
    @Override
    public String getMeterId() {
        return this._meterId;
	}
    @Override
    public Boolean getSubstitude() {
        return this._substitute;
	}
    @Override
    public String printDateOfReading() {
        return this._dateOfReading.toString();
	}
    //Attributes
    private UUID _id;
    private String _comment;
    private ICustomer _customer;
    private LocalDate _dateOfReading;
    private KindOfMeter _kindOfMeter;
    private Double _meterCount;
    private String _meterId;
    private Boolean _substitute;
}
