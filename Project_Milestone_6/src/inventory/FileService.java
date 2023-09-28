package inventory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Provides services for reading from and writing to files. This class abstracts
 * file operations like reading entire files into strings and writing strings to
 * files.
 *
 * @version 09/17/2023 ID: 21024608
 * @author toafik otiotio
 */
public class FileService {

    /**
     * Initializes a new instance of the FileService class.
     */
    public FileService() {
        // constructor body...
    }

    /**
     * Reads the content of a file and returns it as a String.
     *
     * @param path The path to the file to be read.
     * @return The content of the file as a String.
     * @throws IOException If there's an error reading the file.
     */
    public String readFile(String path) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            StringBuilder data = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line).append(System.lineSeparator());
            }
            return data.toString();
        }
    }

    /**
     * Writes a string to a file.
     *
     * @param path The path to the file to be written.
     * @param data The string data to be written to the file.
     * @throws IOException If there's an error writing to the file.
     */
    public void writeFile(String path, String data) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write(data);
        }
    }
}
