package com.seproject.view;

import com.seproject.model.Board;
import com.seproject.model.PathNode;
import com.seproject.model.Piece;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BoardPanelFX extends Pane {

    // ---------- constants ---------- //

    private static final Color[] PLAYER_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE
    };

    // ---------- fields ---------- //

    private final Board     board;
    private final Piece[][] pieces;

    private final Canvas canvas = new Canvas();

    // ---------- constructor ---------- //

    public BoardPanelFX(Board board, Piece[][] pieces) {
        this.board  = board;
        this.pieces = pieces;
        getChildren().add(canvas);

        // 초기 크기 설정 (Scene 크기에 맞춰 자동 조정됨)
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        // 사이즈 바뀔 때마다 다시 그리기
        widthProperty() .addListener((obs, o, n) -> draw());
        heightProperty().addListener((obs, o, n) -> draw());
        draw();
    }

    // ---------- public helper ---------- //

    // 클릭 좌표가 말 내부인지 검사 (GamePlayUI에서 호출) //
    public boolean isContain(double px, double py, Piece p) {
        if (p.getCurrentPathNodeId() == -5 || p.getCurrentPathNodeId() == 100)
            return false;

        double w = getWidth();
        double h = getHeight();
        double scale       = Math.min(w, h) / 800.0;
        double pieceRadius = 15 * scale;

        PathNode node = board.getPathNodes()[p.getCurrentPathNodeId()];
        double cx = node.x_ratio * w;
        double cy = node.y_ratio * h;

        return Math.pow(px - cx, 2) + Math.pow(py - cy, 2) <= pieceRadius * pieceRadius;
    }

    // ---------- drawing ---------- //

    public void draw() {
        double w = getWidth();
        double h = getHeight();
        GraphicsContext g = canvas.getGraphicsContext2D();

        // 배경
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, w, h);

        double scale          = Math.min(w, h) / 800.0;
        double baseR          = 25 * scale;
        double crossR         = 35 * scale;
        double pieceR         = 15 * scale;

        g.setLineWidth(3 * scale);

        // --- 노드 그리기 --- //
        for (PathNode node : board.getPathNodes()) {
            if (!node.isVisible()) continue;

            double x = node.x_ratio * w;
            double y = node.y_ratio * h;

            if (node.isCross()) {
                g.strokeOval(x - crossR, y - crossR, crossR * 2, crossR * 2);
            }
            g.strokeOval(x - baseR, y - baseR, baseR * 2, baseR * 2);
        }

        // START 라벨 //
        g.setFill(Color.BLACK);
        g.setFont(Font.font("Arial", 13 * scale));
        double startX = board.getPathNodes()[0].x_ratio * w - 0.85 * baseR;
        double startY = board.getPathNodes()[0].y_ratio * h + 2 * baseR;
        g.fillText("START", startX, startY);

        // --- 말 그리기 --- //
        for (Piece[] playerPieces : pieces) {
            for (Piece p : playerPieces) {
                int nodeId = p.getCurrentPathNodeId();
                if (nodeId < 0 || nodeId >= board.getPathNodes().length) continue;

                PathNode node = board.getPathNodes()[nodeId];
                double cx = node.x_ratio * w;
                double cy = node.y_ratio * h;

                // 말
                g.setFill(PLAYER_COLORS[p.getPlayerId() % PLAYER_COLORS.length]);
                g.fillOval(cx - pieceR, cy - pieceR, pieceR * 2, pieceR * 2);

                // 업힌 말 개수 표시 //
                int grouped = p.getEachPieces().size();
                if (grouped > 1) {
                    String txt = "x" + grouped;
                    g.setFont(Font.font("Arial", Font.getDefault().getSize() * scale));
                    double tx = cx + pieceR / 3;
                    double ty = cy - pieceR / 2;

                    // 배경 검정 네모
                    g.setFill(Color.BLACK);
                    g.fillRoundRect(tx - 2, ty - 12, 22, 16, 5, 5);

                    // 흰 글씨
                    g.setFill(Color.WHITE);
                    g.fillText(txt, tx, ty);
                }

                // 선택된 말 강조 //
                if (p.selected) {
                    g.setStroke(Color.BLACK);
                    g.setLineWidth(3 * scale);
                    g.strokeOval(cx - pieceR, cy - pieceR, pieceR * 2, pieceR * 2);
                }
            }
        }
    }
}
