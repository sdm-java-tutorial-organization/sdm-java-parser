package refactoring.util;

public class TagValidation {

    public static boolean equalsOpenTagMark(char nowCharInLoop) {
        return nowCharInLoop == '<';
    }

    public static boolean equalsCloseTagMark(char nowCharInLoop) {
        return nowCharInLoop == '>';
    }

    public static boolean checkEndOfTagName(char nowCharInLoop) {
        return nowCharInLoop == ' ' || nowCharInLoop == '/' || nowCharInLoop == '>' || nowCharInLoop == '\n';
    }

}
