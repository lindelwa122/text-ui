package textui.helper;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HelperTest {

    @Test
    void testFindLargestInList() {
        assertEquals(9, Helper.findLargestInList(List.of(1, 3, 9, 2, 4)));
        assertEquals(1, Helper.findLargestInList(List.of(1)));
        assertEquals(-1, Helper.findLargestInList(List.of(-10, -3, -7, -1, -5)));
    }

    @Test
    void testFindExtraSpaceInRow() {
        assertEquals(2, Helper.findExtraSpaceInRow(List.of("hi", "there"), 10));
        assertEquals(0, Helper.findExtraSpaceInRow(List.of("a", "b", "c"), 5));  // total length 3 + 2 = 5
        assertEquals(-2, Helper.findExtraSpaceInRow(List.of("long", "word"), 7));  // 4+4+1 = 9 > 7
        assertEquals(0, Helper.findExtraSpaceInRow(List.of("singleword"), 10));   // no space between, 10-10 = 0
    }

    @Test
    void testCapitalise() {
        assertEquals("Hello", Helper.capitalise("hello"));
        assertEquals("Hello", Helper.capitalise("HELLO"));
        assertEquals("A", Helper.capitalise("a"));
        assertEquals("B", Helper.capitalise("B"));
        assertEquals("Dog", Helper.capitalise("dOG"));
    }

    @Test
    void testCapitalise_EmptyStringThrows() {
        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            Helper.capitalise("");
        });
    }
}
