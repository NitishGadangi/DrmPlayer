package nitish.vidflix1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class LiveTvPage extends AppCompatActivity {

    RecyclerView rv_liveTv;
    RecyclerView.LayoutManager lm_liveTV;

    ProgressBar prog_liveTv;

    BGtask_serv bGtask_serv;

    int curPG= 2;

    ImageView liveTv_search,img_searchEt,imgClear_search,liveTv_logo;
    EditText et_search;
    TextView liveTvHead;
    Boolean inSearch=false;
    TextView tv_serv1_livTV,tv_serv2_livTV;

    SearchTask searchTask;

    long delay = 100; // 1 seconds after user stops typing
    long last_text_edit = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        bGtask_serv.cancel(true);
        super.onDestroy();
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
                startActivity(new Intent(getApplicationContext(),Search.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
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
        findViewById(R.id.img_down_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
//                startActivity(new Intent(getApplicationContext(),LiveTvPage.class));
//                finish();
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tv_page);

        btmButtons();

        rv_liveTv=findViewById(R.id.rv_liveTv);
        lm_liveTV= new GridLayoutManager(getApplicationContext(),2);
        rv_liveTv.setLayoutManager(lm_liveTV);

        prog_liveTv=findViewById(R.id.prog_liveTv);

        liveTv_search=findViewById(R.id.liveTv_search);
        img_searchEt=findViewById(R.id.img_searchEt);
        imgClear_search=findViewById(R.id.imgClear_search);
        liveTv_logo=findViewById(R.id.liveTv_logo);
        et_search=findViewById(R.id.et_search);
        liveTvHead=findViewById(R.id.liveTvHead);

        bGtask_serv=new BGtask_serv();

        tv_serv2_livTV=findViewById(R.id.tv_serv2_livTV);
        tv_serv1_livTV=findViewById(R.id.tv_serv1_livTV);

        server2();

        tv_serv1_livTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                view.setAlpha(1f);
                findViewById(R.id.tv_serv2_livTV).setAlpha(0.5f);
                if (curPG!=1)
                    server1();
            }
        });
        tv_serv2_livTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                view.setAlpha(1f);
                findViewById(R.id.tv_serv1_livTV).setAlpha(0.5f);
                if (curPG!=2)
                    server2();
            }
        });

        final SwipeRefreshLayout swipe=findViewById(R.id.swipe_liveTv);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!inSearch){
                    if (curPG==1)
                        server1();
                    else if (curPG==2)
                        server2();

                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            // Stop animation (This will be after 3 seconds)
                            swipe.setRefreshing(false);
                        }
                    }, 3000);
                }else
                    swipe.setRefreshing(false);
            }
        });

        liveTv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inSearch){
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(500);
                    view.startAnimation(animation1);
                    //-------------------------//
                    inSearch=false;
                    liveTv_logo.setImageDrawable(getResources().getDrawable(R.drawable.main_logo));
                    liveTv_search.setVisibility(View.VISIBLE);
                    liveTvHead.setVisibility(View.VISIBLE);
                    tv_serv2_livTV.setVisibility(View.VISIBLE);
                    tv_serv1_livTV.setVisibility(View.VISIBLE);
                    et_search.setVisibility(View.GONE);

                    if (curPG==1)
                        server1();
                    else if (curPG==2)
                        server2();
                }
            }
        });

        liveTv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inSearch= true;
                liveTv_logo.setImageDrawable(getResources().getDrawable(R.drawable.left_arrow));
                liveTv_search.setVisibility(View.GONE);
                liveTvHead.setVisibility(View.GONE);
                tv_serv2_livTV.setVisibility(View.GONE);
                tv_serv1_livTV.setVisibility(View.GONE);
                et_search.setVisibility(View.VISIBLE);
            }
        });

        searchTask=new SearchTask();

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

        rv_liveTv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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



    }

    void server1(){
        curPG=1;
        bGtask_serv.cancel(true);
        bGtask_serv=new BGtask_serv();
        bGtask_serv.execute("1");

    }

    void server2(){
        curPG=2;
        bGtask_serv.cancel(true);
        bGtask_serv=new BGtask_serv();
        bGtask_serv.execute("2");

    }

    class SearchTask extends AsyncTask<String,Void,Void>{
        String res="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.nomatch).setVisibility(View.GONE);
            rv_liveTv.setVisibility(View.GONE);
            prog_liveTv.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            res=DataHandlers.getContent(DataHandlers.liveTV_search+strings[0]);
            res=DataHandlers.decryptMain(res);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog_liveTv.setVisibility(View.GONE);
            if (!res.contains("\"status\":\"false\"")){
                try{
                    JSONArray jsonArray=new JSONArray(res);
                    rv_liveTv.setAdapter(new SearchAdapter(jsonArray));
                    rv_liveTv.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    findViewById(R.id.nomatch).setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
                rv_liveTv.setVisibility(View.VISIBLE);
            }else {
                rv_liveTv.setVisibility(View.GONE);
                findViewById(R.id.nomatch).setVisibility(View.VISIBLE);
            }

        }
    }

    class BGtask_serv extends AsyncTask<String,Void,Void>{
        String serv_data="FAILED";
        String servNum="1";
        @Override
        protected void onPreExecute() {
            rv_liveTv.setVisibility(View.GONE);
            prog_liveTv.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            servNum=strings[0];
            if (strings[0].equals("1")){
                serv_data=DataHandlers.getContent(DataHandlers.liveTV_serv1);
            }
            else if (strings[0].equals("2"))
                serv_data=DataHandlers.getContent(DataHandlers.liveTV_serv2);
            serv_data=DataHandlers.decryptMain(serv_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rv_liveTv.setVisibility(View.VISIBLE);
            prog_liveTv.setVisibility(View.GONE);
            try{
                JSONArray jsonArray=new JSONArray(serv_data);
                if (servNum.equals("1"))
                    rv_liveTv.setAdapter(new RecAdapter_serv1(jsonArray));
                else if (servNum.equals("2"))
                    rv_liveTv.setAdapter(new RecAdapter_serv2(jsonArray));
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }

    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
        JSONArray mainArr;
        int size=20;


        SearchAdapter(JSONArray mainArr){
            this.mainArr=mainArr;
            this.size=mainArr.length();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livetv_template1,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try{
                final JSONObject temp_tempObj=mainArr.getJSONObject(position);
                String serverNum=temp_tempObj.getString("server");
                String timestamp=temp_tempObj.getString("timestamp");
                final JSONObject tempObj=new JSONObject(DataHandlers.completeDecrypt(temp_tempObj.getString("data"),timestamp));

                if (serverNum.equals("2")){
                String name = tempObj.getString("name");
                String thumbnail = DataHandlers.decryptData(tempObj.getString("thumbnail"));
                holder.tv_tvName.setText(name);
                Glide.with(getApplicationContext()).load(thumbnail).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_tvLogo);

                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //------Animation-----------//
                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(500);
                        view.startAnimation(animation1);
                        //-------------------------//
                        new BottomSheetLO(tempObj,1,getApplicationContext()).show(getSupportFragmentManager(),"LiveTv Sheet");

                    }
                });
                }else if (serverNum.equals("1")){
                    final String name = tempObj.getString("name"),
                            logo_url=DataHandlers.liveTV_2img_baseurl+tempObj.getString("logo");

                    holder.tv_tvName.setText(name);
                    Glide.with(getApplicationContext()).load(logo_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_tvLogo);

                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //------Animation-----------//
                            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                            animation1.setDuration(500);
                            view.startAnimation(animation1);
                            //-------------------------//
                            new BottomSheetLO(tempObj,2,getApplicationContext()).show(getSupportFragmentManager(),"LiveTv Sheet");

                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.i("BAKUP_TEST",position+"m"+e);
            }

        }

        @Override
        public int getItemCount() {
            return size;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            ConstraintLayout parentLayout;
            TextView tv_tvName;
            ImageView img_tvLogo;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                parentLayout = itemView.findViewById(R.id.livetv_template1);
                img_tvLogo=itemView.findViewById(R.id.img_tvLogo);
                tv_tvName=itemView.findViewById(R.id.tv_tvName);
            }
        }
    }

    class RecAdapter_serv1 extends RecyclerView.Adapter<RecAdapter_serv1.ViewHolder>{
        JSONArray mainArr;
        int size=20;


        RecAdapter_serv1(JSONArray mainArr){
            this.mainArr=mainArr;
            this.size=mainArr.length();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livetv_template1,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try{
                final JSONObject tempObj=mainArr.getJSONObject(position);
//                String timestamp=tempObj.getString("timestamp");
//                String name = DataHandlers.completeDecrypt(tempObj.getString("name"),timestamp);
                String name = tempObj.getString("name");
//                String hlsurl = DataHandlers.completeDecrypt(tempObj.getString("hlsurl"),timestamp);

                String thumbnail = DataHandlers.decryptData(tempObj.getString("thumbnail"));
//                thumbnail= DataHandlers.completeDecrypt(thumbnail,timestamp);

                holder.tv_tvName.setText(name);
                Glide.with(getApplicationContext()).load(thumbnail).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_tvLogo);

//                JSONArray jsonArray = new JSONArray(hlsurl);
//                Log.i("BAKUP_TEST","m"+jsonArray);
//                hlsurl=jsonArray.getJSONObject(0).getString("url");

//                final String finalHlsurl = hlsurl;
//                final String finalName = name;
//                final String finalThumbnail = thumbnail;
                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //------Animation-----------//
                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(500);
                        view.startAnimation(animation1);
                        //-------------------------//

//                        Intent mIntent = StreamPlayer.getStartIntent(getApplicationContext(), finalHlsurl,"LiveTV : "+ finalName, finalThumbnail);
////                        startActivity(mIntent);

                        new BottomSheetLO(tempObj,1,getApplicationContext()).show(getSupportFragmentManager(),"LiveTv Sheet");

                    }
                });

            }catch (Exception e){
                e.printStackTrace();
                Log.i("BAKUP_TEST",position+"m"+e);
            }

        }

        @Override
        public int getItemCount() {
            return size;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            ConstraintLayout parentLayout;
            TextView tv_tvName;
            ImageView img_tvLogo;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                parentLayout = itemView.findViewById(R.id.livetv_template1);
                img_tvLogo=itemView.findViewById(R.id.img_tvLogo);
                tv_tvName=itemView.findViewById(R.id.tv_tvName);
            }
        }
    }

    class RecAdapter_serv2 extends RecyclerView.Adapter<RecAdapter_serv2.ViewHolder>{
        JSONArray mainArr;
        int size=20;


        RecAdapter_serv2(JSONArray mainArr){
            this.mainArr=mainArr;
            this.size=mainArr.length();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livetv_template1,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try{
                final JSONObject tempObj=mainArr.getJSONObject(position);
//                String timestamp=tempObj.getString("timestamp");
                final String name = tempObj.getString("name"),
                        logo_url=DataHandlers.liveTV_2img_baseurl+tempObj.getString("logo"),
                        broadcasterId=tempObj.getString("broadcasterId");


                holder.tv_tvName.setText(name);
                Glide.with(getApplicationContext()).load(logo_url).centerCrop().placeholder(R.drawable.dummy_img).into(holder.img_tvLogo);

                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //------Animation-----------//
                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(500);
                        view.startAnimation(animation1);
                        //-------------------------//

//                        Intent mIntent = StreamPlayer.getStartIntent(getApplicationContext(), stream_link,"LiveTV : "+ name,logo_url);
//                        startActivity(mIntent);

                        new BottomSheetLO(tempObj,2,getApplicationContext()).show(getSupportFragmentManager(),"LiveTv Sheet");

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
            TextView tv_tvName;
            ImageView img_tvLogo;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                parentLayout = itemView.findViewById(R.id.livetv_template1);
                img_tvLogo=itemView.findViewById(R.id.img_tvLogo);
                tv_tvName=itemView.findViewById(R.id.tv_tvName);
            }
        }
    }

    public static class BottomSheetLO extends BottomSheetDialogFragment{
        JSONObject mainObj;
        int server;
        Context context;

        public BottomSheetLO(JSONObject mainObj, int server, Context context) {
            Log.i("TVSET","s  "+mainObj);
            this.mainObj=mainObj;
            this.server=server;
            this.context=context;
        }

        @Override
        public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme2);
        }

        @androidx.annotation.Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.live_tv_sheet,container,false);

            TextView sheet_tvDes=rootView.findViewById(R.id.sheet_tvDes),
                    sheet_tvHead=rootView.findViewById(R.id.sheet_tvHead);
            ImageView sheet_img_head=rootView.findViewById(R.id.sheet_img_head);

            RecyclerView rv_tv_sheet_qual =rootView.findViewById(R.id.rv_tv_sheet_qual);
            rv_tv_sheet_qual.setLayoutManager(new LinearLayoutManager(getContext()));
            try {
                if (server==1){
//                    String timestamp=mainObj.getString("timestamp");
                    String name = mainObj.getString("name");
                    String hlsurl = mainObj.getString("hlsurl");
                    String shortDesc = DataHandlers.decryptData(mainObj.getString("shortDesc"));
                    String thumbnail = DataHandlers.decryptData(mainObj.getString("thumbnail"));



                    sheet_tvHead.setText(name);
                    sheet_tvDes.setText(shortDesc);
                    Glide.with(context).load(thumbnail).centerCrop().placeholder(R.drawable.dummy_img).into(sheet_img_head);

                    rv_tv_sheet_qual.setAdapter(new ExpAdapter(new JSONArray(hlsurl),name,thumbnail));

                }
                if (server==2){
//                    String timestamp=mainObj.getString("timestamp");
                    final String name = mainObj.getString("name"),
                            str_qualities=mainObj.getString("qualities"),
                            logo_url=DataHandlers.liveTV_2img_baseurl+mainObj.getString("logo"),
                            broadcasterId=mainObj.getString("broadcasterId");

                    rv_tv_sheet_qual.setAdapter(new ExpAdapter(new JSONArray(str_qualities),broadcasterId,name,logo_url));
                    sheet_tvHead.setText(name);
                    sheet_tvDes.setVisibility(View.GONE);
                    Glide.with(context).load(logo_url).centerCrop().placeholder(R.drawable.dummy_img).into(sheet_img_head);
                }
            }catch (Exception e){
                e.printStackTrace();
            }




            return rootView;
        }


        class ExpAdapter extends RecyclerView.Adapter<ExpAdapter.ViewHolder>{
            int size=5;
            int server;
            JSONArray jsonArray;

            String broadcasterId;



            String header;
            String img_url;


            ExpAdapter(JSONArray jsonArray,String header,String img_url){
                this.jsonArray=jsonArray;
                server=1;
                size=jsonArray.length();
                Log.i("ENC_TEST1",jsonArray.toString());
                this.header=header;
                this.img_url=img_url;
            }
            ExpAdapter(JSONArray jsonArray,String broadcasterId,String header,String img_url){
                this.jsonArray=jsonArray;
                size=jsonArray.length();
                server=2;
                this.broadcasterId=broadcasterId;
                this.header=header;
                this.img_url=img_url;
                Log.i("ENC_TEST2",jsonArray.toString());
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
                    if (server==1){
                        JSONObject tempObj=jsonArray.getJSONObject(position);
                        String qual=tempObj.getString("qual");
                        final String url=tempObj.getString("url");
                        holder.tv_head.setText(qual);

                        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //------Animation-----------//
                                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                animation1.setDuration(500);
                                view.startAnimation(animation1);
                                //-------------------------//

                                Intent mIntent = StreamPlayer.getStartIntent(context, url,"LiveTV : "+ header, img_url);
                                startActivity(mIntent);
                                dismiss();
                            }
                        });

                    }
                    if (server==2){
                        JSONObject tempObj=jsonArray.getJSONObject(position);
                        String qual=tempObj.getString("qual");
                        final String url=tempObj.getString("url")+"&broadcasterId="+broadcasterId;
                        holder.tv_head.setText(qual);

                        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //------Animation-----------//
                                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                                animation1.setDuration(500);
                                view.startAnimation(animation1);
                                //-------------------------//

                                Intent mIntent = StreamPlayer.getStartIntent(context, url,"LiveTV : "+ header, img_url);
                                startActivity(mIntent);
                                dismiss();

                            }
                        });
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
                TextView tv_head;


                public ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    parentLayout = itemView.findViewById(R.id.text_list_template1);
                    tv_head=itemView.findViewById(R.id.tv_text_list1);

                }
            }
        }
    }

}
