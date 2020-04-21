package nitish.vidflix1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Loader extends AppCompatActivity {
    Intent mainIntent;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        DataHandlers.deviceID=android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        DataHandlers.packageName=getPackageName();

        if (!isNetworkAvailable()){
            new AlertDialog.Builder(Loader.this,R.style.MyAlertDialogStyle)
                    .setTitle("Not Connected to internet?")
                    .setMessage("This app requires active internet connetion. Please check your internet connection and open again.")
                    .setPositiveButton("Ok",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();

        }else {

        mainIntent = new Intent(Loader.this,MainActivity.class);
        try {
            JSONObject updateStuff = DataHandlers.checkUpdate(Long.toString(getVersionCode()),DataHandlers.secret_key);
            Log.i("BROOOOO",updateStuff.toString());
            String error=updateStuff.getString("error");
            mainIntent.putExtra("ERROR",error);
            if (error.equals("true")){
                mainIntent.putExtra("MSG",updateStuff.getString("msg"));
                mainIntent.putExtra("CHANGE_LOG",updateStuff.getString("change_log"));
                mainIntent.putExtra("URL",updateStuff.getString("url"));
            }
        } catch (Exception e) {
            Log.i("BROOOOO","ERR");
            mainIntent.putExtra("ERROR","false");
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                Loader.this.startActivity(mainIntent);
                Loader.this.finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, 100);
        }

    }
    long getVersionCode() throws PackageManager.NameNotFoundException {
        long versionCode=2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            versionCode=versionCode=getApplicationContext().getPackageManager().getPackageInfo(getPackageName(),0).getLongVersionCode();
        else
            versionCode=getApplicationContext().getPackageManager().getPackageArchiveInfo(getPackageName(),0).versionCode;
        return versionCode;
    }
}
