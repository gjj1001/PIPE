package cn.com.casit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.com.casit.tools.KuangXuanQueryListenner;
import cn.com.casit.tools.LengthLiangSuanListenner;
import cn.com.casit.view.BufferQueryView;
import cn.com.casit.view.DingWeiView;
import cn.com.casit.view.EventReportView;
import cn.com.casit.view.ExitPopupView;
import cn.com.casit.view.PointQueryView;
import cn.com.casit.view.ServerSetPopupView;
import cn.com.casit.view.SheBeiPopupView;
import cn.com.casit.view.UserPopupView;
import cn.com.casit.view.XunJianView;
import cn.com.casit.vo.CItem;
import cn.com.casit.vo.LoginUser;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Envelope;
import android.widget.AdapterView;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;  
import android.widget.Toast;

public class MainActivity extends Activity {

	public ArcGISDynamicMapServiceLayer  dynamicMapServiceLayer ;
	
	public String loginname;//当前
	public String loginpassword;
    public  MapView mMapView;
    LinearLayout linearLayout_top ;   
    LinearLayout  main_leftall123;//左边菜单页面
    int isopen=1;//是否显示左边页面
    public GraphicsLayer draw_graphicsLayer=new GraphicsLayer();// 绘制图形的图层
    
    public LoginUser loginUser=new LoginUser();
    
    EventReportView eventReportView;
    
   public LinearLayout linearLayout_mainwindows ;
    //Envelope fullExtent = new Envelope(103.639711926823, 30.7903974150216,107.972759839346, 33.6435209173561);
    //Envelope fullExtent = new Envelope(115922195.49489748, 3651487.636494998, 11593935.15771025, 3652354.6478050016);
    //管网服务
    Envelope fullExtent = new Envelope(13213255.5491, 23339.96499999985, 13218241.7577, 27857.546300001442);   
    //String mapURL = "http://server.arcgisonline.com/ArcGIS/rest/services/ESRI_StreetMap_World_2D/MapServer";
    String mapURL = "http://192.168.1.23:6080/arcgis/rest/services/zilaishui/MapServer";
    
    //当前选定的二级菜单    
    Button  select_btntwo;
    //当前选定的一级菜单    
    Button  select_btnOne;   
	    //一级菜单的内容
		Button button_chaxuntongji;
		Button button_xunjianrenwu ;
		Button button_shijianshangbao ;
		Button button_xitongshezhi ;
    ///////////////////////////////
    LinearLayout  linearLayout_chaxun;//查询统计的二级菜单
    
	    Button button_dingwei ;
		Button button_dianxuan ;
		Button button_kuangxuan ;
		Button button_juliliangsuan ;
		Button button_mianji;
		Button button_buffer ;	
	
    LinearLayout  linearLayout_shezhi;//系统设置的二级菜单    
    
	    Button button_userinfo ;
		Button button_shebeiinfo ;
		Button button_mapset ;
		Button button_exit ;
    
    ImageView  image_left_control;//控制左边影藏的按钮	
    
	    LinearLayout linearLayout_topleftbg;
	    ImageView imageView_bottom1;
	    LinearLayout imageView_leftshu;   
	
//////////////////////////////////////////////	    
	    String userinfo; 
	    TextView textView_userinfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        
        Bundle extras = getIntent().getExtras();
        userinfo = extras.getString("userinfo");
        loginname=extras.getString("loginname");
        loginpassword=extras.getString("loginpassword");
   ////////////////////////////////////////      
       
        mMapView = (MapView) findViewById(R.id.map);
        linearLayout_top = (LinearLayout) findViewById(R.id.main_top);
    	//ArcGISTiledMapServiceLayer tiledMapServiceLayer = new ArcGISTiledMapServiceLayer(mapURL);
    	dynamicMapServiceLayer = new ArcGISDynamicMapServiceLayer(mapURL);
        mMapView.addLayer(dynamicMapServiceLayer);
    	//this.mMapView.setExtent(fullExtent);
        mMapView.setMapBackground(Color.WHITE, Color.WHITE, 0, 0);//0xfffff
        mMapView.addLayer(this.draw_graphicsLayer);
//查询统计二级菜单
    	button_dingwei = (Button) findViewById(R.id.button_dingwei);
    	button_dianxuan = (Button) findViewById(R.id.button_dianxuan);
    	button_kuangxuan = (Button) findViewById(R.id.button_kuangxuan);
    	button_juliliangsuan = (Button) findViewById(R.id.button_juliliangsuan);
    	button_mianji = (Button) findViewById(R.id.button_mianji);
    	button_buffer = (Button) findViewById(R.id.button_buffer);    	
    	
    	button_dingwei.setOnClickListener(mylisten);
    	button_dianxuan.setOnClickListener(mylisten);
    	button_kuangxuan.setOnClickListener(mylisten);
    	button_juliliangsuan.setOnClickListener(mylisten);
    	button_mianji.setOnClickListener(mylisten);
    	button_buffer.setOnClickListener(mylisten);  
    	
//系统设置二级菜单
    	button_userinfo = (Button) findViewById(R.id.button_userinfo);
    	button_shebeiinfo = (Button) findViewById(R.id.button_shebeiinfo);
    	button_mapset = (Button) findViewById(R.id.button_mapset);
		button_exit = (Button) findViewById(R.id.button_exit);
		
		button_userinfo.setOnClickListener(mylisten);
		button_shebeiinfo.setOnClickListener(mylisten);
		button_mapset.setOnClickListener(mylisten);
		button_exit.setOnClickListener(mylisten);    	   
    	
//地图控制按钮    	
		Button	imageView_fangda = (Button) findViewById(R.id.imageView_fangda);
		Button	imageView_suoxiao = (Button) findViewById(R.id.imageView_suoxiao);
		Button	imageView_quantu = (Button) findViewById(R.id.imageView_quantu);
		Button	imageView_qingchu = (Button) findViewById(R.id.imageView_qingchu);	
		
		imageView_fangda.setOnClickListener(mylisten_map); 
		imageView_suoxiao.setOnClickListener(mylisten_map);
		imageView_quantu.setOnClickListener(mylisten_map); 
		imageView_qingchu.setOnClickListener(mylisten_map); 
		
//顶部一级菜单
    	button_chaxuntongji = (Button) findViewById(R.id.button_chaxuntongji);
    	button_xunjianrenwu = (Button) findViewById(R.id.button_xunjianrenwu);
    	button_shijianshangbao = (Button) findViewById(R.id.button_shijianshangbao);
    	button_xitongshezhi = (Button) findViewById(R.id.button_xitongshezhi);
    	
    	button_chaxuntongji.setOnClickListener(mylistenTop);
    	button_xunjianrenwu.setOnClickListener(mylistenTop);
    	button_shijianshangbao.setOnClickListener(mylistenTop);
    	button_xitongshezhi.setOnClickListener(mylistenTop); 
    	
    	/////
    	
    	linearLayout_mainwindows=(LinearLayout) findViewById(R.id.main_windows);    	
    
    	////
    	linearLayout_chaxun=(LinearLayout) findViewById(R.id.main_left_chaxun);
    	linearLayout_shezhi=(LinearLayout) findViewById(R.id.main_left_shezhi);     
    	  
    	main_leftall123=(LinearLayout) findViewById(R.id.main_leftall123);     	
    	
    	linearLayout_topleftbg=(LinearLayout)findViewById(R.id.linearLayout_topleftbg);  
    	imageView_bottom1=(ImageView)findViewById(R.id.imageView_bottom1);
        imageView_leftshu=(LinearLayout)findViewById(R.id.imageView_leftshu);   		
    		
    
   		image_left_control = (ImageView) this.findViewById(R.id.image_left_control);
   		image_left_control.setOnClickListener(new ImageView.OnClickListener() {
        	   public void onClick(View v) {         	
        		   if(isopen==1){
        			main_leftall123.setVisibility(8);
        			
        			//改变图片背景
        			leftvisible_false();
        			isopen=0;
        			}
        		   else{	
        			 main_leftall123.setVisibility(1);
        				//改变图片背景
        			 leftvisible_true();
        			 isopen=1;
        			 }        		 
        	   }  
        	  });      		  	
    	 
 //////////////////////////////////////////////////////////////////////////////
  		textView_userinfo= (TextView) findViewById(R.id.textView_userinfo);
  	    JSONTokener jsonParser = new JSONTokener(userinfo); 	
	    JSONObject person;
		try {
			person = (JSONObject) jsonParser.nextValue();
			 // 接下来的就是JSON对象的操作了    
			   String  username=  person.getJSONObject("resultObj").getString("name"); 
			  int  userid=  person.getJSONObject("resultObj").getInt("id"); 
			 
			   loginUser.setLoginname(loginname);
			   loginUser.setLoginpassword(loginpassword);
			   loginUser.setName(username);
			   loginUser.setId(userid);
			
			   textView_userinfo.setText("当前用户："+username);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   	   

    }
    
    public void leftvisible_true(){
			linearLayout_topleftbg.setBackgroundResource(R.drawable.top01);         		
 			imageView_bottom1.setImageResource(R.drawable.bottom11);
 			imageView_leftshu.setVisibility(8);    	
    	
    }
    
    public void leftvisible_false(){
		linearLayout_topleftbg.setBackgroundResource(R.drawable.top01_close);
		imageView_bottom1.setImageResource(R.drawable.bottom1_close);
		imageView_leftshu.setVisibility(1); 	
	
     }
        
        OnClickListener mylisten_map = new OnClickListener() {
            public void onClick(View v) {            	
            	
            	if(R.id.imageView_fangda == v.getId())
            	{        		
            		mMapView.zoomin();
            	}
            	if(R.id.imageView_suoxiao == v.getId())
            	{        		
            		mMapView.zoomout();
            	}
            	
            	if(R.id.imageView_quantu== v.getId())
            	{        		
            		mMapView.setExtent(fullExtent);
            	}
            	if(R.id.imageView_qingchu == v.getId())
            	{        		
            		init();
            	}           	
            
              } 
            };
        
    
    OnClickListener mylisten = new OnClickListener() {
        public void onClick(View v) {            	
        	if(select_btntwo!=null)
        	{
        		select_btntwo.setBackgroundResource(R.drawable.leftbtnbg);
        	}
        	 Button button_dangqian = (Button)v;
        	 button_dangqian.setBackgroundResource(R.drawable.leftbtnbg2);
        	 select_btntwo=button_dangqian;
        	 
        		if(R.id.button_dingwei == v.getId())
            	{       
            		init();
            		DingWeiView	dingWeiView=new DingWeiView(MainActivity.this);
            	}
        	 if(R.id.button_dianxuan == v.getId())
        	{       
        		init();
        		PointQueryView	eventReportView=new PointQueryView(MainActivity.this);
        	}
        	 
        	 
        	 if(R.id.button_buffer == v.getId())
         	{       
         		init();
         		BufferQueryView	eventReportView=new BufferQueryView(MainActivity.this);
         	}
        	 
        	 
        	 if(R.id.button_kuangxuan == v.getId())
         	{       
         		init();
         		KuangXuanQueryListenner kuangxuan_Listener = new KuangXuanQueryListenner(MainActivity.this, mMapView);
        		mMapView.setOnTouchListener(kuangxuan_Listener);
         	
             }
        	 
        	 
        	 
        	 if(R.id.button_mianji == v.getId())
         	{       
         		init();
         		LengthLiangSuanListenner lengthLiangSuanListenner = new LengthLiangSuanListenner(MainActivity.this, mMapView);
    			lengthLiangSuanListenner.setType("Polygon");
    			lengthLiangSuanListenner.bIsMeasureLength=false;
    			mMapView.setOnTouchListener(lengthLiangSuanListenner);
         	}
        	 
        	 if(R.id.button_juliliangsuan == v.getId())
          	{       
          		init();
          		LengthLiangSuanListenner lengthLiangSuanListenner = new LengthLiangSuanListenner(MainActivity.this, mMapView);
        		mMapView.setOnTouchListener(lengthLiangSuanListenner);
          	}
        	         	 
        	 
        	if(R.id.button_exit == v.getId())
        	{        		
        		ExitPopupView exitPopupView=new ExitPopupView();
        		exitPopupView.initPopupWindow(R.layout.exit_layout,MainActivity.this);
        		exitPopupView.showPopupWindow();
        	}
        	
        	
        	if(R.id.button_userinfo == v.getId())
        	{             		
        		UserPopupView myPopupWindowDraw=new UserPopupView();
				myPopupWindowDraw.initPopupWindow(R.layout.userview_layout,MainActivity.this);
				myPopupWindowDraw.showPopupWindow();
        	}
        	
        	if(R.id.button_shebeiinfo == v.getId())
        	{             		
        		SheBeiPopupView myPopupWindowDraw=new SheBeiPopupView();
				myPopupWindowDraw.initPopupWindow(R.layout.shebei_view_layout,MainActivity.this);
				myPopupWindowDraw.showPopupWindow();
        	}
        	
        	if(R.id.button_mapset == v.getId())
        	{             		
        		ServerSetPopupView myPopupWindowDraw=new ServerSetPopupView();
				myPopupWindowDraw.initPopupWindow(R.layout.server_view_layout,MainActivity.this);
				myPopupWindowDraw.showPopupWindow();
        	}
        	
        	        
          } 
        };
        
        
        
        
        OnClickListener mylistenTop = new OnClickListener() {
            public void onClick(View v) {            	
            	if(select_btnOne!=null)
            	{
            		select_btnOne.setBackgroundResource(R.drawable.top_btn);
            	}
            	 Button button_dangqian = (Button)v;
            	 button_dangqian.setBackgroundResource(R.drawable.top_btn3);
            	 select_btnOne=button_dangqian;
            	if(R.id.button_xitongshezhi == v.getId())
            	{        
            		init();
            		linearLayout_chaxun.setVisibility(8);
            		linearLayout_shezhi.setVisibility(1);            	
            		 main_leftall123.setVisibility(1);
            		 leftvisible_true();
        			 isopen=1;
            	}
            	if(R.id.button_chaxuntongji == v.getId())
            	{     
            		init();
            		linearLayout_chaxun.setVisibility(1);
            		linearLayout_shezhi.setVisibility(8);            		

            		 main_leftall123.setVisibility(1);
            		 leftvisible_true();
        			 isopen=1;
            	}
            	//巡检任务
            	if(R.id.button_xunjianrenwu == v.getId())
            	{              	 
            		init();      
            		
            		new XunJianView(MainActivity.this);
            	}            	
            	
            	//事件上报
            	if(R.id.button_shijianshangbao == v.getId())
            	{        
            		init();
            		eventReportView=new EventReportView(MainActivity.this);
            		
            	}
            	
              } 
            };
           
      
            
       	 public  void init(){
    		 draw_graphicsLayer.removeAll();
    		  //移除地图长按监听
     		 mMapView.setOnLongPressListener(null);  
     		mMapView.setOnSingleTapListener(null);
    		mMapView.setOnTouchListener(new MapOnTouchListener(mMapView.getContext(), mMapView));
     		 mMapView.getCallout().hide();
     		  this.linearLayout_mainwindows.removeAllViews();
     		 
    	 }
       	 
       	 
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        	
	        	super.onActivityResult(requestCode, resultCode, data);  
	            if (requestCode==8888) {  
	                 Bitmap bitmap = BitmapFactory.decodeFile(eventReportView.mPhotoPath, null);    
	                 eventReportView.mImageView.setImageBitmap(bitmap);  
	                
	            }  
	        }  
          
          
}
