//$Id$
package com.zoho.abtest.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.zoho.abtest.webhook.Webhook;
import com.zoho.abtest.common.ZABModel;



/**
 * @Base class for trigger
 *
 */
public abstract class  CustomTrigger extends ZABModel {

	private Object obj;
	
	private static final Logger LOGGER = Logger.getLogger(CustomTrigger.class.getName());
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	/**
	 * Please provide details for validation and variables
	 */
		
	//Add default implementation
	/**
	 * @param wrapper
	 * @return list of webhooks for a given trigger type
	 * @throws Exception
	 */
	public  ArrayList<Webhook>  getWebhooks(WebhookWrapper wrapper) throws Exception{
		 ArrayList<Webhook> al=new ArrayList();		
		try{
			String type=wrapper.getType().getCode().toString();
			if(CustomMapping.getTrigger().containsKey(type)){
				for(int i=0;i<CustomMapping.getTrigger().get(type).size();i++){
					Webhook webhook=new Webhook();
					webhook=(Webhook) CustomMapping.getTrigger().get(type).get(i).clone();
					al.add(webhook);
				}
			}
			else{
				al=null;
			}
		}
		catch(Exception e){
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return al;
	}
	/**
	 * @param object
	 * @return key and value pairs for different variables 
	 * @throws Exception
	 */
	public abstract  HashMap<String ,String> getVariables(WebhookWrapper object) throws Exception;
	
	
	public String trigger(WebhookWrapper wrapper){
		try{
			         HashMap<String,String> hs=null;
		             hs = getVariables(wrapper);
		             ArrayList<Webhook> al=getWebhooks(wrapper);
			         if(al!=null){
                     for(int i=0;i<al.size();i++)
                     {
        				 String customclass=CustomMapping.getAuthtype().get(String.valueOf(al.get(i).getAuthType()));
        				 Class cls = Class.forName(customclass);
      				     Object obj = cls.newInstance();
      				     if(((CustomAuth) obj).authentication(al.get(i))!=null)
      				      {
      				    	 al.set(i, ((CustomAuth) obj).authentication(al.get(i)));
      				      }
                        WebhookBody.call(hs, al.get(i));

		              }
                     
             }
		}
		catch(Exception e){
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return null;
	}
	

}
