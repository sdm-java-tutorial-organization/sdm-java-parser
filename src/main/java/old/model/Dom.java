package old.model;

import old.common.Define;
import old.implement.FunctionPrint;

import java.util.*;

/* Dom - Model */
public class Dom {

    /* Field - static */
    private static FunctionPrint fpPrint = (Dom dom, String string) -> {
        dom._print(string);
    };
    private static FunctionPrint fpPrintClean = (Dom dom, String string) -> {
        dom._printClean(string);
    };

    /* Field */
    public boolean isTag;                   // 태그여부
    public String tag;                      // tag == null 이라면 Text 객체
    public int tagType;                     // 태그타입 ( 0:텍스트, 1:열림+닫힘, 2:조인, 3:열림 )
    public Map<String, String> attribute;   // 속성
    public Dom[] childs;                    // 자식객체
    public String innerText;                // 내부택스트

    /* Constructor - 텍스트 */
    public Dom(String innerText) {
        this.isTag = false;
        this.tag = null;
        this.tagType = 0;
        this.childs = null;
        this.attribute = null;
        this.innerText = innerText;
    }

    /* Constructor - 태그 (1) */
    public Dom(String tag, int tagType, Map<String, String> attribute, Dom[] childs) {
        this.isTag = true;
        this.tag = tag;
        this.tagType = tagType;
        this.childs = childs;
        this.attribute = attribute;
    }

    /* Constructor - 태그 (2) */
    public Dom(String tag, int tagType, Map<String, String> attribute) {
        this.isTag = true;
        this.tag = tag;
        this.tagType = tagType;
        this.childs = null;
        this.attribute = attribute;
        this.innerText = "";
    }

    /* getElementByTagName   */
    public Dom[] getElementByTagName(String tag) {
        if (isTag) {
            List<Dom> list = new LinkedList<>();
            getSameTagName(list, tag);
            return list.toArray(new Dom[list.size()]);
        }
        return null;
    }

    /* getSameTagName */
    void getSameTagName(List<Dom> tags, String tag) {
        if (isTag) {
            if (this.tag.equals(tag)) {
                tags.add(this);
            }
            if (childs != null) {
                for (Dom d : childs) {
                    d.getSameTagName(tags, tag);
                }
            }
        }
    }

    /* getElementById */
    public Dom getElementById(String id) {
        if (isTag) {
            if (attribute != null) {
                String domId = attribute.get("id");
                if (domId != null && domId.equals(id)) {
                    return this;
                }
            }
            if (childs != null) {
                for (Dom d : childs) {
                    Dom target = d.getElementById(id);
                    if (target != null) return target;
                }
            }
        }
        return null;
    }

    /* getElementByClassName */
    public Dom[] getElementByClassName(String clazz) {
        if (isTag) {
            List<Dom> list = new LinkedList<>();
            getSameClassName(list, clazz);
            return list.toArray(new Dom[list.size()]);
        }
        return null;
    }

    /* getSameClassName */
    public void getSameClassName(List<Dom> doms, String clazz) {
        if (isTag) {
            if (attribute != null) {
                String domClass = attribute.get("class");
                if (
                        domClass instanceof String
                                &&
                                domClass.indexOf(clazz) >= 0
                ) doms.add(this);
            }
            if (childs != null) {
                for (Dom d : childs) {
                    d.getSameClassName(doms, clazz);
                }
            }
        }
    }

    /* getInnerText */
    public String getInnerText() {
        return "";
    }

    /* print */
    public void print() {
        _print("");
    }

    /* _print */
    public void _print(String tab) {
        if (isTag) _printTag(false, tab, fpPrint);
        else _printLine(false, tab);
    }

    /* printClean */
    public void printClean() {
        _printClean("");
    }

    /* _printClean */
    private void _printClean(String tab) {
        if (isTag) _printTag(true, tab, fpPrintClean);
        else _printLine(true, tab);
    }

    /* _printTag */
    private void _printTag(boolean isClean, String tab, FunctionPrint functionPrint) {
        if (isClean) {
            // == Js & Css 제외출력 ==
            if (this.tag.equals("script") || this.tag.equals("style")) return;
        }

        // == 열림태크 ==
        System.out.println(tab + _toOpenString());
        if (this.tagType == Define.DOM_TAG_TYPE_A) {
            // == 내부자식 ==
            if (childs != null) {
                for (Dom d : childs) functionPrint.print(d, tab + "\t");
            }
            // == 닫힘태크 ==
            System.out.println(tab + _toCloseString());
        }
    }

    /* _printLine */
    private void _printLine(boolean isClean, String tab) {
        if (isClean) {
            // == 주석제외출력 ==
            if (this.innerText.indexOf("<!--") >= 0) return;
        }
        for (String line : this.innerText.split("\n")) {
            System.out.println(tab + line);
        }
    }

    /* toString */
    public String toString() {
        // == <open> ==
        String rtn = _toOpenString();
        // == </close> ==
        if (this.tagType == Define.DOM_TAG_TYPE_A) rtn += _toCloseString();
        return rtn;
    }

    /* _toOpenString */
    private String _toOpenString() {
        StringBuffer sb = new StringBuffer();

        // == <open> ==
        sb.append("<" + tag);
        // == Attribute 있다면 ==
        if (attribute != null && attribute.size() > 0) {
            sb.append(" ");
            attribute.forEach((k, v) -> {
                if (v != null) {
                    sb.append(String.format("%s=\"%s\" ", k, v));
                } else {
                    sb.append(k + " ");
                }
            });
            sb.deleteCharAt(sb.length() - 1);
        }

        // == <openclose/> ==
        if (this.tagType == Define.DOM_TAG_TYPE_B) sb.append("/");
        sb.append(">");
        return sb.toString();
    }

    /* _toCloseString */
    private String _toCloseString() {
        return String.format("</%s>", tag);
    }
}
