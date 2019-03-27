package old.parser;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/* ResourceReader */
public class ResourceReader {

    /* readFromFile - 파일로부터읽기 */
    public static String readFromFile(String src) {
        String resource = "";
        int readCharNum;
        char[] charBuffer = new char[100];
        try {
            FileReader fr = new FileReader(src);
            while ((readCharNum = fr.read(charBuffer)) != -1) {
                resource += new String(charBuffer, 0, readCharNum);
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource;
    }

    /* readFromUrl - Url로부터읽기 */
    public static String readFromUrl(String src) {
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(src).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

}
