package kazaf.chen.ontime.activity;

/*
 * Main page of InTime
 * -------------------
 * Showing Timer, upcoming event.
 */
import kazaf.chen.intime.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CountActivity extends Activity {

	private TextView tv_timer, tv_hint;
	private long startTime = 0;
	private static int second, minute, hour;
	private String playMode;
	private ImageButton btn_start;

	// runs without a timer by reposting this handler at the end of the runnable
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			long duration = System.currentTimeMillis() - startTime;
			second = (int) (duration / 1000) % 60;
			minute = (int) ((duration / (1000 * 60)) % 60);
			hour = (int) ((duration / (1000 * 60 * 60)) % 24);

			String text = format(hour) + ":" + format(minute) + ":"
					+ format(second);
			tv_timer.setText(text);

			handler.postDelayed(this, 0);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);

		tv_timer = (TextView) findViewById(R.id.tv_timer);
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		btn_start = (ImageButton) findViewById(R.id.btn_start);
		//		playMode.setText("start");
		playMode = "start";
		btn_start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (playMode.equals("stop")) {
					handler.removeCallbacks(runnable);
					playMode="restart";
					btn_start.setImageResource(R.drawable.img_restart);
					tv_hint.setText(getString(R.string.count_ready));
				} else {
					startTime = System.currentTimeMillis();
					handler.postDelayed(runnable, 0);
					playMode="stop";
					btn_start.setImageResource(R.drawable.img_pause);
					tv_hint.setText(getString(R.string.count_counting));
				}
			}
		});
	}

	// add 0 to o'clock
	private static String format(int x) {
		String number = "" + x;
		if (number.length() == 1)
			number = "0" + number;
		return number;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.count, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_count:

			if (playMode.equals("stop")) {
				Toast.makeText(this, R.string.count_toast,
						Toast.LENGTH_LONG).show();
			} else {
				Intent intent = new Intent(this, CountEventActivity.class);
				Bundle bundle = new Bundle();
				Log.i("TEST", "second:" + second);
				Log.i("TEST", "minute:" + minute);
				Log.i("TEST", "hour:" + hour);
				bundle.putString("minute", String.valueOf((minute + 1)));
				bundle.putString("hour", String.valueOf(hour));
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("test", "pause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("test", "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(runnable);
		Log.i("test", "onDestroy");
	}
}
