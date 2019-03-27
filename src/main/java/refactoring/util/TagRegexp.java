package refactoring.util;

import old.common.Define;
import refactoring.define.TypeOfLine;

public class TagRegexp {

    public static String regExpOpenTag = "<\\w+(\\s+)?>";
    public static String regExpOpenTagWithAttribute = "<\\w+(\\s+)?((\\s+)?([^\"=]{0,}(\\s+)?=(\\s+)?\"[^\"]{0,}\"|\\w+)?){0,}(\\s+)?>";
    public static String regExpCloseTag = "</\\w+(\\s+)?>";
    public static String regExpOpenAndCloseTag = "<\\w+(\\s+)?((\\s+)?([^\"=]{0,}(\\s+)?=(\\s+)?\"[^\"]{0,}\"|\\w+)?){0,}(\\s+)?/>";

    /**
     * 입력받은 스트링을 정규식으로 비교하기
     */
    public static TypeOfLine compareStringWithRegexp(String stringOfLine) {
        if (stringOfLine.matches(regExpOpenTag)) {
            return TypeOfLine.OPEN;
        } else if (stringOfLine.matches(regExpCloseTag)) {
            return TypeOfLine.CLOSE;
        } else if (stringOfLine.matches(regExpOpenTagWithAttribute)) {
            return TypeOfLine.OPEN;
        } else if (stringOfLine.matches(regExpOpenAndCloseTag)) {
            return TypeOfLine.ALL;
        } else {
            return TypeOfLine.NOT; /* no tag */
        }
    }

}
