package com.seproject.model;

public class PathNode {
    public PathNode(int pathNodeId, int BoardType){
        //생성자이다. 로케이션 아이디와 게임판 형식을 인자로 받고 그 아이디에 대응하는 정보를 함수가 입력한다. 말을 움직일 때 이는 switch문으로 하나하나 처리되어야 한다. 아래는 게임판 타입 정수 변수가 4일때의 참고용 그림이다.
        switch(BoardType){
            case 4:{ //사각형
                if(pathNodeId==5 || pathNodeId==10){
                    inBranch=1;
                }
                else{
                    inBranch=0;
                }
                if(pathNodeId==0){ // 골인지점에 위치한 경우 빽도가 아니라면 30으로 이동해서 골인했음을 의미함 (30 이상은 모두 골인을 의미)
                    possibleDoMove=30;
                    possibleGaeMove=30;
                    possibleGurlMove=30;
                    possibleYutMove=30;
                    possibleMoMove=30;
                    possibleBackDoMove=19;
                }
                else if(pathNodeId==-5){
                    possibleDoMove=1;
                    possibleGaeMove=2;
                    possibleGurlMove=3;
                    possibleYutMove=4;
                    possibleMoMove=5;
                    possibleBackDoMove=-5;
                }
                else if(inBranch==1){
                    possibleDoMove=pathNodeId+15;
                    possibleGaeMove=pathNodeId+16;
                    possibleGurlMove=pathNodeId+17; //22번과 27번은 같은 pathNode임
                    possibleYutMove=pathNodeId+18;
                    possibleMoMove=pathNodeId+19;
                    possibleBackDoMove=pathNodeId-1;
                }
                else if(pathNodeId==22){ //22번 노드에 위치한 경우 27번으로 간주하고 도착지점을 향한 길로 이동
                    possibleDoMove=28;
                    possibleGaeMove=29;
                    possibleGurlMove=0; //0은 출발지점이자 도착지점 위에 위치함 여기서 +1칸 가면 골인임
                    possibleYutMove=30; //30 이상은 골인했음을 의미함
                    possibleMoMove=31;
                    possibleBackDoMove=21;
                }
                else if(pathNodeId>19 && pathNodeId<25){ //지름길을 통해서 마지막 일반 길(15 ~ 19 노드)로 복귀했을 경우의 수식임
                    possibleDoMove=((pathNodeId+1)/25)*15+((pathNodeId+1)%25);
                    possibleGaeMove=((pathNodeId+2)/25)*15+((pathNodeId+2)%25);
                    possibleGurlMove=((pathNodeId+3)/25)*15+((pathNodeId+3)%25);
                    possibleYutMove=((pathNodeId+4)/25)*15+((pathNodeId+4)%25);
                    possibleMoMove=((pathNodeId+5)/25)*15+((pathNodeId+5)%25);
                    if(pathNodeId==20){ //20번 노드에서 빽도가 나와 다시 분기점으로 돌아가는 경우
                        possibleBackDoMove=5;
                    }
                    else {
                        possibleBackDoMove = pathNodeId - 1;
                    }
                }
                else if(pathNodeId==25){ // 빽도의 경우 분기점으로 돌아가기 때문에 별도로 처리
                    possibleDoMove=pathNodeId+1;
                    possibleGaeMove=pathNodeId+2;
                    possibleGurlMove=pathNodeId+3;
                    possibleYutMove=pathNodeId+4;
                    possibleMoMove=0;
                    possibleBackDoMove=10;
                }
                else{ //일반적인 경우 단순히 윷 결과만큼 더해서 이동
                    possibleDoMove=pathNodeId+1;
                    possibleGaeMove=pathNodeId+2;
                    possibleGurlMove=pathNodeId+3;
                    possibleYutMove=pathNodeId+4;
                    possibleMoMove=pathNodeId+5;
                    possibleBackDoMove=pathNodeId-1;
                    // 일반 길에서 골인지점을 넘어갔을 경우 possibleMove를 30으로 지정해서 골인했음을 표시, 골인지점에 위치할 경우 0으로 변환
                    if(pathNodeId<20 && possibleDoMove>20){
                        possibleDoMove=30;
                    }
                    else if(possibleDoMove==20){
                        possibleDoMove=0;
                    }
                    if(pathNodeId<20 && possibleGaeMove>20){
                        possibleGaeMove=30;
                    }
                    else if(possibleGaeMove==20){
                        possibleGaeMove=0;
                    }
                    if(pathNodeId<20 && possibleGurlMove>20){
                        possibleGurlMove=30;
                    }
                    else if(possibleGurlMove==20){
                        possibleGurlMove=0;
                    }
                    if(pathNodeId<20 && possibleYutMove>20){
                        possibleYutMove=30;
                    }else if(possibleYutMove==20){
                        possibleYutMove=0;
                    }
                    if(pathNodeId<20 && possibleMoMove>20){
                        possibleMoMove=30;
                    }
                    else if(possibleMoMove==20){
                        possibleMoMove=0;
                    }
                }
                if(pathNodeId>=0 && pathNodeId<6){
                    x_ratio=0.9;
                    y_ratio=0.9 - (pathNodeId*0.16);
                }
                else if(pathNodeId>=6 && pathNodeId<11){
                    x_ratio=0.9 - ((pathNodeId-5)*0.16);
                    y_ratio=0.1;
                }
                else if(pathNodeId>=11 && pathNodeId<16){
                    x_ratio=0.1;
                    y_ratio=0.1 + ((pathNodeId-10)*0.16);
                }
                else if(pathNodeId>=16 && pathNodeId<20){
                    x_ratio=0.1+((pathNodeId-15)*0.16);
                    y_ratio=0.9;
                }
                else if(pathNodeId>=20 && pathNodeId<25){
                    x_ratio=0.9 - ((pathNodeId-19)*0.13);
                    y_ratio=0.1 + ((pathNodeId-19)*0.13);
                }
                else if(pathNodeId>=25 && pathNodeId<30){
//                    x_ratio=0.12 + ((pathNodeId-24)*0.13); // 중앙 노드 겹치게 하기 위함임
//                    y_ratio=0.1 * ((pathNodeId-24)*0.13);
                    if (pathNodeId == 27) {
                        this.visible = false;
                    }
                    x_ratio = 0.25 + ((pathNodeId - 25) * 0.12);
                    y_ratio = 0.25 + ((pathNodeId - 25) * 0.12);
                }
            } break;
            case 5:{ //오각형
                if(pathNodeId==5 || pathNodeId==10 || pathNodeId==15){
                    inBranch=1;
                }
                else{
                    inBranch=0;
                }
                if(pathNodeId==0){ // 골인지점에 위치한 경우 빽도가 아니라면 골인했음을 의미하는 ??로 이동
                    possibleDoMove=40;
                    possibleGaeMove=40;
                    possibleGurlMove=40;
                    possibleYutMove=40;
                    possibleMoMove=40;
                    possibleBackDoMove=24;
                }
                else if(pathNodeId==-5){
                    possibleDoMove=1;
                    possibleGaeMove=2;
                    possibleGurlMove=3;
                    possibleYutMove=4;
                    possibleMoMove=5;
                    possibleBackDoMove=-5;
                }
                else if(inBranch==1){ // 5번 분기점과 10번 분기점에서 동일하게 더하므로 28-33, 29-34 쌍으로 같은 노드를 의미한다
                    possibleDoMove=pathNodeId+20;
                    possibleGaeMove=pathNodeId+21;
                    possibleGurlMove=pathNodeId+22; //27, 32, 37번은 같은 pathNode임
                    possibleYutMove=pathNodeId+23;
                    possibleMoMove=pathNodeId+24;
                    possibleBackDoMove=pathNodeId-1;
                }
                else if(pathNodeId==27 || pathNodeId==32){
                    possibleDoMove=38;
                    possibleGaeMove=39;
                    possibleGurlMove=0;
                    possibleYutMove=40;
                    possibleMoMove=40;
                    possibleBackDoMove=pathNodeId-1;
                }
                else if(pathNodeId>24 && pathNodeId<30){
                    possibleDoMove=((pathNodeId+1)/30)*20+((pathNodeId+1)%30);
                    possibleGaeMove=((pathNodeId+2)/30)*20+((pathNodeId+2)%30);
                    possibleGurlMove=((pathNodeId+3)/30)*20+((pathNodeId+3)%30);
                    possibleYutMove=((pathNodeId+4)/30)*20+((pathNodeId+4)%30);
                    possibleMoMove=((pathNodeId+5)/30)*20+((pathNodeId+5)%30);
                    if(pathNodeId==25){
                        possibleBackDoMove=5;
                    }
                    else{
                        possibleBackDoMove=pathNodeId-1;
                    }
                }
                else if(pathNodeId>29 && pathNodeId<35){
                    possibleDoMove=((pathNodeId+1)/35)*20+((pathNodeId+1)%35);
                    possibleGaeMove=((pathNodeId+2)/35)*20+((pathNodeId+2)%35);
                    possibleGurlMove=((pathNodeId+3)/35)*20+((pathNodeId+3)%35);
                    possibleYutMove=((pathNodeId+4)/35)*20+((pathNodeId+4)%35);
                    possibleMoMove=((pathNodeId+5)/35)*20+((pathNodeId+5)%35);
                    if(pathNodeId==30){
                        possibleBackDoMove=10;
                    }
                    else{
                        possibleBackDoMove=pathNodeId-1;
                    }
                }
                else if(pathNodeId==35){
                    possibleDoMove=pathNodeId+1;
                    possibleGaeMove=pathNodeId+2;
                    possibleGurlMove=pathNodeId+3;
                    possibleYutMove=pathNodeId+4;
                    possibleMoMove=0;
                    possibleBackDoMove=15;
                }
                else{ //일반적인 경우 단순히 윷 결과만큼 더해서 이동
                    possibleDoMove=pathNodeId+1;
                    possibleGaeMove=pathNodeId+2;
                    possibleGurlMove=pathNodeId+3;
                    possibleYutMove=pathNodeId+4;
                    possibleMoMove=pathNodeId+5;
                    possibleBackDoMove=pathNodeId-1;
                    // 일반 길에서 골인지점을 넘어갔을 경우 possibleMove를 30으로 지정해서 골인했음을 표시, 골인지점에 위치할 경우 0으로 변환
                    if(pathNodeId<25 && possibleDoMove>25){
                        possibleDoMove=40;
                    }
                    else if(possibleDoMove==25){
                        possibleDoMove=0;
                    }
                    if(pathNodeId<25 && possibleGaeMove>25){
                        possibleGaeMove=40;
                    }
                    else if(possibleGaeMove==25){
                        possibleGaeMove=0;
                    }
                    if(pathNodeId<25 && possibleGurlMove>25){
                        possibleGurlMove=40;
                    }
                    else if(possibleGurlMove==25){
                        possibleGurlMove=0;
                    }
                    if(pathNodeId<25 && possibleYutMove>25){
                        possibleYutMove=40;
                    }else if(possibleYutMove==25){
                        possibleYutMove=0;
                    }
                    if(pathNodeId<25 && possibleMoMove>25){
                        possibleMoMove=40;
                    }
                    else if(possibleMoMove==25){
                        possibleMoMove=0;
                    }
                }
                if(pathNodeId>=0 && pathNodeId<6){
                    x_ratio=0.75 + (pathNodeId*0.03);
                    y_ratio=0.9 - (pathNodeId*0.096);
                }
                else if(pathNodeId>=6 && pathNodeId<11){
                    x_ratio=0.9- ((pathNodeId-5)*0.08);
                    y_ratio=0.42 - ((pathNodeId-5)*0.064);
                }
                else if(pathNodeId>=11 && pathNodeId<16){
                    x_ratio=0.5 - ((pathNodeId-10)*0.08);
                    y_ratio=0.1 + ((pathNodeId-10)*0.064);
                }
                else if(pathNodeId>=16 && pathNodeId<21){
                    x_ratio=0.1 + ((pathNodeId-15)*0.03);
                    y_ratio=0.42 + ((pathNodeId-15)*0.096);
                }
                else if(pathNodeId>=21 && pathNodeId<25){
                    x_ratio=0.25 + ((pathNodeId-20)*0.1);
                    y_ratio = 0.9;
                }
                else if(pathNodeId>=25 && pathNodeId<28){
                    x_ratio=0.89 - ((pathNodeId-24)*0.13);
                    y_ratio=0.43 + ((pathNodeId-24)*0.05); //중앙 노드의 좌표 비 : 0.5, 0.58
                }
                else if(pathNodeId>=28 && pathNodeId<30){
                    x_ratio=0.5 - ((pathNodeId-27)*0.08);
                    y_ratio=0.58 + ((pathNodeId-27)*0.1);
                }
                else if(pathNodeId>=30 && pathNodeId<33){
                    x_ratio=0.5;
                    y_ratio=0.1 + ((pathNodeId - 29)*0.16);
                }
                else if(pathNodeId>=33 && pathNodeId<35){
                    x_ratio = 0.5 - ((pathNodeId - 32)*0.08);
                    y_ratio = 0.58 + ((pathNodeId - 32)*0.1);
                }
                else if(pathNodeId>=35 && pathNodeId<38){
                    x_ratio = 0.11 + ((pathNodeId-34)*13);
                    y_ratio = 0.43 + ((pathNodeId-34)*0.05);
                }
                else if(pathNodeId>=38 && pathNodeId<40){
                    x_ratio = 0.5 + ((pathNodeId-37)*0.08);
                    y_ratio = 0.58 + ((pathNodeId-37)*0.1);
                }
            } break;
            case 6:{ //6각형의 경우 //
                if(pathNodeId==5 || pathNodeId==10 || pathNodeId==15 || pathNodeId==20){
                    inBranch=1;
                }
                else{
                    inBranch=0;
                }
                if(pathNodeId==0){ // 골인지점에 위치한 경우 빽도가 아니라면 골인했음을 의미하는 50으로 이동
                    possibleDoMove=50;
                    possibleGaeMove=50;
                    possibleGurlMove=50;
                    possibleYutMove=50;
                    possibleMoMove=50;
                    possibleBackDoMove=29;
                }
                else if(pathNodeId==-5){
                    possibleDoMove=1;
                    possibleGaeMove=2;
                    possibleGurlMove=3;
                    possibleYutMove=4;
                    possibleMoMove=5;
                    possibleBackDoMove=-5;
                }
                else if(inBranch==1){
                    possibleDoMove=pathNodeId+25;
                    possibleGaeMove=pathNodeId+26;
                    possibleGurlMove=pathNodeId+27; // 32, 37, 42, 47번은 같은 pathNode임
                    possibleYutMove=pathNodeId+28; //33, 38, 43번은 같은 노드
                    possibleMoMove=pathNodeId+29; //34, 39, 44번은 같은 노드
                    possibleBackDoMove=pathNodeId-1;
                }
                else if(pathNodeId==32 || pathNodeId==37 || pathNodeId==42){
                    possibleDoMove=48;
                    possibleGaeMove=49;
                    possibleGurlMove=0;
                    possibleYutMove=50;
                    possibleMoMove=50;
                    possibleBackDoMove=pathNodeId-1;
                }
                else if(pathNodeId>29 && pathNodeId<35){
                    possibleDoMove=((pathNodeId+1)/35)*25+((pathNodeId+1)%35);
                    possibleGaeMove=((pathNodeId+2)/35)*25+((pathNodeId+2)%35);
                    possibleGurlMove=((pathNodeId+3)/35)*25+((pathNodeId+3)%35);
                    possibleYutMove=((pathNodeId+4)/35)*25+((pathNodeId+4)%35);
                    possibleMoMove=((pathNodeId+5)/35)*25+((pathNodeId+5)%35);
                    if(pathNodeId==30){
                        possibleBackDoMove=5;
                    }
                    else{
                        possibleBackDoMove=pathNodeId-1;
                    }
                }
                else if(pathNodeId>34 && pathNodeId<40){
                    possibleDoMove=((pathNodeId+1)/40)*25+((pathNodeId+1)%40);
                    possibleGaeMove=((pathNodeId+2)/40)*25+((pathNodeId+2)%40);
                    possibleGurlMove=((pathNodeId+3)/40)*25+((pathNodeId+3)%40);
                    possibleYutMove=((pathNodeId+4)/40)*25+((pathNodeId+4)%40);
                    possibleMoMove=((pathNodeId+5)/40)*25+((pathNodeId+5)%40);
                    if(pathNodeId==35){
                        possibleBackDoMove=10;
                    }
                    else{
                        possibleBackDoMove=pathNodeId-1;
                    }
                }
                else if(pathNodeId>39 && pathNodeId<45){
                    possibleDoMove=((pathNodeId+1)/45)*25+((pathNodeId+1)%45);
                    possibleGaeMove=((pathNodeId+2)/45)*25+((pathNodeId+2)%45);
                    possibleGurlMove=((pathNodeId+3)/45)*25+((pathNodeId+3)%45);
                    possibleYutMove=((pathNodeId+4)/45)*25+((pathNodeId+4)%45);
                    possibleMoMove=((pathNodeId+5)/45)*25+((pathNodeId+5)%45);
                    if(pathNodeId==40){
                        possibleBackDoMove=15;
                    }
                    else{
                        possibleBackDoMove=pathNodeId-1;
                    }
                }
                else if(pathNodeId==45){
                    possibleDoMove=pathNodeId+1;
                    possibleGaeMove=pathNodeId+2;
                    possibleGurlMove=pathNodeId+3;
                    possibleYutMove=pathNodeId+4;
                    possibleMoMove=0;
                    possibleBackDoMove=20;
                }
                else{ //일반적인 경우 단순히 윷 결과만큼 더해서 이동
                    possibleDoMove=pathNodeId+1;
                    possibleGaeMove=pathNodeId+2;
                    possibleGurlMove=pathNodeId+3;
                    possibleYutMove=pathNodeId+4;
                    possibleMoMove=pathNodeId+5;
                    possibleBackDoMove=pathNodeId-1;
                    // 일반 길에서 골인지점을 넘어갔을 경우 possibleMove를 50으로 지정해서 골인했음을 표시, 골인지점에 위치할 경우 0으로 변환
                    if(pathNodeId<30 && possibleDoMove>30){
                        possibleDoMove=50;
                    }
                    else if(possibleDoMove==30){
                        possibleDoMove=0;
                    }
                    if(pathNodeId<30 && possibleGaeMove>30){
                        possibleGaeMove=50;
                    }
                    else if(possibleGaeMove==30){
                        possibleGaeMove=0;
                    }
                    if(pathNodeId<30 && possibleGurlMove>30){
                        possibleGurlMove=50;
                    }
                    else if(possibleGurlMove==30){
                        possibleGurlMove=0;
                    }
                    if(pathNodeId<30 && possibleYutMove>30){
                        possibleYutMove=50;
                    }else if(possibleYutMove==30){
                        possibleYutMove=0;
                    }
                    if(pathNodeId<30 && possibleMoMove>30){
                        possibleMoMove=50;
                    }
                    else if(possibleMoMove==30){
                        possibleMoMove=0;
                    }
                }
            }break;
        }
    };

    //빨간 화살표와 빨간 원 설명과 무관하니 무시하자.
    //가장자리는 순서대로 0~19/22/30로 설정하고, 안의 부분은 순서 상관없이 겹치지만 않게 설정하면 충돌은 없다. 분기점에 대해서는 inBranch == 1로 설정하시면 된다. 이부분은 하드코딩 부분이 있어 길어질것 같으니.. 파일을 따로 분리하는 것도 좋을 것 같다.
    //아래 5개의 변수에는 현재 위치에서 도, 개, 걸, 윷, 모가 나왔을 시 이동할 수 있는 위치를 지정한다. 현재 규칙 상 이는 각각 원소의 개수가 하나이므로 int로 선언해도 되지만, 분기점에서의 규칙이 바뀔 경우를 대비하여 어레이로 선언하는 쪽으로 생각했다.
    private int possibleDoMove;
    private int possibleGaeMove;
    private int possibleGurlMove;
    private int possibleYutMove;
    private int possibleMoMove;
    private int possibleBackDoMove;
    public double x_ratio; // x축 위치 비율
    public double y_ratio; // y축 위치 비율

    public int getPossibleDoMove(){
        return possibleDoMove;
    };
    public int getPossibleGaeMove(){
        return possibleGaeMove;
    };
    public int getPossibleGurlMove(){
        return possibleGurlMove;
    };
    public int getPossibleYutMove(){
        return possibleYutMove;
    };
    public int getPossibleMoMove(){
        return possibleMoMove;
    };
    public int getPossibleBackDoMove(){
        return possibleMoMove;
    };

    private int inBranch;   //분기점(갈림길)인지 아닌지를 저장한다. 디폴트값은 0

    public int getInBranch() {
        return inBranch;
    };

    private boolean visible = true;
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }


};