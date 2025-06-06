package textui.element;

/**
 * Defines text transformation rules for rendering content in the text-based UI.
 * Determines how the case of text should be displayed inside elements.
 */
public enum Case {
    
    /** 
     * Capitalizes the first letter of each sentence and makes no further modifications on other words. 
     */
    NORMAL,

    /** 
     * Capitalizes the first letter of each word. Typically used for titles or headings.
     */
    TITLE,

    /** 
     * Converts all characters in the text to uppercase.
     */
    UPPERCASE,

    /** 
     * Converts all characters in the text to lowercase.
     */
    LOWERCASE,

    /** 
     * Prevents any case-related modification, preserving text as-is regardless of context.
     */
    NOEDIT;
}
