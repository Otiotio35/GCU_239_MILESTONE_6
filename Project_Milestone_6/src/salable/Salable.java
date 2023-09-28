package salable;

/**
 * Represents a general salable item that has a type, name, description, price, and quantity.
 */
public class Salable {

    /** The general type or category of the salable item. */
    private String type;

    /** The name of the salable item. */
    private String name;

    /** A brief description of the salable item. */
    private String description;

    /** The price of the salable item. */
    private double price;

    /** The available quantity of the salable item. */
    private int quantity;

    /**
     * Constructs a Salable object with the provided details.
     *
     * @param type        The general type or category of the salable item.
     * @param name        The name of the salable item.
     * @param description A brief description of the salable item.
     * @param price       The price of the salable item.
     * @param quantity    The available quantity of the salable item.
     */
    public Salable(String type, String name, String description, double price, int quantity) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Gets the general type or category of the salable item.
     *
     * @return The type or category.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the general type or category of the salable item.
     *
     * @param type The type or category to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the name of the salable item.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the salable item.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets a brief description of the salable item.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a brief description for the salable item.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the price of the salable item.
     *
     * @return The price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price for the salable item.
     *
     * @param price The price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the available quantity of the salable item.
     *
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the available quantity for the salable item.
     *
     * @param quantity The quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
