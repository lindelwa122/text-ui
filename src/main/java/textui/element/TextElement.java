package textui.element;

import java.util.ArrayList;
import java.util.List;

import textui.exception.ChildrenNotAllowedException;
import textui.exception.ValueCannotBeSetException;
import textui.helper.Helper;

/**
 * Represents a text-based UI element that displays formatted paragraphs of text.
 * 
 * This class extends Element and manages text content organized as a list of
 * paragraphs, where each paragraph is a list of words. It supports text alignment
 * (left, right, center) and word casing options.
 * 
 * TextElement does not support child elements and overrides insertChild methods
 * to prevent adding sub-elements.
 * 
 * The class is responsible for rendering the text content within its allocated
 * area, respecting padding, margin, borders, and alignment settings.
 */
public class TextElement extends Element {
    // VARIABLES
    /**
     * Stores the text content of the element as a list of paragraphs,
     * where each paragraph is represented as a list of words (strings).
     * 
     * Each inner List represents one line or paragraph broken down into words,
     * allowing flexible text processing like alignment or wrapping.
     */
    protected List<List<String>> paragraph = new ArrayList<>();
    private TextAlign textAlign = TextAlign.LEFT;
    private Case wordCase = Case.NOEDIT;


    // CONSTRUCTORS 
    /**
     * Constructs a TextElement with default dimensions.
     */
    public TextElement() {
        super();
    }

    /**
     * Constructs a TextElement with the same value used
     * for both height and width.
     *
     * @param size the value to use for both height and width
     */
    public TextElement(int size) {
        super(size);
    }

    /**
     * Constructs a TextElement with specified height and width.
     *
     * @param height the initial height of the element
     * @param width  the initial width of the element
     */
    public TextElement(int height, int width) {
        super(height, width);
    }


    // SETTERS
   /**
     * Sets the minimum height of this TextElement.
     *
     * @param height the minimum height to set
     * @return this TextElement instance for method chaining
     */
    @Override
    public TextElement setMinHeight(int height) {
        super.setMinHeight(height);
        return this;
    }

    /**
     * Sets the maximum height of this TextElement.
     *
     * @param height the maximum height to set
     * @return this TextElement instance for method chaining
     */
    @Override
    public TextElement setMaxHeight(int height) {
        super.setMaxHeight(height);
        return this;
    }

    /**
     * Sets the minimum width of this TextElement.
     *
     * @param width the minimum width to set
     * @return this TextElement instance for method chaining
     */
    @Override
    public TextElement setMinWidth(int width) {
        super.setMinWidth(width);
        return this;
    }

    /**
     * Sets the maximum width of this TextElement.
     *
     * @param width the maximum width to set
     * @return this TextElement instance for method chaining
     */
    @Override
    public TextElement setMaxWidth(int width) {
        super.setMaxWidth(width);
        return this;
    }

    /**
     * Sets the text content of this TextElement, applying the configured word casing
     * and wrapping words into lines according to the element's width and height constraints.
     * 
     * <p>The method processes the input text by splitting it into words and transforming
     * each word according to the {@code wordCase} setting, which can be NORMAL, TITLE,
     * UPPERCASE, LOWERCASE, or NOEDIT. It then arranges these words into lines that fit
     * within the available width, respecting padding, margin, and border sizes.</p>
     * 
     * <p>If the width or height of the element is not yet set, it will calculate and set
     * these values based on the text length and configured minimum and maximum dimensions.</p>
     * 
     * @param text the string text content to set; if empty or blank, the method returns immediately
     * @return this TextElement instance for method chaining
     */
    public TextElement setText(String text) {
        List<String> words = new ArrayList<>();

        if (text.isEmpty() || text.isBlank()) return this;

        for (String word : text.split(" ")) {
            switch (this.wordCase) {
                case NORMAL:
                    if (words.size() == 0
                        || words.getLast().endsWith(".")
                        || words.getLast().endsWith("!")
                        || words.getLast().endsWith("?")) {

                        words.add(Helper.capitalise(word));
                    }

                    else {
                        words.add(word);
                    }
                    break;
            
                case TITLE:
                    words.add(Helper.capitalise(word));
                    break;

                case UPPERCASE:
                    words.add(word.toUpperCase());
                    break;

                case LOWERCASE:
                    words.add(word.toLowerCase());
                    break;

                case NOEDIT:
                    words.add(word);
                    break;
            }
        };
        
        if (!this.widthSet) {
            if (text.length() < this.minWidth) {
                this.width = this.minWidth;
            }

            else if (text.length() > this.maxWidth) {
                this.width = this.maxWidth;
            }

            else {
                this.width = text.length() 
                    + this.padding.getPaddingHorizontal() 
                    + this.margin.getMarginHorizontal() 
                    + this.border.getBorderHorizontal();
            }

            this.widthSet = true;
        }

        if (!this.heightSet) {
            this.height = this.getHeight();

            if (this.minHeight != -1 && this.height < this.minHeight) {
                this.height = this.minHeight;
            }

            else if (maxHeight != -1 && this.height > this.maxHeight) {
                this.height = this.maxHeight;
            }

            else {
                this.height = text.length() / this.width;
            }

            this.heightSet = true;
        }

        int contentHeight = this.getHeight();
        int contentWidth = this.getWidth();

        for (int i = 0; i < contentHeight; i++) {
            if (words.isEmpty()) break;

            int rowCount = 0;
            List<String> row = new ArrayList<>();

            while (rowCount <= contentWidth) {
                if (words.isEmpty()) break;

                String word = words.removeFirst();

                if (rowCount + word.length() > contentWidth) {
                    words.addFirst(word);
                    break;
                };

                row.add(word);
                rowCount += word.length() + 1;
            }
            paragraph.add(row);
        }

        return this;
    }

   /**
     * Sets the text alignment for this TextElement.
     *
     * @param textAlign the desired text alignment (e.g., LEFT, RIGHT, CENTER)
     * @return this TextElement instance for method chaining
     */
    public TextElement setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    /**
     * Sets the casing style for the words in this TextElement.
     * 
     * <p>This method must be called before any text is set. If text
     * has already been added (i.e., the paragraph is not empty), 
     * it throws a {@link ValueCannotBeSetException}.</p>
     *
     * @param wordCase the case style to apply to words (e.g., NORMAL, TITLE, UPPERCASE)
     * @return this TextElement instance for method chaining
     * @throws ValueCannotBeSetException if called after text content has been set
     */
    public TextElement setWordCase(Case wordCase) throws ValueCannotBeSetException {
        if (paragraph.size() > 0) {
            throw new ValueCannotBeSetException("wordCase cannot be set after setting text");
        }

        this.wordCase = wordCase;
        return this;
    }


    // GETTERS
   /**
     * Returns the current word case style used in this TextElement.
     *
     * @return the word case style (e.g., NORMAL, TITLE, UPPERCASE)
     */
    public Case getWordCase() {
        return this.wordCase;
    }

    /**
     * Returns the current text alignment of this TextElement.
     *
     * @return the text alignment (e.g., LEFT, RIGHT, CENTER)
     */
    public TextAlign getTextAlign() {
        return this.textAlign;
    }


    // BEHAVIOR METHODS
   /**
     * This element does not support adding child elements.
     *
     * @param child the child element to insert
     * @throws ChildrenNotAllowedException always thrown because this element cannot have children
     */
    @Override
    public Element insertChild(Element child) throws ChildrenNotAllowedException {
        throw new ChildrenNotAllowedException("This element cannot have sub-elements");
    }

    /**
     * This element does not support adding multiple child elements.
     *
     * @param children the list of child elements to insert
     * @throws ChildrenNotAllowedException always thrown because this element cannot have children
     */
    @Override
    public Element insertChildren(List<Element> children) throws ChildrenNotAllowedException {
        throw new ChildrenNotAllowedException("This element cannot have sub-elements");
    }

    /**
     * Prints a single row of words onto the screen buffer at a calculated position
     * that accounts for padding, margin, and border offsets.
     *
     * @param row the list of words to print in the row
     * @param rowCount the zero-based index of the row being printed (relative to content)
     * @param extraSpacing additional horizontal offset (in characters) to apply before printing
     */
    private void printRow(List<String> row, int rowCount, int extraSpacing) {
        int borderTop = this.border.top() ? 1 : 0;
        int heightStep = this.padding.top() + this.margin.top() + borderTop;

        int borderLeft = this.border.left() ? 1 : 0;
        int widthStep = this.padding.left() + this.margin.left() + borderLeft;
        int charCount = 0;

        int targetRow = heightStep + rowCount;
        int targetCol = widthStep + extraSpacing;

        for (String word : row) {
            for (int j = 0; j < word.length(); j++) {
                try {
                    this.screen.get(targetRow).set(targetCol + j + charCount, word.charAt(j));
                } catch (IndexOutOfBoundsException e) {
                    // If out of bounds, skip the character
                    continue;
                }
            }
            charCount = charCount + word.length() + 1;
        }
    }

    /**
     * Renders the current text element onto the screen buffer, applying border and alignment.
     * Resets the screen before printing, then adds borders.
     * Depending on the text alignment setting, adjusts horizontal positioning:
     * - LEFT: prints rows starting at the left edge.
     * - RIGHT: prints rows right-aligned within the available width.
     * - CENTER: prints rows centered within the available width.
     *
     * @return this TextElement instance for chaining.
     */
    @Override
    public TextElement printScreen() {
        this.resetScreen();
        this.addBorder();

        if (this.textAlign == TextAlign.LEFT) {
            for (int i = 0; i < this.paragraph.size(); i++) {
                List<String> row = this.paragraph.get(i);
                this.printRow(row, i, 0);
            }
        }

        else if (this.textAlign == TextAlign.RIGHT) {
            for (int i = 0; i < this.paragraph.size(); i++) {
                List<String> row = this.paragraph.get(i);
                this.printRow(row, i, Helper.findExtraSpaceInRow(row, this.width));
            }
        }

        else if (textAlign == TextAlign.CENTER) {
            for (int i = 0; i < this.paragraph.size(); i++) {
                List<String> row = this.paragraph.get(i);
                this.printRow(row, i, Helper.findExtraSpaceInRow(row, this.width) / 2);
            }
        }

        return this;
    }
}
