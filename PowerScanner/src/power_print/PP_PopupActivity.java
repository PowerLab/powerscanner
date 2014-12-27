package power_print;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import edu.cnu.PowerTutor.R;

public class PP_PopupActivity extends Activity {
	private ArrayList<String> FileList;
	private boolean noFile = false;
	private Button btn_Select, btn_Cancel;
	private int selected_file = -1;
	private ListView lv_file;
	private ArrayAdapter<String> Adapter;
	private static final String sdPath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/Power_Scanner";
	private String FilePath;
	private static String errMsg = "Failed to the File Selection";
	private Bundle extra_FilePath = new Bundle();
	private Intent intent_FilePath = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("TAG", "onCreate PP_PopupActivity");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pp__popup);

		lv_file = (ListView) findViewById(R.id.PP_pop_fileList);
		FileList = returnFileList();
		getFileList();

		btn_Select = (Button) findViewById(R.id.PP_pop_btn_select);
		btn_Cancel = (Button) findViewById(R.id.PP_pop_btn_cancel);

		btn_Select.setOnClickListener(l_btn_Select);
		btn_Cancel.setOnClickListener(l_btn_Cancel);
	}

	protected void onPause() {
		super.onPause();
		Log.d("TAG", "onPause PP_PopupActivity");
	}

	private void getFileList() {
		if (FileList == null || FileList.size() == 0) {
			noFile = true;
			FileList.add("Not found file in this directory");
			lv_file.setChoiceMode(ListView.CHOICE_MODE_NONE);

		} else {
			noFile = false;
			lv_file.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}

		Adapter = new ArrayAdapter<String>(this,
				R.layout.activity_pp__list_item_checked_text_view, FileList);
		lv_file.setAdapter(Adapter);
		lv_file.setOnItemClickListener(mItemClickListener);
	}

	private ArrayList<String> returnFileList() {
		try {
			File[] files = new File(sdPath).listFiles();
			if (files == null) {
				return null;
			} else {
				ArrayList<String> file_list = new ArrayList<String>();
				for (int i = 0; i < files.length; i++) {
					file_list.add(files[i].getName());
				}
				return file_list;
			}
		} catch (Exception e) {
			return null;
		}
	}

	OnClickListener l_btn_Cancel = new OnClickListener() {
		public void onClick(View v) {
			extra_FilePath.putString("PATH", errMsg);
			intent_FilePath.putExtras(extra_FilePath);
			setResult(RESULT_CANCELED, intent_FilePath);

			finish();
		}
	};

	OnClickListener l_btn_Select = new OnClickListener() {
		public void onClick(View v) {
			/*
			 * When you select file and click open button, Add Source code ->
			 * File open
			 */
			if (noFile == false && selected_file != -1) {
				FilePath = sdPath +"/"+ FileList.get(selected_file);
				Toast.makeText(PP_PopupActivity.this, FilePath,
						Toast.LENGTH_SHORT).show();
				extra_FilePath.putString("PATH", FilePath);
				intent_FilePath.putExtras(extra_FilePath);
				setResult(RESULT_OK, intent_FilePath);
				finish();
			} else
				Toast.makeText(PP_PopupActivity.this, errMsg,
						Toast.LENGTH_SHORT).show();
		}
	};

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (noFile == false) {
				selected_file = position;
			}
		}
	};
}
