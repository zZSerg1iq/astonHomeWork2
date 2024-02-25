package ru.zinoviev.resthomework.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParamValidator {

    public static boolean validateIdValueQuery(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateNameValueQuery(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9А-Яа-я]+$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
