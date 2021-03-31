package com.company;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Util {

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static String encodeValue(String value) throws Exception
    {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}
