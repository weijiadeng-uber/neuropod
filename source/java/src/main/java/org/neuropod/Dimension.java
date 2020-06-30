package org.neuropod;

/**
 * The type Dimension.
 */
public class Dimension {
    /**
     * The Value.
     */
    long value;
    /**
     * The Symbol.
     */
    String symbol;

    /**
     * Instantiates a new Dimension.
     *
     * @param value the value
     */
    public Dimension(long value) {
        this.value = value;
    }

    /**
     * Instantiates a new Dimension.
     *
     * @param symbol the symbol
     */
    public Dimension(String symbol) {
        this.symbol = symbol;
        this.value = -2;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public long getValue() {
        return value;
    }

    /**
     * Gets symbol.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        if (value >= 0) {
            return String.valueOf(value);
        } else if (value == -1){
            return "None";
        } else if (value == -2) {
            return symbol;
        }
        return "Not Set!";
    }
}
