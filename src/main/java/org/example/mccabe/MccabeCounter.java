package org.example.mccabe;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MccabeCounter {
    public void start(String path) {
        Map<String, CKClassResult> results = new HashMap<>();

        boolean useJars = false;
        int maxAtOnce = 20;
        boolean variablesAndFields = false;
        new CK(useJars, maxAtOnce, variablesAndFields).calculate(path, new CKNotifier() {
            @Override
            public void notify(CKClassResult result) {
                results.put(result.getClassName(), result);
            }

            @Override
            public void notifyError(String sourceFilePath, Exception e) {
                System.err.println("Ошибка при чтении файла: " + sourceFilePath);
                e.printStackTrace(System.err);
            }
        });

        for(Map.Entry<String, CKClassResult> entry : results.entrySet()){
            System.out.println("Класс: " + entry.getValue().getClassName() + ", V(G) = " + entry.getValue().getWmc());
        }
    }
}
