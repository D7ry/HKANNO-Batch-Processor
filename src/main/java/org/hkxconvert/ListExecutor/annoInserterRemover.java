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

public class annoInserterRemover extends ListFixExecutor {

    /**
     * @param txt the txt file output from HKANNO.
     * @param line the line on which to perform action.
     * @param template the string to be formatted as a new annotation to insert.
     * @throws FileNotFoundException
     */
    public annoInserterRemover(File txt, String line, String template) throws FileNotFoundException {
        super(txt, line, template);
    }

    @Override
    public boolean fix(List<FilePath> filePaths) throws IOException {
        return false;
    }

    /**read through the list and add lines at corresponding positions */
    private void inserterProcess(double frame, boolean insertAfter) {
        while (_reader.hasNextLine()) {
            String line = _reader.nextLine();
            _lines.add(line);
            if (line.contains(_line)) {
                String prevFrame = getTime(line);
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
    /**what actuallys happens in remover */
    private void removerProcess() {
        while(_reader.hasNextLine()) {
            String line = _reader.nextLine();
            if (!line.contains(_line)) {
                _lines.add(line);
            } else {
                System.out.println("removing line: " + line);
                _fixed = true;
            }
        }
    }

    /** quick AMR bug fix. */
    private void repetitionRemoverProcess() {
        ArrayList<String> repetitiveLines = new ArrayList<>();
        String prevPos = null;
        while (_reader.hasNextLine()) {
            String line = _reader.nextLine();
            if (prevPos != null && line.contains("animmotion") && AMRannoSpliter(line).equals(prevPos)) {
                System.out.println("repetitive line: " + line);
                repetitiveLines.add(line);
                _fixed = true;
            } else {
                if (!repetitiveLines.isEmpty()) {
                    String pairAnno = repetitiveLines.get(repetitiveLines.size() - 1);
                    _lines.add(pairAnno);
                    System.out.println("pair anno appended: " + pairAnno);
                }
                _lines.add(line);
                if (line.contains("animmotion")) {
                    prevPos = AMRannoSpliter(line);
                }
                repetitiveLines.clear();
            }
        }
        if (!repetitiveLines.isEmpty()) {
            _lines.add(repetitiveLines.get(repetitiveLines.size() - 1));
        }
    }

    /**parse AMR anno into x y z */
    private String AMRannoSpliter (String line) {
        String[] spLine = line.split(" ");
        return spLine[2] + " " + spLine[3] + " " + spLine[4];
    }

    /**what actually happens in replacer */
    private void replacerProcess(String newAnno) {
        while (_reader.hasNextLine()) {
            String line = _reader.nextLine();
            if (line.contains(_line)) {
                String prevFrame = getTime(line);
                String newLine = prevFrame + " " + newAnno;
                _lines.add(newLine);
                System.out.println("replaced: " + line);
                System.out.println("with: " + newLine);
                _fixed = true;
            } else {
                _lines.add(line);
            }
        }
    }

    /**replace lines containing TEMPLATE with NEWANNO
     *
     */
    public boolean replace(String newAnno) throws FileNotFoundException {
        replacerProcess(newAnno);
        _reader.close();
        writeAnno();
        return _fixed;
    }

    /**
     * remove lines containing TEMPLATE.
     */
    public boolean remove() throws FileNotFoundException {
        removerProcess();
        _reader.close();
        writeAnno();
        return _fixed;
    }

    /**
     * remove repetitve AMR annos.
     */
    public boolean removeRep() throws FileNotFoundException {
        repetitionRemoverProcess();
        _reader.close();
        writeAnno();
        return _fixed;
    }

    /** insert TEMPLATE after the _LINE by default frame.
     */
    public boolean insertAfter() throws IOException {
        inserterProcess(ONE_FRAME, true);
        _reader.close();
        writeAnno();
        return _fixed;
    }

    /** insert TEMPLATE FRAME after the _LINE
     */
    public boolean insertAfter(double frame) throws IOException {
        inserterProcess(frame, true);
        _reader.close();
        writeAnno();
        return _fixed;
    }

    /** insert TEMPLATE FRAME before the _LINE. */
    public boolean insertBefore() throws IOException {
        inserterProcess(ONE_FRAME, false);
        _reader.close();
        writeAnno();
        return _fixed;
    }
    /**insert TEMPLATE before the _LINE by default frame. */
    public boolean insertBefore(double frame) throws IOException{
        inserterProcess(frame, false);
        _reader.close();
        writeAnno();
        return _fixed;
    }




}
