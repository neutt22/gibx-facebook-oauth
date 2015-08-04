package com.guevent.gibx.jim;


public class F360Member {
	
	private String firstName, middleName, lastName, fullName, birthdate;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		fullName = firstName + " " + middleName + " " + lastName;
		return fullName;
	}

	public String getBirthdate() {
		return birthdate.substring(0, birthdate.length() - 5);
	}
	
	public String getBirthdateRaw(){
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
}
