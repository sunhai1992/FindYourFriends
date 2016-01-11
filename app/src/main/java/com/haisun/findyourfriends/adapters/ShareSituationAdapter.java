package com.haisun.findyourfriends.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haisun.findyourfriends.R;

import java.util.List;


public class ShareSituationAdapter extends BaseAdapter {

	private int resource;
	private LayoutInflater inflater;
	List<String> situations;
	public ShareSituationAdapter(Context context,List<String> situations,int resource) {
		this.situations = situations;
		this.resource=resource;
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public ShareSituationAdapter(){}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){                                   //如果为空，则表明是第一页
			convertView=inflater.inflate(resource, null);        //生成条目对象
		}
			TextView nameView=(TextView)convertView.findViewById(R.id.name);
			TextView phoneView=(TextView)convertView.findViewById(R.id.exit);
			
			String str=situations.get(position);
			nameView.setText(position);
			phoneView.setText("退出");
			return convertView;
	} 
}
