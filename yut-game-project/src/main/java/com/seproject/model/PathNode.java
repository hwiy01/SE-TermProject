public class PathNode {
    public PathNode(int pathNodeId, int BoardType){
        //생성자이다. 로케이션 아이디와 게임판 형식을 인자로 받고 그 아이디에 대응하는 정보를 함수가 입력한다. 말을 움직일 때 이는 switch문으로 하나하나 처리되어야 한다. 아래는 게임판 타입 정수 변수가 4일때의 참고용 그림이다.
        switch(BoardType){
            case 4:{
                if(pathNodeId==5 || pathNodeId==10){
                    inBranch=1;
                }
                else{
                    inBranch=0;
                }
                if(inBranch==1){
                    possibleDoMove=pathNodeId+15;
                    possibleGaeMove=pathNodeId+16;
                    possibleGurlMove=pathNodeId+17; //22번과 27번은 같은 pathNode임
                    possibleYutMove=pathNodeId+18;
                    possibleMoMove=pathNodeId+19;
                    possibleBackDoMove=pathNodeId-1;
                }
                else if(pathNodeId==22){
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
                else{ //일반적인 경우 단순히 윷 결과만큼 더해서 이동
                    possibleDoMove=pathNodeId+1;
                    possibleGaeMove=pathNodeId+2;
                    possibleGurlMove=pathNodeId+3;
                    possibleYutMove=pathNodeId+4;
                    possibleDoMove=pathNodeId+5;
                    possibleBackDoMove=pathNodeId-1;
                }
            } break;
            case 5:{
                //5각형의 경우
            } break;
            case 6:{
                //6각형의 경우
            }break;
        }
    };

    //빨간 화살표와 빨간 원 설명과 무관하니 무시하자.
    //가장자리는 순서대로 0~19/22/30로 설정하고, 안의 부분은 순서 상관없이 겹치지만 않게 설정하면 충돌은 없다. 분기점에 대해서는 inBranch == 1로 설정하시면 된다. 이부분은 하드코딩 부분이 있어 길어질것 같으니.. 파일을 따로 분리하는 것도 좋을 것 같다.
    //아래 5개의 변수에는 현재 위치에서 도, 개, 걸, 윷, 모가 나왔을 시 이동할 수 있는 위치를 지정한다. 현재 규칙 상 이는 각각 원소의 개수가 하나이므로 int로 선언해도 되지만, 분기점에서의 규칙이 바뀔 경우를 대비하여 어레이로 선언하는 쪽으로 생각했다.
    private int[] possibleDoMove;
    private int[] possibleGaeMove;
    private int[] possibleGurlMove;
    private int[] possibleYutMove;
    private int[] possibleMoMove;
    private int[] possibleBackDoMove;

    public int[] getPossibleDoMove;
    public int[] getPossibleGaeMove;
    public int[] getPossibleGurlMove;
    public int[] getPossibleYutMove;
    public int[] getPossibleMoMove;
    public int[] getPossibleBackDoMove;

    private int inBranch;   //분기점(갈림길)인지 아닌지를 저장한다. 디폴트값은 0

    public int getInBranch() {
        return inBranch;
    };

};