package refactoring.parser;


import refactoring.util.TagValidation;

public class TagNameParser extends Parser {

    private String stringOfLine;
    private String tagName;

    public TagNameParser(String stringOfLine) {
        this.stringOfLine = stringOfLine;
    }

    @Override
    public void parse() {
        int startOfTagName = getStartOfTagName(stringOfLine.charAt(1));
        int endOfTagName = getEndOfTagName(startOfTagName, stringOfLine);
        tagName = stringOfLine.substring(startOfTagName, endOfTagName);
    }

    @Override
    public String getResult() {
        return tagName;
    }

    private int getStartOfTagName(char secondCharInLine) {
        if (secondCharInLine != '/') {
            return 1;
        } else {
            return 2;
        }
    }

    private int getEndOfTagName(int startOfTagName, String stringOfLine) {
        for (int i = startOfTagName; i < stringOfLine.length(); i++) {
            char c = stringOfLine.charAt(i);
            if (TagValidation.checkEndOfTagName(c)) {
                return i;
            }
        }
        return 0; //
    }

}
