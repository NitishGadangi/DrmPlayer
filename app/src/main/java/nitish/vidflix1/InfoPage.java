package nitish.vidflix1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.google.android.exoplayer2.C;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoPage extends AppCompatActivity {
    ProgressDialog progressDialog;

    static String mainHead,apiLink,subHead,data_type;

    String streamHead="";

    TextView tv_mainHead,tv_subHead,tv_des,tv_dir,tv_cast,tv_relDate,tv_info_wait;
    TextView tv_trivia11,tv_director11,tv_rel_date11,tv_cast11;

    Boolean isFirstTime=true;

    Button btn_watchNow,btn_download;

    ProgressBar prog_info_page;

    ImageView img_top;

    MainSetup mainSetup;

    static String scrnUrl;


    @Override
    public void onBackPressed() {
        mainSetup.cancel(true);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);

        Intent intent = getIntent();
        apiLink=intent.getStringExtra("MAIN_API_LINK");
        mainHead=intent.getStringExtra("HEADING");
        subHead=intent.getStringExtra("SUB_HEAD");
        isFirstTime=intent.getBooleanExtra("IS_FIRST_TIME",true);
        data_type=intent.getStringExtra("DATA_TYPE");

        img_top=findViewById(R.id.img_top);
        prog_info_page=findViewById(R.id.prog_info_page);
        btn_watchNow=findViewById(R.id.btn_watchNow);
        tv_relDate=findViewById(R.id.tv_relDate);
        tv_cast=findViewById(R.id.tv_cast);
        tv_dir=findViewById(R.id.tv_dir);
        tv_des=findViewById(R.id.tv_des);
        tv_subHead=findViewById(R.id.tv_subHead);
        tv_mainHead=findViewById(R.id.tv_mainHead);
        tv_info_wait=findViewById(R.id.tv_info_wait);
        btn_download=findViewById(R.id.btn_download);

        tv_cast11=findViewById(R.id.tv_cast11);
        tv_rel_date11=findViewById(R.id.tv_rel_date11);
        tv_director11=findViewById(R.id.tv_director11);
        tv_trivia11=findViewById(R.id.tv_trivia11);

        tv_mainHead.setText(mainHead);

        tv_subHead.setText(subHead);

        progressDialog = new ProgressDialog(InfoPage.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

//        if (isFirstTime){
//            tv_info_wait.setVisibility(View.VISIBLE);
//        }else{
//
//            FragMovies.backgroundTask.cancel(true);
//            FragSeries.backgroundTask.cancel(true);
//        }

        if (!apiLink.equals(null)){
            mainSetup=new MainSetup();
            mainSetup.execute();
        }

        findViewById(R.id.btn_share1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    class MainSetup extends AsyncTask<Void,Void,Void>{
        String jsonSrc;
        Boolean isFailed=false;

        @Override
        protected Void doInBackground(Void... voids) {

            jsonSrc=DataHandlers.getContent(apiLink);
            Log.i("BAAABY",apiLink+"  a"+jsonSrc);
            jsonSrc=DataHandlers.decryptMain(jsonSrc);
            if (jsonSrc.contains("\"film_name\":\"\"")){
                isFailed=true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (data_type.equals(""))
                isFailed=true;

            if (!isFailed) {
                tv_info_wait.setVisibility(View.GONE);

                btn_watchNow.setAlpha(1);
                btn_download.setAlpha(1);
                 String subH,film_name="",series_name,total_seasons;

                try {
                    JSONObject jsonObject = new JSONObject(jsonSrc);
                    String rel_date = jsonObject.getString("release_date");
                    String description = jsonObject.getString("description");
                    String timestamp=jsonObject.getString("timestamp");
                    String temp_icon_url = DataHandlers.completeDecrypt(jsonObject.getString("icon_url"),timestamp);
                    if (temp_icon_url.length()>1)
                        if (temp_icon_url.substring(0,1).equals("/"))
                            temp_icon_url=DataHandlers.base_url+temp_icon_url;
                    final String icon_url = temp_icon_url;
                    scrnUrl=icon_url;
                    String temp_thumbnail = DataHandlers.completeDecrypt(jsonObject.getString("thumbnail"),timestamp);
                    if (temp_thumbnail.length()>1)
                        if (temp_thumbnail.substring(0,1).equals("/"))
                            temp_thumbnail=DataHandlers.base_url+temp_thumbnail;
                    final String thumbnail = temp_thumbnail;
                    String director = jsonObject.getString("director");
                    String cast = jsonObject.getString("cast");
                    String quality = jsonObject.getString("quality");
                    String timing = jsonObject.getString("timing");
                    subH=timing+" • "+quality;
                    if (data_type.equals("MOVIE")){
                        film_name = DataHandlers.completeDecrypt(jsonObject.getString("film_name"),timestamp);
                        final String vid_url = DataHandlers.completeDecrypt(jsonObject.getString("vid_url"),timestamp);

                        btn_watchNow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //------Animation-----------//
                                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                animation1.setDuration(500);
                                view.startAnimation(animation1);
                                //-------------------------//


                                BottomSheetLO bottomSheetLO;
                                    bottomSheetLO=new BottomSheetLO(false,vid_url,thumbnail,false,getApplicationContext(),"","");

                                bottomSheetLO.show(getSupportFragmentManager(),"SelectQuality");
                            }
                        });

                        btn_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //------Animation-----------//
                                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                animation1.setDuration(500);
                                view.startAnimation(animation1);
                                //-------------------------//


                                BottomSheetLO bottomSheetLO;
                                bottomSheetLO=new BottomSheetLO(false,vid_url,thumbnail,true,getApplicationContext(),"","");

                                bottomSheetLO.show(getSupportFragmentManager(),"SelectQuality");
                            }
                        });

                        tv_mainHead.setText(film_name);
                    }else if(data_type.equals("SERIES")) {
                        Log.i("EXTRAAA","a"+jsonObject);
                        series_name=DataHandlers.completeDecrypt(jsonObject.getString("series_name"),timestamp);
                        final String season_data=DataHandlers.completeDecrypt(jsonObject.getString("season_data"),timestamp);
                        total_seasons=jsonObject.getString("total_seasons")+" Seasons";
                        subH=subH+" • "+total_seasons;

                        final String videoId_UC=jsonObject.getString("video_id");
                        final String server_UC=jsonObject.getString("server");

                        btn_watchNow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //------Animation-----------//
                                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                animation1.setDuration(500);
                                view.startAnimation(animation1);
                                //-------------------------//

                                BottomSheetLO bottomSheetLO;
                                    bottomSheetLO=new BottomSheetLO(true,season_data,thumbnail,false,getApplicationContext(),server_UC,videoId_UC);
                                bottomSheetLO.show(getSupportFragmentManager(),"SelectQuality");
                            }
                        });

                        btn_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //------Animation-----------//
                                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                animation1.setDuration(500);
                                view.startAnimation(animation1);
                                //-------------------------//

                                BottomSheetLO bottomSheetLO;
                                bottomSheetLO=new BottomSheetLO(true,season_data,thumbnail,true,getApplicationContext(),server_UC,videoId_UC);
                                bottomSheetLO.show(getSupportFragmentManager(),"SelectQuality");
                            }
                        });

                        tv_mainHead.setText(series_name);
                    }





                    String tot_cast = "";

                    if (!cast.equals("null")) {
                        JSONArray jsonArray = new JSONArray(cast);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject tempObj = jsonArray.getJSONObject(i);
                            if (i == jsonArray.length() - 1)
                                tot_cast += tempObj.getString("name") + ".";
                            else
                                tot_cast = tot_cast + tempObj.getString("name") + " , ";
                        }
                    }

                    tv_subHead.setText(subH);

                    if (!tot_cast.equals("")){
                    tv_cast.setText(tot_cast);
                    tv_cast.setVisibility(View.VISIBLE);
                    }else {
                        tv_cast11.setVisibility(View.GONE);
                    }

                    if (!description.equals("")) {
                        tv_des.setText(description);
                        tv_des.setVisibility(View.VISIBLE);
                    }else {
                        tv_trivia11.setVisibility(View.GONE);
                    }

                    if (!director.equals("")){
                    tv_dir.setText(director);
                    tv_dir.setVisibility(View.VISIBLE);
                    }else {
                        tv_director11.setVisibility(View.GONE);
                    }

                    if (!rel_date.equals("")){
                    tv_relDate.setText(rel_date);
                    tv_relDate.setVisibility(View.VISIBLE);
                    }else {
                        tv_rel_date11.setVisibility(View.GONE);
                    }

//                    RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.dummy_img).centerCrop();
//                    Glide.with(getApplicationContext()).load(icon_url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(img_top);

//                    Glide.with(getApplicationContext()).load(icon_url).centerCrop().placeholder(R.drawable.dummy_img).into(img_top);
                    Glide.with(getApplicationContext()).load(icon_url).centerCrop().placeholder(R.drawable.dummy_img).into((ImageView) findViewById(R.id.info_img_ken));
                    Glide.with(getApplicationContext()).load(thumbnail).centerCrop().placeholder(R.drawable.dummy_img).into((ImageView) findViewById(R.id.img_subH));

                    prog_info_page.setVisibility(View.GONE);
                    findViewById(R.id.scrl_info).setVisibility(View.VISIBLE);


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }else {
                tv_mainHead.setText("DATA NOT FOUND");
                tv_subHead.setText("UNKNOWN");
                prog_info_page.setVisibility(View.GONE);
                tv_info_wait.setVisibility(View.VISIBLE);
                tv_info_wait.setText("PLEASE GO BACK..!");
            }

        }
    }

    public static class BottomSheetLO extends BottomSheetDialogFragment{
        Boolean isSeries=false;
        String contentData;
        String icon_url;
        Boolean isDownload=false;
        Context main_context;

        String serverUC,videoID_UC;

        public BottomSheetLO(Boolean isSeries,String contentData,String icon_url,Boolean isDownload,Context context,String serverUC,String videoID_UC) {
            this.isSeries=isSeries;
            this.contentData=contentData;
            this.icon_url=icon_url;
            this.isDownload=isDownload;
            this.main_context=context;
            this.serverUC=serverUC;
            this.videoID_UC=videoID_UC;
        }

        @Override
        public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        }

        @androidx.annotation.Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {

            View rootView ;
            rootView= inflater.inflate(R.layout.select_quality,container,false);
            if (isSeries){
                rootView= inflater.inflate(R.layout.select_season,container,false);

                try {
                    JSONArray mainArr = new JSONArray(DataHandlers.decryptData(contentData));
                    JSONObject firstObj = mainArr.getJSONObject(0);

                    TextView seasonHead = rootView.findViewById(R.id.tv_seasonHeading);
                    String firHead=firstObj.getString("season_name");
                    seasonHead.setText("Season "+firHead);

                    RecyclerView rv_selQual=rootView.findViewById(R.id.rv_episonde_list);
                    RecyclerView.LayoutManager lm_selQual=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    RecAdapter recAdapter = new RecAdapter(true,new JSONArray(firstObj.getString("episodes")),icon_url,firHead,isDownload);
                    rv_selQual.setLayoutManager(lm_selQual);
                    rv_selQual.setAdapter(recAdapter);

                    RecyclerView rv_exp_layout=rootView.findViewById(R.id.rv_season_list);
                    rv_exp_layout.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_exp_layout.setAdapter(new ExpAdapter(rootView,mainArr,icon_url));


                    rootView.findViewById(R.id.tv_updateContent).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //------Animation-----------//
                            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                            animation1.setDuration(1000);
                            view.startAnimation(animation1);
                            //-------------------------//

                            String[] params={videoID_UC,serverUC};
                            new UpdateContent(main_context).execute(params);
//                            Toast.makeText(main_context, "This content will be updated soon.\nGet back here after a while.", Toast.LENGTH_LONG).show();
                            view.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }else {
                rootView= inflater.inflate(R.layout.select_quality,container,false);
                RecyclerView rv_selQual=rootView.findViewById(R.id.rv_selectQual);
                RecyclerView.LayoutManager lm_selQual=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rv_selQual.setLayoutManager(lm_selQual);
                RecAdapter recAdapter = null;
                try {
                    recAdapter = new RecAdapter(false,new JSONArray(DataHandlers.decryptData(contentData)),icon_url,"",isDownload);
                    rv_selQual.setAdapter(recAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }





            return rootView;
        }

        class ExpAdapter extends RecyclerView.Adapter<ExpAdapter.ViewHolder>{
            int size=5;
            View rootView;
            JSONArray mainArr;
            String icon_url;

            ExpAdapter(View rootView,JSONArray mainArr,String icon_url){
                this.rootView=rootView;
                this.size=mainArr.length();
                this.mainArr=mainArr;
                this.icon_url=icon_url;
            }


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_list_template,parent,false);
                ViewHolder holder = new ViewHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
                try {
                    JSONObject tempObj=mainArr.getJSONObject(position);
                    final String seasonName = tempObj.getString("season_name");
                    String episodesArrStr=tempObj.getString("episodes");
                    final JSONArray episodesArr=new JSONArray(episodesArrStr);

                    holder.tv_head.setText("Season "+seasonName);
                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //------Animation-----------//
                            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                            animation1.setDuration(500);
                            view.startAnimation(animation1);
                            //-------------------------//

                            TextView seasonHead = rootView.findViewById(R.id.tv_seasonHeading);
                            seasonHead.setText("Season "+seasonName);

                            RecyclerView rv_selQual=rootView.findViewById(R.id.rv_episonde_list);
                            RecyclerView.LayoutManager lm_selQual=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            RecAdapter recAdapter = new RecAdapter(true,episodesArr,icon_url,seasonName,isDownload);
                            rv_selQual.setLayoutManager(lm_selQual);
                            rv_selQual.setAdapter(recAdapter);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public int getItemCount() {
                return size;
            }


            class ViewHolder extends RecyclerView.ViewHolder{
                ConstraintLayout parentLayout;
                TextView tv_head;


                public ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    parentLayout = itemView.findViewById(R.id.text_list_template1);
                    tv_head=itemView.findViewById(R.id.tv_text_list1);

                }
            }
        }

        class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
            int size;
            String url_base=DataHandlers.base_url;
            JSONArray jsonArr;
            Boolean isSeries=false;
            String icon_url;
            String season="";
            Boolean isDown=false;



            RecAdapter(Boolean isSeries,JSONArray jsonArr,String icon_url,String season,Boolean isDown){
                this.isSeries=isSeries;
                this.jsonArr=jsonArr;
                this.icon_url=icon_url;
                this.size=jsonArr.length();
                this.season=season;
                this.isDown=isDown;
            }


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_template1,parent,false);
                ViewHolder holder = new ViewHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                String head="00";
                try {

                    JSONObject tempObj = jsonArr.getJSONObject(position);
                    if (isSeries){
                        String file_url=tempObj.getString("episode_url").trim();
                        file_url=file_url.replace(" ","%20");
                        head=tempObj.getString("episode_name");
                        if (head.length()==1)
                            head="E0"+head;
                        else
                            head="E"+head;

                        final String finalFile_url = file_url;
                        final String finalHead = head;

                        if (isDown){
                            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //------Animation-----------//
                                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                    animation1.setDuration(500);
                                    view.startAnimation(animation1);
                                    //-------------------------//
                                    String filName="[VIDFLIX]"+"S"+season+":"+ finalHead+"_"+mainHead+"."+DataHandlers.getExtention(finalFile_url);
                                    filName=filName.replace(" ","_");
                                    downloadNow(getContext(),finalFile_url,filName,"S"+season+":"+ finalHead+"_"+mainHead);

                                    dismiss();
                                }
                            });
                        }else{
                            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //------Animation-----------//
                                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                animation1.setDuration(500);
                                view.startAnimation(animation1);
                                //-------------------------//

                                Intent mIntent = StreamPlayer.getStartIntent(getContext(), finalFile_url,"S"+season+":"+ finalHead+ "  " +mainHead,scrnUrl);
                                startActivity(mIntent);
                                dismiss();

                            }
                            });
                        }
                        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {


                                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Download URL", finalFile_url);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                    }else {
                        String file_url=tempObj.getString("file_url").trim();
                        file_url=file_url.replace(" ","%20");
                        head=tempObj.getString("label");
                        final String finalFile_url = file_url;
                        if (isDown){
                            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //------Animation-----------//
                                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                    animation1.setDuration(500);
                                    view.startAnimation(animation1);
                                    //-------------------------//
                                    String filName="[VIDFLIX]"+mainHead+"."+DataHandlers.getExtention(finalFile_url);
                                    filName=filName.replace(" ","_");
                                    downloadNow(getContext(),finalFile_url,filName,mainHead);

                                    dismiss();
                                }
                            });
                        }else {
                            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //------Animation-----------//
                                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                    animation1.setDuration(500);
                                    view.startAnimation(animation1);
                                    //-------------------------//

                                    Intent mIntent = StreamPlayer.getStartIntent(getContext(), finalFile_url,mainHead,scrnUrl);
                                    startActivity(mIntent);
                                    dismiss();
                                }
                            });
                        }
                        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {


                                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Download URL", finalFile_url);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }
                    holder.tv_head.setText(head);

//                    RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.dummy_img).centerCrop();
//                    Glide.with(getActivity()).load(icon_url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.bg_icon);

                    Glide.with(getContext()).load(icon_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.bg_icon);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public int getItemCount() {
                return size;
            }


            class ViewHolder extends RecyclerView.ViewHolder{
                ConstraintLayout parentLayout;
                TextView tv_head;
                ImageView bg_icon;


                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    parentLayout = itemView.findViewById(R.id.episode_template1);
                    tv_head=itemView.findViewById(R.id.tv_name_ep_template1);
                    bg_icon=itemView.findViewById(R.id.img_bg_ep_template1);

                }
            }
        }

    }

    static void downloadNow(Context context,String url,String file,String head){
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/VIDFLIX").mkdirs();
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(head);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+"/VIDFLIX", file);
        downloadmanager.enqueue(request);
        Toast.makeText(context, "Added to Download Queue.!\nCheck Notification panel", Toast.LENGTH_LONG).show();
    }

    static class UpdateContent extends AsyncTask<String,Void,Void>{
        Context context;
        String res;
        ProgressDialog progressDialog;

         UpdateContent(Context context) {
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (res.contains("\"status\":\"success\"")){
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    res=jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            res = DataHandlers.updateContent(strings[0],strings[1]);
            return null;
        }
    }


}
