package com.budgetplanner.service;

import com.budgetplanner.model.Category;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to manage budget operations
 */
public class BudgetManager {
    private double totalBudget;
    private List<Category> categories;

    public BudgetManager() {
        this.totalBudget = 0.0;
        this.categories = new ArrayList<>();
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) throws IllegalArgumentException {
        if (totalBudget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        this.totalBudget = totalBudget;
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public void addCategory(String name, double amount) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (getTotalAllocated() + amount > totalBudget) {
            throw new IllegalArgumentException("Total allocation exceeds budget");
        }
        categories.add(new Category(name, amount));
    }

    public void updateCategory(int index, String name, double amount) throws IllegalArgumentException {
        if (index < 0 || index >= categories.size()) {
            throw new IllegalArgumentException("Invalid category index");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        
        double currentAmount = categories.get(index).getAmount();
        double newTotal = getTotalAllocated() - currentAmount + amount;
        
        if (newTotal > totalBudget) {
            throw new IllegalArgumentException("Total allocation exceeds budget");
        }
        
        categories.get(index).setName(name);
        categories.get(index).setAmount(amount);
    }

    public void deleteCategory(int index) throws IllegalArgumentException {
        if (index < 0 || index >= categories.size()) {
            throw new IllegalArgumentException("Invalid category index");
        }
        categories.remove(index);
    }

    public double getTotalAllocated() {
        return categories.stream().mapToDouble(Category::getAmount).sum();
    }

    public double getRemainingBudget() {
        return totalBudget - getTotalAllocated();
    }

    public double getCategoryPercentage(Category category) {
        if (totalBudget == 0) return 0;
        return (category.getAmount() / totalBudget) * 100;
    }
}