package com.havar.gameoflife.model.countstratgegies;

public class CountNeighborsWithWrapAroundStrategy implements NeighborCountingStrategy {
	 @Override
	    public int countNeighbors(int rowPosition, int colPosition, boolean[][] cells, int rows, int columns) {
	        int count = 0;
	        for (int i = -1; i <= 1; i++) {
	            for (int j = -1; j <= 1; j++) {
	                int r = (rowPosition + i + rows) % rows;
	                int c = (colPosition + j + columns) % columns;

	                if (cells[r][c]) {
	                    count++;
	                }
	            }
	        }
	        if (cells[rowPosition][colPosition]) {
	            count--; // Don't include the cell itself
	        }
	        return count;
	    }
}
