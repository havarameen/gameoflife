package com.havar.gameoflife.model;

import com.havar.gameoflife.model.countstratgegies.NeighborCountingStrategy;

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
	void clearBoard();
	void setNeighborCountingStrategy(NeighborCountingStrategy strategy);
}