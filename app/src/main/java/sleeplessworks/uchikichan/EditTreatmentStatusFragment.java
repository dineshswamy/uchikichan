package sleeplessworks.uchikichan;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by practo on 11/09/15.
 */
public class EditTreatmentStatusFragment extends Fragment implements  View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    View layout,click_to_dismiss;
    Button follow_up_btn,call_consult_btn;
    DiscreteSeekBar seekBar;
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
        follow_up_btn.setOnClickListener(this);
        call_consult_btn.setOnClickListener(this);
        click_to_dismiss.setOnClickListener(this);

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void closeFragmentAndActivity() {
        getActivity().finish();
    }
}
