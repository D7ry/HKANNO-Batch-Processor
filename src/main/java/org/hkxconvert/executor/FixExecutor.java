package org.hkxconvert.executor;

import lombok.Data;
import org.hkxconvert.FilePath;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

@Data
public abstract class FixExecutor {
    File txt;
    RandomAccessFile reader;
    boolean notFix = true;
    String line;
    String temp = null;
    long tempPointer = 0;
    int tempSize = 0;

    public FixExecutor(File txt, RandomAccessFile reader) {
        this.txt = txt;
        this.reader = reader;
    }

    public abstract boolean fix(List<FilePath> filePaths) throws IOException;

    /**
     * append ANNO to the end of the file
     */
    public void addAnno(String anno, RandomAccessFile raf) {
        try (RandomAccessFile fileWriter = raf) {
            fileWriter.seek(fileWriter.length());
            fileWriter.writeBytes(anno);
        } catch (IOException e) {
            System.out.println("Error appending File.");
            e.printStackTrace();
        }
    }
}
