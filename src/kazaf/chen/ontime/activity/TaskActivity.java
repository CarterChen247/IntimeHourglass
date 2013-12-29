package kazaf.chen.ontime.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kazaf.chen.background.CallAlarm;
import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.OnTimeDAO;
import kazaf.chen.intime.R;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//Page:Task
public class TaskActivity extends Activity {

	private OnTimeDAO myDAO;
	private ListView lv_Task;
	private ArrayList<HashMap<String, String>> data;
	private ActionBar actionBar;
	private static Calendar calendar = Calendar.getInstance();
	ActionMode.Callback callback;
	ActionMode actionMode;
	static String id; 			 // listview item id 
	static int naviPosition = 0; //actionBar spinner item position
	int scream;

	//Custom navigation view
	public class CustomNavigationSpinner extends BaseAdapter {
		Context context;
		LayoutInflater inflater;

		public CustomNavigationSpinner(Context context) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v_actionBar = inflater.inflate(R.layout.actionbar_view, null);
			TextView title = (TextView) v_actionBar
					.findViewById(R.id.actionbar_view_title);
			TextView subtitle = (TextView) v_actionBar
					.findViewById(R.id.actionbar_view_subtitle);
			title.setText(R.string.act_title);
			switch (position) {
			case 0:
				subtitle.setText(R.string.act_sub_current);
				break;
			case 1:
				subtitle.setText(R.string.act_sub_past);
				break;
			}

			return v_actionBar;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View v_actionBar_dropDown = inflater.inflate(
					R.layout.actionbar_dropdown_item, null);
			TextView subtitle = (TextView) v_actionBar_dropDown
					.findViewById(R.id.actionbar_dropdown_item);
			subtitle.setText(R.string.act_sub_current);
			
			switch (position) {
			case 0:
				subtitle.setText(R.string.act_sub_current);
				naviPosition = 0;
				break;
			case 1:
				subtitle.setText(R.string.act_sub_past);
				naviPosition = 1;
				break;
			}
			
			return v_actionBar_dropDown;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);


		lv_Task = (ListView) findViewById(R.id.lv_Task);
		myDAO = new OnTimeDAO(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_event, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case R.id.menu_add:
			addTask();
			break;
		}
		return true;
	}

	private void addTask() {
		Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
		startActivity(intent);

	}

	private void pastTask() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		String TimerNow = calendar.getTimeInMillis() + "";
		data = myDAO.getPastTaskData(TimerNow);
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.listview_task, new String[] { "ps", "event", "time",
						"day" }, new int[] { R.id.tv_id, R.id.tv_event,
						R.id.tv_time, R.id.tv_dayOfWeek });
		lv_Task.setAdapter(adapter);
		lv_Task.setSelection(data.size());

	}

	@Override
	protected void onResume() {
		super.onResume();
		naviPosition = 0;//solve the problem of navigation and content not the same
		
		refreshNavi();
		refreshAdapter();

		callback = new ActionMode.Callback() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.setTitle(R.string.delete_remove);
				getMenuInflater().inflate(R.menu.menu_delete, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.menu_delete:

					deleteTask();
					Log.i("TEST", "naviPosition: "+naviPosition);
					if ( naviPosition ==1 ){
						pastTask();
					}else{
						refreshAdapter();
					}
					
					mode.finish();
					Toast.makeText(TaskActivity.this, R.string.delete_already,
							Toast.LENGTH_LONG).show();

					break;

				}
				return true;
			}

		};

		lv_Task.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				long milli = Long.parseLong(data.get(position).get(OnTime.Milli));
				scream = (int) ( milli / 1000 );
				id = data.get(position).get(OnTime.ID);
				

				
				startActionMode(callback);

			}
		});

	}

	//refresh the spinner of navigation
	private void refreshNavi() {
		actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_LIST);
		CustomNavigationSpinner customNavigationSpinner = new CustomNavigationSpinner(this);
		OnNavigationListener onNavigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {

				switch(itemPosition){
				case 0:
					refreshAdapter();
					break;
				case 1:
					pastTask();
					break;
					
					}


				return true;
			}
		};
		actionBar.setListNavigationCallbacks(customNavigationSpinner, onNavigationListener);
	}

	//refresh the list adapter
	private void refreshAdapter() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		String TimerNow = calendar.getTimeInMillis() + "";
		data = myDAO.getTaskData(TimerNow);
		final SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.listview_task, new String[] { "ps", "event", "time",
						"day" }, new int[] { R.id.tv_id, R.id.tv_event,
						R.id.tv_time, R.id.tv_dayOfWeek });
		lv_Task.setAdapter(adapter);
		lv_Task.setEmptyView(findViewById(R.id.empty));
	}

	//method for delete the task
	private void deleteTask() {
		myDAO.deleteOnTimeTask(id);
		stopAlarm();

	}
	
	public void stopAlarm(){
		Intent intent = new Intent(this, CallAlarm.class);
		PendingIntent pending = PendingIntent.getBroadcast(this, scream, intent, 0);
		AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
		am.cancel(pending);
	}

}