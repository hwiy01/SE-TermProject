package com.seproject.model;
import java.util.ArrayList;

public class Piece {
    public Piece(int pieceId, int playerId){
        this.pieceId=pieceId;
        this.playerId=playerId;
        this.currentPathNodeId=0;
        pieces = new ArrayList<>();
        pieces.add(new EachPiece(pieceId, playerId)); //기본 eachPiece 객체 하나 배열에 저장
    }; // 생성자
	private int pieceId;
	private int playerId;
    private int currentPathNodeId; //0으로 초기화

    //동적 배열로 선언하였음
	private ArrayList<EachPiece> pieces; // 기본으로 배열에 EachPiece를 하나씩 갖고, 그룹화가 필요할 때마다 여기에 하나씩 추가된다. 이런 식으로 그룹화에 관한 사항을 내부적으로 구현할 수 있다.}

    public void setPathNodeId(int pathNodeId){
        currentPathNodeId=pathNodeId;
    };
    //이동시킨다. 게임 시작할 때 0으로 이동하고, 게임 하는 도중에도 계속 쓸 예정이다.
    public int getPieceId(){
        return pieceId;
    };
    public int getPlayerId(){
        return playerId;
    };
    public ArrayList<EachPiece> getEachPieces(){
        return pieces;
    }; // 이 그룹에 포함된 말들을 반환하는 getter
    public void groupPiece(Piece group){
        for(EachPiece piece : group.getEachPieces()){
            pieces.add(piece);
        }
    }; //말을 업는 기능 Piece를 인자로 받아서 인자에 포함되어 있는 eachPiece를 전부 현재 객체의 pieces에 추가한다
    //양쪽 Piece 모두 eachPiece를 추가해서 동일한 정보의 객체를 2개 유지할것인지 한쪽에만 추가하고 다른 한쪽은 제거할것인지 고민 필요
    public void separatePieceGroup(){

    }; //piece를 분리하는 기능이 꼭 필요한가? 말이 잡혔을 경우 객체 자체를 제거하고 다시 출발할 때 piece 객체를 생성하면 어떨까?

    public int getCurrentPathNodeId() {
        return currentPathNodeId;
    }

}
