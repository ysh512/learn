package com.example.telphone.receiver;

import com.example.telphone.TelApplication;
import com.example.telphone.activity.Calling;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

public class PhoneReceiver extends BroadcastReceiver {

	private static final String TAG = "PhoneReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("action" + intent.getAction());
		// �����ȥ��
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String phoneNumber = intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Log.d(TAG, "call OUT:" + phoneNumber);
		} else {
			// ������android�ĵ���ò��û��ר�����ڽ��������action,���ԣ���ȥ�缴����.
			// ���������Ҫ�����绰�Ĳ���״������Ҫ��ô���� :
			// * ��һ����ȡ�绰���������TelephonyManager manager =
			// this.getSystemService(TELEPHONY_SERVICE);
			// * �ڶ���ͨ��TelephonyManagerע������Ҫ�����ĵ绰״̬�ı��¼���manager.listen(new
			// MyPhoneStateListener(),
			// *
			// PhoneStateListener.LISTEN_CALL_STATE);�����PhoneStateListener.LISTEN_CALL_STATE����������Ҫ
			// * ������״̬�ı��¼�������֮�⣬���кܶ������¼�Ŷ��
			// * ��������ͨ��extends PhoneStateListener�������Լ��Ĺ��򡣽�����󴫵ݸ��ڶ�����Ϊ������
			// * ���Ĳ�����һ������Ҫ���Ǿ��Ǹ�Ӧ�����Ȩ�ޡ�android.permission.READ_PHONE_STATE

			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
			// ����һ��������
		}
	}

	PhoneStateListener listener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// ע�⣬��������д��super�������棬����incomingNumber�޷���ȡ��ֵ��
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

//				System.out.println("�Ҷ�");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
//				System.out.println("����");
				//stop background music

				break;
			case TelephonyManager.CALL_STATE_RINGING:
//				System.out.println("����:�������" + incomingNumber);
				

				Calling.stopPlayBgMusic();
				
//				if(null!=Calling.mediaPlayer && Calling.mediaPlayer.isPlaying())
//				{
//					Calling.mediaPlayer.stop();
//					Calling.mediaPlayer.release();
//					Calling.mediaPlayer = null;
//				}
				
				// ����������
				if(TelApplication.getCallingRunning())
				{
					SharedPreferences s= Calling.context.getSharedPreferences("test", Activity.MODE_PRIVATE);				
					try
					{
						if(s.getBoolean("auto", false) && TelApplication.getCallingRunning())
						{
							Thread.sleep(1500);
							answerRingingCallWithBroadcast(Calling.context,Calling.tm);
							
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					

					if(null!=Calling.context)
					{
						((Activity) Calling.context).finish();
						Calling.context = null;
					}
				}
				

				
				TelApplication.setCallingRunning(false);

				
				break;
			}
		}
	};
	
	


	private void answerRingingCallWithBroadcast(Context context,
			TelephonyManager telmanager) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// �ж��Ƿ�����˶���
		if (!audioManager.isWiredHeadsetOn()) {
			// 4.1����ϵͳ�����˲���Ȩ�ޣ� ʹ������4.1�汾������ʾ���棺Permission Denial: not allowed to
			// send broadcast android.intent.action.HEADSET_PLUG from pid=1324,
			// uid=10017
			// ������Ҫע��һ�㣬���͹㲥ʱ����Ȩ�ޡ�android.permission.CALL_PRIVLEGED��������ܸù㲥ʱҲ��Ҫ���Ӹ�Ȩ�ޡ�����4.1���ϰ汾ò�����Ȩ��ֻ��ϵͳӦ�òſ��Եõ������Ե�ʱ���Զ���Ľ������޷����ܵ��˹㲥������ȥ�������Ȩ�ޣ���ΪNULL����Լ������ˡ�

			if (android.os.Build.VERSION.SDK_INT >= 15) {
				Intent meidaButtonIntent = new Intent(
						Intent.ACTION_MEDIA_BUTTON);
				KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
						KeyEvent.KEYCODE_HEADSETHOOK);
				meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
				context.sendOrderedBroadcast(meidaButtonIntent, null);
			} else {
				// ����������Android2.3��2.3���ϵİ汾�� �������Է���4.1ϵͳ�ϲ����á�
				Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
				localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				localIntent1.putExtra("state", 1);
				localIntent1.putExtra("microphone", 1);
				localIntent1.putExtra("name", "Headset");
				context.sendOrderedBroadcast(localIntent1,
						"android.permission.CALL_PRIVILEGED");

				Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
				KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_HEADSETHOOK);
				localIntent2.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent1);
				context.sendOrderedBroadcast(localIntent2,
						"android.permission.CALL_PRIVILEGED");

				Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
				KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
						KeyEvent.KEYCODE_HEADSETHOOK);
				localIntent3.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent2);
				context.sendOrderedBroadcast(localIntent3,
						"android.permission.CALL_PRIVILEGED");

				Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
				localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				localIntent4.putExtra("state", 0);
				localIntent4.putExtra("microphone", 1);
				localIntent4.putExtra("name", "Headset");
				context.sendOrderedBroadcast(localIntent4,
						"android.permission.CALL_PRIVILEGED");
			}

		} else {
			Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
					KeyEvent.KEYCODE_HEADSETHOOK);
			meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
			context.sendOrderedBroadcast(meidaButtonIntent, null);
		}
	}
	
	
	

}