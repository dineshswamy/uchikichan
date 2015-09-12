package sleeplessworks.uchikichan;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by practo on 11/09/15.
 */
public class EditTreatmentStatusFragment extends Fragment implements  View.OnClickListener, DiscreteSeekBar.OnProgressChangeListener, Response.ErrorListener, Response.Listener<String> {
    View layout,click_to_dismiss,buttons_layout;
    Button follow_up_btn,call_consult_btn;
    ImageView smiley_1,smiley_2,smiley_3,smiley_4,smiley_5;
    DiscreteSeekBar seekBar;
    TreatmentStatusDBHelper treatmentStatusDBHelper;
    String url = "https://www-jitendra12.practodev.com/appointments";
    RequestQueue requestQueue ;
    String TAG = "EditTreatmentStatusFragment";
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
    public EditTreatmentStatusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.edit_treatment_fragment, container, false);
        follow_up_btn = (Button)layout.findViewById(R.id.follow_up_btn);
        call_consult_btn = (Button)layout.findViewById(R.id.call_consult_btn);
        buttons_layout = (LinearLayout)layout.findViewById(R.id.buttons_layout);
        click_to_dismiss = (LinearLayout)layout.findViewById(R.id.click_to_dismiss);
        seekBar = (DiscreteSeekBar)layout.findViewById(R.id.range_bar);
        smiley_1 = (ImageView)layout.findViewById(R.id.smiley_1);
        smiley_2 = (ImageView)layout.findViewById(R.id.smiley_2);
        smiley_3 = (ImageView)layout.findViewById(R.id.smiley_3);
        smiley_4 = (ImageView)layout.findViewById(R.id.smiley_4);
        smiley_5 = (ImageView)layout.findViewById(R.id.smiley_5);
        seekBar.setMax(100);
        seekBar.setOnProgressChangeListener(this);
        follow_up_btn.setOnClickListener(this);
        call_consult_btn.setOnClickListener(this);
        click_to_dismiss.setOnClickListener(this);
        treatmentStatusDBHelper = new TreatmentStatusDBHelper(getActivity());
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int pastProgess = preferences.getInt("last_status_value",0);
        seekBar.setProgress(pastProgess);
        setProgressForSmiley(pastProgess);
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_up_btn:
                break;
            case R.id.call_consult_btn:
                break;
            case R.id.click_to_dismiss:
                closeFragmentAndActivity();
                break;
        }
    }



    public void closeFragmentAndActivity() {
        //send Broadcast to update
//        Intent intent = new Intent();
//        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
//        getActivity().sendBroadcast(intent);
        updateWidget();
        getActivity().finish();
    }

    public void updateWidget() {
        ComponentName thisWidget = new ComponentName(getActivity().getApplicationContext(), FabricWidgetProvider.class);

        RemoteViews views = new RemoteViews(getActivity().getPackageName(), R.layout.widget_layout);
        //remoteView.setInt(R.id.widget_main, "setBackgroundResource", R.drawable.widget_background_dark_4x2);
        //Reset the views
        views.setInt(R.id.smiley_1, "setBackgroundResource", 0);
        views.setInt(R.id.smiley_2, "setBackgroundResource", 0);
        views.setInt(R.id.smiley_3, "setBackgroundResource", 0);
        views.setInt(R.id.smiley_4, "setBackgroundResource", 0);
        views.setInt(R.id.smiley_5, "setBackgroundResource", 0);
        int chosen_smiley_icon = getSmileyIconResforProgress(preferences.getInt("last_status_value", 0));
        Intent intent = new Intent(getActivity().getApplicationContext(), EditTreatmentStatusActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
        views.setInt(chosen_smiley_icon, "setBackgroundResource", R.drawable.circular_layout);
        views.setOnClickPendingIntent(R.id.parent_layout, pendingIntent);
        AppWidgetManager.getInstance(getActivity()).updateAppWidget(thisWidget, views);
    }
    public void setProgressForSmiley(int progress) {
        resetSmileyBackground();
        ImageView smileForTheProgress = getSmileyIconforProgress(progress,false);
        smileForTheProgress.setBackgroundResource(R.drawable.circular_layout);
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar discreteSeekBar, final int progress, boolean b) {
        setProgressForSmiley(progress);
    }

    public int getSmileyIconResforProgress(final int progress) {
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


    public ImageView getSmileyIconforProgress(final int progress,boolean imageView) {
        if (progress>0 && progress<20) {
            return smiley_1;
        } else if (progress>20 && progress<40) {
            return smiley_2;
        } else if (progress>40 && progress<60) {
            return smiley_3;
        } else if (progress>60 && progress<80) {
            return smiley_4;
        } else if(progress>80) {
            return smiley_5;
        }
        return smiley_1;
    }

    public void resetSmileyBackground() {
        smiley_1.setBackgroundResource(0);
        smiley_2.setBackgroundResource(0);
        smiley_3.setBackgroundResource(0);
        smiley_4.setBackgroundResource(0);
        smiley_5.setBackgroundResource(0);
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {

    }


    @Override
    public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {

        //Insert into the db
        final String appointmentId = "42349";
        final String doctor_id = "124";
        final String patient_id = "225";
        Date statusTime = new Date();
        final int progress = discreteSeekBar.getProgress();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        final String statusTimeStr = dateFormat.format(statusTime);
        treatmentStatusDBHelper.insertIntoTreatmentStatus(appointmentId, progress, statusTimeStr);

        //send data to server
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,this,this) {

            @Override
            public Map<String, String> getParams() {
                HashMap mParams = new HashMap<String, String>();

                mParams.put("doctor_id",doctor_id);
                mParams.put("patient_id",patient_id);
                mParams.put("appointment_id",appointmentId);
                mParams.put("time",statusTimeStr);
                mParams.put("value",Integer.toString(progress));
                mParams.put("name","Ramesh");

                return mParams;
            }
        };

        requestQueue.add(stringRequest);
        putStatusValuesInPrefereces(progress);
        Log.w(TAG, "Print all the rows in db");
        Cursor cursor = treatmentStatusDBHelper.getAll();
        while (cursor.moveToNext()) {
            Log.w(TAG, cursor.getString(cursor.getColumnIndex(TreatmentStatusDBHelper.KEY_STATUS_VALUE)));
            Log.w(TAG, cursor.getString(cursor.getColumnIndex(TreatmentStatusDBHelper.KEY_STATUS_TIME)));
        }

    }

    public void putStatusValuesInPrefereces(int currentProgress) {

        SharedPreferences _prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int lastProgress = _prefs.getInt("last_status_value", 0);
        if (lastProgress > currentProgress) {
            //Show the buttons
            buttons_layout.setVisibility(View.VISIBLE);
        } else {
            buttons_layout.setVisibility(View.INVISIBLE);
        }

        SharedPreferences.Editor editor = _prefs.edit();
        editor.putInt("last_status_value",currentProgress);
        editor.commit();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.w(TAG,"Error occured while updating the value");
    }

    @Override
    public void onResponse(String response) {
        Log.w(TAG,"Successfully sent " +response);
    }
}
