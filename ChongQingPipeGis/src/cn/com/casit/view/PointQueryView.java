package cn.com.casit.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

import cn.com.casit.MainActivity;
import cn.com.casit.R;
import cn.com.casit.tools.GisRenderSymbol;
import cn.com.casit.tools.MyIdentifyTask;
import cn.com.casit.vo.CItem;
import cn.com.casit.vo.LoginUser;

public class PointQueryView {
	
	    public MainActivity mainActivity;
	    public Spinner  spinner;
	    EditText editText_rongcha;
	    Button button_jihuo;
	    String jsonStr;	   
	    String image;
	    LoginUser loginUser;
	    MapView mMapView;
	    GraphicsLayer draw_graphicsLayer;
	    DecimalFormat De = new DecimalFormat("0.0000"); 
	    Point pt ;
	    public GisRenderSymbol gisRenderSymbol =new GisRenderSymbol();
	    
	public PointQueryView(MainActivity mainActivity1){
		this.mainActivity=mainActivity1;
		this.draw_graphicsLayer=mainActivity.draw_graphicsLayer;
		this.mMapView=this.mainActivity.mMapView;
		
		
		//添加点选的页面
	    LinearLayout layout = (LinearLayout)LayoutInflater.from(mainActivity).inflate(R.layout.pointquery_layout, null);
	   // mainActivity.linearLayout_mainwindows.removeAllViews();
		mainActivity.linearLayout_mainwindows.addView(layout);		
		spinner = (Spinner) layout.findViewById(R.id.spinner_layers);
		
		button_jihuo=(Button) layout.findViewById(R.id.button_jihuo);  
		
		ArcGISDynamicMapServiceLayer dynamicMapServiceLayer=this.mainActivity.dynamicMapServiceLayer;
		 ArcGISLayerInfo[]  agsinfos=dynamicMapServiceLayer.getAllLayers() ;
		 
		List<CItem> lst = new ArrayList<CItem>();
		 for (int ii = 0; ii < agsinfos.length; ii++)
  	   {
			 ArcGISLayerInfo agsinfo= agsinfos[ii];
			
  		//String xxx= jsonObject.getString("name");
  		//int yyy=jsonObject.getInt("id");
  		// Toast.makeText(MainActivity.this, xxx, Toast.LENGTH_SHORT).show();     
  		CItem item = new CItem(agsinfo.getId(),agsinfo.getName());
			lst.add(item);
  	   }
		 
			ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(mainActivity, android.R.layout.simple_spinner_item, lst);
    		//设置下拉列表的风格
    		myaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		
    		//将adapter 添加到spinner中
    		spinner.setAdapter(myaAdapter);                                 		
    		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    				/*
    				 * ids是刚刚新建的list里面的ID
    				 */
    				//int ids = ((CItem) spinner.getSelectedItem()).GetID();
    				
                  }

    			public void onNothingSelected(AdapterView<?> arg0) {
    				
    			}
    		});
    		
    		
		
		
    		//激活点选按钮
    		button_jihuo.setOnClickListener(new OnClickListener() {        	    

    	      	   public void onClick(View v) {
    	      		//开始点查询
    	      		   
    	      		   
    	      		   
    	      		//实例化对象，并且给实现初始化相应的值

      	      		 final IdentifyParameters	 params = new IdentifyParameters();

      	      		            params.setTolerance(5);
      	      		            params.setDPI(98);
      	      		            params.setLayers(new int[]{7});
      	      		            params.setLayerMode(IdentifyParameters.ALL_LAYERS);

      	      		 //给map添加一个点击地图的事件监听       
      	      		        mMapView.setOnSingleTapListener(new OnSingleTapListener() {         

      	      		                  private static final long serialVersionUID = 1L;  

      	      		                  public void onSingleTap(final float x,final float y) {   	      		                       

      	      		                       if(!mMapView.isLoaded()){
      	      		                                   return;
      	      		                       }
      	      		                       //establish the identify parameters
      	      		                             Point identifyPoint = mMapView.toMapPoint(x, y);
      	      		                             params.setGeometry(identifyPoint);
      	      		                             params.setSpatialReference(mMapView.getSpatialReference());                                             
      	      		                             params.setMapHeight(mMapView.getHeight());
      	      		                             params.setMapWidth(mMapView.getWidth());
      	      		                             Envelope env = new Envelope();
      	      		                             mMapView.getExtent().queryEnvelope(env);
      	      		                             params.setMapExtent(env);
      	      		                             //我们自己扩展的异步类
      	      		                             //GYAndroidGisActivity.this, draw_graphicsLayer, mMapView
      	      		                             MyIdentifyTask mTask = new MyIdentifyTask(identifyPoint,mainActivity,draw_graphicsLayer,mMapView);
      	      		                             mTask.execute(params);//执行异步操作并传递所需的参数                     

      	      		                       }

    	      		            });  
    	      		            
    	      		            
    	      		   
    	      		}  
    	      	  });    
    		 
    		 
    		 
    		 
		  //////关闭框       
        ImageView  image_shijian = (ImageView) layout.findViewById(R.id.image_shijian);
        image_shijian.setOnClickListener(new ImageView.OnClickListener() {        	    

      	   public void onClick(View v) {  
      		 closethewindow();
      	   }  
      	  }); 
		
		
	}
	
	 //窗口关闭的函数
	 public  void closethewindow(){
		 draw_graphicsLayer.removeAll();
		 mMapView.getCallout().hide();
		  //移除地图长按监听
 		 mMapView.setOnLongPressListener(null);  
 		mMapView.setOnSingleTapListener(null);
 		 mainActivity.linearLayout_mainwindows.removeAllViews();
 		 
	 }

}







