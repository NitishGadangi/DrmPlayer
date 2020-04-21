package nitish.vidflix1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.spec.DHGenParameterSpec;

public class FragSeries extends Fragment {

    String deviceId="FAILED";

    ProgressBar prog_frag_movies,prog_pager_series;
    RecyclerView rv_allseries;
    RecyclerView.LayoutManager lm_allseries;

    SharedPreferences bak_json;

    Boolean isFirstTime=false;


    static BackgroundTask backgroundTask;

    String s1_json_series,s2_json_series,s3_json_series,s4_json_series;
    String jsonBackup1="[]",jsonBackup2="[]",jsonBackup3="[]",jsonBackup4="[]";

    int server=1;
    int showingPage=0;
    Boolean eof=false,isLoading=false;

    TextView tv_server1,tv_server2,tv_server3,tv_server4;

    int curServer=1;

    FragSeries(int server){
        this.server=server;
    }
    FragSeries(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.frag_series,container,false);

        tv_server1=rootView.findViewById(R.id.ser_serv1);
        tv_server2=rootView.findViewById(R.id.ser_serv2);
        tv_server3=rootView.findViewById(R.id.ser_serv3);
        tv_server4=rootView.findViewById(R.id.ser_serv4);


        tv_server1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                tv_server1.setAlpha(1f);
                tv_server2.setAlpha(0.5f);
                tv_server3.setAlpha(0.5f);
                tv_server4.setAlpha(0.5f);

                if (curServer!=1){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try {
                        if (!s1_json_series.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(s1_json_series);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,1);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allseries.setAdapter(ra_allmovies);

                    backgroundTask.cancel(true);
                    backgroundTask=new BackgroundTask(1,1);
                    backgroundTask.execute();
                }
                curServer=1;

            }
        });

        tv_server2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                tv_server1.setAlpha(0.5f);
                tv_server2.setAlpha(1f);
                tv_server3.setAlpha(0.5f);
                tv_server4.setAlpha(0.5f);

                if (curServer!=2){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try {
                        if (!s2_json_series.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(s2_json_series);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,2);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allseries.setAdapter(ra_allmovies);

                    backgroundTask.cancel(true);
                    backgroundTask=new BackgroundTask(2,1);
                    backgroundTask.execute();
                }
                curServer=2;

            }
        });
        tv_server3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                tv_server1.setAlpha(0.5f);
                tv_server2.setAlpha(0.5f);
                tv_server3.setAlpha(1f);
                tv_server4.setAlpha(0.5f);

                if (curServer!=3){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try {
                        if (!s3_json_series.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(s3_json_series);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,3);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allseries.setAdapter(ra_allmovies);

                    backgroundTask.cancel(true);
                    backgroundTask=new BackgroundTask(3,1);
                    backgroundTask.execute();
                }
                curServer=3;

            }
        });
        tv_server4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                tv_server1.setAlpha(0.5f);
                tv_server2.setAlpha(0.5f);
                tv_server3.setAlpha(0.5f);
                tv_server4.setAlpha(1f);

                if (curServer!=4){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try {
                        if (!s4_json_series.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(s4_json_series);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,4);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allseries.setAdapter(ra_allmovies);

                    backgroundTask.cancel(true);
                    backgroundTask=new BackgroundTask(4,1);
                    backgroundTask.execute();
                }
                curServer=4;

            }
        });

        deviceId = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        prog_frag_movies= rootView.findViewById(R.id.prog_frag_series);
        prog_pager_series=rootView.findViewById(R.id.prog_pager_series);

        rv_allseries=rootView.findViewById(R.id.rv_allseries);
        rv_allseries.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Log.i("-----","end");
                    if (!isLoading && !eof){

                        prog_pager_series.setVisibility(View.VISIBLE);

                        backgroundTask.cancel(true);
                        backgroundTask=new BackgroundTask(curServer,showingPage+1);
                        backgroundTask.execute();
                    }
                }
            }
        });

        bak_json=getContext().getSharedPreferences(getResources().getString(R.string.bak_json), Context.MODE_PRIVATE);
        s1_json_series=bak_json.getString(getResources().getString(R.string.json_series_home)+"all1","FAILED");
        s2_json_series=bak_json.getString(getResources().getString(R.string.json_series_home)+"all12","FAILED");
        s3_json_series=bak_json.getString(getResources().getString(R.string.json_series_home)+"all13","FAILED");
        s4_json_series=bak_json.getString(getResources().getString(R.string.json_series_home)+"all14","FAILED");


        RecAdapter ra_allmovies = new RecAdapter(false);
        try {
            if (!s1_json_series.equals("FAILED")){
                JSONArray allmoviesArr = new JSONArray(s1_json_series);
                ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,1);
            }else
                isFirstTime=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        rv_allseries.setAdapter(ra_allmovies);

        try {
            lm_allseries=new GridLayoutManager(getContext(),3);
            rv_allseries.setLayoutManager(lm_allseries);

        }catch (Exception e){
            e.printStackTrace();
        }

        backgroundTask=new BackgroundTask(server,1);
//        if (backgroundTask.isCancelled())
            backgroundTask.execute();

        final SwipeRefreshLayout swipe=rootView.findViewById(R.id.swipe_series);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                backgroundTask.cancel(true);
//                backgroundTask=new BackgroundTask(curServer,1);
//                backgroundTask.execute();


                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipe.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return  rootView;

    }

    class BackgroundTask extends AsyncTask<Void,Void,Void> {
        String allSeries="\"status\":\"false\"";
        int serv;
        int page;

        BackgroundTask(int sev,int page){
            this.serv=sev;
            this.page=page;
            Log.i("BAKUP_TEST","ser"+sev);
        }

        @Override
        protected void onCancelled() {
            prog_frag_movies.setVisibility(View.GONE);
            Log.i("BAKUP_TEST","SER_CANCELED");
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading=true;
            Log.i("BAKUP_TEST","SER_STARTED");
//            prog_frag_movies.setVisibility(View.VISIBLE);

//            if (serv==1){
//                RecAdapter ra_allmovies = new RecAdapter(false);
//                try {
//                    if (!s1_json_series.equals("FAILED")){
//                        JSONArray allmoviesArr = new JSONArray(s1_json_series);
//                        ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,1);
//                    }else
//                        isFirstTime=true;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                rv_allseries.setAdapter(ra_allmovies);
//            }else if (serv==2){
//                RecAdapter ra_allmovies = new RecAdapter(false);
//                try {
//                    if (!s2_json_series.equals("FAILED")){
//                        JSONArray allmoviesArr = new JSONArray(s2_json_series);
//                        ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,2);
//                    }else
//                        isFirstTime=true;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                rv_allseries.setAdapter(ra_allmovies);
//            }

        }
        @Override
        protected Void doInBackground(Void... voids) {
            if (serv==1)
                allSeries = DataHandlers.getTvSeriesHome(deviceId,page);
            else if (serv==2)
                allSeries = DataHandlers.getContent(DataHandlers.allseries2+"?page="+page);
            else if (serv==3)
                allSeries = DataHandlers.getContent(DataHandlers.allseries3+"?page="+page);
            else if (serv==4)
                allSeries = DataHandlers.getContent(DataHandlers.allseries4+"?page="+page);
            allSeries=DataHandlers.decryptMain(allSeries);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog_frag_movies.setVisibility(View.GONE);
            prog_pager_series.setVisibility(View.GONE);

            if (!allSeries.equals(null)&&!allSeries.contains("\"status\":\"false\"")){
                eof=false;
                SharedPreferences.Editor editor = bak_json.edit();
                if (serv==1){
                    allSeries= DataHandlers.joinJSON(jsonBackup1,allSeries);
                    jsonBackup1=allSeries;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_series_home)+"all1",allSeries).apply();
                    s1_json_series=allSeries;
                }
                else if (serv==2){
                    allSeries= DataHandlers.joinJSON(jsonBackup2,allSeries);
                    jsonBackup2=allSeries;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_series_home)+"all12",allSeries).apply();
                    s2_json_series=allSeries;
                }
                else if (serv==3){
                    allSeries= DataHandlers.joinJSON(jsonBackup3,allSeries);
                    jsonBackup3=allSeries;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_series_home)+"all13",allSeries).apply();
                    s3_json_series=allSeries;
                }
                else if (serv==4){
                    allSeries= DataHandlers.joinJSON(jsonBackup4,allSeries);
                    jsonBackup4=allSeries;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_series_home)+"all14",allSeries).apply();
                    s4_json_series=allSeries;
                }

                try {
                    JSONArray allmoviesArr = new JSONArray(allSeries);
//                lm_allseries=new GridLayoutManager(getContext(),3);
                    RecAdapter ra_allmovies=null;
//                    if (serv==1)
//                        ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,1);
//                    else if(serv==2)
                        ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,true,serv);
//                rv_allseries.setLayoutManager(lm_allseries);
                    Parcelable recyclerViewState;
                    recyclerViewState = rv_allseries.getLayoutManager().onSaveInstanceState();
                    rv_allseries.setAdapter(ra_allmovies);
                    rv_allseries.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                    showingPage++;
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("STRM_TYPE","s  "+e);
                }
            }else {
                eof=true;
            }
            isLoading=false;
        }
    }




    class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
        int size;
        String url_base;
        JSONArray jsonArr;
        String jsonHead;
        Boolean isSeries=false;
        int server=1;
        Boolean isLoaded=true;


        RecAdapter(JSONArray jsonArr,String url_base){
            this.jsonArr=jsonArr;
            this.url_base=url_base;
            this.size=jsonArr.length();

        }

        RecAdapter(Boolean isLoaded){
            this.isLoaded=isLoaded;
            this.size=20;
        }

        RecAdapter(JSONArray jsonArr,String url_base,Boolean isSeries,int server){
            this.jsonArr=jsonArr;
            this.url_base=url_base;
            this.size=jsonArr.length();
            this.isSeries=isSeries;
            this.server=server;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_template1,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (isLoaded) {
                try {
                    String subbH="";
                    JSONObject tempObj = jsonArr.getJSONObject(position);
                    String timestamp=tempObj.getString("timestamp");
//                JSONObject tempObj = posObj.getJSONObject(jsonHead);
                    String img_url = url_base + DataHandlers.completeDecrypt(tempObj.getString("thumbnail"),timestamp);
                    String seasons=tempObj.getString("total_seasons");

                    if (seasons.equals("1"))
                        seasons=seasons+" Season";
                    else
                        seasons=seasons+" Seasons";
                    subbH=seasons;
                    final String title;
                    if (isSeries)
                        title = DataHandlers.completeDecrypt(tempObj.getString("series_name"),timestamp);
                    else
                        title = DataHandlers.completeDecrypt(tempObj.getString("film_name"),timestamp);
                    final String timing = tempObj.getString("timing");
                    final String quality = tempObj.getString("quality");
                    final String video_id=tempObj.getString("video_id");
                    holder.tv_quality_rv_images.setText(quality);
                    if (quality.equals("")){
                        holder.tv_quality_rv_images.setVisibility(View.INVISIBLE);
                    }else {
                        subbH+=DataHandlers.DOT+quality;
                    }
                    holder.tv_timing_rv_images.setText(subbH);



//                    RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.dummy_img).centerCrop();
//                    Glide.with(getActivity()).load(img_url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.img_main_rv_images);

                    Glide.with(getContext()).load(img_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_main_rv_images);

                    holder.tv_moviename_rv_images.setText(title);


                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //------Animation-----------//
                            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                            animation1.setDuration(500);
                            view.startAnimation(animation1);
                            //-------------------------//

                            Intent intent = new Intent(getContext(),InfoPage.class);
                            if (server==1)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.series_server1+"?video_id="+video_id);
                            else if (server==2)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.series_server2+"?video_id="+video_id);
                            else if (server==3)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.series_server3+"?video_id="+video_id);
                            else if (server==4)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.series_server4+"?video_id="+video_id);
                            intent.putExtra("HEADING",title);
                            intent.putExtra("SUB_HEAD",timing+" • "+quality);
                            intent.putExtra("IS_FIRST_TIME",isFirstTime);
                            intent.putExtra("DATA_TYPE","SERIES");
                            startActivity(intent);
                        }
                    });

//                    String castArrStr=tempObj.getString("cast");
//                    JSONArray castArr=new JSONArray(castArrStr);
//                    String cast="FAILED";
//                    cast="( "+castArr.getJSONObject(0).getString("name")+", "+
//                            castArr.getJSONObject(1).getString("name")+", "+
//                            castArr.getJSONObject(2).getString("name")+" )";
//                    holder.tv_cast_gridtemp1.setText(cast);
//                    if(cast.equals("FAILED"))
//                        holder.tv_cast_gridtemp1.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            return size;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            ConstraintLayout parentLayout;
            ImageView img_main_rv_images;
            TextView tv_moviename_rv_images,tv_timing_rv_images,tv_quality_rv_images,tv_cast_gridtemp1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_main_rv_images=itemView.findViewById(R.id.img_main_gridtemp1);
                parentLayout = itemView.findViewById(R.id.grid_template1);
                tv_moviename_rv_images=itemView.findViewById(R.id.tv_movName_gridtemp1);
                tv_timing_rv_images=itemView.findViewById(R.id.tv_dur_gridtemp1);
                tv_quality_rv_images = itemView.findViewById(R.id.tv_qual_gridtemp1);
                tv_cast_gridtemp1=itemView.findViewById(R.id.tv_cast_gridtemp1);
            }
        }
    }
}
