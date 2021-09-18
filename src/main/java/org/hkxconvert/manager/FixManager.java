package org.hkxconvert.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hkxconvert.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.hkxconvert.Const.DUMP_COMMAND_TEMPLATE;
import static org.hkxconvert.Const.UPDATE_COMMAND_TEMPLATE;

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
        File[] fileList = Objects.requireNonNull(root.listFiles());
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
                System.out.println(filePaths.get(filePaths.size() - 1).txt.getPath());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 修复注解
     */
    public abstract void fixAnno();

    /**
     * 将修复后的注解更新到HKX动作文件
     */
    public void updateAnno() {
        for (FilePath file : filePaths) {
            try {
                File txt = file.txt;
                File hkx = file.hkx;
                String command = String.format(UPDATE_COMMAND_TEMPLATE, txt.getPath(), hkx.getPath());

                if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                    System.out.println(hkx.getPath() + "更新成功！");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
