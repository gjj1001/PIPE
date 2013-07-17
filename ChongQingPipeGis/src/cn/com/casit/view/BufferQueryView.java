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
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

import cn.com.casit.MainActivity;
import cn.com.casit.R;
import cn.com.casit.tools.GisRenderSymbol;
import cn.com.casit.tools.MyIdentifyTask;
import cn.com.casit.vo.CItem;
import cn.com.casit.vo.LoginUser;

public class BufferQueryView {
	
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
	    
	public BufferQueryView(MainActivity mainActivity1){
		this.mainActivity=mainActivity1;
		this.draw_graphicsLayer=mainActivity.draw_graphicsLayer;
		this.mMapView=this.mainActivity.mMapView;
		
		
		//���ҳ��
	    LinearLayout layout = (LinearLayout)LayoutInflater.from(mainActivity).inflate(R.layout.buffer_query_layout, null);
	   // mainActivity.linearLayout_mainwindows.removeAllViews();
		mainActivity.linearLayout_mainwindows.addView(layout);		
		spinner = (Spinner) layout.findViewById(R.id.spinner_type);
		
		button_jihuo=(Button) layout.findViewById(R.id.button_jihuo);  
		
		ArcGISDynamicMapServiceLayer dynamicMapServiceLayer=this.mainActivity.dynamicMapServiceLayer;
		 ArcGISLayerInfo[]  agsinfos=dynamicMapServiceLayer.getAllLayers() ;
		 
		List<CItem> lst = new ArrayList<CItem>();
	
  		//String xxx= jsonObject.getString("name");
  		//int yyy=jsonObject.getInt("id");
  		// Toast.makeText(MainActivity.this, xxx, Toast.LENGTH_SHORT).show();     
  		CItem item = new CItem(0,"��");
		lst.add(item);
  		CItem item2 = new CItem(1,"��");
		lst.add(item2);
		 
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
    				
    				//��map���һ�������ͼ���¼�����       
	      		        mMapView.setOnSingleTapListener(new OnSingleTapListener() {         

	      		                  private static final long serialVersionUID = 1L;  

	      		                  public void onSingleTap(final float x,final float y) {   	      		                       

	      		                       if(!mMapView.isLoaded()){
	      		                                   return;
	      		                       }
	      		                     draw_graphicsLayer.removeAll();
	      		                 	Point pt = mMapView.toMapPoint(new Point(x, y));
	      		  	    		Polygon pg = GeometryEngine.buffer(pt, mMapView.getSpatialReference(), 50, null);
	      		  	    		//Envelope nowExtent = new Envelope();	    		   
	      		  	    		//mMapView.getExtent().queryEnvelope(nowExtent);	
	      		  	    		//nowExtent.setYMax(nowExtent.getYMax()-0.02);
	      		  	    		//Geometry geo = GeometryEngine.clip(pg,nowExtent, mMapView.getSpatialReference());//�õ�ǰ��ͼ��Χ���вü�
	      		  	    		Graphic g1 = new Graphic(pg,gisRenderSymbol.fillSymbol);
	      		  	    		draw_graphicsLayer.addGraphic(g1);
	      		  	    		
	      		  	    		
	      		                  }
	      		        });
    				
                  }

    			public void onNothingSelected(AdapterView<?> arg0) {
    				
    			}
    		});
    		
    		
		
		
    		//��ʼ����
    		button_jihuo.setOnClickListener(new OnClickListener() {        	    

    	      	   public void onClick(View v) {
    	      		//��ʼ���ѯ
    	      		   
    	      	   }
    	      	  });    
    		 
    		 
    		 
    		 
		  //////�رտ�       
        ImageView  image_shijian = (ImageView) layout.findViewById(R.id.image_shijian);
        image_shijian.setOnClickListener(new ImageView.OnClickListener() {        	    

      	   public void onClick(View v) {  
      		 closethewindow();
      	   }  
      	  }); 
		
		
	}
	
	 //���ڹرյĺ���
	 public  void closethewindow(){
		 draw_graphicsLayer.removeAll();
		 mMapView.getCallout().hide();
		  //�Ƴ���ͼ��������
 		 mMapView.setOnLongPressListener(null);  
 		mMapView.setOnSingleTapListener(null);
 		 mainActivity.linearLayout_mainwindows.removeAllViews();
 		 
	 }

}







