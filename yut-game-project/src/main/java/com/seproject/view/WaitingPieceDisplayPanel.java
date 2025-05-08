package com.seproject.view;

import javax.swing.*;
import java.awt.*;

public class WaitingPieceDisplayPanel extends JPanel {
    private int playerId;
    private int waitingPiecesCount;
    private static final Color[] PLAYER_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE
    };

    public WaitingPieceDisplayPanel(int playerId) {
        this.playerId = playerId;
        this.waitingPiecesCount = 0;
        setPreferredSize(new Dimension(100, 25));
        setBackground(Color.LIGHT_GRAY);
    }

    public void updateWaitingPieces(int count) {
        if (this.waitingPiecesCount != count) {
            this.waitingPiecesCount = count;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (waitingPiecesCount > 0 && playerId >= 0 && playerId < PLAYER_COLORS.length) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(PLAYER_COLORS[playerId]);

            int pieceDiameter = 15; // 대기 중인 말의 지름
            int spacing = 4;        // 말 사이 간격
            int totalWidth = waitingPiecesCount * pieceDiameter + Math.max(0, waitingPiecesCount - 1) * spacing;
            int startX = (getWidth() - totalWidth) / 2;
            int startY = (getHeight() - pieceDiameter) / 2;

            for (int i = 0; i < waitingPiecesCount; i++) {
                g2d.fillOval(startX + i * (pieceDiameter + spacing), startY, pieceDiameter, pieceDiameter);
            }
        }
    }

    public int getWaitingPiecesCount(){
        return waitingPiecesCount;
    }
}