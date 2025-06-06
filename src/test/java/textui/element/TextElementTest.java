package textui.element;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import textui.TextUI;
import textui.exception.ChildrenNotAllowedException;
import textui.exception.ValueCannotBeSetException;

public class TextElementTest {
    @Test
    void testSize() {
        TextElement text = new TextElement(5, 10);
        assertEquals(5, text.getHeight());
        assertEquals(10, text.getWidth());

        TextElement text2 = new TextElement(6);
        assertEquals(6, text2.getHeight());
        assertEquals(6, text2.getWidth());
    }

    @Test
    void testSizeWithText() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement(3, 30)
                .setText("Hello! My name is Luno, and this is my friend, Luco.")
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "################################\n" + //
                                "#Hello! My name is Luno, and   #\n" + //
                                "#this is my friend, Luco.      #\n" + //
                                "#                              #\n" + //
                                "################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMinimumSize() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement(1, 1)
                .setMinHeight(4)
                .setMinWidth(30)
                .setText("Hello! My name is Luno, and this is my friend, Luco.")
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##############################\n" + //
                                "#Hello! My name is Luno, and #\n" + //
                                "#this is my friend, Luco.    #\n" + //
                                "##############################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMaximumSize() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement(10, 100)
                .setMaxHeight(4)
                .setMaxWidth(50)
                .setText("Hello! My name is Luno, and this is my friend, Luco.")
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##################################################\n" + //
                                "#Hello! My name is Luno, and this is my friend,  #\n" + //
                                "#Luco.                                           #\n" + //
                                "##################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testNoSpecifiedSize() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement()
                .setText("Hello! My name is Luno, and this is my friend, Luco.")
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "######################################################\n" + //
                                "#Hello! My name is Luno, and this is my friend, Luco.#\n" + //
                                "######################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testPadding() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement()
                .setText("Hello! My name is Luno, and this is my friend, Luco.")
                .setPadding(1, 2)
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##########################################################\n" + //
                                "#                                                        #\n" + //
                                "#  Hello! My name is Luno, and this is my friend, Luco.  #\n" + //
                                "#                                                        #\n" + //
                                "##########################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testCaseNoEdit() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement()
                .setWordCase(Case.NOEDIT)
                .setText("Hello! My name is Luno, And this is mY friend, LuCo.")
                .setPadding(1, 2)
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##########################################################\n" + //
                                "#                                                        #\n" + //
                                "#  Hello! My name is Luno, And this is mY friend, LuCo.  #\n" + //
                                "#                                                        #\n" + //
                                "##########################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testCaseLowercase() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement()
                .setWordCase(Case.LOWERCASE)
                .setText("Hello! My name is Luno, And this is mY friend, LuCo.")
                .setPadding(1, 2)
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##########################################################\n" + //
                                "#                                                        #\n" + //
                                "#  hello! my name is luno, and this is my friend, luco.  #\n" + //
                                "#                                                        #\n" + //
                                "##########################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testCaseUppercase() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement()
                .setWordCase(Case.UPPERCASE)
                .setText("Hello! My name is Luno, And this is mY friend, LuCo.")
                .setPadding(1, 2)
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##########################################################\n" + //
                                "#                                                        #\n" + //
                                "#  HELLO! MY NAME IS LUNO, AND THIS IS MY FRIEND, LUCO.  #\n" + //
                                "#                                                        #\n" + //
                                "##########################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testCaseTitle() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement()
                .setWordCase(Case.TITLE)
                .setText("Hello! My name is Luno, And this is mY friend, LuCo.")
                .setPadding(1, 2)
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##########################################################\n" + //
                                "#                                                        #\n" + //
                                "#  Hello! My Name Is Luno, And This Is My Friend, Luco.  #\n" + //
                                "#                                                        #\n" + //
                                "##########################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testCaseNormal() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element text = new TextElement()
                .setWordCase(Case.NORMAL)
                .setText("heLLo! mY name is Luno, and this is my friend, Luco.")
                .setPadding(1, 2)
                .setBorder();

            new TextUI(text).draw();

            String output = outputStream.toString();

            String expected = "##########################################################\n" + //
                                "#                                                        #\n" + //
                                "#  Hello! My name is Luno, and this is my friend, Luco.  #\n" + //
                                "#                                                        #\n" + //
                                "##########################################################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }


}