package com.prestigecode.mobilebank.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

/* Thank you
https://stackoverflow.com/questions/22724301/yes-or-no-confirmation-in-android-preferences/22724342#22724342

*/

public class YesNo extends DialogPreference
{
    public YesNo(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onClick()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Reset application?");
        dialog.setMessage("This action will delete all your data. Are you sure you want to continue?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //reset database
                Toast.makeText(getContext(), "Application reset!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dlg, int which)
            {
                dlg.cancel();
            }
        });

        AlertDialog al = dialog.create();
        al.show();
    }
}
