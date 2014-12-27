package power_print;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnu.PowerTutor.R;

public class PP_MainActivity extends Activity {
	private static final int request_FilePath = 1;
	private String filePath;
	private boolean isFileSelected = false;
	private static final String TAG = PP_MainActivity.class.getSimpleName();
	private byte[] log = null;
	private final int MENU_DOWNLOAD = 0; // menu
	private final int MENU_ALL = 1;
	private final int MENU_EXIT = -1;
	private int MENU_MODE = MENU_DOWNLOAD;

	private PackageManager pm;
	private LinearLayout pp_LoadingContainer;
	private ListView pp_ListView = null;
	private PP_Adapter pp_Adapter = null;

	private HashMap<String, String> map_AppList;

	private class ViewContainer {
		public ImageView mIcon;
		public TextView mName;
		public TextView mPacakge;
		public TextView mAppVol;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("TAG", "onCreate PP_MainActivity");
		setContentView(R.layout.activity_pp__main);

		Intent intent = new Intent(PP_MainActivity.this, PP_PopupActivity.class);
		startActivityForResult(intent, request_FilePath);

		map_AppList = new HashMap<String, String>();

		pp_LoadingContainer = (LinearLayout) findViewById(R.id.loading_container);
		pp_ListView = (ListView) findViewById(R.id.PP_listView);

		pp_Adapter = new PP_Adapter(this);
		pp_ListView.setAdapter(pp_Adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.w("TAG", "onResume PP_MainActivity");
		if (filePath != null) {
			PP_LogParsing logClass = new PP_LogParsing(filePath);
			logClass.logParsing_AppList();
			logClass.logParsing_CPU();
			map_AppList = logClass.parsingMap_Merge();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.w("TAG", "onPause PP_MainActivity");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ALL, 2, R.string.pp_menu_all);
		menu.add(0, MENU_EXIT, 3, R.string.pp_menu_exit);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	//	menu.findItem(MENU_ALL).setVisible(true);
		menu.findItem(MENU_EXIT).setVisible(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int menuId = item.getItemId();

		switch (menuId) {
		case MENU_EXIT:
			this.finish();
		case MENU_ALL:
			MENU_MODE = MENU_ALL;
			startTask();
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case request_FilePath:
			if (resultCode == RESULT_OK) {
				filePath = intent.getExtras().getString("PATH");
				isFileSelected = true;
				startTask();
			} else if (resultCode == RESULT_CANCELED) {
				filePath = null;
				isFileSelected = false;
				finish();
			}
		}
	}

	private void startTask() {
		new AppTask().execute();
	}

	private class AppTask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			setLoadingView(true);
		}

		protected Void doInBackground(Void... params) {
			pp_Adapter.rebuild();

			return null;
		}

		protected void onPostExecute(Void result) {

			setLoadingView(false);
			pp_Adapter.notifyDataSetChanged();
		}
	};

	private void setLoadingView(boolean isView) {
		if (isView) {
			pp_LoadingContainer.setVisibility(View.VISIBLE);
			pp_ListView.setVisibility(View.GONE);
		} else {
			pp_ListView.setVisibility(View.VISIBLE);
			pp_LoadingContainer.setVisibility(View.GONE);
		}
	}

	private class PP_Adapter extends BaseAdapter {
		private Context mContext = null;
		private List<ApplicationInfo> mAppList = null;
		private ArrayList<pp_AppInfo> mListData = new ArrayList<pp_AppInfo>();

		public PP_Adapter(Context mContext) {
			super();
			this.mContext = mContext;
		}

		public int getCount() {
			return mListData.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewContainer App_info;

			if (convertView == null) {
				App_info = new ViewContainer();
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_pp_list_item,
						null);

				App_info.mIcon = (ImageView) convertView
						.findViewById(R.id.app_icon);
				App_info.mName = (TextView) convertView
						.findViewById(R.id.app_name);
				App_info.mPacakge = (TextView) convertView
						.findViewById(R.id.app_package);
				App_info.mAppVol = (TextView) convertView
						.findViewById(R.id.app_voltage);

				convertView.setTag(App_info);
			} else {
				App_info = (ViewContainer) convertView.getTag();
			}

			pp_AppInfo data = mListData.get(position);

			if (data.mIcon != null) {
				App_info.mIcon.setImageDrawable(data.mIcon);
			}
			App_info.mName.setText(data.mAppName);
			App_info.mPacakge.setText(data.mAppPackge);
			App_info.mAppVol.setText(data.mAppVol + "mW");

			return convertView;
		}

		public void rebuild() {
			if (mAppList == null) {
				Log.d(TAG, "Is Empty Application List");

				pm = PP_MainActivity.this.getPackageManager();

				mAppList = pm
						.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
								| PackageManager.GET_DISABLED_COMPONENTS);
			}

			mListData.clear();
			pp_AppInfo addInfo = null;

			for (ApplicationInfo app : mAppList) {
				addInfo = new pp_AppInfo();
				String avg_vol;
				addInfo.mAppName = app.loadLabel(pm).toString();
				if ((avg_vol = map_AppList.get(addInfo.mAppName)) != null) {
					addInfo.mIcon = app.loadIcon(pm);

					addInfo.mAppPackge = app.packageName;
					addInfo.mAppVol = avg_vol;
					mListData.add(addInfo);
				}
			}
			Collections.sort(mListData, pp_AppInfo.ALPHA_COMPARATOR);
		}
	}
}
