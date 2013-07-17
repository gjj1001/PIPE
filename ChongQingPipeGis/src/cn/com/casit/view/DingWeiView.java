package cn.com.casit.view;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.com.casit.MainActivity;
import cn.com.casit.R;
import cn.com.casit.tools.GisRenderSymbol;

public class DingWeiView {
	
	 public MainActivity mainActivity;
	 EditText	editText_quyuid;
	 GraphicsLayer draw_graphicsLayer;
	 MapView mMapView;
		public GisRenderSymbol gisRenderSymbol =new GisRenderSymbol();
	public DingWeiView(MainActivity mainActivity1){
		
		this.mainActivity=mainActivity1;
		this.draw_graphicsLayer=mainActivity.draw_graphicsLayer;
		this.mMapView=this.mainActivity.mMapView;
		LinearLayout layout  =(LinearLayout)LayoutInflater.from(mainActivity).inflate(R.layout.dingwei_layout, null);
		mainActivity.linearLayout_mainwindows.addView(layout);			
		
        editText_quyuid=(EditText)layout.findViewById(R.id.editText_quyuid);
        Button  button_dingwei=(Button)layout.findViewById(R.id.button_dingwei);
        
        button_dingwei.setOnClickListener(new OnClickListener() {        	    

	      	   public void onClick(View v) {
	      		 rundingweiquery();
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
	 
	
	public  void rundingweiquery(){
		
		String url = "http://192.168.1.23:6080/arcgis/rest/services/zilaishui/MapServer";
    	String targetLayer = url.concat("/0");
    	String[] queryParams = { targetLayer, "name='"+editText_quyuid.getText()+"'"};
    //	String[] queryParams = { targetLayer, "OBJECTID='1'"};
        
//    	/OBJECTID 
    	AsyncQueryTask ayncQuery = new AsyncQueryTask();
    	ayncQuery.execute(queryParams);
	}
	
	/**
	 * 
	 * Query Task executes asynchronously.
	 * 
	 */
	ProgressDialog progress;
	public class AsyncQueryTask extends AsyncTask<String, Void, FeatureSet> {

		protected void onPreExecute() {
		    progress = ProgressDialog.show(mainActivity, "","正在执行框选查询...");
		}

		protected FeatureSet doInBackground(String... queryParams) {
			if (queryParams == null || queryParams.length <= 1)
				return null;
			
			String url = queryParams[0];
			Query query = new Query();
			String whereClause = queryParams[1];
			SpatialReference sr = SpatialReference.create(102113);//这个一定要设置正确,不然不会渲染
			//query.setGeometry(polygon);
			query.setOutSpatialReference(sr);
			query.setReturnGeometry(true);
			query.setWhere(whereClause);
			QueryTask qTask = new QueryTask(url);
			FeatureSet fs = null;

			try {
				fs = qTask.execute(query);
			} catch (Exception e) {
				
				e.printStackTrace();
				return fs;
			}
			return fs;

		}
	
		protected void onPostExecute(FeatureSet result) {
			
			String message = "没有结果返回";
			if (result != null) {
				Graphic[] grs = result.getGraphics();
				
				if (grs.length > 0) {
				 
				    SimpleFillSymbol symbol = new SimpleFillSymbol(Color.RED);
			            symbol.setAlpha(99);			
			           // draw_graphicsLayer.setRenderer(new SimpleRenderer(symbol));
			            draw_graphicsLayer.setRenderer(new SimpleRenderer(gisRenderSymbol.markerSymbol));
				           
			            draw_graphicsLayer.removeAll();//移除以前的
			            draw_graphicsLayer.addGraphics(grs);
				    message = (grs.length == 1 ? "1 result has " : Integer
							.toString(grs.length) + " results have ")
							+ "come back";			    
				    
				    //定位缩放操作
				  //  mMapView.setExtent(grs[0].getGeometry());
				    
				   Point p1=(Point) grs[0].getGeometry();
				   
				  mMapView.zoomTo(p1, 0);
				  mMapView.zoomin();
				//  mMapView.centerAt(p1, true);
				}

			}
			progress.dismiss();
			Toast toast = Toast.makeText(mainActivity, message,Toast.LENGTH_LONG);
			toast.show();
		

		}

	}

}
