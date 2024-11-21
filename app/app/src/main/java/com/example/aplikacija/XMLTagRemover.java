package com.example.aplikacija;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLTagRemover {
    public static String strip(String input) {

        String regex = "<[^>]+>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        String result = matcher.replaceAll("");

        return result;
    }
}