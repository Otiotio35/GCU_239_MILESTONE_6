package shopping;

import java.util.ArrayList;
import java.util.List;

import salable.Salable;

/**
 * The ShoppingCart class represents a shopping cart in a store front
 * application.
 */
public class ShoppingCart {
    private List<Salable> items = new ArrayList<>();

    /**
     * Constructs an empty ShoppingCart with an empty list of items.
     */
    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a salable product to the shopping cart. If the product is already in the
     * cart, the quantity is updated accordingly.
     *
     * @param product The Salable product to be added to the cart.
     */
    public void addItem(Salable product) {
        addItem(product, 1);
    }

    /**
     * Adds a salable product with the specified quantity to the shopping cart. If
     * the product is already in the cart, the quantity is updated accordingly.
     *
     * @param product  The Salable product to be added to the cart.
     * @param quantity The quantity of the product to be added to the cart.
     */
    public void addItem(Salable product, int quantity) {
        // Check if the product is already in the cart
        for (Salable item : items) {
            if (item.getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // If the product is not in the cart, add it as a new item
        Salable newItem = new Salable(product.getName(), product.getDescription(), null, product.getPrice(), quantity);
        items.add(newItem);
    }

    /**
     * Removes a salable product from the shopping cart.
     *
     * @param product The Salable product to be removed from the cart.
     */
    public void removeItem(Salable product) {
        items.remove(product);
    }

    /**
     * Returns the total cost of all the goods in the shopping cart after being
     * calculated.
     *
     * @return The total price of all items in the cart.
     */
    public double getTotalPrice() {
        double totalPrice = 0;
        for (Salable item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    /**
     * Handles the checkout process. For simplicity, this method just prints a
     * message indicating that the checkout is completed.
     */
    public void checkout() {
        // Code for the checkout process goes here
        // For simplicity, we'll just print a message
        System.out.println("Checkout completed!");
    }

    /**
     * Clears the shopping cart.
     */
    public void clearCart() {
        items.clear();
    }

    /**
     * Displays the contents of the shopping cart.
     */
    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("===== Shopping Cart =====");
            System.out.println("Name\t\tDescription\t\tPrice\tQuantity");
            for (Salable item : items) {
                System.out.println(item.getName() + "\t" + item.getDescription() + "\t$" + item.getPrice() + "\t"
                        + item.getQuantity());
            }
            System.out.println("=========================");
            System.out.println("Total Price: $" + getTotalPrice());
        }
    }

    /**
     * Returns the items in the shopping cart.
     *
     * @return A list of salable products in the cart.
     */
    public List<Salable> getItems() {
        return new ArrayList<>(items); // Returning a defensive copy to ensure the original list is not modified
                                       // externally
    }
}
