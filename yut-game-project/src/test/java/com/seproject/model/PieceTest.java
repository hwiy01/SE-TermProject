package com.seproject.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class PieceTest {

    // ---------- 1. 생성자 기본 상태 ---------- //
    @Test
    public void constructorInitialisesFields() {
        Piece p = new Piece(7, 1);

        assertEquals(7, p.getPieceId());
        assertEquals(1, p.getPlayerId());
        assertEquals(-5, p.getCurrentPathNodeId());
        assertFalse (p.selected);

        List<EachPiece> list = p.getEachPieces();
        assertEquals(1, list.size());
        assertEquals(7, list.get(0).getEachPieceId());
        assertEquals(1, list.get(0).getPlayerId());
    }

    // ---------- 2. setPathNodeId 동작 ---------- //
    @Test
    public void setPathNodeIdUpdatesPosition() {
        Piece p = new Piece(0, 0);
        p.setPathNodeId(12);
        assertEquals(12, p.getCurrentPathNodeId());
    }

    // ---------- 3. groupPiece : 중복 없이 병합 ---------- //
    @Test
    public void groupPieceMergesWithoutDuplicates() {
        Piece p1 = new Piece(0, 0);         // 기본 1개
        Piece p2 = new Piece(1, 0);         // 다른 말
        Piece p3 = new Piece(2, 1);         // 다른 플레이어

        p1.groupPiece(p2);
        p1.groupPiece(p3);
        p1.groupPiece(p2);                  // 중복 추가 시도

        List<EachPiece> list = p1.getEachPieces();
        assertEquals("중복 없이 3개만 존재", 3, list.size());

        /* 포함된 ID 집합 검증 */
        Set<Integer> ids = new HashSet<>();
        list.forEach(e -> ids.add(e.getEachPieceId()));
        assertTrue(ids.containsAll(Arrays.asList(0, 1, 2)));
    }

    // ---------- 4. separatePieceGroup 리셋 ---------- //
    @Test
    public void separatePieceGroupResetsState() {
        Piece p = new Piece(5, 2);
        p.groupPiece(new Piece(6, 2));          // 2개로 확장
        assertEquals(2, p.getEachPieces().size());

        p.setPathNodeId(10);
        p.separatePieceGroup();

        /* 그룹이 1개로 줄고, 위치가 -5 로 초기화 */
        assertEquals(1, p.getEachPieces().size());
        assertEquals(-5, p.getCurrentPathNodeId());

        EachPiece only = p.getEachPieces().get(0);
        assertEquals(5, only.getEachPieceId());
        assertEquals(2, only.getPlayerId());
    }
}