package org.hkxconvert.executor;

import org.hkxconvert.FilePath;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

public class PowerAttackFix extends FixExecutor {

    public PowerAttackFix(File txt, RandomAccessFile reader) {
        super(txt, reader);
    }

    @Override
    public boolean fix(List<FilePath> filePaths) {
        // TODO: 2021/9/19 修复SKYSA重击动画BUG
        return isNotFix();
    }
}
