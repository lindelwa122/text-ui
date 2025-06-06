package textui.element;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import textui.TextUI;
import textui.exception.ValueCannotBeSetException;

public class UnsortedListElementTest {
    @Test
    void testNoBorderNoSpeciedSize() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element sortedList = new UnsortedListElement()
                .addItem("Java")
                .addItem("JavaScript")
                .addItem("CoffeeScript");

            new TextUI(sortedList).draw();

            String output = outputStream.toString();

            String expected = "- Java        \n" + //
                                "- JavaScript  \n" + //
                                "- CoffeeScript\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element sortedList = new UnsortedListElement()
                .addItem("Java")
                .addItem("JavaScript")
                .addItem("CoffeeScript")
                .setBorder();

            new TextUI(sortedList).draw();

            String output = outputStream.toString();

            String expected = "################\n" + //
                                "#- Java        #\n" + //
                                "#- JavaScript  #\n" + //
                                "#- CoffeeScript#\n" + //
                                "################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testPadding() throws ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element sortedList = new UnsortedListElement()
                .addItem("Java")
                .addItem("JavaScript")
                .addItem("CoffeeScript")
                .setBorder()
                .setPadding(1, 2);

            new TextUI(sortedList).draw();

            String output = outputStream.toString();

            String expected = "####################\n" + //
                                "#                  #\n" + //
                                "#  - Java          #\n" + //
                                "#  - JavaScript    #\n" + //
                                "#  - CoffeeScript  #\n" + //
                                "#                  #\n" + //
                                "####################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testSpecifiedSize() throws ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element sortedList = new UnsortedListElement(5, 25)
                .addItem("Java")
                .addItem("JavaScript")
                .addItem("CoffeeScript")
                .setBorder()
                .setPadding(1, 2);

            new TextUI(sortedList).draw();

            String output = outputStream.toString();

            String expected = "###############################\n" + //
                                "#                             #\n" + //
                                "#  - Java                     #\n" + //
                                "#  - JavaScript               #\n" + //
                                "#  - CoffeeScript             #\n" + //
                                "#                             #\n" + //
                                "#                             #\n" + //
                                "#                             #\n" + //
                                "###############################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMaxValues() throws ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element sortedList = new UnsortedListElement(5, 25)
                .addItem("Java")
                .addItem("JavaScript")
                .addItem("CoffeeScript")
                .setBorder()
                .setPadding(1, 2)
                .setMaxWidth(20)
                .setMaxHeight(7);

            new TextUI(sortedList).draw();

            String output = outputStream.toString();

            String expected = "####################\n" + //
                                "#                  #\n" + //
                                "#  - Java          #\n" + //
                                "#  - JavaScript    #\n" + //
                                "#  - CoffeeScript  #\n" + //
                                "#                  #\n" + //
                                "####################\n" + //
                                "\n" ; 

            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMinValues() throws ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element sortedList = new UnsortedListElement(5, 10)
                .setMinWidth(25)
                .setMinHeight(10)
                .addItem("Java")
                .addItem("JavaScript")
                .addItem("CoffeeScript")
                .setBorder()
                .setPadding(1, 2);

            new TextUI(sortedList).draw();

            String output = outputStream.toString();

            String expected = "#########################\n" + //
                                "#                       #\n" + //
                                "#  - Java               #\n" + //
                                "#  - JavaScript         #\n" + //
                                "#  - CoffeeScript       #\n" + //
                                "#                       #\n" + //
                                "#                       #\n" + //
                                "#                       #\n" + //
                                "#                       #\n" + //
                                "#########################\n" + //
                                "\n" ; 

            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }
}
