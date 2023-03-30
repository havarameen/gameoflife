## Havars Game of Life
This is a simple proof of concept implementation of Conway's Game of Life, written in Java. Shows a simple MVC implementation using interfaces for the logic layer.

## Technologies Used
- Java 19
- JavaFX
- JUnit 4
- Maven (for dependency management)

## Features
- Adjustable grid size
- Adjustable wrap around setting on board.
- Pause/resume features
- Draw/undraw own patterns/cells (hold left mouse button to draw, right button to clear/erase cells)
- Adjustable framerate/delay.
- Generate random grids

## Screenshots
![Example screenshot](https://user-images.githubusercontent.com/26072135/228055566-b02a811f-dcb5-4173-84ea-fa4fd9d4569b.png)
<!-- If you have screenshots you'd like to share, include them here. -->

## Setup
- You'll need the JavaFX JDK since it's no longer bundled with Java. See https://gluonhq.com/products/javafx/ 
- Clone the repo and make sure your IDE supports Maven so dependencies can be correctly installed.

For IntelliJ Idea:
- Go to File -> Project Structure -> Project Settings and check the modules once you have the maven dependencies downloadd. 
(https://user-images.githubusercontent.com/26072135/228968131-bcdba372-db32-46df-9745-a39b3ac545c3.png)

- Go to Run -> Edit Configuration -> Modify Options -> Set VM arguments and add the items shown in the screenshot. Make sure the JavaFX path points to your own JavaFX Lib path. 
(https://user-images.githubusercontent.com/26072135/228968237-62a79be6-28f7-4cb3-8ef8-d3df1940ac67.png)

For eclipse: 
Nothing in particular, make sure dependencies show correctly in build configuration for the project.

Finally: Run main.java to start the application.

## Usage
Run the main.java class to initiate the game. 

## Project Status
Project is: _complete_. No further work is planned on this for the time being. 

## Contact
Created by [https://github.com/havarameen] - feel free to contact me!
