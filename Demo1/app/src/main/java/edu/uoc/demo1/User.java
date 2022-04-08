package edu.uoc.demo1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	@JsonProperty("name")
	String name;
	@JsonProperty("email")
	String email;
	@JsonProperty("image")
	String image;
	@JsonProperty("amountSpent")
	int amountSpent;

	User() {}
	
	User (String name, String email, String image){
		this.name = name;
		this.email = email;
		this.image = image;
		this.amountSpent = 0;
	}
	public void ResetAmount(){
		amountSpent = 0;
	}
	
	public String getName () {
		return this.name;
	}
	public void AddAmountSpent (int amount) {
		this.amountSpent+= amount;
	}
	
	public String toString() {
		String info = "";
		info += "{ Name: " + this.name;
		info += ", Email: " + this.name;
		info += "}";
		return info;
	}
}
