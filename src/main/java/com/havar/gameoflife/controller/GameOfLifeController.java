package com.havar.gameoflife.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.havar.gameoflife.model.IGameOfLife;
import com.havar.gameoflife.model.countstratgegies.CountNeighborsWithBordersStrategy;
import com.havar.gameoflife.model.countstratgegies.CountNeighborsWithWrapAroundStrategy;
import com.havar.gameoflife.view.GameOfLifeView;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
	private ExecutorService executor;
	private long delay = 100;
	private boolean paused = false;
	private volatile boolean running;
	Task<Void> simulationTask;

	public GameOfLifeController(IGameOfLife model, GameOfLifeView view) {
		this.model = model;
		this.view = view;
		model.generateRandomBoard();
		this.executor = Executors.newSingleThreadExecutor();

		setGameOfLifeGridMouseListeners();
		setGameOfLifeControllerListeners();
	}

	public void execute() {
		if (executor == null || executor.isShutdown()) {
			executor = Executors.newSingleThreadScheduledExecutor();
		}

		if (!running) {
			running = true;
			startSimulation();
		}
	}

	public void stop() {
		running = false;
		executor.shutdownNow();
	}

	/**
	 * Initiate thread to run the simulation using JavaFX concurrent Task. Other
	 * options
	 * 
	 */
	private void startSimulation() {
		Task<Void> simulationTask = new Task<>() {
			@Override
			protected Void call() throws Exception {
				while (running) {
					if (!paused) {
						model.nextIteration();
						updateView();
					}
					Thread.sleep(delay);
				}
				return null;
			}
		};
		simulationTask.setOnSucceeded(e -> {
			executor.shutdown();
			running = false;
		});
		executor.submit(simulationTask);
	}

	private void updateView() {
		boolean[][] cells = model.getCells();
		Platform.runLater(() -> view.updateBoard(cells));
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
			execute();
		});

		view.getStopButton().setOnAction(event -> {
			stop();
		});

		view.getSizeSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!view.getSizeSlider().isValueChanging()) {
				if (newValue != oldValue) {
					regenerateBoard();
				}
			}
		});

		view.getDelaySpinner().valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != oldValue) {
				delay = newValue;
				stop();
				execute();
			}
		});

		view.getClearButton().setOnAction(event -> {
			model.clearBoard();
		});

		view.getRegenButton().setOnAction(event -> {
			regenerateBoard();
		});

		view.getWrapAroundBox().setOnAction(event -> {
			if (view.getWrapAroundBox().isSelected()) {
				System.out.println("Wrao aropnd");
				model.setNeighborCountingStrategy(new CountNeighborsWithWrapAroundStrategy());
			} else {
				System.out.println("NOPE aropnd");
				model.setNeighborCountingStrategy(new CountNeighborsWithBordersStrategy());
			}
		});
	}
}
