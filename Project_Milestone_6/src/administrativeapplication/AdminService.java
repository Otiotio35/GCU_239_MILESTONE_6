package administrativeapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * The `AdminService` class provides administrative functionality for managing the store's inventory.
 * It allows administrators to perform actions such as adding, removing, or updating products in the inventory.
 * This service is typically used by authorized administrators to maintain the store's catalog.
 * @version 09/17/2023 ID: 21024608
 * @author toafik otiotio
 */
public class AdminService {
	/**
	 * Default constructor for the AdminService class.
	 * This constructor initializes the AdminService with default settings.
	 */
	public AdminService() {
	    // Constructor logic, if any
	}
    private static final int ADMIN_SERVER_PORT = 8899;
    private static final int STORE_FRONT_PORT = 9900;
    private final String inventoryFilePath = "InFile.txt";
    private final String storeFrontAddress = "localhost";
    /**
     * The main entry point for the administrative service application.
     * This method is responsible for initializing and starting the administrative service.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        AtomicInteger portRef = new AtomicInteger(ADMIN_SERVER_PORT);

        if (args.length > 0) {
            try {
                portRef.set(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port " + ADMIN_SERVER_PORT);
            }
        }

        AdminService adminService = new AdminService();
        Thread serverThread = new Thread(() -> adminService.startServer(portRef.get()));
        serverThread.start();
    }

    private void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Admin Service listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleAdminCommand(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private void handleAdminCommand(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String command = in.readLine();

            switch (command) {
                case "U":
                    StringBuilder updateData = new StringBuilder();
                    String inputLine;
                    while (!(inputLine = in.readLine()).equals("END_OF_FILE")) {
                        updateData.append(inputLine).append("\n");
                    }
                    boolean updated = updateInventory(updateData.toString());
                    if (updated) {
                        out.println("Inventory updated successfully.");
                    } else {
                        out.println("Failed to update inventory.");
                    }
                    break;
                case "R":
                    String inventoryData = retrieveInventoryAsString();
                    out.println(inventoryData);
                    out.println("END_OF_FILE");
                    break;
                default:
                    sendCommandToStoreFront(command);
                    out.println("Command sent to Store Front.");
                    break;
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    private boolean updateInventory(String data) {
        try {
            Files.write(Paths.get(inventoryFilePath), data.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String retrieveInventoryAsString() {
        try {
            return new String(Files.readAllBytes(Paths.get(inventoryFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching inventory.";
        }
    }

    private void sendCommandToStoreFront(String command) {
        try (Socket storeFrontSocket = new Socket(storeFrontAddress, STORE_FRONT_PORT);
             PrintWriter out = new PrintWriter(storeFrontSocket.getOutputStream(), true)) {
            out.println(command);
            // Optionally, you can also read responses here and relay them back to the Admin application if required.
        } catch (IOException e) {
            System.err.println("Error communicating with Store Front: " + e.getMessage());
        }
    }
}
