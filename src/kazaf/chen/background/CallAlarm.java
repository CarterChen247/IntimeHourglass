package kazaf.chen.background;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.OnTimeDAO;
import kazaf.chen.intime.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;


public class CallAlarm extends BroadcastReceiver{
	
	private OnTimeDAO myDAO;
	private ArrayList<HashMap<String, String>> data;
	private static Calendar calendar = Calendar.getInstance();
	private NotificationManager ntfMgr;
	private Vibrator vibrator;
	private PowerManager pm;
	private String Ticker;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ntfMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		
		calendar.setTimeInMillis(System.currentTimeMillis());
		String TimerNow = (calendar.getTimeInMillis()) + "";
		myDAO = new OnTimeDAO(context);
		data = myDAO.getAlarmTask(TimerNow);
	
		
//		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				calendar.get(Calendar.MINUTE), intent, 0);

		ntfMgr.cancelAll();//•u≈„•‹1µßnotification
		
		Notification notification = new Notification.Builder(context)
				.setTicker(context.getString(R.string.dialog_notify))
				.setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
//				.setVibrate(new long[] { 0, 400, 200, 400 })
				.setVibrate(new long[] { 0, 200, 100, 100, 100, 100, 100, 200, 100, 200, 500, 200, 100, 200 })
				.setContentTitle(data.get(0).get(OnTime.EVENT)).setContentText(data.get(0).get(OnTime.PS))
				.setSmallIcon(R.drawable.ic_stat_notify)
				.setAutoCancel(true).setContentIntent(pendingIntent)
				.getNotification();

		ntfMgr.notify(calendar.get(Calendar.MINUTE), notification);
		
		Log.i("CallAlarm", calendar.get(Calendar.MINUTE)+"");
		Intent receiveIntent = new Intent(context, AlarmAlert.class);
	    receiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(receiveIntent);
	}

}

