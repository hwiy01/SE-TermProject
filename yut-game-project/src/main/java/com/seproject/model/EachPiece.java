package com.seproject.model;

public class EachPiece {
    public EachPiece(int eachPieceId, int palyerId) {
        this.eachPieceId=eachPieceId;
        this.playerId=playerId;
    };
   
    private int eachPieceId; //말 별로 식별이 가능하게 하도록 하는 아이디이다. 디버깅에도 쓰일 수 있고 다른 기능들에서도 필요할 시 사용할 수 있다.
    private int playerId;

    public int getPlayerId(){
        return playerId;
    };

    //업기에서 어떤 말인지 확인하는 용도
    public int getEachPieceId(){
        return eachPieceId;
    };
}
