package nitish.vidflix1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity  {
     int pageOpened=0;
    CustomViewPager viewPager;
    TextView tv_home,tv_movies,tv_series;
    TextView tv_server1,tv_server2;

    Boolean updateAvailable=false;
    String changeLog,update_url;
    SharedPreferences updateAssist;

    @Override
    public void onBackPressed() {
        if (pageOpened==0){
            new AlertDialog.Builder(MainActivity.this,R.style.MyAlertDialogStyle)
                    .setTitle("Had some work to do ?")
                    .setMessage("Do you really wanted to exit from VIDFLIX ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Enjoy VIDFLIXing", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setIcon(R.drawable.ic_stop_hand)
                    .setCancelable(false)
                    .show();
        }
        else if (pageOpened==1||pageOpened==2)
            viewPager.setCurrentItem(0);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void btmButtons(){

        findViewById(R.id.img_home_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//

                if (pageOpened==1||pageOpened==2)
                    viewPager.setCurrentItem(0);

//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                finish();
//                overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
            }
        });
        findViewById(R.id.img_search_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
                startActivity(new Intent(getApplicationContext(),Search.class));
//                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        findViewById(R.id.img_down_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
                startActivity(new Intent(getApplicationContext(),LiveTvPage.class));
//                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_home=findViewById(R.id.tv_vp_home);
        tv_series=findViewById(R.id.tv_vp_series);
        tv_movies=findViewById(R.id.tv_vp_movies);
        tv_server1=findViewById(R.id.tv_head_server1);
        tv_server2=findViewById(R.id.tv_head_server2);

        viewPager = findViewById(R.id.vp_main_mainpage);

        //-----------Permission part------------------//
        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permission2 = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (getApplicationContext().checkCallingOrSelfPermission(permission1)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        if (getApplicationContext().checkCallingOrSelfPermission(permission2)== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        //---------------Done Permission--------------//

        //----------------One Signal------------------------//
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();
        //--------------------------------------------------//

        //-----------------------Updater---------------------//

        Intent updater = getIntent();
        String error = updater.getStringExtra("ERROR");

        //----------------------------------------------------//

        if (!isNetworkAvailable()){
            new AlertDialog.Builder(MainActivity.this,R.style.MyAlertDialogStyle)
                    .setTitle("Not Connected to internet?")
                    .setMessage("This app requires active internet connetion otherwise there is a fair chance for app crash.")
                    .setPositiveButton("Ok",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();

        }else {

            if (error.equals("true")){
                String msg = updater.getStringExtra("MSG");
                String changeLog = updater.getStringExtra("CHANGE_LOG");
                final String url = updater.getStringExtra("URL");
                new AlertDialog.Builder(MainActivity.this,R.style.MyAlertDialogStyle)
                        .setTitle(msg)
                        .setMessage(changeLog)
                        .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        })
                        .setIcon(R.drawable.ic_stop_hand)
                        .setCancelable(false)
                        .show();
            }else {
            btmButtons();

            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),1));
            viewPager.setOffscreenPageLimit(2);
            viewPager.setPagingEnabled(false);
            }
        }

        findViewById(R.id.img_more_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
                startActivity(new Intent(getApplicationContext(),MorePage.class));
//                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tl_home);
        tabLayout.setupWithViewPager(viewPager);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    tv_home.setVisibility(View.VISIBLE);
                    tv_movies.setVisibility(View.VISIBLE);
                    tv_series.setVisibility(View.VISIBLE);
                    tv_server1.setVisibility(View.GONE);
                    tv_server2.setVisibility(View.GONE);
                    pageOpened=0;

                }else if (position==1){
                    tv_home.setVisibility(View.GONE);
                    tv_movies.setVisibility(View.VISIBLE);
                    tv_series.setVisibility(View.GONE);
                    tv_server1.setVisibility(View.INVISIBLE);
                    tv_server2.setVisibility(View.INVISIBLE);
                    pageOpened=1;

                }else if (position==2){
                    tv_home.setVisibility(View.GONE);
                    tv_movies.setVisibility(View.GONE);
                    tv_series.setVisibility(View.VISIBLE);
                    tv_server1.setVisibility(View.INVISIBLE);
                    tv_server2.setVisibility(View.INVISIBLE);
                    pageOpened=2;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.img_logo_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                viewPager.setCurrentItem(0);
            }
        });

        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                viewPager.setCurrentItem(0);
            }
        });

        tv_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                viewPager.setCurrentItem(1);
            }
        });

        tv_series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                viewPager.setCurrentItem(2);
            }
        });




    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        int server=1;

        public ViewPagerAdapter(FragmentManager fm,int server) {
            super(fm);
            this.server=server;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                //ChildFragment1 at position 0
                case 0:
                    return new FragHome(); //ChildFragment2 at position 1
                case 1:
                    return new FragMovies(); //ChildFragment3 at position 2
                case 2:
                    return new FragSeries();
            }
            return null; //does not happen
        }

        @Override
        public int getCount() {
            return 3; //three fragments
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {

                case 0:
                    return "Home";
                case 1:
                    return "Movies";
                case 2:
                    return "Series";
            }
            return null;
        }
    }






}
