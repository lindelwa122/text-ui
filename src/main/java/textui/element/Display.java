package textui.element;

/**
 * Specifies how an element should be laid out within its parent container
 * in the terminal-based UI layout system.
 */
public enum Display {

    /**
     * Element is rendered on its own line, taking up the full available horizontal space.
     * Stacks vertically with other block elements.
     */
    BLOCK,

    /**
     * Element is rendered inline with other elements, allowing horizontal flow.
     * Respects surrounding inline context and wraps as needed.
     */
    INLINE,

    /**
     * Element participates in flexible layout logic, sharing space
     * dynamically with other children under a flex-enabled parent.
     * This may affect alignment and distribution.
     */
    FLEX;
}
