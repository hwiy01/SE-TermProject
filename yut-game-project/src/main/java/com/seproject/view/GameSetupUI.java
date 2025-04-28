public class GameSetupUI {
    private String[] nicknameBuffer;
    //닉네임이 겹치는지 미리 확인하기 위해 존재한다.

    public void showMainMenu(){
        //메인 메뉴를 보여준다.
    };

    public int showNumberOfPiecesInEachPlayerChoice(){
        //각각의 플레이어가 몇 개의 말을 사용할 것인지 사용자에게서 입력받는다.   
    }

    public int showNumberOfPlayersChoice(){
        //플레이어가 몇명할지 선택하도록 창을 띄우고 입력받는다
    };

    public int showBoardShapeChoice(){
        //게임 보드를 선택하도록 창을 띄우고 입력받는다
    };


    public String[] showNicknameInput(){
        //이름을 정하는 것을 유저에게 요구한다. 정해진 이름은 nicknameBuffer과 비교해서 겹치는 게 있나 확인한 후 겹치는게 없을 시 버퍼에 저장한다. 입력이 끝나면 지금까지 입력받은 닉네임들을 반환한다.
    };
}
