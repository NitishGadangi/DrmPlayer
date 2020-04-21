package nitish.vidflix1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search extends AppCompatActivity {

    RecyclerView rv_search;
    RecyclerView.LayoutManager lm_search;

    ProgressBar prog_search;
    EditText et_search;

    SearchTask searchTask;

    long delay = 100; // 1 seconds after user stops typing
    long last_text_edit = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
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
                onBackPressed();
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
//                startActivity(new Intent(getApplicationContext(),Search.class));
//                finish();
//                overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
            }
        });
        findViewById(R.id.img_more_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
                startActivity(new Intent(getApplicationContext(),MorePage.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //LiveTv
        findViewById(R.id.img_down_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
                startActivity(new Intent(getApplicationContext(),LiveTvPage.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

            btmButtons();

//        FragHome.backgroundTask.cancel(true);
//        FragMovies.backgroundTask.cancel(true);
//        FragSeries.backgroundTask.cancel(true);


        et_search=findViewById(R.id.et_search);
        prog_search=findViewById(R.id.prog_search);

        rv_search=findViewById(R.id.rv_search);
//        lm_search=new GridLayoutManager(getApplicationContext(),3);
//        RecAdapter recAdapter=new RecAdapter(false);
//        rv_search.setAdapter(recAdapter);
//        rv_search.setLayoutManager(lm_search);
        rv_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("BAKUP_TEST","m"+newState);
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }


        });


        searchTask=new SearchTask();

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                        searchTask.cancel(true);
                        searchTask=new SearchTask();
                        searchTask.execute(et_search.getText().toString().replace(" ","%20"));

                    return true;
                }
                return false;
            }
        });

        //---------------AutoSearch-----------------------------//

        final Handler handler = new Handler();
        final Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + delay - 50)) {
                        searchTask.cancel(true);
                        searchTask=new SearchTask();
                        searchTask.execute(et_search.getText().toString().replace(" ","%20"));
                }
            }
        };


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);



            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                }
            }
        });

//-------------------------End of AutoSearch---------------------------------//

        findViewById(R.id.imgClear_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//
                et_search.setText("");
            }
        });

        findViewById(R.id.img_searchEt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//
                et_search.callOnClick();
            }
        });



    }

    class SearchTask extends AsyncTask<String,Void,Void>{
        String jsonSrc="FAILED";

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Log.i("SEARCH__","Cancelled;;");
        }

        @Override
        protected void onPreExecute() {
            prog_search.setVisibility(View.VISIBLE);
            Log.i("SEARCH__","Started;;");
            findViewById(R.id.dunb_info1).setVisibility(View.GONE);
            findViewById(R.id.dunb_info2).setVisibility(View.GONE);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            jsonSrc=DataHandlers.getSearch(strings[0]);
            jsonSrc=DataHandlers.decryptMain(jsonSrc);
            Log.i("SEARCH__","Background;;");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("SEARCH__","src: "+jsonSrc);
            prog_search.setVisibility(View.GONE);
            if (!jsonSrc.contains("null")){
                rv_search.setVisibility(View.VISIBLE);
                findViewById(R.id.tv_noRes).setVisibility(View.GONE);
                try {
                    JSONArray jsonArray=new JSONArray(jsonSrc);
                    lm_search=new GridLayoutManager(getApplicationContext(),3);
                    RecAdapter recAdapter=new RecAdapter(jsonArray, DataHandlers.base_url);
                    rv_search.setAdapter(recAdapter);
                    rv_search.setLayoutManager(lm_search);

                } catch (JSONException e) {
                    Log.i("SEARCH__","Catch1;;");
                    e.printStackTrace();
                }
            }else {
                rv_search.setVisibility(View.GONE);
                findViewById(R.id.tv_noRes).setVisibility(View.VISIBLE);
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
            this.size=10;
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
            if (isLoaded) {
                try {
                    JSONObject tempObj = jsonArr.getJSONObject(position);
                    final String quality=tempObj.getString("quality"),
                            timestamp=tempObj.getString("timestamp"),
                            icon_url=url_base+DataHandlers.completeDecrypt(tempObj.getString("thumbnail"),timestamp),
                            film_name=DataHandlers.completeDecrypt(tempObj.getString("film_name"),timestamp),
                            video_id=tempObj.getString("video_id"),
                            server=tempObj.getString("server");
                    final String subH=DataHandlers.capitalize(server.substring(0,server.indexOf("_")))+DataHandlers.DOT+quality;
                    holder.tv_head.setText(film_name);
                    holder.tv_subHead.setText(subH);
                    Log.i("ENC_TEST","a  "+icon_url);
                    Log.i("ENC_TEST","b  "+tempObj.getString("thumbnail"));
                    Log.i("ENC_TEST","c  "+timestamp);
//                    RequestOptions requestOptions = RequestOptions.placeholderOf(R.drawable.dummy_img).centerCrop();
//
//                    Glide.with(getApplicationContext()).load(icon_url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.img_main_rv_images);

                    Glide.with(getApplicationContext()).load(icon_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_main_rv_images);

                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //------Animation-----------//
                            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                            animation1.setDuration(500);
                            view.startAnimation(animation1);
                            //-------------------------//
                            String serverUrl=DataHandlers.getServerUrl(server,video_id);
                            if (!serverUrl.equals("FAILED")){
                                Intent intent = new Intent(getApplicationContext(),InfoPage.class);
                                intent.putExtra("MAIN_API_LINK",serverUrl);
                                intent.putExtra("HEADING",film_name);
                                intent.putExtra("SUB_HEAD",subH);
                                intent.putExtra("IS_FIRST_TIME",false);
                                if (serverUrl.contains("movie"))
                                    intent.putExtra("DATA_TYPE","MOVIE");
                                else
                                    intent.putExtra("DATA_TYPE","SERIES");
                                startActivity(intent);
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("SEARCH__","Catch1;;");
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
            TextView tv_head,tv_subHead;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img_main_rv_images=itemView.findViewById(R.id.img_main_gridtemp2);
                parentLayout = itemView.findViewById(R.id.grid_template2);
                tv_head=itemView.findViewById(R.id.tv_head_search);
                tv_subHead=itemView.findViewById(R.id.tv_subHead_search);


            }
        }
    }
}
