package refactoring.parser;

import refactoring.factory.LineFactory;
import refactoring.util.TagRegexp;
import refactoring.define.TypeOfLine;
import refactoring.model.Line;
import refactoring.util.TagValidation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 스트링으로 된 HTML을 "<"과 ">" 단위로 객체화하는 클래스
 */
public class RawParser extends Parser {

    private String rawString;
    private List<Line> listOfLine = new LinkedList<Line>();
    private Stack<Integer> quotes = new Stack<Integer>(); // TODO 변수이름구체화
    private int flagOfFront = 0;
    private int flagOfOpen;

    public RawParser(String rawString) {
        this.rawString = rawString;
        initFlagOfOpen();
    }

    @Override
    public void parse() {
        for (int i = 0; i < this.rawString.length(); i++) {
            compareChar(rawString.charAt(i), i);
        }
    }

    @Override
    public Line[] getResult() {
        return listOfLine.toArray(new Line[listOfLine.size()]);
    }

    private void initFlagOfOpen() {
        this.flagOfOpen = -1; /* -1 is init */
    }

    private void compareChar(char nowCharInLoop, int positionInLoop) {
        if (TagValidation.equalsOpenTagMark(nowCharInLoop)) {
            flagOfOpen = positionInLoop;
        } else if (TagValidation.equalsCloseTagMark(nowCharInLoop)) {
            appendLine(positionInLoop);
        }
    }

    private void appendLine(int positionInLoop) {
        String stringOfLine = this.rawString.substring(flagOfOpen, positionInLoop);
        Line line = LineFactory.getInstance(stringOfLine);
        listOfLine.add(line);
    }

}
