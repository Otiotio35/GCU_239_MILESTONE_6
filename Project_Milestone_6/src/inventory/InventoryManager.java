package inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import salable.Salable;
/**
 * The `InventoryManager` class represents a manager for inventory items in a store.
 * It provides methods to load, manage, and interact with the store's inventory of salable products.
 *
 * <p>The inventory is stored as a collection of salable products, and it can be loaded from and saved to a file.
 * This class offers functionality to add, remove, purchase, and cancel the purchase of products, as well as
 * display the inventory and perform sorting operations.
 *
 * <p>Additionally, it can update the inventory from a data string and retrieve the inventory as a formatted string.
 *
 * @author Your Name
 * @version 1.0
 */
public class InventoryManager {

    private Map<String, Salable> products;
    private String filePath;
    private FileService fileService;
    /**
     * Constructor for the `InventoryManager` class.
     * Initializes the inventory manager with an empty product collection and loads inventory data from a file.
     *
     * @param filePath The file path to the inventory data file.
     */
    public InventoryManager(String filePath) {
        this.products = new HashMap<>();
        this.filePath = filePath;
        this.fileService = new FileService();
        loadInventory();
    }

    /**
     * Loads inventory data from a file and populates the product collection.
     */
    private void loadInventory() {
        try {
            String fileData = fileService.readFile(filePath);
            String[] lines = fileData.split("\\n");

            for (String line : lines) {
                processInventoryLine(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
    }
  
    private void processInventoryLine(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length == 5) {
                String type = parts[0].trim().substring("Type: ".length());
                String name = parts[1].trim().substring("Name: ".length());
                String description = parts[2].trim().substring("Description: ".length());
                double price = Double.parseDouble(parts[3].trim().substring("Price: ".length()));
                int quantity = Integer.parseInt(parts[4].trim().substring("Quantity: ".length()));

                Salable item = new Salable(type, name, description, price, quantity);
                products.put(name.toLowerCase(), item);
            }
        } catch (NumberFormatException ex) {
            System.err.println("Error processing line: " + line);
        }
    }
    /**
     * Gets the current inventory as a list of salable products.
     *
     * @return A list of salable products representing the inventory.
     */
    public List<Salable> getInventory() {
        return new ArrayList<>(products.values());
    }
    /**
     * Gets a product from the inventory by its name.
     *
     * @param name The name of the product to retrieve.
     * @return The salable product with the specified name, or null if not found.
     */
    public Salable getProduct(String name) {
        return products.get(name.toLowerCase());
    }
    /**
     * Adds a new product to the inventory.
     *
     * @param product The salable product to add.
     */
    public void addProduct(Salable product) {
        products.put(product.getName().toLowerCase(), product);
    }
    /**
     * Removes a specified quantity of a product from the inventory.
     *
     * @param product  The salable product to remove.
     * @param quantity The quantity of the product to remove.
     * @return `true` if the removal was successful, `false` if the product is not found or the quantity is not available.
     */

    public boolean removeProduct(Salable product, int quantity) {
        Salable existingProduct = products.get(product.getName().toLowerCase());
        if (existingProduct == null) return false;

        if (existingProduct.getQuantity() <= quantity) {
            products.remove(product.getName().toLowerCase());
        } else {
            existingProduct.setQuantity(existingProduct.getQuantity() - quantity);
        }
        return true;
    }
    /**
     * Purchases a specified quantity of a product, reducing its quantity in the inventory.
     *
     * @param name     The name of the product to purchase.
     * @param quantity The quantity of the product to purchase.
     * @return `true` if the purchase was successful, `false` if the product is not found or the quantity is not available.
     */
    public boolean purchaseProduct(String name, int quantity) {
        Salable product = getProduct(name);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            return true;
        }
        return false;
    }
    /**
     * Cancels the purchase of a product and restores its quantity in the inventory.
     *
     * @param name     The name of the product to cancel the purchase for.
     * @param quantity The quantity of the product to cancel.
     * @return `true` if the purchase was successfully canceled and the product's quantity was restored,
     *         `false` if the product is not found or the specified quantity is not available for cancelation.
     */
    public boolean cancelPurchase(String name, int quantity) {
        Salable product = getProduct(name);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            return true;
        }
        return false;
    }

/**
 * Displays the current inventory, including product details such as type, name, description, price, and quantity.
 */

    public void displayInventory() {
        System.out.println("Current Inventory:");
        System.out.printf("%-10s %-15s %-30s %-10s %-8s%n", "Type", "Name", "Description", "Price", "Quantity");
        for (Salable product : products.values()) {
            System.out.printf("%-10s %-15s %-30s $%-9.2f %-8d%n", product.getType(), product.getName(),
                    product.getDescription(), product.getPrice(), product.getQuantity());
        }
    }
    /**
     * Saves the current inventory data to a file.
     */
    public void saveInventoryToFile() {
        StringBuilder fileData = new StringBuilder();
        for (Salable product : products.values()) {
            String line = String.format("Type: %s|Name: %s|Description: %s|Price: %.2f|Quantity: %d", product.getType(),
                    product.getName(), product.getDescription(), product.getPrice(), product.getQuantity());
            fileData.append(line).append("\n");
        }

        try {
            fileService.writeFile(filePath, fileData.toString());
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }
    /**
     * Gets the inventory sorted by product name, either in ascending or descending order.
     *
     * @param ascending `true` for ascending order, `false` for descending order.
     * @return A sorted list of salable products.
     */
    public List<Salable> getInventorySortedByName(boolean ascending) {
        return products.values().stream()
            .sorted(ascending ? Comparator.comparing(Salable::getName) :
                                Comparator.comparing(Salable::getName).reversed())
            .collect(Collectors.toList());
    }
    /**
     * Gets the inventory sorted by product price, either in ascending or descending order.
     *
     * @param ascending `true` for ascending order, `false` for descending order.
     * @return A sorted list of salable products.
     */
    public List<Salable> getInventorySortedByPrice(boolean ascending) {
        return products.values().stream()
            .sorted(ascending ? Comparator.comparingDouble(Salable::getPrice) :
                                Comparator.comparingDouble(Salable::getPrice).reversed())
            .collect(Collectors.toList());
    }
    /**
     * Updates the inventory with new data provided as a string.
     *
     * @param data The string containing updated inventory data.
     * @return `true` if the update was successful, `false` otherwise.
     */
    public boolean updateInventory(String data) {
        try {
            products.clear();
            String[] lines = data.split("\\n");

            for (String line : lines) {
                processInventoryLine(line);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Gets the inventory as a formatted string suitable for display or saving to a file.
     *
     * @return A formatted string representing the inventory.
     */
    public String getInventoryAsString() {
        StringBuilder inventoryData = new StringBuilder();
        for (Salable product : products.values()) {
            String line = String.format("Type: %s|Name: %s|Description: %s|Price: %.2f|Quantity: %d", product.getType(),
                    product.getName(), product.getDescription(), product.getPrice(), product.getQuantity());
            inventoryData.append(line).append("\n");
        }
        return inventoryData.toString();
    }
}
