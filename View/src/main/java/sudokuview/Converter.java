package sudokuview;

import java.util.regex.Pattern;
import javafx.util.StringConverter;

public class Converter extends StringConverter<Integer> {
    /**
     * Convert Integer to String.
     * @param integer Integer
     * @return String
     */
    @Override
    public String toString(Integer integer) {
        if (integer.equals(0)) {
            return "";
        }
        return String.valueOf(integer);
    }

    /**
     * Convert String to Integer.
     * @param s String
     * @return Integer
     */
    @Override
    public Integer fromString(String s) {
        Pattern pattern = Pattern.compile("[1-9]");
        if (pattern.matcher(s).matches()) {
            return Integer.valueOf(s);
        }
        return 0;
    }
}
