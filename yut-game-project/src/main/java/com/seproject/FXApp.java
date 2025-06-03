package com.seproject;

import com.seproject.controller.GameManager;
import com.seproject.view.GameSetupUIFX;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // FX 스레드 안에서 GameManager & 첫 화면 생성
        GameManager gameManager = new GameManager();
        new GameSetupUIFX(gameManager);
    }
}
