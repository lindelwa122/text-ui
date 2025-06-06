package textui.element;

import java.util.ArrayList;
import java.util.List;

/**
 * A UI element representing a sorted (numbered) list of text entries.
 * Each added item is prefixed with a number (e.g., 1., 2., etc.).
 * The element respects minimum and maximum width/height constraints,
 * and automatically adjusts dimensions if not explicitly set.
 */
public class SortedListElement extends TextElement {

    /**
     * Constructs a SortedListElement with default dimensions.
     */
    public SortedListElement() {
        super();
    }

    /**
     * Constructs a SortedListElement with both width and height set to {@code size}.
     *
     * @param size the size to use for both height and width
     */
    public SortedListElement(int size) {
        super(size);
    }

    /**
     * Constructs a SortedListElement with specified height and width.
     *
     * @param height the height of the element
     * @param width  the width of the element
     */
    public SortedListElement(int height, int width) {
        super(height, width);
    }

    /**
     * Sets the minimum height of the element.
     *
     * @param height the minimum height
     * @return this element for method chaining
     */
    @Override
    public SortedListElement setMinHeight(int height) {
        super.setMinHeight(height);
        return this;
    }

    /**
     * Sets the maximum height of the element.
     *
     * @param height the maximum height
     * @return this element for method chaining
     */
    @Override
    public SortedListElement setMaxHeight(int height) {
        super.setMaxHeight(height);
        return this;
    }

    /**
     * Sets the minimum width of the element.
     *
     * @param width the minimum width
     * @return this element for method chaining
     */
    @Override
    public SortedListElement setMinWidth(int width) {
        super.setMinWidth(width);
        return this;
    }

    /**
     * Sets the maximum width of the element.
     *
     * @param width the maximum width
     * @return this element for method chaining
     */
    @Override
    public SortedListElement setMaxWidth(int width) {
        super.setMaxWidth(width);
        return this;
    }

    /**
     * Adds a new item to the sorted list. If height is not set, it grows automatically.
     * If the list is already full, the item is not added.
     * If width is not set, it is adjusted to fit the current item.
     *
     * @param item the string content to add
     * @return this element for method chaining
     */
    public SortedListElement addItem(String item) {
        if (!this.heightSet) {
            this.height++;
        }

        int contentHeight = Math.min(Math.max(this.minHeight, this.height), this.maxHeight);
        if (paragraph.size() >= contentHeight) {
            return this;
        }

        List<String> listItem = new ArrayList<>();

        // Estimate needed width if width was not set
        if (!this.widthSet) {
            int estimatedWidth = item.length() + item.split(" ").length + 2;
            this.width = Math.max(this.width, estimatedWidth);
        }

        int contentWidth = Math.min(Math.max(this.minWidth, this.width), this.maxWidth);

        // Add index number
        listItem.add((this.paragraph.size() + 1) + ".");

        // Append words until reaching max content width
        int charCount = 2; // Starts with "1." (length 2)
        for (String word : item.split(" ")) {
            if (charCount + word.length() > contentWidth) {
                break;
            }
            listItem.add(word);
            charCount += word.length() + 1; // +1 for space
        }

        this.paragraph.add(listItem);
        return this;
    }
}
