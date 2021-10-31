package org.hkxconvert.ListExecutor;

import lombok.Data;
import org.hkxconvert.FilePath;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
public abstract class ListFixExecutor {

    /**the txt file to deal with */
    File _txt;
    /**self evident */
    Scanner _reader;
    /** after which line to add anno*/
    String _line;
    /**template of anno */
    String _template;
    /**temporary storage for lines in TXT*/
    ArrayList<String> _lines;
    boolean _fixed;

    public ListFixExecutor(File txt, String line, String template) throws FileNotFoundException {
        _txt = txt;
        _reader = new Scanner(txt);
        _line = line;
        _template = template;
        _lines = new ArrayList<String>();
        _fixed = false;
        System.out.println("\nstart fixing: " + _txt.getName());
    }

    public abstract boolean fix(List<FilePath> filePaths) throws IOException;

    /**write annotations in the list back to the txt*/
    void writeAnno() throws FileNotFoundException {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(_txt);
            for (String line : _lines) {
                pw.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }




}
