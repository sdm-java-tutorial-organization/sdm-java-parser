package old.parser;

import java.util.*;

import old.common.Define;
import old.model.Line;

/* TextParser - 문자열검색 및 정규식처리 */
public class TextParser {

    /* Field */
    public static String regExpOpenTag = "<\\w+(\\s+)?>";
    public static String regExpOpenTagPlus = "<\\w+(\\s+)?((\\s+)?([^\"=]{0,}(\\s+)?=(\\s+)?\"[^\"]{0,}\"|\\w+)?){0,}(\\s+)?>";
    public static String regExpCloseTag = "</\\w+(\\s+)?>";
    public static String regExpOpenAndCloseTag = "<\\w+(\\s+)?((\\s+)?([^\"=]{0,}(\\s+)?=(\\s+)?\"[^\"]{0,}\"|\\w+)?){0,}(\\s+)?/>";

    /* ParseTextToLine - 문자열검색 - 한번의 반복문속에서 처리할수 있을까? */
    public static Line[] parseTextToLine(String text) {
        // @특이사항1 -> <script> 태그일때, 문자열주의
        // @특이사항2 -> <!-- --> 태그일때, 주석주의

        /**
         * [SAMPLE] front - f, open - o, close - c
         *
         * f              f         f   f    f
         *           o   c    o    co  co   c o   c
         * helloworld<tag>gogo</tag><h1></h1>d<tag></tag>
         * */

        List<Line> lines = new LinkedList<>();
        Stack<Integer> quotes = new Stack<>();

        int flagFront = 0;              // 시작플래그
        int flagOpen = -1;              // 열림플래그
        byte isTagOpen = 0;             // 태그타입 1 (open) -1 (close) 0 (open & close)
        boolean isScriptOpen = false;   // 스크립트시작여부

        byte rs = 0;                    // 주석시작패턴 포인터
        byte re = 0;                    // 주석종료패턴 포인터
        int flagRemarkOpen = -1;        // 주석시작위치

        // == 전체문자열반복 ==
        for (int i = 0; i < text.length(); i++) {


            if (flagRemarkOpen != -1) {
                // == 주석내부라면 ==
                if (text.charAt(i) == Define.REMARK_PATTERN_END/*-->*/.charAt(re)) {
                    re++;
                    if (re == Define.REMARK_PATTERN_END/*-->*/.length()) {

                        // == 내용생성 flagFront ~ flagOpen ==
                        if (flagFront != flagRemarkOpen) {
                            String textArea = text.substring(flagFront, flagRemarkOpen).trim();
                            if (textArea.length() > 0) lines.add(new Line(textArea));
                        }

                        // == 주석생성 ==
                        String remarkArea = text.substring(flagRemarkOpen + 1, i + 1);
                        lines.add(new Line(remarkArea));

                        // == 초기화 ==
                        flagFront = i + 1;
                        flagRemarkOpen = -1;
                        re = 0;
                        continue;
                    }
                }
            } else {
                // == 주석이아니라면 ==
                if (quotes.size() == 0) {


                    // == <script> 닫힘상태 ( 문자열열리는지확인 ) ==
                    if (isScriptOpen) {
                        // == 문자열시작 ==
                        if (text.charAt(i) == '\"') {
                            quotes.push(2);
                            continue;
                        } else if (text.charAt(i) == '\'') {
                            quotes.push(1);
                            continue;
                        }
                    }


                    if (flagOpen == -1) {
                        // == 태그밖 (태그시작찾기 - "<") ==
                        if (text.charAt(i) == '<') {
                            flagOpen = i;
                            rs++;
                        }
                    } else {

                        // == 주석진행인지확인 ==
                        if (rs > 0) {
                            // == 주석시작지점찾기 ==
                            if (text.charAt(i) == Define.REMARK_PATTERN_START.charAt(rs)) {
                                rs++;
                                if (rs == Define.REMARK_PATTERN_START.length()) {
                                    flagRemarkOpen = i - Define.REMARK_PATTERN_START.length();
                                    rs = 0;
                                    continue;
                                }
                            } else rs = 0;
                        }

                        // == 태그안 (태그종료찾기 - ">") ==
                        if (text.charAt(i) == '>') {

                            // 태그생성 (flagOpen ~ i+1) (열림,닫힘,열닫 + 태그이름 + 태그속성)
                            String tagArea = text.substring(flagOpen, i + 1);
                            if (tagArea.matches(regExpOpenTag)) {
                                // == 열림 (단순) ==
                                isTagOpen = Define.TAG_TYPE_A; // 1
                            } else if (tagArea.matches(regExpCloseTag)) {
                                // == 닫힘 ==
                                isTagOpen = Define.TAG_TYPE_B; // -1
                            } else if (tagArea.matches(regExpOpenTagPlus)) {
                                // == 열림 (속성) ==
                                isTagOpen = Define.TAG_TYPE_A; // 1
                            } else if (tagArea.matches(regExpOpenAndCloseTag)) {
                                // == 열림 + 닫힘 ==
                                isTagOpen = Define.TAG_TYPE_C; // 0
                            } else if (true) {
                                // == 태그형식아님 ==
                                flagOpen = -1;
                                isTagOpen = Define.TAG_TYPE_C; // init (0)
                                continue;
                            }


                            // == <script> 태그여부 ==
                            String tagName = XmlParser.getTagName(tagArea);
                            if (isScriptOpen) {
                                // == <script> 닫힘찾기 ==
                                if (tagName.equals(Define.TAG_SCRIPT) && isTagOpen != 1) {
                                    isScriptOpen = false;
                                } else {
                                    continue;
                                }
                            } else {
                                // == <script> 열림찾기 ==
                                if (tagName.equals(Define.TAG_SCRIPT) && isTagOpen == 1) {
                                    isScriptOpen = true;
                                }
                            }

                            // == 내용생성 (flagFront ~ flagOpen) ==
                            if (flagFront != flagOpen) {
                                String textArea = text.substring(flagFront, flagOpen).trim();
                                if (textArea.length() > 0) {
                                    lines.add(new Line(textArea));
                                }
                            }


                            // == 속성생성 ==
                            Map<String, String> attributes = null;
                            if (isTagOpen != -1) {
                                attributes = XmlParser.getTagAttribute(tagArea);
                            }

                            // == 태그생성 ==
                            lines.add(new Line(tagArea, tagName, isTagOpen, attributes));

                            // == 초기화 ==
                            flagOpen = -1;
                            flagFront = i + 1;
                            isTagOpen = 0;

                        } else if (text.charAt(i) == '<') {
                            flagOpen = i;
                            rs++;
                        }
                    }
                } else {
                    // == <script> 오픈상태 ( & 문자열열림상태 ) ==

                    // == 문자열안 (i>0) :: 문자열의탈출을찾음 ==
                    if (text.charAt(i) == '\"') {
                        if (text.charAt(i - 1) != '\\' && quotes.peek() == 2) {
                            quotes.pop();
                        }
                    }
                    if (text.charAt(i) == '\'') {
                        if (text.charAt(i - 1) != '\\' && quotes.peek() == 1) {
                            quotes.pop();
                        }
                    }
                }
            }
        }

        // @Test
        //        for (int i = 0; i < lines.size(); i++) {
        //            lines.get(i).print();
        //        }

        return lines.toArray(new Line[lines.size()]);
    }

    private void checkRemarkEnd() {

    }

}
