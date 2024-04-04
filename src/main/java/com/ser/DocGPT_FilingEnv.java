/*
 * Created on Feb 23, 2005
 */
package com.ser;

import com.ser.evITAWeb.EvitaWebException;
import com.ser.evITAWeb.scripting.filing.FilingEnvironmentScripting;

/**
 * Sample class that demonstrates scripting the metadata dialog of a document class from within the filing environment. <BR>
 * <A href=FilingEnvironmentScriptingExampleA.java.html target=_blank>Display Java source code in separate window.</A>
 */
public class DocGPT_FilingEnv extends FilingEnvironmentScripting {
    /** Sample implementation for onInit method */
    @Override
    public void onInit() throws EvitaWebException {
        log.info("onInit DocGPT_FilingEnv");
        this.addArchiveScripting("*", DocGPT_Archive.class.getName());
    }

}
