package com.ser;

import com.ser.blueline.IDocument;
import com.ser.evITAWeb.EvitaWebException;
import com.ser.evITAWeb.api.controls.*;
import com.ser.evITAWeb.scripting.archive.ArchiveScripting;
import utils.Utils;

/**
 * Example class that presets two text boxes while the archive dialog is loaded and automatically indexes the document on submit with the file's name and size. Requires an archive dialog with text box
 * named "filename" and another text box named "filesize". <BR>
 * <A href=ArchiveScriptingExampleA.java.html target=_blank>Display Java source code in separate window.</A>
 */
public class DocGPT_Archive extends ArchiveScripting {
    @Override
    public void onInit() throws EvitaWebException {
        try {

            Utils.session = getSession();
            Utils.server = Utils.session.getDocumentServer();

            IDocument jsonDoc = null;
            /*
            IControl _ObjectDocumentReference = this.getDialog().getFieldByName("ObjectDocumentReference");
            if (_ObjectDocumentReference != null && _ObjectDocumentReference instanceof ITextField) {
                String jsonDocId = Utils.getText(_ObjectDocumentReference);
                if (jsonDocId == null) jsonDocId = "";
                if (!jsonDocId.isEmpty()){
                    jsonDoc = Utils.server.getDocument4ID(jsonDocId, Utils.session);
                }
                if (jsonDoc == null) {
                    jsonDoc = Utils.createDocument(GeneralLib.ClassIDs.JSONDataDocument);
                    if (jsonDoc != null) {
                        String path = System.getProperty("java.io.tmpdir") + "/" + java.util.UUID.randomUUID() + ".json";
                        Utils.saveToFile(path, "");
                        Utils.uploadDefaultRepr(jsonDoc, path);
                        jsonDoc.commit();

                        Utils.setTextfieldValue(_ObjectDocumentReference, jsonDoc.getID());
                    }
                }
            }
            */
            IControl addin = this.getDialog().getFieldByName("DocGPT");
            if (addin instanceof IControlContainer) {
                String strURL = this.getDoxisServer().getRequestURL()
                        + "/xtra/controls/doc-gpt/main.jsp?"
                        + "&docId=" + (jsonDoc == null ? "" : jsonDoc.getID())
                        + "&width=" + addin.getWidth()
                        + "&height=" + addin.getHeight() + "&";
                ((IControlContainer) addin).setURL(strURL, false);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new EvitaWebException(e.getMessage());
        }
    }
}
