package conceptcard.conceptcard;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class Splashscreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		Runnable rn = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SharedPreferences p = getSharedPreferences(INSConst.PREF_EDROID, Context.MODE_PRIVATE);

				if (p.getBoolean(INSConst.KEY_USER_PRESENT, false)) {
					finish();
					Intent isp = new Intent(getApplicationContext(), MainActivity.class);
					isp.putExtra("username", p.getString(INSConst.KEY_NAME, "--ANYNOMOUS--"));
					startActivity(isp);
				} else {
					finish();
					Intent isp = new Intent(getApplicationContext(), Login.class);
					startActivity(isp);
				}

			}
		};
		Handler hr = new Handler();
		hr.postDelayed(rn, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splashscreen, menu);
		return true;
	}

}
