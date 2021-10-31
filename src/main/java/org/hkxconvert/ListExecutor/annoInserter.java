package org.hkxconvert.ListExecutor;

import org.hkxconvert.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import static org.hkxconvert.Utils.addFrame;
import static org.hkxconvert.Const.ONE_FRAME;

/** class to help add annotation after certain LINE indicated by TEMPLATE.
 *
 */

public class annoInserter extends ListFixExecutor {

    /**
     * @param txt the txt file output from HKANNO.
     * @param line the line before/after which the annotation is added.
     * @param template the string to be formatted as a new annotation to insert.
     * @throws FileNotFoundException
     */
    public annoInserter(File txt, String line, String template) throws FileNotFoundException {
        super(txt, line, template);
    }

    @Override
    public boolean fix(List<FilePath> filePaths) throws IOException {
        return false;
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




}
