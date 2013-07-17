package cn.com.casit.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.google.gson.Gson;

import cn.com.casit.HttpThread2;
import cn.com.casit.MainActivity;
import cn.com.casit.R;
import cn.com.casit.tools.GisRenderSymbol;
import cn.com.casit.vo.CItem;
import cn.com.casit.vo.LoginUser;
import cn.com.casit.vo.ReportEventBean;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class EventReportView {
	
	
    Button  button_paizhao;//����
    Button  button_shijian_tijiao;//
    public ImageView mImageView;  
    public File mPhotoFile;  
    public String mPhotoPath;  
    public final static int CAMERA_RESULT=8888;  
    public final static String TAG="xx"; 
    public MainActivity mainActivity;
    public Spinner  spinner;
    EditText editText_beizhu;
    EditText editText_gps;
    String jsonStr;
    String filename; 
    String image;
    LoginUser loginUser;
    MapView mMapView;
    GraphicsLayer draw_graphicsLayer;
    DecimalFormat De = new DecimalFormat("0.0000"); 
    Point pt ;
    public GisRenderSymbol gisRenderSymbol =new GisRenderSymbol();
    
	public EventReportView(MainActivity mainActivity1){
		
		this.mainActivity=mainActivity1;
		this.draw_graphicsLayer=mainActivity.draw_graphicsLayer;
		this.mMapView=this.mainActivity.mMapView;
		
		mMapView.setOnLongPressListener(mapOnLongPressListener);
		
		
		//����ͼ�е����ʵ����������¼�����
		//  LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.event_reporting_layout, null).findViewById(R.id.hellolayout);
	    LinearLayout layout = (LinearLayout)LayoutInflater.from(mainActivity).inflate(R.layout.event_reporting_layout, null);
	  //  mainActivity.linearLayout_mainwindows.removeAllViews();
		mainActivity.linearLayout_mainwindows.addView(layout);		 
		
		spinner = (Spinner) layout.findViewById(R.id.spinner_shijiantype);
               	
		button_paizhao=(Button) layout.findViewById(R.id.button_paizhao);    	
    	mImageView = (ImageView) layout.findViewById(R.id.zhaopianimageView);  
    	button_paizhao.setOnClickListener(mylisten_paizhao);
    	
    	 editText_beizhu=(EditText) layout.findViewById(R.id.editText_beizhu); 
    	 editText_gps=(EditText) layout.findViewById(R.id.editText_gps); 
    	 loginUser= mainActivity.loginUser;
	      
    	// Toast.makeText(mainActivity, "�û�ID:"+loginUser.getId(), Toast.LENGTH_LONG).show();
			


    	
    	  //////�ر��¼��ϱ���       
        ImageView  image_shijian = (ImageView) layout.findViewById(R.id.image_shijian);
        image_shijian.setOnClickListener(new ImageView.OnClickListener() {        	    

      	   public void onClick(View v) {  
      		 closethewindow();
      	   }  
      	  }); 
        
         ResponseOnClick(mainActivity.loginname,mainActivity.loginpassword);      

        
   	  //////�ύ����     
         button_shijian_tijiao = (Button) layout.findViewById(R.id.button_shijian_tijiao);
         button_shijian_tijiao.setOnClickListener(new ImageView.OnClickListener() {        	    

      	   public void onClick(View v) {
      		   //����ж�һ�����ύ
      		   if("".equals(editText_gps.getText().toString())){
      			 Toast.makeText(mainActivity, "����ȷ��д��Ϣ!", Toast.LENGTH_LONG).show();
       			 
      			  
      		   }
      		   else{
      			uploadEventOnClick(mainActivity.loginname,mainActivity.loginpassword); 
           	  
      			 
      		   }
      		}  
      	  });        
        
	}
	
	
	 OnClickListener mylisten_paizhao = new OnClickListener() {
	        public void onClick(View v) {            	
	        	
	        	try {  
	                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	                
	                filename= getPhotoFileName();
	                mPhotoPath="mnt/sdcard/DCIM/Camera/"+filename;  
	                mPhotoFile = new File(mPhotoPath);  
	                if (!mPhotoFile.exists()) {  
	                    mPhotoFile.createNewFile();
	                    
	                }  
	              
	                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));  
	                mainActivity.startActivityForResult(intent,CAMERA_RESULT); 
	               // 
	                
	            } catch (Exception e) {  
	            }  

	          } 
	        };
	        
	        private String getPhotoFileName() {  
	            Date date = new Date(System.currentTimeMillis());  
	            SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");  
	            return dateFormat.format(date) + ".jpg";  
	        }  
	    

	        
/////////����������¼�����/////////////////////////////
	        
            public  void  ResponseOnClick(String loginname,String loginpassword){   
           	 HashMap <String ,Object> params=new HashMap<String ,Object>(); 
           	 params.put("arg0", loginname);
           	 params.put("arg1", loginpassword);
           	//����һ���߳�     
                   HttpThread2 thread=new HttpThread2(handler,mainActivity);   
                   String url="http://192.168.1.30:8080/PollingService/MobilePollingPort?wsdl"; 
                   String nameSpace = "http://polling.vr.casit.com/";  
                   String methodName = "getEventType";   
                   // ��ʼ���߳̽���WebService����  
                   thread.doStart(url, nameSpace, methodName, params); 
                }  
            List<CItem> lst = new ArrayList<CItem>();
            @SuppressLint("HandlerLeak")
        	Handler handler=new Handler(){   
                public void handleMessage(Message m){ 
                        ArrayList <String> myList=(ArrayList<String>)m.getData().getStringArrayList("data");     
                       if(myList !=null){                       
                                       
                                        for(int i=0;i<myList.size();i++){   
                                                                
                                            try {   
                                            	    JSONTokener jsonParser = new JSONTokener(myList.get(i));   
                                            	  JSONObject person = (JSONObject) jsonParser.nextValue();   
                                            	    // �������ľ���JSON����Ĳ�����    
                                            	 String resultObj=  person.getString("resultObj");  
                                            	 JSONArray jsonArray = new JSONArray(resultObj);                                            	 
                                            	 for (int ii = 0; ii < jsonArray.length(); ii++)
                                            	   {
                                            		 JSONObject   jsonObject = jsonArray.getJSONObject(ii);
                                            		String xxx= jsonObject.getString("name");
                                            		int yyy=jsonObject.getInt("id");
                                            		// Toast.makeText(MainActivity.this, xxx, Toast.LENGTH_SHORT).show();     
                                            		CItem item = new CItem(yyy, xxx);
                                        			lst.add(item);
                                            	   }

                                            	} catch (JSONException ex) {   
                                            	    // �쳣�������   
                                            	}                                      	
                                          
                                        }  
                                	
                                		ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(mainActivity, android.R.layout.simple_spinner_item, lst);
                                		//���������б�ķ��
                                		myaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                		
                                		//��adapter ��ӵ�spinner��
                                		spinner.setAdapter(myaAdapter);                                 		
                                		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                                			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                				/*
                                				 * ids�Ǹո��½���list�����ID
                                				 */
                                				//int ids = ((CItem) spinner.getSelectedItem()).GetID();
                                				
                                              }

                                			public void onNothingSelected(AdapterView<?> arg0) {
                                				
                                			}
                                		});
                                		
                                		
                        }   
                }      

          }; 
          
          public OnLongPressListener mapOnLongPressListener=  new OnLongPressListener() {

  			private static final long serialVersionUID = 1L;

  			public void onLongPress(float x, float y) {
  			
  			pt = mMapView.toMapPoint(x, y);
  			Graphic drawGraphic=new Graphic(pt,gisRenderSymbol.markerSymbol); 
  			draw_graphicsLayer.removeAll();
  			draw_graphicsLayer.addGraphic(drawGraphic);	
  			
  			
  			String gpsinfo="X:"+De.format(pt.getX()) +";"+ "Y:"+De.format(pt.getY());
  			editText_gps.setText(gpsinfo);
  			//
  	       }
  	
  	     };
  	     
  	 //���ڹرյĺ���
    	 public  void closethewindow(){
    		 draw_graphicsLayer.removeAll();
    		  //�Ƴ���ͼ��������
      		 mMapView.setOnLongPressListener(null);     		
      		 mainActivity.linearLayout_mainwindows.removeAllViews();
    		 
    	 }
          
///////////�ύ����////////////////////////////////
	
          //////////////////////////////////////
          public  void  uploadEventOnClick(String loginname,String loginpassword){   
         	      HashMap <String ,Object> params=new HashMap<String ,Object>();
         	 
         	      Bitmap bitmapOrg = BitmapFactory.decodeFile(mPhotoPath);
         	        ByteArrayOutputStream bao = new ByteArrayOutputStream();
   
         	        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);  
   
         	        byte[] ba = bao.toByteArray();    
         	        image=  Base64.encodeToString(ba, Base64.DEFAULT);
         	
         	        
         	      SimpleDateFormat ww = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
         	      String date_now = ww.format(new java.util.Date());
         	      
         	      
         		     ReportEventBean event = new ReportEventBean();
         		     event.setInspectstaffid(loginUser.getId());//�û�ID
         		     event.setDeviceid(1001);
         		     event.setPollingeventid(((CItem) spinner.getSelectedItem()).GetID());
         		     event.setReporttime(date_now);
         		     event.setPipecode("qwe");
         		     event.setDescription(editText_beizhu.getText().toString());//��ע
         		     event.setLatitude(Double.valueOf(De.format(pt.getY())));
         		     event.setLongitude(Double.valueOf(De.format(pt.getX())));
         		     Gson gson=new Gson();
         		     jsonStr=gson.toJson(event);    
         	
         	       
		         	 params.put("arg0", loginname);
		         	 params.put("arg1", loginpassword);         	 
		         	 params.put("arg2", jsonStr);
		         	 params.put("arg3", filename);
		         	 params.put("arg4", image);
         	 
         	//����һ���߳�     
                 HttpThread2 thread=new HttpThread2(handler2,mainActivity);   
                 String url="http://192.168.1.30:8080/PollingService/MobilePollingPort?wsdl"; 
                 String nameSpace = "http://polling.vr.casit.com/";  
                 String methodName = "uploadEvent";   
                 // ��ʼ���߳̽���WebService����  
                 thread.doStart(url, nameSpace, methodName, params); 
              }  
         
          @SuppressLint("HandlerLeak")
      	Handler handler2=new Handler(){   
              public void handleMessage(Message m){ 
                      ArrayList <String> myList=(ArrayList<String>)m.getData().getStringArrayList("data");     
                     if(myList !=null){                      
                                     
                                      for(int i=0;i<myList.size();i++){                                                                                   	  
                                    	 
                                          try {   
                                          	    JSONTokener jsonParser = new JSONTokener(myList.get(i));   
                                          	    JSONObject person = (JSONObject) jsonParser.nextValue();   
                                          	    // �������ľ���JSON����Ĳ�����    
                                          	    String resultObj=  person.getString("resflag");
                                          	    if("1".equals(resultObj)){
                                          	    	//��ʾ�ύ�ɹ�
                                          	    	Toast.makeText(mainActivity, "������ӳɹ�!", Toast.LENGTH_LONG).show();
                                          			//�ر��������
                                          	    	closethewindow();
                                          	    }
                                          	    else{
                                          	    	Toast.makeText(mainActivity, "�������ʧ��!", Toast.LENGTH_LONG).show();
                                          			                                         	    	
                                          	    }                                          	 
                                          	} catch (JSONException ex) {   
                                          	    // �쳣�������   
                                          	}
                                      }
                      }   
              }  
        }; 
        
        
   
        
}
