package com.budgetplanner.gui;

import com.budgetplanner.model.Category;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Custom JPanel to display budget allocation as a pie chart
 */
public class PieChartPanel extends JPanel {
    private List<Category> categories;
    private double totalBudget;
    private Color[] colors = {
        new Color(255, 99, 132),
        new Color(54, 162, 235),
        new Color(255, 206, 86),
        new Color(75, 192, 192),
        new Color(153, 102, 255),
        new Color(255, 159, 64),
        new Color(199, 199, 199),
        new Color(83, 102, 255),
        new Color(255, 99, 255),
        new Color(99, 255, 132)
    };

    public PieChartPanel() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }

    public void setData(List<Category> categories, double totalBudget) {
        this.categories = categories;
        this.totalBudget = totalBudget;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (categories == null || categories.isEmpty() || totalBudget == 0) {
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            String message = "No budget allocated yet";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g2d.drawString(message, x, y);
            return;
        }

        int diameter = Math.min(getWidth(), getHeight()) - 100;
        int x = (getWidth() - diameter) / 2;
        int y = 50;

        double startAngle = 0;

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            double percentage = (category.getAmount() / totalBudget) * 100;
            double arcAngle = (category.getAmount() / totalBudget) * 360;

            g2d.setColor(colors[i % colors.length]);
            g2d.fillArc(x, y, diameter, diameter, (int) startAngle, (int) arcAngle);

            startAngle += arcAngle;
        }

        // Draw legend
        int legendX = 20;
        int legendY = y + diameter + 30;
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            double percentage = (category.getAmount() / totalBudget) * 100;

            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect(legendX, legendY, 15, 15);

            g2d.setColor(Color.BLACK);
            String label = String.format("%s: ₹%.2f (%.1f%%)", 
                category.getName(), category.getAmount(), percentage);
            g2d.drawString(label, legendX + 20, legendY + 12);

            legendY += 20;
        }
    }
}