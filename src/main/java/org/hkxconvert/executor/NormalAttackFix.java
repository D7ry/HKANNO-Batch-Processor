package org.hkxconvert.executor;

import org.hkxconvert.FilePath;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static org.hkxconvert.Const.ATTACK_STOP;
import static org.hkxconvert.Const.FIX_TEMPLATE;
import static org.hkxconvert.Utils.addFrame;

public class NormalAttackFix extends FixExecutor {

    public NormalAttackFix(File txt, RandomAccessFile reader) {
        super(txt, reader);
    }

    @Override
    public boolean fix(List<FilePath> filePaths) throws IOException {
        while ((line = reader.readLine()) != null) {
            long pointer = getReader().getFilePointer();

            if (line.contains("SkySA_AttackWinEnd")) {
                temp = line;
                tempPointer = pointer;
                tempSize = line.length();
            }

            if ((line.contains("Stop") || line.contains("stop")) && temp != null) {
                String attackStopFrame = line.split(" ")[0];
                reader.seek(pointer - line.length() - 2);
                reader.writeBytes(String.format(ATTACK_STOP, attackStopFrame));
                String fixedString = addFrame(attackStopFrame);
                reader.seek(tempPointer - tempSize - 2);
                reader.writeBytes(String.format(FIX_TEMPLATE, fixedString));
                reader.seek(pointer);
                setNotFix(false);
                System.out.println(getTxt() + line);
            }
        }
        return isNotFix();
    }

}
