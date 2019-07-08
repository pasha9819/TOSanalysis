package tosamara.util;

import org.apache.commons.codec.digest.DigestUtils;
import tosamara.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Wrapper for generating the secret key depending on the ToSamara API method being called.
 */
public abstract class TokenGenerator {
    /**
     * Key for access to ToSamara API
     */
    private static String SECRET_KEY;

    static {
        try(BufferedReader r = new BufferedReader(new FileReader(Configuration.KEY_PATH))){
            SECRET_KEY = r.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Decode string by SHA-1 algorithm.
     * @param str string for encoding
     * @return encoded string
     */
    private static String getSHA(String str)  {
        return DigestUtils.sha1Hex(str);
    }

    /**
     * Build string for encoding.
     * @param arg objects for encoding
     * @return encoded string
     */
    public static String getToken(Object... arg) {
        StringBuilder b = new StringBuilder();
        for (Object o : arg){
            b.append(o.toString());
        }
        b.append(SECRET_KEY);
        return getSHA(b.toString());
    }
}
