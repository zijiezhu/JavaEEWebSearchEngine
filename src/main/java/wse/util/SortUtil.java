package main.java.wse.util;

import java.io.IOException;

public class SortUtil {

    public void sortByUnixSort(String input, String output) {
        try {
            String cmd[] = {
                    "/bin/sh",
                    "-c",
                    "sort -k1,1 -k2n,2" + " " + input + " > " + output
            };
          System.out.println(cmd[2]);
          Process process =  Runtime.getRuntime().exec(cmd);
          process.waitFor();

        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
