package com.derek.ltapoc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteRateTemplateDialogFragment extends DialogFragment {
	private String mRateTemplateId;
	private DeleteRateTemplateDialogFragmentCallbacks mCallbacks;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// builder.setTitle("Delete");
		builder.setMessage("Are you sure to delete this rate template?");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				mCallbacks.onDidDeleteRateTemplate(mRateTemplateId);
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				DeleteRateTemplateDialogFragment.this.getDialog().cancel();
			}
		});

		return builder.create();
	}

	public interface DeleteRateTemplateDialogFragmentCallbacks {
		void onDidDeleteRateTemplate(String rateTemplateId);
	}

	public void setCallbacks(DeleteRateTemplateDialogFragmentCallbacks callbacks) {
		mCallbacks = callbacks;
	}

	public void setRateTemplateId(String rateTemplateId) {
		mRateTemplateId = rateTemplateId;
	}
}
