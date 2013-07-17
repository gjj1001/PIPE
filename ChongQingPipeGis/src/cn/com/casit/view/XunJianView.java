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
	
	 TableView listView;//�Զ�����ҳ��
	 List<String[]> list = new ArrayList<String[]>();//�Զ����������
	 LinearLayout xunjian_popwindow;//Ѳ�촰��
	 public MainActivity mainActivity;
	public XunJianView(MainActivity mainActivity1){
		
		
		this.mainActivity=mainActivity1;
		LinearLayout layout  =(LinearLayout)LayoutInflater.from(mainActivity).inflate(R.layout.xunjian_layout, null);
		xunjian_popwindow=(LinearLayout)layout.findViewById(R.id.xunjian_popwindow);
		makeData();
		String[] title = new String[] { "���","����", "Ѳ�����", "���Ѳ��", "��ע" };
  		listView = new TableView(mainActivity, title, list);
  		listView.setTitleTextColor(Color.WHITE);
  		listView.setTitleBackgroundColor(Color.GRAY);
  		// listView.setAutoColumnWidth(3);
  		listView.setItemBackgroundColor(Color.argb(200, 235, 248, 255), Color.WHITE);
  		
  		xunjian_popwindow.addView(listView);
  		
  		mainActivity.linearLayout_mainwindows.addView(layout);	
  		
  	  //////�ر��¼��ϱ���       
  	    ImageView  image_shijian = (ImageView) layout.findViewById(R.id.image_xunjian);
  	    image_shijian.setOnClickListener(new ImageView.OnClickListener() {        	    

  	  	   public void onClick(View v) {  
  	  		 closethewindow();
  	  	   }  
  	  	  }); 
	}

	
    
	 //���ڹرյĺ���
	 public  void closethewindow(){
		// draw_graphicsLayer.removeAll();
		  //�Ƴ���ͼ��������
 		// mMapView.setOnLongPressListener(null);     		
 		 mainActivity.linearLayout_mainwindows.removeAllViews();
		 
	 }
	
	 private void makeData() {
			list.add(new String[] { "1", "����1.2", "����1.3", "����1.4" });
			list.add(new String[] { "2", "����2.2", "����2.3", "����2.4", "����2.5" });
			list.add(new String[] { "3", "����3.2", "����3.3", "����3.4" });
			list.add(new String[] { "4", "����4.2", "����4.3", "����4.4" });
			list.add(new String[] { "5", "����5.2", "����5.3", "����5.4", "����5.5" });
			list.add(new String[] { "6", "����6.2", "����6.3", "����6.4" });
			list.add(new String[] { "7", "����7.2", "����7.3", "����7.4", "����7.5",	"����7.6" });
			list.add(new String[] { "8", "����8.2", "����8.3", "����8.4" });
			list.add(new String[] { "9", "����9.2", "����9.3", "����9.4" });
			list.add(new String[] { "10", "����10.2", "����10.3", "����10.4", "����10.5","����10.6", "����10.7" });
			list.add(new String[] { "11", "����11.2", "����11.3", "����11.4", "����11.5" });
			list.add(new String[] { "12", "����12.2", "����12.3", "����12.4", "����12.5" });
			list.add(new String[] { "13", "����13.2", "����13.3", "����13.4", "����13.5" });
			list.add(new String[] { "14", "����14.2", "����14.3", "����14.4", "����14.5" });
			list.add(new String[] { "15", "����15.2", "����15.3", "����15.4", "����15.5" });
			list.add(new String[] { "16", "����16.2", "����16.3", "����16.4", "����16.5" });
			list.add(new String[] { "17", "����17.2", "����17.3", "����17.4", "����17.5" });


		} 
	    
	 
	 
}
