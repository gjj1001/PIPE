package cn.com.casit.tools;

import android.graphics.Color;

import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

public class GisRenderSymbol {
    
    
	public MarkerSymbol markerSymbol;
	public LineSymbol lineSymbol;
	public FillSymbol fillSymbol;
	
	public  GisRenderSymbol(){
	    
	  //点线面的样式	
		this.markerSymbol = new SimpleMarkerSymbol(Color.RED, 16,SimpleMarkerSymbol.STYLE.CIRCLE);
		this.lineSymbol = new SimpleLineSymbol(Color.RED, 2); 		
		this.fillSymbol = new SimpleFillSymbol(Color.RED); 		
		this.fillSymbol.setAlpha(90);
	    
	}

}
