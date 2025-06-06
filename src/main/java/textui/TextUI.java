package textui;

import java.util.List;
import textui.element.Element;

/**
 * The TextUI class manages and renders a UI based on a root Element.
 */
public class TextUI {
    private Element body;

    /**
     * Constructs a TextUI instance with the given root Element.
     * 
     * @param body the root Element representing the UI body
     */
    public TextUI(Element body) {
        this.body = body; 
    }

    /**
     * Draws the UI by printing the character screen representation of the root Element.
     * This method retrieves the rendered screen from the root Element and prints it line by line.
     */
    public void draw() {
        String output = "";

        List<List<Character>> screen = this.body.getScreen();

        for (List<Character> row : screen) {
            for (char c : row) output += c;
            output += '\n';
        }

        System.out.println(output);
    }
}
