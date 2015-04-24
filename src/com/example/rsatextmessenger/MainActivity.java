package com.example.rsatextmessenger;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnSendSMS;
	IntentFilter intentFilter;
	
	private BroadcastReceiver intentReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent){
			TextView SMSes = (TextView) findViewById(R.id.textView1);
			SMSes.setText(intent.getExtras().getString("sms"));
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		intentFilter = new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");
		
		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		btnSendSMS.setOnClickListener(new View.OnClickListener()) 
		{
			public void onClick(View v){
				sendSMS();
			}
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		};
		setContentView(R.layout.activity_main);
	}
	
	private void sendSMS(String phoneNumber, String message){
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
		
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
		
		registerReceiver(new BroadcastReceiver(){
			public void onReceive(Context arg0, Intent arg1){
				switch(getResultCode()){
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic Failure", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No Service", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU", 
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off", 
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));
		
		registerReceiver(new BroadcastReceiver(){
			public void onReceive(Context arg0, Intent arg1){
				switch(getResultCode()){
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS delivered", 
								Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "SMS not delivered", 
								Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
