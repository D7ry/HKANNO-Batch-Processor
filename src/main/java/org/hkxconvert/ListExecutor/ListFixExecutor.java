package org.hkxconvert.ListExecutor;

import lombok.Data;
import org.hkxconvert.FilePath;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
public abstract class ListFixExecutor {
    File _txt;
    Scanner _reader;
    boolean _notFix = true;
    String _line;
    String _temp = null;
    long _tempPointer = 0;
    int _tempSize = 0;

    public ListFixExecutor(File txt, Scanner reader) {
        this._txt = txt;
        this._reader = reader;
    }

    public abstract boolean fix(List<FilePath> filePaths) throws IOException;



}
