package org.hkxconvert.manager;

import org.hkxconvert.FilePath;
import org.hkxconvert.ListExecutor.annoInserter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ListIterator;

import static org.hkxconvert.Const.*;

public class EnemyBrutalizerHvy extends FixManager {

    public EnemyBrutalizerHvy(File root, List<FilePath> filePaths) {
        super(root, filePaths);
        System.out.println("start chaining Skysa light to heavy combos");
    }

    @Override
    public void fixAnno() throws IOException {
        ListIterator<FilePath> li = getFilePaths().listIterator();
        while (li.hasNext()) {
            File txt = li.next().txt;
            annoInserter fixer = new annoInserter(txt, SKYSA_ATTACKWINSTART, SKYSA_HVY_COMBO_ANNO);
            if (fixer.insertAfter()) {
                System.out.println("successfully fixed:" + txt.getName());
            } else {
                li.remove();
                System.out.println("failed to fix:" + txt.getName());
            }
        }
    }
}
