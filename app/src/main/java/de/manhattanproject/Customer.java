package de.manhattanproject;

import java.time.LocalDate;
import java.util.UUID;

public class Customer implements ICustomer {
    //Setter
    @Override
    public void setId(UUID id) {
        this._id = id;
    }
    @Override
    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }
    @Override
    public void setLastName(String lastName) {
        this._lastName = lastName;
    }
    @Override
    public void setBirthDate(LocalDate birthDate) {
        this._birthDate = birthDate;
    }
    @Override
    public void setGender(Gender gender) {
        this._gender = gender;
    }
    //Getter
    @Override
    public UUID getId() {
        return this._id;
    }
    @Override
    public String getFirstName() {
        return this._firstName;
    }
    @Override
    public String getLastName() {
        return this._lastName;
    }
    @Override
    public LocalDate getBirthDate() {
        return this._birthDate;
    }
    @Override
    public Gender getGender() {
        return this._gender;
    }
    //Attributes
    private UUID _id;
    private String _firstName;
    private String _lastName;
    private LocalDate _birthDate;
    private Gender _gender;
}
