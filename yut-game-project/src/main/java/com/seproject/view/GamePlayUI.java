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
    private JLabel resultLabel;     // 윷 결과를 표시하는 라벨
    private JLabel turnLabel;       // 현재 턴 정보를 표시하는 라벨
    private JButton rollButton;     // 윷 던지기 버튼
    private JTextArea scoreArea;    // 플레이어들의 점수 출력 영역
    private GameManager gameManager;

    public GamePlayUI(final GameManager gameManager) {
        this.gameManager = gameManager;

        setTitle("Game Play");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        turnLabel = new JLabel("현재 턴: ", SwingConstants.CENTER);
        resultLabel = new JLabel("윷 결과: ", SwingConstants.CENTER);
        scoreArea = new JTextArea("점수:");
        scoreArea.setEditable(false);

        rollButton = new JButton("윷 던지기");
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiceResult result = gameManager.rollDice();
                showDiceResult(result);
                updateScore();
                showCurrentTurn();
                if (gameManager.checkWinCondition()) {
                    showWinner(gameManager.getCurrentPlayer());
                    rollButton.setEnabled(false);
                } else {
                    gameManager.nextTurn();
                }
            }
        });

        panel.add(turnLabel);
        panel.add(resultLabel);
        panel.add(rollButton);
        panel.add(new JScrollPane(scoreArea));

        add(panel);
        setVisible(true);
    }

    public void updateBoard(Board gameBoard){
        //현재 눈에 보이는 게임판의 상태를 내부 시스템 상태에 맞게 업데이트한다.
    };

    public int selectDiceChoice(){
        // 랜덤 윷던지기 / 지정 윷던지기 선택 사용자로부터 받아옴
        String[] options = {"랜덤 윷던지기", "지정 윷던지기"};
        return JOptionPane.showOptionDialog(this, "윷 던지기 방식을 선택하세요:",
                "윷 선택", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

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

    public int showWhichResultToUse(DiceResult[] outcomes){
        //윷이나 모가 나와서 여러 번 던질 시에 관한 예외 처리이다. Outcomes 중 어떤 것을 선택해서 움직일지 플레이어로부터 입력을 받는다. 선택된 length(도라면 1)를 반환한다.
        Object selected = JOptionPane.showInputDialog(
                this, "사용할 윷 결과를 선택하세요:", "윷 선택",
                JOptionPane.PLAIN_MESSAGE, null, outcomes, outcomes[0]);
        for (int i = 0; i < outcomes.length; i++) {
            if (outcomes[i] == selected) return i;
        }
        return -1;
    };

    public int showWhichPieceToMove(){
        //게임 중에 무슨 말을 움직일 건지 입력받아 해당 말의 id를 반환한다. 실제로 가능한가는 gameManager안에서만 확인할 수 있다.
        String input = JOptionPane.showInputDialog(this, "이동시킬 말의 번호를 입력하세요:");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "잘못된 입력입니다. 숫자를 입력하세요.");
            return -1;
        }
    };

    public void updateScore() {
        // 게임 점수를 업데이트하여 보여줌
        StringBuilder sb = new StringBuilder();
        sb.append("현재 점수:\n");
        for (int i = 0; i < gameManager.getNumberOfPlayers(); i++) {
            sb.append(gameManager.getPlayerName(i)).append(": ")
                    .append(gameManager.getPlayerScore(i)).append("점\n");
        }
        scoreArea.setText(sb.toString());
    };

    public void showCurrentTurn() {
        // 현재 누구의 턴인지 시각적으로 보여준다. 윷던지기 버튼을 누를때까지 보여줌
        turnLabel.setText("현재 턴: " + gameManager.getCurrentTurnName());
    };
}