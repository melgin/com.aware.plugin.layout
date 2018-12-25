package com.aware.plugin.layout;

import android.accessibilityservice.AccessibilityService;
import android.content.ContentValues;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Layout extends AccessibilityService {

    private static String TAG = "AWARE::Layout";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (Aware.getSetting(getApplicationContext(), Settings.STATUS_PLUGIN_LAYOUT).equals("true") && event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            AccessibilityNodeInfo src = event.getSource();
            AccessibilityNodeInfo parent = getRootParent(src);
            if (parent == null || src == null) {
                return;
            }


            if(parent != null) {
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(new LayoutData(parent));

                ContentValues rowData = new ContentValues();
                rowData.put(Provider.AWAREColumns.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
                rowData.put(Provider.AWAREColumns.TIMESTAMP, System.currentTimeMillis());
                rowData.put(Provider.Layouts_Data.DOM_TREE, json);
                rowData.put(Provider.Layouts_Data.PACKAGE, parent.getPackageName() == null ? null : parent.getPackageName().toString());

                if (awareSensor != null){
                    awareSensor.onLayoutChanged(rowData);
                }

                getContentResolver().insert(Provider.Layouts_Data.CONTENT_URI, rowData);

                Log.d(TAG, json);
            }
        }
    }

    private static AWARESensorObserver awareSensor;

    public static void setSensorObserver(AWARESensorObserver observer) {
        awareSensor = observer;
    }

    public static AWARESensorObserver getSensorObserver() {
        return awareSensor;
    }

    public interface AWARESensorObserver {
        void onLayoutChanged(ContentValues data);
    }

    private static AccessibilityNodeInfo getRootParent(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        if (current != null) {
            while (current.getParent() != null) {
                AccessibilityNodeInfo oldCurrent = current;
                current = current.getParent();
                oldCurrent.recycle();
            }
        }
        return current;
    }

    @Override
    public void onInterrupt() {

    }
}
