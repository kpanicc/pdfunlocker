package org.kpanic.pdfunlocker;

import com.itextpdf.text.pdf.PdfReader;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;

public class PdfUnlockerMain {
    private static final String filenameAppend = "_unlocked";

    public static void main(String[] args) {
        ArgumentParser parser = setUpArgParser();

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        List<String> files = ns.getList("files");

        PdfReader.unethicalreading = true; // :/

        if (files.size() == 0)
            openUI();
        else
            unlockFileNames(files);
    }

    public static void openUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                JFrame frame = new JFrame("PDF Unlock: Drop files to unlock them");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new PdfUnlockerUI());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static List<String> unlockFileNames(List<String> fileNames) {
        List<File> files = new LinkedList<>();
        fileNames.forEach((String fileName) -> files.add(new File(fileName)));

        return unlockFiles(files);
    }

    public static List<String> unlockFiles(List<File> files) {
        List<String> unlockedFiles = new LinkedList<>();

        files.forEach((File file) -> {
            String outputName = new DecryptPdfRunnable().apply(file);
            if (outputName != null)
                unlockedFiles.add(outputName);
        });

        return unlockedFiles;
    }

    public static ArgumentParser setUpArgParser() {
        ArgumentParser argParser = ArgumentParsers.newFor("PDFUnlocker").build()
                .defaultHelp(true)
                .description("Unlock encrypted PDF files");

        argParser.addArgument("files").nargs("*");

        return argParser;
    }

    public static String generateOutputRoute(File inputFile) {
        String basePath = inputFile.getParent();
        String fileName = FilenameUtils.removeExtension(inputFile.getName());
        String extension = FilenameUtils.getExtension(inputFile.getName());
        return FilenameUtils.concat(basePath, fileName + filenameAppend + "." + extension);
    }
}
