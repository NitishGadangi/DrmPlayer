package nitish.vidflix1;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Switch;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DataHandlers {
    static String deviceID="not_found";
    static String packageName="not_found";

    static long VersionCode = 1;
    static String secret_key="@77fuckyoudumbass:?8×€£";

    static String COUNT_PARAM="?count=12";

    static String DOT=" • ";

     static String api_key="vidflix9900okssnzh574zf9==";

     static String base_url="https://api.vidflix.net";

     static String moviesServer1="https://api.vidflix.net/app/api/get_movie1.php",
                    moviesServer2="https://api.vidflix.net/app/api/get_movie2.php",
                    moviesServer3="https://api.vidflix.net/app/api/get_server3.php",
                    moviesServer4="https://api.vidflix.net/app/api/get_server4.php",
                    moviesServer5="https://api.vidflix.net/app/api/get_movie3.php",
                    moviesServer6="https://api.vidflix.net/app/api/get_movie4.php",
                    series_server1="https://api.vidflix.net/app/api/get_series1.php",
                    series_server2="https://api.vidflix.net/app/api/get_series2.php",
                    series_server3="https://api.vidflix.net/app/api/get_series3.php",
                    series_server4="https://api.vidflix.net/app/api/get_series4.php";

     static String cat_bollywood_api="https://api.vidflix.net/app/api/categories/api/bollywood.php",
                    cat_punjabi_api="https://api.vidflix.net/app/api/categories/api/punjabi.php",
                    cat_dubbed_api="https://api.vidflix.net/app/api/categories/api/dubbed.php",
                    cat_kids_api="https://api.vidflix.net/app/api/categories/api/kids.php",
                    cat_hollywood_api="https://api.vidflix.net/app/api/categories/api/hollywood.php",
                    cat_marvel_api="https://api.vidflix.net/app/api/categories/api/marvel.php",
                    cat_anime_api="https://api.vidflix.net/app/api/categories/api/anime.php";

     static String liveTV_serv1="https://api.vidflix.net/app/api/livetv/api/",
                    liveTV_serv2="http://livetv.vidflix.net/livetv/api/",
                    liveTV_2img_baseurl="http://livetv.vidflix.net/livetv/images/",
//                    liveTV_search="http://livetv.vidflix.net/livetv/api/search/?search=";
                    liveTV_search="https://api.vidflix.net/app/api/livetv/api/search/search.php?search=";

     static  String allmovies2="https://api.vidflix.net/app/api/allmovies2.php",
                    allmovies3="https://api.vidflix.net/app/api/server3.php",
                    allmovies4="https://api.vidflix.net/app/api/server4.php",
                    allmovies5="https://api.vidflix.net/app/api/allmovies3.php",
                    allmovies6="https://api.vidflix.net/app/api/allmovies4.php";

    static  String allseries2="https://api.vidflix.net/app/api/allseries2.php",
                    allseries3="https://api.vidflix.net/app/api/allseries3.php",
                    allseries4="https://api.vidflix.net/app/api/allseries4.php";

    static String viu1="http://api.vidflix.net/app/api/premium/viu/",
                    viu_org="http://api.vidflix.net/app/api/premium/viu/original_content.php";

    static String major_home_api="http://api.vidflix.net/app/api/home/create.php";



    static String getContent(String finUrl){
        String finString="FAILED";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
        HttpGet httpget = new HttpGet(finUrl);
        httpget.setHeader("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");// Set the action you want to do

        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        httpget.setHeader("x-signature",getSignature(deviceID,timeStamp,secret_key,packageName));
        httpget.setHeader("x-timestamp-current",timeStamp);
        httpget.setHeader("x-deviceId",deviceID);


        try {
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line + "\n");

            finString = sb.toString(); // Result is here


            is.close(); // Close the stream
        }catch (Exception e){
            Log.i("BAAABY","a "+e);
            e.printStackTrace();
        }
        return finString;
    }

    static String getSignature(String devID,String time,String secretapikey,String packageName){
        String signature=new String(Hex.encodeHex(DigestUtils.md5(devID+time+secretapikey+packageName)));
        return signature;
    }

    static String getBollyMoviesHome(String deviceId,int count){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));


        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/categories/api/bollywood.php?count="+count;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getHollyMoviesHome(String deviceId,int count){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/categories/api/hollywood.php?count="+count;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getDubbedMoviesHome(String deviceId,int count){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/categories/api/dubbed.php?count="+count;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getPunjabiMoviesHome(String deviceId,int count){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/categories/api/punjabi.php?count="+count;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getAnimeMoviesHome(String deviceId,int count){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/categories/api/anime.php?count="+count;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getKidsMoviesHome(String deviceId,int count){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/categories/api/kids.php?count="+count;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getMarvelMoviesHome(String deviceId,int count){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/categories/api/marvel.php?count="+count;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getTvSeriesHome(String deviceId,int page){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/allseries1.php?page="+page;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getAllMovies(String deviceId,int page){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/allmovies1.php?page="+page;

        String res_json = getContent(api_link);


        return res_json;
    }

    static String getCategories(String deviceId){
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        String api_link = "https://vidflix.net/api/vidflix/v1/get_movies.php?api_key="+api_key+"&timestamp="+timeStamp+"&device_id="+deviceId;

        //staging
        api_link = "https://api.vidflix.net/app/api/allcat/";

        String res_json = getContent(api_link);


        return res_json;
    }
    static JSONObject checkUpdate(String build,String key) throws JSONException {
        String res=getContent("https://api.vidflix.net/app/api/chk_update/?build="+build+"&secret_key="+key);
        Log.i("BROOOOO1",res);
        return new JSONObject(res);
    }

    public static String capitalize(String str)
    {
        if(str == null) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    static String getSearch(String token){
        String apiKey="https://api.vidflix.net/app/api/search/?term=";
        return getContent(apiKey+token);
    }

    static String getServerUrl(String server,String video_id){
        if (server.equals("movies_server1"))
            return moviesServer1+"?video_id="+video_id;
        else if (server.equals("movies_server2"))
            return moviesServer2+"?video_id="+video_id;
        else if (server.equals("series_server1"))
            return series_server1+"?video_id="+video_id;
        else if (server.equals("series_server2"))
            return series_server2+"?video_id="+video_id;
        else
            return "FAILED";
    }

    static String decryptData(String enc_string){
        byte[] dec= Base64.decode(enc_string,0);
        return new String(dec);
    }

    static String getLiveTVUrl(String str,String quality){
        String base="http://livetv.vidflix.net/livetv/play.m3u8";
        return base+"?channel="+str+"&quality="+quality;
    }

     static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.i("STRM_TYPE","de-mime  "+extension);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    static String getExtention(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.i("STRM_TYPE","de-mime  "+extension);
        return extension;
    }

    static String joinJSON(String json1,String json2){
        Log.i("JSON_JOIN_1",json1);
        Log.i("JSON_JOIN_2",json1);
        String res=json1.substring(0,json1.lastIndexOf("]"))+","+json2.substring(1);
        res=res.replace("[,{","[{");
        return res;
    }

    static String completeDecrypt(String encTxt,String time){
        time=Integer.toString(Integer.parseInt(time)+1200);
//        String iv = DigestUtils.sha1Hex("vidflixislub"+time);
        String iv = new String(Hex.encodeHex(DigestUtils.sha1("vidflixislub"+time)));
        iv=iv.substring(0,16);
//        String key= DigestUtils.md5Hex("vidflixislub"+time);
        String key = new String(Hex.encodeHex(DigestUtils.md5("vidflixislub"+time)));
        return decrypt3(encTxt,key,iv);
    }


    static String decrypt3(String encrypted,String key,String initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted,0));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    static String decryptMain(String encData){
        return decrypt3(encData,"youareafuckingassholefucckkyoouu","HGJAO899#@OO00!&");
    }

    static String updateContent(String video_id,String server){
        String res=getContent("http://103.194.171.183:81/app/api/update/?video_id="+video_id+"&server="+server);
        return res;

    }

}
