package com.seproject.controller;

import com.seproject.enums.DiceResult;
import com.seproject.model.Board;
import com.seproject.model.Dice;
import com.seproject.model.Piece;
import com.seproject.model.Player;
import com.seproject.view.GamePlayUI;
import com.seproject.view.GamePlayUIFX;

import java.util.ArrayList;

public class GameManager {
    public void playGame(){
        if (this.numberOfPlayers > 0 && this.numberOfPiecesForEachPlayer > 0) {
            this.dice = new Dice[4];
            for (int i = 0; i < 4; i++) {
                dice[i] = new Dice();
            }
            this.gamePieces = new Piece[this.numberOfPlayers][this.numberOfPiecesForEachPlayer];
            for (int i = 0; i < this.numberOfPlayers; i++) {
                for (int j = 0; j < this.numberOfPiecesForEachPlayer; j++) {
                    this.gamePieces[i][j] = new Piece(j, i); // pieceId, playerId
                }
            }

            this.playerScores = new int[this.numberOfPlayers];

        }
        //this.gamePlayUI = new GamePlayUI(this); // swing ui 객체 생성
        this.gamePlayUIFX = new GamePlayUIFX(this); // FX ui 객체 생성
    } //전체 게임 흐름을 담당하는 로직이다. main에서 호출될 예정이다. 승리자가 나올 때까지 아래의 함수들을 게임 흐름에 맞게 순차적으로 호출한다.

    public GameManager() {
        //생성자이다. GameSetupUI에서 받아온 정보를 토대로 게임 매니저 내부의 players에 입력 받은 어레이를 복사하고, 팀 개수와 각 팀에서 사용할 게임 말의 개수를 내부 변수에 채워 넣는다.
        this.currentTurn = 0;
        this.oneMoreChance = true;
        this.currentDiceResult = new ArrayList<>();
        this.dice = new Dice[4];
        for (int i = 0; i < 4; i++) {
            dice[i] = new Dice();
        }

        //게임 말 정보 저장
        gamePieces = new Piece[numberOfPlayers][numberOfPiecesForEachPlayer];
        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = 0; j < numberOfPiecesForEachPlayer; j++) {
                gamePieces[i][j] = new Piece(j, i); // pieceId, playerId
            }
        }
    };

    private int numberOfPiecesForEachPlayer;  //각 “플레이어”당 말 개수를 의미한다.
    private int numberOfPlayers; //플레이어들의 숫자를 내부적으로 저장한다.
    private Player[] players;
    //플레이어 어레이이다. 각각의 플레이어 객체를 저장한다. 몇 명의 플레이어가 플레이 할지 모르기 때문에 게임 초기 입력을 받아서 그 숫자를 확정지어야 한다.

    private Board board; //게임판을 의미합니다. 모양이 달라질 경우 해당 모양에 대응되는 게임판 형식을 새로 하드코딩하여 보드 클래스 내부에 집어 넣고, 이 변수에 그 값을 집어넣는 식으로 사용할 수 있다. Board의 생성자를 이용하자.
    //private GamePlayUI gamePlayUI; //swing 사용을 위한 변수
    private GamePlayUIFX gamePlayUIFX; // FX 사용을 위한 변수
    private Dice[] dice;  //윷이다. // 길이 4 배열로 초기화

    //private DiceResult[] currentDiceResult;
    private ArrayList<DiceResult> currentDiceResult;

    private Piece[][] gamePieces;
    //private Piece[] gamePieces; //게임말들이다


    private int currentTurn;
    //현재 누구의 턴인지 보여준다. 윷놀이는 참가자들이 한명씩 번갈아가면서 윷을 던지므로 범위는 0 ~ length(players) – 1이다. 그런 식으로 진행한다면 현재 윷을 던질 사람은 players[currentTurn]이 된다. 이렇게 게임 흐름에 따라 유연하게 계산하기 위한 내부 변수이다.

    private boolean oneMoreChance;
    //윷을 던질 수 있는가에 대한 플래그

    public void nextTurn(){
        oneMoreChance = true;
        currentTurn = (currentTurn + 1) % numberOfPlayers;
    };

    public void processMove(int srcPathNodeId, int length) {
        //한마디로 srcPathNodeId에 있는 모든 말들을 length만큼 전진시키는 함수이다.
        if(srcPathNodeId==-5){ // 아직 출발하지 않은 말의 이동인 경우 이동되는 위치를 명시적으로 제공하며 하나의 말만 출발한다
            for(int i=0; i<numberOfPiecesForEachPlayer; i++){
                if(gamePieces[currentTurn][i].getCurrentPathNodeId()==-5){
                    switch (length) {
                        case 1: gamePieces[currentTurn][i].setPathNodeId(1); break;
                        case 2: gamePieces[currentTurn][i].setPathNodeId(2); break;
                        case 3: gamePieces[currentTurn][i].setPathNodeId(3); break;
                        case 4: gamePieces[currentTurn][i].setPathNodeId(4); break;
                        case 5: gamePieces[currentTurn][i].setPathNodeId(5); break;
                        case -1: gamePieces[currentTurn][i].setPathNodeId(-5); break;
                    }
                    isCaptureOrGroup(gamePieces[currentTurn][i].getCurrentPathNodeId()); // 이동된 위치에 말이 있으면 잡거나 업는다
                    return; // 출발하지 않은 말은 하나만 출발
                }
            }
        }
        for(int i=0; i<numberOfPiecesForEachPlayer; i++){ //해당 위치에 있는 모든 말들을 이동한다 == 업은 모든 말 이동
            if(gamePieces[currentTurn][i].getCurrentPathNodeId()==srcPathNodeId){
                switch (length) {
                    case 1: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleDoMove()); break;
                    case 2: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleGaeMove()); break;
                    case 3: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleGurlMove()); break;
                    case 4: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleYutMove()); break;
                    case 5: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleMoMove()); break;
                    case -1: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleBackDoMove()); break;
                }
                if(gamePieces[currentTurn][i].getCurrentPathNodeId()==100){ // 골인했다면 점수를 획득한다
                    scoreUp(i);
                }
                else{
                    isCaptureOrGroup(gamePieces[currentTurn][i].getCurrentPathNodeId()); // 잡았는지 업는지 판단
                }
                gamePieces[currentTurn][i].selected = false; // 이동 후 선택 해제
            }
        }
    };

    private int[] playerScores;
    public void scoreUp(int pieceId){
        //pieceId에 해당하는 게임 말을 조회하고, 그 말의 수만큼 그 팀(개인전이므로 해당 플레이어가 된다)의 스코어를 올린다.
        Piece piece = gamePieces[currentTurn][pieceId]; // 현재 플레이어 기준
        int playerId = piece.getPlayerId();             // 말에 저장된 플레이어 ID
        int groupSize = piece.getEachPieces().size(); // 말에 업힌 개수

        playerScores[playerId]+=1;

        System.out.println("Player " + players[playerId].getName() +
                " scored " + groupSize + " point(s)!");
    };

    public void isCaptureOrGroup(int srcNodeId){
        //겹쳤는가? 겹쳤다면 잡는가? 그게 아니라면 업는가?에 관한 함수이다. 인자로 시작 위치를 받았을 경우에는 반드시 아무 연산도 하지 않고 return한다. 게임 시작 시 시작 위치에 겹쳐 있기 때문이다.
        //인자로 받은 위치를 조회하고, 그 위치에 존재하는 모든 말을 추적하여 만일 겹치는 말이 있다면 잡을지 업을지 결정한다.
        // 방금 윷을 던진 팀을 추적하고, 해당 위치에 있는 다른 말이 상태 편 말이라면 상대편 말(들)의 위치를 -5으로 바꾸고 oneMoreChance를 true로 바꾸고 return한다.
        // 이는 상대편 말이 잡혀서 시작 위치로 돌아간 것과 잡으면 한번 더 하는 것을 구현한 것이다.
        // 그게 아군의 말이라면 그 위치에 있는 모든 아군의 말들에 관한 업기 연산을 수행한다.
        if (srcNodeId==-5) return; // 시작 위치에서는 겹침을 무시한다.

        Piece movedPiece=null; // 현재 턴에 이동한 자신의 말

        // 현재 턴의 플레이어 말 중에서 srcNodeId에 위치한 말 찾기
        for (int i = 0; i < numberOfPiecesForEachPlayer; i++) {
            Piece piece = gamePieces[currentTurn][i];
            if (piece.getCurrentPathNodeId() == srcNodeId) {
                movedPiece = piece;
                break;
            }
        }
        // 모든 플레이어의 말들을 검사해서 겹치는 말이 있는지 확인
        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = 0; j < numberOfPiecesForEachPlayer; j++) {
                Piece target = gamePieces[i][j];

                // 자기 자신은 건너뜀
                if (i == currentTurn && target == movedPiece) continue;

                // 같은 위치에 있는 다른 말 발견
                if (target.getCurrentPathNodeId() == srcNodeId) {
                    if (target.getPlayerId() != currentTurn) {
                        // 적군 말이면 잡기
                        target.separatePieceGroup(); // 상대 말 제거
                        oneMoreChance = true;
                        System.out.println("Player " + currentTurn + " captured opponent piece!");
                    } else {
                        // 아군 말이면 업기
                        // 서로 업는 것으로 하여 서로에 대한 정보를 저장한다
                        movedPiece.groupPiece(target);
                        target.groupPiece(movedPiece);
                        System.out.println("Player " + currentTurn + " grouped with own piece.");
                    }
                }
            }
        }
    };

    public boolean checkWinCondition(){
        // 이동한 후에 호출된다
        // 플레이어의 점수가 총 말의 수와 같아지면 해당 플레이어가 승리한다.
        for (int i = 0; i < numberOfPlayers; i++) {
            if (playerScores != null && i < playerScores.length &&
                    players != null && i < players.length && players[i] != null) {
                if (playerScores[i] == numberOfPiecesForEachPlayer) {
                    System.out.println("Player " + players[i].getName() + " wins! All " + numberOfPiecesForEachPlayer + " pieces scored.");
                    return true;
                }
            }
        }
        return false;
    }

    public void rollDice(){
        //roll.Dice를 호출하여 랜덤 DiceResult 값 받아옴
        //Dice.rollDice는 ‘랜덤으로 던지기’ 기능을 수행하는 함수로, DiceResult를 정해진 비율에 따라 반환

        int[] result = new int[4]; // 각 윷의 결과를 저장하는 배열
        int count = 0; // 납작한 면이 나온 개수를 count
        for (int i = 0; i < 4; i++) {
            result[i] = dice[i].roll(); // 윷 4개 각각 던지기
            count += result[i];
        }

        // 납작한 면이 1개만 나왔고, 납작한 면이 나온 윷이 첫번째 윷일 경우를 빽도로 판정함.
        if (count == 1 && result[0] == 1 && result[1] == 0 && result[2] == 0 && result[3] == 0) {
            currentDiceResult.add(DiceResult.BACKDO);
        } else {
            switch (count) {
                case 0: currentDiceResult.add(DiceResult.MO); break;
                case 1: currentDiceResult.add(DiceResult.DO); break;
                case 2: currentDiceResult.add(DiceResult.GAE); break;
                case 3: currentDiceResult.add(DiceResult.GEOL); break;
                case 4: currentDiceResult.add(DiceResult.YUT); break;
                default: throw new IllegalStateException("Unexpected dice count: " + count);
            }
        }
    }

    //플레이어의 수를 받아와서 저장한 후 Player 배열을 생성한다
    public void setPlayer(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        players = new Player[numberOfPlayers];
    }

    //말 수를 받아와서 저장한다
    public void setPiece(int numberOfPiecesForEachPlayer){
        this.numberOfPiecesForEachPlayer = numberOfPiecesForEachPlayer;
    }

    //판의 모양을 받아와서 저장한다
    public void setBoard(int shape){
        this.board = new Board(shape);
    }

    //플레이어의 이름 배열을 받아와서 각 이름으로 Player 객체를 생성한다
    public void setPlayerName(String[] names){
        for(int i=0; i<numberOfPlayers; i++){
            players[i] = new Player(names[i], i); //플레이어의 ID는 0부터 순차적으로 증가시키면서 지정한다
        }
    }

    //플레이어의 수를 반환한다
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    //플레이어의 이름을 반환한다
    public String getPlayerName(int index) {
        return players[index].getName();
    }

    //플에이어의 점수를 반환한다
    public int getPlayerScore(int index) {
        return playerScores[index];
    }

    //현재 턴의 플레이어 이름을 반환한다
    public String getCurrentTurnName() {
        return players[currentTurn].getName();
    }

    //현재 턴의 플레이어를 반환한다
    public Player getCurrentPlayer() {
        return players[currentTurn];
    }

    //플레이어당 말의 수를 반환한다
    public int getNumberOfPiecesPerPlayer() {
        return numberOfPiecesForEachPlayer;
    }

    //보드의 모양을 반환한다
    public Board getBoard() {
        return board;
    }

    //모든 말을 반환한다
    public Piece[][] getGamePieces() {
        return gamePieces;
    }

    //현재 턴을 반환한다
    public int getCurrentTurn(){
        return currentTurn;
    }

    // 해당 플레이어의 대기 중인 말의 개수를 반환
    public int getNumberOfWaitingPieces(int playerId) {
        int count = 0;
        if (gamePieces == null || playerId < 0 || playerId >= numberOfPlayers || gamePieces[playerId] == null) {
            return 0;
        }
        for (Piece piece : gamePieces[playerId]) {
            if (piece.getCurrentPathNodeId() == -5) {
                count++;
            }
        }
        return count;
    }

    //윷을 던질 기회가 있는지 반환한다
    public boolean getChance(){
        return oneMoreChance;
    }

    //윷을 던질 기회가 없도록 바꾼다
    public void noChance(){
        oneMoreChance = false;
    }

    //윷을 던질 기회를 준다
    public void moreChance(){
        oneMoreChance = true;
    }

    //윷 결과를 반환한다
    public ArrayList<DiceResult> getDiceResult() {
        return currentDiceResult;
    }

    //지정 윷 결과를 저장한다
    public void addSelectDiceResult(DiceResult select){
        currentDiceResult.add(select);
    }

    //사용한 윷을 윷 결과에서 제거한다
    public void deleteUsedDiceResult(DiceResult use){
        currentDiceResult.remove(use);
    }

    //윷 결과에 빽도만 있는지 확인한다
    public boolean onlyBackDo(){
        for (DiceResult diceResult : currentDiceResult) {
            if (diceResult.getMoveSteps() != -1) {
                return false;
            }
        }
        return true;
    }
}