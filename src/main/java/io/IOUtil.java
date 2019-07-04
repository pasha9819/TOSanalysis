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

    public static synchronized void print(String str){
        System.out.print(str);
    }

    public static synchronized void print(Object o){
        System.out.print(o);
    }

    public static synchronized void println(String str){
        System.out.println(str);
    }

    public static synchronized void println(Object o){
        System.out.println(o);
    }
}
