package conceptcard.conceptcard;

/*import android.app.Activity;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;

 public class IssueGiftCard extends Activity {
 Button GoBack, Return;
 @Override
 protected void onCreate(Bundle savedInstanceState) {
 // TODO Auto-generated method stub
 super.onCreate(savedInstanceState);
 setContentView(R.layout.issue_gift_card);
 Return = (Button) findViewById(R.id.bReturn);

 Return.setOnClickListener(new OnClickListener() {

 @Override
 public void onClick(View v) {
 final TextView cardnum = (TextView) findViewById(R.id.etcardNumber);
 String cardNumber = cardnum.getText().toString();
 // TODO Auto-generated method stub
 // startActivity(new
 // Intent(IssueGiftCard.this,AfterReturn.class));
 Toast.makeText(IssueGiftCard.this,cardNumber, Toast.LENGTH_LONG).show();
 startActivity(new Intent(IssueGiftCard.this, AfterReturn.class).putExtra("cnum", (CharSequence)cardNumber));

 }
 });
 GoBack=(Button)findViewById(R.id.bGoBack);
 GoBack.setOnClickListener(new OnClickListener() {

 @Override
 public void onClick(View v) {
 // TODO Auto-generated method stub
 startActivity(new Intent(IssueGiftCard.this, MainActivity.class));
 }
 });

 }

 }*/

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class IssueGiftCard extends Activity implements OnClickListener {
	TextView display,cardexistmsg;
	ScrollView addgiftcard,giftcardexist;
	ProgressDialog pd;
	private EditText value,etAddGiftCardnumber,etAddGiftCardvalue;
	private String valueStr, cardNumStr, cardValueStr;
	private Button btn,giftredeembtn,giftaddbtn;
	String responseStr;
	MediaPlayer btnsound;
	private static final int ABOUT = 0;
	private static final String TAG_RESPONSE_SUCCESS = "success";
	final Context context = this;

	// private ProgressBar pb;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.issue_gift_card);
		value = (EditText) findViewById(R.id.etCardNumber);
		etAddGiftCardnumber = (EditText) findViewById(R.id.etAddGiftCardnumber);
		
		etAddGiftCardvalue = (EditText) findViewById(R.id.etAddGiftCardvalue);
		etAddGiftCardvalue.setRawInputType(Configuration.KEYBOARD_12KEY);
		
		btn = (Button) findViewById(R.id.bReturn);
		giftaddbtn = (Button)findViewById(R.id.giftaddbtn);
		giftredeembtn = (Button)findViewById(R.id.giftredeem);
		ImageButton GoBack=(ImageButton)findViewById(R.id.bGoBack);
		
		display=(TextView)findViewById(R.id.tvCardNumber);
		cardexistmsg = (TextView)findViewById(R.id.cardexistmsg);
		
		addgiftcard = (ScrollView)findViewById(R.id.addgift);
		giftcardexist = (ScrollView)findViewById(R.id.giftcardexist);
		ImageButton about =(ImageButton)findViewById(R.id.about);
		/*about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog alertabout = new AlertDialog.Builder(IssueGiftCard.this).create();
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
		});*/
		// pb=(ProgressBar)findViewById(R.id.progressBar1);
		// pb.setVisibility(View.GONE);
		btnsound = MediaPlayer.create(IssueGiftCard.this, R.raw.buttonclick);
		 GoBack.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
		 // TODO Auto-generated method stub
			 btnsound.start();
			 Intent bgob = new Intent(IssueGiftCard.this, MainActivity.class);
			 finish();
			 startActivity(bgob);
		 }
		 });
		 ImageButton logout = (ImageButton)findViewById(R.id.logout);
			
			
			logout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					logout();
					Intent lgintent = new Intent(IssueGiftCard.this,Login.class);
					startActivity(lgintent);
					finish();
				}
			});
			
			
		btn.setOnClickListener(this);
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
			AlertDialog alertabout = new AlertDialog.Builder(IssueGiftCard.this).create();
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
			return true;
		}
		return false;
	}
	@Override
	public void onBackPressed() {
	//what it should do on back
		super.onBackPressed();
		startActivity(new Intent(IssueGiftCard.this, MainActivity.class));
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
		if (value.getText().toString().length() < 1) {

			// out of range
			Toast.makeText(this, "Please Enter Card Number", Toast.LENGTH_LONG)
					.show();
		} else {
			valueStr = value.getText().toString();
			cardNumStr = etAddGiftCardnumber.getText().toString();
			cardValueStr = etAddGiftCardvalue.getText().toString();
            Log.d("value", valueStr);
            Log.d("cardnum", cardNumStr);
            Log.d("cardvalue", cardValueStr);
			// pb.setVisibility(View.VISIBLE);
			new MyAsyncTask().execute(value.getText().toString());
		}

	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(IssueGiftCard.this);
			pd.setTitle("Processing....");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		protected String doInBackground(String... params) {
			String res = null;
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			   localContext.setAttribute(ClientContext.COOKIE_STORE, INSConst.cookieStore);
			HttpPost httppost = new HttpPost(
					"http://conceptcard.oopsnepal.com/tblgiftcards/json_encode/"
							+ valueStr);
			/*HttpPost httppost = new HttpPost(
					"http://10.0.2.2/conceptcard/tblgiftcards/json_encode/"
							+ value.getText().toString());*/
			
			Log.i("value", valueStr);
			try {
				// Add your data

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("myHttpData",
						valueStr));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost, localContext);
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
			Log.d("post",finalres);
			if(finalres.equals("not")){
				addgiftcard.setVisibility(View.VISIBLE);
				etAddGiftCardnumber.setText(value.getText().toString());
				giftcardexist.setVisibility(View.GONE);
				giftaddbtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						btnsound.start();
						if (etAddGiftCardnumber.getText().toString().length() < 1 || etAddGiftCardvalue.getText().toString().length()<1) {

							// out of range
							Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG)
									.show();
						} else {
                            valueStr = value.getText().toString();
                            cardNumStr = etAddGiftCardnumber.getText().toString();
                            cardValueStr = etAddGiftCardvalue.getText().toString();
							asyncConnection addC = new asyncConnection();
							addC.execute();
						}
					}
				});
			}
			else{
				addgiftcard.setVisibility(View.GONE);
				giftcardexist.setVisibility(View.VISIBLE);
				giftredeembtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						btnsound.start();
						
						Intent redeem_pnt = new Intent(getApplicationContext(),Redeemgift.class);
						startActivity(redeem_pnt);
					}
				});
			}

		}

	}
	
private class asyncConnection extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(IssueGiftCard.this);
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
			HttpPost httppost = new HttpPost(
				"http://conceptcard.oopsnepal.com/tblgiftcards/json_giftAdd/");
			/*HttpPost httppost = new HttpPost(
					"http://10.0.2.2/conceptcard/tblgiftcards/json_giftAdd/");*/
						
			try {
				// Add your data

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("CardNumber",
						cardNumStr));
				nameValuePairs.add(new BasicNameValuePair("OriginalValue",
						cardValueStr));
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
				// Getting JSON Array
				if(json.has("exception")) {
					if(json.getBoolean("exception"))
						Toast.makeText(getApplicationContext(), "Oops!! Something went wrong.", Toast.LENGTH_LONG).show();
				} else {
					boolean c = json.getBoolean(TAG_RESPONSE_SUCCESS);
					if(c){
						Intent mainpage = new Intent(getApplicationContext(),MainActivity.class);
						startActivity(mainpage);
					}else{
						Toast.makeText(getApplicationContext(), "Error occured!Please try again", Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}