package administrativeapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * The `AdministrativeApplication` class represents a client application that communicates with a server
 * to perform administrative tasks, such as updating data and requesting inventory information.
 * It attempts to establish a connection with the server and retries a specified number of times
 * with a specified retry interval in case of connection failures.
 *
 * <p>The communication with the server involves sending commands and receiving responses using a socket.
 * This class sends an update command to update data from a file and then requests inventory information
 * from the server.
 *
 * <p>This class is designed to be run as a standalone application.
 *
* @version 09/17/2023 ID: 21024608
 * @author toafik otiotio
 */
public class AdministrativeApplication {
	
	/**
	 * Default constructor for the AdministrativeApplication class.
	 * This constructor is responsible for initializing the class.
	 */
	public AdministrativeApplication() {
	    // Constructor logic, if any
	}
	 // Constants for server connection and retry configuration

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8899;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_INTERVAL_MS = 2000; // 2 seconds
    /**
     * The main method of the `AdministrativeApplication` class.
     * It attempts to connect to the server, send an update command, receive responses, and handle connection errors.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Command to Update with the pipe separator
                String commandUpdate = "U|";
                String fileContent = new String(Files.readAllBytes(Paths.get("InFile.txt")));
                out.println(commandUpdate + fileContent);
                out.println("END_OF_FILE");

                String response = in.readLine();
                System.out.println("Response: " + response);

                // For receiving inventory with the pipe separator
                String commandRequest = "R|";
                out.println(commandRequest);

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while (!(line = in.readLine()).equals("END_OF_FILE")) {
                    responseBuilder.append(line).append("\n");
                }

                System.out.println("Received Inventory:\n" + responseBuilder.toString());

                return; // Successfully connected and processed, exit the loop and the main method.

            } catch (IOException e) {
                System.err.println("Connection error (Attempt " + (i + 1) + "): " + e.getMessage());

                if (i < MAX_RETRIES - 1) {
                    System.err.println("Retrying in " + RETRY_INTERVAL_MS + " milliseconds...");
                    try {
                        Thread.sleep(RETRY_INTERVAL_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.err.println("Interrupted while waiting for retry: " + ie.getMessage());
                        break;
                    }
                }
            }
        }

        System.err.println("Failed to connect after " + MAX_RETRIES + " attempts.");
    }
}
