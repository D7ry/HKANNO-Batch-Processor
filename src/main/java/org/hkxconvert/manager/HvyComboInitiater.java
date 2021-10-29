package org.hkxconvert.manager;

import org.hkxconvert.FilePath;
import org.hkxconvert.executor.FixExecutor;
import org.hkxconvert.executor.HKXConnector;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ListIterator;

import static org.hkxconvert.Const.SKYSA_HVY_COMBO_ANNO;

public class HvyComboInitiater extends FixManager {

    public HvyComboInitiater(File root, List<FilePath> filePaths) {
        super(root, filePaths);
    }

    @Override
    public void fixAnno() {
        ListIterator<FilePath> li = getFilePaths().listIterator();
        while (li.hasNext()) {
            File txt = li.next().txt;
            FixExecutor fixExecutor;
            try (RandomAccessFile reader = new RandomAccessFile(txt, "rw")) {
                fixExecutor = new HKXConnector(txt, reader, SKYSA_HVY_COMBO_ANNO);
                if (fixExecutor.fix(getFilePaths()))
                    li.remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
