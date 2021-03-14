package mainsystem;

import java.io.*;

/**
 * A gateway class to save information to files.
 */
public class Persistence {

    /**
     * Saves the given information to the file at the given file path.
     * @param filePath The path to the file where we store the information.
     * @param data The data to store in the file.
     */
    public void saveDataToPath(String filePath, String data){
        File file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                System.out.println("File exception");
            }
        }
        try{
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), false); // clear the file and
            // write to it again (append = false)
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write(data);
            bufferWriter.close();
        }catch(IOException e){
            System.out.println("File Saving Error");
        }
    }

    /**
     * Reads in information from a file at the given file path.
     * @param filePath The path to the file where we store the information.
     * @return The data retrieved from the file.
     */
    public String readStringFromPath(String filePath){
        BufferedReader input;
        try {
            if (!new File(filePath).exists()){return null;} // return null if the file does not exist.

            StringBuilder builder = new StringBuilder();
            input = new BufferedReader(new FileReader(filePath));
            while(true){
                String line = input.readLine();
                if(line == null){
                    break;
                }
                builder.append(line).append("\n");
            }
            return builder.toString();

        }catch (IOException e){
            System.out.println("Error while reading file");
            return null;
        }
    }

}