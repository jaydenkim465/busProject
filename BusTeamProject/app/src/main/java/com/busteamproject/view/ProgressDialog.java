package com.busteamproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.busteamproject.R;

public class ProgressDialog extends Dialog {

	public ProgressDialog(@NonNull Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_progress);
		setCancelable(false);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
}
