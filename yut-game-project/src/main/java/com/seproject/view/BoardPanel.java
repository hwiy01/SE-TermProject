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
        for(Piece[] playerPieces : piece){
            for(Piece eachP : playerPieces){
                int currentPathNodeId = eachP.getCurrentPathNodeId();
                if (currentPathNodeId >= 0 && currentPathNodeId < board.getPathNodes().length) {
                    PathNode currentNode = board.getPathNodes()[currentPathNodeId];

                    // 말의 중심 좌표 계산
                    int pieceCenterX = (int) (currentNode.x_ratio * width);
                    int pieceCenterY = (int) (currentNode.y_ratio * height);

                    // 그릴 말의 실제 위치 (좌상단 기준)
                    int drawX = pieceCenterX - pieceRadius;
                    int drawY = pieceCenterY - pieceRadius;
                    g2.setColor(PLAYER_COLORS[eachP.getPlayerId() % PLAYER_COLORS.length]);
                    g2.fillOval(drawX, drawY, pieceRadius * 2, pieceRadius * 2);

                    // 업힌 말 개수 표시
                    int groupedCount = eachP.getEachPieces().size();
                    if (groupedCount > 1) {
                        String countText = "x" + groupedCount;

                        Font currentFont = g2.getFont();
                        Font newFont = currentFont.deriveFont(Font.BOLD, currentFont.getSize() * 1.0F);
                        g2.setFont(newFont);

                        FontMetrics fm = g2.getFontMetrics();
                        int textWidth = fm.stringWidth(countText);
                        int textHeight = fm.getHeight(); // 전체 텍스트 높이 (ascent + descent + leading)
                        int textAscent = fm.getAscent(); // 베이스라인으로부터 텍스트 상단까지의 높이

                        // 텍스트 위치 계산 (drawString의 y좌표는 베이스라인 기준)
                        int textBaselineX = pieceCenterX + pieceRadius / 3;
                        int textBaselineY = pieceCenterY - pieceRadius / 2 + textAscent / 2 - (int) (3*scale);

                        // 배경 사각형을 위한 패딩
                        int padding = 1; // 2픽셀 패딩

                        // 배경 사각형 좌표 및 크기 계산
                        int bgX = textBaselineX - padding;
                        int bgY = textBaselineY - textAscent - padding; // 텍스트 상단에서 패딩만큼 위로
                        int bgWidth = textWidth + (2 * padding);
                        int bgHeight = textHeight + (2 * padding); // ascent + descent + leading 을 포함한 높이에 패딩

                        g2.setColor(Color.BLACK);
                        g2.fillRoundRect(bgX, bgY, bgWidth, bgHeight, 5, 5);


                        // 숫자 색상 설정 및 그리기
                        g2.setColor(Color.WHITE);
                        g2.drawString(countText, textBaselineX, textBaselineY);

                        g2.setFont(currentFont);
                    }

                    if(eachP.selected){ //만약 움직이기 위해 선택된 말이라면 테두리를 두껍게 함
                        g2.setColor(Color.BLACK);
                        g2.setStroke(new BasicStroke(3));
                        g2.drawOval(drawX, drawY, pieceRadius * 2, pieceRadius * 2);
                    }
                }
            }
        }
    }

    // 클릭 이벤트에서 말을 클릭했는지 확인하기 위한 함수
    boolean isContain(int px, int py, Piece p){
        if(p.getCurrentPathNodeId()==-5 || p.getCurrentPathNodeId()==100){
            return false;
        }
        int width = getWidth();
        int height = getHeight();
        double scale = Math.min(width, height) / 800.0;
        int pieceRadius =  (int) (15 * scale);
        int x = (int) (board.getPathNodes()[p.getCurrentPathNodeId()].x_ratio * width);
        int y = (int) (board.getPathNodes()[p.getCurrentPathNodeId()].y_ratio * height);

        return Math.pow(px - x, 2) + Math.pow(py - y, 2) <= pieceRadius * pieceRadius; // 거리가 반지름보다 작으면 true
    }
}