package kazaf.chen.ontime;

import kazaf.chen.ontime.Constant.OnTime;
import android.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class OnTimeHelper extends SQLiteOpenHelper {

	String exampleEvent;
	String examplePS;
	public OnTimeHelper(Context context) {
		super(context, "OnTime", null, 1);
		exampleEvent = context.getString(kazaf.chen.intime.R.string.example_event);
		examplePS = context.getString(kazaf.chen.intime.R.string.example_ps);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + OnTime.TABLE_EVENT + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, event TEXT NOT NULL, hour TEXT NOT NULL, minute TEXT NOT NULL, ps TEXT NOT NULL );");
		db.execSQL("CREATE TABLE " + OnTime.TABLE_TASK + "( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"event TEXT NOT NULL, " +
				"beginHour TEXT NOT NULL, " +
				"beginMin TEXT NOT NULL, " +
				"finishHour TEXT NOT NULL, " +
				"finishMin TEXT NOT NULL, " +
				"date DATETIME, " +
				"day TEXT NOT NULL, " + 
				"milli int, " +
				"ps TEXT NOT NULL );");
		db.execSQL("INSERT INTO " + OnTime.TABLE_EVENT + " ( event, hour, minute, ps) VALUES ( '" + exampleEvent + "', 0, 10, '" + examplePS +"' );");
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + OnTime.TABLE_EVENT);
		db.execSQL("DROP TABLE IF EXISTS " + OnTime.TABLE_TASK);
		onCreate(db);

	}
}
