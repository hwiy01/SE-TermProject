package com.seproject.model;

public class Board {
    private PathNode[] pathNodes;
    private int numberOfShape; //모양 정보를 저장한다. 

    public int getNumberOfShape(){
        return numberOfShape;
    };

    public Board(int numberOfShape){
        //생성자이다. N을 변수로 받고 numberOfShape형 게임판을 생성한다. N의 개수는 현재 2025.04.20 기준으로 4, 5, 6이다. 요구 사항이 바뀔 때마다 바뀔 가능성이 있기에 날짜를 써 놓았다. 아래는 어레이 길이 예시이다.
        //여기서 numberOfShape 에 따라 PathNode 배열 수를 다르게 생성한다.

        // 4각형 -> PathNode 29자리 배열
        // 5각형 -> PathNode 33자리 배열
        // 6각형 -> PathNode 43자리 배열
        switch(numberOfShape){
            case 4: {
                pathNodes=new PathNode[30]; //가운데 중복 인덱스로 30자리 배열로 선언하였음
                for(int i=0; i<30; i++){
                    pathNodes[i]=new PathNode(i, 4);
                }
            } break;
            case 5: {
                pathNodes=new PathNode[40];
                for(int i=0; i<40; i++){
                    pathNodes[i]=new PathNode(i, 5);
                }
            } break;
            case 6: {
                pathNodes=new PathNode[50];
                for(int i=0; i<50; i++){
                    pathNodes[i]=new PathNode(i, 6);
                }
            } break;
        }
    };

    public PathNode[] getPathNodes() {
        return pathNodes;
    }
}

