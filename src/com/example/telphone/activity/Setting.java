package com.example.telphone.activity;

import com.example.telphone.R;
import com.example.telphone.TelApplication;
import com.example.telphone.tool.Variable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Setting extends BaseActivity implements OnClickListener,OnCheckedChangeListener{

	private static final String TAG="Setting";
	
	private Button bt_logout;
	
	private ToggleButton tb_auto;
	private ToggleButton tb_bgmusic;
	private ToggleButton tb_extcall;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		
		TelApplication.add(this);
//		TelApplication app = (TelApplication)getApplicationContext();
//		app.add(this);
	}

	private void initView() {
		
		this.tv_title.setText(R.string.title_setting);
		this.setLinearView(R.layout.setting);
		bt_logout = (Button)findViewById(R.id.bt_s_logout);
		
		bt_logout.setOnClickListener(this);
		
		tb_auto = (ToggleButton)findViewById(R.id.tb_s_autoanswer);
		tb_bgmusic = (ToggleButton)findViewById(R.id.tb_s_backmusic);
		tb_extcall = (ToggleButton)findViewById(R.id.tb_s_extcall);
		
		tb_auto.setOnCheckedChangeListener(this);
		
		SharedPreferences mySharedPreferences= getSharedPreferences(Variable.SHARE_PRE_NAME, 
				Activity.MODE_PRIVATE); 
		boolean auto = mySharedPreferences.getBoolean("auto", false);
		tb_auto.setChecked(auto);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.bt_s_logout:
			SharedPreferences mySharedPreferences= getSharedPreferences(Variable.SHARE_PRE_NAME, 
					Activity.MODE_PRIVATE); 
			mySharedPreferences.edit().clear().commit();
//			TelApplication app = (TelApplication)getApplicationContext();
//			app.finishAll();
			TelApplication.finishAll();
			Intent it = new Intent(this,Login.class);
			startActivity(it);

			break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		TelApplication app = (TelApplication)getApplicationContext();
//		app.remove(this);
		TelApplication.remove(this);
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		Log.d(TAG,"state changed:"+arg1);
		switch (arg0.getId())
		{
		case R.id.tb_s_autoanswer:
			if(arg1)
			{
				TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
				
				answerRingingCallWithBroadcast(this,tm);
			}
			SharedPreferences mySharedPreferences= getSharedPreferences(Variable.SHARE_PRE_NAME, 
					Activity.MODE_PRIVATE); 
			Editor e = mySharedPreferences.edit();
			e.putBoolean("auto", arg1);
			e.commit();
			
			break;
		}		
	}
	
	
	//>android 2.3
	private void answerRingingCall1() { 
        //�����
           Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
           localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
           localIntent1.putExtra("state", 1);
           localIntent1.putExtra("microphone", 1);
           localIntent1.putExtra("name", "Headset");
           sendOrderedBroadcast(localIntent1,"android.permission.CALL_PRIVILEGED");
        //���¶�����ť
           Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
           KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
           localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
           sendOrderedBroadcast(localIntent2,"android.permission.CALL_PRIVILEGED");
        //�ſ�������ť
           Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
           KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
           localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
           sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");
        //�γ�����
           Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
           localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
           localIntent4.putExtra("state", 0);
           localIntent4.putExtra("microphone", 1);
           localIntent4.putExtra("name", "Headset");
           sendOrderedBroadcast(localIntent4,"android.permission.CALL_PRIVILEGED");
    }
	
	//android 4.1
	private void answerRingingCall2() { 

	     //�ſ�������ť
	        Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
	        KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
	        localIntent3.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent2);
	        sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");
	        
	        //�����
	           Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
	           localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	           localIntent1.putExtra("state", 1);
	           localIntent1.putExtra("microphone", 1);
	           localIntent1.putExtra("name", "Headset");
	           sendOrderedBroadcast(localIntent1,"android.permission.CALL_PRIVILEGED");
	        //���¶�����ť
	           Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
	           KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
	           localIntent2.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent1);
	           sendOrderedBroadcast(localIntent2,"android.permission.CALL_PRIVILEGED");
	        //�ſ�������ť
	            localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
	            localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
	           localIntent3.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent2);
	           sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");
	        //�γ�����
	           Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
	           localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	           localIntent4.putExtra("state", 0);
	           localIntent4.putExtra("microphone", 1);
	           localIntent4.putExtra("name", "Headset");
	           sendOrderedBroadcast(localIntent4,"android.permission.CALL_PRIVILEGED");
	    }
	
	
	private void answerRingingCallWithBroadcast(Context context,TelephonyManager telmanager){  
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);  
        //�ж��Ƿ�����˶���  
        if (! audioManager.isWiredHeadsetOn()) { 
       //4.1����ϵͳ�����˲���Ȩ�ޣ� ʹ������4.1�汾������ʾ���棺Permission Denial: not allowed to send broadcast android.intent.action.HEADSET_PLUG from pid=1324, uid=10017
//������Ҫע��һ�㣬���͹㲥ʱ����Ȩ�ޡ�android.permission.CALL_PRIVLEGED��������ܸù㲥ʱҲ��Ҫ���Ӹ�Ȩ�ޡ�����4.1���ϰ汾ò�����Ȩ��ֻ��ϵͳӦ�òſ��Եõ������Ե�ʱ���Զ���Ľ������޷����ܵ��˹㲥������ȥ�������Ȩ�ޣ���ΪNULL����Լ������ˡ�

        if(android.os.Build.VERSION.SDK_INT >=15 ){
                Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);  
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);  
                meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);  
                context.sendOrderedBroadcast(meidaButtonIntent, null);  
        }else{
// ����������Android2.3��2.3���ϵİ汾�� �������Է���4.1ϵͳ�ϲ����á�
        Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);  
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);  
                localIntent1.putExtra("state", 1);  
                localIntent1.putExtra("microphone", 1);  
                localIntent1.putExtra("name", "Headset");  
                context.sendOrderedBroadcast(localIntent1,  "android.permission.CALL_PRIVILEGED");  
                
                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);  
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_HEADSETHOOK);  
                localIntent2.putExtra(Intent.EXTRA_KEY_EVENT,   localKeyEvent1);  
                context. sendOrderedBroadcast(localIntent2,  "android.permission.CALL_PRIVILEGED"); 
                
                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);  
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,  KeyEvent.KEYCODE_HEADSETHOOK);  
                localIntent3.putExtra(Intent.EXTRA_KEY_EVENT,  localKeyEvent2);  
                context.sendOrderedBroadcast(localIntent3,   "android.permission.CALL_PRIVILEGED");  
                
                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);  
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);  
                localIntent4.putExtra("state", 0);  
                localIntent4.putExtra("microphone", 1);  
                localIntent4.putExtra("name", "Headset");  
                context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
        }
              
        } else {  
            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);  
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);  
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);  
            context.sendOrderedBroadcast(meidaButtonIntent, null);  
        }  
    } 

}
