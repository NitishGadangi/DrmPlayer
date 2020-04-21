package nitish.vidflix1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragHome extends Fragment {
    String deviceId="FAILED";

    ProgressBar prog_frag_home;

    TextView tv_loadinfo_home;

    RecyclerView rv_main_home;

    SharedPreferences bak_json;
    SharedPreferences.Editor edit_bak_json;

    static BackgroundTask backgroundTask;

    Boolean isFirstTime=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.frag_home,container,false);


        prog_frag_home = rootView.findViewById(R.id.prog_frag_home);

        rv_main_home=rootView.findViewById(R.id.rv_main_home);
        rv_main_home.setLayoutManager(new LinearLayoutManager(getContext()));

        deviceId = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        tv_loadinfo_home=rootView.findViewById(R.id.tv_loadinfo_home);

        bak_json=getContext().getSharedPreferences(getResources().getString(R.string.bak_json), Context.MODE_PRIVATE);
        edit_bak_json=bak_json.edit();

        backgroundTask=new BackgroundTask();
        backgroundTask.execute();




        return  rootView;
    }

    class BackgroundTask extends AsyncTask<Void,Void,Void>{
        String dataObtained;

        @Override
        protected void onCancelled() {
            super.onCancelled();
            prog_frag_home.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog_frag_home.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataObtained=DataHandlers.getContent(DataHandlers.major_home_api);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jsonObject= new JSONObject(dataObtained);
                JSONArray mainArr = jsonObject.getJSONArray("data");
                rv_main_home.setAdapter(new OuterAdap(mainArr));
            }catch (Exception e){
                e.printStackTrace();
            }


            tv_loadinfo_home.setVisibility(View.GONE);
            try {
                prog_frag_home.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

//    class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
//        int size;
//        String url_base;
//        JSONArray jsonArr;
//        String jsonHead;
//        Boolean isSeries=false;
//        Boolean isLoaded=true;
//
//        RecAdapter(Boolean isLoaded){
//            this.isLoaded = isLoaded;
//            size=20;
//        }
//
//
//        RecAdapter(JSONArray jsonArr,String url_base){
//         this.jsonArr=jsonArr;
//         this.url_base=url_base;
//         this.size=jsonArr.length();
//
//        }
//
//        RecAdapter(JSONArray jsonArr,String url_base,Boolean isSeries){
//            this.jsonArr=jsonArr;
//            this.url_base=url_base;
//            this.size=20;
//            this.isSeries=isSeries;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_images,parent,false);
//            ViewHolder holder = new ViewHolder(view);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            if(isLoaded){
//            try {
//                String subH="";
//                JSONObject tempObj = jsonArr.getJSONObject(position);
//                String timestamp=tempObj.getString("timestamp");
////                JSONObject tempObj = posObj.getJSONObject(jsonHead);
//                String img_url = url_base+DataHandlers.completeDecrypt(tempObj.getString("thumbnail"),timestamp);
//                if (img_url.equals(url_base+"")){
//                    img_url = url_base+DataHandlers.completeDecrypt(tempObj.getString("icon_url"),timestamp);
//                }
//
//                final String title;
//
//                if (isSeries){
//                    title = DataHandlers.completeDecrypt(tempObj.getString("series_name"),timestamp);
//                }
//                else
//                    title = DataHandlers.completeDecrypt(tempObj.getString("film_name"),timestamp);
//                final String timing = tempObj.getString("timing");
//                final String quality = tempObj.getString("quality");
//                final String video_id=tempObj.getString("video_id");
//                holder.tv_quality_rv_images.setText(quality);
//                if (quality.equals(""))
//                    holder.tv_quality_rv_images.setVisibility(View.INVISIBLE);
//                else
//                    subH+=quality+DataHandlers.DOT;
//                if (timing.equals(""))
//                    holder.tv_timing_rv_images.setVisibility(View.INVISIBLE);
//                else
//                    subH+=timing;
//                holder.tv_timing_rv_images.setText(subH);
//
////                RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.dummy_img).centerCrop();
////                Glide.with(getActivity()).load(img_url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.img_main_rv_images);
//
//                Glide.with(getContext()).load(img_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_main_rv_images);
//
//                holder.tv_moviename_rv_images.setText(title);
//
//                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //------Animation-----------//
//                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
//                        animation1.setDuration(500);
//                        view.startAnimation(animation1);
//                        //-------------------------//
//
//
//                            Intent intent = new Intent(getContext(),InfoPage.class);
//                            intent.putExtra("HEADING",title);
//                            intent.putExtra("SUB_HEAD",timing+" • "+quality);
//                            intent.putExtra("IS_FIRST_TIME",isFirstTime);
//                            if (isSeries){
//                                intent.putExtra("DATA_TYPE","SERIES");
//                                intent.putExtra("MAIN_API_LINK",DataHandlers.series_server1+"?video_id="+video_id);
//                            }
//                            else{
//                                intent.putExtra("DATA_TYPE","MOVIE");
//                                intent.putExtra("MAIN_API_LINK",DataHandlers.moviesServer1+"?video_id="+video_id);
//                            }
//                            startActivity(intent);
//
//
//
//                    }
//                });
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            }
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return size;
//        }
//
//
//        class ViewHolder extends RecyclerView.ViewHolder{
//            ConstraintLayout parentLayout;
//            ImageView img_main_rv_images;
//            TextView tv_moviename_rv_images,tv_timing_rv_images,tv_quality_rv_images;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                img_main_rv_images=itemView.findViewById(R.id.img_main_rv_images);
//                parentLayout = itemView.findViewById(R.id.rv_images);
//                tv_moviename_rv_images=itemView.findViewById(R.id.tv_moviename_rv_images);
//                tv_timing_rv_images=itemView.findViewById(R.id.tv_timing_rv_images);
//                tv_quality_rv_images = itemView.findViewById(R.id.tv_quality_rv_images);
//            }
//        }
//    }

    class OuterAdap extends RecyclerView.Adapter<OuterAdap.ViewHolder>{
        int size;
        JSONArray mainArr;

        OuterAdap(JSONArray mainArr){
            this.mainArr=mainArr;
            size=mainArr.length();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_main_home,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_head_viu1.setTextColor(Color.rgb(202,33,40));
            try {
                JSONObject jsonObject = mainArr.getJSONObject(position);
                String name = jsonObject.getString("name"),
                        type=jsonObject.getString("type"),
                        content=jsonObject.getString("content");
                holder.tv_head_viu1.setText(name);
                if (type.equals("1")){
                    holder.rec_viu_out.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                }else if (type.equals("2")){
                    holder.rec_viu_out.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                }

                holder.rec_viu_out.setAdapter(new InnerAdap(new JSONArray(content),type));
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
            TextView tv_head_viu1;
            RecyclerView rec_viu_out;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                parentLayout=itemView.findViewById(R.id.viu_out);
                tv_head_viu1=itemView.findViewById(R.id.tv_head_viu1);
                rec_viu_out=itemView.findViewById(R.id.rec_viu_out);
            }
        }
    }

    class InnerAdap extends RecyclerView.Adapter<InnerAdap.ViewHolder>{
        int size;
        JSONArray mainArr;
        String type;
        InnerAdap(JSONArray mainArr,String type){
            this.type=type;
            this.mainArr=mainArr;
            size=mainArr.length();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (type.equals("1")){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circular_template2,parent,false);
            }else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circular_template1,parent,false);
            }
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                final JSONObject jsonObject =mainArr.getJSONObject(position);
//                Log.i("BOOM","p  "+jsonObject);
                final String category_name=DataHandlers.capitalize(jsonObject.getString("category_name")),
                        icon_url=jsonObject.getString("icon_url"),
                        thumbnail=jsonObject.getString("thumbnail"),
                        api_url=jsonObject.getString("api_url");

                holder.tv_cir_temp1.setText(category_name);
                Glide.with(getContext()).load(thumbnail).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_cir_temp1);

                if (type.equals("1")){
                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //------Animation-----------//
                            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                            animation1.setDuration(500);
                            view.startAnimation(animation1);
                            //-------------------------//
                            try {
                                String sub_api = jsonObject.getString("sub_api");
                                Intent intent = new Intent(getContext(),DispMoreCategories1.class);
                                intent.putExtra("MAIN_API_LINK",api_url);
                                intent.putExtra("SUB_API_LINK",sub_api);
                                intent.putExtra("TOP_HEADING",category_name);
                                intent.putExtra("JSON_BACKUP_KEY",getResources().getString(R.string.json_allmovies)+category_name);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }else if (type.equals("2")){
                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //------Animation-----------//
                            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                            animation1.setDuration(500);
                            view.startAnimation(animation1);
                            //-------------------------//

                            Intent intent = new Intent(getContext(),premium_viu1.class);
                            intent.putExtra("URL",api_url);
                            intent.putExtra("HEADER",category_name);
                            startActivity(intent);
                        }
                    });
                }

            }catch (Exception e){
                Log.i("BOOM","p  "+e);
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return size;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            ConstraintLayout parentLayout;
            ImageView img_cir_temp1;
            TextView tv_cir_temp1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                if (type.equals("1"))
                    parentLayout = itemView.findViewById(R.id.circular_template2);
                else
                    parentLayout = itemView.findViewById(R.id.circular_template1);
                img_cir_temp1 = itemView.findViewById(R.id.img_cir_temp1);
                tv_cir_temp1 = itemView.findViewById(R.id.tv_cir_temp1);
            }
        }
    }

    public static String capitalize(String str)
    {
        if(str == null) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
