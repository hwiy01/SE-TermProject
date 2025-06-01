package com.seproject;

import com.seproject.controller.GameManager;
import com.seproject.view.GameSetupUI;
import javafx.application.Application;
import javafx.stage.Stage;

//
// Java FX 진입점.
//
// 프로그램을 시작하면 Java FX Runtime이 먼저 뜨고,
// {@link GameSetupUI}가 자체 Stage를 만들어 UI를 표시합니다.
//
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // FX 스레드 안에서 GameManager & 첫 화면 생성
        GameManager gameManager = new GameManager();
        new GameSetupUI(gameManager);

        // 
        // GameSetupUI가 자체 Stage를 띄우므로
        // primaryStage에는 Scene을 설정하지 않고 그대로 둡니다.
        // (primaryStage.show()를 호출하지 않으면 빈 창이 뜨지 않습니다.)
        //
    }

    public static void main(String[] args) {
        launch(args);   // Java FX 런타임 시작
    }
}