package com.example.demo.common;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {

    public static boolean checkProgramArgs(String[] args, int count) {
        return StringUtils.isNoneEmpty(args) && args.length == count;
    }
}
