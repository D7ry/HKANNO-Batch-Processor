package org.hkxconvert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hkxconvert.Const.PATH;
import static org.hkxconvert.Utils.dumpAnno;
import static org.hkxconvert.Utils.fixAnno;

public class Main {

    public static void main(String[] args) {
        File root = new File(PATH);
        List<FilePath> filePaths = new ArrayList<>();
        dumpAnno(root, filePaths);
        fixAnno(filePaths);
//        updateAnno(filePaths);
    }

}
