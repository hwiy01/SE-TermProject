package com.seproject;

import com.seproject.controller.GameManager;
import com.seproject.view.GameSetupUI;

public class App 
{
    public static void main( String[] args )
    {
        // GameManager 객체 생성
        // GameManager의 playGame 함수 호출, 모든 실행 여기서 관여
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameManager gameManager = new GameManager();
            new GameSetupUI(gameManager); // UI 실행
        });
    }
}
