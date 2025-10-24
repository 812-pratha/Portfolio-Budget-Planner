package com.budgetplanner.main;

import com.budgetplanner.gui.PieChartPanel;
import com.budgetplanner.model.Category;
import com.budgetplanner.service.BudgetManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BudgetPlannerApp extends JFrame {
    private BudgetManager budgetManager;
    private PieChartPanel pieChartPanel;
    
    // GUI Components
    private JTextField totalBudgetField;
    private JTextField categoryNameField;
    private JTextField categoryAmountField;
    private JLabel totalAllocatedLabel;
    private JLabel remainingLabel;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    public BudgetPlannerApp() {
        budgetManager = new BudgetManager();
        initializeGUI();
    }
    private void initializeGUI() {
        setTitle("Portfolio Budget Planner");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create main panels
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(53, 73, 94));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Portfolio Budget Planner");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Budget Setup Panel
        JPanel budgetSetupPanel = createBudgetSetupPanel();
        
        // Category Management Panel
        JPanel categoryPanel = createCategoryManagementPanel();
        
        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Add panels
        centerPanel.add(budgetSetupPanel, BorderLayout.NORTH);
        centerPanel.add(categoryPanel, BorderLayout.CENTER);
        centerPanel.add(tablePanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    private JPanel createBudgetSetupPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Budget Setup"));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Total Budget (₹):"));
        totalBudgetField = new JTextField(15);
        panel.add(totalBudgetField);

        JButton setBudgetBtn = new JButton("Set Budget");
        setBudgetBtn.setBackground(new Color(46, 204, 113));
        setBudgetBtn.setForeground(Color.WHITE);
        setBudgetBtn.setFocusPainted(false);
        setBudgetBtn.addActionListener(e -> setBudget());
        panel.add(setBudgetBtn);

        totalAllocatedLabel = new JLabel("Total Allocated: ₹0.00");
        totalAllocatedLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(totalAllocatedLabel);

        remainingLabel = new JLabel("Remaining: ₹0.00");
        remainingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        remainingLabel.setForeground(new Color(41, 128, 185));
        panel.add(remainingLabel);

        return panel;
    }

    private JPanel createCategoryManagementPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add/Update Category"));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Category Name:"));
        categoryNameField = new JTextField(15);
        panel.add(categoryNameField);

        panel.add(new JLabel("Amount (₹):"));
        categoryAmountField = new JTextField(10);
        panel.add(categoryAmountField);

        JButton addBtn = new JButton("Add Category");
        addBtn.setBackground(new Color(52, 152, 219));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> addCategory());
        panel.add(addBtn);

        JButton updateBtn = new JButton("Update Selected");
        updateBtn.setBackground(new Color(230, 126, 34));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.addActionListener(e -> updateCategory());
        panel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteCategory());
        panel.add(deleteBtn);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Budget Categories"));
        panel.setBackground(Color.WHITE);

        String[] columns = {"Category Name", "Amount (₹)", "Percentage (%)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.setRowHeight(25);
        categoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Budget Visualization"));
        panel.setBackground(Color.WHITE);

        pieChartPanel = new PieChartPanel();
        panel.add(pieChartPanel, BorderLayout.CENTER);

        return panel;
    }

    private void setBudget() {
        try {
            double budget = Double.parseDouble(totalBudgetField.getText().trim());
            budgetManager.setTotalBudget(budget);
            updateDisplay();
            JOptionPane.showMessageDialog(this, 
                "Budget set successfully: ₹" + String.format("%.2f", budget),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for budget",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        try {
            String name = categoryNameField.getText().trim();
            double amount = Double.parseDouble(categoryAmountField.getText().trim());
            
            budgetManager.addCategory(name, amount);
            categoryNameField.setText("");
            categoryAmountField.setText("");
            updateDisplay();
            
            JOptionPane.showMessageDialog(this, 
                "Category added successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for amount",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a category to update",
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String name = categoryNameField.getText().trim();
            double amount = Double.parseDouble(categoryAmountField.getText().trim());
            
            budgetManager.updateCategory(selectedRow, name, amount);
            categoryNameField.setText("");
            categoryAmountField.setText("");
            updateDisplay();
            
            JOptionPane.showMessageDialog(this, 
                "Category updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for amount",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a category to delete",
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this category?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                budgetManager.deleteCategory(selectedRow);
                updateDisplay();
                JOptionPane.showMessageDialog(this, 
                    "Category deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDisplay() {
        // Update labels
        totalAllocatedLabel.setText(String.format("Total Allocated: ₹%.2f", 
            budgetManager.getTotalAllocated()));
        remainingLabel.setText(String.format("Remaining: ₹%.2f", 
            budgetManager.getRemainingBudget()));
        
        // Update remaining label color
        if (budgetManager.getRemainingBudget() < 0) {
            remainingLabel.setForeground(Color.RED);
        } else {
            remainingLabel.setForeground(new Color(41, 128, 185));
        }

        // Update table
        tableModel.setRowCount(0);
        for (Category category : budgetManager.getCategories()) {
            double percentage = budgetManager.getCategoryPercentage(category);
            tableModel.addRow(new Object[]{
                category.getName(),
                String.format("%.2f", category.getAmount()),
                String.format("%.2f", percentage)
            });
        }

        // Update pie chart
        pieChartPanel.setData(budgetManager.getCategories(), 
            budgetManager.getTotalBudget());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BudgetPlannerApp());
    }
}