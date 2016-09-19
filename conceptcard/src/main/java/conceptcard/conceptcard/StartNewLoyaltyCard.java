package conceptcard.conceptcard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class StartNewLoyaltyCard extends Activity implements OnClickListener {
	private EditText cardNumber;
	String responseStr;
	ProgressDialog pd;
	TextView cardexistmsg,tvEmail,tvName;
	Button giftredeembtn,bSave;
	EditText etName,etEmail,etLoyaltyCardnumber,etInitialPoint;
	ScrollView addnewloyalty,alreadyexist;
	MediaPlayer btnsound;
	private static final String TAG_RESPONSE_SUCCESS = "success";
	private static final int ABOUT = 0;
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_new_loyalty_card);
		addnewloyalty = (ScrollView)findViewById(R.id.addnewloyalty);
		alreadyexist = (ScrollView)findViewById(R.id.alreadyexist);
		Button Return = (Button) findViewById(R.id.bReturn);
		cardNumber = (EditText) findViewById(R.id.etCardNumber);
		cardexistmsg = (TextView)findViewById(R.id.bcardexistmsg);
		giftredeembtn = (Button)findViewById(R.id.bgiftredeem);
		tvEmail =(TextView)findViewById(R.id.tvEmail);
		tvName =(TextView)findViewById(R.id.tvName);
		bSave = (Button)findViewById(R.id.bSave);
		etName = (EditText)findViewById(R.id.etName);
		etEmail = (EditText)findViewById(R.id.etEmail);
		etLoyaltyCardnumber = (EditText)findViewById(R.id.etLoyaltyCardnumber);
		etInitialPoint = (EditText)findViewById(R.id.etInitialPoint);
		etInitialPoint.setRawInputType(Configuration.KEYBOARD_12KEY);
		btnsound = MediaPlayer.create(StartNewLoyaltyCard.this, R.raw.buttonclick);
		ImageButton about =(ImageButton)findViewById(R.id.about);
		ImageButton GoBack = (ImageButton) findViewById(R.id.bGoBack);
		GoBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnsound.start();
				Intent goback = new Intent(StartNewLoyaltyCard.this,MainActivity.class);
				finish();
				startActivity(goback);
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
		Return.setOnClickListener(this);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_display_notification,
		// menu);

		menu.add(0, ABOUT, 0, "About");
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case ABOUT:
			AlertDialog alertabout = new AlertDialog.Builder(StartNewLoyaltyCard.this).create();
			alertabout.setTitle("About Us");
			alertabout.setMessage("Loyalty Card and Gift Card terminal by Concept Card");
			alertabout.setCancelable(true);
			alertabout.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			alertabout.show();
			return true;
		}
		return false;
	}
	@Override
	public void onBackPressed() {
	//what it should do on back
		super.onBackPressed();
		startActivity(new Intent(StartNewLoyaltyCard.this, MainActivity.class));
		finish();
	}

	public void logout() {
		SharedPreferences p = getSharedPreferences(INSConst.PREF_EDROID, Context.MODE_PRIVATE);
		Editor e = p.edit();
		e.putBoolean(INSConst.KEY_USER_PRESENT, false);
		e.commit();
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		btnsound.start();
		if (cardNumber.getText().toString().length() < 1) {
			// out of range
			Toast.makeText(this, "Please Enter Card Number", Toast.LENGTH_LONG)
					.show();
		} else {
			// pb.setVisibility(View.VISIBLE);
			new MyAsyncTask().execute(cardNumber.getText().toString());
		}

	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {
		
		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(StartNewLoyaltyCard.this);
			pd.setTitle("Processing....");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String res = null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://customerloyaltyplus.com/tblloyaltycardholders/json_encode/" + cardNumber.getText().toString());
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("myHttpData", cardNumber.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				String responseStr = EntityUtils.toString(response.getEntity());
				Log.d("response",responseStr);
				res = responseStr.toString();
				Log.d("background",res);
				
			} catch (ClientProtocolException e) {
				Log.i("st", "cl");
				// TODO Auto-generated catch block
			} catch (IOException e) {
				Log.i("st", "io");
				// TODO Auto-generated catch block

			}
			return res;
			// return null;
		}

		protected void onPostExecute(String result) {
			pd.dismiss();
			String finalres = result.toString();
			if(finalres.equals("not")){
				alreadyexist.setVisibility(View.GONE);
				addnewloyalty.setVisibility(View.VISIBLE);
				etLoyaltyCardnumber.setText(cardNumber.getText().toString());
				bSave.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						btnsound.start();
						if (etEmail.getText().toString().length() < 1 || etName.getText().toString().length()<1 || etLoyaltyCardnumber.getText().toString().length()<1 || etInitialPoint.getText().toString().length()<1) {
							// out of range
							Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG)
									.show();
						} else {
							asyncConnection addC = new asyncConnection();
							addC.execute();
						}
					}
				});
			}
			else{
				alreadyexist.setVisibility(View.VISIBLE);
				addnewloyalty.setVisibility(View.GONE);
				giftredeembtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						btnsound.start();
						Intent redeem_pnt = new Intent(getApplicationContext(),AmountSpent.class);
						startActivity(redeem_pnt);
					}
				});
			}

		}

		
	}
	
private class asyncConnection extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(StartNewLoyaltyCard.this);
			pd.setTitle("Processing....");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject res= null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, INSConst.cookieStore);
			HttpPost httppost = new HttpPost("http://customerloyaltyplus.com/tblloyaltycardholders/json_addnewloyalty/");

			try {
				// Add your data

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("FirstName", etName.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("Email1", etEmail.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("CardNumber", etLoyaltyCardnumber.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("Points", etInitialPoint.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost, localContext);
				String responseStr = EntityUtils.toString(response.getEntity());
				Log.d("response",responseStr);
				res = new JSONObject(responseStr);
			} catch (ClientProtocolException e) {
				Log.i("st", "cl");
				try {
					res = new JSONObject("{exception: true}");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
			} catch (IOException e) {
				Log.i("st", "io");
				try {
					res = new JSONObject("{exception: true}");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					res = new JSONObject("{exception: true}");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return res;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			pd.dismiss();
			// TODO Auto-generated method stub
			try {
				if(json.has("exception")) {
					if(json.getBoolean("exception"))
						Toast.makeText(getApplicationContext(), "Oops!! Something went wrong.", Toast.LENGTH_LONG).show();
				} else {
				// Getting JSON Array
					boolean c = json.getBoolean(TAG_RESPONSE_SUCCESS);
					if(c){
						Toast.makeText(getApplicationContext(), "New loyalty card saved .", Toast.LENGTH_LONG).show();
						Intent mainpage = new Intent(getApplicationContext(),MainActivity.class);
						startActivity(mainpage);
					}else{
						String msg = json.getString("error_msg");
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					}
				}
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
