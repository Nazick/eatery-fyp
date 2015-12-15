package com.eatery.engine.preprocessing;

import apiClient.JSONParser;
import apiClient.ServiceHandler;
import domain.Lang;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bruntha on 9/15/15.
 */
public class DetectLanguageCom {        // api from deteclanguage.com
    private ServiceHandler serviceHandler;
    private org.apache.http.HttpResponse httpResponse;
    private boolean isSuccessWebService;
    private HttpEntity httpEntity;
    private InputStream is;
    private JSONParser parsing;
    private JSONObject json;

    public static void main(String args[]) {
        DetectLanguageCom detectLanguageCom=new DetectLanguageCom();
        detectLanguageCom.getLanguage("boy");
    }

    public Lang getLanguage(String word) {
        Lang lang=null;

        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(word);
        if (!matcher.find()) {
            lang=new Lang("",false,0.0);
        } else {
            serviceHandler = new ServiceHandler();
            httpResponse = serviceHandler.makeServiceCallJson("http://ws.detectlanguage.com/0.2/detect?q="+word+"&key=73bc07b472b0ab36b055f0a56d3eb4c9", 1, null);

            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                isSuccessWebService=true;
            }else if(httpResponse.getStatusLine().getStatusCode()==403){
                isSuccessWebService=false;
            }else if(httpResponse.getStatusLine().getStatusCode()==404){
                isSuccessWebService=false;
            }else {
                isSuccessWebService=false;
            }

            if(isSuccessWebService)
            {
                try {
                    httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                    parsing = new JSONParser();
                    json = parsing.getJSONFromResponse(is);

                    JSONObject data=json.getJSONObject("data");
                    JSONArray detections=data.getJSONArray("detections");
                    if (detections.length() > 0) {
                        JSONObject lan=detections.getJSONObject(0);
                        lang=new Lang(lan.getString("language"),lan.getBoolean("isReliable"),lan.getDouble("confidence"));
                    }else {
                        lang=new Lang("",false,0.0);

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return lang;
    }
}