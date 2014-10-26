package com.example.ipc;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private boolean isBinding = false;

	private int remoteMsg;

	private Button bindBtn, sendBtn;
	private TextView statusTv, resultTv;

	private String preStr1 = "isBinding --> ";
	private String preStr2 = "remote message --> ";

	private ServiceConnection conn = new MyServiceConnection();
	private Messenger mService;
	private Messenger mMessenger = new Messenger(new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == RemoteService.GET_RESULT) {
				remoteMsg = msg.arg1;
				resultTv.setText(preStr2 + remoteMsg);

			} else {
				super.handleMessage(msg);
			}
		};
	});

	class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO 自动生成的方法存根
			Toast.makeText(MainActivity.this, "service have be connected!",
					Toast.LENGTH_LONG).show();
			mService = new Messenger(service);
			isBinding = true;
			statusTv.setText(preStr1 + isBinding);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO 自动生成的方法存根
			isBinding = false;
			conn = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		bindBtn = (Button) findViewById(R.id.button1);
		sendBtn = (Button) findViewById(R.id.button2);
		statusTv = (TextView) findViewById(R.id.textView1);
		resultTv = (TextView) findViewById(R.id.textView2);

		statusTv.setText(preStr1 + isBinding);
		resultTv.setText(preStr2 + "not");
		
		bindBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO 自动生成的方法存根
		if (view == bindBtn) {
			bindRemoteService();
		} else if (view == sendBtn) {
			if (mService == null) {
				Toast.makeText(this, "service not be connected!",
						Toast.LENGTH_LONG).show();
				return;
			}
			Message msg = Message.obtain(null, RemoteService.GET_RESULT);
			msg.replyTo = mMessenger;
			try {
				mService.send(msg);
			} catch (RemoteException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	private void bindRemoteService() {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(this, RemoteService.class);

		bindService(intent, conn, BIND_AUTO_CREATE);
	}

}
