package ru.kor_inc.andy;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import au.com.bytecode.opencsv.*;
import java.io.*;
import java.text.*;
import java.util.*;
import ru.kor_inc.andy.*;

public class CsvExporter extends AsyncTask<String, Void, Boolean>
 {
	 public Activity act;
	 public DbTool db;
	 
	public CsvExporter (Activity acti, DbTool db){
		this.act=acti;
		this.db=db;
	}
	 
	 
//	private final ProgressDialog dialog = new ProgressDialog(act);

	@Override
	protected void onPreExecute() {
		//act.dialog.setMessage("Exporting database...");
		//act.dialog.show();
	}

	protected Boolean doInBackground(final String... args) {

		Intent intent = act.getIntent();
		final String currentTable = intent.getStringExtra("currentTable");

		File dbFile = act.getDatabasePath(act.getFilesDir().getPath()+"databases/firstAnyDynamicDataTable.db");
		System.out.println(dbFile);  // displays the data base path in your logcat 
		File exportDir = new File(Environment.getExternalStorageDirectory(), "AnDyExport");

		if (!exportDir.exists()) { exportDir.mkdirs(); }

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String date = sdf.format(new Date(System.currentTimeMillis()));

		File file = new File(exportDir, "newstuffexport"+".csv");
		try {
			file.createNewFile();
			CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
			Cursor curCSV = db.getCursor(currentTable, act);
			csvWrite.writeNext(curCSV.getColumnNames());
			while(curCSV.moveToNext()) {
				String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2), curCSV.getString(3),curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7),curCSV.getString(8)};
				csvWrite.writeNext(arrStr);
			}
			// Леха выше : добавил curCSV.getString(с 5 по 8) , в экспорте довалились недостающие колонки.
			csvWrite.close();
			curCSV.close();
			return true;
		} catch(SQLException sqlEx) {
			Log.e("Box", sqlEx.getMessage(), sqlEx);
			return false;
		} catch (IOException e) {
			Log.e("Box", e.getMessage(), e);
			return false;
		}
	}

	protected void onPostExecute(final Boolean success) {
		//if (this.dialog.isShowing()) { this.dialog.dismiss(); }
		if (success) {
			Toast.makeText(act, "все ок", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(act, "Что-то с экспортом не так пошло...", Toast.LENGTH_SHORT).show();
		}
	}
}
