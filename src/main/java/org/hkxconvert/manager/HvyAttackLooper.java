package org.hkxconvert.manager;

import org.hkxconvert.FilePath;
import org.hkxconvert.ListExecutor.annoInserter;
import org.hkxconvert.executor.FixExecutor;
import org.hkxconvert.executor.HKXConnector;
import org.hkxconvert.ListExecutor.ListFixExecutor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import static org.hkxconvert.Const.SKYSA_HVY_COMBO_ANNO;
import static org.hkxconvert.Const.HIT_FRAME;

public class HvyAttackLooper extends FixManager{

    public HvyAttackLooper (File root, List<FilePath> filePaths) {
        super(root, filePaths);
    }

    @Override
    public void fixAnno() {
        ListIterator<FilePath> li = getFilePaths().listIterator();
        while (li.hasNext()) {
            File txt = li.next().txt;
            ListFixExecutor fixer;
            try (Scanner reader = new Scanner(txt)) {
                //fixer = new annoInserter(txt, reader, HIT_FRAME, );
                //if (annoInserter.fix(getFilePaths()))
                    li.remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
