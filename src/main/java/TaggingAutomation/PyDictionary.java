package TaggingAutomation;

import apiClient.ApacheHttpRestClient;
import apiClient.JSONParser;
import apiClient.ServiceHandler;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bruntha on 9/7/15.
 */
public class PyDictionary {
    private static ServiceHandler serviceHandler;
    private static org.apache.http.HttpResponse httpResponse;
    private static boolean isSuccessWebService;
    private static HttpEntity httpEntity;
    private static InputStream is;
    private static JSONParser parsing;
    private static JSONObject json;

    public static ArrayList<String> findSynonyms(String word) {
        ArrayList<String> synonyms=new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(word);
        if (!matcher.find() || word.contains("\"") || word.contains("-")|| word.contains("\\")) {
            return synonyms;
        }else {
            String response= ApacheHttpRestClient.getHTTPGetResponse("http://pydictionary-geekpradd.rhcloud.com/api/synonym/" + word);
            response=response.replace("[","");
            response=response.replace("]","");
            response=response.replace("\"","");

            String[] d=response.split(",");
            for (int i = 0; i < d.length; i++) {
                synonyms.add(d[i].trim());
            }
            return synonyms;
        }
    }

    public static ArrayList<String> findMeaning(String word) {
        ArrayList<String> meaning=new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(word);
        if (!matcher.find() || word.contains("\"") || word.contains("-")|| word.contains("\\")) {
            return meaning;
        }else {
            serviceHandler = new ServiceHandler();
            httpResponse = serviceHandler.makeServiceCallJson("http://pydictionary-geekpradd.rhcloud.com/api/meaning/" + word , 1, null);

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

                    if (json.has("Adjective")){
                        JSONArray meaningJson=json.getJSONArray("Adjective");

                        for (int i = 0; i < meaningJson.length() ; i++) {
                            meaning.add(meaningJson.getString(i));
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return meaning;
        }
    }

    public static void main(String[] args) {
        Utility.printArrayList(PyDictionary.findMeaning("staff"));
    }
}
