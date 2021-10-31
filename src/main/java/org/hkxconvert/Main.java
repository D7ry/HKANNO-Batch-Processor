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
        while (true){
            Scanner input = new Scanner(System.in);
            System.out.println("input your operation, input help for help");
            File root = new File(HKXPATH);
            List<FilePath> filePaths = new ArrayList<>();
            FixManager fixManager;
            while (true) {
                String option = input.next().toLowerCase(Locale.ROOT);
                if (option.equals("l")) {
                    fixManager = new EnemyBrutalizer(root, filePaths);
                    break;
                } else if (option.equals("h")) {
                    fixManager = new EnemyBrutalizerHvy(root, filePaths);
                    break;
                } else if (option.equals("c")) {
                    fixManager = new HvyAttackLooper(root, filePaths);
                    break;
                } else if (option.equals("d")) {
                    fixManager = new SimpleDumper(root, filePaths);
                    break;
                } else if (option.equals("u")) {
                    fixManager = new SimpleUpdater(root, filePaths);
                    break;
                } else if (option.equals("rm")) {
                    fixManager = new SimpleRemover(root, filePaths);
                    break;
                } else if (option.equals("rp")) {
                    fixManager = new SimpleReplacer(root, filePaths);
                    break;
                }
                else if (option.equals("help")) {
                    System.out.println("L for light attack combo chaining");
                    System.out.println("H for heavy attack combo chaining");
                    System.out.println("C for heavy attack looper(incomplete)");
                    System.out.println("RM to remove all annotations with keyword");
                    System.out.println("RP to replace all anotations");
                    System.out.println("D to dump all annotations");
                    System.out.println("U to update all annotations with corresponding names");
                }
            }
            fixManager.dumpAnno();
            fixManager.fixAnno();
            fixManager.updateAnno();
            System.out.println("\nAll operations complete, input command to start another operation");
        }
    }

}
