package textui.helper;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility helper methods for common operations on lists and strings.
 */
public class Helper {

    /**
     * Finds the largest integer in the given list.
     * 
     * @param list a list of integers; must not be empty
     * @return the largest integer found in the list
     */
    public static int findLargestInList(List<Integer> list) {
        int largest = list.getFirst();
        for (int num : list) {
            if (num > largest) largest = num;
        }
        return largest;
    }

    /**
     * Calculates the amount of extra space available in a row given its content width.
     * Extra space is the difference between the content width and the sum of the lengths
     * of the words plus spaces between words.
     * 
     * @param row a list of words in the row
     * @param contentWidth the total width available for the content
     * @return the amount of extra space available in the row
     */
    public static int findExtraSpaceInRow(List<String> row, int contentWidth) {
        AtomicInteger totalWordLength = new AtomicInteger(0);
        row.forEach(word -> totalWordLength.addAndGet(word.length()));
        
        int spacing = row.size() - 1;
        int extraSpacing = (contentWidth - (totalWordLength.get() + spacing));

        return extraSpacing;
    }

    /**
     * Capitalizes the first letter of the given word and converts the rest to lowercase.
     * 
     * @param word the word to capitalize
     * @return the word with the first letter capitalized and the rest lowercase
     */
    public static String capitalise(String word) {
        String firstLetter = word.substring(0, 1).toUpperCase();
        return firstLetter + word.substring(1, word.length()).toLowerCase();
    }
}
