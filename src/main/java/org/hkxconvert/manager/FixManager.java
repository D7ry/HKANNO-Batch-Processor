package org.hkxconvert.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hkxconvert.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.hkxconvert.Main.DUMP_COMMAND;
import static org.hkxconvert.Main.UPDATE_COMMAND;

/** manager class used to dump and update anno.
 *
 */
@Data
@AllArgsConstructor
public abstract class FixManager {

    public File root;
    public List<FilePath> _filePaths;

    /**returns THIS filepath* */
    public List<FilePath> getFilePaths(){
        return _filePaths;
    }

    /**
     * 批量导出注解TXT文件
     */
    public void dumpAnno() {
        System.out.println("\ndumping annos");
        RecDir(root, _filePaths);
        System.out.println("dump complete");
    }

    public void RecDir(File root, List<FilePath> filePaths) {
        if (root.listFiles().length == 0) {
            System.out.println("Error: No animation found in /animation folder. Dump failed.");
            return;
        }
        File[] fileList = root.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                RecDir(file, filePaths);
            } else if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.toLowerCase().contains(".hkx")) {
                    //System.out.println("filename is:" + fileName);
                    FilePath filePath = new FilePath(file, new File(file.getPath().split("\\.")[0] + ".txt"));
                    //System.out.println("filePath is:" + filePath);
                    createAnnoTextList(filePath, filePaths);
                }
            }
        }
    }

    void createAnnoTextList(FilePath filePath, List<FilePath> filePaths) {
        try {
            String command = String.format(DUMP_COMMAND, filePath.txt.getPath(), filePath.hkx.getPath());
            //System.out.println("using dump command: " + command);
            if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                filePaths.add(filePath);
                System.out.println("dumped: " + filePath.txt.getPath().split("\\.")[0] + ".txt");
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
    public void updateAnno() throws IOException {
        System.out.println("\nupdating annos");
        for (FilePath file : _filePaths) {
            try {
                File txt = file.txt;
                File hkx = file.hkx;
                //System.out.println(txt);
                //System.out.println(hkx);
                String command = String.format(UPDATE_COMMAND, txt.getPath(), hkx.getPath());
                System.out.println("updated: " + hkx.getName());
                if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("update complete");
    }


}
