package com.jpp.and.thirukkural.pdf;

import android.annotation.TargetApi;
import android.graphics.pdf.PdfDocument;
import android.os.Build;

/**
 * Created by jperiapandi on 23-10-2016.
 */
public class PDFPrinter {

    public void makeCoupletPDF(){

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void makeChapterPDF() {

        PdfDocument pdfDoc = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 300, 1).create();

        //create a page
        PdfDocument.Page page = pdfDoc.startPage(pageInfo);

    }
}
