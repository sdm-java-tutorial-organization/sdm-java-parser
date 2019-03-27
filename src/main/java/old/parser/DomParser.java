package old.parser;

import old.common.Define;
import old.model.Dom;
import old.model.Line;

import java.util.*;

/**
 * [ Class :: DomParser ]
 *
 * @경로 :: /src/old.parser/DomParser.java
 * @목적 :: String[] 을 Dom구조로 처리하는 클래스
 * @진행 ::
 * @주의 ::
 * @일자 :: 2018.07.13
 * @작성 :: SDM
 * @참조 ::
 */
public class DomParser {

    /*ParseLineToDom - <Line>을 <Dom>으로 반환하는 재귀함수 */
    public static Dom[] parseLineToDom(Line[] lines, int start, int end) {

        /**
         * [SAMPLE]
         *
         * [parseLineToDom]
         * <html>
         *  // ============
         *  [parseLineToDom]
         *  <head>
         *      // ============
         *      [parseLineToDom]
         *      <meta>
         *      <link>
         *      <link/>
         *      // ============
         *  </head>
         *  <body>
         *      // ============
         *      [parseLineToDom]
         *      <ol>
         *          // ============
         *          [parseLineToDom]
         *          <li> push
         *              <img> push
         *              <li></li> push & pop
         *              <li></li> push & pop
         *          </li> pop
         *          <li> push
         *              <li></li> push & pop
         *              <li></li> push & pop
         *          </li> pop
         *          // ============
         *      </ol>
         *      // ============
         *  </body>
         *  // ============
         * </html>
         * */

        if (lines != null && lines.length > 0) {
            List<Dom> childs = new LinkedList<Dom>();
            Stack<String> stack = new Stack<String>();
            int ps = start; // 시작포인터
            int pe = end;   // 종료포인터

            while (ps <= pe) {
                Line sLine = lines[ps];
                // == 태그 ==
                if (sLine.isTag) {
                    switch (sLine.tagType) {
                        // == 시작태그 :: <열림> ==
                        case Define.TAG_TYPE_A:
                            stack.clear(); // TODO 초기화가 성능에 좋을까 ? <<< clear()가 성능에 좋을까 ?
                            stack.push(sLine.tagName);
                            int pt = ps; // pointer temp
                            boolean flagClose = false;

                            oLoop:
                            while (pe != pt) {
                                pt++;
                                Line nextLine = lines[pt];
                                if (nextLine.isTag) {
                                    switch (nextLine.tagType) {
                                        // == <열림> ==
                                        case 1:
                                            stack.push(nextLine.tagName);
                                            break;
                                        // == </닫힘> ==
                                        case -1:
                                            if (stack.peek().equals(nextLine.tagName)) {
                                                stack.pop();
                                                if (stack.size() == 0) {
                                                    flagClose = true;
                                                    break oLoop;
                                                }
                                            } else {
                                                for (int i = stack.size() - 2; i >= 0; i--) { // 뒤로 (peek!=이라 -2부터)
                                                    if (stack.get(i).equals(nextLine.tagName)) {
                                                        stack.remove(i);
                                                        if (i == 0) {
                                                            flagClose = true;
                                                            break oLoop;
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            break;
                                    }
                                }
                            }

                            // == </닫힘>있다면 ==
                            if (flagClose) {
                                childs.add(new Dom(
                                        sLine.tagName,
                                        Define.DOM_TAG_TYPE_A,
                                        sLine.attribute,
                                        ((ps + 1 < pt) ? parseLineToDom(lines, ps + 1, pt - 1) : null)));

                                // == 다음위치 ==
                                if (pt != pe) ps = pt + 1;
                                else ps = pe;
                            }
                            // == </닫힘> 없다면 ==
                            else {
                                childs.add(new Dom(
                                        sLine.tagName,
                                        Define.DOM_TAG_TYPE_C,
                                        sLine.attribute));
                                ps++;
                            }
                            break;
                        // == 시작태그 => </닫힘> ==
                        case Define.TAG_TYPE_B:
                            // TODO Error
                            ps++;
                            break;
                        // == 시작태그 :: <열림닫힘/> ==
                        case Define.TAG_TYPE_C:
                            childs.add(new Dom(
                                    sLine.tagName,
                                    Define.DOM_TAG_TYPE_B,
                                    sLine.attribute));
                            ps++;
                            break;
                        // == Default ==
                        default:
                            ps++;
                    }
                }
                // == 텍스트 ==
                else {
                    childs.add(new Dom(sLine.text));
                    ps++;
                }
            }
            return childs.toArray(new Dom[childs.size()]);
        }
        return null;
    }

}
