package textui.element;

/**
 * Defines alignment options for flex layout along the main or cross axis.
 */
public enum FlexAlign {

    /**
     * Items are packed toward the start of the axis.
     */
    FLEX_START,

    /**
     * Items are packed toward the end of the axis.
     */
    FLEX_END,

    /**
     * Items are centered along the axis.
     */
    CENTER,

    /**
     * Items are distributed with equal space around them.
     * Includes space before the first and after the last item.
     */
    SPACE_BETWEEN,
    
    /**
     * Items are distributed with equal space between them.
     * No space before the first or after the last item.
     */
    SPACE_APART;
}
