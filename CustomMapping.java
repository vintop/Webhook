//$Id$
package com.zoho.abtest.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.adventnet.iam.security.SecurityUtil;
import com.adventnet.mfw.bean.BeanUtil;
import com.zoho.abtest.cdn.ZABCDN;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.listener.ZABNotifier;
import com.zoho.abtest.trigger.Trigger;
import com.zoho.abtest.utility.ApplicationProperty;
import com.zoho.abtest.utility.ListenerXMLErrorHandler;
import com.zoho.abtest.webhook.Webhook;

public class CustomMapping {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(CustomMapping.class.getName());

	private static final  HashMap<String,String> TRIGGERTYPE=new HashMap();
	public static HashMap<String, String> getTriggertype() {
		return TRIGGERTYPE;
	}



	public static HashMap<String, ArrayList<Webhook>> getTrigger() {
		return TRIGGER;
	}



	public static HashMap<String, String> getAuthtype() {
		return AUTHTYPE;
	}



	public static HashMap<String, Webhook> getWebhook() {
		return WEBHOOK;
	}



	private static final  HashMap<String,ArrayList<Webhook>> TRIGGER=new HashMap();




	private static final HashMap<String,String> AUTHTYPE=new HashMap();
	private static final HashMap<String,Webhook> WEBHOOK=new HashMap();
	
	public Webhook getWebhookByName(String webhookname){
		return getWebhook().get(webhookname);
	}
	
	public ArrayList<Webhook> getAllWebhooks(String triggertype){
		return getTrigger().get(triggertype);
	}

	// triggers defined in Integration.xml 
	public static void registerTriggers() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		try {
			LOGGER.log(Level.INFO,"******triggertypes start");
			ZABCDN cdn = (ZABCDN)BeanUtil.lookup("ZABCDN");
			cdn.init();
			LOGGER.log(Level.INFO,"**** Flad to start consumer is {0}", ApplicationProperty.getBoolean(ZABConstants.START_MQUEUE_CONSUMER));
				//Read configs and start kafka consumers
				String url = CustomMapping.class.getResource("/../conf/trigger.xml").getPath(); //No I18N
				File file = new File(url);
			    DocumentBuilder documentBuilder = SecurityUtil.getDocumentBuilder();
			    documentBuilder.setErrorHandler(new ListenerXMLErrorHandler());
				Document document = documentBuilder.parse(file);
				
				if (document.getDocumentElement().getElementsByTagName("mapping").getLength()<1) {
					LOGGER.log(Level.INFO,"********** No triggers defined ************");
					return;
				}

				NodeList nodes = document.getElementsByTagName("mapping");
				
				for (int i = 0; i < nodes.getLength(); i++) {
					if(nodes.item(i).getNodeType()==Document.ELEMENT_NODE) {
								TRIGGERTYPE.put(nodes.item(i).getAttributes().getNamedItem("trigger_type").getNodeValue(),nodes.item(i).getAttributes().getNamedItem("class").getNodeValue()); // No I18N

						}}
					}
			 catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
			LOGGER.log(Level.SEVERE, "TERMINATING SERVER as there is an exception registering triggers",e);
		}
		
	}
	
	
	
	public static void registerAuths() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		try {
			LOGGER.log(Level.INFO,"******authentication types start");
			ZABCDN cdn = (ZABCDN)BeanUtil.lookup("ZABCDN");
			cdn.init();
			LOGGER.log(Level.INFO,"**** Flad to start consumer is {0}", ApplicationProperty.getBoolean(ZABConstants.START_MQUEUE_CONSUMER));
				//Read configs and start kafka consumers
				String url = CustomMapping.class.getResource("/../conf/authentication.xml").getPath(); //No I18N
				File file = new File(url);
			    DocumentBuilder documentBuilder = SecurityUtil.getDocumentBuilder();
			    documentBuilder.setErrorHandler(new ListenerXMLErrorHandler());
				Document document = documentBuilder.parse(file);
				
				
				if (document.getDocumentElement().getElementsByTagName("mapping").getLength()<1) {
					LOGGER.log(Level.INFO,"********** No auths defined ************");
					return;
				}

				NodeList types = document.getElementsByTagName("mapping");
				
				for (int i = 0; i < types.getLength(); i++) 
				{
					if(types.item(i).getNodeType()==Document.ELEMENT_NODE) {
								AUTHTYPE.put(types.item(i).getAttributes().getNamedItem("auth_type").getNodeValue(),types.item(i).getAttributes().getNamedItem("class").getNodeValue()); // No I18N

						}}
					}
			 catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
			LOGGER.log(Level.SEVERE, "TERMINATING SERVER as there is an exception registering authentication",e);
		}
		
	}
	
	
	
	// Webhooks defined in Integration.xml 
	public static void registerWebhooks() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		try {
			LOGGER.log(Level.INFO,"**** Flad to start consumer is {0}", ApplicationProperty.getBoolean(ZABConstants.START_MQUEUE_CONSUMER));
				//Read configs and start kafka consumers
       		String url = CustomMapping.class.getResource("/../conf/integration.xml").getPath(); //No I18N
				File file = new File(url);
			    DocumentBuilder documentBuilder = SecurityUtil.getDocumentBuilder();
			    documentBuilder.setErrorHandler(new ListenerXMLErrorHandler());
				Document document = documentBuilder.parse(file);
				
				
				if (document.getDocumentElement().getElementsByTagName("webhook").getLength()<1) {
					LOGGER.log(Level.INFO,"********** No webhooks defined ************");
					return;
				}

				NodeList webhookNodes = document.getElementsByTagName("webhook");
				
				for (int i = 0; i < webhookNodes.getLength(); i++) {
					String webhookname=null;
					String s=null;
					ZABNotifier notifier = new ZABNotifier();
					int len=0;
					Node node = webhookNodes.item(i);
					NodeList listenerNodeList=node.getChildNodes();
				    JSONArray jsonarray=new JSONArray();
					for(int j=0;j<listenerNodeList.getLength();j++) {
						Node listenerNode=listenerNodeList.item(j);
						if(listenerNode.getNodeType()==Document.ELEMENT_NODE) {
							
							if(listenerNode.getNodeName().equals("webhook_config"))
							   {
								NodeList child=listenerNode.getChildNodes();
								webhookname=listenerNode.getAttributes().getNamedItem("name").getNodeValue();// No I18N

								if(!WEBHOOK.containsKey(webhookname)){
								WEBHOOK.put(webhookname,new Webhook());
								}
								WEBHOOK.get(webhookname).setWebhookLinkName(webhookname);
								
								for(int k=0;k<child.getLength();k++){		

								if(child.item(k).getNodeName().equals("body")){
								WEBHOOK.get(webhookname).setWebhookBody(child.item(k).getTextContent()); }
								if(child.item(k).getNodeName().equals("url")){
								WEBHOOK.get(webhookname).setWebhookUrl(child.item(k).getTextContent());}
								if(child.item(k).getNodeName().equals("auth_type")){
									 s=child.item(k).getTextContent().trim();
								WEBHOOK.get(webhookname).setAuthType(Integer.parseInt(s));  } 
								if(child.item(k).getNodeName().equals("method")){
								WEBHOOK.get(webhookname).setMethod(s);  } 
								}
								
								}

							if(listenerNode.getNodeName().equals("headers")){
								NodeList headerslist=listenerNode.getChildNodes();
								for(int k=0;k<headerslist.getLength();k++) {
									Node headerNode=headerslist.item(k);
									  if(headerNode.getNodeType()==Document.ELEMENT_NODE) 
									       {
										JSONObject json=new JSONObject();
										json.put(headerNode.getAttributes().getNamedItem("key").getTextContent()	, headerNode.getAttributes().getNamedItem("value").getTextContent());
										jsonarray.put(len,json);
										len++;
								         	}         		
								}
								WEBHOOK.get(webhookname).setHeaders(jsonarray.toString());
							}
							if(listenerNode.getNodeName().equals("oauth") && s.equals("2") )
							    {
								NodeList child=listenerNode.getChildNodes();
								for(int k=0;k<child.getLength();k++){
									if(child.item(k).getNodeName().equals("authorization_url")){
								WEBHOOK.get(webhookname).setAuthorizationUrl(child.item(k).getTextContent()); }
								if(child.item(k).getNodeName().equals("access_token_url")){
								WEBHOOK.get(webhookname).setAccessTokenUrl(child.item(k).getTextContent());}
								if(child.item(k).getNodeName().equals("callback_url")){
								WEBHOOK.get(webhookname).setCallBackUrl(child.item(k).getTextContent());	}		
								
                		
								}}
							
							if(listenerNode.getNodeName().equals("trigger_config"))
						    {
							String type=listenerNode.getAttributes().getNamedItem("type").getTextContent(); // No I18N
							if(!TRIGGER.containsKey(type)){
								TRIGGER.put(type, new ArrayList<Webhook>());						
								}            
							TRIGGER.get(type).add(WEBHOOK.get(webhookname));
						    }
							
							}
						
						}
						}
					}
			 catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
			LOGGER.log(Level.SEVERE, "TERMINATING SERVER as there is an exception registering webhooks",e);
		}
		
	




	
	}


}
