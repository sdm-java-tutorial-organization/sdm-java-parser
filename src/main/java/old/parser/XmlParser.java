package old.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class XmlParser {

    /* getTagName - 태그문자열에서 태그이름 반환 */
    public static String getTagName(String line) {

        /**
         * [CASE]
         *
         * #01. <a>
         * #02. <a id="">
         * #03. <a />
         * #04. <a id="" />
         * #05. </a>
         * #06. <a\n>
         * */

        if (line.indexOf("<") == 0) {
            int startPoint, endPorint = 0;
            if (line.indexOf("/") != 1) startPoint = 1;
            else startPoint = 2;
            for (int i = startPoint; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == ' ' || c == '/' || c == '>' || c == '\n') {
                    endPorint = i;
                    break;
                }
            }
            return line.substring(startPoint, endPorint);
        }
        return "";
    } // getTagName

    /* getTagAttribute - 태그문자열에서 태그속성을 Map으로 반환 */
    public static Map<String, String> getTagAttribute(String line) {

        /**
         * [CASE]
         *
         * #01. <a id=""> :: 태그뒤에는 공백이 필수
         * #02. <a > :: 공백은 있고 속성을 없을 수도 있음
         * #03. <a id=""class=""> :: 속성이 붙어 있을 수도 있음
         * #04. <a id=""  class=""   > :: 속성사이에는 공백거리가 제한이 없음
         * #05. <a id = "" class = "" > :: 속성이름과 속성 사이의 공백도 가능
         * #06. <a disabled> :: 단일속성도 가능
         *
         * #솔루션
         * #01 첫번째 띄어쓰기가 있을때까지, 뒤에 한칸 제거함
         *  <a id="" class="" >
         *      disabled id="identity" helloworld class="myclass"
         *          ====(split)====> [ "disable id=", "identity", "helloworld class=", "myclass", "" ]
         *      disabled helloworld id=""
         *          ====(split)====> [ "disable helloworld id=", "", ""]
         *      disabled helloworld
         *          [ "disabled hellowowlrd" ]
         * */

        // == 필수조건 - 다음중 1개는 있어야함 ==
        int firstBlank = line.indexOf(" ");
        int firstBreak = line.indexOf("\n");

        if (
                firstBlank >= 0
                        ||
                        firstBreak >= 0
        ) {

            Map<String, String> map = new HashMap<>();
            int ps = firstBlank + 1;
            int pe = line.length() - 1;

            // == 속성값분할 ==
            String[] arrAttribute = line.substring(ps, pe).split("\"");
            System.out.println(Arrays.toString(arrAttribute));
            switch (arrAttribute.length) {
                // == 속성없음 ==
                case 0:
                    break;
                // == 단일속성 ==
                case 1:
                    if (arrAttribute[0].trim().length() != 0)
                        map.put(arrAttribute[0].trim(), null);
                    break;
                default:
                    for (int i = 0; i < arrAttribute.length; i++) {
                        String minify = arrAttribute[i].trim();
                        if (minify.length() > 0) {
                            String[] names = minify.split("\\s+");
                            for (int j = 0; j < names.length; j++) {
                                // == 01. [ ... , "id=" ] ==
                                if (names[j].charAt(names[j].length() - 1) == '=') {
                                    map.put(names[j].substring(0, names[j].length() - 1), arrAttribute[i + 1]);
                                    i += 1;
                                }
                                // == 02. [ ... , "id", "=" ] ==
                                else if (
                                        j + 1 < names.length
                                                &&
                                                names[j + 1].equals("=")
                                ) {
                                    map.put(names[j], arrAttribute[i + 1]);
                                    i += 1;
                                    break;
                                }
                                // == 03. [ ... , "disabled" ] ==
                                else {
                                    map.put(names[j], null);
                                }
                            }
                        }
                    }
                    break;
            }

            // @Test
            //            map.forEach((key, value) -> {
            //                System.out.printf("key : %s , value : %s\n", key, value);
            //            });

            return map;
        }
        return null;
    } // getTagAttribute

}
