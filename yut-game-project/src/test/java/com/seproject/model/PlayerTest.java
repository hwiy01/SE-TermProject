package com.seproject.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

    // ---------- 1. 생성자 값 보존 ---------- //
    @Test
    public void constructorStoresNameAndId() {
        Player p = new Player("Alice", 0);

        assertEquals("Alice", p.getName());
        assertEquals(0,       p.getId());
    }

    // ---------- 2. 서로 다른 인스턴스 ---------- //
    @Test
    public void differentPlayersHoldIndependentValues() {
        Player p1 = new Player("Bob",   1);
        Player p2 = new Player("Chris", 2);

        assertNotSame(p1, p2);
        assertEquals("Bob",   p1.getName());
        assertEquals("Chris", p2.getName());
        assertEquals(1, p1.getId());
        assertEquals(2, p2.getId());
    }
}