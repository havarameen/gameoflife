package com.havar.gameoflife.view;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The view class for the Game of Life.
 * 
 * @author Havar Ameen
 */
public class GameOfLifeView extends AnchorPane {
	private final int DEFAULT_GRID_SIZE = 100;
	private final double DEFAULT_GRID_WIDTH = 1000.0;
	private final Color ALIVE_COLOR = Color.web("#00abf4");
	private final Color DEAD_COLOR = Color.web("#072232");

	private GridPane gridPane;
	private int currentGridSize;
	private double cellSize;
	private Slider sizeSlider;
    private Spinner<Integer> delaySpinner;

	private Label sizeLabel = new Label("Grid size");
	private Label delayLabel = new Label("Delay (ms)");
	private AnchorPane controlPane = new AnchorPane();
	private Button startButton = new Button("Start");
	private Button stopButton = new Button("Stop");
	private Button clearButton = new Button("Clear grid");
	private Button regenButton = new Button("Regenerate grid");
	
	private boolean resizing = false;

	/**
	 * Creates a new GameOfLifeView object with the given cells.
	 *
	 * @param cells the cells to display
	 */
	public GameOfLifeView() {
		this.currentGridSize = DEFAULT_GRID_SIZE;
		this.cellSize = DEFAULT_GRID_WIDTH / currentGridSize; // Using a 1000x1000 pane, adjust each cellsize to make use of available space accordingly.

		setTopAnchor(createGridPane(), 0.0);
		setBottomAnchor(createControlPane(), 0.0);
	}

	/**
	 * Creates the control settings pane and adds it to the view.
	 * 
	 * @return The created AnchorPane for the controls.
	 */
	private AnchorPane createControlPane() {
		controlPane = new AnchorPane();

		AnchorPane.setLeftAnchor(controlPane, 10.0);
		AnchorPane.setRightAnchor(controlPane, 10.0);

		sizeSlider = new Slider(10, 200, DEFAULT_GRID_SIZE);
		sizeSlider.setMajorTickUnit(20);
		sizeSlider.setMinorTickCount(10);
		sizeSlider.setBlockIncrement(100);
		sizeSlider.setShowTickMarks(true);
		sizeSlider.setShowTickLabels(true);
		sizeSlider.setSnapToTicks(true);

		delaySpinner = new Spinner<Integer>();
		delaySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 2000, 50));
		delaySpinner.setEditable(true);

		HBox buttonBox = new HBox(startButton, stopButton, new Separator(Orientation.VERTICAL), delayLabel, delaySpinner, new Separator(Orientation.VERTICAL), clearButton, regenButton);
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		controlPane.getChildren().addAll(sizeLabel, sizeSlider, buttonBox);

		AnchorPane.setTopAnchor(sizeSlider, 10.0);
		AnchorPane.setLeftAnchor(sizeSlider, 50.0);
		AnchorPane.setRightAnchor(sizeSlider, 10.0);

		AnchorPane.setBottomAnchor(buttonBox, 10.0);
		AnchorPane.setLeftAnchor(buttonBox, 10.0);
		AnchorPane.setRightAnchor(buttonBox, 10.0);
		AnchorPane.setTopAnchor(sizeLabel, 10.0);
		AnchorPane.setBottomAnchor(sizeLabel, buttonBox.getHeight() + 65.0);
		getChildren().add(controlPane);

		return controlPane;
	}

	/**
	 * Creates the grid pane and adds it to the view.
	 * 
	 * @return The created GridPane
	 */
	private GridPane createGridPane() {
		this.gridPane = new GridPane();
		gridPane.setSnapToPixel(false);
		gridPane.setStyle("-fx-background-color: black;");
		for (int i = 0; i < currentGridSize; i++) {
			for (int j = 0; j < currentGridSize; j++) {
				Rectangle cell = new Rectangle(cellSize, cellSize);
				cell.setFill(DEAD_COLOR);
				gridPane.add(cell, i, j);
			}
		}
		getChildren().add(gridPane);
		return gridPane;
	}

	/**
	 * Manually updates a specific cell and set its to alive or dead. 
	 * 
	 * @param row The x-coordinate
	 * @param col The y-coordnate 
	 * @param alive If it should be alive or dead (true or false). 
	 */
	public void updateCell(int row, int col, boolean alive) {
		if (row < 0 || row >= currentGridSize || col < 0 || col >= currentGridSize) {
			throw new IllegalArgumentException("Invalid value entered for the row or column position.");
		}

		Rectangle cell = (Rectangle) gridPane.getChildren().get(row * currentGridSize + col);
		if (alive) {
			cell.setFill(ALIVE_COLOR);
		} else {
			cell.setFill(DEAD_COLOR);
		}
	}

	/**
	 * Updates the board to reflect the current state of the cells. This marks each
	 * cell as either alive or dead.
	 */
	public void updateBoard(boolean[][] cells) {
		currentGridSize = cells.length;

		for (int i = 0; i < currentGridSize; i++) {
			for (int j = 0; j < currentGridSize; j++) {
				if (!isResizing()) {
					Rectangle cell = null;
					try {
						cell = (Rectangle) gridPane.getChildren().get(i * currentGridSize + j);
						if (cells[i][j]) {
							cell.setFill(ALIVE_COLOR);
						} else {
							cell.setFill(DEAD_COLOR);
						}
					} catch (IndexOutOfBoundsException e) {
					}
				}
			}
		}
	}

	/**
	 * Resizes the board and calculates the new size of the cells based on the new
	 * grid size.
	 * 
	 * @param cells
	 */
	public void resizeGrid(int gridSize) {
		if (gridSize <= 0) {
			throw new IllegalArgumentException("The grid size needs to be positive.");
		}

		resizing = true;
		gridPane.getChildren().clear();
		getChildren().remove(gridPane);
		this.currentGridSize = gridSize;
		this.cellSize = DEFAULT_GRID_WIDTH / gridSize;
		createGridPane();
		resizing = false;
	}

	public double getCellSize() {
		return cellSize;
	}

	public int getGridSize() {
		return currentGridSize;
	}

	public GridPane getGridPane() {
		return gridPane;
	}
	
	public Button getStartButton() {
		return startButton;
	}

	public Button getStopButton() {
		return stopButton;
	}

	public Slider getSizeSlider() {
		return sizeSlider;
	}

	public boolean isResizing() {
		return resizing;
	}

	public Spinner<Integer> getDelaySpinner() {
		return delaySpinner;
	}

	public Button getClearButton() {
		return clearButton;
	}

	public Button getRegenButton() {
		return regenButton;
	}
}
