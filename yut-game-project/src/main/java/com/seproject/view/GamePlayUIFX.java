package com.seproject.view;

import com.seproject.controller.GameManager;
import com.seproject.enums.DiceResult;
import com.seproject.model.Piece;
import com.seproject.model.Player;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Optional;

public class GamePlayUIFX {
    // -------------------------------------------------
    //               constants - 색상 팔레트
    // -------------------------------------------------
    private static final Color[] PLAYER_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE
    };

    // -------------------------------------------------
    //                       fields
    //  -------------------------------------------------
    private final GameManager gameManager;

    private Label instructionLbl;
    private Label turnLbl;
    private Label resultLbl;
    private Button rollRandomBtn;
    private Button rollSpecificBtn;

    private Label[]                    playerInfoLbls   = new Label[4];
    private WaitingPieceDisplayPanelFX[] waitingDisplays = new WaitingPieceDisplayPanelFX[4];
    private VBox[]                     playerSlots      = new VBox[4];

    private Stage stage;

    private BoardPanelFX boardPanel;   // 실제 보드를 그리는 FX 패널

    // -------------------------------------------------
    //                   constructor
    // -------------------------------------------------
    public GamePlayUIFX(GameManager gm) {
        this.gameManager = gm;

        if (Platform.isFxApplicationThread()) {
            buildAndShow();
        } else {
            Platform.runLater(this::buildAndShow);
        }
    }

    // -------------------------------------------------
    //                     UI 빌드
    // -------------------------------------------------
    private void buildAndShow() {

        // -------- top area -------- //
        HBox top = new HBox(30);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);

        instructionLbl = new Label("실행 방법 : 윷 던지기 → 자신의 말 클릭");
        instructionLbl.setFont(Font.font("Malgun Gothic", FontWeight.BOLD, 16));

        turnLbl = new Label();
        turnLbl.setFont(Font.font("Malgun Gothic", FontWeight.BOLD, 20));

        resultLbl = new Label("윷 결과: ");
        resultLbl.setFont(Font.font("Malgun Gothic", FontWeight.BOLD, 20));

        top.getChildren().addAll(instructionLbl, turnLbl, resultLbl);

        // -------- 보드 -------- //
        boardPanel = new BoardPanelFX(
                gameManager.getBoard(),
                gameManager.getGamePieces()
        );

        // 보드 클릭 → 말 선택 //
        boardPanel.setOnMouseClicked(me -> {
            if (me.getButton() != MouseButton.PRIMARY) return;
            if (gameManager.getDiceResult().isEmpty())   return;

            for (Piece p : gameManager.getGamePieces()[gameManager.getCurrentTurn()]) {
                if (boardPanel.isContain(me.getX(), me.getY(), p)) {
                    // 하나만 선택되도록 전부 해제 후 선택
                    for (Piece each : gameManager.getGamePieces()[gameManager.getCurrentTurn()]) {
                        each.selected = false;
                    }
                    p.selected = true;
                    boardPanel.draw();

                    DiceResult mv = selectMove();
                    if (mv == null) {
                        p.selected = false;
                        boardPanel.draw();
                        return;
                    }
                    gameManager.processMove(p.getCurrentPathNodeId(), mv.getMoveSteps());
                    afterMoveCommon();
                    break;
                }
            }
        });

        // -------- 왼쪽 -------- //
        VBox left = new VBox(10);
        left.setPadding(new Insets(10));
        left.setAlignment(Pos.CENTER);

        rollRandomBtn = new Button("랜덤 윷 던지기");
        rollRandomBtn.setOnAction(e -> onRollRandom());

        left.getChildren().addAll(
                buildPlayerSlot(0),
                new Region(), rollRandomBtn, new Region(),
                buildPlayerSlot(2)
        );
        VBox.setVgrow(left.getChildren().get(1), Priority.ALWAYS);
        VBox.setVgrow(left.getChildren().get(3), Priority.ALWAYS);

        // -------- 오른쪽 -------- //
        VBox right = new VBox(10);
        right.setPadding(new Insets(10));
        right.setAlignment(Pos.CENTER);

        rollSpecificBtn = new Button("지정 윷 던지기");
        rollSpecificBtn.setOnAction(e -> onRollSpecific());

        right.getChildren().addAll(
                buildPlayerSlot(1),
                new Region(), rollSpecificBtn, new Region(),
                buildPlayerSlot(3)
        );
        VBox.setVgrow(right.getChildren().get(1), Priority.ALWAYS);
        VBox.setVgrow(right.getChildren().get(3), Priority.ALWAYS);

        // -------- root & stage -------- //
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(boardPanel);
        root.setLeft(left);
        root.setRight(right);

        stage = new Stage();
        stage.setTitle("Yut-Nol-I Game");
        stage.setScene(new Scene(root, 1000, 800));
        stage.show();

        // 최초 UI 값 //
        updateTurn();
        updatePlayerWaitingInfo();
    }

    // =================================================
    //                 버튼 : 랜덤 윷
    // =================================================
    private void onRollRandom() {
        if (!gameManager.getChance()) return;

        gameManager.rollDice();
        showDiceResult(gameManager.getDiceResult());

        ArrayList<DiceResult> list = gameManager.getDiceResult();
        DiceResult last = list.get(list.size() - 1);
        yutResultImage(last.getMoveSteps());

        gameManager.noChance();
        if (last.getMoveSteps() == 4 || last.getMoveSteps() == 5) {
            gameManager.moreChance();
        }

        if (gameManager.onlyBackDo() && !gameManager.getChance() && !hasMovablePiece()) {
            infoPopup("빽도! 움직일 수 있는 말이 없어 다음 턴으로 넘어갑니다.");
            list.clear();
            gameManager.nextTurn();
            postTurnChange();
        }
    }

    // =================================================
    //                 버튼 : 지정 윷
    // =================================================
    private void onRollSpecific() {
        if (!gameManager.getChance()) return;

        DiceResult choice = selectDiceResult();
        if (choice == null) return;

        gameManager.addSelectDiceResult(choice);
        showDiceResult(gameManager.getDiceResult());

        yutResultImage(choice.getMoveSteps());

        gameManager.noChance();
        if (choice.getMoveSteps() == 4 || choice.getMoveSteps() == 5) {
            gameManager.moreChance();
        }

        if (gameManager.onlyBackDo() && !gameManager.getChance() && !hasMovablePiece()) {
            infoPopup("빽도만 있어 움직일 말이 없어 다음 턴으로 넘어갑니다.");
            gameManager.getDiceResult().clear();
            gameManager.nextTurn();
            postTurnChange();
        }
    }

    // =================================================
    //                    공통 로직
    // =================================================
    private VBox buildPlayerSlot(int idx) {
        playerSlots[idx] = new VBox(5);
        playerSlots[idx].setAlignment(Pos.CENTER);

        Label info = new Label();
        info.setFont(Font.font(14));
        playerInfoLbls[idx] = info;

        waitingDisplays[idx] = new WaitingPieceDisplayPanelFX(idx);
        waitingDisplays[idx].setOnMouseClicked(me -> {
            if (gameManager.getCurrentTurn() != idx) return;
            if (waitingDisplays[idx].getWaitingPiecesCount() == 0) return;
            if (gameManager.onlyBackDo()) return;

            for (Piece p : gameManager.getGamePieces()[idx]) {
                if (p.getCurrentPathNodeId() == -5) {
                    DiceResult mv = selectMove();
                    if (mv == null) return;
                    gameManager.processMove(p.getCurrentPathNodeId(), mv.getMoveSteps());
                    afterMoveCommon();
                    break;
                }
            }
        });

        playerSlots[idx].getChildren().addAll(info, waitingDisplays[idx]);
        return playerSlots[idx];
    }

    private boolean hasMovablePiece() {
        for (Piece p : gameManager.getGamePieces()[gameManager.getCurrentTurn()]) {
            if (p.getCurrentPathNodeId() != -5) return true;
        }
        return false;
    }

    private void afterMoveCommon() {
        showDiceResult(gameManager.getDiceResult());
        boardPanel.draw();
        if (gameManager.checkWinCondition()) {
            showWinner(gameManager.getCurrentPlayer());
            rollRandomBtn.setDisable(true);
            rollSpecificBtn.setDisable(true);
            showGameEndOptions();
        }
        if (!gameManager.getChance() && gameManager.getDiceResult().isEmpty()) {
            gameManager.nextTurn();
            postTurnChange();
        }
        updatePlayerWaitingInfo();
    }

    private void postTurnChange() {
        updateTurn();
        showDiceResult(gameManager.getDiceResult());
    }

    // ---------------- UI 업데이트 ---------------- //
    private void updateTurn() {
        turnLbl.setText("현재 턴: " + gameManager.getCurrentTurnName());
    }

    private void updatePlayerWaitingInfo() {
        int numPlayers  = gameManager.getNumberOfPlayers();
        int totalPieces = gameManager.getNumberOfPiecesPerPlayer();

        for (int i = 0; i < 4; i++) {
            if (i < numPlayers) {
                String name   = gameManager.getPlayerName(i);
                int waiting   = gameManager.getNumberOfWaitingPieces(i);
                int score     = gameManager.getPlayerScore(i);
                playerInfoLbls[i].setText(
                        String.format("이름: %s%n득점: %d", name, score));
                waitingDisplays[i].updateWaitingPieces(waiting, totalPieces);
                playerSlots[i].setVisible(true);
            } else {
                playerSlots[i].setVisible(false);
            }
        }
    }

    // ---------------- 다이얼로그 ---------------- //
    private DiceResult selectDiceResult() {
        ChoiceDialog<DiceResult> dlg = new ChoiceDialog<>(DiceResult.DO, DiceResult.values());
        dlg.setTitle("지정 윷 결과 선택");
        dlg.setHeaderText("윷 결과를 선택하세요");
        Optional<DiceResult> res = dlg.showAndWait();
        return res.orElse(null);
    }

    private DiceResult selectMove() {
        DiceResult[] arr = gameManager.getDiceResult().toArray(new DiceResult[0]);
        ChoiceDialog<DiceResult> dlg = new ChoiceDialog<>(arr[0], arr);
        dlg.setTitle("윷 결과 선택");
        dlg.setHeaderText("움직일 윷 결과를 선택하세요");
        Optional<DiceResult> res = dlg.showAndWait();
        res.ifPresent(gameManager::deleteUsedDiceResult);
        return res.orElse(null);
    }

    private void showDiceResult(ArrayList<DiceResult> list) {
        StringBuilder sb = new StringBuilder("윷 결과: ");
        list.forEach(r -> sb.append(r.name()).append(' '));
        resultLbl.setText(sb.toString());
    }

    private void showWinner(Player p) {
        Alert a = new Alert(Alert.AlertType.INFORMATION,
                p.getName() + " 님이 승리하였습니다!",
                ButtonType.OK);
        a.setTitle("게임 종료");
        a.showAndWait();
    }

    private void showGameEndOptions() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("게임 종료");
        a.setHeaderText("게임이 끝났습니다. 다시 시작하시겠습니까?");
        a.getButtonTypes().setAll(
                new ButtonType("새 게임", ButtonBar.ButtonData.YES),
                new ButtonType("종료",    ButtonBar.ButtonData.NO)
        );
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get().getButtonData() == ButtonBar.ButtonData.YES) {
            GameManager ngm = new GameManager();
            new GameSetupUI(ngm);
            stage.close();
        } else {
            Platform.exit();
        }
    }

    private void infoPopup(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.initOwner(stage);
        a.show();
        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(e -> a.hide());
        pt.play();
    }

    // ---------------- 윷 결과 이미지 ---------------- //
    private void yutResultImage(int result) {
        String path = switch (result) {
            case  1 -> "/img/DO_IMG.jpg";
            case  2 -> "/img/GAE_IMG.jpg";
            case  3 -> "/img/GEOL_IMG.jpg";
            case  4 -> "/img/YUT_IMG.jpg";
            case  5 -> "/img/MO_IMG.jpg";
            case -1 -> "/img/BACKDO_IMG.jpg";
            default -> null;
        };
        if (path == null) return;

        Image img = new Image(getClass().getResourceAsStream(path), 200, 200, true, true);
        ImageView iv = new ImageView(img);

        Stage pop = new Stage();
        pop.initOwner(stage);
        pop.initModality(Modality.NONE);
        pop.setAlwaysOnTop(true);
        pop.setScene(new Scene(new StackPane(iv)));
        pop.show();

        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        pt.setOnFinished(e -> pop.close());
        pt.play();
    }
}