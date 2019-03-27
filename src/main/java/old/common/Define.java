package old.common;

public class Define {

    // @TAG
    public static final String TAG_ROOT = "root";
    public static final String TAG_SCRIPT = "script";

    public static final byte TAG_TYPE_A = 1; // <열림>
    public static final byte TAG_TYPE_B = -1; // </닫힘>
    public static final byte TAG_TYPE_C = 0; // <열림닫힘>

    public static final byte DOM_TAG_TYPE_A = 1; // <열림></닫힘>
    public static final byte DOM_TAG_TYPE_B = 2; // <열림닫힘/>
    public static final byte DOM_TAG_TYPE_C = 3; // <열림>

    public static String REMARK_PATTERN_START = "<!--";
    public static String REMARK_PATTERN_END = "-->";
}
