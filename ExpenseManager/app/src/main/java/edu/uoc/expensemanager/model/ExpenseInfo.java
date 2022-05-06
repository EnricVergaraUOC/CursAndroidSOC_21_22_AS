package edu.uoc.expensemanager.model;

public class ExpenseInfo {
    public String description;
    public String date;
    public int totalAmount;

    public ExpenseInfo(String description, String date, int amount){
        this.date = date;
        this.description = description;
        this.totalAmount = amount;
    }
}
