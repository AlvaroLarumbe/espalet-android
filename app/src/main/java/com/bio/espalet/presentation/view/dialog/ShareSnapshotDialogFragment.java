package com.bio.espalet.presentation.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bio.espalet.R;
import com.bio.espalet.model.SnapshotConstants;
import com.bio.espalet.presentation.view.activity.MainActivity;

public class ShareSnapshotDialogFragment extends DialogFragment {

    private Button franceButton;
    private Button spainButton;
    private ShareableSnapshot callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_share);

        this.configureViews(dialog);
        this.configureListeners();

        return dialog;
    }

    public interface ShareableSnapshot {
        void onShareSnapshotSelected(short option);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.callback = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ShareableSnapshot");
        }
    }

    private void configureViews(Dialog dialog) {
        this.franceButton = (Button) dialog.findViewById(R.id.share_france_button);
        this.spainButton = (Button) dialog.findViewById(R.id.share_spain_button);

        if(!getArguments().getBoolean("showFrance")) {
            this.franceButton.setVisibility(View.GONE);
        }

        if(!getArguments().getBoolean("showSpain")) {
            this.spainButton.setVisibility(View.GONE);
        }
    }

    private void configureListeners() {
        this.franceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                callback.onShareSnapshotSelected(SnapshotConstants.FRANCE);
            }
        });

        this.spainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                callback.onShareSnapshotSelected(SnapshotConstants.SPAIN);
            }
        });
    }

}
