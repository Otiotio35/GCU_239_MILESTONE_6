package salable;

/**
 * Represents a weapon, a specialized salable item that has a power attribute.
 * Weapons can be compared based on their names.
 */
public class Weapon extends Salable implements Comparable<Weapon> {

    /** The power level or strength of the weapon. */
    private int power;

    /**
     * Constructs a Weapon object with the provided details.
     *
     * @param type        The general type or category of the weapon.
     * @param name        The name of the weapon.
     * @param description A brief description of the weapon.
     * @param price       The price of the weapon.
     * @param quantity    The available quantity of the weapon.
     * @param power       The power level or strength of the weapon.
     */
    public Weapon(String type, String name, String description, double price, int quantity, int power) {
        super(type, name, description, price, quantity);
        this.power = power;
    }

    /**
     * Gets the power level or strength of the weapon.
     *
     * @return The power level.
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the power level or strength of the weapon.
     *
     * @param power The power level to set.
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Compares this weapon with another based on their names.
     *
     * @param otherWeapon The other weapon to compare with.
     * @return A negative integer, zero, or a positive integer as this weapon's name
     *         is less than, equal to, or greater than the specified weapon's name.
     */
    @Override
    public int compareTo(Weapon otherWeapon) {
        return this.getName().compareToIgnoreCase(otherWeapon.getName());
    }
}
