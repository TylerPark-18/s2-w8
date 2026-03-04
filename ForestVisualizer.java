public class ForestVisualizer {
    public static void printForest(Forest forest) {
        System.out.println("Forest: " + forest.getName() + " | Size: " + forest.gridRows + "x" + forest.gridCols);
        Tree[][] grid = forest.getGrid();
        for (int i = 0; i < forest.gridRows; i++) {
            for (int j = 0; j < forest.gridCols; j++) {
                int state = grid[i][j].getState();
                char c;
                if (state == Tree.EMPTY) c = '.';
                else if (state == Tree.TREE) c = 'T';
                else if (state == Tree.BURNING) c = 'F';
                else c = '?';
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
        System.out.flush();
    }
}
