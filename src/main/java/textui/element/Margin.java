package textui.element;

/**
 * Represents the margin around an element.
 * Margins define the space outside the element's border.
 *
 * @param top    The margin on the top side.
 * @param right  The margin on the right side.
 * @param bottom The margin on the bottom side.
 * @param left   The margin on the left side.
 */
public record Margin(int top, int right, int bottom, int left) {

    /**
     * Returns the total horizontal margin (left + right).
     *
     * @return the combined left and right margins
     */
    public int getMarginHorizontal() {
        return this.left + this.right;
    }

    /**
     * Returns the total vertical margin (top + bottom).
     *
     * @return the combined top and bottom margins
     */
    public int getMarginVertical() {
        return this.top + this.bottom;
    }
}
