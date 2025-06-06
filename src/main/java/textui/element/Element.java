package textui.element;

import textui.exception.ChildrenNotAllowedException;
import textui.exception.ValueCannotBeSetException;
import textui.helper.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a UI element that can be rendered as text on a screen.
 * 
 * <p>An Element has configurable dimensions, padding, margin, border, and display properties.
 * It supports nesting child elements and can render itself and its children according to
 * different display modes (BLOCK, INLINE, FLEX).</p>
 * 
 * <p>Elements manage their own text content and visual layout constraints such as min/max width
 * and height, alignment, and fill behavior.</p>
 * 
 * <p>Note: Some properties (e.g., padding, margin, display) cannot be changed once children
 * are added to the element.</p>
 */
public class Element {
    private class ExtraSpacing {
        private final boolean WIDTH;
        private final FlexAlign ALIGNMENT;

        public ExtraSpacing(boolean width, FlexAlign alignment) {
            this.WIDTH = width;
            this.ALIGNMENT = alignment;
        }

        private int getExtraSpaceAlongTheXAxis() {
            int contentWidth = width;
            AtomicInteger totalChildrenWidth = new AtomicInteger(0);
            childElements.forEach(child -> totalChildrenWidth.addAndGet(child.getWidth()));
            return contentWidth - totalChildrenWidth.get();
        }

        private int getExtraSpaceAlongTheYAxis(Element child) {
            int contentHeight = height;
            return contentHeight - child.getHeight();
        }

        private int getHeightForFlexStart(Element child) {
            return 0;
        }

        private int getHeightForCenter(Element child) {
            return this.getExtraSpaceAlongTheYAxis(child) / 2;
        }

        private int getHeightForFlexEnd(Element child) {
            return this.getExtraSpaceAlongTheYAxis(child);
        }

        private int getWidthForFlexStart() {
            return 0;
        }

        private int getWidthForFlexEnd() {
            return this.getExtraSpaceAlongTheXAxis();
        }

        private int getWidthForCenter() {
            return this.getExtraSpaceAlongTheXAxis() / 2;
        }

        private int getWidthForSpaceApart() {
            return this.getExtraSpaceAlongTheXAxis() / (childElements.size()-1);
        }

        private int getWidthForSpaceBetween() {
            return this.getExtraSpaceAlongTheXAxis() / (childElements.size()+1);
        }

        private int getForWidth(int step) {
            switch (this.ALIGNMENT) {
                case FLEX_START: return this.getWidthForFlexStart();
                case FLEX_END: return this.getWidthForFlexEnd();
                case CENTER: return this.getWidthForCenter();
                case SPACE_APART: return step>0 ? this.getWidthForSpaceApart() : 0;
                case SPACE_BETWEEN: return this.getWidthForSpaceBetween();
                default: return 0;
            }
        }

        private int getForHeight(Element child) {
            switch (this.ALIGNMENT) {
                case FLEX_START:
                case SPACE_APART:
                case SPACE_BETWEEN: 
                    return this.getHeightForFlexStart(child);
                case CENTER:
                    return this.getHeightForCenter(child);
                case FLEX_END:
                    return this.getHeightForFlexEnd(child);
                default:
                    return 0;
            }
        }

        public int get(int step) {
            if (this.WIDTH) return this.getForWidth(step);
            else return 0;
        }

        public int get(Element child) {
            if (!this.WIDTH) return this.getForHeight(child);
            else return 0;
        }

        public int addToPrevRowWidth(int step) {
            if (this.ALIGNMENT == FlexAlign.SPACE_APART || this.ALIGNMENT == FlexAlign.SPACE_BETWEEN) {
                return this.get(step);
            } else {
                return 0;
            }
        }

    }

    // VARIABLES
        /**
     * The border configuration for this element, indicating which sides have borders.
     * Defaults to no borders on any side.
     */
    protected Border border = new Border(false, false, false, false);

    /**
     * The padding inside the element, specifying space between content and border.
     * Initialized with zero padding on all sides.
     */
    protected Padding padding = new Padding(0, 0, 0, 0);

    /**
     * The margin outside the element, specifying space between this element and others.
     * Initialized with zero margin on all sides.
     */
    protected Margin margin = new Margin(0, 0, 0, 0);

    /**
     * The display mode for this element, determining layout behavior of its children.
     * Defaults to BLOCK display.
     */
    protected Display display = Display.BLOCK;

    /**
     * The alignment of child elements along the main axis (justify-content).
     * Defaults to FLEX_START (start-aligned).
     */
    protected FlexAlign justifyContent = FlexAlign.FLEX_START;

    /**
     * The alignment of child elements along the cross axis (align-items).
     * Defaults to FLEX_START (start-aligned).
     */
    protected FlexAlign alignItems = FlexAlign.FLEX_START;


    // IF TRUE, THE EMPTY SPACES WILL BE FILLED WITH HASHES (#s)
    /**
     * Whether the element should fill its available space.
     * Defaults to false.
     */
    protected boolean fill = false;

    /**
     * The current height of the element, including content and spacing.
     * Defaults to 0.
     */
    protected int height = 0;

    /**
     * The current width of the element, including content and spacing.
     * Defaults to 0.
     */
    protected int width = 0;

    /**
     * The minimum allowed height of the element.
     * Defaults to 0.
     */
    protected int minHeight = 0;

    /**
     * The minimum allowed width of the element.
     * Defaults to 0.
     */
    protected int minWidth = 0;

    /**
     * The maximum allowed height of the element.
     * Defaults to Integer.MAX_VALUE (no maximum).
     */
    protected int maxHeight = Integer.MAX_VALUE;

    /**
     * The maximum allowed width of the element.
     * Defaults to Integer.MAX_VALUE (no maximum).
     */
    protected int maxWidth = Integer.MAX_VALUE;

    /**
     * Indicates if the height has been explicitly set by the user.
     * Defaults to false.
     */
    protected boolean heightSet = false;

    /**
     * Indicates if the width has been explicitly set by the user.
     * Defaults to false.
     */
    protected boolean widthSet = false;

    /**
     * The list of child elements contained within this element.
     * Initially empty.
     */
    protected List<Element> childElements = new ArrayList<>();

    /**
     * The 2D screen buffer representing the rendered characters of this element.
     * Each inner list is a row of characters.
     */
    protected List<List<Character>> screen = new ArrayList<>();


    // CONTRUCTORS
    /**
     * Default constructor.
     * Initializes an Element with no explicit size set.
     */
    public Element() {
        
    }

    /**
     * Constructs a square Element with given size for both height and width.
     * Marks height and width as explicitly set.
     *
     * @param size the height and width to set for this element
     */
    public Element(int size) {
        this(size, size);

        this.heightSet = true;
        this.widthSet = true;
    }

    /**
     * Constructs an Element with the specified height and width.
     * Marks height and width as explicitly set.
     *
     * @param height the height of the element
     * @param width  the width of the element
     */
    public Element(int height, int width) {
        this.height = height;
        this.width = width;

        this.heightSet = true;
        this.widthSet = true;
    }


    // GETTERS
    /**
     * Returns the alignment setting for items along the cross axis in a flex container.
     *
     * @return the current FlexAlign value for alignItems
     */
    public FlexAlign getAlignItems() {
        return this.alignItems;
    }

    /**
     * Returns the Border object representing this element's border configuration.
     *
     * @return the Border of this element
     */
    public Border getBorder() {
        return this.border;
    }

    /**
     * Returns the display mode of this element (e.g., block, inline, flex).
     *
     * @return the current Display value
     */
    public Display getDisplay() {
        return this.display;
    }

    /**
     * Returns whether this element is set to fill its container.
     *
     * @return true if the element fills its container; false otherwise
     */
    public boolean getFill() {
        return this.fill;
    }

   /**
     * Returns the total height of the element including padding, margin, and borders.
     * The height is clamped between the minimum and maximum height settings.
     *
     * @return the calculated height of the element
     */
    public int getHeight() {
        // Height should including padding top and bottom, margin top and bottom,
        // and borders top and bottom.
        int totalHeight = this.height 
            + this.padding.getPaddingVertical() 
            + this.margin.getMarginVertical() 
            + this.border.getBorderVertical();

        return Math.min(Math.max(this.minHeight, totalHeight), this.maxHeight);
    }

    /**
     * Returns the justification setting for content along the main axis in a flex container.
     *
     * @return the current FlexAlign value for justifyContent
     */
    public FlexAlign getJustifyContent() {
        return this.justifyContent;
    }

    /**
     * Returns the Margin object representing this element's margin configuration.
     *
     * @return the Margin of this element
     */
    public Margin getMargin() {
        return this.margin;
    }

    /**
     * Returns the maximum height allowed for this element.
     *
     * @return the maximum height
     */
    public int getMaxHeight() {
        return this.maxHeight;
    }

    /**
     * Returns the maximum width allowed for this element.
     *
     * @return the maximum width
     */
    public int getMaxWidth() {
        return this.maxWidth;
    }

    /**
     * Returns the minimum height allowed for this element.
     *
     * @return the minimum height
     */
    public int getMinHeight() {
        return this.minHeight;
    }

    /**
     * Returns the minimum width allowed for this element.
     *
     * @return the minimum width
     */
    public int getMinWidth() {
        return this.minWidth;
    }

    /**
     * Returns the Padding object representing this element's padding configuration.
     *
     * @return the padding of this element
     */
    public Padding getPadding() {
        return this.padding;
    }

    /**
     * Returns the current screen buffer of the element as a list of rows of characters.
     * This method triggers a printScreen call to update the screen before returning it.
     *
     * @return the screen buffer as a list of character rows
     */
    public List<List<Character>> getScreen() {
        this.printScreen();
        return this.screen;
    }

    /**
     * Returns the total width of this element, including padding, margin, and border.
     * The returned width is constrained between the minimum and maximum width limits.
     *
     * @return the total width of the element
     */
    public int getWidth() {
        // Width should including padding top and bottom, margin top and bottom,
        // and borders top and bottom.
        int totalWidth = this.width
            + this.padding.getPaddingHorizontal() 
            + this.margin.getMarginHorizontal() 
            + this.border.getBorderHorizontal();

        return Math.min(Math.max(this.minWidth, totalWidth), this.maxWidth);
    }



    // SETTERS
    /**
     * Sets the height of this element and marks the height as explicitly set.
     *
     * @param height the height to set
     * @return this element instance for chaining
     */
    public Element setHeight(int height) {
        this.height = height;
        this.heightSet = true;
        return this;
    }

    /**
     * Sets the width of this element and marks the width as explicitly set.
     *
     * @param width the width to set
     * @return this element instance for chaining
     */
    public Element setWidth(int width) {
        this.width = width;
        this.widthSet = true;
        return this;
    }

    /**
     * Sets the minimum height constraint for this element.
     *
     * @param height the minimum height to set
     * @return this element instance for chaining
     */
    public Element setMinHeight(int height) {
        this.minHeight = height;
        return this;
    }

    /**
     * Sets the maximum height constraint for this element.
     *
     * @param height the maximum height to set
     * @return this element instance for chaining
     */
    public Element setMaxHeight(int height) {
        this.maxHeight = height;
        return this;
    }

    /**
     * Sets the minimum width constraint for this element.
     *
     * @param width the minimum width to set
     * @return this element instance for chaining
     */
    public Element setMinWidth(int width) {
        this.minWidth = width;
        return this;
    }

    /**
     * Sets the maximum width constraint for this element.
     *
     * @param width the maximum width to set
     * @return this element instance for chaining
     */
    public Element setMaxWidth(int width) {
        this.maxWidth = width;
        return this;
    }

    /**
     * Sets the justification of content along the main axis.
     *
     * @param justifyContent the flex alignment for justifying content
     * @return this element instance for chaining
     */
    public Element setJustifyContent(FlexAlign justifyContent) {
        this.justifyContent = justifyContent;
        return this;
    }

    /**
     * Sets the alignment of items along the cross axis.
     *
     * @param alignItems the flex alignment for aligning items
     * @return this element instance for chaining
     */
    public Element setAlignItems(FlexAlign alignItems) {
        this.alignItems = alignItems;
        return this;
    }

    /**
     * Sets whether this element should fill the available space.
     *
     * @param fill true to fill available space, false otherwise
     * @return this element instance for chaining
     */
    public Element setFill(boolean fill) {
        this.fill = fill;
        return this;
    }

    /**
     * Sets the border on all sides of this element to true.
     *
     * @return this element instance for chaining
     */
    public Element setBorder() {
        return this.setBorder(true, true);
    }

    /**
     * Sets the border for top and bottom and right and left sides of this element.
     *
     * @param topAndBottom true to set border on top and bottom sides
     * @param rightAndLeft true to set border on right and left sides
     * @return this element instance for chaining
     */
    public Element setBorder(boolean topAndBottom, boolean rightAndLeft) {
        return this.setBorder(topAndBottom, rightAndLeft, topAndBottom, rightAndLeft);
    }

    /**
     * Sets the border individually on each side of this element.
     *
     * @param top    true to set border on the top side
     * @param right  true to set border on the right side
     * @param bottom true to set border on the bottom side
     * @param left   true to set border on the left side
     * @return this element instance for chaining
     */
    public Element setBorder(boolean top, boolean right, boolean bottom, boolean left) {
        this.border = new Border(top, right, bottom, left);
        return this;
    }

    /**
     * Sets uniform padding on all four sides of this element.
     *
     * @param padding the padding size to apply on top, right, bottom, and left
     * @return this element instance for chaining
     * @throws ValueCannotBeSetException if padding is set after children are inserted
     */
    public Element setPadding(int padding) throws ValueCannotBeSetException {
        return this.setPadding(padding, padding);
    }

    /**
     * Sets padding on top and bottom and right and left sides of this element.
     *
     * @param topAndBottom the padding size to apply on top and bottom sides
     * @param rightAndLeft the padding size to apply on right and left sides
     * @return this element instance for chaining
     * @throws ValueCannotBeSetException if padding is set after children are inserted
     */
    public Element setPadding(int topAndBottom, int rightAndLeft) throws ValueCannotBeSetException {
        return this.setPadding(topAndBottom, rightAndLeft, topAndBottom, rightAndLeft);
    }

    /**
     * Sets padding individually on each side of this element.
     *
     * @param top    padding size on the top side
     * @param right  padding size on the right side
     * @param bottom padding size on the bottom side
     * @param left   padding size on the left side
     * @return this element instance for chaining
     * @throws ValueCannotBeSetException if padding is set after children are inserted
     */
    public Element setPadding(int top, int right, int bottom, int left) throws ValueCannotBeSetException {
        if (this.childElements.size() > 0) {
            throw new ValueCannotBeSetException("padding cannot be set after inserting a child");
        }

        this.padding = new Padding(top, right, bottom, left);
        return this;
    }

    /**
     * Sets uniform margin on all four sides of this element.
     *
     * @param margin the margin size to apply on top, right, bottom, and left
     * @return this element instance for chaining
     * @throws ValueCannotBeSetException if margin is set after children are inserted
     */
    public Element setMargin(int margin) throws ValueCannotBeSetException {
        return this.setMargin(margin, margin);
    }

    /**
     * Sets margin on top and bottom and right and left sides of this element.
     *
     * @param topAndBottom the margin size to apply on top and bottom sides
     * @param rightAndLeft the margin size to apply on right and left sides
     * @return this element instance for chaining
     * @throws ValueCannotBeSetException if margin is set after children are inserted
     */
    public Element setMargin(int topAndBottom, int rightAndLeft) throws ValueCannotBeSetException {
        return this.setMargin(topAndBottom, rightAndLeft, topAndBottom, rightAndLeft);
    }

    /**
     * Sets margin individually on each side of this element.
     *
     * @param top    margin size on the top side
     * @param right  margin size on the right side
     * @param bottom margin size on the bottom side
     * @param left   margin size on the left side
     * @return this element instance for chaining
     * @throws ValueCannotBeSetException if margin is set after children are inserted
     */
    public Element setMargin(int top, int right, int bottom, int left) throws ValueCannotBeSetException {
        if (this.childElements.size() > 0) {
            throw new ValueCannotBeSetException("margin cannot be set after inserting a child");
        }

        this.margin = new Margin(top, right, bottom, left);
        return this;
    }

    /**
     * Sets the display mode of this element.
     *
     * @param display the display mode to set
     * @return this element instance for chaining
     * @throws ValueCannotBeSetException if display is set after children are inserted
     */
    public Element setDisplay(Display display) throws ValueCannotBeSetException {
        if (this.childElements.size() > 0) {
            throw new ValueCannotBeSetException("display cannot be set after inserting a child");
        }

        this.display = display;
        return this;
    }


    // BEHAVIOR METHODS

    /**
     * Adds borders to the element's screen buffer based on the border settings.
     * Calls the respective methods to add top, right, bottom, and left borders
     * if they are enabled in the {@link #border} property.
     */
    protected void addBorder() {
        if (this.border.top()) this.addTopBorder();
        if (this.border.right()) this.addRightBorder();
        if (this.border.bottom()) this.addBottomBorder();
        if (this.border.left()) this.addLeftBorder();
    }

    private void addBottomBorder() {
        int height = this.getHeight();

        int bottomIndex = height - this.margin.bottom() - 1;
        List<Character> bottom = this.screen.get(bottomIndex);

        int start = this.margin.left();
        int end = bottom.size() - this.margin.right();

        for (int i = start; i < end; i++) {
            bottom.set(i, '#');
        }
    }

    private void addLeftBorder() {
        int start = this.margin.top();
        int end = this.screen.size() - this.margin.bottom();

        for (int i = start; i < end; i++) {
            List<Character> row = this.screen.get(i);

            int index = this.margin.left();
            row.set(index, '#');
        }
    }

    private void addRightBorder() {
        int width = this.getWidth();

        int start = this.margin.top();
        int end = this.screen.size() - this.margin.bottom();

        for (int i = start; i < end; i++) {
            List<Character> row = this.screen.get(i);

            int index = width - this.margin.right() - 1;
            row.set(index, '#');
        }
    }

    private void addTopBorder() {
        int topIndex = this.margin.top();
        List<Character> top = this.screen.get(topIndex);

        int start = this.margin.left();
        int end = top.size() - this.margin.right();

        for (int i = start; i < end; i++) {
            top.set(i, '#');
        }
    }


    private void addScreensUsingInlineMethod() {
        int prevChildHeight = 0;
        int prevRowWidth = 0;
        List<Integer> rowChildHeights = new ArrayList<>();

        for (int i = 0; i < this.childElements.size(); i++) {
            Element child = this.childElements.get(i);

            if ((this.width-prevRowWidth) >= child.getWidth()) {
                int heightStep = prevChildHeight + this.padding.top();
                heightStep = heightStep + (this.border.top() ? 1 : 0);

                int widthStep = prevRowWidth + this.padding.left();
                widthStep = widthStep + (this.border.left() ? 1 : 0);

                this.printChildScreen(child, heightStep, widthStep);

                prevRowWidth += child.getWidth();
                rowChildHeights.add(child.getHeight());
            } else {
                prevChildHeight += Helper.findLargestInList(rowChildHeights);
                rowChildHeights.clear();

                if (this.height-prevChildHeight < child.getHeight()) return;

                int heightStep = prevChildHeight + this.padding.top() + (this.border.top() ? 1 : 0);
                int widthStep = this.padding.left() + (this.border.left() ? 1 : 0);
                this.printChildScreen(child, heightStep, widthStep);

                prevRowWidth = child.getWidth();
            }
        }
    }

    private void addScreensUsingBlockMethod() {
        int prevChildHeight = 0;
        for (int i = 0; i < childElements.size(); i++) {
            Element child = childElements.get(i);

            int borderTop = this.border.top() ? 1 : 0;
            int heightStep = prevChildHeight + this.padding.top() + borderTop;

            int borderLeft = this.border.left() ? 1 : 0;
            int widthStep = this.padding.left() + borderLeft;

            this.printChildScreen(child, heightStep, widthStep);

            prevChildHeight += child.getHeight();
        }
    }

    private void addScreensUsingFlexMethod() {
        int prevChildHeight = 0;
        int prevRowWidth = 0;
        List<Integer> rowChildHeights = new ArrayList<>();

        ExtraSpacing widthSpacing = new ExtraSpacing(true, this.justifyContent);
        ExtraSpacing heightSpacing = new ExtraSpacing(false, this.alignItems);

        for (int i = 0; i < this.childElements.size(); i++) {
            Element child = this.childElements.get(i);

            int heightStep = heightSpacing.get(child)+prevChildHeight+this.padding.top();
            int widthStep = widthSpacing.get(i)+prevRowWidth+this.padding.left();

            heightStep = heightStep + (this.border.top() ? 1 : 0);
            widthStep = widthStep + (this.border.left() ? 1 : 0);

            this.printChildScreen(child, heightStep, widthStep);

            prevRowWidth += child.getWidth() + widthSpacing.addToPrevRowWidth(i);
            rowChildHeights.add(child.getHeight());
        }
    }

    /**
     * Calculates the effective height of this element, considering its own size
     * and the layout of its child elements according to the display mode.
     * 
     *  If the height is manually set (heightSet == true), returns the set height.
     *  If the display mode is INLINE, delegates to {@link #getElementHeightInline()}.
     *  If the display mode is FLEX, returns the maximum height among children.
     *  If the display mode is BLOCK, returns the sum of all children heights.
     * 
     * The returned height is clamped between the element's minHeight and maxHeight.
     * 
     * @return The calculated height of the element.
     */
    protected int getElementHeight() {
        if (this.heightSet) return this.height;

        if (this.display.equals(Display.INLINE)) {
            return this.getElementHeightInline();
        }

        int height = 0;

        for (Element child : this.childElements) {
            int childHeight = child.getElementHeight();

            if (this.display.equals(Display.FLEX) && childHeight > height) {
                height = childHeight;
            } else if (this.display.equals(Display.BLOCK)) {
                height += childHeight;
            }
        }

        return Math.min(Math.max(height, this.minHeight), this.maxHeight);
    }

    /**
     * Calculates the height of this element when the display mode is INLINE.
     * 
     * The height is calculated by fitting children horizontally up to maxWidth,
     * wrapping to new rows as needed. The height of each row is the largest child height
     * in that row. The total height is the sum of all row heights.
     * 
     * If maxWidth is -1 (no max width), the height is the maximum child height.
     * 
     * @return The calculated height when using INLINE display.
     */
    protected int getElementHeightInline() {
        int rowWidth = 0;
        int height = 0;
        List<Integer> rowElementsHeight = new ArrayList<>();

        for (Element child : this.childElements) {
            int childHeight = child.getElementHeight();
            int childWidth = child.getElementWidth();

            if (this.maxWidth == -1 && childHeight > height) {
                height = childHeight;
            } else if (this.maxWidth != -1 && rowWidth + childWidth > maxWidth) {
                // Wrap to new row
                rowWidth = childWidth;

                // Add height of completed row (max child height)
                height += Helper.findLargestInList(rowElementsHeight);
                rowElementsHeight.clear();

                rowElementsHeight.add(childHeight);
            } else if (this.maxWidth != -1) {
                rowWidth += childWidth;
                rowElementsHeight.add(childHeight);
            }
        }

        if (!rowElementsHeight.isEmpty()) {
            height += Helper.findLargestInList(rowElementsHeight);
        }

        return height;
    }

    /**
     * Calculates the effective width of this element, considering its own size
     * and the layout of its child elements according to the display mode.
     * 
     *  If the width is manually set (widthSet == true), returns the set width.
     *  If the display mode is BLOCK, the width is the maximum width among children.
     *  Otherwise (e.g., INLINE or FLEX), the width is the sum of all child widths.
     * 
     * The returned width is clamped between the element's minWidth and maxWidth.
     * 
     * @return The calculated width of the element.
     */
    protected int getElementWidth() {
        if (this.widthSet) return this.width;

        int width = 0;
        for (Element child : this.childElements) {
            int childWidth = child.getElementWidth();
            if (this.display.equals(Display.BLOCK) && childWidth > width) {
                // BLOCK display: width is max child's width
                width = childWidth;
            } else {
                // INLINE or FLEX display: width is sum of children widths
                width += childWidth;
            }
        }

        return Math.min(Math.max(width, this.minWidth), this.maxWidth);
    }

    /**
     * Inserts a single child element into this element's children list.
     *
     * @param child the child element to insert
     * @return this element instance for chaining
     * @throws ChildrenNotAllowedException if this element does not allow children
     */
    public Element insertChild(Element child) 
    throws ChildrenNotAllowedException {

        this.childElements.add(child);
        return this;
    }

    /**
     * Inserts multiple child elements into this element's children list.
     *
     * @param children the list of child elements to insert
     * @return this element instance for chaining
     * @throws ChildrenNotAllowedException if this element does not allow children
     */
    public Element insertChildren(List<Element> children) 
    throws ChildrenNotAllowedException {

        for (Element child : children) {
            this.insertChild(child);
        }
        
        return this;
    }

    /**
     * Renders the element's content onto its screen representation,
     * including borders and child elements according to the display mode.
     *
     * The method resets the current screen, adds borders, and then adds
     * child elements using the appropriate layout method based on the
     * element's display property:
     * <ul>
     *   <li>{@link Display#BLOCK} uses block layout</li>
     *   <li>{@link Display#INLINE} uses inline layout</li>
     *   <li>{@link Display#FLEX} uses flex layout</li>
     * </ul>
     *
     * @return this element instance for chaining
     */
    public Element printScreen() {
        this.resetScreen();
        this.addBorder();

        // Add children screens
        if (display == Display.BLOCK) {
            this.addScreensUsingBlockMethod();
        } 
        
        else if (this.display == Display.INLINE) {
            this.addScreensUsingInlineMethod();
        } 

        else if (display == Display.FLEX) {
            this.addScreensUsingFlexMethod();
        }
        
        return this;
    }

    private void printChildScreen(Element child, int heightStep, int widthStep) {
        List<List<Character>> childScreen = child.getScreen();

        for (int j = 0; j < child.getHeight(); j++) {
            for (int k = 0; k < child.getWidth(); k++) {
                char c = childScreen.get(j).get(k);

                try {
                    this.screen.get(heightStep+j).set(widthStep+k, c);
                }

                catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
    }

    /**
     * Resets the screen buffer to an empty state based on the current element size.
     * 
     * This method clears the current screen content and reinitializes it as a
     * 2D list of characters with dimensions equal to the element's height and width.
     * Each cell is filled with '#' if the fill flag is true; otherwise, it is
     * filled with a space character ' '.
     * 
     * The screen acts as a visual buffer representing the element's rendering area.
     */
    protected void resetScreen() {
        this.screen.clear();

        int height = this.getHeight();
        int width = this.getWidth();

        for (int h = 0; h < height; h++) {
            List<Character> row = new ArrayList<>();
            for (int w = 0; w < width; w++) {
                row.add(this.fill ? '#' : ' ');
            }
            this.screen.add(row);
        }
    }
}
