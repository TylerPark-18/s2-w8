
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ForestFire {
    public static void main(String[] args) throws IOException, FileNotFoundException{


        File f = new File("forests.csv");
        Scanner scanner = new Scanner(f);
        scanner.nextLine();
        ArrayList<Forest> forests = new ArrayList<Forest>();

        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            String[] line = s.split(",");
            Forest x = new Forest(line[0],line[1],line[2],Double.parseDouble(line[3]),Integer.parseInt(line[4]),Integer.parseInt(line[5]),Integer.parseInt(line[6]),Integer.parseInt(line[7]));
            forests.add(x);
        
        }

        forests.get(1).initializeForest();
        

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

        // Step 4: We will vibe code our way to visualization

    }

}
