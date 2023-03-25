package com.havar.gameoflife.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.havar.gameoflife.model.GameOfLifeLogic;
import com.havar.gameoflife.model.IGameOfLife;
import com.havar.gameoflife.view.GameOfLifeView;

import javafx.scene.input.MouseButton;

/**
 * The controller class for the Game of Life.
 * 
 * @author Havar Ameen
 *
 */
public class GameOfLifeController {
	private IGameOfLife model;
	private GameOfLifeView view;
	private ScheduledExecutorService executor;
	private long delay = 1000;
	private boolean paused = false;
	private boolean resizeCalled = false;

	public GameOfLifeController(IGameOfLife model, GameOfLifeView view) {
		this.model = model;
		this.view = view;
		model.generateRandomBoard();
		
		setGameOfLifeGridMouseListeners();
		setGameOfLifeControllerListeners();
	}

	public void execute() {
		if (executor == null || executor.isTerminated()) {
			executor = Executors.newSingleThreadScheduledExecutor();
		}
		executor.scheduleAtFixedRate(this::runGameOfLife, 0, delay, TimeUnit.MILLISECONDS);
	}

	private void runGameOfLife() {
		if (!paused) {
			view.updateBoard(model.nextIteration());
		}
		
		if(resizeCalled) {
			
		}
	}

	private void regenerateBoard() {
		stop();
		int newSize = view.getSizeSlider().valueProperty().intValue();
		model.resizeGrid(newSize);
		model.generateRandomBoard();
		view.resizeGrid(newSize);
		setGameOfLifeGridMouseListeners();
		execute();
	}

	/**
	 * Shuts down the executor and the thread running assigned to calling the game
	 * of life iterations.
	 * 
	 */
	public void stop() {
		executor.shutdown();
	}

	/**
	 * Pauses current iteration of Game of Life, but keeps thread going still.
	 */
	public void pause() {
		paused = true;
	}

	/**
	 * Resumes iterating new generations of Game of Life.
	 */
	public void resume() {
		paused = false;
	}

	private void setGameOfLifeGridMouseListeners() {
		view.getGridPane().setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown() || event.isSecondaryButtonDown()) {
				pause();
			}
		});

		view.getGridPane().setOnMouseReleased(event -> {
			if (!event.isPrimaryButtonDown() && !event.isSecondaryButtonDown()) {
				resume();
			}
		});

		view.getGridPane().setOnMouseClicked(event -> {
			int row = (int) (event.getX() / view.getCellSize());
			int col = (int) (event.getY() / view.getCellSize());
			if (row >= 0 && row < view.getGridSize() && col >= 0 && col < view.getGridSize()) {
				if (event.getButton() == MouseButton.PRIMARY) {
					model.getCells()[row][col] = true;
					view.updateCell(row, col, true);
				} else if (event.getButton() == MouseButton.SECONDARY) {
					model.getCells()[row][col] = false;
					view.updateCell(row, col, false);
				}
			}
		});

		view.getGridPane().setOnMouseDragged(event -> {
			int row = (int) (event.getX() / view.getCellSize());
			int col = (int) (event.getY() / view.getCellSize());
			if (row >= 0 && row < view.getGridSize() && col >= 0 && col < view.getGridSize()) {
				if (event.getButton() == MouseButton.PRIMARY) {
					model.getCells()[row][col] = true;
					view.updateCell(row, col, true);
				} else if (event.getButton() == MouseButton.SECONDARY) {
					model.getCells()[row][col] = false;
					view.updateCell(row, col, false);
				}
			}
		});
	}

	private void setGameOfLifeControllerListeners() {
		view.getStartButton().setOnAction(event -> {
			resume();
		});

		view.getStopButton().setOnAction(event -> {
			pause();
		});

		view.getSizeSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!view.getSizeSlider().isValueChanging()) {
				if (newValue != oldValue) {
					regenerateBoard();
				}
			}
		});
	}
}
