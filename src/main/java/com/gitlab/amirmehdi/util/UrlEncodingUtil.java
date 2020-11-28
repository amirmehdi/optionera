package com.gitlab.amirmehdi.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncodingUtil {

    public static String getEncode(String connectionToken) {
        try {
            return URLEncoder.encode(connectionToken, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return connectionToken;
        }
    }
}
