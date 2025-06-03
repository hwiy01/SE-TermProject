package com.seproject.view;

import com.seproject.controller.GameManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//
// Java FX 버전의 게임 설정 화면.
//
//  ※ 기존 Swing 코드에서 했던 일을 그대로 수행하지만,
//  {@link javax.swing.JFrame}/{@link javax.swing.JDialog} 대신
//  {@link Stage}·{@link Scene}·Java FX 컨트롤을 사용합니다.
//
// 이 클래스는 <strong>FX Application Thread</strong> 안에서 인스턴스화되어야 합니다.
// App.java(시작점)에서 {@code Application.launch()} 후
// {@code new GameSetupUI(gameManager);} 식으로 호출해 주세요.
//
public class GameSetupUIFX {

    // --------- fields --------- //

    private final GameManager gameManager;
    private final Stage primaryStage;

    // --------- constructor --------- //

    public GameSetupUIFX(GameManager gameManager) {
        this.gameManager = gameManager;

        //
        // 만약 FX-스레드가 아직 뜨지 않은 상황에서 호출됐다면
        // Platform.startup() 이 두 번 이상 호출될 수 없으므로
        // 예외를 방지하기 위해 runLater() 로 넘겨 놓는다.
        //
        if (Platform.isFxApplicationThread()) {
            this.primaryStage = buildAndShow();
        } else {
            final Stage[] holder = new Stage[1];
            Platform.runLater(() -> holder[0] = buildAndShow());
            this.primaryStage = holder[0];  // 나중에 필요할 수도 있으므로 필드에 보관
        }
    }

    // --------- UI 빌드 --------- //

    private Stage buildAndShow() {

        // ---- 최상위 레이아웃 ---- //
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // ---- 제목 ---- //
        Label title = new Label("Yut-Nol-I setting");
        title.setFont(Font.font("Malgun Gothic", FontWeight.BOLD, 20));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // ---- 가운데: 설정 섹션 ---- //
        VBox settingBox = new VBox(15);
        settingBox.setAlignment(Pos.CENTER);
        settingBox.setPadding(new Insets(10));

        // 플레이어 수 & 말 수 //
        HBox countRow = new HBox(30);
        countRow.setAlignment(Pos.CENTER);

        HBox numPlayerBox = new HBox(5);
        numPlayerBox.setAlignment(Pos.CENTER);
        numPlayerBox.getChildren().add(new Label("The Number of players"));
        ComboBox<Integer> playerCombo = new ComboBox<>();
        playerCombo.getItems().addAll(2, 3, 4);
        playerCombo.getSelectionModel().selectFirst();
        numPlayerBox.getChildren().add(playerCombo);

        HBox numPieceBox = new HBox(5);
        numPieceBox.setAlignment(Pos.CENTER);
        numPieceBox.getChildren().add(new Label("The Number of pieces"));
        ComboBox<Integer> pieceCombo = new ComboBox<>();
        pieceCombo.getItems().addAll(2, 3, 4, 5);
        pieceCombo.getSelectionModel().selectFirst();
        numPieceBox.getChildren().add(pieceCombo);

        countRow.getChildren().addAll(numPlayerBox, numPieceBox);
        settingBox.getChildren().add(countRow);

        // 보드 모양 선택 //
        VBox shapeBox = new VBox(5);
        shapeBox.setAlignment(Pos.CENTER);

        shapeBox.getChildren().add(new Label("Shape of Board"));

        HBox shapeBtnRow = new HBox(10);
        shapeBtnRow.setAlignment(Pos.CENTER);

        ToggleGroup shapeGroup = new ToggleGroup();
        ToggleButton squareBtn   = new ToggleButton("SQUARE");
        ToggleButton pentagonBtn = new ToggleButton("PENTAGON");
        ToggleButton hexagonBtn  = new ToggleButton("HEXAGON");
        squareBtn  .setToggleGroup(shapeGroup);
        pentagonBtn.setToggleGroup(shapeGroup);
        hexagonBtn .setToggleGroup(shapeGroup);
        squareBtn.setSelected(true);

        shapeBtnRow.getChildren().addAll(squareBtn, pentagonBtn, hexagonBtn);
        shapeBox.getChildren().add(shapeBtnRow);

        settingBox.getChildren().add(shapeBox);
        root.setCenter(settingBox);

        // ---- 하단: NEXT 버튼 ---- //
        Button nextBtn = new Button("NEXT");
        nextBtn.setMaxWidth(Double.MAX_VALUE);
        BorderPane.setAlignment(nextBtn, Pos.CENTER);
        nextBtn.setOnAction(e -> onNext(
                playerCombo.getValue(),
                pieceCombo.getValue(),
                squareBtn.isSelected(),
                pentagonBtn.isSelected()));
        root.setBottom(nextBtn);
        BorderPane.setMargin(nextBtn, new Insets(10, 0, 0, 0));

        // ---- Stage 세팅 ---- //
        Scene scene = new Scene(root, 500, 300);
        Stage stage = new Stage();
        stage.setTitle("Yut-Nol-I setting");
        stage.setScene(scene);
        stage.show();

        return stage;
    }

    // --------- NEXT 버튼 동작 --------- //

    private void onNext(int numPlayers,
                        int numPieces,
                        boolean squareSelected,
                        boolean pentagonSelected) {

        // 컨트롤러에 값 전달 //
        gameManager.setPlayer(numPlayers);
        gameManager.setPiece(numPieces);
        if (squareSelected)       gameManager.setBoard(4);
        else if (pentagonSelected) gameManager.setBoard(5);
        else                       gameManager.setBoard(6);

        // 이름 입력 다이얼로그 //
        String[] names = showNicknameInput(numPlayers);
        for (String name : names) {
            if (name == null) {
                // 사용자가 취소했거나 입력 미완료
                return;
            }
        }
        gameManager.setPlayerName(names);

        // 다음 단계로 진행 //
        gameManager.playGame();

        // 이 설정 창 닫기 //
        primaryStage.close();
    }

    // --------- 닉네임 입력 다이얼로그 --------- //

    public String[] showNicknameInput(int numberOfPlayer) {
        String[] names = new String[numberOfPlayer];
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("input name");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // 입력 필드 //
        VBox nameBox = new VBox(5);
        nameBox.setPadding(new Insets(10));
        ArrayList<TextField> fields = new ArrayList<>();

        for (int i = 0; i < numberOfPlayer; i++) {
            HBox row = new HBox(5);
            row.setAlignment(Pos.CENTER_LEFT);
            Label lbl = new Label("Name of Player " + (i + 1) + ":");
            TextField tf = new TextField();
            fields.add(tf);
            row.getChildren().addAll(lbl, tf);
            nameBox.getChildren().add(row);
        }
        root.setCenter(nameBox);

        // START 버튼 //
        Button startBtn = new Button("START");
        startBtn.setMaxWidth(Double.MAX_VALUE);
        startBtn.setOnAction(e -> {
            // 검증
            Set<String> used = new HashSet<>();
            for (int i = 0; i < fields.size(); i++) {
                String text = fields.get(i).getText().trim();
                if (text.isEmpty()) {
                    showAlert("Input all names");
                    return;
                }
                if (!used.add(text)) {
                    showAlert("duplication: \"" + text + "\"");
                    return;
                }
                names[i] = text;
            }
            dialog.close();
        });
        root.setBottom(startBtn);
        BorderPane.setMargin(startBtn, new Insets(10, 0, 0, 0));

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();

        return names;
    }

    // --------- 유틸: 경고 --------- //

    private void showAlert(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }
}
