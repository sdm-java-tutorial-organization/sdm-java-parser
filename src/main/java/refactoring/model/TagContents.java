package refactoring.model;

import java.util.Map;

public class TagContents {

    private String tagName;
    private Map<String, String> tagAttribute;

    public TagContents(String tagName, Map<String, String> tagAttribute) {
        this.tagName = tagName;
        this.tagAttribute = tagAttribute;
    }

    public String getTagName() {
        return tagName;
    }

    public Map<String, String> getTagAttribute() {
        return tagAttribute;
    }

    // TODO
    public void print() {

    }

}
