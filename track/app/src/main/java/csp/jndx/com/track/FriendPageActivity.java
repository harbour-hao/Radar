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

import static java.security.AccessController.doPrivileged;
import static java.security.AccessController.getContext;

public class FriendPageActivity extends AppCompatActivity implements ListAdapter.InnerItemOnclickListener,OnItemClickListener {
    private String TAG="freindpage";
    private ListView listView;
    private ArrayList<person> contentlist=new ArrayList<>();
    private ListAdapter adapter;
    private Listoperate operate=new Listoperate();
    private HashMap<String,ArrayList<person>> AllDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        //缓存加载
        this.AllDate=operate.load(getBaseContext());
        if (AllDate==null)
            AllDate=new HashMap<String, ArrayList<person>>();
        if(AllDate.get("friend")!=null)
              this.contentlist=AllDate.get("friend");
        listView=(ListView)findViewById(R.id.lvw_friends_list);
        adapter = new ListAdapter(this,this.contentlist);
        adapter.setOnInnerItemOnClickListener(this);
        listView =(ListView)this.findViewById(R.id.lvw_friends_list);
        listView.setAdapter(adapter);
        Log.d(TAG, "test");
        //listview的点击事件
        listView.setOnItemClickListener(this);


        //添加
        final Button addbtn=(Button)findViewById(R.id.btn_friends_list_add);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog addDialog=new AlertDialog.Builder(FriendPageActivity.this,R.style.Mydialog).create();//创建自定义对话框，默认会遮住整个页面
                addDialog.show();

                //窗口内容绑定
                addDialog.getWindow().setContentView(R.layout.dialog_add_friend);//整个窗口内容看作一个新页面
                addDialog.setCancelable(true);//返回键返回
                //点击取消
                addDialog.getWindow().findViewById(R.id.btn_dialog_close)
                        .setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                addDialog.dismiss();
                            }
                        });
                final EditText nameedit=addDialog.getWindow().findViewById(R.id.txt_friend_name);
                final EditText numedit=addDialog.getWindow().findViewById(R.id.txt_friend_number);
                //添加
                addDialog.getWindow().findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(nameedit.getText())||TextUtils.isEmpty(nameedit.getText()))
                        Toast.makeText(getApplicationContext(), "请填写名字和电话", Toast.LENGTH_SHORT).show();
                        else {
                            person man=new person();
                            man.setName(nameedit.getText().toString());
                            man.setPhone(numedit.getText().toString());
                            adapter.addItem(man);
                            adapter.notifyDataSetChanged();
                            changesave();//addItem改变的contentlist指向同一个内存
                            addDialog.dismiss();
                        }
                    }
                });
                //删除
            }
        });
    }
    //点击listview内的组件点击绑定
    @Override
    public void itemClick(View v) {
        final int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.delete_button_cell:
                final AlertDialog deleteDialog=new AlertDialog.Builder(FriendPageActivity.this,R.style.Mydialog).create();//创建自定义对话框，默认会遮住整个页面
                deleteDialog.show();
                //窗口内容绑定
                deleteDialog.getWindow().setContentView(R.layout.dialog_delete);//整个窗口内容看作一个新页面
                deleteDialog.setCancelable(true);//返回键返回
                //点击窗口之外可取消
                deleteDialog.getWindow().findViewById(R.id.btn_dialog_close)
                        .setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                deleteDialog.dismiss();
                            }
                        });
                TextView deletenumber=deleteDialog.getWindow().findViewById(R.id.delete_number);
                deletenumber.setText(contentlist.get(position).getPhone());
                deleteDialog.getWindow().findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            adapter.removeItem(position);
                            adapter.notifyDataSetChanged();
                            changesave();//addItem改变的contentlist指向同一个内存
                            deleteDialog.dismiss();
                    }
                });
            break;
            default:
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle= data.getExtras();
                    int status=bundle.getInt("status");
                    int id=bundle.getInt("position");
                    Log.d(TAG,"remove1:"+id);
                    if(status==1)
                    {
                        Log.d(TAG,"remove2:"+id);
                        adapter.removeItem(id);
                        adapter.notifyDataSetChanged();
                        changesave();//addItem改变的contentlist指向同一个内存
                    }

                    break;
                }
        }
    }
   //listview点击，记得把button设置焦点false
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        person selectcontent = contentlist.get(position);//获取选中的数据
        Intent intent = new Intent(FriendPageActivity.this, FriendDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("name", selectcontent.getName());
        bundle.putString("number", selectcontent.getPhone());
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    private void changesave()
    {
        AllDate.put("friend",contentlist);
        operate.save(getBaseContext(),AllDate);
    }
    }
