package org.hkxconvert.manager;

import org.hkxconvert.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class SimpleRenamer extends SimpleUpdater{
    public SimpleRenamer(File root, List<FilePath> filePaths) {
        super(root, filePaths);
        _arrFile = root.listFiles();
        _folderPathStr = root.toString();
    }

    /**rename files according to input */
    public void rename(){
        Scanner input = new Scanner(System.in);
        System.out.println("choose your renaming scheme");
        System.out.println("2hw to make everything 2hw, 2hm to make everything 2hm, 1hm to make everything 1hm");
        String option = null;
        while (true) {
            option = input.next().toLowerCase(Locale.ROOT);
            if (option.equals("2hw") || option.equals("2hm") || option.equals("1hm")) {
                break;
            } else {
                System.out.println("wrong input");
            }
        }
        renameProcesser(option);
    }


    private void renameProcesser(String type) {
        for (int i = 0; i < _arrFile.length; i++) {
            if (_arrFile[i].isFile()) {
                String fileName = _arrFile[i].getName();
                String fileNameLc = fileName.toLowerCase(Locale.ROOT);
                String newFileName = null;
                File thisFile = new File(_folderPathStr + "\\" + fileName);
                if (fileNameLc.contains("skysa")) {
                    //start Skysa fixing Scheme
                } else if ((fileNameLc.contains("1hm") || fileNameLc.contains("2hm") || fileNameLc.contains("2hw")) && fileNameLc.contains("hkx") ) {
                    newFileName = type + fileName.substring(3);
                }
                thisFile.renameTo(new File(_folderPathStr + "\\" + newFileName));
                System.out.println(fileName + " renamed to " + newFileName);
            }
        }
    }


    @Override
    public void fixAnno() throws IOException {

    }
    File[] _arrFile;

    String _folderPathStr;
}
