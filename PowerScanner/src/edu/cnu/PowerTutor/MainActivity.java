package edu.cnu.PowerTutor;

import power_dataManagement.PDM_MainActivity;
import power_measure.PM_MainActivity;
import power_print.PP_MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	static final boolean DEBUG = true;

	Button btn_measure;
	Button btn_print;
	Button btn_filemanagement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_measure = (Button) findViewById(R.id.Main_btn_measure);
		btn_print = (Button) findViewById(R.id.Main_btn_print);
		btn_filemanagement = (Button) findViewById(R.id.Main_btn_filemanagement);

		btn_measure.setOnClickListener(l_measure);
		btn_print.setOnClickListener(l_print);
		btn_filemanagement.setOnClickListener(l_filemanagement);

		if (DEBUG) {
			Log.w("TAG", "onCreate MainActivity");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (DEBUG)
			Log.w("TAG", "onResume MainActivity");
	}

	@Override
	public void onPause() {
		super.onPause();
		if (DEBUG)
			Log.w("TAG", "onPause MainActivity");
	}

	OnClickListener l_measure = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (DEBUG)
				Log.i("TAG", "Clicked 측정 Button");

			Intent intent = new Intent(MainActivity.this, PM_MainActivity.class);
			startActivity(intent);
		}
	};
	OnClickListener l_print = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (DEBUG)
				Log.i("TAG", "Clicked 출력 Button");

			Intent intent = new Intent(MainActivity.this, PP_MainActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener l_filemanagement = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (DEBUG)
				Log.i("TAG", "Clicked 데이터 관리 Button");

			Intent intent = new Intent(MainActivity.this,
					PDM_MainActivity.class);
			startActivity(intent);
		}
	};
}
