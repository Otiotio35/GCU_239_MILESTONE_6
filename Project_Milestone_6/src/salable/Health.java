package salable;

/**
 * Represents a specialized type of salable item - a Health item.
 * This class extends the Salable class and adds an attribute specific to health items: power.
 */
public class Health extends Salable {

    /** The potency or power of the health item. */
    private int power;

    /**
     * Constructs a Health object with the provided details.
     *
     * @param type        The general type/category of the item.
     * @param name        The name of the health item.
     * @param description A description of the health item.
     * @param price       The price of the health item.
     * @param quantity    The available quantity of the health item.
     * @param power       The potency or power of the health item.
     */
    public Health(String type, String name, String description, double price, int quantity, int power) {
        super(type, name, description, price, quantity);
        this.power = power;
    }

    /**
     * Gets the potency or power of the health item.
     *
     * @return The potency or power.
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the potency or power of the health item.
     *
     * @param power The potency or power to set.
     */
    public void setPower(int power) {
        this.power = power;
    }
}
