public class EachPiece {
    public EachPiece(int eachPieceId, int palyerId) {
        this.eachPieceId=eachPieceId;
        this.playerId=playerId;
    };
   
    private int eachPieceId; //말 별로 식별이 가능하게 하도록 하는 아이디이다. 디버깅에도 쓰일 수 있고 다른 기능들에서도 필요할 시 사용할 수 있다.
    private int playerId;

    public int getPlayerId(){
        // playerId getter 함수, 플레이어 별로 말의 색을 구분하기 위해 필요하다.
        return playerId;
    };
    
    public int getEachPieceId(){
        //위치 아이디이다. 현재 어디에 있는지를 알려주며, startGame이 호출될 때 0으로 초기화된다. 자동으로 처음으로 날라가는 것.
        return eachPieceId;
    };
}
