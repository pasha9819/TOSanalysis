package io;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class with static IO methods
 */
public class IOUtil {
    /**
     * Read data from <code>BufferedReader</code>
     * @param reader buffer for reading
     * @return A String containing the contents of the buffer
     * @throws IOException If an I/O error occurs
     */
    public static String readFromBuffReader(BufferedReader reader) throws IOException {
        String temp;
        StringBuilder sb = new StringBuilder();
        while ((temp = reader.readLine()) != null) {
            sb.append(temp).append('\n');
        }
        return sb.toString();
    }
}
