package com.budgetplanner.model;

/**
 * Represents a budget category with name and allocated amount
 */
public class Category {
    private String name;
    private double amount;

    public Category(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return name + ": ₹" + String.format("%.2f", amount);
    }
}