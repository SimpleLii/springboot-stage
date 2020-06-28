package com.simplelii.app.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SimpleLii
 * @description
 * @date 14:24 2020/6/24
 */
public class CamelToUnderlineUtil {
    public static final char UNDERLINE = '_';

    /**
     *  首字母驼峰标准化
     * @param param
     * @return
     */
    public static String camelStanderFormatFirstChar(String param){
        if (StringUtils.isEmpty(param)){
            return "";
        }
        String regStr = "[A-Z]{1}";
       return param.replaceFirst(regStr, param.substring(0, 1).toLowerCase());
    }

    public static String camelToUnderline(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE).append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(param);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf(UNDERLINE);
            if (index > 0) {
                sb.append(word.substring(1, index));
            } else {
                sb.append(word.substring(1));
            }
        }
        return sb.toString();
    }
}
