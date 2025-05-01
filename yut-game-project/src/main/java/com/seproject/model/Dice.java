
import com.seproject.enums.DiceResult;

public class Dice {
   public DiceResult rollDice() { //enum을 사용하도록 수정하였다
    int randomValue = (int) (Math.random() * 5);  // 0~4 랜덤값
    return DiceResult.values()[randomValue];      // 랜덤하게 DO~MO 중 하나 반환
}
}
