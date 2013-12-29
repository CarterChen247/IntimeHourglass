package kazaf.chen.ontime.activity;

import kazaf.chen.ontime.OnTimeDAO;
import kazaf.chen.intime.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CountEventActivity extends Activity {
	private OnTimeDAO myDao;
	private EditText et_event, et_ps;
	private static Button btn_pickTime;
	private static String str_cost, str_hour, str_minute;

	// �N��l�ɶ��]�w��0��0��
	private static int eventHour;
	private static int eventMinute;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		// actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);//�o�ˤ~�i�H��home��

		// ����ƿ�J���
		et_event = (EditText) findViewById(R.id.et_event);
		et_ps = (EditText) findViewById(R.id.et_ps);
		btn_pickTime = (Button) findViewById(R.id.btn_pickTime);
		str_cost = getString(R.string.add_event_cost) + " ";
		str_hour = " " + getString(R.string.hour) + " ";
		str_minute = " " + getString(R.string.minute);

		// new��ƮwHelper���O
		myDao = new OnTimeDAO(this);

		et_event.setHint(R.string.add_count_title_hint);
		Bundle bundle = this.getIntent().getExtras();
		eventMinute = Integer.parseInt(bundle.getString("minute"));
		eventHour = Integer.parseInt(bundle.getString("hour"));
		updateTimeDisplay();
		// �B�zbtn_pickTime�P��s�o����쪺�ʧ@
		LaunchTimePicker();

	}

	// �B�zbtn_pickTime�P��s�o����쪺�ʧ@
	private void LaunchTimePicker() {

		btn_pickTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TimePickerFragment timePickerFragment = new TimePickerFragment();
				FragmentManager fm = getFragmentManager();
				timePickerFragment.show(fm, "timePicker");
			}
		});

	}

	private static void updateTimeDisplay() {

		StringBuilder sb = new StringBuilder().append(str_cost)
				.append(eventHour).append(str_hour).append(eventMinute)
				.append(str_minute);

		btn_pickTime.setText(sb);

	}

	// ���]�w
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_event_add, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// case R.id.menu_clear:
		// finish();
		// Toast.makeText(this, "No Event has been added", Toast.LENGTH_SHORT)
		// .show();
		// break;

		case R.id.menu_done:
			eventDone();

		}

		return super.onOptionsItemSelected(item);
	}

	private void eventDone() {
		String event = et_event.getText().toString();
		String hour = String.valueOf(eventHour);
		String minute = String.valueOf(eventMinute);
		String ps = et_ps.getText().toString();

		myDao.createOnTimeEvent(event, hour, minute, ps);
		Toast.makeText(this, R.string.toast_event_done_create,
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, EventActivity.class);
		startActivity(intent);

	}

	// �ۦ�q�sTimeDialog�˦�
	public static class TimePickerFragment extends DialogFragment implements
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

//	@Override
//	protected void onPause() {// ���~���}�s�W���A�i�J �Nsb�]��0:0
//		super.onPause();
//		eventHour = 0;
//		eventMinute = 0;
//	}

}