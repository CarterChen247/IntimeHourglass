package kazaf.chen.background;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.OnTimeDAO;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ResetTimer extends BroadcastReceiver {

	private OnTimeDAO myDAO;
	private ArrayList<HashMap<String, String>> data;
	private static Calendar calendar = Calendar.getInstance();
	private String savedDate;
	private int taskCount;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction().toString();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			// Toast.makeText(context, "boot completed action has got",
			// Toast.LENGTH_LONG).show();

			calendar.setTimeInMillis(System.currentTimeMillis());
			String TimerNow = (calendar.getTimeInMillis()) + "";
			myDAO = new OnTimeDAO(context);
			data = myDAO.getTaskDate(TimerNow);

			for (taskCount = 1; taskCount <= data.size(); taskCount++) {
				savedDate = data.get(taskCount - 1).get(OnTime.DATE);// get
																		// yyyy-MM-dd
																		// HH:mm:ss
				Log.i("ResetTimer", "savedDate: " + savedDate);
				long alarmLong = Long.parseLong(savedDate);
				Intent alarmIntent = new Intent(context, CallAlarm.class);
				int scream = (int) (alarmLong / 1000);
				PendingIntent pending = PendingIntent.getBroadcast(context,
						scream, alarmIntent, 0);
				AlarmManager am = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, alarmLong, pending);

				// SimpleDateFormat formatter = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//alarmManager allow
				// long
				// only, so convert the date to milliseconds.
				// try {
				// Log.i("ResetTimer", "savedDate: "+savedDate);
				// Date alarmDate = formatter.parse(savedDate);
				// long alarmLong = alarmDate.getTime(); //the long to set
				// alarm.
				// Intent alarmIntent = new Intent(context, CallAlarm.class);
				// int scream = (int) ( alarmLong /1000 );
				// PendingIntent pending = PendingIntent.getBroadcast(context,
				// scream, intent, 0);
				// AlarmManager am=
				// (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
				// am.set(AlarmManager.RTC_WAKEUP, alarmLong, pending);
				//
				// } catch (ParseException e) {
				// e.printStackTrace();
				// }
			}

			Toast.makeText(context, "complete", Toast.LENGTH_LONG).show();
		}
	}
}
