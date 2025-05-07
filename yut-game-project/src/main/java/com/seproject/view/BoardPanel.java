package com.seproject.view;

import com.seproject.model.Board;
import com.seproject.model.PathNode;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private final Board board;

    public BoardPanel(Board board) {
        this.board = board;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) g;

        double scale = Math.min(width, height) / 800.0;
        int baseNodeRadius = (int)(25 * scale);
        int crossNodeRadius = (int)(35 * scale);

        for (PathNode node : board.getPathNodes()) {
            if (!node.isVisible()) continue;

            int x = (int) (node.x_ratio * width);
            int y = (int) (node.y_ratio * height);

            g2.setStroke(new BasicStroke(3));

            if(node.isCross()){
                g2.drawOval(x - crossNodeRadius, y - crossNodeRadius, crossNodeRadius * 2, crossNodeRadius * 2);
            }
            g2.drawOval(x - baseNodeRadius, y - baseNodeRadius, baseNodeRadius * 2, baseNodeRadius * 2);
        }
    }
}