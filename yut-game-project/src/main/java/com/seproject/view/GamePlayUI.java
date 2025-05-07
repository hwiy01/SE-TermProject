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
    private WaitingPieceDisplayPanel[] waitingPieceDisplays; // 새로 추가될 필드
    private JPanel[] playerSlots; // 플레이어 정보와 대기 말을 함께 담을 패널 배열
    private static final Color[] PLAYER_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE
    };

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

        // 플레이어 정보 및 대기 말 표시를 위한 초기화
        this.playerInfoLabels = new JLabel[4];
        this.waitingPieceDisplays = new WaitingPieceDisplayPanel[4];
        this.playerSlots = new JPanel[4];

        for (int i = 0; i < 4; i++) {
            playerSlots[i] = new JPanel();
            playerSlots[i].setLayout(new BoxLayout(playerSlots[i], BoxLayout.Y_AXIS));
            playerSlots[i].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            playerInfoLabels[i] = new JLabel("", SwingConstants.CENTER);
            playerInfoLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            waitingPieceDisplays[i] = new WaitingPieceDisplayPanel(i);
            waitingPieceDisplays[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            playerSlots[i].add(playerInfoLabels[i]);
            playerSlots[i].add(Box.createRigidArea(new Dimension(0, 5)));
            playerSlots[i].add(waitingPieceDisplays[i]);
        }

        // 왼쪽 패널 수정
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rollRandomButton = new JButton("랜덤 윷 던지기");
        rollRandomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollRandomButton.addActionListener((ActionEvent e) -> {
            DiceResult result = gameManager.rollDice();
            showDiceResult(result);
            // processMove 호출
            // 예시: 사용자가 말을 선택하고 이동하는 로직이 여기에 들어간다고 가정
            // if (말 이동이 성공적으로 완료되었다면) {
            //    updatePlayerAndWaitingPiecesInfo(); // 말 상태 변경 후 UI 업데이트
            // }

            if (gameManager.checkWinCondition()) {
                showWinner(gameManager.getCurrentPlayer());
                rollRandomButton.setEnabled(false);
                rollSpecificButton.setEnabled(false);
            } else {
                gameManager.nextTurn();
                updateTurn(); // 현재 턴 표시 업데이트
                updatePlayerAndWaitingPiecesInfo(); // 다음 턴 시작 시 대기 말 포함 전체 정보 업데이트
            }
        });

        leftPanel.add(playerSlots[0]); // 플레이어 1
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(rollRandomButton);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(playerSlots[2]); // 플레이어 3
        add(leftPanel, BorderLayout.WEST);


        // 오른쪽 패널 수정
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rollSpecificButton = new JButton("지정 윷 던지기");
        rollSpecificButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollSpecificButton.addActionListener((ActionEvent e) -> {
            DiceResult result = selectDiceResult();
            if (result == null) return; // 사용자가 선택 취소한 경우
            showDiceResult(result);
            // processMove 호출
            // if (말 이동이 성공적으로 완료되었다면) {
            //    updatePlayerAndWaitingPiecesInfo();
            // }

            if (gameManager.checkWinCondition()) {
                showWinner(gameManager.getCurrentPlayer());
                rollRandomButton.setEnabled(false);
                rollSpecificButton.setEnabled(false);
            } else {
                gameManager.nextTurn();
                updateTurn();
                updatePlayerAndWaitingPiecesInfo();
            }
        });

        rightPanel.add(playerSlots[1]); // 플레이어 2
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(rollSpecificButton);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(playerSlots[3]); // 플레이어 4
        add(rightPanel, BorderLayout.EAST);

        // 마우스 클릭 이벤트
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 한 번에 하나만 선택되게 함
                for (Piece p : gameManager.getGamePieces()[gameManager.getCurrentTurn()]) { // 현재 턴인 플레이어의 말만 검사함
                    if (boardPanel.isContain(e.getX(), e.getY(), p)) { // 올바르게 말이 선택되었다면?
                        // 전부 false로 바꾼 다음 선택된 말을 true로 바꿔서 한 번에 하나의 말만 선택할 수 있도록 한다
                        for (Piece eachP : gameManager.getGamePieces()[gameManager.getCurrentTurn()]){
                            eachP.selected = false;
                        }
                        p.selected = true;
                        repaint();
                        break;
                    }
                }
            }
        });

        updatePlayerAndWaitingPiecesInfo();
        setVisible(true);
    }

    public void updatePlayerAndWaitingPiecesInfo() {
        int numPlayers = gameManager.getNumberOfPlayers();
        int piecesPerPlayer = gameManager.getNumberOfPiecesPerPlayer(); // 총 말 개수

        for (int i = 0; i < 4; i++) { // 최대 4개 슬롯
            if (i < numPlayers) {
                String name = gameManager.getPlayerName(i);
                int waitingCount = gameManager.getNumberOfWaitingPieces(i); // 대기 중인 말 개수 가져오기

                playerInfoLabels[i].setText("<html><div style='text-align: center;'>" +
                        "이름: " + name + "<br>" +
                        "총 말: " + piecesPerPlayer +
                        "</div></html>");
                waitingPieceDisplays[i].updateWaitingPieces(waitingCount); // 대기 말 개수 업데이트하여 다시 그리기
                playerSlots[i].setVisible(true); // 해당 플레이어 슬롯 보이기
            } else {
                playerSlots[i].setVisible(false); // 참여하지 않는 플레이어 슬롯 숨기기
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
        DiceResult[] values = DiceResult.values();
        return (DiceResult) JOptionPane.showInputDialog(
                this, "윷 결과를 선택하세요:", "지정 윷 결과 선택",
                JOptionPane.PLAIN_MESSAGE, null, values, values[0]); // 선택된 결과 또는 null 반환
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