
import com.seproject.enums.DiceResult;

public class GamePlayUI {
    public void updateBoard(Board gameBoard){
        //현재 눈에 보이는 게임판의 상태를 내부 시스템 상태에 맞게 업데이트한다.
    };

    public void showDiceResult(DiceResult result){
        //윷을 던진 결과를 시각화한다.
    };

    public void showWinner(Player player){
        //승리한 플레이어를(개인전이므로) 화면에 출력한다.
    };

    public int showWhichResultToUse(DiceResult[] outcomes){
        //윷이나 모가 나와서 여러 번 던질 시에 관한 예외 처리이다. Outcomes 중 어떤 것을 선택해서 움직일지 플레이어로부터 입력을 받는다. 선택된 length(도라면 1)를 반환한다.
    };

    public Piece showWhichPieceToMove(){
        //게임 중에 무슨 말을 움직일 건지 입력받는다. 실제로 가능한가는 gameManager안에서만 확인할 수 있다.
    };
}
