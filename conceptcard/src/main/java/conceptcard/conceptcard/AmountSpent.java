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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AmountSpent extends Activity{
	EditText etcardnumber,etspend;
	Button breturn;
	ImageButton bgoback;
	ProgressDialog pd;
	MediaPlayer btnsound;
	TextView setCurrentPoint;
	private static final String TAG_RESPONSE_SUCCESS = "success";
	private static final String TAG_POINT_SUCCESS = "success";
	private static final String TAG_POINT_VALUE = "point";
	private static final int ABOUT = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lcap_amtspent);
		Button Return =(Button)findViewById(R.id.bReturn);
		
		etcardnumber = (EditText)findViewById(R.id.etCardNum);
		etspend = (EditText)findViewById(R.id.etSpend);
		etspend.setRawInputType(Configuration.KEYBOARD_12KEY);
		breturn = (Button)findViewById(R.id.bReturn);
		bgoback = (ImageButton)findViewById(R.id.bGoBack);
		setCurrentPoint = (TextView)findViewById(R.id.setRedeemCurrentPoint); 
		btnsound = MediaPlayer.create(AmountSpent.this, R.raw.buttonclick);
		ImageButton about = (ImageButton)findViewById(R.id.about);
		/*about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog alertabout = new AlertDialog.Builder(AmountSpent.this).create();
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
			}
		});*/
		
		//on cardnumber editfield change event
		etcardnumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { 
            	asyncTextchangedConnection textchanged = new asyncTextchangedConnection();
            	textchanged.execute();
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                Log.v("TAG", "beforeTextChanged");
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
		
		bgoback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnsound.start();
				Intent bgo = new Intent(getApplicationContext(), MainActivity.class);
				finish();
				startActivity(bgo);
			}
		});
		breturn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btnsound.start();
				if (etcardnumber.getText().toString().length() < 1 || etspend.getText().toString().length() < 1) {

					// out of range
					Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG)
							.show();
				} else {
					// pb.setVisibility(View.VISIBLE);
					asyncConnection addC = new asyncConnection();
					addC.execute();
				}
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
	}
	@Override
	public void onBackPressed() {
	//what it should do on back
		super.onBackPressed();
		startActivity(new Intent(AmountSpent.this, MainActivity.class));
		finish();
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
			AlertDialog alertabout = new AlertDialog.Builder(AmountSpent.this).create();
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

	public void logout() {
		SharedPreferences p = getSharedPreferences(INSConst.PREF_EDROID, Context.MODE_PRIVATE);
		Editor e = p.edit();
		e.putBoolean(INSConst.KEY_USER_PRESENT, false);
		e.commit();
		
	}
	
	private class asyncConnection extends AsyncTask<Void, Void, JSONObject> {
		
		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(AmountSpent.this);
			pd.setTitle("Processing....");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject res = null;
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, INSConst.cookieStore);
			HttpPost httppost = new HttpPost("http://customerloyaltyplus.com/tblloyaltycardhistories/json_redeem_points/");
							
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("cardnumber",
						etcardnumber.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("pointRedeem",
						etspend.getText().toString()));
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
			// TODO Auto-generated method stub
			pd.dismiss();
			try {
				if(json.has("exception")) {
					if(json.getBoolean("exception"))
						Toast.makeText(getApplicationContext(), "Oops!! Something went wrong.", Toast.LENGTH_LONG).show();
				} else {
				// Getting JSON Array
					boolean c = json.getBoolean(TAG_RESPONSE_SUCCESS);
					if(c){
						Toast.makeText(getApplicationContext(), "Point is redeem from loyaltycard", Toast.LENGTH_LONG).show();
						Intent mainpage = new Intent(getApplicationContext(),MainActivity.class);
						startActivity(mainpage);
					}else{
						String msg = json.getString("error_msg");
							Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
					}
					if(!json.has("user")) {
						logout();
						Intent lgintent = new Intent(getApplicationContext(),Login.class);
						startActivity(lgintent);
						finish();
					}
				}
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	
	private class asyncTextchangedConnection extends AsyncTask<Void, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject res= null;
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, INSConst.cookieStore);
			HttpPost httppost = new HttpPost("http://customerloyaltyplus.com/tblloyaltycardholders/json_remainingPoint/");
							
			try {
				// Add your data
	
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("cardnumber",
						etcardnumber.getText().toString()));
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
			// TODO Auto-generated method stub
			try {
				if(json.has("exception")) {
					if(json.getBoolean("exception"))
						Toast.makeText(getApplicationContext(), "oops!! something went wrong.", Toast.LENGTH_LONG).show();
				} else {
					// Getting JSON Array
					boolean c = json.getBoolean(TAG_POINT_SUCCESS);
					if(c){
						String point = json.getString(TAG_POINT_VALUE);
						setCurrentPoint.setText(point);
					}else{
						String point = "0";
						setCurrentPoint.setText(point);
						String msg = json.getString("error_msg");
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					}
					if(!json.has("user")) {
						logout();
						Intent lgintent = new Intent(getApplicationContext(),Login.class);
						startActivity(lgintent);
						finish();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
	}
}
