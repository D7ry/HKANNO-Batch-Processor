package org.hkxconvert.manager;

import org.hkxconvert.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SimpleDumper extends FixManager{
    public SimpleDumper(File root, List<FilePath> filePaths) {
        super(root, filePaths);
        System.out.println("dumping all annos");
    }

    @Override
    public void fixAnno() throws IOException {

    }
}
