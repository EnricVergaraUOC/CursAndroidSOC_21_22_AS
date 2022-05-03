package edu.uoc.expensemanager;

public class ExpenseInfo {
    public String description;
    public String date;
    public int totalAmount;

    ExpenseInfo(String description, String date, int amount){
        this.date = date;
        this.description = description;
        this.totalAmount = amount;
    }
}
