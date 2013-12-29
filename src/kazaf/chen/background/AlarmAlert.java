package kazaf.chen.background;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kazaf.chen.intime.R;
import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.OnTimeDAO;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

public class AlarmAlert extends Activity {
	private OnTimeDAO myDAO;
	private ArrayList<HashMap<String, String>> data;
	private static Calendar calendar = Calendar.getInstance();
	private TextView tv_event, tv_ps, tv_time;
	private NotificationManager ntfMgr;
	int ntfID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_alarm);

		tv_event = (TextView) findViewById(R.id.tv_event);
		tv_ps = (TextView) findViewById(R.id.tv_ps);
		tv_time = (TextView) findViewById(R.id.tv_time);
	}

	@Override
	protected void onResume() {
		super.onResume();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String TimerNow = (calendar.getTimeInMillis()) + "";
		myDAO = new OnTimeDAO(this);
		data = myDAO.getAlarmTask(TimerNow);
		
		tv_event.setText(data.get(0).get(OnTime.EVENT));
		tv_ps.setText(data.get(0).get(OnTime.PS));
		tv_time.setText(data.get(0).get(OnTime.Time));
		ntfID = Integer.valueOf(data.get(0).get(OnTime.BEGIN_MIN));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ntfMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this, CallAlarm.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
				ntfID, intent, 0);

		ntfMgr.cancelAll();//•u≈„•‹1µßnotification
		
		Notification notification = new Notification.Builder(this)
				
		.setSound(null)
		.setContentTitle(data.get(0).get(OnTime.EVENT)).setContentText(data.get(0).get(OnTime.PS))
		.setSmallIcon(R.drawable.ic_stat_notify)
		.setAutoCancel(true).setContentIntent(pendingIntent)
		.getNotification();
		
		Log.i("AlarmAlert", ntfID+"");
		ntfMgr.notify(ntfID, notification);
	}

	
}
