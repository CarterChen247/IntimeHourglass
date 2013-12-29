package kazaf.chen.ontime.activity;

/*
 * Main page of InTime
 * -------------------
 * Showing Timer, upcoming event.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kazaf.chen.intime.R;
import kazaf.chen.ontime.OnTimeDAO;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private OnTimeDAO myDAO;
	long milliNow, milliUpComing, milliTimer;
	int hour, minute, second;
	static MyCountDown myCD;
	private static Calendar calendar = Calendar.getInstance();
	private TextView tv_timer, tv_event, tv_id, tv_time, tv_dayOfWeek, tv_hint,
			tv_pref_myTasks, tv_pref_myEvents;
	private RelativeLayout btn_myTasks, btn_myEvents, btn_eventTimer;
	private LinearLayout btn_my_task;
	private Button btn_addTask, btn_addEvent;
	ActionBar actionbar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_timer = (TextView) findViewById(R.id.tv_timer);
		tv_event = (TextView) findViewById(R.id.tv_event);
		tv_id = (TextView) findViewById(R.id.tv_id);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_dayOfWeek = (TextView) findViewById(R.id.tv_dayOfWeek);
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		tv_pref_myTasks = (TextView) findViewById(R.id.tv_pref_myTasks);
		tv_pref_myEvents = (TextView) findViewById(R.id.tv_pref_myEvents);
		btn_myTasks = (RelativeLayout) findViewById(R.id.btn_myTasks);
		btn_myEvents = (RelativeLayout) findViewById(R.id.btn_myEvents);
		btn_eventTimer = (RelativeLayout) findViewById(R.id.btn_eventTimer);
		btn_addTask = (Button) findViewById(R.id.btn_addTask);
		btn_addEvent = (Button) findViewById(R.id.btn_addEvent);
		btn_my_task = (LinearLayout) findViewById(R.id.btn_my_task);
		myDAO = new OnTimeDAO(this);

		btn_my_task.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LaunchTaskActivity();

			}
		});
		btn_myTasks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LaunchTaskActivity();

			}
		});
		btn_myEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LaunchEventActivity();

			}
		});
		btn_eventTimer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LaunchCountActivity();

			}
		});

		btn_addTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AddTaskActivity.class);
				startActivity(intent);

			}
		});
		btn_addEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AddEventActivity.class);
				startActivity(intent);

			}
		});

		
	}

	// set up timer in the upper place of this page
	// calculate milliseconds and setText
	private void setTimer() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		String TimerNow = calendar.getTimeInMillis() + "";
		String TimerReturn = myDAO.getTimer(TimerNow);
		ArrayList<HashMap<String, String>> data;

		if (TimerReturn == "empty") {
			tv_timer.setText(":-)");
			tv_event.setText(R.string.main_greeting);
			tv_id.setText(R.string.main_subGtn);
			tv_time.setText("");
			tv_dayOfWeek.setText("");
			tv_hint.setText("");
			tv_pref_myTasks.setText(R.string.no);
		} else {
			milliUpComing = Long.valueOf(TimerReturn);
			milliNow = calendar.getTimeInMillis();

			milliTimer = milliUpComing - milliNow;
			Log.i("Test", "milliUpComing - milliNow = " + milliTimer);

			myCD = new MyCountDown(milliTimer, 1000);
			myCD.start();

			data = myDAO.getTaskData(TimerNow);
			tv_event.setText(data.get(0).get("event"));
			tv_id.setText(data.get(0).get("ps"));
			tv_time.setText(data.get(0).get("time"));
			tv_dayOfWeek.setText(data.get(0).get("dayOfWeek"));
			tv_hint.setText(R.string.main_hint);
			tv_pref_myTasks.setText(String.valueOf(data.size()));
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.menu_event:
	// LaunchEventActivity();
	// break;
	// case R.id.menu_count:
	// LaunchCountActivity();
	// break;
	// }
	// return true;
	// }

	private void LaunchTaskActivity() {
		Intent intent = new Intent(this, TaskActivity.class);
		startActivity(intent);

	}

	private void LaunchEventActivity() {
		Intent intent = new Intent(this, EventActivity.class);
		startActivity(intent);

	}

	private void LaunchCountActivity() {
		Intent intent = new Intent(this, CountActivity.class);
		startActivity(intent);

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (myCD != null) {
			myCD.cancel();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		setTimer();
		int TotalEvents = myDAO.getTotalEvents();
		tv_pref_myEvents.setText(String.valueOf(TotalEvents));
	}

	class MyCountDown extends CountDownTimer {

		public MyCountDown(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {

		}

		@Override
		public void onTick(long millisUntilFinished) {
			second = (int) (millisUntilFinished / 1000) % 60;
			minute = (int) ((millisUntilFinished / (1000 * 60)) % 60);
			hour = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
			String text = format(hour) + ":" + format(minute) + ":"
					+ format(second);
			tv_timer.setText(text);

		}

	}

	// add 0 to o'clock
	private static String format(int x) {
		String number = "" + x;
		if (number.length() == 1)
			number = "0" + number;
		return number;
	}
}