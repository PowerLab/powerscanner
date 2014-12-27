package power_dataManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.cnu.PowerTutor.R;
import edu.cnu.PowerTutor.ui.AppChoiceView;

public class PDM_MainActivity extends Activity {
	private Button PDM_btn_trans;
	private Button PDM_btn_receive;
	private Button PDM_btn_filemanagement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("TAG", "onCreate PDM_MainActivity");
		
		setContentView(R.layout.activity_pdm__main);

		PDM_btn_trans = (Button) findViewById(R.id.PDM_btn_trans);
		PDM_btn_receive = (Button) findViewById(R.id.PDM_btn_receive);
		PDM_btn_filemanagement = (Button) findViewById(R.id.PDM_btn_filemanagement);

		PDM_btn_trans.setOnClickListener(l_trans);
		PDM_btn_receive.setOnClickListener(l_receive);
		PDM_btn_filemanagement.setOnClickListener(l_filemanagement);
	}

	OnClickListener l_trans = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PDM_MainActivity.this,AppChoiceView.class);
			startActivity(intent);
		}
	};

	OnClickListener l_receive = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	OnClickListener l_filemanagement = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PDM_MainActivity.this, PDM_PopupActivity.class);
			startActivity(intent);
		}
	};
}
