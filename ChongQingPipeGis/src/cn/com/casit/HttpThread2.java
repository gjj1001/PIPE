package cn.com.casit;

import java.util.ArrayList;
import java.util.HashMap;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpThread2 extends Thread{     

                private Handler handle=null; 
                String url=null; 
                String nameSpace=null;  
                String methodName=null;   
                HashMap <String ,Object> params=null;  
                ProgressDialog progressDialog=null;     
                MainActivity mainActivity;
                //构造函数    
                public HttpThread2(Handler hander,MainActivity loginActivity){     
                        handle=hander;     
                        this.mainActivity=loginActivity;
                }   
                /**    
                 * 启动线程    
                 */   
                public void doStart(String url, String nameSpace, String methodName,HashMap<String, Object> params) {    
                        this.url=url; 
                        this.nameSpace=nameSpace;  
                        this.methodName=methodName; 
                        this.params=params;     
                        progressDialog=ProgressDialog.show(mainActivity, "提示","正在请求请稍等……", false,true);     
                        this.start();     
                     }     

                /**    
                 * 线程运行   
                 */   
             @Override   
             public void run() { 
                     super.run(); 
                     try{
                     //web service请求
                	 Object result=CallWebService();                            
                             //构造数据    
                             ArrayList<String> list=null;
                             if(result !=null ){ 
                                     list=new ArrayList<String>();  
                                     list.add(result.toString());  
                                     //a取消进度对话框   
                                     progressDialog.dismiss();
                                     //构造消息     
                                     Message message=handle.obtainMessage(); 
                                     Bundle b=new Bundle(); 
                                     b.putStringArrayList("data", list); 
                                     message.setData(b);     
                                     handle.sendMessage(message);    
                             }                               

                     }catch(Exception ex){    
                             ex.printStackTrace();  
                     }finally{    
                     }   
             }    
             /**    
              * 请求web service  
              */   
             protected Object CallWebService(){     
        	
                     //创建SoapObject实例     
                     SoapObject request=new SoapObject(nameSpace,methodName);   
                     
                  // 参数
                     if(this.params != null && !this.params.isEmpty() ){
                     for(Iterator it=this.params.entrySet().iterator();it.hasNext();){
                     Map.Entry e=(Entry) it.next();
                     request.addProperty(e.getKey().toString(),e.getValue());

                     }}

                     //生成调用web service方法的soap请求消息  
                     SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);     
                     envelope.bodyOut = request;
                     //发送请求     
                     HttpTransportSE androidHttpTrandsport=new HttpTransportSE(url);                     
                     Object result=null; 
                     try{   
                            //web service请求    
                            androidHttpTrandsport.call(null, envelope); 
                            //得到返回结果  
                            result=(Object) envelope.getResponse(); 
                    }catch(Exception ex){                 	 
                            ex.printStackTrace(); 
                    }                     
                    return result;  
            }    
      }     
