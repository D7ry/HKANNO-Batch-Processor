package org.hkxconvert;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static java.math.RoundingMode.DOWN;
import static org.hkxconvert.Const.*;

public class Utils {

    public static void dumpAnno(File root, List<FilePath> filePaths) {
        File[] fileList = Objects.requireNonNull(root.listFiles());
        for (File file : fileList) {
            if (file.isDirectory()) {
                dumpAnno(file, filePaths);
            } else if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.toLowerCase().contains(".hkx")) {
                    FilePath filePath = new FilePath(file, new File(file.getPath().split("\\.")[0] + ".txt"));
                    createAnnoTextList(filePath, filePaths);
                }
            }
        }
    }

//    public static void dumpAnno(File root, List<FilePath> filePaths) {
//        File[] fileList = Objects.requireNonNull(root.listFiles());
//        for (File file : fileList) {
//            if (file.isDirectory()) {
//                dumpAnno(file, filePaths);
//            } else if (file.isFile()) {
//                String fileName = file.getName();
//                if (fileName.toLowerCase().contains(".txt")) {
//                    FilePath filePath = new FilePath(file, null);
//                    filePaths.add(filePath);
//                }
//            }
//        }
//    }

    private static void createAnnoTextList(FilePath filePath, List<FilePath> filePaths) {
        try {
            String command = String.format(DUMP_COMMAND_TEMPLATE, filePath.txt.getPath(), filePath.hkx.getPath());

            if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                filePaths.add(filePath);
//                System.out.println(filePaths.get(filePaths.size() - 1).txt.getPath());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void fixAnno(List<FilePath> filePaths) {
        for (FilePath file : filePaths) {
            File txt = file.txt;
            try (RandomAccessFile reader = new RandomAccessFile(txt, "rw")) {
                String line;
                String temp = null;
                long tempPointer = 0;
                int tempSize = 0;
                while ((line = reader.readLine()) != null) {
                    long pointer = reader.getFilePointer();
                    if (line.contains("SkySA_AttackWinEnd")) {
                        temp = line;
                        System.out.println(line);
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
                        System.out.println(txt + line);
                        reader.seek(pointer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String addFrame(String fs) {
        BigDecimal decimal = new BigDecimal(fs);
        decimal = decimal.add(BigDecimal.valueOf(0.000001));
        return decimal.setScale(6, DOWN).toString();
    }

    public static void updateAnno(List<FilePath> filePaths) {
        for (FilePath file : filePaths) {
            try {
                File txt = file.txt;
                File hkx = file.hkx;
                String command = String.format(UPDATE_COMMAND_TEMPLATE, txt.getPath(), hkx.getPath());

                if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                    System.out.println(hkx.getName() + "更新成功！");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
