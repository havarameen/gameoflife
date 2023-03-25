package com.havar.gameoflife.model;

import java.util.Random;

/**
 * The model class for Havars Game of Life.
 * 
 * @author Havar Ameen
 *
 */
public class GameOfLifeLogic implements IGameOfLife {
	private int rows, columns;
	private boolean[][] cells;
	private boolean[][] nextIteration;
	private Random random = new Random();

	public GameOfLifeLogic(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.cells = new boolean[rows][columns];
		this.nextIteration = new boolean[rows][columns];
	}

	/**
	 * Generates values for each cell on the board, either true or false. Will
	 * account for grid size.
	 * 
	 */
	@Override
	public void generateRandomBoard() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells[i][j] = random.nextBoolean();
			}
		}
	}

	/**
	 * Defines the next iteration of cells based on the cells of the current cells.
	 * Applies the following logic:
	 * 
	 * 1: Any live cell with two or three live neighbours survives. 
	 * 2: Any dead cell with three live neighbours becomes a live cell. 
	 * 3: All other live cells die in the next generation. Similarly, all other dead cells stay dead.
	 */
	@Override
	public boolean[][] nextIteration() {
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				
				int aliveNeighbors = countNeighbors(i, j);

				if (cells[i][j] == true && (aliveNeighbors == 2 || aliveNeighbors == 3)) { // Rule 1
					nextIteration[i][j] = true;
				} else if (cells[i][j] == false && aliveNeighbors == 3) { // Rule 2
					nextIteration[i][j] = true;
				} else {
					nextIteration[i][j] = false; // Rule 3
				}
			}
		}
		cells = nextIteration;
		nextIteration = new boolean[rows][columns];
		return cells;
	}

	/**
	 * Counts the amount of adjacent cells that are alive. Excludes itself and skips
	 * cells tha would fall out of bounds of the defined table.
	 * 
 	 * [(-1,-1), (-1,0), (-1,1)]
	 * [(0,-1) ,  ( X ),  (0,1)]
	 * [(1,-1) ,  (1,0),  (1,1)]
	 * 
	 * @param rowPosition The row position to check (x-coordinate)
	 * @param colPosition The column position to check (y-coordinate)
	 * @return
	 */
	@Override
	public int countNeighbors(int rowPosition, int colPosition) {
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int r = rowPosition + i;
				int c = colPosition + j;
			
				// Make sure to stay within bounds of the table, don't bother checking outside
				// it.
				if (r >= 0 && r < rows && c >= 0 && c < columns && cells[r][c]) {
					count++;
				}
			}
		}
		if (cells[rowPosition][colPosition]) {
			count--; // Don't include the cell itself
		}
		return count;
	}

	/**
	 * Resizes the grid/board.
	 * 
	 * @param size The row and columnt count in a symmetrical way.
	 */
	@Override
	public void resizeGrid(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive");
		}
		
		if (size == rows) {
			return; 
		}
		
		boolean[][] newCells = new boolean[size][size];
		nextIteration = new boolean[size][size];
		cells = newCells;
		rows = size;
		columns = size;
	}

	@Override
	public boolean[][] getCells() {
		return cells;
	}

	public void setCells(boolean[][] cells) {
		this.cells = cells;
	}

	public void setCellState(int i, int j, boolean b) {
		cells[i][j] = b;		
	}
}
