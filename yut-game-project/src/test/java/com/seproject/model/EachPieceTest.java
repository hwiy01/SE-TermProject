package com.seproject.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class EachPieceTest {

    // ---------- 1. 생성자 값 보존 ---------- //
    @Test
    public void constructorStoresIdsCorrectly() {
        EachPiece p = new EachPiece(3, 1);

        assertEquals(3, p.getEachPieceId());
        assertEquals(1, p.getPlayerId());
    }

    // ---------- 2. 서로 다른 인스턴스 독립 ---------- //
    @Test
    public void differentInstancesHoldIndependentValues() {
        EachPiece p1 = new EachPiece(0, 0);
        EachPiece p2 = new EachPiece(1, 0);
        EachPiece p3 = new EachPiece(0, 2);

        assertNotSame(p1, p2);                 // 물리적 다른 객체
        assertEquals(0, p1.getEachPieceId());
        assertEquals(1, p2.getEachPieceId());
        assertEquals(0, p3.getEachPieceId());

        assertEquals(0, p1.getPlayerId());
        assertEquals(0, p2.getPlayerId());
        assertEquals(2, p3.getPlayerId());
    }
}