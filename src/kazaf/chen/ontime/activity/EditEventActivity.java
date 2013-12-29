package kazaf.chen.ontime.activity;

import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.OnTimeDAO;
import kazaf.chen.intime.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditEventActivity extends Activity {

	private static EditText et_event, et_ps;
	private static Button btn_pickTime;
	private ActionBar actionBar;
	private OnTimeDAO myDao;
	String id;
	private static String str_cost, str_hour, str_minute; 
	private static int eventHour;
	private static int eventMinute;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		et_event = (EditText) findViewById(R.id.et_event);
		et_ps = (EditText) findViewById(R.id.et_ps);
		btn_pickTime = (Button) findViewById(R.id.btn_pickTime);
		str_cost = getString(R.string.add_event_cost) + " ";
		str_hour = " " + getString(R.string.hour) + " ";
		str_minute = " " + getString(R.string.minute);

		getData();
		updateTimeDisplay();
		myDao = new OnTimeDAO(this);
		LaunchTimePicker();

	}

	public void getData() {
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getString(OnTime.ID);
		et_event.setText(bundle.getString(OnTime.EVENT));
		eventHour = Integer.parseInt(bundle.getString(OnTime.HOUR));
		eventMinute = Integer.parseInt(bundle.getString(OnTime.MINUTE));
		et_ps.setText(bundle.getString(OnTime.PS));
		Log.i("Test", "PS: " + bundle.getString(OnTime.PS));

	}

	private void LaunchTimePicker() {
		btn_pickTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editTimePickerFragment editTimePickerFragment = new editTimePickerFragment();
				FragmentManager fm = getFragmentManager();
				editTimePickerFragment.show(fm, "timePicker");
			}
		});

	}

	public static void updateTimeDisplay() {

		 StringBuilder sb = new StringBuilder().append(str_cost)
		 .append(eventHour).append(str_hour).append(eventMinute)
		 .append(str_minute);

		btn_pickTime.setText(sb);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case R.id.menu_done:
			
			String event = et_event.getText().toString();
			String hour = String.valueOf(eventHour);
			String minute = String.valueOf(eventMinute);
			String ps = et_ps.getText().toString();
			myDao.updateOnTimeEvent(id, event, hour, minute, ps);
			finish();
			Toast.makeText(this, R.string.toast_done,
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_delete:
			myDao.deleteOnTimeEvent(id);
			finish();
			Toast.makeText(this, R.string.toast_delete,
					Toast.LENGTH_SHORT).show();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	public static class editTimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			TimePickerDialog timePickerDialog = new TimePickerDialog(
					getActivity(), this, eventHour, eventMinute, true);
			timePickerDialog.setTitle(R.string.dialog_event_time);
			return timePickerDialog;

		}

		@Override
		public void onTimeSet(android.widget.TimePicker view, int hourOfDay,
				int minute) {
			eventHour = hourOfDay;
			eventMinute = minute;
			updateTimeDisplay();

		}

	}

}
