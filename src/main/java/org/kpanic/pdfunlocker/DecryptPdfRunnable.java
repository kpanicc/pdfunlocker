package org.kpanic.pdfunlocker;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.function.Function;

public class DecryptPdfRunnable implements Function<File, String>  {
    @Override
    public String apply(File file) {
        String outputFilename = PdfUnlockerMain.generateOutputRoute(file);

        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(file.getAbsolutePath());
            stamper = new PdfStamper(reader, new FileOutputStream(outputFilename));
        } catch (Exception e) {
            outputFilename = null;
            e.printStackTrace();
        }
        finally {
            try {
                if (stamper != null)
                    stamper.close();
                if (reader != null)
                    reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return outputFilename;
    }
}
