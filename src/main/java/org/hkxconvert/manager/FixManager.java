package org.hkxconvert.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hkxconvert.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.hkxconvert.Const.DUMP_COMMAND_TEMPLATE;
import static org.hkxconvert.Const.UPDATE_COMMAND_TEMPLATE;

/** manager class used to dump and update anno.
 *
 */
@Data
@AllArgsConstructor
public abstract class FixManager {

    private File root;
    private List<FilePath> filePaths;


    /**
     * 批量导出注解TXT文件
     */
    public void dumpAnno() {
        RecDir(root, filePaths);
    }

    public static void RecDir(File root, List<FilePath> filePaths) {
        if (root.listFiles() == null) {
            System.out.println("Error: No valid hkx found in hkxconvert folder. Terminating process.");
            System.exit(10);
        }
        File[] fileList = root.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                RecDir(file, filePaths);
            } else if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.toLowerCase().contains(".hkx")) {
                    FilePath filePath = new FilePath(file, new File(file.getPath().split("\\.")[0] + ".txt"));
                    createAnnoTextList(filePath, filePaths);
                }
            }
        }
    }

    private static void createAnnoTextList(FilePath filePath, List<FilePath> filePaths) {
        try {
            String command = String.format(DUMP_COMMAND_TEMPLATE, filePath.txt.getPath(), filePath.hkx.getPath());
            if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                filePaths.add(filePath);
                System.out.println("Anno exported to: " + filePaths.get(filePaths.size() - 1).txt.getPath());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * fix annos.
     */
    public abstract void fixAnno() throws IOException;

    /**
     * update fixed annos to txt files.
     */
    public void updateAnno() {
        System.out.println();
        for (FilePath file : filePaths) {
            try {
                File txt = file.txt;
                File hkx = file.hkx;
                String command = String.format(UPDATE_COMMAND_TEMPLATE, txt.getPath(), hkx.getPath());
                System.out.println("successfully updated: " + hkx.getName());
                if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
