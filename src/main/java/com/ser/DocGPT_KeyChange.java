package com.ser;

import com.ser.blueline.IDocument;
import com.ser.blueline.IDocumentPart;
import com.ser.blueline.IFDE;
import com.ser.blueline.IFileFDE;
import com.ser.evITAWeb.EvitaWebException;
import com.ser.evITAWeb.api.controls.IControl;
import com.ser.evITAWeb.api.controls.IControlContainer;
import com.ser.evITAWeb.scripting.keychange.KeyChangeScripting;
import utils.OCRService;
import utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DocGPT_KeyChange extends KeyChangeScripting {
    public DocGPT_KeyChange() {
        super();
    }

    @Override
    public void onInit() throws EvitaWebException {
        IDocument document = getDocument();
        String uuid = java.util.UUID.randomUUID().toString();
        String mdir = "C:/tmp2/doc-gpt/blobs";
        String slnk = "";

        try {
            OCRService.url = "http://localhost:20244";
            OCRService.log = log;
            Utils.loadDirectory(mdir);
            if(document != null) {
                IDocumentPart partDocument = document.getPartDocument(document.getDefaultRepresentation() , 0);
                IFDE fde = partDocument.getFDE();
                String fext = ((IFileFDE) fde).getShortFormatDescription().toLowerCase();
                slnk = OCRService.run(Utils.exportDocument(document, mdir, uuid), fext);
            }
        } catch (Exception e) {
            log.error(" Exception :: " + e.toString());
            throw new RuntimeException(e);
        }

        IControl editor = getDialog().getFieldByName("docgpt_DocGPT");
        if (!slnk.isEmpty() && editor instanceof IControlContainer) {
            String strURL = getDoxisServer().getRequestURL()
                    + "/xtra/controls/doc-gpt/main.jsp?"
                    + "&uuid=" + uuid
                    + "&slnk=" + slnk
                    + "&width=" + editor.getWidth()
                    + "&height=" + editor.getHeight() + "&";
            ((IControlContainer) editor).setURL(strURL, false);
        }
    }
}
