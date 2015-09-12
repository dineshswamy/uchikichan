package sleeplessworks.uchikichan;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by practo on 11/09/15.
 */
public class FabricWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("FabricWidgetProvider","Just now received an update");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), FabricWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        SharedPreferences _prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
                int appWidgetId = appWidgetIds[i];
                // Create an Intent to launch ExampleActivity
                Intent intent = new Intent(context, EditTreatmentStatusActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                // Get the layout for the App Widget and attach an on-click listener
                // to the button
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                //remoteView.setInt(R.id.widget_main, "setBackgroundResource", R.drawable.widget_background_dark_4x2);
                //Reset the views
                views.setInt(R.id.smiley_1, "setBackgroundResource", 0);
                views.setInt(R.id.smiley_2, "setBackgroundResource", 0);
                views.setInt(R.id.smiley_3, "setBackgroundResource", 0);
                views.setInt(R.id.smiley_4, "setBackgroundResource", 0);
                views.setInt(R.id.smiley_5, "setBackgroundResource", 0);
                int chosen_smiley_icon = getSmileyIconforProgress(_prefs.getInt("last_status_value", 0));
                views.setInt(chosen_smiley_icon, "setBackgroundResource", R.drawable.circular_layout);
                views.setOnClickPendingIntent(R.id.parent_layout, pendingIntent);
                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }



    public int getSmileyIconforProgress(final int progress) {
        if (progress>0 && progress<20) {
            return R.id.smiley_1;
        } else if (progress>20 && progress<40) {
            return R.id.smiley_2;
        } else if (progress>40 && progress<60) {
            return R.id.smiley_3;
        } else if (progress>60 && progress<80) {
            return R.id.smiley_4;
        } else if(progress>80) {
            return R.id.smiley_5;
        }
        return R.id.smiley_1;
    }




}
