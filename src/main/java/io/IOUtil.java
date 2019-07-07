package io;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtil {
    public static String readFromBuffReader(BufferedReader reader) throws IOException {
        String temp;
        StringBuilder sb = new StringBuilder();
        while ((temp = reader.readLine()) != null) {
            sb.append(temp).append('\n');
        }
        return sb.toString();
    }
}
