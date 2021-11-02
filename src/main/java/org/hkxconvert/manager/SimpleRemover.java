package org.hkxconvert.manager;

import org.hkxconvert.FilePath;
import org.hkxconvert.ListExecutor.annoInserterRemover;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Scanner;

import static org.hkxconvert.Const.COMBO_ANNO;

public class SimpleRemover extends FixManager {
    public SimpleRemover(File root, List<FilePath> filePaths) {
        super(root, filePaths);
        System.out.println("start removing anno");
    }


    @Override
    public void fixAnno() throws IOException {
        System.out.println("\ninput anno keyword to be removed:");
        Scanner input = new Scanner(System.in);
        ListIterator<FilePath> li = getFilePaths().listIterator();
        if (input.hasNext()) {
            String kwd = input.next();
            while (li.hasNext()) {
                File txt = li.next().txt;
                annoInserterRemover fixer = new annoInserterRemover(txt, kwd, COMBO_ANNO);
                if (fixer.remove()) {
                    System.out.println("successfully fixed:" + txt.getName());
                } else {
                    System.out.println("failed to fix:" + txt.getName());
                }
            }
        }
        System.out.println("Sucessfully removed annos; keep removing? [Y/N]");
        String command = input.next().toLowerCase(Locale.ROOT);
        if (command.equals("y")) {
            fixAnno();
        } else if (command.equals("n")) {
            return;
        }
    }

    public void BatchFixAnno(String[] args) throws FileNotFoundException {
        ListIterator<FilePath> li = getFilePaths().listIterator();
        for (String kwd : args) {
            while (li.hasNext()) {
                File txt = li.next().txt;
                annoInserterRemover fixer = new annoInserterRemover(txt, kwd, SKYSA_COMBO_ANNO);
                if (fixer.remove()) {
                    System.out.println("successfully fixed:" + txt.getName());
                } else {
                    System.out.println("failed to fix:" + txt.getName());
                }
            }
        }
        System.out.println("Sucessfully removed annos.");
    }

}
