package com.canyapan.blog.taskmanager;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.canyapan.blog.taskmanager.R;

public class TaskListAdapter extends ArrayAdapter<TaskModel> {
	private int resource;

	public TaskListAdapter(Context context, int resource, ArrayList<TaskModel> tasks) {
		super(context, resource, tasks);

		setResource(resource);
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(getResource(), parent, false);
		} else {
			v = convertView;
		}

		setItem(v, getItem(position));

		return v;
	}

	private void setItem(View v, TaskModel item) {
		TextView textView = (TextView) v.findViewById(R.id.textViewTask);
		
		textView.setText(item.getText());
		if (item.isDone()) {
			textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
		}
		
		v.setTag(item.getId());
	}
}
