package com.seproject.view;

import com.seproject.controller.GameManager;
import com.seproject.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GameSetupUI extends JFrame {

    public GameSetupUI(GameManager gameManager){
        setTitle("Yut-Nol-I setting"); //Frame의 title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //x버튼으로 창 종료 기능
        setSize(500, 300); //창 크기
        setLocationRelativeTo(null); //창 가운데에 나타나도록 설정
        setLayout(new BorderLayout()); //layout 설정

        //상단에 레이블 설정
        JLabel title = new JLabel("Yut-Nol-I setting", SwingConstants.CENTER); //제목 레이블 생성
        title.setFont(new Font("맑은고딕", Font.BOLD, 20)); //폰트 설정
        add(title, BorderLayout.NORTH); //상단에 레이블 추가

        //중간 부분에 layout 설정
        JPanel setting = new JPanel(new GridLayout(2, 1)); //중간부분을 2행 1열로 나누어서 각각 패널을 넣기 위한 layout
        JPanel count = new JPanel(new GridLayout(1, 2)); //1행에 들어갈 패널 설정

        //플레이어 수 선택 콤보박스 설정
        JPanel numPlayer =new JPanel(new FlowLayout());
        numPlayer.add(new JLabel("The Number of players"));
        JComboBox<Integer> playerCombo = new JComboBox<>(new Integer[]{2, 3, 4}); //2, 3, 4 선택 가능한 콤보박스
        numPlayer.add(playerCombo); //플레이어 수 콤보박스 numPlayer 패널에 추가

        //말 수 선택 콤보박스 선택
        JPanel numPiece = new JPanel(new FlowLayout());
        numPiece.add(new JLabel("The Number of pieces"));
        JComboBox<Integer> pieceCombo = new JComboBox<>(new Integer[]{2, 3, 4, 5}); //2 ~ 5 선택 가능한 콤보박스
        numPiece.add(pieceCombo); //말 수 콤보박스 numPiece 패널에 추가

        //플에이어 수랑 말 수에 대한 패널을 count 패널에 추가 -> 중간 부분의 1행에 추가
        count.add(numPlayer);
        count.add(numPiece);
        setting.add(count);

        //윷 판의 모양을 정하는 버튼
        JPanel shapeBoard = new JPanel(new GridLayout(2, 1)); //1행에는 소제목 레이블을 넣는다
        shapeBoard.add(new JLabel("Shape of Board")); //소제목 레이블
        JPanel shapeButton = new JPanel(new FlowLayout()); //2행에 모양을 선택하는 버튼을 넣기 위한 패널
        JToggleButton square = new JToggleButton("SQUARE"); //사각형 버튼
        final JToggleButton pentagon = new JToggleButton("PENTAGON"); //오각형 버튼
        JToggleButton hexagon = new JToggleButton("HEXAGON"); //육각형 버튼

        //세 버튼을 한 번에 하나만 선택되도록 하기 위해 그룹으로 묶는다
        ButtonGroup shapeGroup = new ButtonGroup();
        shapeGroup.add(square);
        shapeGroup.add(pentagon);
        shapeGroup.add(hexagon);
        square.setSelected(true); //기본값으로 사각형이 선택되어 있음

        //2행에 들어갈 패널에 각 버튼 추가
        shapeButton.add(square);
        shapeButton.add(pentagon);
        shapeButton.add(hexagon);

        //2행에 패널 추가 밑 중간부분의 2행에 패널 추가
        shapeBoard.add(shapeButton);
        setting.add(shapeBoard);

        //완성된 중간부분을 위한 패널을 추가한다
        add(setting, BorderLayout.CENTER);

        //next 버튼 -> 클릭 시 플레이어 수 말 수 윷 판 모양을 gamemanager에 저장하고 이름을 입력받는 창을 띄운다
        JButton next = new JButton("NEXT");
        next.addActionListener((ActionEvent e) -> { //버튼 클릭 이벤트
            gameManager.setPlayer((Integer)playerCombo.getSelectedItem()); //플레이어 수 저장
            gameManager.setPiece((Integer)pieceCombo.getSelectedItem()); //말 수 저장
            //윷 판 모양 저장
            if(square.isSelected()){
                gameManager.setBoard(4);
            }
            else if(pentagon.isSelected()){
                gameManager.setBoard(5);
            }
            else{
                gameManager.setBoard(6);
            }
            //이름 입력 창 띄우고 입력받은 이름을 gamemanager에 있는 player들의 이름에 저장한다
            gameManager.setPlayerName(showNicknameInput((Integer)playerCombo.getSelectedItem()));
        });

        //next 버튼을 화면 하단에 추가한다
        add(next, BorderLayout.SOUTH);

        setVisible(true); //창 띄우기
    }
    private String[] nicknameBuffer;
    //닉네임이 겹치는지 미리 확인하기 위해 존재한다.

    public void showMainMenu(){
        //메인 메뉴를 보여준다.
    };

//    public int showNumberOfPiecesInEachPlayerChoice(){
//        //각각의 플레이어가 몇 개의 말을 사용할 것인지 사용자에게서 입력받는다.
//    }
//
//    public int showNumberOfPlayersChoice(){
//        //플레이어가 몇명할지 선택하도록 창을 띄우고 입력받는다
//    };
//
//    public int showBoardShapeChoice(){
//        //게임 보드를 선택하도록 창을 띄우고 입력받는다
//    };
    
    //이름 입력 창
    public String[] showNicknameInput(int numberOfPlayer){
        //이름을 정하는 것을 유저에게 요구한다. 정해진 이름은 nicknameBuffer과 비교해서 겹치는 게 있나 확인한 후 겹치는게 없을 시 버퍼에 저장한다. 입력이 끝나면 지금까지 입력받은 닉네임들을 반환한다.
        JDialog subFrame = new JDialog(this, "input name", true); //서브 창 생성
        subFrame.setLayout(new BorderLayout());

        //플레이어의 수 만큼 이름 입력 칸을 만든다
        JPanel name = new JPanel(new GridLayout(numberOfPlayer, 2, 5, 5));
        ArrayList<JTextField> inputName = new ArrayList<>();
        for(int i=0; i<numberOfPlayer; i++){
            name.add(new JLabel("Name of Player" + i+1 + ": "));
            JTextField field = new JTextField();
            inputName.add(field);
            name.add(field);
        }

        String[] names = new String[numberOfPlayer]; //입력받은 이름을 저장할 배열
        //start 버튼 -> 클릭 시 게임이 시작된다 (이름을 전부 입력하지 않거나 중복 이름을 입력할 경우 메시지 출력 후 다시 입력받는다)
        JButton start = new JButton("START");
        start.addActionListener((ActionEvent e) -> { //버튼 클릭 이벤트

            int i = 0; //저장한 이름의 개수 -> 이름이 중복인지 확인하기 위한 것
            for(JTextField field : inputName){ //입력된 이름을 하나씩 가져와서 for문을 실행한다
                for(int j=0; j<i; j++){ //중복인지 확인
                    if(field.getText().trim().equals(names[j])){ //중복이면 메시지 출력 후 클릭 이벤트 종료 -> 게임 실행 안됨
                        JOptionPane.showMessageDialog(subFrame, "duplication: \"" + field.getText().trim() + "\"");
                        return;
                    }
                    if(field.getText().trim().isEmpty()){ //이름 미입력 시 메시지 출력 후 클릭 이벤트 종료 -> 게임 실행 안됨
                        JOptionPane.showMessageDialog(subFrame, "Input all names");
                        return;
                    }
                    names[i] = field.getText().trim(); //문제 없으면 입력된 이름을 배열에 저장
                }
                i++;
            }
        });
        subFrame.dispose(); //서브 창 제거
        this.dispose(); //메인 창 제거 -> 다음 단계로 넘어간다 (게임 실행 단계)
        return names; //입력된 이름을 반환한다
    };
}
