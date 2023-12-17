package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MetricCounter {
    private List<String> allLines = new ArrayList<>();
    private List<Double> values = new ArrayList<>();
    private boolean read = false;
    public void readAllSourceCodeLines(Path path) {
        if (read) {
            return;
        }
        try {
            Files.walk(path).forEach(p -> {
                if (p.getFileName().toString().endsWith(".java")) {
                    try {
                        allLines.addAll(Files.readAllLines(p));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        read = true;
    }

    public void countLOCMetrics(Path path){
        readAllSourceCodeLines(path);

        System.out.println("LOC = " + allLines.size());

        int n = 10;
        int linesPerBlock = allLines.size() / n;
        int cnt = 0;
        int numOfComments = 0;
        boolean longComment = false;
        for (int i = 0; i < allLines.size(); i++) {
            if (cnt == linesPerBlock) {
                values.add(numOfComments/ (double)linesPerBlock - 0.1);
                cnt = 0;
                numOfComments = 0;
            }

            int singleCommInd = allLines.get(i).indexOf("//");
            int multiCommInd = allLines.get(i).indexOf("/*");
            int multiCommIndEnd = allLines.get(i).indexOf("*/");

            if (multiCommIndEnd != -1) {
                if (multiCommInd == -1) {
                    numOfComments++;
                    longComment = false;
                }
            }

            if (longComment) {
                numOfComments++;
            }

            if (singleCommInd != -1) {
                if (multiCommInd != -1) {
                    if (multiCommInd < singleCommInd) {
                        longComment = true;
                        numOfComments++;
                    } else {
                        numOfComments++;
                    }
                }else {
                    numOfComments++;
                }
            } else if (multiCommInd != -1) {
                longComment = true;
                numOfComments++;
            }

            cnt++;
        }
        if (numOfComments != 0) {
            values.add(numOfComments/ (double)linesPerBlock - 0.1);
        }

        int result = 0;
        for (Double val : values) {
            if (val > 0) {
                result++;
            } else if (val < 0){
                result--;
            }
        }

        System.out.println("F = " + result);
    }

    public int countIfElse(Path path){
        readAllSourceCodeLines(path);

        int res = 0;
        for (String line : allLines) {
            if (line.contains("if") || line.contains("else")) {
                res++;
            }
        }

        return res;
    }
}
