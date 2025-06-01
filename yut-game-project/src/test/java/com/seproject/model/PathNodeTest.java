package com.seproject.model;

import static org.junit.Assert.*;

import org.junit.Test;

 // “모든 노드”를 일일이 검사하기엔 코드량이 너무 과도하므로
 // 의미가 다른 대표 노드를 골라 4·5·6각형별 핵심 규칙을 검증합니다.

public class PathNodeTest {

    // ============================================================
    //                       사   각   형 (BoardType = 4)
    // ============================================================ 

    // 0번 노드: 출발/도착
    @Test
    public void squareNode0_properties() {
        PathNode n0 = new PathNode(0, 4);

        // “교차점” 플래그 검증 (0번은 꼭짓점이므로 cross = true)
        assertTrue("squareNode0: should be a crossing node", n0.isCross());

        // possibleDoMove: 원래 30 → 30 이상인 경우 100으로 변환
        assertEquals("squareNode0: getPossibleDoMove() → 100", 100, n0.getPossibleDoMove());

        // possibleBackDoMove: 정의된 대로 19
        assertEquals("squareNode0: getPossibleBackDoMove() → 19", 19, n0.getPossibleBackDoMove());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("squareNode0: x_ratio in [0,1]", inUnitRange(n0.x_ratio));
        assertTrue("squareNode0: y_ratio in [0,1]", inUnitRange(n0.y_ratio));
    }

    // 5번 노드: 분기점(Branch) & 교차점(Cross)
    @Test
    public void squareNode5_isBranchAndCross() {
        PathNode n5 = new PathNode(5, 4);

        // inBranch 플래그: 5번은 분기점이므로 1
        assertEquals("squareNode5: inBranch → 1", 1, n5.getInBranch());

        // possibleBackDoMove: 분기점 공식에 따라 pathNodeId - 1 = 4
        assertEquals("squareNode5: getPossibleBackDoMove() → 4", 4, n5.getPossibleBackDoMove());

        // 교차점인지 확인 (5번은 꼭짓점이므로 cross = true)
        assertTrue("squareNode5: should be a crossing node", n5.isCross());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("squareNode5: x_ratio in [0,1]", inUnitRange(n5.x_ratio));
        assertTrue("squareNode5: y_ratio in [0,1]", inUnitRange(n5.y_ratio));
    }

    // 18번 노드: 일반 노드 + “골인” 처리(20 초과 시 100으로 변환) 
    @Test
    public void squareNode18_longMovesBecomeGoal() {
        PathNode n18 = new PathNode(18, 4);

        // possibleGaeMove: 18 + 2 = 20 → 20이면 “출발점”인 0으로 변환
        assertEquals("squareNode18: getPossibleGaeMove() → 0", 0, n18.getPossibleGaeMove());

        // possibleYutMove: 18 + 4 = 22 → 22 > 20 이므로 일단 30, 그 후 30 이상은 100으로 변환
        assertEquals("squareNode18: getPossibleYutMove() → 100", 100, n18.getPossibleYutMove());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("squareNode18: x_ratio in [0,1]", inUnitRange(n18.x_ratio));
        assertTrue("squareNode18: y_ratio in [0,1]", inUnitRange(n18.y_ratio));
    }

    // 22번 노드: 특수 분기점(Inner Branch) 처리 
    @Test
    public void squareNode22_innerBranchValues() {
        PathNode n22 = new PathNode(22, 4);

        // 분기점 공식: possibleDoMove = 28, possibleGaeMove = 29
        assertEquals("squareNode22: getPossibleDoMove() → 28", 28, n22.getPossibleDoMove());
        assertEquals("squareNode22: getPossibleGaeMove() → 29", 29, n22.getPossibleGaeMove());

        // possibleGurlMove: 22번 전용 공식에 의해 0 (출발점)
        assertEquals("squareNode22: getPossibleGurlMove() → 0", 0, n22.getPossibleGurlMove());

        // possibleYutMove: 22 + 4 = 26 → 26 > 20이므로 30 → 100 변환
        assertEquals("squareNode22: getPossibleYutMove() → 100", 100, n22.getPossibleYutMove());

        // possibleMoMove: 22 + 5 = 27 → 27 > 20이므로 30 → 100 변환
        assertEquals("squareNode22: getPossibleMoMove() → 100", 100, n22.getPossibleMoMove());

        // possibleBackDoMove: 21
        assertEquals("squareNode22: getPossibleBackDoMove() → 21", 21, n22.getPossibleBackDoMove());

        // 교차점 여부: 22번은 꼭짓점이므로 cross = true
        assertTrue("squareNode22: should be a crossing node", n22.isCross());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("squareNode22: x_ratio in [0,1]", inUnitRange(n22.x_ratio));
        assertTrue("squareNode22: y_ratio in [0,1]", inUnitRange(n22.y_ratio));
    }

    // 27번 노드: 중앙 “겹침” 노드 → visible=false 
    @Test
    public void squareNode27_isInvisible() {
        PathNode n27 = new PathNode(27, 4);
        assertFalse("squareNode27: should be invisible", n27.isVisible());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("squareNode27: x_ratio in [0,1]", inUnitRange(n27.x_ratio));
        assertTrue("squareNode27: y_ratio in [0,1]", inUnitRange(n27.y_ratio));
    }


    // ============================================================
    //                       오   각   형 (BoardType = 5)
    // ============================================================

    // 10번 노드: 분기점(Branch) & 교차점 여부 확인
    @Test
    public void pentagonNode10_branchAndCross() {
        PathNode n10 = new PathNode(10, 5);

        // 분기점: 10번은 5,10,15에 해당 → inBranch = 1
        assertEquals("pentagonNode10: inBranch → 1", 1, n10.getInBranch());

        // possibleDoMove: 10 + 20 = 30 (40 초과만 100으로 변환되므로, 30 그대로)
        assertEquals("pentagonNode10: getPossibleDoMove() → 30", 30, n10.getPossibleDoMove());

        // possibleBackDoMove: 9
        assertEquals("pentagonNode10: getPossibleBackDoMove() → 9", 9, n10.getPossibleBackDoMove());

        // 교차점 여부: 10번은 cross = true
        assertTrue("pentagonNode10: should be a crossing node", n10.isCross());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("pentagonNode10: x_ratio in [0,1]", inUnitRange(n10.x_ratio));
        assertTrue("pentagonNode10: y_ratio in [0,1]", inUnitRange(n10.y_ratio));
    }

    // 32번 노드: 중앙 “겹침” 노드였으나, 실제 구현 상 isVisible = true, isCross = false
    @Test
    public void pentagonNode32_visibleAndCrossFlags() {
        PathNode n32 = new PathNode(32, 5);

        // 구현 상 32번 노드는 invisible 대상으로 처리되지 않음 → visible = true
        assertTrue("pentagonNode32: should be visible", n32.isVisible());

        // “교차점” 플래그: 32번은 cross 목록에 포함되지 않으므로 cross = false
        assertFalse("pentagonNode32: should not be a crossing node", n32.isCross());

        // possibleGurlMove: 0 (걸 → 출발점)
        assertEquals("pentagonNode32: getPossibleGurlMove() → 0", 0, n32.getPossibleGurlMove());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("pentagonNode32: x_ratio in [0,1]", inUnitRange(n32.x_ratio));
        assertTrue("pentagonNode32: y_ratio in [0,1]", inUnitRange(n32.y_ratio));
    }


    // ============================================================
    //                       육   각   형 (BoardType = 6)
    // ============================================================ 

    // 25번 노드: 분기 전 일반 노드, “골인” 임계값 30 사용 
    @Test
    public void hexagonNode25_basicMoves() {
        PathNode n25 = new PathNode(25, 6);

        // possibleDoMove: 25 + 1 = 26 (30 초과만 50 이상 시 100으로 변환, 26 그대로)
        assertEquals("hexagonNode25: getPossibleDoMove() → 26", 26, n25.getPossibleDoMove());

        // possibleBackDoMove: 24
        assertEquals("hexagonNode25: getPossibleBackDoMove() → 24", 24, n25.getPossibleBackDoMove());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("hexagonNode25: x_ratio in [0,1]", inUnitRange(n25.x_ratio));
        assertTrue("hexagonNode25: y_ratio in [0,1]", inUnitRange(n25.y_ratio));
    }
    
    // 49번 노드: '개'(Gae)로 골인(Goal) 임계값 50 사용 
    @Test
    public void hexagonNode49_gaeBecomesGoal() {
        PathNode n49 = new PathNode(49, 6);

        // 개(Gae) 이동: 49 + 2 = 51 → 50 이상이므로 100으로 변환
        assertEquals("hexagonNode49: getPossibleGaeMove() → 100 (골인)", 100, n49.getPossibleGaeMove());

        // 되돌리기(BackDo) 이동: 49 - 1 = 48
        assertEquals("hexagonNode49: getPossibleBackDoMove() → 48", 48, n49.getPossibleBackDoMove());
        
        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("hexagonNode49: x_ratio in [0,1]", inUnitRange(n49.x_ratio));
        assertTrue("hexagonNode49: y_ratio in [0,1]", inUnitRange(n49.y_ratio));
    }

    // 32번 노드: 중앙 “겹침” & “교차점” 처리 
    @Test
    public void hexagonNode32_invisibleAndCross() {
        PathNode n32 = new PathNode(32, 6);

        // 구현 상 32번은 invisible 처리
        assertFalse("hexagonNode32: should be invisible", n32.isVisible());

        // 교차점 플래그: 32번은 cross = false
        assertFalse("hexagonNode32: should be a crossing node", n32.isCross());

        // possibleGurlMove: 걸(3) → 출발점(0)
        assertEquals("hexagonNode32: getPossibleGurlMove() → 0", 0, n32.getPossibleGurlMove());

        // x_ratio, y_ratio는 [0.0, 1.0] 범위 안에 있어야 한다
        assertTrue("hexagonNode32: x_ratio in [0,1]", inUnitRange(n32.x_ratio));
        assertTrue("hexagonNode32: y_ratio in [0,1]", inUnitRange(n32.y_ratio));
    }


    // ============================================================
    //                          공  통
    // ============================================================ 

    // x_ratio, y_ratio는 0 ≤ r ≤ 1 안에 있어야 한다 
    private static boolean inUnitRange(double r) {
        return r >= 0.0 && r <= 1.0;
    }
}