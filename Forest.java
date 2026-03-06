// Forest.java
// Stores metadata and grid for a forest

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Forest {
    private String name;
    private String type;
    private String vegetation;
    private double burnRate;
    private int initialTreeCount;
    private int burnDuration;
    public int gridRows;
    public int gridCols;
    public Tree[][] grid;

    public Forest(String name, String type, String vegetation, double burnRate, int burnDuration, int initialTreeCount, int gridRows, int gridCols) {
        this.gridRows = gridRows;
        this.gridCols = gridCols;
        this.grid = new Tree[gridRows][gridCols]; //TODO; initialize grid to using gridRows and gridCols
        this.name = name;
        this.type = type;
        this.vegetation = vegetation;
        this.burnRate = burnRate;
        this.initialTreeCount = initialTreeCount;
        this.burnDuration = burnDuration;
        this.initializeForest(); //This is going to populate the grid with trees.
    }
    
    public void initializeForest(){
        int maxTrees = gridRows * gridCols;
        int treesToPlace = Math.min(initialTreeCount, maxTrees);
        int numTrees = 0;
        for(int i = 0; i< grid.length;i++){
            for(int k = 0; k< grid[0].length;k++){
                grid[i][k] = new Tree(0);
            }
        }
        int attempts = 0;
        int maxAttempts = treesToPlace * 10;
        while(numTrees < treesToPlace && attempts < maxAttempts){
            int row = (int)(Math.random()*grid.length);
            int column = (int)(Math.random()*grid[0].length);
            if(grid[row][column].getState() == 0){
                grid[row][column].setState(1);
                numTrees++;
            }
            attempts++;
        }
        if (numTrees < treesToPlace) {
            System.out.println("Warning: Could not place all trees. Placed " + numTrees + " out of " + treesToPlace);
        }
        for(int i = 0; i< grid.length; i++){
            for(int k = 0; k< grid[0].length;k++){
                if(grid[i][k].getBurnTime() >0){
                    grid[i][k].setBurnTime(0);
                }
            }
        }


    }

    public Tree[][] deepCopy(){
        Tree[][] newTree = new Tree[gridRows][gridCols];
        for(int i = 0; i< gridRows;i++){
            for(int k = 0; k< gridCols;k++){
                newTree[i][k] = new Tree(grid[i][k].getState());
                newTree[i][k].setBurnTime(grid[i][k].getBurnTime());
            }
        }
        return newTree;
    }

    public void spreadFire() {
        Tree[][] tree = deepCopy();
        for(int row = 0; row < gridRows; row++){
            for(int col = 0; col < gridCols; col++){
                if(tree[row][col].getState() == Tree.BURNING) {
                    // Left neighbor
                    if(col > 0 && tree[row][col-1].getState() == Tree.TREE) {
                        if(Math.random() <= burnRate) {
                            tree[row][col-1].setState(Tree.BURNING);
                            tree[row][col-1].setBurnTime(0);
                        }
                    }
                    // Right neighbor
                    if(col < gridCols-1 && tree[row][col+1].getState() == Tree.TREE) {
                        if(Math.random() <= burnRate) {
                            tree[row][col+1].setState(Tree.BURNING);
                            tree[row][col+1].setBurnTime(0);
                        }
                    }
                    // Up neighbor
                    if(row > 0 && tree[row-1][col].getState() == Tree.TREE) {
                        if(Math.random() <= burnRate) {
                            tree[row-1][col].setState(Tree.BURNING);
                            tree[row-1][col].setBurnTime(0);
                        }
                    }
                    // Down neighbor
                    if(row < gridRows-1 && tree[row+1][col].getState() == Tree.TREE) {
                        if(Math.random() <= burnRate) {
                            tree[row+1][col].setState(Tree.BURNING);
                            tree[row+1][col].setBurnTime(0);
                        }
                    }
                    // Increase burn time
                    tree[row][col].setBurnTime(tree[row][col].getBurnTime() + 1);
                }
            }
        }
        // Turn burned out trees to EMPTY
        for(int row = 0; row < gridRows; row++){
            for(int col = 0; col < gridCols; col++){
                if(tree[row][col].getState() == Tree.BURNING && tree[row][col].getBurnTime() >= burnDuration){
                    tree[row][col].setState(Tree.EMPTY);
                }
            }
        }
        // Update grid
        for(int row = 0; row < gridRows; row++){
            for(int col = 0; col < gridCols; col++){
                grid[row][col] = tree[row][col];
            }
        }
    }

    public double percentBurned() {
        int x = 0;
        for(int row = 0; row < gridRows;row++){
            for(int col = 0; col <gridCols;col++){
                if(grid[row][col].getBurnTime() >= burnDuration){
                    x++;
                }
            }
        }
        if(initialTreeCount == 0){
            return 0;
        }        
        // Step 1: Count how many trees have burned out (commonly represented as EMPTY after burning).
        // Step 2: Compute and return (burnedCount * 100.0) / initialTreeCount as a percentage.
        // Step 3: Guard against divide-by-zero if the initialTreeCount is 0.
        return ((x*100.0)/initialTreeCount)*100;
    }

    public void saveGridSnapshotToFile() {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return;
        }

        String safeName = name == null ? "forest" : name.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
        if (safeName.isEmpty()) {
            safeName = "forest";
        }
        String fileName = safeName + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("=== GRID SNAPSHOT ===");
            writer.newLine();
            for (Tree[] row : grid) {
                StringBuilder line = new StringBuilder();
                for (Tree tree : row) {
                    char cell;
                    if (tree == null) {
                        cell = '?';
                    } else if (tree.getState() == Tree.EMPTY) {
                        cell = '.';
                    } else if (tree.getState() == Tree.TREE) {
                        cell = 'T';
                    } else if (tree.getState() == Tree.BURNING) {
                        cell = 'F';
                    } else {
                        cell = '?';
                    }
                    line.append(cell);
                }
                writer.write(line.toString());
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write grid snapshot to file: " + fileName, e);
        }
    }

    public void setGrid(Tree[][] grid) {
        this.grid = grid;
    }
    public Tree[][] getGrid() {
        return grid;
    }
    public double getBurnRate() { return burnRate; }
    public int getBurnDuration() { return burnDuration; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getVegetation() { return vegetation; }
    public int getInitialTreeCount() { return initialTreeCount; }
}
