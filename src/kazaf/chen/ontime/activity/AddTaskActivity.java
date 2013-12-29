package kazaf.chen.ontime.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import kazaf.chen.background.CallAlarm;
import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.OnTimeDAO;
import kazaf.chen.intime.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddTaskActivity extends Activity {

	private Spinner sp_task;
	private static Button sp_date, sp_time;
	private Button btn_add_event;
	private static TextView prepare_date, prepare_time;
	private OnTimeDAO myDAO;
	private ArrayList<HashMap<String, String>> data;
	private static int myYear, myMonth, myDay, myHour, myMinute, eventHour,
			eventMinute;
	private String eventName, eventPs;
	private static String showDay;
	final static Calendar preCalendar = Calendar.getInstance();
	boolean selected = false;//check if time spinner has been selected, avoid instant-pop event occurs.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		myDAO = new OnTimeDAO(this);
		
		
		// Initial components in layout
		sp_date = (Button) findViewById(R.id.sp_date);
		sp_time = (Button) findViewById(R.id.sp_time);
		sp_task = (Spinner) findViewById(R.id.sp_task);
		prepare_date = (TextView) findViewById(R.id.prepare_date);
		prepare_time = (TextView) findViewById(R.id.prepare_time);

		

		getCurrentTime();
		updatePrepareDisplay(myYear, myMonth, myDay, myHour, myMinute);

		// the setting of event that user click the DatePickerDialog Button
		sp_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePickerFragment = new DatePickerFragment();
				FragmentManager fm = getFragmentManager();
				datePickerFragment.show(fm, "datePicker");
			}
		});

		// the setting of event that user click the TimePickerDialog Button
		sp_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selected = true;
				spinnerTimePickerFragment timePickerFragment = new spinnerTimePickerFragment();
				FragmentManager fm = getFragmentManager();
				timePickerFragment.show(fm, "timePicker");
			}
		});
		
//		btn_add_event.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(AddTaskActivity.this, AddEventActivity.class);
//				startActivity(intent);
//			}
//		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		setSpinner();
	}

	private void setSpinner() {
		// put events in spinner
		data = myDAO.getEventData();
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.sp_event, new String[] { "ps", "event", "hour",
						"minute" }, new int[] { R.id.tv_id, R.id.tv_event,
						R.id.tv_hour, R.id.tv_minute });
		sp_task.setAdapter(adapter);
		sp_task.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				eventName = data.get(position).get(OnTime.EVENT);
				eventPs = data.get(position).get(OnTime.PS);
				eventHour = Integer.parseInt(data.get(position)
						.get(OnTime.HOUR));
				eventMinute = Integer.parseInt(data.get(position).get(
						OnTime.MINUTE));
				updatePrepareDisplay(myYear, myMonth, myDay, myHour, myMinute);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Log.i("Test", "nothing selected");
				Toast.makeText(AddTaskActivity.this, "No event selected. Add new event?", Toast.LENGTH_LONG).show();

			}
		});
		
	}

	// Get current time
	private void getCurrentTime() {
		final Calendar calendar = Calendar.getInstance();
		myYear = calendar.get(Calendar.YEAR);
		myMonth = calendar.get(Calendar.MONTH);
		myDay = calendar.get(Calendar.DAY_OF_MONTH);
		myHour = calendar.get(Calendar.HOUR_OF_DAY);
		myMinute = calendar.get(Calendar.MINUTE);//avoid add pop up accident
		updateDisplay();
	}

	// Refresh the Display of the buttons
	private static void updateDisplay() {
		StringBuilder sb_date = new StringBuilder().append(myYear).append("/")
				.append(myMonth + 1).append("/").append(myDay);
		StringBuilder sb_time = new StringBuilder().append(format(myHour)).append(":")
				.append(format(myMinute));
		sp_date.setText(sb_date);
		sp_time.setText(sb_time);

	}

	// DatePickerDialog
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					getActivity(), this, myYear, myMonth, myDay);
			return datePickerDialog;
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month,
				int dayOfMonth) {

			myYear = year;
			myMonth = month;
			myDay = dayOfMonth;
			updateDisplay();

			preCalendar.set(Calendar.YEAR, year);
			preCalendar.set(Calendar.MONTH, month);
			preCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updatePrepareDisplay(year, month, dayOfMonth, myHour, myMinute);

		}
	}

	// TimePickerDialog
	public static class spinnerTimePickerFragment extends DialogFragment
			implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			TimePickerDialog timePickerDialog = new TimePickerDialog(
					getActivity(), this, myHour, myMinute, true);
			return timePickerDialog;

		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			myHour = hourOfDay;
			myMinute = minute;
			updateDisplay();

			preCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			preCalendar.set(Calendar.MINUTE, minute);
			
			myYear = preCalendar.get(Calendar.YEAR);
			myMonth = preCalendar.get(Calendar.MONTH);
			myDay = preCalendar.get(Calendar.DAY_OF_MONTH);
			//I don't know what's going on here, either.
			
			updatePrepareDisplay(myYear, myMonth, myDay, hourOfDay, minute);

		}
	}

	// display the prepare time TextViews
	private static void updatePrepareDisplay(int year, int month,
			int dayOfMonth, int hourOfDay, int minute) {
		preCalendar.set(Calendar.YEAR, year);
		preCalendar.set(Calendar.MONTH, month);
		preCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		preCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		preCalendar.set(Calendar.MINUTE, minute);
		preCalendar.set(Calendar.SECOND, 0);

		preCalendar.add(Calendar.HOUR_OF_DAY, -eventHour);
		preCalendar.add(Calendar.MINUTE, -eventMinute);

		StringBuilder pr_date = new StringBuilder().append(preCalendar.get(Calendar.YEAR)).append("/")
				.append(preCalendar.get(Calendar.MONTH) + 1).append("/").append(preCalendar.get(Calendar.DAY_OF_MONTH));
		StringBuilder pr_time = new StringBuilder().append(format(preCalendar.get(Calendar.HOUR_OF_DAY))).append(":")
				.append(format(preCalendar.get(Calendar.MINUTE)));
		
		prepare_date.setText(pr_date);
		prepare_time.setText(pr_time);
		
	}

	//add 0 to o'clock 
	private static String format(int x) {
		String number = "" + x;
		if (number.length() == 1)
			number = "0" + number;
		return number;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_event_add, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
//		case R.id.menu_clear:
//			finish();
//			Toast.makeText(this, "No Task has been added", Toast.LENGTH_SHORT)
//					.show();
//			break;

		case R.id.menu_done:
			if ( eventName == null ){
				Toast.makeText(this, R.string.insert_event, Toast.LENGTH_LONG).show();
			}else if (selected == false){
				Toast.makeText(this, R.string.toast_select_time, Toast.LENGTH_LONG).show();
			}
			else{
				
			saveTaskData(); // save the data to db and display to listview;
			setAlarm(); // set alarm;
			Toast.makeText(
					this,
					R.string.toast_task_done_create, Toast.LENGTH_SHORT)
					.show();
			
			Intent intent = new Intent(this, TaskActivity.class);
			startActivity(intent);
			
			}
		}

//		return super.onOptionsItemSelected(item);
		return true;
	}

	private void saveTaskData() {
		String date = preCalendar.get(Calendar.YEAR) + "-" + format(preCalendar.get(Calendar.MONTH)+1) + "-" + format(preCalendar.get(Calendar.DAY_OF_MONTH)) + "_" + format(preCalendar.get(Calendar.HOUR_OF_DAY)) +":" + format(preCalendar.get(Calendar.MINUTE));
		String milli = String.valueOf(preCalendar.getTimeInMillis());
		SimpleDateFormat simpleDateFormate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date myDate =simpleDateFormate.parse(preCalendar.get(Calendar.YEAR) + "-" + format(preCalendar.get(Calendar.MONTH)+1) + "-" + format(preCalendar.get(Calendar.DAY_OF_MONTH)));
			SimpleDateFormat showDayOfweek = new SimpleDateFormat("MMM d");
			showDay=showDayOfweek.format(myDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myDAO.createOnTimeTask(eventName, format(preCalendar.get(Calendar.HOUR_OF_DAY)), format(preCalendar.get(Calendar.MINUTE)), format(myHour), format(myMinute), date, showDay, milli, eventPs);
		
		
		Log.d("Test", "eventName: " + eventName);
		Log.d("Test", "start time: " + format(preCalendar.get(Calendar.HOUR_OF_DAY))+":"+format(preCalendar.get(Calendar.MINUTE)));
		Log.d("Test", "end   time: " + format(myHour)+":"+ format(myMinute));
		Log.d("Test", "date: " + preCalendar.get(Calendar.YEAR) + "-" + format(preCalendar.get(Calendar.MONTH)) + "-" + format(preCalendar.get(Calendar.DAY_OF_MONTH)) + " " + format(preCalendar.get(Calendar.HOUR_OF_DAY)) +":" + format(preCalendar.get(Calendar.MINUTE)) + ":" + format(preCalendar.get(Calendar.SECOND)));
	}

	private void setAlarm() {
		Intent intent = new Intent(this, CallAlarm.class);
		int scream = (int) (( preCalendar.getTimeInMillis() )/1000);//set the time for alarm to shout
		PendingIntent pending = PendingIntent.getBroadcast(this, scream, intent, 0);//0 stands for what?
		AlarmManager am= (AlarmManager)getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, preCalendar.getTimeInMillis(), pending);		

	}
	
//	public void stopAlarm(){
//		Intent intent = new Intent(this, CallAlarm.class);
//		PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
//		AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//		am.cancel(pending);
//	}
}
