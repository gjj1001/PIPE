package cn.com.casit.tools;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import cn.com.casit.MainActivity;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;

public class MyArcGisQuery {
    
	MainActivity gYGisAndroidActivity;
    GraphicsLayer graphicsLayer;
    Polygon polygon;
    GraphicsLayer  gl;
    public FeatureSet run(Polygon polygon,GraphicsLayer graphicsLayer,MainActivity gYGisAndroidActivity) {
	this.gYGisAndroidActivity=gYGisAndroidActivity;
	this.graphicsLayer=graphicsLayer;
	this.polygon=polygon;
	String url = "http://192.168.1.23:6080/arcgis/rest/services/zilaishui/MapServer";
	String targetLayer = url.concat("/7");
	String[] queryParams = { targetLayer, "OBJECTID>1" };
	AsyncQueryTask ayncQuery = new AsyncQueryTask();
	ayncQuery.execute(queryParams);
	
	 gl = new GraphicsLayer();
	SimpleRenderer sr = new SimpleRenderer(new SimpleFillSymbol(Color.RED));
	gl.setRenderer(sr);
	gYGisAndroidActivity.mMapView.addLayer(gl);		
		
	return null;

	}
    

/**
 * 
 * Query Task executes asynchronously.
 * 
 */
ProgressDialog progress;
public class AsyncQueryTask extends AsyncTask<String, Void, FeatureSet> {

	protected void onPreExecute() {
	    progress = ProgressDialog.show(gYGisAndroidActivity, "","正在执行框选查询...");
	}

	protected FeatureSet doInBackground(String... queryParams) {
		if (queryParams == null || queryParams.length <= 1)
			return null;
		
		String url = queryParams[0];
		Query query = new Query();
		//String whereClause = queryParams[1];
		SpatialReference sr = SpatialReference.create(102113);//这个一定要设置正确,不然不会渲染
		query.setGeometry(polygon);
		query.setOutSpatialReference(sr);
		query.setReturnGeometry(true);
		//query.setWhere(whereClause);
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
	public GisRenderSymbol gisRenderSymbol =new GisRenderSymbol();
	protected void onPostExecute(FeatureSet result) {
		
		String message = "没有结果返回";
		if (result != null) {
			Graphic[] grs = result.getGraphics();
			
			if (grs.length > 0) {
			 
			    SimpleFillSymbol symbol = new SimpleFillSymbol(Color.RED);
		            symbol.setAlpha(99);			
		            graphicsLayer.setRenderer(new SimpleRenderer(symbol));
		            graphicsLayer.removeAll();//移除以前的
		            graphicsLayer.addGraphics(grs);
			    message = (grs.length == 1 ? "1 result has " : Integer
						.toString(grs.length) + " results have ")
						+ "come back";			    
			    
			    //可以进一步弹出一个对话框来显示内容
			   // gYGisAndroidActivity. tanchu_kuangxuan();
			}

		}
		progress.dismiss();
		Toast toast = Toast.makeText(gYGisAndroidActivity, message,Toast.LENGTH_LONG);
		toast.show();
	

	}

}

}

