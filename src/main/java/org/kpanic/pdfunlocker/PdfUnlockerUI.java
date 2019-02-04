package org.kpanic.pdfunlocker;

import javafx.stage.FileChooser;
import li.flor.nativejfilechooser.NativeJFileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PdfUnlockerUI extends JPanel {

    private java.awt.List lstUnlockedFiles;


    public PdfUnlockerUI() {
        DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DropListener(), true);

        lstUnlockedFiles = new java.awt.List();
        lstUnlockedFiles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                File[] files;
                if (e.getClickCount() == 2) {
                    files = openFileDialog();
                    if (files.length == 0)
                        return;
                    List<String> unlockedFiles = PdfUnlockerMain.unlockFiles(Arrays.asList(files));
                    unlockedFiles.forEach((String unlockedFile) -> lstUnlockedFiles.add(unlockedFile, 0));
                }
            }
        });

        this.setLayout(new BorderLayout());
        this.add(lstUnlockedFiles, BorderLayout.CENTER);
    }

    private File[] openFileDialog() {
        JFileChooser fileChooser = new NativeJFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.showDialog(PdfUnlockerUI.this, "Decrypt");
        return fileChooser.getSelectedFiles(); // If failed returns an empty array
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 200);
    }

    private class DropListener implements DropTargetListener {
        @Override
        public void dragEnter(DropTargetDragEvent dtde) {

        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {

        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {

        }

        @Override
        public void dragExit(DropTargetEvent dte) {

        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            Transferable t = dtde.getTransferable();
            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                Thread tr = new Thread(() -> {
                    try {
                        Object td = t.getTransferData(DataFlavor.javaFileListFlavor);
                        if (td instanceof java.util.List) {
                            List<File> pdfs = (List<File>) td;
                            List<String> unlockedFiles = PdfUnlockerMain.unlockFiles(pdfs);

                            for (String unlockedPdf : unlockedFiles) {
                                if (!unlockedPdf.isEmpty())
                                    lstUnlockedFiles.add(unlockedPdf, 0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                tr.run();
            }
        }
    }
}
