package com.nitish.vidtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Util;
import com.tonyodev.fetch2.DefaultFetchNotificationManager;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;

import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Dummy extends AppCompatActivity {
    Fetch fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        final EditText editText= findViewById(R.id.editText);
        final EditText editText2= findViewById(R.id.editText2);
        final EditText et_referer= findViewById(R.id.et_referer);
        final EditText et_drmUrl= findViewById(R.id.et_drmUrl);
        EditText et_ua=findViewById(R.id.et_user_agent);
        findViewById(R.id.btn_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_ua=et_ua.getText().toString();
                if (temp_ua.equals("")||temp_ua.equals(null)){
                    temp_ua=Util.getUserAgent(getApplicationContext(), getString(R.string.app_name));
                }
                String ref = et_referer.getText().toString();
                String drm = et_drmUrl.getText().toString();


                if (drm.equals("")||drm.equals(null)){
                    drm="NONE";
                }

                Log.i("HH_UA",temp_ua);
                Log.i("HH_Ref","s"+ref);
                Log.i("HH_drm","s"+drm);

                Intent mIntent = MainActivity.getStartIntent(getApplicationContext(),
                        editText.getText().toString(),editText2.getText().toString(),
                        temp_ua,ref,drm);
                startActivity(mIntent);
            }
        });

        Log.i("STRM_TYPE","UA  "+ Util.getUserAgent(this, getString(R.string.app_name)));



        byte[] out=Base64.decode("W3sibGFiZWwiOiI3MjBwKE1QNCkiLCJmaWxlX3VybCI6Im" +
                "h0dHA6XC9cL2RsLnNlcnZlcmRsLnB3XC8xXC9maWxlc1wvTWF6ZS5SdW5uZXIu" +
                "VGhlLlNjb3JjaC5UcmlhbHMuMjAxNS5CbHVyYXkuNzIwcC5ta3YiLCJzdWJ0aXRsZS" +
                "I6Ilwvc3ViXC9tb3ZpZXNfODI2MDFfMTEzNi52dHQiLCJzdWJfbGFuZyI6IkVuZ2xpc2gifSx7I" +
                "mxhYmVsIjoiMTA4MHAoV0VCTSkiLCJmaWxlX3VybCI6Imh0dHA6XC9cL2RsLnNlcnZlcmRsLnB3XC8xXC9" +
                "maWxlc1wvTWF6ZS5SdW5uZXIuVGhlLlNjb3JjaC5UcmlhbHMuMjAxNS5CbHVyYXkuMTA4MHAubWt2Iiwic3VidGl" +
                "0bGUiOiJcL3N1YlwvbW92aWVzXzc2NjY5XzExMzYudnR0Iiwic3ViX2xhbmciOiJFbmdsaXNoIn1d",0);

        RecyclerView rv_test=findViewById(R.id.rv_test);
        RecAdapter recAdapter = new RecAdapter(5);
        rv_test.setAdapter(recAdapter);
        rv_test.setLayoutManager(new LinearLayoutManager(this));




        findViewById(R.id.down_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadNow(editText.getText().toString(), et_ua.getText().toString());
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("KEYGEN","dd   "+decrypt3("CBvHOVjL49+Mzd0ortIPZU68Zmc1oRqo01ebQS1qYs8=","848af206c02d6b11ee0087e92cb2003e","96f0199098a7b9f0"));
                Log.i("KEYGEN","aa   "+decrypt("Z3nWBM8G1zY3Zkh0NiB8ZaDF1Cdz5QdE2UvbrFeskcI=",1571162533+""));
            }
        });


    }

    public static String decrypt(String encTxt,String time){
        String iv = DigestUtils.sha1Hex("vidflixislub"+time);
        Log.i("KEYGEN","iv  "+iv);
        iv=iv.substring(0,16);
        String key= DigestUtils.md5Hex("vidflixislub"+time);
        Log.i("KEYGEN","key  "+key);
        return decrypt3(encTxt,key,iv);
    }


    public static String decrypt3(String encrypted,String key,String initVector) {
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



    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.i("STRM_TYPE","de-mime  "+extension);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getExtention(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.i("STRM_TYPE","de-mime  "+extension);
        return extension;
    }


    void downloadNow(String url,String file){


        Log.i("STRM_TYPE","mime  "+getMimeType(url));
        Log.i("STRM_TYPE","mime  "+Environment.DIRECTORY_DOWNLOADS);

//        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setTitle("My File");
//        request.setDescription("Downloading");
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file);
//        long ld=downloadmanager.enqueue(request);
//
//        Log.i("STRM_TYPE","ld  "+ld);
//        queryStatus(downloadmanager,ld);


    }

    public void queryStatus(DownloadManager mgr, long lastDownload) {
        Cursor c=mgr.query(new DownloadManager.Query().setFilterById(lastDownload));
        Log.i("STRM_TYPE","c  "+c);

        if (c==null) {
            Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show();
        }
        else {
            c.moveToFirst();

            Log.d(getClass().getName(), "COLUMN_ID: "+
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));
            Log.d(getClass().getName(), "COLUMN_BYTES_DOWNLOADED_SO_FAR: "+
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
            Log.d(getClass().getName(), "COLUMN_LAST_MODIFIED_TIMESTAMP: "+
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
            Log.d(getClass().getName(), "COLUMN_LOCAL_URI: "+
                    c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
            Log.d(getClass().getName(), "COLUMN_STATUS: "+
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
            Log.d(getClass().getName(), "COLUMN_REASON: "+
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));

            Toast.makeText(this, statusMessage(c), Toast.LENGTH_LONG).show();
        }
    }
    private String statusMessage(Cursor c) {
        String msg="???";

        switch(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg="Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg="Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg="Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg="Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg="Download complete!";
                break;

            default:
                msg="Download is nowhere in sight";
                break;
        }
        return(msg);
    }



    class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
        int size;
        String url_base;
        JSONArray jsonArr;
        String jsonHead;
        Boolean isSeries=false;




        RecAdapter(int size){
            this.size=size;

        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return size;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout parentLayout;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                parentLayout = itemView.findViewById(R.id.test);

            }
        }
    }
}
