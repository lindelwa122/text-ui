package textui.element;

/**
 * Represents the padding inside an element.
 * Padding defines the space between the element's content and its border.
 *
 * @param top    The padding on the top side.
 * @param right  The padding on the right side.
 * @param bottom The padding on the bottom side.
 * @param left   The padding on the left side.
 */
public record Padding(int top, int right, int bottom, int left) {

    /**
     * Returns the total horizontal padding (left + right).
     *
     * @return the combined left and right paddings
     */
    public int getPaddingHorizontal() {
        return this.left + this.right;
    }

    /**
     * Returns the total vertical padding (top + bottom).
     *
     * @return the combined top and bottom paddings
     */
    public int getPaddingVertical() {
        return this.top + this.bottom;
    }
}
