package com.seproject.model;

public class Dice {
    //하나의 윷을 던진다. 앞, 뒤만 반환한다. 둥근 면이 0, 납작한 면이 1이다. (실제 모양을 고려해 직관적으로 설계했다.)
    public int roll(){
        return (int)(Math.random()*2); // 0 또는 1을 리턴
    }
}