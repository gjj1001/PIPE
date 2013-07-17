package cn.com.casit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	TextView textViewxxx;
	
	String username;
	String password;
	EditText editText_username;
	EditText editText_password;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
       textViewxxx= (TextView) this.findViewById(R.id.textViewxxx);
       editText_username =(EditText) this.findViewById(R.id.editText_username);
       editText_password =(EditText) this.findViewById(R.id.editText_password);
        ImageView imageView_enter= (ImageView) this.findViewById(R.id.imageView_enter);
        
        imageView_enter.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	  
    	    	username=editText_username.getText().toString().trim();
    	    	password=editText_password.getText().toString();
    	    	//获取输入框的值
    	    ResponseOnClick(username,password);
    	    }
    	});
        
        
        
    }
    
    
    ///////////////调用webservice///////////////////////////////////////
    
    //响应按钮单击事件的函数     

     public  void  ResponseOnClick(String username,String password){   
	 HashMap <String ,Object> params=new HashMap<String ,Object>(); 
	 params.put("arg0", username);
	 params.put("arg1", password);
	//创建一个线程     
        HttpThread thread=new HttpThread(handler,LoginActivity.this);   
        String url="http://192.168.1.30:8080/PollingService/MobilePollingPort?wsdl"; 
        String nameSpace = "http://polling.vr.casit.com/";  
        String methodName = "login";   
        // 开始新线程进行WebService请求  
        thread.doStart(url, nameSpace, methodName, params); 
     }      
    /**    
             * 捕获消息队列    
             * 
             */   
    
    
    @SuppressLint("HandlerLeak")
	Handler handler=new Handler(){   
        public void handleMessage(Message m){ 
                ArrayList <String> myList=(ArrayList<String>)m.getData().getStringArrayList("data");     
               if(myList !=null){                         
                               
                                for(int i=0;i<myList.size();i++){     
                                    Map<String, Object> item=new HashMap<String, Object>();     
                                    item.put("name", myList.get(i));                                     
                                 // Toast.makeText(LoginActivity.this, myList.get(i), Toast.LENGTH_SHORT).show();     
                                    textViewxxx.setText( myList.get(i));                                    
                                    String rustlogin="0";                                    
                                    try {   
                                    	    JSONTokener jsonParser = new JSONTokener(myList.get(i));   
                                    	    // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。   
                                    	    // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）   
                                    	    JSONObject person = (JSONObject) jsonParser.nextValue();   
                                    	    // 接下来的就是JSON对象的操作了    
                                    	    rustlogin=  person.getString("resflag");                                      	    
                                    	} catch (JSONException ex) {   
                                    	    // 异常处理代码   
                                    	}                                      	
                                    	if("1".equals(rustlogin)){
                                    	
//                                    //如果成功将跳转
                                
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    
                                    Bundle bundle = new Bundle();//该类用作携带数据
                                  
                                    bundle.putString("userinfo", myList.get(i));
                                    bundle.putString("loginname", username);
                                    bundle.putString("loginpassword", password);
                                  

                                    intent.putExtras(bundle);
                                  //  intent.putExtra("userinfo", myList.get(i));

                                    startActivityForResult(intent, 0);
                            		editText_username.setText("");
                            		editText_password.setText("");
                                    	}  
                                    	else{
                                    		Toast.makeText(LoginActivity.this, "输入不正确！请重新登录！", Toast.LENGTH_SHORT).show(); 
                                    		
                                    	}
                                }    
                }   
        }      

  }; 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
}
