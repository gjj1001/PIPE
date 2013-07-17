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
                //���캯��    
                public HttpThread2(Handler hander,MainActivity loginActivity){     
                        handle=hander;     
                        this.mainActivity=loginActivity;
                }   
                /**    
                 * �����߳�    
                 */   
                public void doStart(String url, String nameSpace, String methodName,HashMap<String, Object> params) {    
                        this.url=url; 
                        this.nameSpace=nameSpace;  
                        this.methodName=methodName; 
                        this.params=params;     
                        progressDialog=ProgressDialog.show(mainActivity, "��ʾ","�����������Եȡ���", false,true);     
                        this.start();     
                     }     

                /**    
                 * �߳�����   
                 */   
             @Override   
             public void run() { 
                     super.run(); 
                     try{
                     //web service����
                	 Object result=CallWebService();                            
                             //��������    
                             ArrayList<String> list=null;
                             if(result !=null ){ 
                                     list=new ArrayList<String>();  
                                     list.add(result.toString());  
                                     //aȡ�����ȶԻ���   
                                     progressDialog.dismiss();
                                     //������Ϣ     
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
              * ����web service  
              */   
             protected Object CallWebService(){     
        	
                     //����SoapObjectʵ��     
                     SoapObject request=new SoapObject(nameSpace,methodName);   
                     
                  // ����
                     if(this.params != null && !this.params.isEmpty() ){
                     for(Iterator it=this.params.entrySet().iterator();it.hasNext();){
                     Map.Entry e=(Entry) it.next();
                     request.addProperty(e.getKey().toString(),e.getValue());

                     }}

                     //���ɵ���web service������soap������Ϣ  
                     SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);     
                     envelope.bodyOut = request;
                     //��������     
                     HttpTransportSE androidHttpTrandsport=new HttpTransportSE(url);                     
                     Object result=null; 
                     try{   
                            //web service����    
                            androidHttpTrandsport.call(null, envelope); 
                            //�õ����ؽ��  
                            result=(Object) envelope.getResponse(); 
                    }catch(Exception ex){                 	 
                            ex.printStackTrace(); 
                    }                     
                    return result;  
            }    
      }     
