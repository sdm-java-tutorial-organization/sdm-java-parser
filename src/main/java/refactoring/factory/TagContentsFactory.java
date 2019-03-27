package refactoring.factory;

import refactoring.define.TypeOfLine;
import refactoring.model.Line;
import refactoring.model.TagContents;
import refactoring.parser.TagAttributeParser;
import refactoring.parser.TagNameParser;
import refactoring.util.TagRegexp;

import java.util.Map;

public class TagContentsFactory {

    public static TagContents getInstance(String stringOfLine) {

        String tagName;
        Map<String, String> tagAttribute;

        TagNameParser tagNameParser = new TagNameParser(stringOfLine);
        tagNameParser.parse();
        tagName = tagNameParser.getResult();

        TagAttributeParser tagAttributeParser = new TagAttributeParser(stringOfLine);
        tagAttributeParser.parse();
        tagAttribute = tagAttributeParser.getResult();

        return new TagContents(tagName, tagAttribute);

    }

}
