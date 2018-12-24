package csp.jndx.com.track;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FriendDetailActivity extends AppCompatActivity {
    private String name;
    private String number;
    private  int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail);
        Bundle bundle;
        bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        number=bundle.getString("number");
        position=bundle.getInt("position");
        TextView nameTextview=(TextView)findViewById(R.id.txt_friend_name);
        nameTextview.setText(name);
        TextView numTextview=(TextView)findViewById(R.id.txt_friend_number);
        numTextview.setText(number);

        Button DoneButton = (Button) findViewById(R.id.btn_radar);
        DoneButton.setOnClickListener(new View.OnClickListener()
          {
            @Override
            public void onClick(View v) {
                Log.d("freindpage","position"+position);
                Intent intent=new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 0);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                Log.d("freindpage",""+bundle.getInt("status"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
