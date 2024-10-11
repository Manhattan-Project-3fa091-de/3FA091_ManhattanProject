package de.manhattanproject;

class Customer implements ICustomer {
    //Setter
    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }
    public void setLastName(String lastName) {
        this._lastName = lastName;
    }
    public void setBirthDate(LocalDate birthDate) {
        this._birthDate = birthDate;
    }
    public void setGender(Gender gender) {
        this._gender = gender;
    }
    //Getter
    public String getFirstName() {
        return this._firstName;
    }
    public String getLastName() {
        return this._lastName;
    }
    public LocalDate getBirthDate() {
        return this._birthDate;
    }
    public Gender getGender() {
        return this._gender;
    }
    //Attributes
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
}
