package power_dataManagement;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
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

public class PDM_PopupActivity extends Activity {
	private ArrayList<String> FileList;
	private String[] noFile;
	private Button btn_DEL, btn_Cancel;
	private int selected_file = -1;
	private ListView lv_file;
	private ArrayAdapter<String> Adapter;
	private static final String sdPath = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("TAG", "onCreate PDM_PopupActivity");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pdm__popup);

		lv_file = (ListView) findViewById(R.id.PDM_pop_fileList);
		FileList = returnFileList();
		getFileList();

		btn_DEL = (Button) findViewById(R.id.PDM_pop_btn_delete);
		btn_Cancel = (Button) findViewById(R.id.PDM_pop_btn_cancel);

		btn_DEL.setOnClickListener(l_btn_DEL);
		btn_Cancel.setOnClickListener(l_btn_Cancel);
	}

	private void getFileList() {

		if (FileList == null || FileList.size() == 0) {
			noFile = new String[1];
			noFile[0] = "Not found file in directory";
			Adapter = new ArrayAdapter<String>(this,
					R.layout.activity_pdm__list_item_checked_text_view, noFile);
			lv_file.setChoiceMode(ListView.CHOICE_MODE_NONE);
			lv_file.setAdapter(Adapter);
		} else {
			noFile = null;
			Adapter = new ArrayAdapter<String>(this,
					R.layout.activity_pdm__list_item_checked_text_view, FileList);
			lv_file.setAdapter(Adapter);
			lv_file.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
		lv_file.setOnItemClickListener(mItemClickListener);
	}

	private ArrayList<String> returnFileList() {
		try {
			File file = new File(sdPath + "/Power_Scanner");
			File[] files = file.listFiles();
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
			finish();
		}
	};

	OnClickListener l_btn_DEL = new OnClickListener() {
		public void onClick(View v) {
			if (selected_file != -1) {
				new File(sdPath + "/Power_Scanner/"
						+ FileList.get(selected_file)).delete();
				FileList.remove(selected_file);

				selected_file = -1;
				getFileList();
			} else {
				String mes = "Failed to the file remove.";
				Toast.makeText(PDM_PopupActivity.this, mes, Toast.LENGTH_SHORT)
						.show();
			}
		}

	};

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (noFile == null) {
				selected_file = position;
			}
		}
	};

}
