package org.hkxconvert;

import org.hkxconvert.manager.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        File myDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        File hkxRoot = new File(myDir + "/animations");
        while (true){
            Scanner input = new Scanner(System.in);
            if (!hkxRoot.exists()) {
                System.out.println("animations folder not found, creating new folder");
                if (!hkxRoot.mkdir()) {
                    System.out.println("failed to create folder, restart the program as Admin");
                }
            }
            List<FilePath> filePaths = new ArrayList<>();
            if (!dirProcess(myDir)) {
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                return;
            }
            System.out.println("input your operation, input help to show all available commands");
            FixManager fixManager;
            while (true) {
                String option = input.next().toLowerCase(Locale.ROOT);
                if (option.equals("l")) {
                    fixManager = new EnemyBrutalizer(hkxRoot, filePaths);
                    break;
                } else if (option.equals("h")) {
                    fixManager = new EnemyBrutalizerHvy(hkxRoot, filePaths);
                    break;
                } else if (option.equals("c")) {
                    fixManager = new HvyAttackLooper(hkxRoot, filePaths);
                    break;
                } else if (option.equals("d")) {
                    fixManager = new SimpleDumper(hkxRoot, filePaths);
                    break;
                } else if (option.equals("u")) {
                    fixManager = new SimpleUpdater(hkxRoot, filePaths);
                    break;
                } else if (option.equals("rm")) {
                    fixManager = new SimpleRemover(hkxRoot, filePaths);
                    break;
                } else if (option.equals("rp")) {
                    fixManager = new SimpleReplacer(hkxRoot, filePaths);
                    break;
                }
                else if (option.equals("help")) {
                    System.out.println("L for light attack combo chaining");
                    System.out.println("H for heavy attack combo chaining");
                    System.out.println("C for heavy attack looper(incomplete)");
                    System.out.println("RM to remove annotations by your choice");
                    System.out.println("RP to replace annotations by your choice");
                    System.out.println("D to dump all annotations for manual edit");
                    System.out.println("U to update all animations with manually edited .txt dump");
                }
            }
            fixManager.dumpAnno();
            fixManager.fixAnno();
            fixManager.updateAnno();
            System.out.println("\nAll operations complete, input command to start another operation");
        }
    }

    /**pre processes the current directory */
    private static boolean dirProcess(File dir) {
        String hka64Str = dir.getPath() + "/hkanno64.exe";
        File hkanno64 = new File(hka64Str);
        hka64Str = hkanno64.toString();
        if (!hkanno64.exists()) {
            System.out.println("Error: Hkanno64 not found in current directory, please make sure you have hkanno64.exe it in " + dir);
            return false;
        }
        DUMP_COMMAND = "cmd /c " + hka64Str + " dump -o \"%s\" \"%s\"";
        UPDATE_COMMAND = "cmd /c " + hka64Str + " update -i \"%s\" \"%s\"";
        return true;
    }

    public static String DUMP_COMMAND;

    public static String UPDATE_COMMAND;

}
