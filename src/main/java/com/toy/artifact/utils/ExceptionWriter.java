package com.toy.artifact.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionWriter {
    public static String dump(Exception e) {
        var writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer, true));
        return writer.toString();
    }
}
