package textui.element;

/**
 * Represents the presence or absence of borders on each side of a UI element
 * in a terminal-based box model system.
 *
 * @param top    true if the top border is present
 * @param right  true if the right border is present
 * @param bottom true if the bottom border is present
 * @param left   true if the left border is present
 */
public record Border(boolean top, boolean right, boolean bottom, boolean left) {

    /**
     * Calculates the total number of horizontal borders (left and right).
     *
     * @return 0, 1, or 2 depending on how many horizontal borders are set
     */
    public int getBorderHorizontal() {
        return (this.left ? 1 : 0) + (this.right ? 1 : 0);
    }

    /**
     * Calculates the total number of vertical borders (top and bottom).
     *
     * @return 0, 1, or 2 depending on how many vertical borders are set
     */
    public int getBorderVertical() {
        return (this.top ? 1 : 0) + (this.bottom ? 1 : 0);
    }
}
