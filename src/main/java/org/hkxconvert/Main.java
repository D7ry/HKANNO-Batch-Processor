package org.hkxconvert;

import org.hkxconvert.manager.EnemyBrutalizer;
import org.hkxconvert.manager.FixManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hkxconvert.Const.PATH;

public class Main {

    public static void main(String[] args) {
        File root = new File(PATH);
        List<FilePath> filePaths = new ArrayList<>();
        FixManager fixManager = new EnemyBrutalizer(root, filePaths);
        fixManager.dumpAnno();
        fixManager.fixAnno();
        fixManager.updateAnno();
    }

}
