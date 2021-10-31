package org.hkxconvert;

import org.hkxconvert.manager.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static org.hkxconvert.Const.HKXPATH;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose your operation, type help for help");
        File root = new File(HKXPATH);
        List<FilePath> filePaths = new ArrayList<>();
        FixManager fixManager;
        while (true) {
            String option = input.next().toLowerCase(Locale.ROOT);
            if (option.equals("l")) {
                fixManager = new EnemyBrutalizer(root, filePaths);
                break;
            } else if (option.equals("h")) {
                fixManager = new HvyComboInitiater(root, filePaths);
                break;
            } else if (option.equals("c")) {
                fixManager = new HvyAttackLooper(root, filePaths);
                break;
            } else if (option.equals("d")) {
                fixManager = new SimpleDumper(root, filePaths);
                fixManager.dumpAnno();
                System.out.println("dump complete");
                System.exit(0);
            } else if (option.equals("help")) {
                System.out.println("L for light attack combo");
                System.out.println("H for heavy attack combo");
                System.out.println("C for heavy attack looper");
                System.out.println("D to dump all annotations");
            }
        }
        fixManager.dumpAnno();
        fixManager.fixAnno();
        fixManager.updateAnno();
        System.out.println("All fixes complete.");
    }

}
