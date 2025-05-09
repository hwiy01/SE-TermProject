package com.seproject.model;

public class Player {
    public Player(String nickname, int playerId){
        //닉네임을 이용해 플레이어 클래스를 생성한다.
        this.name=nickname; //입력 받은 이름 설정
        this.playerId=playerId;
    };

    private String name; //인터페이스로 입력 받은 이름이다. 닉네임도 상관없다.
    private int playerId; //유저 아이디이다. 유저를 판별할 때 사용한다.
    
    public String getName(){
        //이름을 반환한다.
        return name;
    };  

    public int getId(){
        //자신의 유저 아이디를 반환한다.
        return playerId;
    }; 
}
