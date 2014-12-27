package power_measure;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.InflaterInputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnu.PowerTutor.R;
import edu.cnu.PowerTutor.service.ICounterService;
import edu.cnu.PowerTutor.service.PowerEstimator;
import edu.cnu.PowerTutor.service.UMLoggerService;
import edu.cnu.PowerTutor.service.UidInfo;
import edu.cnu.PowerTutor.ui.EditPreferences;
import edu.cnu.PowerTutor.ui.PowerTabs;
import edu.cnu.PowerTutor.ui.PowerViewer;
import edu.umich.PowerTutor.phone.PhoneSelector;
import edu.umich.PowerTutor.util.Counter;
import edu.umich.PowerTutor.util.Recycler;
import edu.umich.PowerTutor.util.SystemInfo;

public class PM_MainActivity extends Activity implements Runnable {
	static final boolean DEBUG = true;
	private static boolean searching = false;
	private static final int MENU_PREFERENCES = 0;
	private static final int MENU_KEY = 10;
	private static final int MENU_WINDOW = 11;

	private static final int DIALOG_START_SENDING = 0;
	private static final int DIALOG_STOP_SENDING = 1;
	private static final int DIALOG_TOS = 2;
	private static final int DIALOG_RUNNING_ON_STARTUP = 3;
	private static final int DIALOG_NOT_RUNNING_ON_STARTUP = 4;
	private static final int DIALOG_UNKNOWN_PHONE = 5;
	private static final int DIALOG_KEY = 6;
	private static final int DIALOG_WINDOW = 7;

	public static final String CURRENT_VERSION = "1.2"; // Don't change this...
	public static final String SERVER_IP = "spidermonkey.eecs.umich.edu";
	public static final int SERVER_PORT = 5204;

	private SharedPreferences prefs;
	private ICounterService counterService;
	private CounterServiceConnection conn;
	private Intent serviceIntent;

	Button btn_startNquit;
	Button btn_save;
	LinearLayout llay_ListView;
	LinearLayout llay_FilterView;
	// ///////////////////////////////////////////////////////////////////////
	private static final double HIDE_UID_THRESHOLD = 0.1;

	public static final int KEY_CURRENT_POWER = 0;
	public static final int KEY_AVERAGE_POWER = 1;
	public static final int KEY_TOTAL_ENERGY = 2;
	private static final CharSequence[] KEY_NAMES = { "Current power",
			"Average power", "Energy usage" };
	private int noUidMask;
	private String[] componentNames;
	private LinearLayout filterGroup;
	private LinearLayout topGroup;

	// //////////////////////////////////////////////////////////
	private Handler handler;
	private TextView mainPageText;
	private ScrollView scrollView;

	private void createDirectory() {
		String sdPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File dir = new File(sdPath, "Power_Scanner");

		if (!dir.exists())
			dir.mkdir();
	}

	private void initButton() {
		btn_startNquit = (Button) findViewById(R.id.PM_btn_startNquit);
		btn_startNquit.setOnClickListener(l_startNquit);

		btn_save = (Button) findViewById(R.id.PM_btn_save);
		btn_save.setOnClickListener(l_save);
		btn_save.setEnabled(false);
	}

	private void initMainPageTextViewinLayout() {
		mainPageText = new TextView(this);
		mainPageText.setText(R.string.pm_text_info_main);
		mainPageText.setGravity(Gravity.CENTER);
	}

	private void addMainPageTextViewinLayout() {
		llay_ListView.setVerticalGravity(Gravity.CENTER);
		llay_ListView.addView(mainPageText);
	}

	private void removeMainPageTextViewinLayout() {
		llay_ListView.removeView(mainPageText);
	}

	private void addListViewinLayout() {
		topGroup = new LinearLayout(this);
		topGroup.setOrientation(LinearLayout.VERTICAL);

		scrollView = new ScrollView(this);
		scrollView.addView(topGroup);

		llay_ListView.addView(scrollView);
	}

	private void removeListViewinLayout() {
		scrollView.removeAllViews();
		llay_ListView.removeViewInLayout(scrollView);
	}

	private String makeFileNameYMDHMS() {
		Date date = new Date();

		return String.format("%02d%02d%02d_%02d%02d%02d",
				(date.getYear() - 100), (date.getMonth() + 1), date.getDate(),
				date.getHours(), date.getMinutes(), date.getSeconds());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DEBUG)
			Log.w("TAG", "onCreate PM_MainActivity");

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		serviceIntent = new Intent(this, UMLoggerService.class);
		conn = new CounterServiceConnection();

		if (savedInstanceState != null) {
			componentNames = savedInstanceState
					.getStringArray("componentNames");
			noUidMask = savedInstanceState.getInt("noUidMask");
		}

		setContentView(R.layout.activity_pm__main);

		ArrayAdapter<?> adapterxaxis = ArrayAdapter.createFromResource(this,
				R.array.xaxis, android.R.layout.simple_spinner_item);
		adapterxaxis
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		initButton();
		if (searching == true)
			btn_startNquit.setText("��\t��");

		filterGroup = new LinearLayout(this);
		filterGroup.setOrientation(LinearLayout.HORIZONTAL);
		filterGroup.setMinimumHeight(50);

		llay_ListView = (LinearLayout) findViewById(R.id.PM_LinearLayout3);
		llay_FilterView = (LinearLayout) findViewById(R.id.PM_LinearLayout4);
		llay_FilterView.addView(filterGroup);

		initMainPageTextViewinLayout();
		addMainPageTextViewinLayout();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (DEBUG)
			Log.w("TAG", "onResume PM_MainActivity");

		addListViewinLayout();

		handler = new Handler();
		handler.postDelayed(this, 100);
		getApplicationContext().bindService(serviceIntent, conn, 0);

		if (prefs.getBoolean("firstRun", true)) {
			if (PhoneSelector.getPhoneType() == PhoneSelector.PHONE_UNKNOWN) {
				showDialog(DIALOG_UNKNOWN_PHONE);
			} else {
				showDialog(DIALOG_TOS);
			}
		}
		Intent startingIntent = getIntent();
		if (startingIntent.getBooleanExtra("isFromIcon", false)) {
			Intent copyIntent = (Intent) getIntent().clone();
			copyIntent.putExtra("isFromIcon", false);
			setIntent(copyIntent);
			Intent intent = new Intent(this, PowerTabs.class);
			startActivity(intent);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (DEBUG)
			Log.w("TAG", "onPause PM_MainActivity");

		removeListViewinLayout();

		getApplicationContext().unbindService(conn);
		handler.removeCallbacks(this);
		handler = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (DEBUG)
			Log.w("TAG", "onSaveInstanceState PM_MainActivity");

		outState.putStringArray("componentNames", componentNames);
		outState.putInt("noUidMask", noUidMask);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, MENU_PREFERENCES, 0, "System Viewer");
		menu.add(0, MENU_KEY, 0, "Display Type");
		menu.add(0, MENU_WINDOW, 0, "Time Span");
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		/*
		 * We need to make sure that the user can't cause any of the dialogs to
		 * be created before we have contacted the Power Tutor service to get
		 * the component names and such.
		 */
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setEnabled(counterService != null);
		}

		if (DEBUG)
			Log.w("TAG", "onPrepareOptionsMenu PM_MainActivity");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PREFERENCES:
			Intent intent = new Intent(this, PowerTabs.class);
			startActivity(intent);
			return true;
		case MENU_KEY:
			showDialog(DIALOG_KEY);
			return true;
		case MENU_WINDOW:
			showDialog(DIALOG_WINDOW);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** This function includes all the dialog constructor */
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_TOS:
			builder.setMessage(R.string.information)
					.setCancelable(false)
					.setPositiveButton("Agree",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									prefs.edit().putBoolean("firstRun", false)
											.putBoolean("runOnStartup", true)
											.putBoolean("sendPermission", true)
											.commit();
									dialog.dismiss();
									createDirectory();
								}
							})
					.setNegativeButton("Do not agree",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									prefs.edit().putBoolean("firstRun", true)
											.commit();
									finish();
								}
							});
			return builder.create();
		case DIALOG_STOP_SENDING:
			builder.setMessage(R.string.stop_sending_text)
					.setCancelable(true)
					.setPositiveButton("Stop",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									prefs.edit()
											.putBoolean("sendPermission", false)
											.commit();
									dialog.dismiss();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			return builder.create();
		case DIALOG_START_SENDING:
			builder.setMessage(R.string.start_sending_text)
					.setCancelable(true)
					.setPositiveButton("Start",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									prefs.edit()
											.putBoolean("sendPermission", true)
											.commit();
									dialog.dismiss();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			return builder.create();
		case DIALOG_RUNNING_ON_STARTUP:
			builder.setMessage(R.string.running_on_startup).setCancelable(true)
					.setNeutralButton("Ok", null);
			return builder.create();
		case DIALOG_NOT_RUNNING_ON_STARTUP:
			builder.setMessage(R.string.not_running_on_startup)
					.setCancelable(true).setNeutralButton("Ok", null);
			return builder.create();
		case DIALOG_UNKNOWN_PHONE:
			builder.setMessage(R.string.unknown_phone)
					.setCancelable(false)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									showDialog(DIALOG_TOS);
								}
							});
			return builder.create();
		case DIALOG_KEY:
			builder.setTitle("Select sort key");
			builder.setItems(KEY_NAMES, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					prefs.edit().putInt("topKeyId", item).commit();
				}
			});
			return builder.create();
		case DIALOG_WINDOW:
			builder.setTitle("Select window type");
			builder.setItems(Counter.WINDOW_NAMES,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							prefs.edit().putInt("topWindowType", item).commit();
						}
					});
			return builder.create();

		}
		return null;
	}

	private class CounterServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className,
				IBinder boundService) {
			counterService = ICounterService.Stub
					.asInterface((IBinder) boundService);

			try {
				componentNames = counterService.getComponents();
				noUidMask = counterService.getNoUidMask();
				filterGroup.removeAllViews();
				for (int i = 0; i < componentNames.length; i++) {
					int ignMask = prefs.getInt("topIgnoreMask", 0);
					if ((noUidMask & 1 << i) != 0)
						continue;
					final TextView filterToggle = new TextView(
							PM_MainActivity.this);
					final int index = i;
					filterToggle.setText(componentNames[i]);
					filterToggle.setGravity(Gravity.CENTER);
					filterToggle
							.setTextColor((ignMask & 1 << index) == 0 ? 0xFFFFFFFF
									: 0xFF888888);
					filterToggle
							.setBackgroundColor(filterGroup.getChildCount() % 2 == 0 ? 0xFF444444
									: 0xFF555555);
					filterToggle.setFocusable(true);
					filterToggle.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							int ignMask = prefs.getInt("topIgnoreMask", 0);
							if ((ignMask & 1 << index) == 0) {
								prefs.edit()
										.putInt("topIgnoreMask",
												ignMask | 1 << index).commit();
								filterToggle.setTextColor(0xFF888888);
							} else {
								prefs.edit()
										.putInt("topIgnoreMask",
												ignMask & ~(1 << index))
										.commit();
								filterToggle.setTextColor(0xFFFFFFFF);
							}
						}
					});
					filterGroup.addView(filterToggle,
							new LinearLayout.LayoutParams(
									ViewGroup.LayoutParams.FILL_PARENT,
									ViewGroup.LayoutParams.FILL_PARENT, 1f));
				}
			} catch (RemoteException e) {
				counterService = null;
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			counterService = null;
			getApplicationContext().unbindService(conn);
			getApplicationContext().bindService(serviceIntent, conn, 0);

			Toast.makeText(PM_MainActivity.this, "Profiler stopped",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void run() {
		refreshView();

		if (handler != null) {
			handler.postDelayed(this, 2 * PowerEstimator.ITERATION_INTERVAL);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////
	private void refreshView() {
		if (counterService == null) {
			return;
		}

		int keyId = prefs.getInt("topKeyId", KEY_TOTAL_ENERGY);
		try {

			byte[] rawUidInfo = counterService.getUidInfo(
					prefs.getInt("topWindowType", Counter.WINDOW_TOTAL),
					noUidMask | prefs.getInt("topIgnoreMask", 0));
			if (rawUidInfo != null) {
				UidInfo[] uidInfos = (UidInfo[]) new ObjectInputStream(
						new ByteArrayInputStream(rawUidInfo)).readObject();
				double total = 0;
				for (UidInfo uidInfo : uidInfos) {
					if (uidInfo.uid == SystemInfo.AID_ALL)
						continue;
					switch (keyId) {
					case KEY_CURRENT_POWER:
						uidInfo.key = uidInfo.currentPower;
						uidInfo.unit = "W";
						break;
					case KEY_AVERAGE_POWER:
						uidInfo.key = uidInfo.totalEnergy
								/ (uidInfo.runtime == 0 ? 1 : uidInfo.runtime);
						uidInfo.unit = "W";
						break;
					case KEY_TOTAL_ENERGY:
						uidInfo.key = uidInfo.totalEnergy;
						uidInfo.unit = "J";
						break;
					default:
						uidInfo.key = uidInfo.currentPower;
						uidInfo.unit = "W";
					}
					total += uidInfo.key;
				}
				if (total == 0)
					total = 1;
				for (UidInfo uidInfo : uidInfos) {
					uidInfo.percentage = 100.0 * uidInfo.key / total;
				}
				Arrays.sort(uidInfos);

				int sz = 0;
				int item_cnt = topGroup.getChildCount();
				for (int i = 0; i < uidInfos.length; i++) {
					if (uidInfos[i].uid == SystemInfo.AID_ALL
							|| uidInfos[i].percentage < HIDE_UID_THRESHOLD) {
						continue;
					}

					UidPowerView powerView;
					if (sz < item_cnt) {
						powerView = (UidPowerView) topGroup.getChildAt(sz);
					} else {
						powerView = UidPowerView.obtain(this, getIntent());
						topGroup.addView(powerView);
					}
					powerView.setBackgroundDrawable(null);
					powerView.setBackgroundColor((sz & 1) == 0 ? 0xFF000000
							: 0xFF222222);
					powerView.init(uidInfos[i], keyId);
					sz++;
				}
				for (int i = sz; i < item_cnt; i++) {
					UidPowerView powerView = (UidPowerView) topGroup
							.getChildAt(i);
					powerView.recycle();
				}
				topGroup.removeViews(sz, topGroup.getChildCount() - sz);
			}
		} catch (IOException e) {
		} catch (RemoteException e) {
		} catch (ClassNotFoundException e) {
		} catch (ClassCastException e) {
		}

		if (keyId == KEY_CURRENT_POWER) {
			setTitle(KEY_NAMES[keyId]);
		} else {
			setTitle(KEY_NAMES[keyId]
					+ " over "
					+ Counter.WINDOW_DESCS[prefs.getInt("topWindowType",
							Counter.WINDOW_TOTAL)]);
		}

		removeMainPageTextViewinLayout();
		llay_ListView.setVerticalGravity(Gravity.TOP);
	}

	private static class UidPowerView extends LinearLayout {
		private static Recycler<UidPowerView> recycler = new Recycler<UidPowerView>();

		public static UidPowerView obtain(Activity activity, Intent startIntent) {
			UidPowerView result = recycler.obtain();
			if (result == null)
				return new UidPowerView(activity, startIntent);
			return result;
		}

		public void recycle() {
			recycler.recycle(this);
		}

		private UidInfo uidInfo;
		private String name;
		private Drawable icon;

		private ImageView imageView;
		private TextView textView;

		private UidPowerView(final Activity activity, final Intent startIntent) {
			super(activity);
			setMinimumHeight(50);
			setOrientation(LinearLayout.HORIZONTAL);
			imageView = new ImageView(activity);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setAdjustViewBounds(true);
			imageView.setMaxHeight(40);
			imageView.setMaxWidth(40);
			imageView.setMinimumWidth(50);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			textView = new TextView(activity);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));

			addView(imageView);
			addView(textView);

			setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent viewIntent = new Intent(v.getContext(),
							PowerTabs.class);
					viewIntent.putExtras(startIntent);
					viewIntent.putExtra("uid", uidInfo.uid);
					activity.startActivityForResult(viewIntent, 0);
				}
			});
			setFocusable(true);
		}

		public void init(UidInfo uidInfo, int keyType) {
			SystemInfo sysInfo = SystemInfo.getInstance();
			this.uidInfo = uidInfo;
			PackageManager pm = getContext().getPackageManager();
			name = sysInfo.getUidName(uidInfo.uid, pm);
			icon = sysInfo.getUidIcon(uidInfo.uid, pm);
			imageView.setImageDrawable(icon);
			String prefix;
			if (uidInfo.key > 1e12) {
				prefix = "G";
				uidInfo.key /= 1e12;
			} else if (uidInfo.key > 1e9) {
				prefix = "M";
				uidInfo.key /= 1e9;
			} else if (uidInfo.key > 1e6) {
				prefix = "k";
				uidInfo.key /= 1e6;
			} else if (uidInfo.key > 1e3) {
				prefix = "";
				uidInfo.key /= 1e3;
			} else {
				prefix = "m";
			}
			long secs = (long) Math.round(uidInfo.runtime);

			textView.setText(String.format(
					"%1$.1f%% [%3$d:%4$02d:%5$02d] %2$s\n" + "%6$.1f %7$s%8$s",
					uidInfo.percentage, name, secs / 60 / 60, (secs / 60) % 60,
					secs % 60, uidInfo.key, prefix, uidInfo.unit));
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////
	OnClickListener l_startNquit = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (DEBUG)
				Log.i("TAG", "Clicked ���� / ���� Button");

			String btn_text = btn_startNquit.getText().toString();
			if (btn_text.equals(getResources()
					.getString(R.string.pm_text_start))) {
				searching = true;
				Log.i("TAG", "����");

				btn_startNquit.setText(R.string.pm_text_quit);
				btn_startNquit.setEnabled(true);

				if (counterService == null) {
					if (conn == null) {
						Toast.makeText(PM_MainActivity.this,
								"Profiler failed to start", Toast.LENGTH_SHORT)
								.show();
					} else {
						removeMainPageTextViewinLayout();
						mainPageText.setText("Loading . . .");
						addMainPageTextViewinLayout();

						startService(serviceIntent);
					}
				}
			} else {
				Log.i("TAG", "����");
				searching = false;
				btn_startNquit.setText(R.string.pm_text_start);
				btn_save.setEnabled(true);
				btn_startNquit.setEnabled(false);

				topGroup.removeAllViews();

				removeMainPageTextViewinLayout();
				mainPageText.setText(R.string.pm_text_info_save);
				addMainPageTextViewinLayout();

				stopService(serviceIntent);
			}
		}
	};

	OnClickListener l_save = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (DEBUG)
				Log.i("TAG", "Clicked ���� Button");

			removeMainPageTextViewinLayout();
			mainPageText.setText(R.string.pm_text_info_main);
			addMainPageTextViewinLayout();

			new Thread() {
				public void start() {
					File writeFile = new File(
							Environment.getExternalStorageDirectory()
									+ "/Power_Scanner", "PowerTrace"
									+ makeFileNameYMDHMS() + ".log");
					try {
						InflaterInputStream logIn = new InflaterInputStream(
								openFileInput("PowerTrace.log"));
						BufferedOutputStream logOut = new BufferedOutputStream(
								new FileOutputStream(writeFile));

						byte[] buffer = new byte[20480];
						int start = logIn.read(buffer);
						for (int ln = start; ln != -1; ln = logIn.read(buffer)) {
							logOut.write(buffer, 0, ln);
						}
						logIn.close();
						logOut.close();
						Toast.makeText(
								PM_MainActivity.this,
								"Wrote log to \n" + writeFile.getAbsolutePath(),
								Toast.LENGTH_SHORT).show();
						return;
					} catch (java.io.EOFException e) {
						Toast.makeText(PM_MainActivity.this,
								"Failed to write log to sdcard",
								Toast.LENGTH_SHORT).show();
						return;
					} catch (IOException e) {
						Toast.makeText(PM_MainActivity.this,
								"Failed to write log to sdcard",
								Toast.LENGTH_SHORT).show();
						return;
					}

				}
			}.start();

			btn_save.setEnabled(false);
			btn_startNquit.setEnabled(true);
		}
	};
}
