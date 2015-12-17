package com.example.greg.gpssportraker.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.greg.gpssportraker.MapsActivity;
import com.example.greg.gpssportraker.R;
import com.example.greg.gpssportraker.history.HistoryListActivity;

/**
 * Created by Geerg on 2015.12.02..
 */
public class HistoryDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.historydialogtitle);
                builder.setMessage(R.string.historydialogmsg)
                        .setPositiveButton(R.string.historydialogOK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent showHistory = new Intent(getActivity(), HistoryListActivity.class);
                                startActivity(showHistory);
                            }
                        })
                        .setNegativeButton(R.string.historydialogCancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
