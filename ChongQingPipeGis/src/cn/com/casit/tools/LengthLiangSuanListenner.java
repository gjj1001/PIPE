package cn.com.casit.tools;

import java.util.ArrayList;

import android.app.AlertDialog;
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

public class LengthLiangSuanListenner extends MapOnTouchListener{

    
    private Geometry.Type geoType = Geometry.Type.Polyline;//�����ж���ǰѡ��ļ���ͼ������   
    private Point ptStart = null;//���   
    private Point ptPrevious = null;//��һ����   
    private ArrayList<Point> points = null;//��¼ȫ����   
    private Polygon tempPolygon = null;//��¼���ƹ����еĶ����   
    MapView map ;
    public GraphicsLayer drawLayer;//����ͼ�ε�ͼ��
    public  Boolean bIsMeasureLength=true;
	public GisRenderSymbol gisRenderSymbol =new GisRenderSymbol();
	MainActivity gYGisAndroidActivity;
	
	
    public LengthLiangSuanListenner(Context context, MapView mapview) {
	super(context, mapview);
	gYGisAndroidActivity=(MainActivity)context;
	this.map=mapview;
	this.drawLayer=gYGisAndroidActivity.draw_graphicsLayer;
	points = new ArrayList<Point>();  
    }

 // �����û�ѡ�����õ�ǰ���Ƶļ���ͼ������   
    public void setType(String geometryType) {  
        if(geometryType.equalsIgnoreCase("Point"))  
            this.geoType = Geometry.Type.Point;  
        else if(geometryType.equalsIgnoreCase("Polyline"))  
            this.geoType = Geometry.Type.Polyline;  
        else if(geometryType.equalsIgnoreCase("Polygon"))  
            this.geoType = Geometry.Type.Polygon;  
    }  
      
    public Geometry.Type getType() {  
        return this.geoType;  
    }  
      
    @Override  
    public boolean onSingleTap(MotionEvent point) {  
        Point ptCurrent = map.toMapPoint(new Point(point.getX(), point.getY()));  
        if(ptStart == null) drawLayer.removeAll();//��һ�ο�ʼǰ�����ȫ��graphic   
          
        if (geoType == Geometry.Type.Point) {//ֱ�ӻ���                
            drawLayer.removeAll();  
            ptStart = ptCurrent;  
      
            Graphic graphic = new Graphic(ptStart,gisRenderSymbol.markerSymbol);  
            drawLayer.addGraphic(graphic);  
              
          //  btnClear.setEnabled(true);  
            return true;  
        }  
        else//�����߻�����   
        {  
            points.add(ptCurrent);//����ǰ�����㼯����   
             
           if(ptStart == null){//���߻����εĵ�һ����   
                ptStart = ptCurrent;  
                                      
               //���Ƶ�һ����   
               Graphic graphic = new Graphic(ptStart,gisRenderSymbol.markerSymbol);  
               drawLayer.addGraphic(graphic);                                    
           }  
            else{//���߻����ε�������   
                //����������   
               Graphic graphic = new Graphic(ptCurrent,gisRenderSymbol.markerSymbol);  
                drawLayer.addGraphic(graphic);  
                  
                //���ɵ�ǰ�߶Σ��ɵ�ǰ�����һ���㹹�ɣ�   
                Line line = new Line();  
                line.setStart(ptPrevious);  
                line.setEnd(ptCurrent);  
                  
                if(geoType == Geometry.Type.Polyline){  
                    //���Ƶ�ǰ�߶�   
                    Polyline polyline = new Polyline();  
                    polyline.addSegment(line, true);  
                      
                    Graphic g = new Graphic(polyline, gisRenderSymbol.lineSymbol);  
                    drawLayer.addGraphic(g);  
                      
                    // ���㵱ǰ�߶εĳ���   
                //   String length = Double.toString(Math.round(line.calculateLength2D())) + " ��";  
                      
                    Toast.makeText(map.getContext(), line.calculateLength2D()*100000+"", Toast.LENGTH_SHORT).show();  
                }  
                else{  
                    //������ʱ�����   
                    if(tempPolygon == null) tempPolygon = new Polygon();  
                    tempPolygon.addSegment(line, false);  
                      
                    drawLayer.removeAll();  
                    Graphic g = new Graphic(tempPolygon, gisRenderSymbol.fillSymbol);  
                    drawLayer.addGraphic(g);  
                      
                    //���㵱ǰ���   
                    String sArea = getAreaString(tempPolygon.calculateArea2D());  
                      
                    Toast.makeText(map.getContext(), sArea, Toast.LENGTH_SHORT).show();  
                }  
            }  
              
            ptPrevious = ptCurrent;  
            return true;  
        }  
    }  
      
    @Override  
    public boolean onDoubleTap(MotionEvent point) {  
       drawLayer.removeAll();  
    	points.add(map.toMapPoint(point.getX(), point.getY()));
        if(bIsMeasureLength == true){  
            Polyline polyline = new Polyline();  
      
            Point startPoint = null;  
            Point endPoint = null;  
              
            // �����������߶�   
            for(int i=1;i<points.size();i++){  
               startPoint = points.get(i-1);  
                endPoint = points.get(i);  
                  
                Line line = new Line();  
                line.setStart(startPoint);  
                line.setEnd(endPoint);  
      
                polyline.addSegment(line, false);  
           }  
             
           Graphic g = new Graphic(polyline, gisRenderSymbol.lineSymbol);  
            drawLayer.addGraphic(g);  
              
            // �����ܳ���   
            //String length = Double.toString(Math.round(polyline.calculateLength2D())) + " ��";  
            new AlertDialog.Builder(gYGisAndroidActivity).setTitle("��ǰ·������Ϊ:") .setMessage(polyline.calculateLength2D()*100000+"��") .setPositiveButton("ȷ��", null) .show();  
          //  Toast.makeText(map.getContext(), polyline.calculateLength2D()*100000+"", Toast.LENGTH_SHORT).show();  
        }  
        else{  
            Polygon polygon = new Polygon();  
      
            Point startPoint = null;  
            Point endPoint = null;  
            // ���������Ķ����   
            for(int i=1;i<points.size();i++){  
                startPoint = points.get(i-1);  
                endPoint = points.get(i);  
                  
                Line line = new Line();  
                line.setStart(startPoint);  
                line.setEnd(endPoint);  
      
                polygon.addSegment(line, false);  
            }  
              
            Graphic g = new Graphic(polygon, gisRenderSymbol.fillSymbol);  
            drawLayer.addGraphic(g);  
                      
            // ���������   
            String sArea = getAreaString(polygon.calculateArea2D());  
             
            Toast.makeText(map.getContext(), sArea, Toast.LENGTH_SHORT).show();  
        }  
          
        // ����������   
      //  btnClear.setEnabled(true);  
        ptStart = null;  
        ptPrevious = null;  
        points.clear();  
        tempPolygon = null;  
          
        return false;  
    }     
      
    private String getAreaString(double dValue){  
        long area = (long) (dValue*10000);  
        String sArea = "";  
        // ˳ʱ����ƶ���Σ����Ϊ������ʱ����ƣ������Ϊ��   
        if(area >= 1000000){                   
          double dArea = area / 1000000.0;  
           sArea = Double.toString(dArea) + " ƽ������";  
      }  
       else  
          sArea = Double.toString(area) + " ƽ����";  
        
        return sArea;  
   }  

}
