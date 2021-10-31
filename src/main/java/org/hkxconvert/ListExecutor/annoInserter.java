package org.hkxconvert.ListExecutor;

import org.hkxconvert.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;

import static org.hkxconvert.Utils.addFrame;

/** class to help add annotation after certain LINE indicated by TEMPLATE.
 *
 */

public class annoInserter {

    public annoInserter(File txt, String line, String template) throws FileNotFoundException {
        _txt = txt;
        _reader = new Scanner(txt);
        _line = line;
        _template = line;
        _lines = new ArrayList<String>();
    }

    /** import all the lines from txt, and insert annos.
     * annos inserted by default are one frame later than the previous anno.
     */
    public boolean insertAfter() throws IOException {
        boolean fixed = false;
        while (_reader.hasNextLine()) {
            String line = _reader.nextLine();
            _lines.add(line);
            if (line.toLowerCase(Locale.ROOT).contains(_line)) {
                String prevFrame = line.split(" ")[0];
                String newLine = String.format(_template, addFrame(prevFrame));
                _lines.add(newLine);
                fixed = true;
            }
        }
        _reader.close();
        writeAnno();
        return fixed;
    }

    /** imports all the lines from txt, and insert annos.
     * annos inserted are FRAME later than the previous anno.
     */
    public boolean insertAfter(float frame) throws IOException {
        boolean fixed = false;
        while (_reader.hasNextLine()) {
            String line = _reader.nextLine();
            _lines.add(line);
            if (line.toLowerCase(Locale.ROOT).contains(_line)) {
                String prevFrame = line.split(" ")[0];
                String newline = String.format(_template, addFrame(prevFrame, frame));
                _lines.add(newline);
                fixed = true;
            }
        }
        _reader.close();
        writeAnno();
        return fixed;
    }

    public boolean insertBefore() {
        boolean fixed = false;
        return fixed;
    }

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
}
