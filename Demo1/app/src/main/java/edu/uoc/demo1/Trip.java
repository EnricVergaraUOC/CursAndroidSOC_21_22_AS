package edu.uoc.demo1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Trip {
	@JsonProperty("image")
	String image;
	@JsonProperty("initDate")
	String initDate;
	@JsonProperty("description")
	String description;
	@JsonProperty("users")
	ArrayList<User> users;
	@JsonProperty("expenses")
	ArrayList<Expense> expenses;
	Trip() {}
	Trip(String image, String initDate, String description){
		this.image = image;
		this.initDate = initDate;
		this.description = description;
		users = new ArrayList<User>();
		expenses = new ArrayList<Expense>();
	}
	
	public ArrayList<String> GetUserNames(){
		ArrayList<String> userNames = new ArrayList<String>();
		for (User u: users) {
			userNames.add(u.getName());
		}
		return userNames;
	}
	public void AddNewUser (User user) {
		users.add(user);
	}
	
	public void AddNewExpense (Expense expense) {
		expenses.add(expense);
	}
	
	public int CalculateSummary () {
		int totalAmount = 0;
		for (User u: users) {
			u.ResetAmount();
		}
		
		for(Expense expense: expenses) {
			totalAmount += expense.getAmount();
			for(User user: users) {
				Integer amount = expense.getAmountSpentByUser(user.getName());
				if ( amount != null) {
					user.AddAmountSpent(amount);	
				}
			}
		}
		
		return totalAmount;
	}
	
	
	public String toString () {
		String info = "";
		info += "{";
		info += "\n - Description: " + this.description;
		info += "\n - Init Date: " + this.initDate;
		info += "\n - Users: ";
		for(User user: users) {
			info += "\n\t " + user.toString();
		}
		info += "\n - Expenses: ";
		for(Expense expense: expenses) {
			info += "\n\t " + expense.toString();
		}
		return info;
	}
}
