package com.erik.statistics;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by erimol on 2016-02-07.
 */
public class Test {

    public static void main(String[] args) {
        Path path = Paths.get("/Users/erimol/dev/statistics-parent/src.main.resources/", "html/index.html");
        System.out.println(path.normalize());
    }
}
