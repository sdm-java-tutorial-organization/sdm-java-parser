package old.parser;

import old.common.Define;
import old.model.Dom;
import old.model.Line;

/* HtmlParser - 사용자 접근 클래스 */
public class HtmlParser {

    /* text -> dom */
    public static Dom textToDom(String text) {

        // == to line (문자열검색 및 정규식처리) ==
        Line[] lines = TextParser.parseTextToLine(text);

        // == root dom (재귀 및 스택) ==
        Dom root = new Dom(
                Define.TAG_ROOT,
                Define.TAG_TYPE_A,
                null,
                DomParser.parseLineToDom(lines, 0, lines.length - 1)
        );

        // @Test
        // root.print();
        // root.printClean();
        return root;
    }

    /* dom -> text */
    public static String domToText(Dom dom) {
        // TODO ...
        return null;
    }

}
