package com.paypal.ipn;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.c2dm.C2DMessaging;

public class RegisterActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		((EditText) findViewById(R.id.textEmailAddress))
				.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent e) {
						if (e.getAction() == KeyEvent.ACTION_DOWN
								&& keyCode == KeyEvent.KEYCODE_ENTER) {
							register(v);
						}
						return false;
					}
				});
	}

	public void register(View view) {
		Toast.makeText(this, "Aguarde", Toast.LENGTH_LONG).show();
		
		((EditText) findViewById(R.id.textEmailAddress)).setEnabled(false);
		((Button) findViewById(R.id.registerButton)).setEnabled(false);

		String email = ((EditText) findViewById(R.id.textEmailAddress))
				.getText().toString();

		SharedPreferences prefs = getSharedPreferences(getResources()
				.getString(R.string.app_id), Context.MODE_PRIVATE);

		Editor editor = prefs.edit();
		editor.putString("receiverEmail", email);
		editor.commit();

		C2DMessaging.register(this, email);
	}
}
