package kazaf.chen.ontime.activity;

import java.util.ArrayList;
import java.util.HashMap;

import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.OnTimeDAO;
import kazaf.chen.intime.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
//Page:Event
public class EventActivity extends Activity {

	private ListView lv_Event;
	private ActionBar actionBar;
	private OnTimeDAO myDAO;
	private ArrayList<HashMap<String, String>> data;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		myDAO = new OnTimeDAO(this);
		data = myDAO.getEventData();
		lv_Event = (ListView) findViewById(R.id.lv_Event);

		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.listview_event, new String[] { "ps", "event", "hour",
						"minute" }, new int[] { R.id.tv_id, R.id.tv_event,
						R.id.tv_hour, R.id.tv_minute });
		lv_Event.setAdapter(adapter);
		// lv_Event.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// lv_Event.setSelection(data.size() - 1);
		// Log.i("Test", "data size:"+ (data.size()-1));
		// }
		// });
		//
		lv_Event.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(EventActivity.this,
						EditEventActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(OnTime.ID, data.get(position).get(OnTime.ID));
				bundle.putString(OnTime.EVENT,
						data.get(position).get(OnTime.EVENT));
				bundle.putString(OnTime.HOUR,
						data.get(position).get(OnTime.HOUR));
				bundle.putString(OnTime.MINUTE,
						data.get(position).get(OnTime.MINUTE));
				bundle.putString(OnTime.PS, data.get(position).get(OnTime.PS));
				Log.i("Test", "position:" + position);
				Log.i("Test", "data size:" + data.size());
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		data = myDAO.getEventData();
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.listview_event, new String[] { "ps", "event", "hour",
						"minute" }, new int[] { R.id.tv_id, R.id.tv_event,
						R.id.tv_hour, R.id.tv_minute });
		lv_Event.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_event, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case R.id.menu_add:
			Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
			startActivity(intent);
			break;

		}
		return true;
	}

}
