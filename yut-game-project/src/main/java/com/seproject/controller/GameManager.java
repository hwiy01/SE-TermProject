package com.seproject.controller;

import com.seproject.enums.DiceResult;
import com.seproject.model.Board;
import com.seproject.model.Dice;
import com.seproject.model.Piece;
import com.seproject.model.Player;
import com.seproject.view.GameSetupUI;
import com.seproject.view.GamePlayUI;

public class GameManager {
    public void playGame(){
        this.gamePlayUI = new GamePlayUI(this); // 테스트용
        // 게임 플레이 로직

        // 1.
        // rollDice 호출
        // UI) GamePlayUI의 showDiceResult 호출

        // 2.
        // UI) selectDiceChoice 호출
        // 랜덤 / 지정 윷던지기 중 선택하도록

        // 2-1. 랜덤 윷던지기 경우
        // rollDice 호출 ()
        // currentDiceResult에 값 저장

        // 2-2. 지정 윷던지기 경우
        // UI) selectDiceResult 호출
        // currentDiceResult[]에 값 저장


        // 3.
        // UI) showDiceResult 호출 윷던지기 결과 보여줌


        // 윷을 던지고, 말을 움직이는 로직
        // 4. processMove 함수 호출
        //

        // 5. isCaptureOrGroup 함수 호출
        //

        // 6. scoreUp 함수 호출
        // UI) updateScore()

        // 7. 이겼는지 확인
        // checkwinCondition 호출, true 일때 종료 프로세스 실행


        // 8. nextTurn 함수 호출
        // UI) showCurrentTurn


    } //전체 게임 흐름을 담당하는 로직이다. main에서 호출될 예정이다. 승리자가 나올 때까지 아래의 함수들을 게임 흐름에 맞게 순차적으로 호출한다.

    public void GameManager(Player[] currentPlayers, int numberOfPiecesForEachTeam, Board gameBoard) {
        //생성자이다. GameSetupUI에서 받아온 정보를 토대로 게임 매니저 내부의 players에 입력 받은 어레이를 복사하고, 팀 개수와 각 팀에서 사용할 게임 말의 개수를 내부 변수에 채워 넣는다.
        // GamePlayUI, Dice, Piece 객체 생성
        this.players = currentPlayers;
        this.numberOfPlayers = currentPlayers.length;
        this.numberOfPiecesForEachPlayer = numberOfPiecesForEachTeam;
        this.board = gameBoard;
        this.currentTurn = 0;
        this.oneMoreChance = false;

        this.dice = new Dice[4];
        for (int i = 0; i < 4; i++) {
            dice[i] = new Dice();
        }

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
    // private GameSetupUI gameSetupUI; // 게임 설정에 대한 정보 받아오면 더 필요없으니 제거하는 것으로 수정
    private GamePlayUI gamePlayUI; // 조작할 UI
    private Dice[] dice;  //윷이다. // 길이 4 배열로 초기화

    //private DiceResult[] currentDiceResult;
    private DiceResult currentDiceResult;

    private Piece[][] gamePieces;
    //private Piece[] gamePieces; //게임말들이다


    private int currentTurn;
    //현재 누구의 턴인지 보여준다. 윷놀이는 참가자들이 한명씩 번갈아가면서 윷을 던지므로 범위는 0 ~ length(players) – 1이다. 그런 식으로 진행한다면 현재 윷을 던질 사람은 players[currentTurn]이 된다. 이렇게 게임 흐름에 따라 유연하게 계산하기 위한 내부 변수이다.

    private boolean oneMoreChance;
    //잡았을 시 set된다. 잡고 한 턴 더 하는 것을 위한 상태 플래그를 구현한 부분이다.
    public void nextTurn(){
        //currentTurn에 계속 1을 더해서 턴을 진행시킨다. 만일 oneMoreChance가 1일시 해당 bool값을 0으로 바꾸고 currentTurn값은 바뀌지 않는다.
        // (currentTurn + 1) % (player – 1)을 currentTurn에 넣고 return한다. 1을 빼는 이유는 인덱싱으로 일괄처리하도록 정했기 때문이다.
        // 다른 계산에서도 혹시 관련 연산을 수행해야 한다면 인덱싱으로 처리하는 것을 우선하기 바란다.
        if (oneMoreChance) {
            oneMoreChance = false;
            return; // 한 번 더 기회가 있었던 경우, 턴 유지
        }
        currentTurn = (currentTurn + 1) % numberOfPlayers;
    };

    public void processMove(int srcPathNodeId, int length) {
        //한마디로 srcPathNodeId에 있는 모든 말들을 length만큼 전진시키는 함수이다.
        /* 이 문단은 정의되지 않은 움직임에 관한 예외처리를 담고 있다. 현재 진영의 말을 선택했는지 꼭 확인해야 한다!! 만일 일치하지 않을 시 사용자가 잘못된 입력을 입력한 것으로 오류 코드인 -1을 반환한다. 그리고 즉시 프로세스를 종료한다. 예외 처리는 메인 함수에서 처리하도록 한다.
        이 문단은 업기의 특수한 예외 상황에 관한 추가적인 처리이다. srcPathNode이 0인지 확인한다. 0일시, 현재 윷을 던진 사람의 팀의 말 하나만 length에 대응하는 로케이 션 내의 어레이의 위치로 이동한다. 만일 이 기능이 제대로 구현되지 않았다면 시작 위치에 있는 말들이 싹 다 이동하는 별로 좋지 않은 일이 벌어질 것이다.
        예를 들면 도인 상황이라면 해당 위치의 말들이 possibleDoMove 해당 int 어레이의 내부 값으로 이동한다.
        그 후 iscaptureOrGroup을 호출하기 전, 골인했는지를 반드시 먼저 확인해야 한다. 그렇지 않으면 골인 지점이자 시작 지점인 위치에서 iscaptureOrGroup이 실행되어 게임 흐름이 뒤죽박죽이 될 수 도 있다. 만일 골인했다면, scoreup을 실행한 뒤, 함수를 종료한다.(return)
        processMove함수의 마지막 처리로 iscaptureOrGroup을 호출한다. 그렇게 하면 이론상 시작 위치를 제외한 다른 위치에서 다른 팀의 말끼리의 공존을 막을 수 있다.  */
        for(int i=0; i<numberOfPiecesForEachPlayer; i++){
            if(gamePieces[currentTurn][i].getCurrentPathNodeId()==srcPathNodeId){
                switch (length) {
                    case 1: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleDoMove()); break;
                    case 2: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleGaeMove()); break;
                    case 3: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleGurlMove()); break;
                    case 4: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleYutMove()); break;
                    case 5: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleMoMove()); break;
                    case -1: gamePieces[currentTurn][i].setPathNodeId(board.getPathNodes()[srcPathNodeId].getPossibleBackDoMove()); break;
                }
                if(gamePieces[currentTurn][i].getCurrentPathNodeId()==100){
                    scoreUp(i);
                }
                else{
                    isCaptureOrGroup(gamePieces[currentTurn][i].getCurrentPathNodeId());
                }
                gamePieces[currentTurn][i].selected = false; // 이동 후 선택 해제
            }
        }
    };

    private int[] playerScores;
    public void scoreUp(int pieceId){
        //pieceId에 해당하는 게임 말을 조회하고, 그 말의 수만큼 그 팀(개인전이므로 해당 플레이어가 된다)의 스코어를 올린다.
        // 이후 그 말(들)은 위치 정보를 -1로 표기한다. (승리조건과 디버깅을 위해, 그리고 이러한 값 설정은 추적에도 용이하다.)
        Piece piece = gamePieces[currentTurn][pieceId]; // 현재 플레이어 기준
        int playerId = piece.getPlayerId();             // 말에 저장된 플레이어 ID
        int groupSize = piece.getEachPieces().size(); // 말에 업힌 개수

        //playerScores[playerId] += groupSize;
        playerScores[playerId]+=1;
        //piece.setPathNodeId(-1);

        System.out.println("Player " + players[playerId].getName() +
                " scored " + groupSize + " point(s)!");
    };

    public void isCaptureOrGroup(int srcNodeId){
        //겹쳤는가? 겹쳤다면 잡는가? 그게 아니라면 업는가?에 관한 함수이다. 인자로 시작 위치를 받았을 경우에는 반드시 아무 연산도 하지 않고 return한다. 게임 시작 시 시작 위치에 겹쳐 있기 때문이다.
        //인자로 받은 위치를 조회하고, 그 위치에 존재하는 모든 말을 추적하여 만일 겹치는 말이 있다면 잡을지 업을지 결정한다.
        // 방금 윷을 던진 팀을 추적하고, 해당 위치에 있는 다른 말이 상태 편 말이라면 상대편 말(들)의 위치를 0으로 바꾸고 oneMoreChance를 1로 바꾸고 return한다.
        // 이는 상대편 말이 잡혀서 시작 위치로 돌아간 것과 잡으면 한번 더 하는 것을 구현한 것이다.
        // 그게 아군의 말이라면 그 위치에 있는 모든 아군의 말들에 관한 업기 연산을 수행한다. 그 후return한다. (업기)’
        if (srcNodeId==0) return; // 시작 위치에서는 겹침을 무시한다.

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
                        movedPiece.groupPiece(target);
                        target.groupPiece(movedPiece);
                        //target.setPathNodeId(-1); // 기존 말을 비활성화
                        System.out.println("Player " + currentTurn + " grouped with own piece.");
                    }
                    return;
                }
            }
        }

        //그리고 isCapturedOrGrouped 기능이 넘 커지는 것 같아서 이 함수는 겹치는지 비교만하고, 잡거나 그룹화 하는 코드는 분리해서 함수로 만드는 거 어때요? -> iscapturedOrGrouped는 그냥 위치에 있는 말 조회해서 겹치는게 있나 있다면 상대편거가 섞여있나 섞여있다고? 너 시작위치로! 아니라고? 그냥 grouped1로 바꾸고 리턴
    };

    public boolean checkWinCondition(){
        // 매 턴이 끝날 때마다 호출된다.
        // 만일 한 팀의 모든 말이 필드에 존재하지 않을 경우(모든 말의 위치가 -1), 게임은 끝나고,
        // 그 팀(개인전이므로 해당 플레이어가 된다)이 승리한다. 어느 팀(개인전이므로 해당 플레이어가 된다)도 그 조건을 만족시키지 못할 경우 게임은 계속된다.
        for (int i = 0; i < numberOfPlayers; i++) {
            boolean allOut = true;
            for (int j = 0; j < numberOfPiecesForEachPlayer; j++) {
                if (gamePieces[i][j].getCurrentPathNodeId() != -1) {
                    allOut = false;
                    break;
                }
            }
            if (allOut) {
                System.out.println("Player " + players[i].getName() + "win!");
                return true;
            }
        }
        return false;
    };


    public DiceResult rollDice(){
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
            currentDiceResult = DiceResult.BACKDO;
        } else {
            switch (count) {
                case 0: currentDiceResult = DiceResult.MO; break;
                case 1: currentDiceResult = DiceResult.DO; break;
                case 2: currentDiceResult = DiceResult.GAE; break;
                case 3: currentDiceResult = DiceResult.GEOL; break;
                case 4: currentDiceResult = DiceResult.YUT; break;
                default: throw new IllegalStateException("Unexpected dice count: " + count);
            }
        }

        return currentDiceResult;
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public String getPlayerName(int index) {
        return players[index].getName();
    }

    public int getPlayerScore(int index) {
        return playerScores[index];
    }

    public String getCurrentTurnName() {
        return players[currentTurn].getName();
    }
    public Player getCurrentPlayer() {
        return players[currentTurn];
    }

    public int getNumberOfPiecesPerPlayer() {
        return numberOfPiecesForEachPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public Piece[][] getGamePieces() {
        return gamePieces;
    }

    public int getCurrentTurn(){
        return currentTurn;
    }
}