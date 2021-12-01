package org.hkxconvert;

import org.hkxconvert.manager.*;
import sun.java2d.pipe.SpanShapeRenderer;

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
        System.out.println("current working directory: " + myDir.getPath());
        HKX_ROOT = new File(myDir + "/animations");
        if (!HKX_ROOT.exists()) {
            System.out.println("animations folder not found, creating new folder");
            if (!HKX_ROOT.mkdir()) {
                System.out.println("failed to create folder, restart the program as Admin");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                return;
            }
        }
        while (true){
            _filePaths = new ArrayList<>();
            if (!dirProcess(myDir)) {
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                return;
            }
            System.out.println("input your operation, input help to show all available commands");
            inputProcess();
            System.out.println("\nAll operations complete, input command to start another operation");
        }
    }

    /**pre processes the current directory */
    private static boolean dirProcess(File dir) {
        String currPath = dir.getPath();
        if (currPath.contains(" ") || currPath.contains(".")) {
            System.out.println("Error: current file directory cannot contain black space or '.'; move the directory elsewhere or rename the directory" +
                    ", and try again. ");
            return false;
        }
        String hka64Str = currPath + "/hkanno64.exe";
        File hkanno64 = new File(hka64Str);
        hka64Str = hkanno64.toString();
        if (!hkanno64.exists()) {
            System.out.println("Error: Hkanno64 not found in current directory, please make sure you have hkanno64.exe it in " + currPath);
            return false;
        }
        DUMP_COMMAND = "cmd /c " + hka64Str + " dump -o \"%s\" \"%s\"";
        UPDATE_COMMAND = "cmd /c " + hka64Str + " update -i \"%s\" \"%s\"";
        return true;
    }

    /** process input and returns corresponding fixmanager*/
    private static void inputProcess() throws IOException {
        Scanner input = new Scanner(System.in);
            String option = input.next().toLowerCase(Locale.ROOT);
            if (option.equals("l")) {
                FixManager fixer =  new EnemyBrutalizer(HKX_ROOT, _filePaths);
                simpleFix(fixer);
            } else if (option.equals("h")) {
                FixManager fixer = new EnemyBrutalizerHvy(HKX_ROOT, _filePaths);
                simpleFix(fixer);
            } else if (option.equals("c")) {
                FixManager fixer = new HvyAttackLooper(HKX_ROOT, _filePaths);
            } else if (option.equals("d")) {
                FixManager fixer = new SimpleDumper(HKX_ROOT, _filePaths);
                fixer.dumpAnno();
            } else if (option.equals("u")) {
                FixManager fixer = new SimpleUpdater(HKX_ROOT, _filePaths); //still need to call simple fix because file indexing is handled by dumpAnno()
                simpleFix(fixer);
            } else if (option.equals("rm")) {
                FixManager fixer = new SimpleRemover(HKX_ROOT, _filePaths);
                simpleFix(fixer);
            } else if (option.equals("rp")) {
                FixManager fixer = new SimpleReplacer(HKX_ROOT, _filePaths);
                simpleFix(fixer);
            } else if (option.equals("rename")) {
                SimpleRenamer renamer = new SimpleRenamer(HKX_ROOT, _filePaths);
                simpleRename(renamer);
            }
            else if (option.equals("cn")) {
                SimpleReplacer fixer = new SimpleReplacer(HKX_ROOT, _filePaths);
                String[] og = {"SkySA_TriggerIntervalWinLoop", "SkySA_AttackLoop"};
                String[] rp = {"SkySA_TriggerIntervalWin", "SkySA_AttackWinStart"};
                fixer.dumpAnno();
                fixer.batchFixAnno(og, rp);
                fixer.updateAnno();
            } else if (option.equals("cl")) {
                SimpleReplacer fixer = new SimpleReplacer(HKX_ROOT, _filePaths);
                String[] rp = {"SkySA_TriggerIntervalWinLoop", "SkySA_AttackLoop"};
                String[] og = {"SkySA_TriggerIntervalWin", "SkySA_AttackWinStart"};
                fixer.dumpAnno();
                fixer.batchFixAnno(og, rp);
                fixer.updateAnno();
            } else if (option.equals("repetition")) {
                FixManager fixer = new newRigFixer(HKX_ROOT, _filePaths);
                simpleFix(fixer);
            }
            else if (option.equals("help")) {
                System.out.println("L for light attack combo chaining");
                System.out.println("H for heavy attack combo chaining");
                System.out.println("C for heavy attack looper(incomplete)");
                System.out.println("RM to remove annotations by your choice");
                System.out.println("RP to replace annotations by your choice");
                System.out.println("D to dump all annotations for manual edit");
                System.out.println("U to update all animations with manually edited .txt dump");
                System.out.println("cn to convert last normal attack to regular normal attack");
                System.out.println("cl to convert normal attack to the last attack with loop annotation");
                System.out.println("rename to initilize batch renamer");
                System.out.println("repetition to remove repetitive AMR annos");
            }
    }


    /** simply performs dump, fix, and update action for FIXER */
    private static void simpleFix(FixManager fixer) throws IOException {
        fixer.dumpAnno();
        fixer.fixAnno();
        fixer.updateAnno();
    }

    private static void simpleRename(SimpleRenamer renamer) {
        renamer.rename();
    }


    public static List<FilePath> _filePaths;

    public static File HKX_ROOT;

    public static String DUMP_COMMAND;

    public static String UPDATE_COMMAND;

}
