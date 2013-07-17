package cn.com.casit.tools;

import java.util.ArrayList;
import java.util.List;


import com.esri.android.action.IdentifyResultSpinner;
import com.esri.android.action.IdentifyResultSpinnerAdapter;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
///import android.widget.Toast;


public class MyIdentifyTask extends AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {

    
    Context context;
    IdentifyTask mIdentifyTask;
    Point mAnchor;
    public MapView mMapView ;
    public GraphicsLayer graphicsLayer;//绘制图形的图层
    public GisRenderSymbol gisRenderSymbol =new GisRenderSymbol();
    
    public MyIdentifyTask(Point anchorPoint,Context context,GraphicsLayer graphicsLayer,MapView mMapView) {
          mAnchor = anchorPoint;
          this.context=context;
          this.mMapView=mMapView;
          this.graphicsLayer=graphicsLayer;
 }
    @Override
    protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
          IdentifyResult[] mResult = null;
          if (params != null && params.length > 0) {
               IdentifyParameters mParams = params[0];
               try {
                     //获取要素数据
                     mResult = mIdentifyTask.execute(mParams);
               } catch (Exception e) {
                     e.printStackTrace();
               }
          }
          return mResult;
    }

    @Override
    protected void onPostExecute(IdentifyResult[] results) {
	if(results.length>0){ 
          ArrayList<IdentifyResult> resultList = new ArrayList<IdentifyResult>();
          for (int index=0; index < results.length; index++){
               if(results[index].getAttributes().get(results[index].getDisplayFieldName())!=null)
                     resultList.add(results[index]);
		
          }                  
              Graphic  drawGraphic=new Graphic(results[0].getGeometry(),gisRenderSymbol.fillSymbol);
              this.graphicsLayer.removeAll();
	      graphicsLayer.addGraphic(drawGraphic);	      
	     // Toast toast = Toast.makeText(context,results[0].getAttributes().get("name").toString(), Toast.LENGTH_LONG);//提示被点击了
         //toast.show(); 
	      
	      mMapView.getCallout().show(mAnchor, createIdentifyContent(resultList));
          }
	else{
	    this.graphicsLayer.removeAll();
	    mMapView.getCallout().hide();}
      
    }
    
    @Override
    protected void onPreExecute() {
    //实例化一个要素识别类对象
      mIdentifyTask = new IdentifyTask("http://192.168.1.23:6080/arcgis/rest/services/zilaishui/MapServer");
    }
    
    
	private ViewGroup createIdentifyContent(final List<IdentifyResult> results){

	          LinearLayout layout = new LinearLayout(context);
	          layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	          layout.setOrientation(LinearLayout.VERTICAL);
	          
	          
	          TextView textview1=new TextView(context);
	          textview1.setText("图层ID:" + results.get(0).getAttributes().get("OBJECTID"));
	          textview1.setTextColor(Color.rgb(112, 112, 112));  

	          TextView textview2=new TextView(context);
	          textview2.setText("图层名称："+results.get(0).getAttributes().get("NAME"));
	          textview2.setTextColor(Color.rgb(112, 112, 112));  
	          
	       
	          
	          layout.addView(textview1);
	          layout.addView(textview2);
	          
	         return layout;
	    }
	
	
	/**
	 * This class allows the user to customize the string shown in the callout.
	 * By default its the display field name.
	 *
	 */
	public class MyIdentifyAdapter extends IdentifyResultSpinnerAdapter{
		String m_show = null;
		List<IdentifyResult> resultList;
		int currentDataViewed = -1;
		Context m_context;


		public MyIdentifyAdapter(Context context, List<IdentifyResult> results) {
			super(context,results);
			this.resultList = results;
			this.m_context = context;
			
		}

		//This is the view that will get added to the callout
		//Create a text view and assign the text that should be visible in the callout		
		public View getView(int position, View convertView, ViewGroup parent) {
			String outputVal = null;
			TextView txtView;
			IdentifyResult curResult = this.resultList.get(position);
	
			
			if(curResult.getAttributes().containsKey("name")){			
				outputVal = curResult.getAttributes().get("name").toString();			
			}
			
			txtView = new TextView(this.m_context);
			txtView.setText(outputVal);
			txtView.setTextColor(Color.BLACK);
			txtView.setLayoutParams(new ListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			txtView.setGravity(Gravity.CENTER_VERTICAL);
		
			return txtView;
		}
		
	}
 
}
