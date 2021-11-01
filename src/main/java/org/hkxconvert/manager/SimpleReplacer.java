package org.hkxconvert.manager;

import org.hkxconvert.FilePath;
import org.hkxconvert.ListExecutor.annoInserterRemover;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Scanner;

import static org.hkxconvert.Const.SKYSA_COMBO_ANNO;

public class SimpleReplacer extends FixManager {
    public SimpleReplacer(File root, List<FilePath> filePaths) {
        super(root, filePaths);
        System.out.println("starting replacer");
    }



    @Override
    public void fixAnno() throws IOException {
        Scanner input = new Scanner(System.in);
        ListIterator<FilePath> li = getFilePaths().listIterator();
        System.out.println("input anno keyword for the anno to be replaced");
        String oldAnno = input.next();
        System.out.println("intput replacer anno");
        String newAnno = input.next();
        while (li.hasNext()) {
            File txt = li.next().txt;
            annoInserterRemover fixer = new annoInserterRemover(txt, oldAnno, SKYSA_COMBO_ANNO);
            if (fixer.replace(newAnno)) {
                System.out.println("successfully fixed:" + txt.getName());
            } else {
                System.out.println("failed to fix:" + txt.getName());
            }
        }
        System.out.println("Sucessfully replaced annos; keep replacing? [Y/N]");
        String command = input.next().toLowerCase(Locale.ROOT);
        if (command.equals("y")) {
            fixAnno();
        } else if (command.equals("n")) {
            return;
        }
    }


}
