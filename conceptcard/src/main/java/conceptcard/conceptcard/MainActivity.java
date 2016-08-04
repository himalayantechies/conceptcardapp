package conceptcard.conceptcard;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	private static final int LOGOUT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity1);
		Button IGC = (Button) findViewById(R.id.bIssueGiftCard);
		Button SNLC = (Button) findViewById(R.id.bStartNewLoyaltyCard);
		Button APTLC = (Button) findViewById(R.id.bAddPointsToLoyaltyCard);
		Button redeem_giftcard = (Button)findViewById(R.id.bRedeemGiftCard);
		Button redeem_loyaltycard = (Button)findViewById(R.id.bRedeemloyaltyCard);
		Button demo = (Button)findViewById(R.id.demo);
		ImageButton about = (ImageButton)findViewById(R.id.about);
		about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog alertabout = new AlertDialog.Builder(MainActivity.this).create();
				alertabout.setTitle("About Us");
				alertabout.setMessage("Loyaltycard and Giftcard terminal by conceptcard.oopsnepal.com");
				alertabout.setCancelable(true);
				alertabout.setButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				alertabout.show();
			}
		});
		ImageButton logout = (ImageButton)findViewById(R.id.logout);
		
		
logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logout();
				Intent lgintent = new Intent(getApplicationContext(),Login.class);
				startActivity(lgintent);
				finish();
			}
		});
//		demo.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(MainActivity.this, MainActivity1.class));
//			}
//		});
		
		IGC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent igc = new Intent(MainActivity.this, IssueGiftCard.class);
				finish();
				startActivity(igc);
			}
		});
		SNLC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent snlc = new Intent(MainActivity.this,StartNewLoyaltyCard.class);
				finish();
				startActivity(snlc);
			}
		});
		APTLC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aptlc = new Intent(MainActivity.this, LCAddPoints.class);
				finish();
				startActivity(aptlc);
			}
		});
		redeem_giftcard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent redeem_gift = new Intent(MainActivity.this, Redeemgift.class);
				finish();
				startActivity(redeem_gift);
			}
		});
		redeem_loyaltycard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent redeem_loyal = new Intent(MainActivity.this, AmountSpent.class);
				finish();
				startActivity(redeem_loyal);
			}
		});
	}
	@Override
	public void onBackPressed() {
	//what it should do on back
		super.onBackPressed();
		finish();
	}

	public void logout() {
		SharedPreferences p = getSharedPreferences(INSConst.PREF_EDROID, Context.MODE_PRIVATE);
		Editor e = p.edit();
		e.putBoolean(INSConst.KEY_USER_PRESENT, false);
		e.commit();
		
	}
}
