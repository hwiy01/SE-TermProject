public class PathNode {
    public PathNode(int pathNodeId, int BoardType){
        //생성자이다. 로케이션 아이디와 게임판 형식을 인자로 받고 그 아이디에 대응하는 정보를 함수가 입력한다. 말을 움직일 때 이는 switch문으로 하나하나 처리되어야 한다. 아래는 게임판 타입 정수 변수가 4일때의 참고용 그림이다.
    };

    //빨간 화살표와 빨간 원 설명과 무관하니 무시하자.
    //가장자리는 순서대로 0~19/22/30로 설정하고, 안의 부분은 순서 상관없이 겹치지만 않게 설정하면 충돌은 없다. 분기점에 대해서는 inBranch == 1로 설정하시면 된다. 이부분은 하드코딩 부분이 있어 길어질것 같으니.. 파일을 따로 분리하는 것도 좋을 것 같다.
    //아래 5개의 변수에는 현재 위치에서 도, 개, 걸, 윷, 모가 나왔을 시 이동할 수 있는 위치를 지정한다. 현재 규칙 상 이는 각각 원소의 개수가 하나이므로 int로 선언해도 되지만, 분기점에서의 규칙이 바뀔 경우를 대비하여 어레이로 선언하는 쪽으로 생각했다.
    private int[] possibleDoMove;
    private int[] possibleGaeMove;
    private int[] possibleGurlMove;
    private int[] possibleYutMove;
    private int[] possibleMoMove;

    public int[] getPossibleDoMove;
    public int[] getPossibleGaeMove;
    public int[] getPossibleGurlMove;
    public int[] getPossibleYutMove;
    public int[] getPossibleMoMove;

    private int inBranch;   //분기점(갈림길)인지 아닌지를 저장한다. 디폴트값은 0

    public int getInBranch() {};

};