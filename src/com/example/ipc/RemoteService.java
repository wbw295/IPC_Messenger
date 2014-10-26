package com.example.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class RemoteService extends Service {

	public static final int GET_RESULT = 1;

	private int remoteInt = 1;

	private final Messenger mMesesenger = new Messenger(new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == GET_RESULT) {
				try {
					msg.replyTo.send(Message.obtain(null, GET_RESULT,
							remoteInt++, 0));
				} catch (RemoteException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			}

		}
	});

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return mMesesenger.getBinder();
	}

}
