package com.canyapan.blog.taskmanager;

import java.util.ArrayList;

import com.canyapan.blog.taskmanager.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class TaskListActivity extends ListActivity {
	ArrayList<TaskModel> tasks = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list_activity);

		if (null == tasks) {
			tasks = TaskModel.selectAllTasks(this);
		}

		setListAdapter(new TaskListAdapter(this, R.layout.task_list_item, tasks));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_clear_completed_tasks:
			ArrayList<TaskModel> removing = new ArrayList<TaskModel>();

			for (TaskModel t : tasks) {
				if (t.isDone()) {
					removing.add(t);
				}
			}

			try {
				TaskModel.deleteCompleted(this);

				tasks.removeAll(removing);

				((TaskListAdapter) getListAdapter()).notifyDataSetChanged();
			} catch (TaskDatabaseException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}

			return true;
		case R.id.action_add:
			final EditText editText = new EditText(this);
			new AlertDialog.Builder(this)
				.setTitle(R.string.add_new_task)
				.setView(editText)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						TaskModel task = new TaskModel(editText.getText().toString());
	
						try {
							task.insert(TaskListActivity.this);
	
							tasks.add(task);
	
							Toast.makeText(TaskListActivity.this, R.string.task_added, Toast.LENGTH_LONG).show();
	
							((TaskListAdapter) getListAdapter()).notifyDataSetChanged();
						} catch (TaskDatabaseException e) {
							tasks.remove(task);
	
							Log.e(this.getClass().getName(), e.getMessage());
						}
					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})
				.show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		TaskModel task = tasks.get(position);
		boolean isDone = task.isDone();

		task.setDone(!isDone);

		try {
			task.update(this);

			Toast.makeText(this, R.string.task_completed, Toast.LENGTH_LONG).show();

			((TaskListAdapter) getListAdapter()).notifyDataSetChanged();
		} catch (TaskDatabaseException e) {
			task.setDone(isDone);

			Log.e(this.getClass().getName(), e.getMessage());
		}
	}

	public void onDeleteTaskButtonClicked(View v) {
		Object tag = ((View) v.getParent().getParent()).getTag();

		if (null != tag) {
			long id = (long) tag;

			for (TaskModel t : tasks) {
				if (t.getId() == id) {
					try {
						t.delete(this);

						tasks.remove(t);

						Toast.makeText(this, R.string.task_removed, Toast.LENGTH_LONG).show();

						((TaskListAdapter) getListAdapter()).notifyDataSetChanged();
					} catch (TaskDatabaseException e) {
						Log.e(this.getClass().getName(), e.getMessage());
					}

					break;
				}
			}
		} else {
			Log.d(this.getClass().getName(), "Null tag.");
		}
	}

	public void onSaveTaskButtonClicked(View v) {
		ViewSwitcher viewSwitcher = (ViewSwitcher) v.getParent().getParent();
		TextView textView = (TextView) viewSwitcher.findViewById(R.id.textViewTask);
		EditText editText = (EditText) viewSwitcher.findViewById(R.id.editViewTask);

		textView.setText(editText.getText());

		viewSwitcher.showPrevious();

		Object tag = ((View) v.getParent().getParent()).getTag();

		if (null != tag) {
			long id = (long) tag;

			for (TaskModel t : tasks) {
				if (t.getId() == id) {
					try {
						t.setText(editText.getText().toString());
						t.update(this);

						Toast.makeText(this, R.string.task_updated, Toast.LENGTH_LONG).show();

						((TaskListAdapter) getListAdapter()).notifyDataSetChanged();
					} catch (TaskDatabaseException e) {
						Log.e(this.getClass().getName(), e.getMessage());
					}

					break;
				}
			}
		} else {
			Log.d(this.getClass().getName(), "Null tag.");
		}
	}

	public void onEditTaskButtonClicked(View v) {
		ViewSwitcher viewSwitcher = (ViewSwitcher) v.getParent().getParent();
		TextView textView = (TextView) viewSwitcher.findViewById(R.id.textViewTask);
		EditText editText = (EditText) viewSwitcher.findViewById(R.id.editViewTask);

		editText.setText(textView.getText());
		editText.requestFocus();

		viewSwitcher.showNext();
	}
}
