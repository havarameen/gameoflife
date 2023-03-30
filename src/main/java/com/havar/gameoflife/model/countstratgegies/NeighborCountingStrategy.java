package com.havar.gameoflife.model.countstratgegies;

public interface NeighborCountingStrategy {
    int countNeighbors(int rowPosition, int colPosition, boolean[][] cells, int rows, int columns);
}
