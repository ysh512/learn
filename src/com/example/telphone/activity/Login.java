package com.example.telphone.activity;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.telphone.TelApplication;
import com.example.telphone.R;
import com.example.telphone.tool.Variable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends BaseActivity implements OnClickListener{
	
	private static final String TAG= "Login";
	
	//---------------------UI-----------------------
	
	//login url : http://121.40.100.250:99/CallReqRet.php?UserID=%s&CallTo=auth
	private Button btn_login;
	private Button btn_recharge;
	private EditText et_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPreferences= getSharedPreferences(Variable.SHARE_PRE_NAME, 
				Activity.MODE_PRIVATE); 
				// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ 
				String login =sharedPreferences.getString("login", ""); 
				String phone =sharedPreferences.getString("phone", ""); 
		if(login.equals("true"))
		{
			this.finish();
			Intent it = new Intent(Login.this,Container.class);
			startActivity(it);
		}
		
		TelApplication.init(this);
		
		initView();
		
		TelApplication.add(this);
	}

	private void initView() {
		this.setLinearView(R.layout.login);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_recharge = (Button)findViewById(R.id.btn_recharge);
		
		btn_login.setOnClickListener(this);
		btn_recharge.setOnClickListener(this);
		et_account = (EditText)findViewById(R.id.editText1);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId())
		{
		case R.id.btn_login:
			LoginTask lt = new LoginTask();
			lt.execute();
			break;
		case R.id.btn_recharge:
			Intent it = new Intent(this,Recharge.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}
	
	class LoginTask extends AsyncTask<Integer, Integer, String>
	{
		boolean login = false;
		
		ProgressDialog dialog ;
		
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(Login.this, "��¼", "���ڵ�½...");
//			dialog.show();
		}

		@Override
		protected String doInBackground(Integer... params) {
			
			HttpClient client = new DefaultHttpClient();  
	        HttpGet get = new HttpGet("http://121.40.100.250:99/CallReqRet.php?UserID="+et_account.getText().toString()+"&CallTo=auth");  
	        HttpResponse response= null;
	        BufferedReader in = null;  
			try {
				response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					login = true;
					String result = EntityUtils.toString(response.getEntity());

					Log.d(TAG, result);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
			
			
			return null;
		}
		
		protected void onPostExecute(String result) 
		{
			dialog.dismiss();
			if(login)
			{
				
				Intent it = new Intent(Login.this,Container.class);
				startActivity(it);
				//ʵ����SharedPreferences���󣨵�һ���� 
				SharedPreferences mySharedPreferences= getSharedPreferences(Variable.SHARE_PRE_NAME, Activity.MODE_PRIVATE); 
				//ʵ����SharedPreferences.Editor���󣨵ڶ����� 
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				//��putString�ķ����������� 
				editor.putString("login", "true"); 
				editor.putString("phone", et_account.getText().toString()); 
				//�ύ��ǰ���� 
				editor.commit(); 
				finish();
				//ʹ��toast��Ϣ��ʾ����ʾ�ɹ�д������ 

			}
			else
			{
				Toast.makeText(Login.this, "��¼ʧ�ܣ�����������û���", 5).show();
			}
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		TelApplication app = ((TelApplication)getApplicationContext());
//        app.remove(this);
		TelApplication.remove(this);
	}
	
	
	
}
