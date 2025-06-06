package textui.element;

import java.util.ArrayList;
import java.util.List;

/**
 * A text element that displays a list of items with bullet points.
 * Each item is added as a single-line entry prefixed by a dash ("-").
 * The layout respects optional min/max height and width constraints.
 */
public class UnsortedListElement extends TextElement {

    /**
     * Constructs an UnsortedListElement with default dimensions.
     */
    public UnsortedListElement() {
        super();
    }

    /**
     * Constructs an UnsortedListElement with the same value used
     * for both height and width.
     *
     * @param size the value to use for both height and width
     */
    public UnsortedListElement(int size) {
        super(size);
    }

    /**
     * Constructs an UnsortedListElement with specified height and width.
     *
     * @param height the initial height of the element
     * @param width the initial width of the element
     */
    public UnsortedListElement(int height, int width) {
        super(height, width);
    }

    /**
     * Sets the minimum height of this element.
     *
     * @param height the minimum height
     * @return this element for chaining
     */
    @Override
    public UnsortedListElement setMinHeight(int height) {
        super.setMinHeight(height);
        return this;
    }

    /**
     * Sets the maximum height of this element.
     *
     * @param height the maximum height
     * @return this element for chaining
     */
    @Override
    public UnsortedListElement setMaxHeight(int height) {
        super.setMaxHeight(height);
        return this;
    }

    /**
     * Sets the minimum width of this element.
     *
     * @param width the minimum width
     * @return this element for chaining
     */
    @Override
    public UnsortedListElement setMinWidth(int width) {
        super.setMinWidth(width);
        return this;
    }

    /**
     * Sets the maximum width of this element.
     *
     * @param width the maximum width
     * @return this element for chaining
     */
    @Override
    public UnsortedListElement setMaxWidth(int width) {
        super.setMaxWidth(width);
        return this;
    }

    /**
     * Adds an item to the unsorted list. The item is prefixed with a dash
     * and split into words based on available width. If height or width
     * are not explicitly set, they may be inferred from content.
     *
     * @param item the item text to add
     * @return this element for chaining
     */
    public UnsortedListElement addItem(String item) {
        if (!this.heightSet) this.height++;

        int contentHeight = Math.min(Math.max(this.minHeight, this.height), this.maxHeight);
        if (paragraph.size() >= contentHeight) return this;

        List<String> listItem = new ArrayList<>();

        if (!this.widthSet && (item.length()+item.split(" ").length+1) > this.width) {
            this.width = item.length() + item.split(" ").length + 1;
        }

        int contentWidth = Math.min(Math.max(this.minWidth, this.width), this.maxWidth);
        listItem.add("-");

        int charCount = 1;
        for (String word : item.split(" ")) {
            if (word.length() + charCount > contentWidth) break;

            listItem.add(word);
            charCount = charCount + word.length() + 1;
        }

        this.paragraph.add(listItem);
        return this;
    }
}
