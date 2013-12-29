package kazaf.chen.ontime.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kazaf.chen.ontime.OnTimeDAO;
import kazaf.chen.intime.R;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PastTaskActivity extends Activity {
	private OnTimeDAO myDAO;
	private ListView lv_Task;
	private ArrayList<HashMap<String, String>> data;
	private ActionBar actionBar;
	private static Calendar calendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_past_task);
	
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		myDAO = new OnTimeDAO(this);
		
		lv_Task = (ListView) findViewById(R.id.lv_pastTask);
		
	}


	@Override
	protected void onResume() {
		super.onResume();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String TimerNow = calendar.getTimeInMillis() + "";
		data = myDAO.getPastTaskData(TimerNow);
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.listview_task, new String[] { "id", "event", "time",
						"day" }, new int[] { R.id.tv_id, R.id.tv_event,
						R.id.tv_time, R.id.tv_dayOfWeek });
		lv_Task.setAdapter(adapter);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}

}
