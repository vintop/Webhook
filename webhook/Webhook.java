//$Id$
package com.zoho.abtest.webhook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.Join;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.UpdateQuery;
import com.adventnet.ds.query.UpdateQueryImpl;
import com.adventnet.iam.IAMUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;
import com.zoho.abtest.EXPERIMENT;
import com.zoho.abtest.OAUTH;
import com.zoho.abtest.PROJECT;
import com.zoho.abtest.PROJECT_IPFILTER;
import com.zoho.abtest.PROJECT_USER_ROLE;
import com.zoho.abtest.ROLES;
import com.zoho.abtest.WEBHOOK;
import com.zoho.abtest.adminconsole.AdminConsoleConstants;
import com.zoho.abtest.adminconsole.AdminConsoleWrapper;
import com.zoho.abtest.adminconsole.AdminConsoleConstants.AcOperationType;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.elastic.ElasticSearchUtil;
import com.zoho.abtest.eventactivity.EventActivityConstants;
import com.zoho.abtest.eventactivity.EventActivityWrapper;
import com.zoho.abtest.eventactivity.EventModuleDetail;
import com.zoho.abtest.eventactivity.EventActivityConstants.Module;
import com.zoho.abtest.exception.ResourceNotFoundException;
import com.zoho.abtest.exception.ZABException;
import com.zoho.abtest.listener.ZABNotifier;
import com.zoho.abtest.privacyconsent.Privacy;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.webhook.Webhook;
import com.zoho.abtest.webhook.WebhookConstants;
import com.zoho.abtest.webhook.WebhookConstants.AuthenticationType;
import com.zoho.abtest.user.ProjectUserRole;
import com.zoho.abtest.user.ZABUser;
import com.zoho.abtest.user.ZABUserConstants;
import com.zoho.abtest.user.RoleConstants.UserRoles;
import com.zoho.abtest.utility.ZABUtil;

public class Webhook extends ZABModel implements Cloneable {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(Webhook.class.getName());
	
	  public Object clone() throws CloneNotSupportedException 
{ 
           return super.clone(); 
}
    private String WebhookName;
      private String method;
    public String getMethod() {
		return method;
	}



	public void setMethod(String method) {
		this.method = method;
	}



	private boolean isSystem=true;
    public boolean isSystem() {
		return isSystem;
	}



	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}



	private String headers;
    public String getHeaders() {
		return headers;
	}



	public void setHeaders(String headers) {
		this.headers = headers;
	}



	private String callBackUrl;
    private String authorizationUrl;
    private String accessTokenUrl;
    public String getCallBackUrl() {
		return callBackUrl;
	}



	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}



	public String getAuthorizationUrl() {
		return authorizationUrl;
	}



	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}



	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}



	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}



	private Long WebhookProjectId;
    private int authType;
    public int getAuthType() {
		return authType;
	}



	public void setAuthType(int authType) {
		this.authType = authType;
	}



	private String WebhookProjectLinkName;
    public String getWebhookProjectLinkName() {
		return WebhookProjectLinkName;
	}



	public void setWebhookProjectLinkName(String webhookProjectLinkname) {
		WebhookProjectLinkName = webhookProjectLinkname;
	}



	public Long getWebhookProjectId() {
		return WebhookProjectId;
	}



	public void setWebhookProjectId(Long webhookProjectId) {
		WebhookProjectId = webhookProjectId;
	}



	private Long WebhookId;
    public String getWebhookName() {
		return WebhookName;
	}



	public void setWebhookName(String webhookName) {
		WebhookName = webhookName;
	}



	public Long getWebhookId() {
		return WebhookId;
	}



	public void setWebhookId(Long webhookId) {
		WebhookId = webhookId;
	}



	public String getWebhookLinkName() {
		return WebhookLinkName;
	}



	public void setWebhookLinkName(String webhookLinkName) {
		WebhookLinkName = webhookLinkName;
	}



	public String getWebhookUrl() {
		return WebhookUrl;
	}



	public void setWebhookUrl(String webhookUrl) {
		WebhookUrl = webhookUrl;
	}



	public String getWebhookBody() {
		return WebhookBody;
	}



	public void setWebhookBody(String webhookBody) {
		WebhookBody = webhookBody;
	}



	private String WebhookLinkName;
    private String WebhookUrl;
    private String WebhookBody;
	
    
    
    public static Long getWebhookId(String webhooklinkname) throws Exception{
    	Long id=null;
    	Criteria c=new Criteria(new Column(WEBHOOK.TABLE,WEBHOOK.WEBHOOK_LINK_NAME),webhooklinkname,QueryConstants.EQUAL,Boolean.FALSE);
    	DataObject dobj=getRow(WEBHOOK.TABLE,c);
        Iterator it=dobj.getRows(WEBHOOK.TABLE);
        if(it.hasNext()) {
		Row row = (Row)it.next();  
          id =(Long)row.get("ID"); 	
    }
        return id;
    }
 
    
    
    public static void auth(HashMap<String,String> hs){
    	try{
    	if(Integer.parseInt(hs.get(WebhookConstants.AUTHENTICATION_TYPE))==(AuthenticationType.OAUTH.getType())){
    		Row row = new Row (OAUTH.TABLE);
    		row.set(OAUTH.WEBHOOK_ID,(Long.parseLong(hs.get(WebhookConstants.WEBHOOK_ID))));
    		row.set(OAUTH.CALLBACK_URL, hs.get(WebhookConstants.CALLBACK_URL));
    		row.set(OAUTH.ACCESS_TOKEN_URL, hs.get(WebhookConstants.ACCESS_TOKEN_URL));
    	//	String refreshtoken=
    		row.set(OAUTH.AUTHORIZATION_URL,hs.get(WebhookConstants.AUTHORIZATION_URL));
    		DataObject d=new WritableDataObject();
			d.addRow(row);
			createResource(d);
			
			//add the row in database.

    	}
    	}
    	 catch(Exception ex){
    			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
    	    }
    	return;
    }
    
    
    public static void updateauth(HashMap<String,String> hs){
    	try{
    		Criteria c = new Criteria(new Column(OAUTH.TABLE, OAUTH.WEBHOOK_ID),Long.parseLong(hs.get(WebhookConstants.WEBHOOK_ID)), QueryConstants.EQUAL);
    		deleteResource(c);
    		if(Integer.parseInt(hs.get(WebhookConstants.AUTHENTICATION_TYPE))==(AuthenticationType.OAUTH.getType())){
        		Row row = new Row (OAUTH.TABLE);
        		row.set(OAUTH.WEBHOOK_ID,Integer.parseInt(hs.get(WebhookConstants.WEBHOOK_ID)));
        		row.set(OAUTH.CALLBACK_URL, hs.get(WebhookConstants.CALLBACK_URL));
        		row.set(OAUTH.ACCESS_TOKEN_URL, hs.get(WebhookConstants.ACCESS_TOKEN_URL));
        	//	String refreshtoken=
        		row.set(OAUTH.AUTHORIZATION_URL,hs.get(WebhookConstants.AUTHORIZATION_URL));
        		DataObject d=new WritableDataObject();
    			d.addRow(row);
    			createResource(d);
    			
    			//add the row in database.

    	}
    	}
    	 catch(Exception ex){
    	
    			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
    	    }
    	return;
    }
    
    
    
	public static Webhook createWebhook(HashMap<String,String> hs) {
		Webhook webhook = null;
		try {
			String displayName = hs.get(WebhookConstants.WEBHOOK_NAME);
			String projectlinkname = hs.get(WebhookConstants.WEBHOOK_PROJECT_LINKNAME);
			displayName = displayName.trim();
			if(displayName.length() == 0 ){
				throw new ZABException(ZABAction.getMessage(WebhookConstants.WEBHOOK_EMPTY_NAME,null));
			}
			Criteria c = new Criteria(new Column(WEBHOOK.TABLE, WEBHOOK.NAME), displayName, QueryConstants.EQUAL, Boolean.FALSE);

			if(resourceExists(WEBHOOK.TABLE, c)) {
				throw new ZABException(ZABAction.getMessage(WebhookConstants.WEBHOOK_ALREADY_EXISTS,new String[]{displayName}));
			}
			
			Long projectId = Project.getProjectId(projectlinkname);
			// TO GET PROJECTC ID FROM PROJECT TABLE
			String linkname = generateLinkName(WEBHOOK.TABLE, "WEBHOOK_LINK_NAME", null, null, displayName);
			hs.put(ZABConstants.LINKNAME, linkname);
			hs.put(ZABUserConstants.CREATED_BY, ZABUtil.getCurrentUser().getUserId().toString());
			String time = ZABUtil.getCurrentTimeInMilliSeconds().toString();
			hs.put(ZABUserConstants.CREATED_TIME, time);
			hs.put(ZABUserConstants.MODIFIED_TIME, time);
			hs.put(WebhookConstants.WEBHOOK_PROJECT_ID,String.valueOf(projectId));
			Row row = new Row (WEBHOOK.TABLE);
			row.set(WEBHOOK.NAME, hs.get(WebhookConstants.WEBHOOK_NAME));
			row.set(WEBHOOK.URL, hs.get(WebhookConstants.WEBHOOK_URL));
			row.set(WEBHOOK.WEBHOOK_LINK_NAME, hs.get(WebhookConstants.WEBHOOK_LINK_NAME));
			row.set(WEBHOOK.BODY, hs.get(WebhookConstants.WEBHOOK_BODY));
			row.set(WEBHOOK.PROJECT_ID, projectId);
			int type = Integer.parseInt(hs.get(WebhookConstants.AUTHENTICATION_TYPE));
			row.set(WEBHOOK.AUTHENTICATION_TYPE, type);
			row.set(WEBHOOK.HEADERS,hs.get(WebhookConstants.HEADERS) );
			row.set(WEBHOOK.METHOD,hs.get(WebhookConstants.METHOD) );
			//to add the new row in the DataObject
			DataObject d=new WritableDataObject();
			d.addRow(row);
			createResource(d);
			
			//add the row in database.
			webhook= getWebhookFromRow(row);
			hs.put(WebhookConstants.WEBHOOK_ID,String.valueOf(webhook.getWebhookId()));
            auth(hs);
			webhook.setSuccess(Boolean.TRUE);
			
		}
		catch (ZABException e) {
			webhook.setSuccess(Boolean.FALSE);
			webhook.setResponseString(e.getMessage());
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		} catch (Exception e) {
			webhook.setSuccess(Boolean.FALSE);
			webhook.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return webhook;	
	}
	
	
	
	public static Webhook getWebhookFromRow(Row row){
		
		Webhook webhook = new Webhook();
		webhook.setSuccess(Boolean.TRUE);
		webhook.setWebhookName((String)row.get(WEBHOOK.NAME));
		webhook.setHeaders((String)row.get(WEBHOOK.HEADERS));
		webhook.setMethod((String)row.get(WEBHOOK.METHOD));
		webhook.setWebhookId((Long)(row.get(WEBHOOK.ID)));
		webhook.setWebhookBody((String)row.get(WEBHOOK.BODY));
		webhook.setWebhookUrl((String)row.get(WEBHOOK.URL));
		webhook.setWebhookLinkName((String)row.get(WEBHOOK.WEBHOOK_LINK_NAME));
		webhook.setWebhookProjectId((Long)row.get(WEBHOOK.PROJECT_ID));
		webhook.setAuthType((Integer.parseInt(row.get(WEBHOOK.AUTHENTICATION_TYPE).toString())));
		return webhook;
	}
	
	
	public static Webhook getWebhookByLinkname(String linkName) {
		Webhook webhook = null;
		try {
			Criteria c = new Criteria(new Column(WEBHOOK.TABLE, WEBHOOK.WEBHOOK_LINK_NAME), linkName, QueryConstants.EQUAL);
			DataObject dobj = getRow(WEBHOOK.TABLE, c);
			if(dobj.containsTable(WEBHOOK.TABLE)) {
				Iterator<?> it = dobj.getRows(WEBHOOK.TABLE);
				if(it.hasNext()) {
					Row row = (Row)it.next();
					webhook= getWebhookFromRow(row);
					
				}
			}
			webhook.setSuccess(Boolean.TRUE);
		}catch (Exception e) {
			webhook.setSuccess(Boolean.FALSE);
			webhook.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
			return webhook;
		}
		
		return webhook;
	}
	
	public static Webhook getWebhookById(Long webhookid) {
		Webhook webhook = null;
		try {
			Criteria c = new Criteria(new Column(WEBHOOK.TABLE, WEBHOOK.ID), webhookid, QueryConstants.EQUAL);
			DataObject dobj = getRow(WEBHOOK.TABLE, c);
			if(dobj.containsTable(WEBHOOK.TABLE)) {
				Iterator<?> it = dobj.getRows(WEBHOOK.TABLE);
				if(it.hasNext()) {
					Row row = (Row)it.next();
					webhook= getWebhookFromRow(row);
					
				}
			}
			webhook.setSuccess(Boolean.TRUE);
		}catch (Exception e) {
			webhook.setSuccess(Boolean.FALSE);
			webhook.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
			return webhook;
		}
		
		return webhook;
	}
	
	
	
	
	public static void deleteWebhook(String linkname) {
		Webhook webhook = null;
		try {			
			webhook = getWebhookByLinkname(linkname);
			
			Criteria c = new Criteria(new Column(WEBHOOK.TABLE, WEBHOOK.WEBHOOK_LINK_NAME), linkname, QueryConstants.EQUAL);	
			deleteResource(c);
			webhook.setSuccess(Boolean.TRUE);
		} 
		catch (Exception e) {
			webhook.setSuccess(Boolean.FALSE);
			webhook.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return ;
	}
	
	
	
	
	
	public static Webhook updateWebhook(HashMap<String, String> hs) {
		Webhook webhook = null;
		try {
			String linkname = hs.get(ZABConstants.LINKNAME);

			Webhook oldweb = Webhook.getWebhookByLinkname(linkname);
			
			String displayName = hs.get(WebhookConstants.WEBHOOK_NAME);
			String projectlinkname = hs.get(WebhookConstants.WEBHOOK_PROJECT_LINKNAME);
			hs.put(ZABUserConstants.MODIFIED_TIME,  ZABUtil.getCurrentTimeInMilliSeconds().toString());
			Criteria c = new Criteria(new Column(WEBHOOK.TABLE, WEBHOOK.WEBHOOK_LINK_NAME), linkname, QueryConstants.EQUAL);
			
			Long projectId = Project.getProjectId(projectlinkname);
			
			UpdateQuery u = new UpdateQueryImpl(WEBHOOK.TABLE);
			u.setCriteria(c);
			u.setUpdateColumn(WEBHOOK.NAME, hs.get(WebhookConstants.WEBHOOK_NAME));
			u.setUpdateColumn(WEBHOOK.PROJECT_ID, projectId);
			u.setUpdateColumn(WEBHOOK.HEADERS,hs.get(WebhookConstants.HEADERS));
			u.setUpdateColumn(WEBHOOK.METHOD,hs.get(WebhookConstants.METHOD));
			u.setUpdateColumn(WEBHOOK.URL, hs.get(WebhookConstants.WEBHOOK_URL));
			u.setUpdateColumn(WEBHOOK.WEBHOOK_LINK_NAME, hs.get(WebhookConstants.WEBHOOK_LINK_NAME));
			u.setUpdateColumn(WEBHOOK.BODY, hs.get(WebhookConstants.WEBHOOK_BODY));
			u.setUpdateColumn(WEBHOOK.AUTHENTICATION_TYPE, (Integer.parseInt(hs.get(WebhookConstants.AUTHENTICATION_TYPE))));
			//update row
			ZABModel.updateRow(u);
			webhook = Webhook.getWebhookByLinkname(linkname);
//			JSONArray js=new JSONArray(hs.get("header"));
//			updateHeaders(js,webhook.getWebhookId());
//			//update headers table
			webhook.setWebhookProjectLinkName(hs.get(WebhookConstants.WEBHOOK_PROJECT_LINKNAME));
			
			webhook.setSuccess(Boolean.TRUE);
			webhook.setWebhookLinkName(linkname);
			webhook.setWebhookId(oldweb.getWebhookId());	
			hs.put(WebhookConstants.WEBHOOK_ID,String.valueOf(webhook.getWebhookId()));
            updateauth(hs);
		}  catch (Exception e) {
			webhook.setSuccess(Boolean.FALSE);
			webhook.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		
		}
		return webhook;
	}
	
	
	
	public static ArrayList<Webhook> getWebhooks(){
		LOGGER.log(Level.INFO,"  TRYING TO GET WEBHOOK INFORMATION");
		Webhook webhook=new Webhook();
		ArrayList<Webhook> al=new ArrayList();
		try{
			Criteria c=null;		
			DataObject dj=getRow(WEBHOOK.TABLE, c);
			Iterator it=dj.getRows(WEBHOOK.TABLE);
			while(it.hasNext())
			{
				Row row = (Row)it.next();
				webhook= getWebhookFromRow(row);
				
				al.add(webhook);
			}
			webhook.setSuccess(Boolean.TRUE);
		}
		catch (Exception e) {
			webhook.setSuccess(Boolean.FALSE);
			webhook.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return al;
	}
	
	
	
}
