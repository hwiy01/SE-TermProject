package com.seproject.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {

    // ---------- 1. 배열 길이 검증 ---------- //

    @Test
    public void squareBoardHas30Nodes() {
        Board board = new Board(4);
        assertNotNull(board.getPathNodes());
        assertEquals(30, board.getPathNodes().length);
    }

    @Test
    public void pentagonBoardHas40Nodes() {
        Board board = new Board(5);
        assertNotNull(board.getPathNodes());
        assertEquals(40, board.getPathNodes().length);
    }

    @Test
    public void hexagonBoardHas50Nodes() {
        Board board = new Board(6);
        assertNotNull(board.getPathNodes());
        assertEquals(50, board.getPathNodes().length);
    }

    // ---------- 2. 각 노드가 null 이 아님 ---------- //

    @Test
    public void everySquareNodeIsInitialised() {
        Board board = new Board(4);
        for (int i = 0; i < board.getPathNodes().length; i++) {
            assertNotNull("node[" + i + "] 는 null 이면 안 됨",
                          board.getPathNodes()[i]);
        }
    }

    // ---------- 3. 지원하지 않는 모양 처리 ---------- //

    @Test
    public void unsupportedShapeCreatesNullArray() {
        Board board = new Board(7);
        assertNull("shape=7 은 현재 사양에서 지원하지 않으므로 pathNodes 가 null 이어야 함",
                   board.getPathNodes());
    }
}