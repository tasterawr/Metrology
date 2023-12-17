package org.example;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LexemeCounter {
    private int distinctOperators =0;
    private int distinctOperands =0;
    private int totalOperators =0;
    private int totalOperands =0;
    private int operatorCount =0;
    private int operandCount =0;

    // construct AST of the .java files
    public static ASTVisitorMod parse(char[] str) {
        ASTParser parser = ASTParser.newParser(8);
        parser.setSource(str);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);

        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_18);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
                JavaCore.VERSION_18);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_18);
        parser.setCompilerOptions(options);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        // Check for compilationUnits problems in the provided file
        IProblem[] problems = cu.getProblems();
        for(IProblem problem : problems) {
            // Ignore some error because of the different versions.
            if (problem.getID() == 1610613332) 		 // 1610613332 = Syntax error, annotations are only available if source level is 5.0
                continue;
            else if (problem.getID() == 1610613329) // 1610613329 = Syntax error, parameterized types are only available if source level is 5.0
                continue;
            else if (problem.getID() == 1610613328) // 1610613328 = Syntax error, 'for each' statements are only available if source level is 5.0
                continue;
            else
            {
                // quit compilation if
                System.out.println("CompilationUnit problem Message " + problem.getMessage() + " \t At line= "+problem.getSourceLineNumber() + "\t Problem ID="+ problem.getID());

                System.out.println("The program will quit now!");
                System.exit(1);
            }
        }

        // visit nodes of the constructed AST
        ASTVisitorMod visitor= new ASTVisitorMod();
        cu.accept(visitor);

        return visitor;
    }



    // parse file in char array
    public static char[] ReadFileToCharArray(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();

        return  fileData.toString().toCharArray();
    }



    // parse files in a directory to list of char array
    public static List<char[]> ParseFilesInDir(List<String> JavaFiles) throws IOException{
        if(JavaFiles.isEmpty())
        {
            System.out.println("There is no java source code in the provided directory");
            System.exit(0);
        }

        List<char[]> FilesRead= new ArrayList<char []>();

        for(int i=0; i<JavaFiles.size(); i++)
        {
            System.out.println("Now, reading: "+ JavaFiles.get(i));
            FilesRead.add(ReadFileToCharArray(JavaFiles.get(i)));
        }

        return FilesRead;
    }



    // retrieve all .java files in the directory and subdirectories.
    public static List<String> retrieveFiles(String directory) {
        List<String> Files = new ArrayList<String>();
        File dir = new File(directory);

        if (!dir.isDirectory())
        {
            System.out.println("The provided path is not a valid directory");
            System.exit(1);
        }

        for (File file : dir.listFiles()) {
            if(file.isDirectory())
            {
                Files.addAll(retrieveFiles(file.getAbsolutePath()));
            }
            if (file.getName().endsWith((".java")))
            {
                Files.add(file.getAbsolutePath());
            }
        }

        return Files;
    }



    public void start(String path) throws IOException {
        List<String> JavaFiles=retrieveFiles(path);

        List<char[]> FilesRead=ParseFilesInDir(JavaFiles);

        ASTVisitorMod ASTVisitorFile;
        distinctOperators =0;
        distinctOperands =0;
        totalOperators =0;
        totalOperands =0;
        operatorCount =0;
        operandCount =0;

        for(int i=0; i<FilesRead.size(); i++)
        {
            ASTVisitorFile=parse(FilesRead.get(i));
            distinctOperators +=ASTVisitorFile.oprt.size();
            distinctOperands +=ASTVisitorFile.names.size();

            operatorCount =0;
            for (int f : ASTVisitorFile.oprt.values()) {
                operatorCount += f;
            }
            totalOperators += operatorCount;

            operandCount =0;
            for (int f : ASTVisitorFile.names.values()) {
                operandCount += f;
            }
            totalOperands += operandCount;
        }
    }

    public int getDistinctOperators() {
        return distinctOperators;
    }

    public int getDistinctOperands() {
        return distinctOperands;
    }

    public int getTotalOperators() {
        return totalOperators;
    }

    public int getTotalOperands() {
        return totalOperands;
    }

    public int getOperatorCount() {
        return operatorCount;
    }

    public int getOperandCount() {
        return operandCount;
    }
}
