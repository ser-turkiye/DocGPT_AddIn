package utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;


public class OCRService {
    public static String url = "";
    public static Logger log;

    public static String run(String path, String extn) throws Exception{
        log.warn("OCRService-path :: " + path);
        log.warn("OCRService-extn :: " + extn);
        JSONObject fncs = functions();
        for(String func : fncs.keySet()){
            log.warn("OCRService-function :: " + func);
            JSONObject options = options(func);
            log.warn("OCRService-options :: " + func + "/" + options.toString());

            if(options.has("uploadDocument") && options.getBoolean("uploadDocument") == true){
                if(options.has("uploadDocument.extensions")) {
                    JSONArray exts = options.getJSONArray("uploadDocument.extensions");
                    log.warn("OCRService-exts :: " + func + "/" + exts.toString());
                    if(!hasJSONArrayValue(exts, extn)){continue;}
                }

            }

            JSONObject fint = init(func);
            log.warn("OCRService-init :: " + func + "/" + fint.toString());
            if(!fint.has("id")){continue;}
            String id = fint.getString("id");

            if(options.has("uploadDocument") && options.getBoolean("uploadDocument") == true){
                JSONObject upload = uploadDocument(func, id, path);
                log.warn("OCRService-upload :: " + func + "/" + upload.toString());
                if (!upload.has("status") || !upload.getString("status").equals("okey")){continue;}
            }

            JSONObject fcll = call(func, id, path);
            log.warn("OCRService-call :: " + func + "/" + fcll.toString());
            if (!fcll.has("okey") || !fcll.getBoolean("okey")){continue;}

            int lcur = 0, lmax = 240;
            while(true){
                if(lcur > lmax){break;}
                lcur++;
                JSONObject fchk = check(func, id, path);
                log.warn("OCRService-check :: " + func + "/" + fchk.toString());
                if (fchk.has("status") && fchk.getString("status").equals("completed")){
                    if(options.has("downloadContent") && options.getBoolean("downloadContent") == true){
                        return "/" + func + "/" + id + "/downloadContent";
                    }
                    return "okey";
                }
                if (fchk.has("status") && fchk.getString("status").equals("error")){break;}
                TimeUnit.MILLISECONDS.sleep(250);
            }
        }
        return "";
    }
    public static boolean hasJSONArrayValue(JSONArray json,String value) {
        for(int i = 0; i < json.length(); i++) {
            if(json.getString(i).equals(value)){return true;}
        }
        return false;
    }
    public static JSONObject functions() throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(OCRService.url + "/functions");
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        JSONObject temp = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
        return temp.has("functions") ? temp.getJSONObject("functions") : new JSONObject();
    }
    public static JSONObject options(String _func) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(OCRService.url + "/" + _func + "/options");
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        JSONObject temp = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
        return temp.has("options") ? temp.getJSONObject("options") : new JSONObject();
    }
    public static JSONObject init(String _func) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(OCRService.url + "/" + _func + "/init");
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        JSONObject rtrn = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
        return rtrn;
    }
    public static JSONObject uploadDocument(String _func, String _id, String _path) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(OCRService.url + "/" + _func + "/" + _id + "/uploadDocument");
        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        final File file = new File(_path);
        builder.addBinaryBody("main", file);
        builder.addTextBody("type", "file");
        final HttpEntity reqEntity = builder.build();
        request.setEntity(reqEntity);

        HttpResponse response = client.execute(request);
        HttpEntity resEntity = response.getEntity();
        JSONObject rtrn = new JSONObject(EntityUtils.toString(resEntity, "UTF-8"));
        return rtrn;
    }
    public static JSONObject call(String _func, String _id, String _path) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(OCRService.url + "/" + _func + "/" + _id + "/call");
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        JSONObject rtrn = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
        return rtrn;
    }
    public static JSONObject check(String _func, String _id, String _path) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(OCRService.url + "/" + _func + "/" + _id + "/check");
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        JSONObject rtrn = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
        return rtrn;
    }
}
