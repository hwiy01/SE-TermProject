package com.seproject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // GameManager 객체 생성
        // GameManager의 playGame 함수 호출, 모든 실행 여기서 관여

        // 1. 
        // GameSetUpUI , GamePlayUI,  생성 
        // GameSetupUI.ShowMainMenu() 호출 //  아래의 옵션들
        // GameSetupUI.ShowNumberOfPiecesInEachPlayerChoice(),  showNumberOfPlayersChoice(), showBoardShapeChoice() showNicknameInput() 호출하여 첫 게임에 필요한 환경설정


        // 2. 
        // Board 객체 생성 - 이때 numberOfShpae 넘겨줌
        // GameManager 객체 생성하여 Board와 위의 옵션들 넘겨줌
        System.out.println( "Hello World!" );
    }
}
