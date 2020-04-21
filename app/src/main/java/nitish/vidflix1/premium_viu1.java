package nitish.vidflix1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONObject;

public class premium_viu1 extends AppCompatActivity {

    ProgressBar prog_viu1;
    RecyclerView rec_main_viu1;
    TextView tv_head_viu1;

    ViuBg viuBg;


    @Override
    public void onBackPressed() {
        viuBg.cancel(true);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_viu1);

        String url = getIntent().getStringExtra("URL");
        String header = getIntent().getStringExtra("HEADER");

        prog_viu1 = findViewById(R.id.prog_viu1);
        rec_main_viu1=findViewById(R.id.rec_main_viu1);
        rec_main_viu1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tv_head_viu1=findViewById(R.id.tv_head_viu1);

        tv_head_viu1.setText(header);

        findViewById(R.id.viu_bak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
                onBackPressed();
            }
        });

        viuBg=new ViuBg(url);
        viuBg.execute();
    }



    class ViuBg extends AsyncTask<Void,Void,Void>{
        String jsonData;
        String url;

        ViuBg(String url){
            this.url=url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            jsonData=DataHandlers.getContent(url);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            prog_viu1.setVisibility(View.INVISIBLE);
            super.onPostExecute(aVoid);
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String arr = jsonObject.getString("content");
                rec_main_viu1.setAdapter(new OuterAdap(new JSONArray(arr)));
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viu_out,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                JSONObject mainObj=mainArr.getJSONObject(position);
                JSONArray subArr = new JSONArray();
                String cat_name = mainObj.getString("cat_name");
                if (!cat_name.equals("")){
                    subArr = mainObj.getJSONArray("content");
                    holder.rec_viu_out.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    holder.rec_viu_out.setAdapter(new InnerAdap(subArr));
                    holder.tv_head_viu1.setText(cat_name);

                }
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
        InnerAdap(JSONArray mainArr){
            this.mainArr=mainArr;
            size=mainArr.length();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_template2,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                JSONObject mainObj=mainArr.getJSONObject(position);
                String name = mainObj.getString("name");
                String id = mainObj.getString("id");
                String poster_url= mainObj.getString("poster_url");
                String in_list = mainObj.getString("in_list");

                holder.tv_moviename_rv_images.setText(name);
                holder.tv_moviename_rv_images.setMaxLines(1);
                holder.tv_timing_rv_images.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(poster_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_main_rv_images);

                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //------Animation-----------//
                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(1000);
                        view.startAnimation(animation1);
                        //-------------------------//


                    }
                });

            }catch (Exception e){
                Log.i("BABYY","k "+e );
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return size;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            ConstraintLayout parentLayout;
            ImageView img_main_rv_images;
            TextView tv_moviename_rv_images,tv_timing_rv_images;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_main_rv_images=itemView.findViewById(R.id.img_main_gridtemp2);
                parentLayout = itemView.findViewById(R.id.grid_template2);
                tv_moviename_rv_images=itemView.findViewById(R.id.tv_head_search);
                tv_timing_rv_images=itemView.findViewById(R.id.tv_subHead_search);
            }
        }
    }
}
