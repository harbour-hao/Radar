package csp.jndx.com.track;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.HashMap;

public class EnermyPageActivity extends AppCompatActivity  {
    private String TAG="enermypage";
    private ListView listView;
    private ArrayList<person> contentlist=new ArrayList<>();
    private EListAdapter adapter;
    private Listoperate operate=new Listoperate();
    private HashMap<String,ArrayList<person>> AllDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enemies_list);
        //缓存加载
        this.AllDate = operate.load(getBaseContext());
        if (AllDate == null)
            AllDate = new HashMap<String, ArrayList<person>>();
        if (AllDate.get("enermy") != null)
            this.contentlist = AllDate.get("enermy");
        listView = (ListView) findViewById(R.id.lvw_enemies_list);
        adapter = new EListAdapter(this, this.contentlist);
        listView.setAdapter(adapter);

        changesave();
    }

    private void changesave()
    {
        AllDate.put("enermy",contentlist);
        operate.save(getBaseContext(),AllDate);
    }
}
