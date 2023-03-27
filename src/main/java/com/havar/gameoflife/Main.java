package com.havar.gameoflife;

import com.havar.gameoflife.controller.GameOfLifeController;

import com.havar.gameoflife.model.GameOfLifeLogic;
import com.havar.gameoflife.view.GameOfLifeView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Simple implementation of Conway's Game of Life.
 * 
 *  Follows these rules:
 *  
 *  1: Any live cell with fewer than two live neighbours dies, as if by underpopulation.
 *  2: Any live cell with two or three live neighbours lives on to the next generation.
 *  3: Any live cell with more than three live neighbours dies, as if by overpopulation.
 *  4: Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 *  
 *  Simplifies into: 
 *  1: Any live cell with two or three live neighbours survives.
 *  2: Any dead cell with three live neighbours becomes a live cell.
 *  3: All other live cells die in the next generation. Similarly, all other dead cells stay dead.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life</a
 * @author Havar Ameen
 *
 */
public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GameOfLifeLogic model = new GameOfLifeLogic(100, 100);
		GameOfLifeView view = new GameOfLifeView();
		GameOfLifeController controller = new GameOfLifeController(model, view);
		
		setStage(primaryStage, view, controller);
	    controller.execute();
	}

	private void setStage(Stage primaryStage, GameOfLifeView view, GameOfLifeController controller) {
		Scene scene = new Scene(view, 1000, 1100);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    primaryStage.setResizable(false);
	    primaryStage.setTitle("Havars Game Of Life");
	    primaryStage.setOnCloseRequest(e -> {
            controller.stop();
            Platform.exit();
        });
	}
}
