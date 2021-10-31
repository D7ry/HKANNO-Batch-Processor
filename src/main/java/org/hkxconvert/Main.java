package org.hkxconvert;

import org.hkxconvert.manager.EnemyBrutalizer;
import org.hkxconvert.manager.FixManager;
import org.hkxconvert.manager.HvyAttackLooper;
import org.hkxconvert.manager.HvyComboInitiater;

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
        System.out.println("Choose your annotation");
        System.out.println("L for light attack combo");
        System.out.println("H for heavy attack combo");
        System.out.println("C for heavy attack looper");
        File root = new File(HKXPATH);
        List<FilePath> filePaths = new ArrayList<>();
        FixManager fixManager;
        while (true) {
            String option = input.next().toLowerCase(Locale.ROOT);
            if (option.equals("l")) {
                System.out.println("start annotating light combos");
                fixManager = new EnemyBrutalizer(root, filePaths);
                break;
            } else if (option.equals("h")) {
                System.out.println("start annotating light to heavy combos");
                fixManager = new HvyComboInitiater(root, filePaths);
                break;
            } else if (option.equals("c")) {
                System.out.println("start annotating heavy attack loops");
                fixManager = new HvyAttackLooper(root, filePaths);
            }
        }
        fixManager.dumpAnno();
        fixManager.fixAnno();
        fixManager.updateAnno();
        System.out.println("All fixes complete.");
    }

}
