package com.seproject.view;

import com.seproject.controller.GameManager;
import com.seproject.enums.DiceResult;
import com.seproject.model.Board;
import com.seproject.model.Piece;
import com.seproject.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GamePlayUI extends JFrame {
    private JLabel resultLabel; // 윷 던지기 결과 표시
    private JLabel turnLabel; // 현재 턴 표시
    private JButton rollRandomButton; // 랜덤 윷 던지기 버튼
    private JButton rollSpecificButton; // 지정 윷 던지기 버튼
    private JLabel[] playerInfoLabels = new JLabel[4]; // 4개의 플레이어 정보
    private GameManager gameManager;

    public GamePlayUI(final GameManager gameManager) {
        this.gameManager = gameManager;

        setTitle("Yut-Nol-I Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널 생성 ( 현재 턴, 윷 던지기 결과 표시 )
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,10));
        turnLabel = new JLabel("현재 턴: " + gameManager.getCurrentTurnName());
        turnLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));
        resultLabel = new JLabel("윷 던지기 결과: ");
        resultLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));
        topPanel.add(turnLabel);
        topPanel.add(resultLabel);
        add(topPanel, BorderLayout.NORTH);

        //보드 패널 생성 및 보드 배치
        BoardPanel boardPanel = new BoardPanel(gameManager.getBoard(), gameManager.getGamePieces());
        add(boardPanel, BorderLayout.CENTER);

        // 왼쪽 - 플레이어 정보 + 랜덤 윷 버튼
        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        playerInfoLabels[0] = new JLabel("", SwingConstants.CENTER);
        rollRandomButton = new JButton("랜덤 윷 던지기");
        playerInfoLabels[2] = new JLabel("", SwingConstants.CENTER);

        rollRandomButton.addActionListener((ActionEvent e) -> {
            DiceResult result = gameManager.rollDice();
            showDiceResult(result);
            updateTurn();
            if (gameManager.checkWinCondition()) {
                showWinner(gameManager.getCurrentPlayer());
                rollRandomButton.setEnabled(false);
                rollSpecificButton.setEnabled(false);
            } else {
                gameManager.nextTurn();
            }
        });

        leftPanel.add(playerInfoLabels[0]);
        leftPanel.add(rollRandomButton);
        leftPanel.add(playerInfoLabels[2]);
        add(leftPanel, BorderLayout.WEST);

        // 오른쪽 - 플레이어 정보 + 지정 윷 버튼
        JPanel rightPanel = new JPanel(new GridLayout(3, 1));
        playerInfoLabels[1] = new JLabel("", SwingConstants.CENTER);
        rollSpecificButton = new JButton("지정 윷 던지기");
        playerInfoLabels[3] = new JLabel("", SwingConstants.CENTER);

        rollSpecificButton.addActionListener((ActionEvent e) -> {
            DiceResult result = selectDiceResult();
            showDiceResult(result);
            updateTurn();
            if (gameManager.checkWinCondition()) {
                showWinner(gameManager.getCurrentPlayer());
                rollRandomButton.setEnabled(false);
                rollSpecificButton.setEnabled(false);
            } else {
                gameManager.nextTurn();
            }
        });

        rightPanel.add(playerInfoLabels[1]);
        rightPanel.add(rollSpecificButton);
        rightPanel.add(playerInfoLabels[3]);
        add(rightPanel, BorderLayout.EAST);

        // 마우스 클릭 이벤트
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 한 번에 하나만 선택되게 함
                for (Piece p : gameManager.getGamePieces()[gameManager.getCurrentTurn()]) { // 현재 턴인 플레이어의 말만 검사함
                    if (boardPanel.isContain(e.getX(), e.getY(), p)) { // 올바르게 말이 선택되었다면?
                        // 전부 false로 바꾼 다음 선택된 말을 true로 바꿔서 한 번에 하나의 말만 선택할 수 있도록 한다
                        for (Piece eachP : gameManager.getGamePieces()[gameManager.getCurrentTurn()]){
                            p.selected = false;
                        }
                        p.selected = true;
                        repaint();
                        break;
                    }
                }
            }
        });

        updatePlayerInfo(); // 플레이어 정보 업데이트
        setVisible(true);
    }

    public void updatePlayerInfo() {
        int numPlayers = gameManager.getNumberOfPlayers();
        int piecesPerPlayer = gameManager.getNumberOfPiecesPerPlayer();
        for (int i = 0; i < 4; i++) {
            if (i < numPlayers) {
                String name = gameManager.getPlayerName(i);
                playerInfoLabels[i].setText("<html>" + "<br/>이름: "+ name +
                        "<br/>말: " + piecesPerPlayer + "</html>");
            } else {
                playerInfoLabels[i].setText(""); // 빈칸
            }
        }
    }

    public void updateBoard(Board gameBoard){
        //현재 눈에 보이는 게임판의 상태를 내부 시스템 상태에 맞게 업데이트한다.
    };

    public void updateTurn() {
        turnLabel.setText("현재 턴: " + gameManager.getCurrentTurnName());
    }

    public DiceResult selectDiceResult(){
        // 지정 윷던지기의 경우 선택지 보여주어 선택하도록 함
        DiceResult[] values = DiceResult.values();
        return (DiceResult) JOptionPane.showInputDialog(
                this, "윷 결과를 선택하세요:", "지정 윷 결과 선택",
                JOptionPane.PLAIN_MESSAGE, null, values, values[0]);
    }

    public void showDiceResult(DiceResult result){
        //윷을 던진 결과를 시각화한다.
        resultLabel.setText("윷 결과: " + result.name());
    };

    public void showWinner(Player player){
        //승리한 플레이어를(개인전이므로) 화면에 출력한다.
        JOptionPane.showMessageDialog(this, player.getName() + " 님이 승리하였습니다!", "게임 종료", JOptionPane.INFORMATION_MESSAGE);
    };

//    public int showWhichResultToUse(DiceResult[] outcomes){
//        //윷이나 모가 나와서 여러 번 던질 시에 관한 예외 처리이다. Outcomes 중 어떤 것을 선택해서 움직일지 플레이어로부터 입력을 받는다. 선택된 length(도라면 1)를 반환한다.
//    };
//
//    public int showWhichPieceToMove(){
//        //게임 중에 무슨 말을 움직일 건지 입력받아 해당 말의 id를 반환한다. 실제로 가능한가는 gameManager안에서만 확인할 수 있다.
//    };
}