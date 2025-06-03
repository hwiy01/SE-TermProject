package com.seproject.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiceTest {

    // roll() 이 0 또는 1 외 값을 내지 않는지 확인 //
    @Test
    public void rollReturnsOnlyZeroOrOne() {
        Dice d = new Dice();

        for (int i = 0; i < 1_000; i++) {
            int r = d.roll();
            assertTrue("roll() must return 0 or 1, but got " + r,
                       r == 0 || r == 1);
        }
    }
}