package tosamara.util;

import org.apache.commons.codec.digest.DigestUtils;
import tosamara.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class TokenGenerator {
    private static String SECRET_KEY;

    static {
        try(BufferedReader r = new BufferedReader(new FileReader(Configuration.KEY_PATH))){
            SECRET_KEY = r.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSHA(String str)  {
        return DigestUtils.sha1Hex(str);
    }

    public static String getToken(Object... arg) {
        StringBuilder b = new StringBuilder();
        for (Object o : arg){
            b.append(o.toString());
        }
        b.append(SECRET_KEY);
        return getSHA(b.toString());
    }
}
