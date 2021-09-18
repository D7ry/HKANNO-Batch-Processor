package org.hkxconvert.manager;

import org.hkxconvert.FilePath;
import org.hkxconvert.executor.FixExecutor;
import org.hkxconvert.executor.NormalAttackFix;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ListIterator;

public class AbrFixManager extends FixManager {

    public AbrFixManager(File root, List<FilePath> filePaths) {
        super(root, filePaths);
    }

    public void fixAnno() {
        ListIterator<FilePath> li = getFilePaths().listIterator();
        while (li.hasNext()) {
            File txt = li.next().txt;
            FixExecutor fixExecutor;

            try (RandomAccessFile reader = new RandomAccessFile(txt, "rw")) {
                fixExecutor = new NormalAttackFix(txt, reader);

                if (fixExecutor.fix(getFilePaths()))
                    li.remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
