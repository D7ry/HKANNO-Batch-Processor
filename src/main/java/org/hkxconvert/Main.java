package org.hkxconvert;

import org.hkxconvert.manager.EnemyBrutalizer;
import org.hkxconvert.manager.FixManager;
import org.hkxconvert.manager.HvyComboInitiater;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static org.hkxconvert.Const.PATH;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose your annotator");
        System.out.println("L for light attack connector");
        System.out.println("H for heavy attack connector");
        File root = new File(PATH);
        List<FilePath> filePaths = new ArrayList<>();
        FixManager fixManager;
        while (true) {
            String option = input.next().toLowerCase(Locale.ROOT);
            if (option.equals("l")) {
                System.out.println("annotating light combos");
                fixManager = new EnemyBrutalizer(root, filePaths);
                break;
            } else if (option.equals("h")) {
                System.out.println("annotating light to heavy combos");
                fixManager = new HvyComboInitiater(root, filePaths);
                break;
            }
        }
        fixManager.dumpAnno();
        fixManager.fixAnno();
        fixManager.updateAnno();
        System.out.println("All fixes complete.");
    }

}
