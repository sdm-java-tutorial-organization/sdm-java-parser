package refactoring.model;

import refactoring.define.TypeOfLine;

import java.util.Map;

/**
 *
 *
 * */
public class Line {

    private String text;
    private TypeOfLine typeOfLine;
    private TagContents tagContents;

    public Line(
            String text,
            TypeOfLine typeOfLine,
            TagContents tagContents
    ) {
        this.text = text;
        this.typeOfLine = typeOfLine;
        this.tagContents = tagContents;
    }

    public String getText() {
        return text;
    }

    public TypeOfLine getTypeOfLine() {
        return typeOfLine;
    }


    public TagContents getTagContents() {
        return tagContents;
    }

    // TODO
    public void print() {

    }

}
