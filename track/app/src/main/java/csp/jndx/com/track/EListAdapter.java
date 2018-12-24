package csp.jndx.com.track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class EListAdapter  extends BaseAdapter{
    private  String TAG = "ListAdapter";
    private List<person> contents = new ArrayList<>();
    private Context context;
    private ListAdapter.InnerItemOnclickListener mListener;
    public EListAdapter(Context context, ArrayList<person> Listview) {
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
        EListAdapter.Viewholder viewholder=null;
        if (convertView==null)
        {
            viewholder=new EListAdapter.Viewholder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.enemies_list_item, null);
            viewholder.textView=(TextView)convertView.findViewById(R.id.name_cell);
            convertView.setTag(viewholder);
        }
        else
            viewholder=(EListAdapter.Viewholder) convertView.getTag();
        viewholder.textView.setText("haohong is a dog");
        return convertView;
    }

    private class Viewholder
    {
        TextView textView;
        Button button;
    }

}

