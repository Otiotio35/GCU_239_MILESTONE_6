package Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import inventory.InventoryManager;
import salable.Salable;
import shopping.ShoppingCart;
/**
 * The `StoreFront` class represents a simple storefront application that allows users to view and manage
 * inventory, add items to a shopping cart, view the cart, and perform checkout operations.
 * It also includes a server thread for receiving administrative commands to update and request inventory data.
 *
 * <p>This class interacts with an inventory manager, shopping cart, and allows customers to perform actions
 * such as viewing the inventory, adding items to the cart, viewing the cart, and checking out.
 *
 * <p>The administrative commands can be sent to the server running on a specified port to update inventory data
 * and request inventory information.
 *
 * @version 09/17/2023 ID: 21024608
 * @author toafik otiotio
 */
public class StoreFront {
	private static final int SERVER_PORT = 9900;
	private String name;
	private InventoryManager inventoryManager;
	private ShoppingCart shoppingCart;
	private Scanner scanner;
	/**
     * Constructor for the `StoreFront` class.
     * Initializes the storefront with a name, sets up the inventory manager, shopping cart, and starts
     * the server thread for receiving admin commands.
     *
     * @param name The name of the storefront.
     */
	public StoreFront(String name) {
		this.name = name;
		this.inventoryManager = new InventoryManager("InFile.txt");
		this.shoppingCart = new ShoppingCart();
		this.scanner = new Scanner(System.in);

		// Start the server thread for admin commands.
		new Thread(this::startAdminServer).start();

		displayWelcomeMessage();
		run();
	}
	  /**
     * Starts the server thread for handling administrative commands.
     */
	private void startAdminServer() {
		try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
			System.out.println("Listening for admin commands on port " + SERVER_PORT);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				new Thread(() -> handleAdminCommands(clientSocket)).start();
			}
		} catch (IOException e) {
			System.err.println("Server error: " + e.getMessage());
		}
	}
	   /**
     * Handles administrative commands received from the admin client.
     *
     * @param socket The socket for communication with the admin client.
     */
	private void handleAdminCommands(Socket socket) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

			String received = in.readLine();
			String[] parts = received.split("\\|", 2);
			String command = parts[0];
			String payload = parts.length > 1 ? parts[1] : null;

			switch (command) {
			case "U":
				if (payload != null) {
					StringBuilder updateData = new StringBuilder(payload);
					String line;
					while ((line = in.readLine()) != null && !line.equals("END_OF_FILE")){
						updateData.append(line).append("\n");
					}
					if (!updateData.toString().trim().isEmpty()) {
						boolean updated = inventoryManager.updateInventory(updateData.toString());
						if (updated) {
							out.println("Inventory updated successfully.");
						} else {
							out.println("Failed to update inventory.");
						}
					} else {
						out.println("No data received for update.");
					}
				} else {
					out.println("No data received for update.");
				}
				break;
			case "R":
				String inventoryData = inventoryManager.getInventoryAsString();
				out.println(inventoryData);
				out.println("END_OF_FILE");
				break;
			default:
				out.println("Invalid command.");
				break;
			}
		} catch (IOException e) {
			System.err.println("Admin command error: " + e.getMessage());
		}
	}
	   /**
     * Adds a product to the shopping cart and updates the inventory.
     *
     * @param product  The product to add to the cart.
     * @param quantity The quantity of the product to add.
     */
	public void purchaseProduct(Salable product, int quantity) {
		shoppingCart.addItem(product, quantity);
		inventoryManager.removeProduct(product, quantity);
	}
	  /**
     * Cancels the purchase of a product, removing it from the cart and adding it back to the inventory.
     *
     * @param product The product to remove from the cart.
     */
	public void cancelPurchase(Salable product) {
		shoppingCart.removeItem(product);
		inventoryManager.addProduct(product);
	}
	  /**
     * Displays the contents of the shopping cart.
     */
	public void viewCart() {
		shoppingCart.viewCart();
	}
	  /**
     * Performs the checkout process, displaying the cart contents and allowing the user to confirm the purchase.
     * If confirmed, the cart is cleared.
     */
	public void checkout() {
		System.out.println("===== Checkout =====");
		shoppingCart.viewCart();
		double totalPrice = shoppingCart.getTotalPrice();
		if (totalPrice > 0) {
			System.out.println("Total Price: $" + totalPrice);
			System.out.print("Confirm checkout (y/n): ");
			String confirm = scanner.next().toLowerCase();
			if (confirm.equals("y")) {
				System.out.println("Checkout completed!");
				shoppingCart.clearCart();
			} else {
				System.out.println("Checkout canceled.");
			}
		} else {
			System.out.println("Cart is empty. Nothing to checkout.");
		}
	}

    /**
     * Displays a welcome message for the storefront.
     */
	public void displayWelcomeMessage() {
		System.out.println("WELCOME TO " + name + "!");
	}
	/**
     * Displays the main menu of actions for the user.
     */
	public void displayMenu() {
		System.out.println("===== Actions =====");
		System.out.println("1. View Inventory");
		System.out.println("2. Add Item to Cart");
		System.out.println("3. View Cart");
		System.out.println("4. Checkout");
		System.out.println("5. Exit");
	}
    /**
     * Runs the main loop of the storefront application, allowing the user to perform actions.
     */

	public void run() {
        while (true) {
            try {
                displayMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character

                switch (choice) {
                    case 1:
                        inventoryManager.displayInventory();
                        break;
                    case 2:
                        System.out.print("Enter the item name to add to the cart: ");
                        String itemName = scanner.nextLine();
                        Salable item = inventoryManager.getProduct(itemName);
                        if (item != null) {
                            System.out.print("Enter the quantity of " + item.getName() + " you wish to buy: ");
                            int quantity = scanner.nextInt();
                            if (quantity > 0 && quantity <= item.getQuantity()) {
                                purchaseProduct(item, quantity);
                                System.out.println(quantity + "x " + item.getName() + " added to the cart.");
                            } else {
                                System.out.println("Invalid quantity. Please try again.");
                            }
                        } else {
                            System.out.println("Item not found in the inventory.");
                        }
                        break;
                    case 3:
                        shoppingCart.viewCart();
                        break;
                    case 4:
                        checkout();
                        break;
                    case 5:
                        System.out.println("Thank you for using the StoreFront!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();  // Clear the invalid input
            }
        }
    }
	 /**
     * The main method of the `StoreFront` class.
     * Creates an instance of the `StoreFront` and initializes the storefront application.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        new StoreFront("TAOFIK OTIOTIO INTERNATIONAL FOOD STORE");
    }
}
