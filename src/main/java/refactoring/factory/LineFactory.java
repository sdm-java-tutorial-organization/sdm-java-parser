package refactoring.factory;

import refactoring.define.TypeOfLine;
import refactoring.model.Line;
import refactoring.model.TagContents;
import refactoring.util.TagRegexp;

public class LineFactory {

    public static Line getInstance(String stringOfLine) {
        TypeOfLine typeOfLine = TagRegexp.compareStringWithRegexp(stringOfLine);
        TagContents tagContents =
                (typeOfLine != TypeOfLine.NOT) ?
                        TagContentsFactory.getInstance(stringOfLine) : null;
        return new Line(stringOfLine, typeOfLine, tagContents);
    }

}
