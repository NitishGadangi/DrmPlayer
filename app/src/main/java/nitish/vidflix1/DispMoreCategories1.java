package nitish.vidflix1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DispMoreCategories1 extends AppCompatActivity {

    String topHead,mainApiLink,subApiLink,jsonBackupKey;
    TextView tv_topHead;
    ImageView btn_back;
    ProgressBar prog_moreCatPage;
    RecyclerView rv_dispMoreCat;
    RecyclerView.LayoutManager lm_dispMoreCat;

    SharedPreferences bak_json;

    BackgroundTask backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_more_categories1);

        Intent intent = getIntent();
        mainApiLink=intent.getStringExtra("MAIN_API_LINK");
        subApiLink=intent.getStringExtra("SUB_API_LINK");
        topHead=intent.getStringExtra("TOP_HEADING");
        jsonBackupKey=intent.getStringExtra("JSON_BACKUP_KEY");

        tv_topHead=findViewById(R.id.tv_head_moreCatPage);
        btn_back=findViewById(R.id.bak_moreCatPage);
        prog_moreCatPage=findViewById(R.id.prog_moreCatPage);
        rv_dispMoreCat=findViewById(R.id.rv_moreCatPage);

        tv_topHead.setText(topHead);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                onBackPressed();
            }
        });


        bak_json=getApplicationContext().getSharedPreferences(getResources().getString(R.string.bak_json), Context.MODE_PRIVATE);
        String json_movies=bak_json.getString(jsonBackupKey,"FAILED");

        try {
            lm_dispMoreCat=new GridLayoutManager(getApplicationContext(),3);
            RecAdapter ra_allmovies = new RecAdapter(false);
            json_movies="FAILED";
            if (!json_movies.equals("FAILED")){
                JSONArray allmoviesArr = new JSONArray(json_movies);
                ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url);
            }
            rv_dispMoreCat.setLayoutManager(lm_dispMoreCat);
            rv_dispMoreCat.setAdapter(ra_allmovies);

        }catch (Exception e){
            e.printStackTrace();
        }

        backgroundTask=new BackgroundTask();
        backgroundTask.execute();

        final SwipeRefreshLayout swipe=findViewById(R.id.swipe_dispMore);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                backgroundTask.cancel(true);
                backgroundTask=new BackgroundTask();
                backgroundTask.execute();


                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipe.setRefreshing(false);
                    }
                }, 3000);
            }
        });


    }

    class BackgroundTask extends AsyncTask<Void,Void,Void> {
        String allmoviesStr;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.i("CHECHH","Triggered");

            prog_moreCatPage.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            allmoviesStr = DataHandlers.getContent(mainApiLink);
            allmoviesStr = DataHandlers.decryptMain(allmoviesStr);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog_moreCatPage.setVisibility(View.GONE);





            try {
                JSONArray allmoviesArr = new JSONArray(allmoviesStr);
                lm_dispMoreCat=new GridLayoutManager(getApplicationContext(),3);
                RecAdapter ra_allmovies = new RecAdapter(allmoviesArr,DataHandlers.base_url);
                rv_dispMoreCat.setLayoutManager(lm_dispMoreCat);
                rv_dispMoreCat.setAdapter(ra_allmovies);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
        int size;
        String url_base;
        JSONArray jsonArr;
        String jsonHead;
        Boolean isSeries=false;

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


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_template1,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String releaseDate="";
            String exemp="Bollywood Movies";
            if (isLoaded) {
                try {
                    String subH="";
                    JSONObject tempObj = jsonArr.getJSONObject(position);
                    String timestamp=tempObj.getString("timestamp");
//                JSONObject tempObj = posObj.getJSONObject(jsonHead);
                    String img_url = url_base+DataHandlers.completeDecrypt(tempObj.getString("thumbnail"),timestamp);
                    if (img_url.equals(url_base+"")){
                        img_url = url_base+DataHandlers.completeDecrypt(tempObj.getString("icon_url"),timestamp);
                    }

//                    releaseDate = tempObj.getString("release_date");

                    final String video_id=tempObj.getString("video_id");

                    final String title;
                    if (isSeries){
                        title = DataHandlers.completeDecrypt(tempObj.getString("series_name"),timestamp);
                    }
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

                            Intent intent = new Intent(getApplicationContext(),InfoPage.class);
                            intent.putExtra("MAIN_API_LINK",subApiLink+"?video_id="+video_id);
                            intent.putExtra("HEADING",title);
                            intent.putExtra("SUB_HEAD",timing+" • "+quality);
                            intent.putExtra("IS_FIRST_TIME",false);
                            intent.putExtra("DATA_TYPE","MOVIE");
                            startActivity(intent);
                        }
                    });

//                    RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.dummy_img).centerCrop();
//                    Glide.with(getApplicationContext()).load(img_url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.img_main_rv_images);

                    Glide.with(getApplicationContext()).load(img_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_main_rv_images);

                    holder.tv_moviename_rv_images.setText(title);

//                    if (topHead.contains(exemp)){
//                        holder.tv_cast_gridtemp1.setText(releaseDate);
//                    }else{
//                    String castArrStr=tempObj.getString("cast");
//                    JSONArray castArr=new JSONArray(castArrStr);
//                    String cast="FAILED";
//                    cast="( "+castArr.getJSONObject(0).getString("name")+", "+
//                            castArr.getJSONObject(1).getString("name")+", "+
//                            castArr.getJSONObject(2).getString("name")+" )";
//                    holder.tv_cast_gridtemp1.setText(cast);}

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
