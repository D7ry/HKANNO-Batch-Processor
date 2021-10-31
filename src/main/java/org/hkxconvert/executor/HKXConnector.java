package org.hkxconvert.executor;


import org.hkxconvert.FilePath;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static org.hkxconvert.Utils.addFrame;

/** and annotation right after certain line.
 */
public class HKXConnector extends FixExecutor {

    public HKXConnector(File txt, RandomAccessFile reader) {
        super(txt, reader);
    }

    public HKXConnector(File txt, RandomAccessFile reader, String fixTemplate) {
        super(txt, reader);
        _fixTemplate = fixTemplate;
    }

    /**
     * adds attackStart anno to attackwinstart to force NPC into the next move
     **/
    @Override
    public boolean fix(List<FilePath> filePaths) throws IOException {
        while ((line = reader.readLine()) != null) {
            long pointer = getReader().getFilePointer();
            if (line.contains("SkySA_AttackWinStart")) {
                String attackStartFrame = line.split(" ")[0];
                String attackStartAnno = String.format(_fixTemplate, addFrame(attackStartFrame));
                System.out.println(attackStartAnno);
                addAnno(attackStartAnno, reader);
                setNotFix(false);
                break;
            }
        }
        return isNotFix();
    }

    private String _fixTemplate;

}
