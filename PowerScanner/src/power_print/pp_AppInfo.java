package power_print;

import java.text.Collator;
import java.util.Comparator;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class pp_AppInfo {
	public Drawable mIcon = null;
	public String mAppName = null;
	public String mAppPackge = null;
	public String mAppVol = null;
	
    public static interface AppFilter {
        public void init();
        public boolean filterApp(ApplicationInfo info);
    }
    
    public static final AppFilter SETUP_FILTER = new AppFilter() {
        public void init(){}        
        @Override
        public boolean filterApp(ApplicationInfo info) {
            if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                return true;
            }
            return false;
        }
    };
 
    // sorting by App name
    public static final Comparator<pp_AppInfo> ALPHA_COMPARATOR = new Comparator<pp_AppInfo>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(pp_AppInfo object1, pp_AppInfo object2) {
            return sCollator.compare(object1.mAppName, object2.mAppName);
        }
    };
}
