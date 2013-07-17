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

    
    private Geometry.Type geoType = Geometry.Type.Polyline;//用于判定当前选择的几何图形类型   
    private Point ptStart = null;//起点   
    private Point ptPrevious = null;//上一个点   
    private ArrayList<Point> points = null;//记录全部点   
    private Polygon tempPolygon = null;//记录绘制过程中的多边形   
    MapView map ;
    public GraphicsLayer drawLayer;//绘制图形的图层
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

 // 根据用户选择设置当前绘制的几何图形类型   
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
        if(ptStart == null) drawLayer.removeAll();//第一次开始前，清空全部graphic   
          
        if (geoType == Geometry.Type.Point) {//直接画点                
            drawLayer.removeAll();  
            ptStart = ptCurrent;  
      
            Graphic graphic = new Graphic(ptStart,gisRenderSymbol.markerSymbol);  
            drawLayer.addGraphic(graphic);  
              
          //  btnClear.setEnabled(true);  
            return true;  
        }  
        else//绘制线或多边形   
        {  
            points.add(ptCurrent);//将当前点加入点集合中   
             
           if(ptStart == null){//画线或多边形的第一个点   
                ptStart = ptCurrent;  
                                      
               //绘制第一个点   
               Graphic graphic = new Graphic(ptStart,gisRenderSymbol.markerSymbol);  
               drawLayer.addGraphic(graphic);                                    
           }  
            else{//画线或多边形的其他点   
                //绘制其他点   
               Graphic graphic = new Graphic(ptCurrent,gisRenderSymbol.markerSymbol);  
                drawLayer.addGraphic(graphic);  
                  
                //生成当前线段（由当前点和上一个点构成）   
                Line line = new Line();  
                line.setStart(ptPrevious);  
                line.setEnd(ptCurrent);  
                  
                if(geoType == Geometry.Type.Polyline){  
                    //绘制当前线段   
                    Polyline polyline = new Polyline();  
                    polyline.addSegment(line, true);  
                      
                    Graphic g = new Graphic(polyline, gisRenderSymbol.lineSymbol);  
                    drawLayer.addGraphic(g);  
                      
                    // 计算当前线段的长度   
                //   String length = Double.toString(Math.round(line.calculateLength2D())) + " 米";  
                      
                    Toast.makeText(map.getContext(), line.calculateLength2D()*100000+"", Toast.LENGTH_SHORT).show();  
                }  
                else{  
                    //绘制临时多边形   
                    if(tempPolygon == null) tempPolygon = new Polygon();  
                    tempPolygon.addSegment(line, false);  
                      
                    drawLayer.removeAll();  
                    Graphic g = new Graphic(tempPolygon, gisRenderSymbol.fillSymbol);  
                    drawLayer.addGraphic(g);  
                      
                    //计算当前面积   
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
              
            // 绘制完整的线段   
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
              
            // 计算总长度   
            //String length = Double.toString(Math.round(polyline.calculateLength2D())) + " 米";  
            new AlertDialog.Builder(gYGisAndroidActivity).setTitle("当前路径长度为:") .setMessage(polyline.calculateLength2D()*100000+"米") .setPositiveButton("确定", null) .show();  
          //  Toast.makeText(map.getContext(), polyline.calculateLength2D()*100000+"", Toast.LENGTH_SHORT).show();  
        }  
        else{  
            Polygon polygon = new Polygon();  
      
            Point startPoint = null;  
            Point endPoint = null;  
            // 绘制完整的多边形   
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
                      
            // 计算总面积   
            String sArea = getAreaString(polygon.calculateArea2D());  
             
            Toast.makeText(map.getContext(), sArea, Toast.LENGTH_SHORT).show();  
        }  
          
        // 其他清理工作   
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
        // 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负   
        if(area >= 1000000){                   
          double dArea = area / 1000000.0;  
           sArea = Double.toString(dArea) + " 平方公里";  
      }  
       else  
          sArea = Double.toString(area) + " 平方米";  
        
        return sArea;  
   }  

}
