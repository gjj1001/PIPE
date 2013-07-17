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
    private Polygon tempPolygon = null;//��¼���ƹ����еĶ����   
	MapView mMapView ;
	private Graphic drawGraphic;
	public GraphicsLayer graphicsLayer;//����ͼ�ε�ͼ��
	private Point startPoint;
	   private Point ptPrevious = null;//��һ����   
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
	
	Log.d("�ҵ�TAG", "onSingleTap������(���ڻ��߶κͶ����)"); 
	Point ptCurrent = mMapView.toMapPoint(event.getX(), event.getY());    				
	if ( (drawType==POLYGON || drawType==POLYLINE)) {
		switch (drawType) {
		case POLYGON:
			
			  
          //  points.add(ptCurrent);//����ǰ�����㼯����   
             
           if(startPoint == null){//���߻����εĵ�һ����   
        	   startPoint = ptCurrent;  
                                      
               //���Ƶ�һ����   
               Graphic graphic = new Graphic(startPoint,gisRenderSymbol.markerSymbol);  
               graphicsLayer.addGraphic(graphic);                                    
           }  
            else{//���߻����ε�������   
                //����������   
               Graphic graphic = new Graphic(ptCurrent,gisRenderSymbol.markerSymbol);  
               graphicsLayer.addGraphic(graphic);  
                  
                //���ɵ�ǰ�߶Σ��ɵ�ǰ�����һ���㹹�ɣ�   
                Line line = new Line();  
                line.setStart(ptPrevious);  
                line.setEnd(ptCurrent);  
                  
                
                    //������ʱ�����   
                    if(tempPolygon == null) tempPolygon = new Polygon();  
                    tempPolygon.addSegment(line, false);  
                      
                    graphicsLayer.removeAll();  
                    Graphic g = new Graphic(tempPolygon, gisRenderSymbol.fillSymbol);  
                    graphicsLayer.addGraphic(g);  
                      
                    //���㵱ǰ���   


            }  
              
            ptPrevious = ptCurrent;  
            
		}}
	 return true; 
}

public boolean onDoubleTap(MotionEvent event) {			
	
	if ((drawType==POLYGON || drawType==POLYLINE)) {

		 Log.d("�ҵ�TAG", "onDoubleTap������(˫��)");
		Point point = mMapView.toMapPoint(event.getX(), event.getY());
		switch (drawType) {						
		case POLYGON:
			tempPolygon.lineTo(point);  
			this.graphicsLayer.removeAll();
			drawGraphic=new Graphic(polygon,gisRenderSymbol.fillSymbol);			
			//��ʼ��ѡ��ѯ
			MyArcGisQuery myArcGisQuery=new MyArcGisQuery();			
			myArcGisQuery.run(tempPolygon, graphicsLayer,gYGisAndroidActivity);
		
			break;
		
		}
		//sendDrawEndEvent();
		//��ʼ��Ⱦ����ͼ��						
   	
		startPoint = null;
		tempPolygon=null;
		return true;
	}
	return super.onDoubleTap(event);
}



}
