package conceptcard.conceptcard;



import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
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










import conceptcard.asynctask.library.checkconnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Login extends Activity {
	Button login;
	EditText password,username;
	MediaPlayer btnsound;
	private static final String TAG_LOGIN_SUCCESS = "success";
	private static final String TAG_ORG_SUCCESS = "pointPound";
	private static final int EXIT = 0;
	RelativeLayout display,progresslayout;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		login = (Button)findViewById(R.id.login);
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		display = (RelativeLayout) findViewById(R.id.logindp);
		btnsound = MediaPlayer.create(Login.this, R.raw.buttonclick);
		ImageButton about = (ImageButton)findViewById(R.id.about);
		about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog alertabout = new AlertDialog.Builder(Login.this).create();
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
		});
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnsound.start();
				checkconnection ck = new checkconnection();
				if(ck.checkInternetConnection(getApplicationContext())){
					asyncConnection addC = new asyncConnection();
					addC.execute();
				}else{
					Toast.makeText(getApplicationContext(), "No Internet connection available", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	@Override
	public void onBackPressed() {
	//what it should do on back
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, EXIT, 0, "exit");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case EXIT:
			finish();
			return true;
		}
		return false;
	}

	private class asyncConnection extends AsyncTask<Void, Void, JSONObject> {
		private boolean cancel;
		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(Login.this);
			pd.setTitle("Processing....");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject res = null;
			int statusCode = 0;

			HttpClient httpclient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, INSConst.cookieStore);
			HttpPost httppost = new HttpPost("http://customerloyaltyplus.com/tblorganisationusers/login/");

			try {
				// Add your data

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("requestFrom", "mobile"));
				nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost,localContext);
				
				//statusCode = response.getStatusLine().getStatusCode();

					String responseStr = EntityUtils.toString(response.getEntity());
					Log.d("resposnse", responseStr);
					try {
						res = new JSONObject(responseStr);
						
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
			} catch (ClientProtocolException e) {
				Log.i("st", "cl");
				e.printStackTrace();
				try {
					res = new JSONObject("{exception: true}");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
			} catch (IOException e) {
				Log.i("st", "io");
				e.printStackTrace();
		        //this.cancel(true);
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
						Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
				} else {
					boolean c = json.getBoolean(TAG_LOGIN_SUCCESS);
					Log.d("Start","start");
					if(c){
						String org = json.getString(TAG_ORG_SUCCESS);
						saveuser(username.getText().toString(),org);
						Intent inte = new Intent(getApplicationContext(), MainActivity.class);
						finish();
						startActivity(inte);
					}
					else{
						Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
public boolean saveuser(String user,String org) {

	SharedPreferences shared = getSharedPreferences(INSConst.PREF_EDROID,
			Context.MODE_PRIVATE); // sharedprefrence
									// is
									// a
									// memory
									// in
									// client
									// where
									// constats
									// are
									// stored
	Editor editor = shared.edit(); // Editor proviedes the handler to use
									// shared prefrence
	editor.putString(INSConst.KEY_NAME, user); // inserting values to
												// shared prefrence
	editor.putString("point", org);
	editor.putBoolean(INSConst.KEY_USER_PRESENT, true);
	editor.commit();
	Log.d("final", shared.getString("point", null));
	Log.d(Login.class.getName(), "Saving user prefrences");
	return true;
}
public void showAlertDialog(Context context, String title, String message) {
    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

    // Setting Dialog Title
    alertDialog.setTitle(title);

    // Setting Dialog Message
    alertDialog.setMessage(message);
     
    // Setting OK Button
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        }
    });

    // Showing Alert Message
    alertDialog.show();
}

}
