package refactoring.parser;

import java.util.Map;

public class TagAttributeParser extends Parser {

    private String stringOfLine;
    private Map<String, String> tagAttribute;

    public TagAttributeParser(String stringOfLine) {
        this.stringOfLine = stringOfLine;
    }

    @Override
    public void parse() {

    }

    @Override
    public Map<String, String> getResult() {
        return null;
    }

}
