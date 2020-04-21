package nitish.vidflix1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Premium_List extends AppCompatActivity {

    String api_url;
    String pagenation;
    String header;

    TextView tv_head_viu1;
    RecyclerView rv_prem_list;
    ProgressBar prog_viu1;
    ProgressBar prog_cir_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium__list);

        Intent intent=getIntent();
        api_url=intent.getStringExtra("API_URL");
        pagenation=intent.getStringExtra("PAGENATION");
        header=intent.getStringExtra("HEADER");

        findViewById(R.id.viu_bak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_head_viu1=findViewById(R.id.tv_head_viu1);
        tv_head_viu1.setText(header);

        rv_prem_list=findViewById(R.id.rv_prem_list);
        prog_viu1=findViewById(R.id.prog_viu1);
        prog_cir_down=findViewById(R.id.prog_cir_down);




    }
}
