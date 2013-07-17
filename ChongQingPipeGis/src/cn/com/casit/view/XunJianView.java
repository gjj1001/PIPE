package cn.com.casit.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.com.casit.MainActivity;
import cn.com.casit.R;
import cn.com.casit.TableView;

public class XunJianView {
	
	 TableView listView;//自定义表格页面
	 List<String[]> list = new ArrayList<String[]>();//自定义表格的数据
	 LinearLayout xunjian_popwindow;//巡检窗口
	 public MainActivity mainActivity;
	public XunJianView(MainActivity mainActivity1){
		
		
		this.mainActivity=mainActivity1;
		LinearLayout layout  =(LinearLayout)LayoutInflater.from(mainActivity).inflate(R.layout.xunjian_layout, null);
		xunjian_popwindow=(LinearLayout)layout.findViewById(R.id.xunjian_popwindow);
		makeData();
		String[] title = new String[] { "序号","名称", "巡检规则", "最近巡检", "备注" };
  		listView = new TableView(mainActivity, title, list);
  		listView.setTitleTextColor(Color.WHITE);
  		listView.setTitleBackgroundColor(Color.GRAY);
  		// listView.setAutoColumnWidth(3);
  		listView.setItemBackgroundColor(Color.argb(200, 235, 248, 255), Color.WHITE);
  		
  		xunjian_popwindow.addView(listView);
  		
  		mainActivity.linearLayout_mainwindows.addView(layout);	
  		
  	  //////关闭事件上报框       
  	    ImageView  image_shijian = (ImageView) layout.findViewById(R.id.image_xunjian);
  	    image_shijian.setOnClickListener(new ImageView.OnClickListener() {        	    

  	  	   public void onClick(View v) {  
  	  		 closethewindow();
  	  	   }  
  	  	  }); 
	}

	
    
	 //窗口关闭的函数
	 public  void closethewindow(){
		// draw_graphicsLayer.removeAll();
		  //移除地图长按监听
 		// mMapView.setOnLongPressListener(null);     		
 		 mainActivity.linearLayout_mainwindows.removeAllViews();
		 
	 }
	
	 private void makeData() {
			list.add(new String[] { "1", "数据1.2", "数据1.3", "数据1.4" });
			list.add(new String[] { "2", "数据2.2", "数据2.3", "数据2.4", "数据2.5" });
			list.add(new String[] { "3", "数据3.2", "数据3.3", "数据3.4" });
			list.add(new String[] { "4", "数据4.2", "数据4.3", "数据4.4" });
			list.add(new String[] { "5", "数据5.2", "数据5.3", "数据5.4", "数据5.5" });
			list.add(new String[] { "6", "数据6.2", "数据6.3", "数据6.4" });
			list.add(new String[] { "7", "数据7.2", "数据7.3", "数据7.4", "数据7.5",	"数据7.6" });
			list.add(new String[] { "8", "数据8.2", "数据8.3", "数据8.4" });
			list.add(new String[] { "9", "数据9.2", "数据9.3", "数据9.4" });
			list.add(new String[] { "10", "数据10.2", "数据10.3", "数据10.4", "数据10.5","数据10.6", "数据10.7" });
			list.add(new String[] { "11", "数据11.2", "数据11.3", "数据11.4", "数据11.5" });
			list.add(new String[] { "12", "数据12.2", "数据12.3", "数据12.4", "数据12.5" });
			list.add(new String[] { "13", "数据13.2", "数据13.3", "数据13.4", "数据13.5" });
			list.add(new String[] { "14", "数据14.2", "数据14.3", "数据14.4", "数据14.5" });
			list.add(new String[] { "15", "数据15.2", "数据15.3", "数据15.4", "数据15.5" });
			list.add(new String[] { "16", "数据16.2", "数据16.3", "数据16.4", "数据16.5" });
			list.add(new String[] { "17", "数据17.2", "数据17.3", "数据17.4", "数据17.5" });


		} 
	    
	 
	 
}
