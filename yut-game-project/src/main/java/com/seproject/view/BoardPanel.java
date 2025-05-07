package com.seproject.view;

import com.seproject.model.Board;
import com.seproject.model.PathNode;
import com.seproject.model.Piece;
import com.seproject.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
    private static final Color[] PLAYER_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE
    };
    private final Board board;
    private final Piece[][] piece;
    public BoardPanel(Board board, Piece[][] pieces) {
        this.board = board;
        this.piece = pieces;
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
        int pieceRadius =  (int) (15 * scale); // 말의 반지름

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

        // 말 그리기
        for(Piece[] player : piece){
            for(Piece eachP : player){
                int x = (int) (board.getPathNodes()[eachP.getCurrentPathNodeId()].x_ratio * width - pieceRadius); //중심의 x좌표
                int y = (int) (board.getPathNodes()[eachP.getCurrentPathNodeId()].y_ratio * height - pieceRadius); //중심의 y좌표

                g2.setColor(PLAYER_COLORS[eachP.getPlayerId()]); //말의 색 플레이어별로 다름
                g2.fillOval(x, y, pieceRadius * 2, pieceRadius * 2); //말 그리기

                if(eachP.selected){ //만약 움직이기 위해 선택된 말이라면 테두리를 두껍게 함
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawOval(x, y, pieceRadius * 2, pieceRadius * 2);
                }
            }
        }
    }

    // 클릭 이벤트에서 말을 클릭했는지 확인하기 위한 함수
    boolean isContain(int px, int py, Piece p){
        int width = getWidth();
        int height = getHeight();
        double scale = Math.min(width, height) / 800.0;
        int pieceRadius =  (int) (15 * scale);
        int x = (int) (board.getPathNodes()[p.getCurrentPathNodeId()].x_ratio * width);
        int y = (int) (board.getPathNodes()[p.getCurrentPathNodeId()].y_ratio * height);

        return Math.pow(px - x, 2) + Math.pow(py - y, 2) <= pieceRadius * pieceRadius; // 거리가 반지름보다 작으면 true
    }
}