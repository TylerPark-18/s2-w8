import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class ForestFire {
    public static ArrayList<Forest> forests;
    public static void main(String[] args) throws IOException, FileNotFoundException{

        
        File f = new File("forests.csv");
        Scanner scanner = new Scanner(f);
        scanner.nextLine();
        forests = new ArrayList<Forest>();

        while(scanner.hasNextLine()){
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) continue; // skip blank lines
            String[] line = s.split(",");
            if (line.length < 10) continue; // skip malformed lines
            Forest x = new Forest(line[0],line[1],line[2],Double.parseDouble(line[7]),Integer.parseInt(line[9]),Integer.parseInt(line[8]),Integer.parseInt(line[5]),Integer.parseInt(line[6]));
            forests.add(x);
        }
        int i = 2;
        forests.get(i).initializeForest();
        boolean nplaced = false;
        int attempts = 0;
        int maxAttempts = forests.get(i).gridRows * forests.get(i).gridCols * 2;
        while(!nplaced && attempts < maxAttempts){
            int row = (int)(Math.random()*forests.get(i).gridRows);
            int col = (int)(Math.random()*forests.get(i).gridCols);
            if(forests.get(i).grid[row][col].getState()== 1){
                forests.get(i).grid[row][col].setState(2);
                nplaced = true;
            }
            attempts++;
        }
        if (!nplaced) {
            System.out.println("Warning: No TREE cell found to start burning.");
        }
        int z = 0;
        while(z < 10){
            forests.get(i).spreadFire();
            ForestVisualizer.printForest(forests.get(i)); // visualize each step
            z++;
        }
        System.out.println("Percent Burned : "+forests.get(i).percentBurned());
        scanner.close();

        // Step 1: Read in the data file (forests.csv) and create Forest objects.

        //   - Open the CSV file.
        //   - Skip/read the header row.
        //   - Parse each line into fields and construct a Forest.
        //   - Store forests in an ArrayList: ArrayList<Forest> forests = ...

        // Step 2: Pick one forest to run the simulation.
        //   - Choose by index.
        //   - Start at least one burning tree to begin the fire.

        // Step 3: Run the simulation.
        //   - Repeat spreadFire() for a fixed number of steps (or until fire ends).
        //   - At the end, print percentBurned() and summary stats.
        //   - At the end of each simulation step, you should write the current state of the Tree[][] grid to a file
        simulateAndRecordData(forests, "forestssim.csv");
        // Step 4: We will vibe code our way to visualization

    }
public static void simulateAndRecordData(ArrayList<Forest> simulator, String filename) throws IOException, FileNotFoundException {
        File f = new File(filename);
        String filePath = filename;
        try(PrintWriter writer = new PrintWriter(new FileWriter(filePath))){
            for(Forest n: simulator){
                String line = String.join(",", n.getName(),n.getType(), n.getVegetation(), Double.toString(n.getBurnRate()),Integer.toString(n.getBurnDuration()), Integer.toString(n.getInitialTreeCount()));
                writer.println(line);
            }
        } catch(IOException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }    
}
