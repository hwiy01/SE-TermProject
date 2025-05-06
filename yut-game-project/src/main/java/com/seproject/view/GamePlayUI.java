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

        // 상단 패널 생성
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,10)); // 가운데 정렬

        // 현재 턴 표시 라벨
        turnLabel = new JLabel("현재 턴: " + gameManager.getCurrentTurnName());
        turnLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));

        // 윷 결과 라벨
        resultLabel = new JLabel("윷 던지기 결과: ");
        resultLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));

        // 라벨들을 상단 패널에 추가
        topPanel.add(turnLabel);
        topPanel.add(resultLabel);

        // 상단 패널을 NORTH에 추가
        add(topPanel, BorderLayout.NORTH);

        // 중앙 - 말판
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.PINK); // 말판 자리
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
        updatePlayerInfo();
        setVisible(true);
    }

    public void updatePlayerInfo() {
        int numPlayers = gameManager.getNumberOfPlayers();
        int piecesPerPlayer = gameManager.getNumberOfPiecesPerPlayer();

        for (int i = 0; i < 4; i++) {
            if (i < numPlayers) {
                String name = gameManager.getPlayerName(i);
                playerInfoLabels[i].setText("<html>" + name + "<br/>말: " + piecesPerPlayer + "</html>");
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
    // 마우스 클릭 이벤트
    addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            int size = Math.min(getWidth(), getHeight());

            // 한 번에 하나만 선택되게
            for (YutPiece piece : pieces) {
                if (piece.contains(e.getX(), e.getY(), size)) {
                    for (YutPiece p : pieces) p.selected = false;
                    piece.selected = true;
                    repaint();
                    break;
                }
            }
        }
    });
    // 마우스 클릭 여부 판단 패널의 사이즈를 바뀌면 말의 크기도 바뀌면서 마우스 선택 영역 조절됨
    public boolean contains(int mouseX, int mouseY, int panelSize) {
        int px = (int) (x * panelSize);
        int py = (int) (y * panelSize);
        int r = panelSize / 10;

        return new Rectangle(px - r, py - r, 2 * r, 2 * r).contains(mouseX, mouseY);
    }

}