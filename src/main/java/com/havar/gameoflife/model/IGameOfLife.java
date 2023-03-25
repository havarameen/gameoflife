package com.havar.gameoflife.model;

/**
 * Interface class for Havars Game of Life. 
 * 
 * @author Havar Ameen
 *
 */
public interface IGameOfLife {
    
    boolean[][] nextIteration();
    int countNeighbors(int rowPosition, int colPosition);
    void resizeGrid(int size);
	boolean[][] getCells();
	void generateRandomBoard();
}