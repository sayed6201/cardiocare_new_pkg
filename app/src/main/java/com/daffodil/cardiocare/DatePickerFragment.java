package com.daffodil.cardiocare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    public static interface OnCompleteListener {
        public abstract void onComplete(String time);
    }

    public static OnCompleteListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /**Init Calendar**/
        final Calendar c = Calendar.getInstance();

        /**Get Year**/
        int year = c.get(Calendar.YEAR);

        /**Get Month**/
        int month = c.get(Calendar.MONTH);

        /**Get Date of Month**/
        int day = c.get(Calendar.DAY_OF_MONTH);

        /**Return a DatePickerDialog**/
        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    /**Date Set Listener**/
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    /**Show Toast when Select any Date**/
                    Toast.makeText(getActivity(), "Selected date is " + view.getYear() + " / " + (view.getMonth() + 1) + " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();

                    String m = (view.getMonth() + 1)+"";
                    String d = view.getDayOfMonth()+"";

                    if(m.length()<2){
                        m = "0"+m;
                    }
                    if(d.length()<2){
                        d = "0"+d;
                    }
                    String value =view.getYear() + "-" + m + "-" + d;

                    DatePickerFragment.mListener.onComplete(value);
                }
            };
}