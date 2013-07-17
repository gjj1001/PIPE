package cn.com.casit.tools;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import cn.com.casit.MainActivity;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;

public class KuangXuanQueryListenner extends MapOnTouchListener{
    public static final int POLYGON = 4;
    public static final int POLYLINE = 3;
    public int drawType=4;
    private Polygon tempPolygon = null;//记录绘制过程中的多边形   
	MapView mMapView ;
	private Graphic drawGraphic;
	public GraphicsLayer graphicsLayer;//绘制图形的图层
	private Point startPoint;
	   private Point ptPrevious = null;//上一个点   
    public Polyline polyline;	
	public Polygon polygon;	
	public GisRenderSymbol gisRenderSymbol =new GisRenderSymbol();
	MainActivity gYGisAndroidActivity;
	
    public KuangXuanQueryListenner(Context context, MapView mapview) {
	super(context, mapview);
	gYGisAndroidActivity=(MainActivity)context;
	this.mMapView=mapview;
	this.graphicsLayer=gYGisAndroidActivity.draw_graphicsLayer;
    }
    
    
    public boolean onSingleTap(MotionEvent event) {
	
	Log.d("我的TAG", "onSingleTap发生了(用于画线段和多边形)"); 
	Point ptCurrent = mMapView.toMapPoint(event.getX(), event.getY());    				
	if ( (drawType==POLYGON || drawType==POLYLINE)) {
		switch (drawType) {
		case POLYGON:
			
			  
          //  points.add(ptCurrent);//将当前点加入点集合中   
             
           if(startPoint == null){//画线或多边形的第一个点   
        	   startPoint = ptCurrent;  
                                      
               //绘制第一个点   
               Graphic graphic = new Graphic(startPoint,gisRenderSymbol.markerSymbol);  
               graphicsLayer.addGraphic(graphic);                                    
           }  
            else{//画线或多边形的其他点   
                //绘制其他点   
               Graphic graphic = new Graphic(ptCurrent,gisRenderSymbol.markerSymbol);  
               graphicsLayer.addGraphic(graphic);  
                  
                //生成当前线段（由当前点和上一个点构成）   
                Line line = new Line();  
                line.setStart(ptPrevious);  
                line.setEnd(ptCurrent);  
                  
                
                    //绘制临时多边形   
                    if(tempPolygon == null) tempPolygon = new Polygon();  
                    tempPolygon.addSegment(line, false);  
                      
                    graphicsLayer.removeAll();  
                    Graphic g = new Graphic(tempPolygon, gisRenderSymbol.fillSymbol);  
                    graphicsLayer.addGraphic(g);  
                      
                    //计算当前面积   


            }  
              
            ptPrevious = ptCurrent;  
            
		}}
	 return true; 
}

public boolean onDoubleTap(MotionEvent event) {			
	
	if ((drawType==POLYGON || drawType==POLYLINE)) {

		 Log.d("我的TAG", "onDoubleTap发生了(双击)");
		Point point = mMapView.toMapPoint(event.getX(), event.getY());
		switch (drawType) {						
		case POLYGON:
			tempPolygon.lineTo(point);  
			this.graphicsLayer.removeAll();
			drawGraphic=new Graphic(polygon,gisRenderSymbol.fillSymbol);			
			//开始框选查询
			MyArcGisQuery myArcGisQuery=new MyArcGisQuery();			
			myArcGisQuery.run(tempPolygon, graphicsLayer,gYGisAndroidActivity);
		
			break;
		
		}
		//sendDrawEndEvent();
		//开始渲染画的图形						
   	
		startPoint = null;
		tempPolygon=null;
		return true;
	}
	return super.onDoubleTap(event);
}



}
