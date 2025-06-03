package com.seproject;

import com.seproject.controller.GameManager;
import com.seproject.view.GameSetupUI;
import com.seproject.view.GameSetupUIFX;
import javafx.application.Application;
import javafx.stage.Stage;

public class App
{
    public static void main( String[] args )
    {

        String UI = "swing";

        if(UI.equals("swing")){
            javax.swing.SwingUtilities.invokeLater(() -> {
                GameManager gameManager = new GameManager();
                new GameSetupUI(gameManager); // UI 실행
            });
        } else if (UI.equals("FX")) {
            FXApp.launch(FXApp.class);
        }
        // GameManager 객체 생성
        // GameManager의 playGame 함수 호출, 모든 실행 여기서 관여
        
    }
}

/*public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // FX 스레드 안에서 GameManager & 첫 화면 생성
        GameManager gameManager = new GameManager();
        new GameSetupUIFX(gameManager);

        //
        // GameSetupUI가 자체 Stage를 띄우므로
        // primaryStage에는 Scene을 설정하지 않고 그대로 둡니다.
        // (primaryStage.show()를 호출하지 않으면 빈 창이 뜨지 않습니다.)
        //
    }

    public static void main(String[] args) {
        launch(args);   // Java FX 런타임 시작
    }
}*/
