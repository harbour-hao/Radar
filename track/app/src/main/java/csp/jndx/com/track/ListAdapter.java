package csp.jndx.com.track;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter implements View.OnClickListener {
    private  String TAG = "ListAdapter";
    private List<person> contents = new ArrayList<>();
    private Context context;
    private InnerItemOnclickListener mListener;
    public ListAdapter(Context context, ArrayList<person> Listview) {
        this.context = context;
        this.contents = Listview;
    }

    //选择了新bar的数据刷新
    public void refreshdata(ArrayList<person> Listview)
    {
        this.contents = Listview;
        //Log.d(TAG,"refresh");
    }

    public void addItem(person man)
    {
        this.contents.add(man);
    }
    public void removeItem(int i)
    {
        this.contents.remove(i);
    }
    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Object getItem(int position) {
        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder=null;
        if (convertView==null)
        {
            viewholder=new Viewholder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.friends_list_item, null);
            viewholder.textView=(TextView)convertView.findViewById(R.id.name_cell);
            viewholder.button=(Button)convertView.findViewById(R.id.delete_button_cell);
            convertView.setTag(viewholder);
        }
        else
            viewholder=(Viewholder) convertView.getTag();
        viewholder.textView.setText(contents.get(position).getName());
        viewholder.button.setOnClickListener(this);
        viewholder.button.setTag(position);
        return convertView;
    }

    private class Viewholder
    {
        TextView textView;
        Button button;
    }
    //内部组件接口，使外部的不同view可以调用adapter的itemClick，目的是通过adapter识别不同view
    interface InnerItemOnclickListener {
        void itemClick(View v);
    }
    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener)
    {
        this.mListener=listener;
    }
    //OnClickListener中的重写
    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
        Log.d("test","点击了内部组件");
    }
}
