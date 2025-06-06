package textui.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import textui.TextUI;
import textui.exception.ChildrenNotAllowedException;
import textui.exception.ValueCannotBeSetException;

public class ElementTest {
    @Test
    void testFill() {
        Element element = new Element(5, 10);
        assertFalse(element.getFill());

        element.setFill(true);
        assertTrue(element.getFill());
    }

    @Test
    void testBorder() {
        Element element = new Element(5, 10);

        assertAll(() -> {
            assertFalse(element.getBorder().bottom());
            assertFalse(element.getBorder().top());
            assertFalse(element.getBorder().left());
            assertFalse(element.getBorder().right());
        });

        element.setBorder(true, false);

        assertAll(() -> {
            assertTrue(element.getBorder().bottom());
            assertTrue(element.getBorder().top());
            assertFalse(element.getBorder().left());
            assertFalse(element.getBorder().right());
        });

        element.setBorder(false, true);

        assertAll(() -> {
            assertFalse(element.getBorder().bottom());
            assertFalse(element.getBorder().top());
            assertTrue(element.getBorder().left());
            assertTrue(element.getBorder().right());
        });

        element.setBorder(false, true, false, false);

        assertAll(() -> {
            assertFalse(element.getBorder().bottom());
            assertFalse(element.getBorder().top());
            assertFalse(element.getBorder().left());
            assertTrue(element.getBorder().right());
        });
        
        element.setBorder();

        assertAll(() -> {
            assertTrue(element.getBorder().bottom());
            assertTrue(element.getBorder().top());
            assertTrue(element.getBorder().left());
            assertTrue(element.getBorder().right());
        });
    }

    @Test
    void testPadding() throws ValueCannotBeSetException {
        Element element = new Element(5, 10);

        assertAll(() -> {
            assertEquals(0, element.getPadding().bottom());
            assertEquals(0, element.getPadding().top());
            assertEquals(0, element.getPadding().left());
            assertEquals(0, element.getPadding().right());
        });
        
        element.setPadding(5, 2);
        assertAll(() -> {
            assertEquals(5, element.getPadding().bottom());
            assertEquals(5, element.getPadding().top());
            assertEquals(2, element.getPadding().left());
            assertEquals(2, element.getPadding().right());
        });
        
        element.setPadding(1, 2, 3, 4);
        assertAll(() -> {
            assertEquals(3, element.getPadding().bottom());
            assertEquals(1, element.getPadding().top());
            assertEquals(4, element.getPadding().left());
            assertEquals(2, element.getPadding().right());
        });
        
        element.setPadding(3);
        assertAll(() -> {
            assertEquals(3, element.getPadding().bottom());
            assertEquals(3, element.getPadding().top());
            assertEquals(3, element.getPadding().left());
            assertEquals(3, element.getPadding().right());
        });
    }

    @Test
    void testMargin() throws ValueCannotBeSetException {
        Element element = new Element(5, 10);

        assertAll(() -> {
            assertEquals(0, element.getMargin().bottom());
            assertEquals(0, element.getMargin().top());
            assertEquals(0, element.getMargin().left());
            assertEquals(0, element.getMargin().right());
        });
        
        element.setMargin(5, 2);
        assertAll(() -> {
            assertEquals(5, element.getMargin().bottom());
            assertEquals(5, element.getMargin().top());
            assertEquals(2, element.getMargin().left());
            assertEquals(2, element.getMargin().right());
        });
        
        element.setMargin(1, 2, 3, 4);
        assertAll(() -> {
            assertEquals(3, element.getMargin().bottom());
            assertEquals(1, element.getMargin().top());
            assertEquals(4, element.getMargin().left());
            assertEquals(2, element.getMargin().right());
        });
        
        element.setMargin(3);
        assertAll(() -> {
            assertEquals(3, element.getMargin().bottom());
            assertEquals(3, element.getMargin().top());
            assertEquals(3, element.getMargin().left());
            assertEquals(3, element.getMargin().right());
        });
    }

    @Test
    void testSize() {
        Element element = new Element(7, 15);
        assertEquals(15, element.getWidth());
        assertEquals(7, element.getHeight());
    }

    @Test
    void testAlignmentSettings() {
        Element element = new Element(5, 10);

        element.setJustifyContent(FlexAlign.CENTER);
        element.setAlignItems(FlexAlign.FLEX_END);

        assertEquals(FlexAlign.CENTER, element.getJustifyContent());
        assertEquals(FlexAlign.FLEX_END, element.getAlignItems());
    }

    @Test
    void testChildManagement() throws ChildrenNotAllowedException {
        Element parent = new Element(10, 5);
        Element child1 = new Element(2, 2);
        Element child2 = new Element(3, 1);

        parent.insertChild(child1);
        parent.insertChild(child2);

        assertEquals(2, parent.childElements.size());
        assertTrue(parent.childElements.contains(child1));
        assertTrue(parent.childElements.contains(child2));
    }

    @Test
    void testMinAndMaxDimensions() throws ValueCannotBeSetException {
        Element element = new Element(10, 5);

        // Initial values
        assertEquals(0, element.getMinWidth());
        assertEquals(0, element.getMinHeight());

        assertEquals(Integer.MAX_VALUE, element.getMaxWidth());
        assertEquals(Integer.MAX_VALUE, element.getMaxHeight());

        // Now set and verify
        element.setMinWidth(3);
        element.setMinHeight(4);
        element.setMaxWidth(15);
        element.setMaxHeight(12);

        assertEquals(3, element.getMinWidth());
        assertEquals(4, element.getMinHeight());
        assertEquals(15, element.getMaxWidth());
        assertEquals(12, element.getMaxHeight());
    }

    @Test
    void testSmallSizedBox() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element container = new Element(3, 5).setBorder();
            new TextUI(container).draw();

            String output = outputStream.toString();

            String expected = "#######\n" + //
                                "#     #\n" + //
                                "#     #\n" + //
                                "#     #\n" + //
                                "#######\n\n" + //
                                "" ; 
            assertEquals(expected, output);

        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMinWidth() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(3, 5).setBorder();
            element.setMinWidth(15);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###############\n" + //
                                "#             #\n" + //
                                "#             #\n" + //
                                "#             #\n" + //
                                "###############\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMinHeight() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(3, 5).setBorder();
            element.setMinHeight(8);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "#######\n" + //
                                "#     #\n" + //
                                "#     #\n" + //
                                "#     #\n" + //
                                "#     #\n" + //
                                "#     #\n" + //
                                "#     #\n" + //
                                "#######\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMaxHeight() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(25, 25).setBorder();
            element.setMaxHeight(8);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testMaxWidth() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder();
            element.setMaxWidth(10);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "##########\n" + //
                                "#        #\n" + //
                                "#        #\n" + //
                                "#        #\n" + //
                                "#        #\n" + //
                                "#        #\n" + //
                                "#        #\n" + //
                                "##########\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testTopBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder(true, false, false, false);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "#########################\n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testRightBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder(false, true, false, false);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testBottomBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder(false, false, true, false);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "#########################\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testLeftBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder(false, false, false, true);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "#                         \n" + //
                                "#                         \n" + //
                                "#                         \n" + //
                                "#                         \n" + //
                                "#                         \n" + //
                                "#                         \n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testTopAndBottomBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder(true, false);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "#########################\n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "                         \n" + //
                                "#########################\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testLeftAndRightBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder(false, true);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testWithoutLeftBorder() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder(true, true, true, false);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "##########################\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "                         #\n" + //
                                "##########################\n\n" + //
                                "" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFill2() {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder().setFill(true);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "###########################\n" + //
                                "###########################\n" + //
                                "###########################\n" + //
                                "###########################\n" + //
                                "###########################\n" + //
                                "###########################\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testInsertChild() throws ChildrenNotAllowedException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25).setBorder();
            Element child = new Element(3, 5).setBorder();
            element.insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "########                  #\n" + //
                                "##     #                  #\n" + //
                                "##     #                  #\n" + //
                                "##     #                  #\n" + //
                                "########                  #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testPadding2() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(6, 25)
                .setBorder()
                .setPadding(1, 2);
            Element child = new Element(3, 5).setBorder();
            element.insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###############################\n" + //
                                "#                             #\n" + //
                                "#  #######                    #\n" + //
                                "#  #     #                    #\n" + //
                                "#  #     #                    #\n" + //
                                "#  #     #                    #\n" + //
                                "#  #######                    #\n" + //
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
    void testMargin2() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25).setBorder();
            Element child = new Element(3, 5)
                .setBorder()
                .setMargin(1, 3);
            element.insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#                         #\n" + //
                                "#   #######               #\n" + //
                                "#   #     #               #\n" + //
                                "#   #     #               #\n" + //
                                "#   #     #               #\n" + //
                                "#   #######               #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testBlockLayout() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25).setBorder();
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "########                  #\n" + //
                                "##     #                  #\n" + //
                                "##     #                  #\n" + //
                                "##     #                  #\n" + //
                                "########                  #\n" + //
                                "########                  #\n" + //
                                "##     #                  #\n" + //
                                "##     #                  #\n" + //
                                "##     #                  #\n" + //
                                "########                  #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testInlineLayout() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.INLINE);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child)
                .insertChild(child)
                .insertChild(child)
                .insertChild(child)
                .insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "######################    #\n" + //
                                "##     ##     ##     #    #\n" + //
                                "##     ##     ##     #    #\n" + //
                                "##     ##     ##     #    #\n" + //
                                "######################    #\n" + //
                                "###############           #\n" + //
                                "##     ##     #           #\n" + //
                                "##     ##     #           #\n" + //
                                "##     ##     #           #\n" + //
                                "###############           #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "###############           #\n" + //
                                "##     ##     #           #\n" + //
                                "##     ##     #           #\n" + //
                                "##     ##     #           #\n" + //
                                "###############           #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout_FlexEnd() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX)
                .setJustifyContent(FlexAlign.FLEX_END);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#           ###############\n" + //
                                "#           #     ##     ##\n" + //
                                "#           #     ##     ##\n" + //
                                "#           #     ##     ##\n" + //
                                "#           ###############\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout_SpaceBetween() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX)
                .setJustifyContent(FlexAlign.SPACE_BETWEEN);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#   #######   #######     #\n" + //
                                "#   #     #   #     #     #\n" + //
                                "#   #     #   #     #     #\n" + //
                                "#   #     #   #     #     #\n" + //
                                "#   #######   #######     #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout_SpaceApart() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX)
                .setJustifyContent(FlexAlign.SPACE_APART);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "########           ########\n" + //
                                "##     #           #     ##\n" + //
                                "##     #           #     ##\n" + //
                                "##     #           #     ##\n" + //
                                "########           ########\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout_Center() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX)
                .setJustifyContent(FlexAlign.CENTER);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#     ##############      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     ##############      #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout_AlignItemsFlexStart() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX)
                .setJustifyContent(FlexAlign.CENTER)
                .setAlignItems(FlexAlign.FLEX_START);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#     ##############      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     ##############      #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout_AlignItemsFlexEnd() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX)
                .setJustifyContent(FlexAlign.CENTER)
                .setAlignItems(FlexAlign.FLEX_END);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#     ##############      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     ##############      #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testFlexLayout_AlignItemsFlexCenter() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX)
                .setJustifyContent(FlexAlign.CENTER)
                .setAlignItems(FlexAlign.CENTER);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child).insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#     ##############      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     #     ##     #      #\n" + //
                                "#     ##############      #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }

    @Test
    void testOverflow() throws ChildrenNotAllowedException, ValueCannotBeSetException {
        // Arrange: prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Element element = new Element(10, 25)
                .setBorder()
                .setDisplay(Display.FLEX);
            Element child = new Element(3, 5)
                .setBorder();
            element.insertChild(child)
                .insertChild(child)
                .insertChild(child)
                .insertChild(child);
            new TextUI(element).draw();

            String output = outputStream.toString();

            String expected = "###########################\n" + //
                                "###########################\n" + //
                                "##     ##     ##     ##    \n" + //
                                "##     ##     ##     ##    \n" + //
                                "##     ##     ##     ##    \n" + //
                                "###########################\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "#                         #\n" + //
                                "###########################\n" + //
                                "\n" ; 
            assertEquals(expected, output);
        } finally {
            // Restore original System.out no matter what
            System.setOut(originalOut);
        }
    }
}
