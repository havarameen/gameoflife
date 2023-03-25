package com.havar.gameoflife.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the GameOfLifeLogic class.
 */
class GameOfLifeLogicTest {

    @Test
    void testGenerateRandomBoard() {
        // Check that board is generated with random values, chances of this not happening is 1 in 100 000 000.  
    	GameOfLifeLogic gameOfLife = new GameOfLifeLogic(10000, 10000);
    	gameOfLife.generateRandomBoard();
        boolean[][] cells = gameOfLife.getCells();
        boolean anyTrue = false;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j]) {
                    anyTrue = true;
                }
            }
        }
        assertTrue(anyTrue);
    }

    /**
     * Empty board should stay empty after an iteration.
     * 
     */
    @Test
    public void testNextIterationWithAllDeadCells() {
        int rows = 10;
        int columns = 10;
        GameOfLifeLogic gameOfLife = new GameOfLifeLogic(rows, columns);
        boolean[][] expectedNextIteration = new boolean[rows][columns];
        boolean[][] actualNextIteration = gameOfLife.nextIteration();
        assertArrayEquals(expectedNextIteration, actualNextIteration);
    }

    /**
     * Test case with a completely filled/alive board. 
     * 
     */
    @Test
    public void testNextIterationWithOnlyAliveNeighbors() {
        int rows = 10;
        int columns = 10;
        GameOfLifeLogic gameOfLife = new GameOfLifeLogic(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameOfLife.setCellState(i, j, true);
            }
        }
        
        // Only corner cells should be alive, we're not doing a wrap around, so these should be the ones with 3 active neighbours on iteration 1. 
        boolean[][] expectedNextIteration = new boolean[rows][columns];
        expectedNextIteration[0][0] = true;
        expectedNextIteration[9][0] = true;
        expectedNextIteration[9][9] = true;
        expectedNextIteration[0][9] = true;
        
        boolean[][] actualNextIteration = gameOfLife.nextIteration();
        assertArrayEquals(expectedNextIteration, actualNextIteration);
    }

    // Check for the rules of Conway's game of life.

    /**
     * 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     * @see https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life 
     */
    @Test
    public void testLiveCellWithFewerThanTwoLiveNeighbors() {
        GameOfLifeLogic gameOflife = new GameOfLifeLogic(10, 10);
        boolean[][] cells = new boolean[][] {
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true,  false, false, false, false, false, false},
            {false, false, false, true,  false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        gameOflife.setCells(cells);
        int count = gameOflife.countNeighbors(1, 3);
        assertEquals(1, count);
        
        cells = gameOflife.nextIteration();
        count = gameOflife.countNeighbors(1, 3);
        assertEquals(0, count);
        assertEquals(false, cells[1][3]);
    }
    
    /**
     * 2. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     * @see https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life 
     */
    @Test
    public void testLiveCellWithToOrThreeLiveNeighbors() {
    	// Check that cell lives with two live neighbors
    	GameOfLifeLogic gameOflife = new GameOfLifeLogic(10, 10);
        boolean[][] cells = new boolean[][] {
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true,  false, false, false, false, false, false},
            {false, false, true,  true,  false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        
        gameOflife.setCells(cells);
        int count = gameOflife.countNeighbors(1, 3);
        assertEquals(2, count);
      
        cells = gameOflife.nextIteration();
        assertEquals(true, cells[1][3]);
        assertEquals(2, count);
        
    	// Check that cell lives with three live neighbors
        cells = new boolean[][] {
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true,  false, false, false, false, false, false},
            {false, false, true,  true,  true,  false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        
        gameOflife.setCells(cells);
        count = gameOflife.countNeighbors(1, 3);
        assertEquals(3, count);
      
        cells = gameOflife.nextIteration();
        assertEquals(true, cells[1][3]);
        assertEquals(3, count);
    }
    
    /**
     * 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
     * @see https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life 
     */
    @Test
    public void testLiveCellWithMoreThanThreeLiveNeighbors() {
        GameOfLifeLogic gameOflife = new GameOfLifeLogic(10, 10);
        boolean[][] cells = new boolean[][] {
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true,  true, false, false, false, false, false},
            {false, false, true,  true,  true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        
        gameOflife.setCells(cells);
        int count = gameOflife.countNeighbors(1, 3);
        assertEquals(4, count);
        assertEquals(true, cells[1][3]);

        cells = gameOflife.nextIteration();
        count = gameOflife.countNeighbors(1, 3);
        
        assertEquals(false, cells[1][3]);
        assertEquals(4, count);
    }
    
    /**
     *  4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     */
    @Test
    public void testDeadCellWithThreeLiveNeighbors() {
        GameOfLifeLogic gameOflife = new GameOfLifeLogic(10, 10);
        boolean[][] cells = new boolean[][] {
            {false, false, false, true,  false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, true,  false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        
        gameOflife.setCells(cells);
        int count = gameOflife.countNeighbors(1, 3);
        assertEquals(3, count);
        assertEquals(false, cells[1][3]);

        cells = gameOflife.nextIteration();
        count = gameOflife.countNeighbors(1, 3);
        assertEquals(0, count);
        assertEquals(true, cells[1][3]);
    }
    
    /**
     *  Try oscilation pattern - Is it alive as expected after 10 iterations? 
     */
    @Test
    public void testRepeatingPatternOscilation() {
        GameOfLifeLogic gameOflife = new GameOfLifeLogic(10, 10);
        boolean[][] cells = new boolean[][] {
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, true,  true , true,  false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false}
        };
        
        // 10 iterations
        for(int i = 0; 10 < i; i++) {
            int count = gameOflife.countNeighbors(1, 3);
            assertEquals(2, count);
            assertEquals(true, cells[1][3]);
        }
    }
    
	/**
	 * Try a pattern that would eventually cross over the edge. It should mean that
	 * the all cells on that board should be empty eventually, or atleast the cells we're checking. 
	 */
	@Test
	public void testEdgeOfBorder() {
		  // Create an initial state with a glider that moves towards the edge
	    boolean[][] cells = new boolean[][] {
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, true,  false, false, false, false, false, false},
	        {false, false, false, false, true,  false, false, false, false, false},
	        {false, false, true,  true,  true,  false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false}
	    };

	    // Initialize the game of life with the initial state
	    GameOfLifeLogic gameOflife = new GameOfLifeLogic(10, 10);
	    gameOflife.setCells(cells);

	    // Simulate many iterations, by any normal means it should have moved outside the board.
	    for(int i = 0; i < 50; i++) {
	    	cells = gameOflife.nextIteration();
	    }
	    
	    // Since glider should move towards bottom right it should end up occupying the bottom right 4 squares, creating a square that is going to remain there no matter how many iterations we do! 
	    cells = gameOflife.nextIteration();
	    assertTrue(cells[9][9]); 
	    assertTrue(cells[8][9]); 
	    assertTrue(cells[9][8]); 
	    assertTrue(cells[8][8]);  
	    
	    for(int i = 0; i < 17; i++) {
	    	cells = gameOflife.nextIteration();
	    }
	    
	    // Make sure after another 17 iterations. 
	    cells = gameOflife.nextIteration();
	    assertTrue(cells[9][9]); 
	    assertTrue(cells[8][9]); 
	    assertTrue(cells[9][8]); 
	    assertTrue(cells[8][8]);
	}
}