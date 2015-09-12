package sleeplessworks.uchikichan;

import android.app.DialogFragment;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
    View layout,click_to_dismiss;
    Button follow_up_btn,call_consult_btn;
    DiscreteSeekBar seekBar;
    TreatmentStatusDBHelper treatmentStatusDBHelper;
    String url = "https://www-jitendra12.practodev.com/appointments";
    RequestQueue requestQueue ;
    String TAG = "EditTreatmentStatusFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());
    }
    public EditTreatmentStatusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.edit_treatment_fragment, container, false);
        follow_up_btn = (Button)layout.findViewById(R.id.follow_up_btn);
        call_consult_btn = (Button)layout.findViewById(R.id.call_consult_btn);
        click_to_dismiss = (LinearLayout)layout.findViewById(R.id.click_to_dismiss);
        seekBar = (DiscreteSeekBar)layout.findViewById(R.id.range_bar);
        seekBar.setMax(100);
        seekBar.setOnProgressChangeListener(this);
        follow_up_btn.setOnClickListener(this);
        call_consult_btn.setOnClickListener(this);
        click_to_dismiss.setOnClickListener(this);
        treatmentStatusDBHelper = new TreatmentStatusDBHelper(getActivity());

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
        getActivity().finish();
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar discreteSeekBar, final int progress, boolean b) {

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

        Log.w(TAG,"Print all the rows in db");
        Cursor cursor = treatmentStatusDBHelper.getAll();
        while (cursor.moveToNext()) {
            Log.w(TAG, cursor.getString(cursor.getColumnIndex(TreatmentStatusDBHelper.KEY_STATUS_VALUE)));
            Log.w(TAG, cursor.getString(cursor.getColumnIndex(TreatmentStatusDBHelper.KEY_STATUS_TIME)));
        }

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
