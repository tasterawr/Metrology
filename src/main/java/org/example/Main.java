package org.example;

import org.example.mccabe.MccabeCounter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь до директории:");
        String dir = scanner.nextLine().replace("\\", "/");
        // Задание 3
        MetricCounter metricCounter = new MetricCounter();
        Path path = Path.of(dir);
        metricCounter.countLOCMetrics(path);

        // Задание 4
        LexemeCounter lexemeCounter = new LexemeCounter();
        lexemeCounter.start(dir);
        int CL = metricCounter.countIfElse(path);
        int n = lexemeCounter.getTotalOperators();
        System.out.println("CL = " + CL);
        System.out.println("N = " + n);
        System.out.println("cl = " + CL / (double) n);

        // Задание 5
        int NUOprnd = lexemeCounter.getDistinctOperands();
        int NUOprtr = lexemeCounter.getDistinctOperators();
        int Noprnd = lexemeCounter.getTotalOperands();
        int Noprtr = lexemeCounter.getTotalOperators();
        System.out.println("NUOprnd = " + NUOprnd);
        System.out.println("NUOprtr = " + NUOprtr);
        System.out.println("Noprnd = " + Noprnd);
        System.out.println("Noprtr = " + Noprtr);


        int HPVoc = NUOprtr + NUOprnd;
        int HPLen = Noprtr + Noprnd;
        double HPVol = (Noprtr + Noprnd)*(Math.log(NUOprtr + NUOprnd)/Math.log(2));
        double HDiff = (NUOprtr/2)*(Noprnd/(double)Noprtr);
        double HEff = HDiff * HPVol;

        System.out.println("HPVoc = " + HPVoc);
        System.out.println("HPLen = " + HPLen);
        System.out.println("HPVol = " + HPVol);
        System.out.println("HDiff = " + HDiff);
        System.out.println("HEff = " + HEff);

        // Задание 6
        MccabeCounter mccabeCounter = new MccabeCounter();
        mccabeCounter.start(dir);
    }
}