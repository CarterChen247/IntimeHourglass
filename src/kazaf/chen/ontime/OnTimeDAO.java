package kazaf.chen.ontime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kazaf.chen.background.ResetTimer;
import kazaf.chen.ontime.Constant.OnTime;
import kazaf.chen.ontime.activity.AddTaskActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OnTimeDAO {

	private SQLiteDatabase db;
	private OnTimeHelper myHelper;

	public OnTimeDAO(Context context) {
		myHelper = new OnTimeHelper(context);
		db = myHelper.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	// + Event
	public void createOnTimeEvent(String event, String hour, String minute,
			String ps) {
		ContentValues values = new ContentValues();
		values.put(OnTime.EVENT, event);
		values.put(OnTime.HOUR, hour);
		values.put(OnTime.MINUTE, minute);
		values.put(OnTime.PS, ps);
		db.insert(OnTime.TABLE_EVENT, null, values);
		db.close();
	}

	// Show Event
	public ArrayList getEventData() {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db
				.query(OnTime.TABLE_EVENT, new String[] { "_id", "event",
						"hour", "minute", "ps" }, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(OnTime.ID, cursor.getString(0));
			hm.put(OnTime.EVENT, cursor.getString(1));
			hm.put(OnTime.HOUR, cursor.getString(2));
			hm.put(OnTime.MINUTE, cursor.getString(3));
			hm.put(OnTime.PS, cursor.getString(4));
			data.add(hm);
			cursor.moveToNext();
		}

		cursor.close();
		return data;
	}

	// update Event
	public void updateOnTimeEvent(String id, String event, String hour,
			String minute, String ps) {
		ContentValues values = new ContentValues();
		values.put(OnTime.EVENT, event);
		values.put(OnTime.HOUR, hour);
		values.put(OnTime.MINUTE, minute);
		values.put(OnTime.PS, ps);
		db.update(OnTime.TABLE_EVENT, values, "_id" + "='" + id + "'", null);
		db.close();
	}

	// - Event
	public void deleteOnTimeEvent(String id) {
		db.delete(OnTime.TABLE_EVENT, "_id" + "='" + id + "'", null);
		db.close();
	}

	// -Task
	public void deleteOnTimeTask(String id) {
		Log.i("Test", "yo man I am here");
		db.delete(OnTime.TABLE_TASK, "_id" + "='" + id + "'", null);
		Log.i("OnTimeDAO", "deleted");
		// db.close();
	}

	// + Task
	public void createOnTimeTask(String event, String beginHour,
			String beginMin, String finishHour, String finishMin, String date,
			String day, String milli, String ps) {
		ContentValues values = new ContentValues();
		values.put(OnTime.EVENT, event);
		values.put(OnTime.BEGIN_HR, beginHour);
		values.put(OnTime.BEGIN_MIN, beginMin);
		values.put(OnTime.FINISH_HR, finishHour);
		values.put(OnTime.FINISH_MIN, finishMin);
		values.put(OnTime.DATE, date);
		values.put(OnTime.DAY, day);
		values.put(OnTime.Milli, milli);
		values.put(OnTime.PS, ps);
		db.insert(OnTime.TABLE_TASK, null, values);
		db.close();
	}

	// Show Task
	public ArrayList getTaskData(String TimerNow) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.query(OnTime.TABLE_TASK, new String[] { "_id",
				"event", "beginHour", "beginMin", "finishHour", "finishMin",
				"date", "day", "milli", "ps" }, "milli > ?",
				new String[] { TimerNow }, null, null, "milli");
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(OnTime.ID, cursor.getString(0));
				hm.put(OnTime.EVENT, cursor.getString(1));
				hm.put(OnTime.BEGIN_HR, cursor.getString(2));
				hm.put(OnTime.BEGIN_MIN, cursor.getString(3));
				hm.put(OnTime.FINISH_HR, cursor.getString(4));
				hm.put(OnTime.FINISH_MIN, cursor.getString(5));
				hm.put(OnTime.DATE, cursor.getString(6));
				hm.put(OnTime.DAY, cursor.getString(7));
				hm.put(OnTime.Milli, cursor.getString(8));
				String time = cursor.getString(2) + ":" + cursor.getString(3)
						+ "~" + cursor.getString(4) + ":" + cursor.getString(5);
				hm.put(OnTime.Time, time);
				hm.put(OnTime.PS, cursor.getString(9));
				data.add(hm);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return data;
	}

	public String getTimer(String TimerNow) {

		String Timer = "empty";
		Cursor cursor = db.query(OnTime.TABLE_TASK, new String[] { "milli" },
				"milli > ?", new String[] { TimerNow }, null, null, "milli");
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			Timer = cursor.getString(0);

		}

		cursor.close();
		return Timer;

	}

	// Show Task (past)
	public ArrayList<HashMap<String, String>> getPastTaskData(String TimerNow) {

		Long milliBefore = Long.parseLong(TimerNow) - 604800000;
		String TimerBefore = String.valueOf(milliBefore);

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.query(OnTime.TABLE_TASK, new String[] { "_id",
				"event", "beginHour", "beginMin", "finishHour", "finishMin",
				"date", "day", "ps", "milli" }, "milli BETWEEN " + TimerBefore
				+ " AND " + TimerNow, null, null, null, "milli", null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(OnTime.ID, cursor.getString(0));
				hm.put(OnTime.EVENT, cursor.getString(1));
				hm.put(OnTime.BEGIN_HR, cursor.getString(2));
				hm.put(OnTime.BEGIN_MIN, cursor.getString(3));
				hm.put(OnTime.FINISH_HR, cursor.getString(4));
				hm.put(OnTime.FINISH_MIN, cursor.getString(5));
				hm.put(OnTime.DATE, cursor.getString(6));
				hm.put(OnTime.DAY, cursor.getString(7));
				hm.put(OnTime.PS, cursor.getString(8));
				hm.put(OnTime.Milli, cursor.getString(9));
				String time = cursor.getString(2) + ":" + cursor.getString(3)
						+ "~" + cursor.getString(4) + ":" + cursor.getString(5);
				hm.put(OnTime.Time, time);
				data.add(hm);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return data;
	}

	// Show in dialog
	public ArrayList<HashMap<String, String>> getAlarmTask(String TimerNow) {

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.query(OnTime.TABLE_TASK, new String[] { "_id",
				"event", "beginHour", "beginMin", "finishHour", "finishMin",
				"date", "day", "ps", "milli" }, "milli < " + TimerNow, null,
				null, null, "milli desc", "1");
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(OnTime.EVENT, cursor.getString(1));
				hm.put(OnTime.PS, cursor.getString(8));
				String time = cursor.getString(2) + ":" + cursor.getString(3)
						+ "~" + cursor.getString(4) + ":" + cursor.getString(5);
				hm.put(OnTime.Time, time);
				hm.put(OnTime.BEGIN_MIN, cursor.getString(3));
				data.add(hm);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return data;
	}

	public ArrayList<HashMap<String, String>> getTaskDate(String TimerNow) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.query(OnTime.TABLE_TASK, new String[] { "date",
				"milli" }, "milli > " + TimerNow, null, null, null,
				"milli asc");
		
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(OnTime.DATE, cursor.getString(1));
				data.add(hm);
				cursor.moveToNext();
			}
		}
		cursor.close();
		Log.i("OnTimeDAO", "date collected");
		return data;

	}

	public int getTotalEvents() {
		
		int TotalEvents = 0;
		Cursor cursor = db.query(OnTime.TABLE_EVENT, new String[] { "_id" },
				null, null, null, null, null);
		if (cursor.getCount() != 0) {
			TotalEvents = cursor.getCount();

		}

		cursor.close();
		return TotalEvents;

	}

}
