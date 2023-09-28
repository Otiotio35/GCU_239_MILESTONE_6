package salable;

/**
 * Represents a specialized type of salable item - an Armor.
 * This class extends the Salable class and adds an attribute specific to armors: armorType.
 */
public class Armor extends Salable {

    /** The specific type or category of the armor (e.g., "Plate", "Leather", "Chainmail"). */
    private String armorType;

    /**
     * Constructs an Armor object with the provided details.
     *
     * @param type        The general type/category of the item.
     * @param name        The name of the armor.
     * @param description A description of the armor.
     * @param price       The price of the armor.
     * @param quantity    The available quantity of the armor.
     * @param armorType   The specific type or category of the armor.
     */
    public Armor(String type, String name, String description, double price, int quantity, String armorType) {
        super(type, name, description, price, quantity);
        this.armorType = armorType;
    }

    /**
     * Gets the specific type or category of the armor.
     *
     * @return The armor type.
     */
    public String getArmorType() {
        return armorType;
    }

    /**
     * Sets the specific type or category of the armor.
     *
     * @param armorType The armor type to set.
     */
    public void setArmorType(String armorType) {
        this.armorType = armorType;
    }
}
