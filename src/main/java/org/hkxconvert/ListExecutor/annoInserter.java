package org.hkxconvert.ListExecutor;

import org.hkxconvert.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

import static org.hkxconvert.Utils.addFrame;
import static org.hkxconvert.Const.ONE_FRAME;

/** class to help add annotation after certain LINE indicated by TEMPLATE.
 *
 */

public class annoInserter {

    /**
     * @param txt the txt file output from HKANNO.
     * @param line the line before/after which the annotation is added.
     * @param template the string to be formatted as a new annotation to insert.
     * @throws FileNotFoundException
     */
    public annoInserter(File txt, String line, String template) throws FileNotFoundException {
        _txt = txt;
        _reader = new Scanner(txt);
        _line = line;
        _template = template;
        _lines = new ArrayList<String>();
        _fixed = false;
        System.out.println("");
        System.out.println("start fixing: " + _txt.getName());
    }

    /**read through the list and add lines at corresponding positions */
    public void readerProcess(double frame, boolean insertAfter) {
        while (_reader.hasNextLine()) {
            String line = _reader.nextLine();
            _lines.add(line);
            if (line.contains(_line)) {
                String prevFrame = line.split(" ")[0];
                if (insertAfter) {
                    String newLine = String.format(_template, addFrame(prevFrame, frame));
                    _lines.add(newLine);
                    System.out.println("after: " + line);
                    System.out.println("added anno: " + newLine);
                } else {
                    String newLine = String.format(_template, addFrame(prevFrame, -frame));
                    _lines.add(_lines.size() - 1, newLine);
                    System.out.println("before: " + line);
                    System.out.println("added anno: " + newLine);
                }
                _fixed = true;
            }
        }
    }

    /** insert TEMPLATE after the _LINE by default frame.
     */
    public boolean insertAfter() throws IOException {
        readerProcess(ONE_FRAME, true);
        _reader.close();
        writeAnno();
        return _fixed;
    }

    /** insert TEMPLATE FRAME after the _LINE
     */
    public boolean insertAfter(double frame) throws IOException {
        readerProcess(frame, true);
        _reader.close();
        writeAnno();
        return _fixed;
    }

    /** insert TEMPLATE FRAME before the _LINE. */
    public boolean insertBefore() throws IOException {
        readerProcess(ONE_FRAME, false);
        _reader.close();
        writeAnno();
        return _fixed;
    }
    /**insert TEMPLATE before the _LINE by default frame. */
    public boolean insertBefore(double frame) throws IOException{
        readerProcess(frame, false);
        _reader.close();
        writeAnno();
        return _fixed;
    }

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
}
