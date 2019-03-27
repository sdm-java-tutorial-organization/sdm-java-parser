package old.model;

import java.util.Map;

/* Line */
public class Line {

    /* Field */
    public boolean isTag;                   // 태그여부
    public String text;                     // 텍스트
    public String tagName;                  // 태그이름
    public byte tagType;                    // 태그타입 1 (open) -1 (close) 0 (open & close)
    public Map<String, String> attribute;   // 태그속성

    /* Constructor (1) */
    public Line(String text) {
        this.isTag = false;
        this.text = text;
        this.tagName = "";
        this.tagType = 0;
        attribute = null;
    }

    /* Constructor (2) */
    public Line(String text, String tagName, byte tagType, Map<String, String> attribute) {
        this.isTag = true;
        this.text = text;
        this.tagName = tagName;
        this.tagType = tagType;
        this.attribute = attribute;
    }

    /* print */
    public void print() {
        System.out.printf("[%s] %s \n", (isTag ? "tag" : "txt"), text);
    }

}
