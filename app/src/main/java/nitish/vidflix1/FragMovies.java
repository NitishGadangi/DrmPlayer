package nitish.vidflix1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

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

public class FragMovies extends Fragment {

    String deviceId="FAILED";

    RecyclerView rv_allmovies;
    RecyclerView.LayoutManager lm_allmovies;

    ProgressBar prog_frag_movies,prog_pager_movies;

    SharedPreferences bak_json;

    static BackgroundTask backgroundTask;

    Boolean isFirstTime=false;

    String m1_json_movies,m2_json_movies,m3_json_movies,m4_json_movies,m5_json_movies,m6_json_movies;
    int server=1;

    TextView tv_server1,tv_server2,tv_server3,tv_server4,tv_server5,tv_server6;

    int curServer=1;

    int showingPage=0;
    Boolean eof=false,isLoading=false;


    String jsonBackup1="[]",jsonBackup2="[]",jsonBackup3="[]",jsonBackup4="[]",jsonBackup5="[]",jsonBackup6="[]";

    FragMovies(int server){
        this.server=server;
    }
    FragMovies(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.frag_movies,container,false);

        tv_server1=rootView.findViewById(R.id.mov_serv1);
        tv_server2=rootView.findViewById(R.id.mov_serv2);
        tv_server3=rootView.findViewById(R.id.mov_serv3);
        tv_server4=rootView.findViewById(R.id.mov_serv4);
        tv_server5=rootView.findViewById(R.id.mov_serv5);
        tv_server6=rootView.findViewById(R.id.mov_serv6);

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
                tv_server5.setAlpha(0.5f);
                tv_server6.setAlpha(0.5f);


                if (curServer!=1){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try{
                        if (!m1_json_movies.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(m1_json_movies);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,1);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allmovies.setAdapter(ra_allmovies);

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
                tv_server5.setAlpha(0.5f);
                tv_server6.setAlpha(0.5f);

                if (curServer!=2){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try{
                        if (!m2_json_movies.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(m2_json_movies);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,2);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allmovies.setAdapter(ra_allmovies);

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
                tv_server5.setAlpha(0.5f);
                tv_server6.setAlpha(0.5f);

                if (curServer!=3){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try{
                        if (!m3_json_movies.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(m3_json_movies);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,3);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allmovies.setAdapter(ra_allmovies);

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
                tv_server4.setAlpha(1);
                tv_server5.setAlpha(0.5f);
                tv_server6.setAlpha(0.5f);

                if (curServer!=4){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try{
                        if (!m4_json_movies.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(m4_json_movies);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,4);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allmovies.setAdapter(ra_allmovies);

                    backgroundTask.cancel(true);
                    backgroundTask=new BackgroundTask(4,1);
                    backgroundTask.execute();
                }
                curServer=4;
            }
        });
        tv_server5.setOnClickListener(new View.OnClickListener() {
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
                tv_server4.setAlpha(0.5f);
                tv_server5.setAlpha(1f);
                tv_server6.setAlpha(0.5f);

                if (curServer!=5){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try{
                        if (!m5_json_movies.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(m5_json_movies);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,5);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allmovies.setAdapter(ra_allmovies);

                    backgroundTask.cancel(true);
                    backgroundTask=new BackgroundTask(5,1);
                    backgroundTask.execute();
                }
                curServer=5;
            }
        });
        tv_server6.setOnClickListener(new View.OnClickListener() {
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
                tv_server4.setAlpha(0.5f);
                tv_server5.setAlpha(0.5f);
                tv_server6.setAlpha(1f);

                if (curServer!=6){
                    RecAdapter ra_allmovies = new RecAdapter(false);
                    try{
                        if (!m6_json_movies.equals("FAILED")){
                            JSONArray allmoviesArr = new JSONArray(m6_json_movies);
                            ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,6);
                        }else
                            isFirstTime=true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    rv_allmovies.setAdapter(ra_allmovies);

                    backgroundTask.cancel(true);
                    backgroundTask=new BackgroundTask(6,1);
                    backgroundTask.execute();
                }
                curServer=6;
            }
        });

        deviceId = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        prog_frag_movies= rootView.findViewById(R.id.prog_frag_movies);
        prog_pager_movies=rootView.findViewById(R.id.prog_pager_movies);

        rv_allmovies=rootView.findViewById(R.id.rv_allmovies);

        rv_allmovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Log.i("-----","end");
                    if (!isLoading && !eof){
                        Log.i("-----ee","t"+eof);
                        prog_pager_movies.setVisibility(View.VISIBLE);

                        backgroundTask.cancel(true);
                        backgroundTask=new BackgroundTask(curServer,showingPage+1);
                        backgroundTask.execute();
                    }
                }
            }
        });

        bak_json=getContext().getSharedPreferences(getResources().getString(R.string.bak_json), Context.MODE_PRIVATE);
        m1_json_movies=bak_json.getString(getResources().getString(R.string.json_allmovies),"FAILED");
        m2_json_movies=bak_json.getString(getResources().getString(R.string.json_allmovies)+"2","FAILED");
        m3_json_movies=bak_json.getString(getResources().getString(R.string.json_allmovies)+"3","FAILED");
        m4_json_movies=bak_json.getString(getResources().getString(R.string.json_allmovies)+"4","FAILED");
        m5_json_movies=bak_json.getString(getResources().getString(R.string.json_allmovies)+"5","FAILED");
        m6_json_movies=bak_json.getString(getResources().getString(R.string.json_allmovies)+"6","FAILED");

        RecAdapter ra_allmovies = new RecAdapter(false);
        try{
            if (!m1_json_movies.equals("FAILED")){
                JSONArray allmoviesArr = new JSONArray(m1_json_movies);
                ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,1);
            }else
                isFirstTime=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        rv_allmovies.setAdapter(ra_allmovies);


        try {
            lm_allmovies=new GridLayoutManager(getContext(),3);
            rv_allmovies.setLayoutManager(lm_allmovies);


        }catch (Exception e){
            e.printStackTrace();
        }

        backgroundTask=new BackgroundTask(server,1);
        backgroundTask.execute();

        final SwipeRefreshLayout swipe=rootView.findViewById(R.id.swipe_movies);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                showingPage=0;
//                loadedPage=1;
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



    class BackgroundTask extends AsyncTask<Void,Void,Void>{
        String allmoviesStr="\"status\":\"false\"";
        int serv;
        int page;

        BackgroundTask(int sev,int page){
            this.serv=sev;
            this.page=page;
            Log.i("BAKUP_TEST","mov"+sev);
        }

        @Override
        protected void onCancelled() {
            prog_frag_movies.setVisibility(View.GONE);
            Log.i("BAKUP_TEST","MOV_CANCELED");
            super.onCancelled();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading=true;
            Log.i("PAGEEE",""+page);

        }
        @Override
        protected Void doInBackground(Void... voids) {
            if (serv==1){
                    allmoviesStr = DataHandlers.getAllMovies(deviceId,page);
            }else if(serv==2)
                    allmoviesStr = DataHandlers.getContent(DataHandlers.allmovies2+"?page="+page);
            else if(serv==3)
                allmoviesStr = DataHandlers.getContent(DataHandlers.allmovies3+"?page="+page);
            else if(serv==4)
                allmoviesStr = DataHandlers.getContent(DataHandlers.allmovies4+"?page="+page);
            else if(serv==5)
                allmoviesStr = DataHandlers.getContent(DataHandlers.allmovies5+"?page="+page);
            else if(serv==6)
                allmoviesStr = DataHandlers.getContent(DataHandlers.allmovies6+"?page="+page);
            allmoviesStr=DataHandlers.decryptMain(allmoviesStr);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog_frag_movies.setVisibility(View.GONE);
            prog_pager_movies.setVisibility(View.GONE);


            if (!allmoviesStr.equals(null)&&!allmoviesStr.contains("\"status\":\"false\"")){
                eof=false;
                SharedPreferences.Editor editor = bak_json.edit();
                if (serv==1){
                    allmoviesStr=DataHandlers.joinJSON(jsonBackup1,allmoviesStr);
                    jsonBackup1=allmoviesStr;
                    if (page==1)
                    editor.putString(getResources().getString(R.string.json_allmovies),allmoviesStr).apply();
                    m1_json_movies=allmoviesStr;
                }
                else if(serv==2){
                    allmoviesStr=DataHandlers.joinJSON(jsonBackup2,allmoviesStr);
                    jsonBackup2=allmoviesStr;
                    if (page==1)
                    editor.putString(getResources().getString(R.string.json_allmovies)+"2",allmoviesStr).apply();
                    m2_json_movies=allmoviesStr;
                }
                else if(serv==3){
                    Log.i("-----ee","t"+" "+allmoviesStr);
                    allmoviesStr=DataHandlers.joinJSON(jsonBackup3,allmoviesStr);
                    jsonBackup3=allmoviesStr;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_allmovies)+"3",allmoviesStr).apply();
                    m3_json_movies=allmoviesStr;
                }
                else if(serv==4){
                    allmoviesStr=DataHandlers.joinJSON(jsonBackup4,allmoviesStr);
                    jsonBackup4=allmoviesStr;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_allmovies)+"4",allmoviesStr).apply();
                    m4_json_movies=allmoviesStr;
                }
                else if(serv==5){
                    allmoviesStr=DataHandlers.joinJSON(jsonBackup5,allmoviesStr);
                    jsonBackup5=allmoviesStr;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_allmovies)+"5",allmoviesStr).apply();
                    m5_json_movies=allmoviesStr;
                }
                else if(serv==6){
                    allmoviesStr=DataHandlers.joinJSON(jsonBackup6,allmoviesStr);
                    jsonBackup6=allmoviesStr;
                    if (page==1)
                        editor.putString(getResources().getString(R.string.json_allmovies)+"6",allmoviesStr).apply();
                    m6_json_movies=allmoviesStr;
                }

                try {
                    JSONArray allmoviesArr = new JSONArray(allmoviesStr);
//                lm_allmovies=new GridLayoutManager(getContext(),3);
                    RecAdapter ra_allmovies=null;
//                    if (serv==1)
//                        ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,1);
//                    else if (serv==2)
                        ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url,serv);
                        if (serv==3||serv==4)
                            ra_allmovies = new RecAdapter(allmoviesArr,"",serv);

                    Parcelable recyclerViewState;
                    recyclerViewState = rv_allmovies.getLayoutManager().onSaveInstanceState();

                    rv_allmovies.setAdapter(ra_allmovies);

                    rv_allmovies.getLayoutManager().onRestoreInstanceState(recyclerViewState);
//                    if (page!=1){
//                        rv_allmovies.scrollToPosition(allmoviesArr.length()-15);
//                    }
                    Log.i("PAGEEE","len"+allmoviesArr.length());

                    showingPage++;
                }catch (Exception e){
                    Log.i("JSON_JOIN",serv+"j  "+allmoviesStr);
                    e.printStackTrace();
                }
            }else {
                eof=true;
                Log.i("-----ee","t"+"  HEre");
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


        RecAdapter(JSONArray jsonArr,String url_base,int server){
            this.jsonArr=jsonArr;
            this.url_base=url_base;
            this.size=jsonArr.length();
            this.server=server;
        }

        RecAdapter(Boolean isLoaded){
            this.isLoaded=isLoaded;
            this.size=20;
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
                    String subH="";
                    final JSONObject tempObj = jsonArr.getJSONObject(position);
//                JSONObject tempObj = posObj.getJSONObject(jsonHead);
                    String timestamp=tempObj.getString("timestamp");

                    final String img_url = url_base + DataHandlers.completeDecrypt(tempObj.getString("thumbnail"),timestamp);

                    final String video_id = tempObj.getString("video_id");

                    final String title;
                    if (isSeries)
                        title = tempObj.getString("series_name");
                    else
                        title = DataHandlers.completeDecrypt(tempObj.getString("film_name"),timestamp);
                    final String timing = tempObj.getString("timing");
                    final String quality = tempObj.getString("quality");
                    holder.tv_quality_rv_images.setText(quality);
                    if (quality.equals(""))
                        holder.tv_quality_rv_images.setVisibility(View.INVISIBLE);
                    else
                        subH+=quality+DataHandlers.DOT;
                    if (timing.equals(""))
                        holder.tv_timing_rv_images.setVisibility(View.INVISIBLE);
                    else
                        subH+=timing;
                     holder.tv_timing_rv_images.setText(subH);

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
                                intent.putExtra("MAIN_API_LINK",DataHandlers.moviesServer1+"?video_id="+video_id);
                            else if (server==2)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.moviesServer2+"?video_id="+video_id);
                            else if (server==3)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.moviesServer3+"?video_id="+video_id);
                            else if (server==4)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.moviesServer4+"?video_id="+video_id);
                            else if (server==5)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.moviesServer5+"?video_id="+video_id);
                            else if (server==6)
                                intent.putExtra("MAIN_API_LINK",DataHandlers.moviesServer6+"?video_id="+video_id);
                            intent.putExtra("HEADING",title);
                            intent.putExtra("SUB_HEAD",timing+" • "+quality);
                            intent.putExtra("IS_FIRST_TIME",isFirstTime);
                            intent.putExtra("DATA_TYPE","MOVIE");
                            startActivity(intent);
                        }
                    });
                    holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Log.i("IMG_DAMAGE",tempObj.toString());
                            Log.i("IMG_DAMAGE",img_url);
                            return false;
                        }
                    });


//                    RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.dummy_img).centerCrop();
//                    Glide.with(getActivity()).load(img_url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.img_main_rv_images);
                    if (img_url.substring(0,1).equals("/"))
                        Glide.with(getContext()).load(DataHandlers.base_url+img_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_main_rv_images);
                    else
                        Glide.with(getContext()).load(img_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_main_rv_images);
                    holder.tv_moviename_rv_images.setText(title);

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
                    Log.i("IMG_DAMAGE","err "+e);
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
