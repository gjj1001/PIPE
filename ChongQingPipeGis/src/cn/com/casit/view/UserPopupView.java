package cn.com.casit.view;

import cn.com.casit.MainActivity;
import cn.com.casit.R;

import com.esri.android.map.MapOnTouchListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
	
public class UserPopupView {
        private View view;
	//private Button btn;
	private PopupWindow mPopupWindow;

	public MainActivity gYAndroidGisActivity;
	public void initPopupWindow(int resId, final MainActivity gYAndroidGisActivity){
	        this.gYAndroidGisActivity=gYAndroidGisActivity;
		LayoutInflater mLayoutInflater = (LayoutInflater)gYAndroidGisActivity.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
	        view = mLayoutInflater.inflate(resId, null);        
		mPopupWindow = new PopupWindow(view, 330,LayoutParams.WRAP_CONTENT);
	        //mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//必须设置background才能消失
		mPopupWindow.setBackgroundDrawable(gYAndroidGisActivity.getResources().getDrawable(R.drawable.bg_frame));
		mPopupWindow.setOutsideTouchable(false);//点焦点外消失	
		//自定义动画
		//mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		//使用系统动画
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		
	
		  //////关闭框       
        ImageView  image_shijian = (ImageView) view.findViewById(R.id.image_shijian);
        image_shijian.setOnClickListener(new ImageView.OnClickListener() {        	    

      	   public void onClick(View v) {  
      		 mPopupWindow.dismiss();
      	   }  
      	  }); 
		

		
	}
	public void showPopupWindow() {
		if(!mPopupWindow.isShowing()){
		//	mPopupWindow.showAsDropDown(view,0,0);
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		}
	}

}
