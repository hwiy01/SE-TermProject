package com.seproject.view;

import com.seproject.controller.GameManager;
import com.seproject.enums.DiceResult;
import com.seproject.model.Board;
import com.seproject.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePlayUI extends JFrame {
//    private JLabel resultLabel;     // 윷 결과를 표시하는 라벨
//    private JLabel turnLabel;       // 현재 턴 정보를 표시하는 라벨
//    private JButton rollButton;     // 윷 던지기 버튼
//    private JTextArea scoreArea;    // 플레이어들의 점수 출력 영역
//    private GameManager gameManager;

//    public GamePlayUI(final GameManager gameManager) {
//        this.gameManager = gameManager;
//
//        setTitle("Game Play");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(500,400);
//        setLocationRelativeTo(null);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(5, 1));
//
//        turnLabel = new JLabel("현재 턴: ", SwingConstants.CENTER);
//        resultLabel = new JLabel("윷 결과: ", SwingConstants.CENTER);
//        scoreArea = new JTextArea("점수:");
//        scoreArea.setEditable(false);
//
//        rollButton = new JButton("윷 던지기");
//        rollButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                DiceResult result = gameManager.rollDice();
//                showDiceResult(result);
//                updateScore();
//                showCurrentTurn();
//                if (gameManager.checkWinCondition()) {
//                    showWinner(gameManager.getCurrentPlayer());
//                    rollButton.setEnabled(false);
//                } else {
//                    gameManager.nextTurn();
//                }
//            }
//        });
//
//        panel.add(turnLabel);
//        panel.add(resultLabel);
//        panel.add(rollButton);
//        panel.add(new JScrollPane(scoreArea));
//
//        add(panel);
//        setVisible(true);
//    }

    private JLabel resultLabel;
    private JLabel turnLabel;

    private JButton rollRandomButton;
    private JButton rollSpecificButton;

    private JLabel[] playerInfoLabels = new JLabel[4]; // 4개의 플레이어 정보 위치
    private GameManager gameManager;

    public GamePlayUI(final GameManager gameManager) {
        this.gameManager = gameManager;

        setTitle("Yut-Nol-I Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 - 현재 턴 표시
        turnLabel = new JLabel("현재 턴: " + gameManager.getCurrentTurnName(), SwingConstants.CENTER);
        turnLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));
        add(turnLabel, BorderLayout.NORTH);

        // 하단 - 윷 결과
        resultLabel = new JLabel("윷 결과: ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("맑은고딕", Font.PLAIN, 18));
        add(resultLabel, BorderLayout.SOUTH);

        // 중앙 - 말판
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK); // 말판 자리
        add(centerPanel, BorderLayout.CENTER);

        // 왼쪽 - 플레이어 정보 + 랜덤 윷 버튼
        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        playerInfoLabels[0] = new JLabel("이름 / 말 개수", SwingConstants.CENTER);
        rollRandomButton = new JButton("랜덤 윷 던지기");
        playerInfoLabels[2] = new JLabel("이름 / 말 개수", SwingConstants.CENTER);

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
        playerInfoLabels[1] = new JLabel("이름 / 말 개수", SwingConstants.CENTER);
        rollSpecificButton = new JButton("지정 윷 던지기");
        playerInfoLabels[3] = new JLabel("이름 / 말 개수", SwingConstants.CENTER);

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

        setVisible(true);
    }

    public void updateBoard(Board gameBoard){
        //현재 눈에 보이는 게임판의 상태를 내부 시스템 상태에 맞게 업데이트한다.
    };

//    public int selectDiceChoice(){
//        // 랜덤 윷던지기 / 지정 윷던지기 선택 사용자로부터 받아옴
//        String[] options = {"랜덤 윷던지기", "지정 윷던지기"};
//        return JOptionPane.showOptionDialog(this, "윷 던지기 방식을 선택하세요:",
//                "윷 선택", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
//                null, options, options[0]);
//
//    }
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
//        Object selected = JOptionPane.showInputDialog(
//                this, "사용할 윷 결과를 선택하세요:", "윷 선택",
//                JOptionPane.PLAIN_MESSAGE, null, outcomes, outcomes[0]);
//        for (int i = 0; i < outcomes.length; i++) {
//            if (outcomes[i] == selected) return i;
//        }
//        return -1;
//    };
}