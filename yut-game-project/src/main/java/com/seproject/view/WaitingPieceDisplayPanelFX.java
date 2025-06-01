package com.seproject.view;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

//
// 플레이어별 '대기 말'을 점으로 나타내는 Java FX 패널.
//
// GamePlayUI (FX)에서
// {@code updateWaitingPieces(waiting, totalPieces)} 로 갱신하며,
// 현재 대기 말 수를 getWaitingPiecesCount()로 조회할 수 있습니다.
//
public class WaitingPieceDisplayPanelFX extends StackPane {

    // ---------- constants ---------- //

    private static final Color[] PLAYER_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE
    };

    // ---------- fields ---------- //

    private final int   playerId;
    private       int   waitingPiecesCount = 0;
    private final HBox  holder             = new HBox(4); // 점들 가로로 배치

    // ---------- constructor ---------- //

    public WaitingPieceDisplayPanelFX(int playerId) {
        this.playerId = playerId;
        setPrefSize(100, 25);

        holder.setAlignment(Pos.CENTER);
        getChildren().add(holder);

        // 라이트 그레이 배경 비슷하게
        setBackground(new Background(
                new BackgroundFill(Color.rgb(230, 230, 230), CornerRadii.EMPTY, null)));
    }

    // ---------- public API ---------- //

    //
    // 대기 말 개수를 바꾸고 다시 그립니다.
    //
    // param count 대기 중인 말 수
    //
    public void updateWaitingPieces(int count) {
        updateWaitingPieces(count, -1);
    }

    //
    // GamePlayUI(FX)에서 전체 말 개수와 함께 호출할 때 사용.
    //
    // param count       대기 말 개수
    // param totalPieces 플레이어가 가진 전체 말 개수(사용하지 않아도 됨)
    //
    public void updateWaitingPieces(int count, int totalPieces) {
        if (this.waitingPiecesCount == count) return;
        this.waitingPiecesCount = count;
        redrawDots();
    }

    public int getWaitingPiecesCount() {
        return waitingPiecesCount;
    }

    // ---------- internal ---------- //

    private void redrawDots() {
        holder.getChildren().clear();

        if (waitingPiecesCount <= 0) return;

        Color color = PLAYER_COLORS[playerId % PLAYER_COLORS.length];

        for (int i = 0; i < waitingPiecesCount; i++) {
            Circle dot = new Circle(7, color); // 지름 ≈ 14px
            holder.getChildren().add(dot);
        }
    }
}