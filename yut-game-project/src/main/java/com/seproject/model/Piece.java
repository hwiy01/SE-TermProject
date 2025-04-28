public class Piece {
    public Piece(int playerId){}; // 생성자
	private int pieceId;
	private int playerId;
    private int currentPathNodeId; //0으로 초기화

	private EachPiece[] pieces; // 기본으로 배열에 EachPiece를 하나씩 갖고, 그룹화가 필요할 때마다 여기에 하나씩 추가된다. 이런 식으로 그룹화에 관한 사항을 내부적으로 구현할 수 있다.}
    public void setPathNodeId(int pathNodeId){};
    //이동시킨다. 게임 시작할 때 0으로 이동하고, 게임 하는 도중에도 계속 쓸 예정이다.
    public int getPieceId;
    public int getPlayerId;
    public EachPiece[] getEachPieces(){}; // 이 그룹에 포함된 말들을 반환하는 getter
    public separatePieceGroup(){};
}
