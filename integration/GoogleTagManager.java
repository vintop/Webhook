//$Id$
package com.zoho.abtest.integration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.iam.IAMUtil;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.zoho.abtest.GOOGLE_TAG_MANAGER_DETAILS;
import com.zoho.abtest.PROJECT_INTEGRATION;
import com.zoho.abtest.audience.AudienceConstants;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.exception.ResourceNotFoundException;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.utility.ApplicationProperty;
import com.zoho.abtest.utility.ZABUtil;

public class GoogleTagManager extends ZABModel {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(GoogleAdwords.class.getName());

	private Long projectId;
	private String emailId;
	private Long tagId;
	private Long accountId;
	private Long containerId;
	private Long workspaceId;
	private String accountName;
	private String containerName;
	private String workspaceName;
	
	public Long getProjectId() {
		return projectId;
	}
	
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public Long getTagId() {
		return tagId;
	}
	
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	
	public Long getAccountId() {
		return accountId;
	}
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	public Long getContainerId() {
		return containerId;
	}
	
	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}
	
	public Long getWorkSpaceId() {
		return workspaceId;
	}
	
	public void setWorkSpaceId(Long workspaceId) {
		this.workspaceId = workspaceId;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getWorkSpaceName() {
		return workspaceName;
	}
	
	public void setWorkSpaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}
	
	public String getContainerName() {
		return containerName;
	}
	
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	public static GoogleTagManager createGoogleTagManager(HashMap<String,String> hs) {
		// TODO Auto-generated method stub
		GoogleTagManager googletagmanager = new GoogleTagManager();
		try {
			
			String tagName = hs.get(IntegrationConstants.TAG_NAME);
			String projectId = hs.get(IntegrationConstants.PROJECT_ID);
			Project project = Project.getProjectByProjectId(Long.parseLong(projectId));
			String projectKey = project.getProjectKey();
			String tagValue = Project.getProjectAsyncJsSnippetEmbedCode(projectKey);
			
			JSONArray triggerId = new JSONArray();
			triggerId.put("2147479553");
			
			JSONObject ja = new JSONObject();
			ja.put("type", "template");
			ja.put("key", "html");
			ja.put("value", tagValue);
			JSONArray parameters = new JSONArray();
			parameters.put(ja);
	        
	        JSONObject tagDetails = new JSONObject();
	        tagDetails.put("tagName", tagName);
	        tagDetails.put("tagType", "html");
	        tagDetails.put("triggerIds", triggerId);
	        tagDetails.put("parameters", parameters);
	        
	        String accountId = hs.get(IntegrationConstants.ACCOUNT_ID);
	        String containerId = hs.get(IntegrationConstants.CONTAINER_ID);
	        String workspaceId = hs.get(IntegrationConstants.WORKSPACE_ID);
	        
	        String GadgetsURL = ApplicationProperty.getString("com.abtest.gtm.serverurl");
	        String url = GadgetsURL + "/api/google/v1/tagmanager/createtag";  // No I18N  
	        CloseableHttpClient c = HttpClientBuilder.create().useSystemProperties().build();
	        HttpResponse r=null;
	        HttpPost p = new HttpPost(url);  
	        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	        urlParameters.add(new BasicNameValuePair("tagDetails", tagDetails.toString())); // No I18N
	        urlParameters.add(new BasicNameValuePair("ticket", IAMUtil.getCurrentTicket())); //"1542802-75a2ce9c-a671959fe1e08784e998d96deaee4323" // No I18N
	        urlParameters.add(new BasicNameValuePair("zservice", "ZohoPageSense")); // No I18N
	        urlParameters.add(new BasicNameValuePair("response_type", "raw")); // No I18N
	        urlParameters.add(new BasicNameValuePair("accountId", accountId));  // "1263482130" // No I18N
	        urlParameters.add(new BasicNameValuePair("containerId", containerId)); // "7573955" // No I18N
	        urlParameters.add(new BasicNameValuePair("workspaceId", workspaceId));// "1"  // No I18N
	        
	        HttpEntity httpEntity = new UrlEncodedFormEntity(urlParameters);
	        p.setHeader("Accept", "application/json"); //No I18N 
	        p.addHeader("content-type", "application/x-www-form-urlencoded"); // No I18N
	        p.setEntity(httpEntity);    
	        r = c.execute(p);
	        BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));                
	        String line = null;
	        while ((line = rd.readLine()) != null) 
	        {
	            StringBuilder    stringBuilder = new StringBuilder();
	            stringBuilder.append(line); 
	            JSONObject jsonObj = new JSONObject(stringBuilder.toString());
	            JSONObject respObj = new JSONObject(jsonObj.getString("response"));  
	            String tagId = respObj.getString("tagId");
	            hs.put(IntegrationConstants.TAG_ID, tagId);
	            hs.put(IntegrationConstants.INTEGRATION_ID, "6"); //No I18N
	            hs.put(IntegrationConstants.PROJECT_ID, projectId);
	            createRow(IntegrationConstants.PROJECT_INTEGRATION_TABLE, PROJECT_INTEGRATION.TABLE, hs);
				createRow(IntegrationConstants.GOOGLE_TAG_MANAGER_TABLE, GOOGLE_TAG_MANAGER_DETAILS.TABLE, hs);
	        }
			rd.close();
			googletagmanager.setSuccess(Boolean.TRUE);
			
			
		} catch (Exception ex) {
			
			googletagmanager.setSuccess(Boolean.FALSE);
			googletagmanager.setResponseString(ex.getMessage());
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		}
		return googletagmanager;
	}
	public static ArrayList<GoogleTagManager> getGoogleTagManagerForAccounts(String emailId)
	{
		
		ArrayList<GoogleTagManager> gtms = new ArrayList<GoogleTagManager>();
		
		try{
			
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gtm.serverurl");
			emailId = URLEncoder.encode(emailId,"UTF-8"); //No I18N
			String GTMAccountsURL = GadgetsURL+"/api/google/v1/tagmanager/getaccountslist?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&response_type=gadgets&mailId="+emailId; //No I18N
			String responseJsonStr = ZABUtil.getResponseStrFromURL(GTMAccountsURL);
			JSONObject responseJson = new JSONObject(responseJsonStr);
			JSONObject respObj = new JSONObject(responseJson.getString("response"));  
			JSONArray acc = new JSONArray(respObj.getString("account")); 

			for(int i=0;i<acc.length();i++){
				
				GoogleTagManager gtm = new GoogleTagManager();
				JSONObject account = new JSONObject(acc.getString(i));
				Long accountId = Long.parseLong(account.getString("accountId"));
				String name = account.getString("name");
				
				gtm.setAccountId(accountId);
				gtm.setAccountName(name);
				
				gtms.add(gtm);
			}
			
	   }
	   catch(Exception e){
		   
		   GoogleTagManager gtm = new GoogleTagManager();
		   gtm=new GoogleTagManager();
		   gtm.setSuccess(Boolean.FALSE);
		   gtm.setResponseString(e.getMessage());
		   gtms.add(gtm);
           LOGGER.log(Level.SEVERE,e.getMessage(),e);
           
        }
		return gtms;
	}
	
	public static ArrayList<GoogleTagManager> getGoogleTagManagerForContainer(Long accountId)
	{
		
		ArrayList<GoogleTagManager> gtms = new ArrayList<GoogleTagManager>();
		
		try{
			
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gtm.serverurl");

			String GTMAccountsURL = GadgetsURL+"/api/google/v1/tagmanager/getcontainerslist?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&response_type=gadgets&accountId="+accountId; //No I18N
			String responseJsonStr = ZABUtil.getResponseStrFromURL(GTMAccountsURL);
			JSONObject responseJson = new JSONObject(responseJsonStr);
			JSONObject respObj = new JSONObject(responseJson.getString("response"));  
			JSONArray acc = new JSONArray(respObj.getString("container")); 

			for(int i=0;i<acc.length();i++){
				
				GoogleTagManager gtm = new GoogleTagManager();
				JSONObject account = new JSONObject(acc.getString(i));
				Long containerId = Long.parseLong(account.getString("containerId"));
				String name = account.getString("name");
				
				gtm.setContainerId(containerId);
				gtm.setContainerName(name);
				gtm.setSuccess(Boolean.TRUE);
				gtms.add(gtm);
			}
			
	   }
	   catch(Exception e){
		   
		   GoogleTagManager gtm = new GoogleTagManager();
		   gtm=new GoogleTagManager();
		   gtm.setSuccess(Boolean.FALSE);
		   gtm.setResponseString(e.getMessage());
		   gtms.add(gtm);
           LOGGER.log(Level.SEVERE,e.getMessage(),e);
           
        }
		return gtms;
	}

	public static ArrayList<GoogleTagManager> getGoogleTagManagerForWorkSpace(Long accountId, Long containerId, String emailId)
	{
		
		ArrayList<GoogleTagManager> gtms = new ArrayList<GoogleTagManager>();
		
		try{
			
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gtm.serverurl");
			
			emailId = URLEncoder.encode(emailId,"UTF-8"); //No I18N
			String GTMAccountsURL = GadgetsURL+"/api/google/v1/tagmanager/getworkspacelist?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&response_type=gadgets&mailId="+emailId+"&accountId="+accountId+"&containerId="+containerId; //No I18N
			String responseJsonStr = ZABUtil.getResponseStrFromURL(GTMAccountsURL);
			JSONObject responseJson = new JSONObject(responseJsonStr);
			JSONObject respObj = new JSONObject(responseJson.getString("response"));  
			JSONArray workspace = new JSONArray(respObj.getString("workspace")); 

			for(int i=0;i<workspace.length();i++){
				
				GoogleTagManager gtm = new GoogleTagManager();
				JSONObject account = new JSONObject(workspace.getString(i));
				Long workspaceId = Long.parseLong(account.getString("workspaceId"));
				String name = account.getString("name");
				
				gtm.setWorkSpaceId(workspaceId);
				gtm.setWorkSpaceName(name);
				
				gtms.add(gtm);
			}
			
	   }
	   catch(Exception e){
		   
		   GoogleTagManager gtm = new GoogleTagManager();
		   gtm=new GoogleTagManager();
		   gtm.setSuccess(Boolean.FALSE);
		   gtm.setResponseString(e.getMessage());
		   gtms.add(gtm);
           LOGGER.log(Level.SEVERE,e.getMessage(),e);
           
        }
		return gtms;
	}
	
	public static GoogleTagManager getGoogleTagManagerForProject(Long projectId)
	{
		GoogleTagManager gtm = null;
		
		try{
			Criteria c=new Criteria(new Column(GOOGLE_TAG_MANAGER_DETAILS.TABLE,GOOGLE_TAG_MANAGER_DETAILS.PROJECT_ID),projectId,QueryConstants.EQUAL);
			DataObject dobj = ZABModel.getRow(GOOGLE_TAG_MANAGER_DETAILS.TABLE, c);
			ArrayList<GoogleTagManager> googleTagManagerRes = getGoogleTagManagerFromDobj(dobj);
			if(googleTagManagerRes!=null) {
				if(!googleTagManagerRes.isEmpty()) {
					gtm=googleTagManagerRes.get(0);
				}else {
					gtm=new GoogleTagManager();
					gtm.setSuccess(Boolean.FALSE);
				}
			}
	   }
	   catch(Exception e){
		   	gtm=new GoogleTagManager();
		   	gtm.setSuccess(Boolean.FALSE);
		   	gtm.setResponseString(e.getMessage());
        	LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
		return gtm;
	}
	
	public static ArrayList<GoogleTagManager> getGoogleTagManagerFromDobj(DataObject dobj) throws DataAccessException
	{
		ArrayList<GoogleTagManager> googletagmanager=new ArrayList<GoogleTagManager>();
		if(dobj.containsTable(GOOGLE_TAG_MANAGER_DETAILS.TABLE)) {
			Iterator it = dobj.getRows(GOOGLE_TAG_MANAGER_DETAILS.TABLE);
			while(it.hasNext()) {
				Row row = (Row)it.next();
				GoogleTagManager g = getGoogleTagManagerFromRow(row);
				googletagmanager.add(g);
			}
		}
		return googletagmanager;
	}
	
	public static GoogleTagManager getGoogleTagManagerFromRow(Row row)
	{
		GoogleTagManager googletagmanager=new GoogleTagManager();
		googletagmanager.setSuccess(Boolean.TRUE);
		googletagmanager.setProjectId((Long)row.get(GOOGLE_TAG_MANAGER_DETAILS.PROJECT_ID));
		googletagmanager.setEmailId((String)row.get(GOOGLE_TAG_MANAGER_DETAILS.EMAIL_ID));
		googletagmanager.setAccountId((Long)row.get(GOOGLE_TAG_MANAGER_DETAILS.ACCOUNT_ID));
		googletagmanager.setContainerId((Long)row.get(GOOGLE_TAG_MANAGER_DETAILS.CONTAINER_ID));
		googletagmanager.setWorkSpaceId((Long)row.get(GOOGLE_TAG_MANAGER_DETAILS.WORKSPACE_ID));
		googletagmanager.setTagId((Long)row.get(GOOGLE_TAG_MANAGER_DETAILS.TAG_ID));
		googletagmanager.setAccountName((String)row.get(GOOGLE_TAG_MANAGER_DETAILS.ACCOUNT_NAME));
		googletagmanager.setContainerName((String)row.get(GOOGLE_TAG_MANAGER_DETAILS.CONTAINER_NAME));
		googletagmanager.setWorkSpaceName((String)row.get(GOOGLE_TAG_MANAGER_DETAILS.WORKSPACE_NAME));
		return googletagmanager;
	}
	
	public static GoogleTagManager deleteGoogleTagManager(String projectId) {
		// TODO Auto-generated method stub
		GoogleTagManager googletagmanager=null;
		try {
			
			GoogleTagManager gtm = GoogleTagManager.getGoogleTagManagerForProject(Long.parseLong(projectId));
			String emailId = gtm.getEmailId();
			String accountId = gtm.getAccountId().toString();
			String containerId = gtm.getContainerId().toString();
			String workspaceId = gtm.getWorkSpaceId().toString();
			String tagId = gtm.getTagId().toString();

			String GadgetsURL = ApplicationProperty.getString("com.abtest.gtm.serverurl");
			emailId = URLEncoder.encode(emailId,"UTF-8"); //No I18N
			URL GTMAccountsURL = new URL(GadgetsURL+"/api/google/v1/tagmanager/deletetag?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&response_type=gadgets&mailId="+emailId+"&accountId="+accountId+"&containerId="+containerId+"&workspaceId="+workspaceId+"&tagId="+tagId); //No I18N
			HttpURLConnection httpCon = (HttpURLConnection) GTMAccountsURL.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" ); //No I18N
			httpCon.setRequestMethod("DELETE"); //No I18N
			httpCon.connect();
			int code = httpCon.getResponseCode(); 
			
			//if(code == 200){
				
				URL url = new URL(GadgetsURL+"/api/google/v1/action/auth/revoke?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&authScopes=GTM.READ&mailId="+emailId); //No I18N
				HttpURLConnection httpCon1 = (HttpURLConnection) url.openConnection();
				httpCon1.setDoOutput(true);
				httpCon1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" ); //No I18N
				httpCon1.setRequestMethod("DELETE"); //No I18N
				httpCon1.connect();
				int responseCode = httpCon1.getResponseCode();
				if(responseCode == 200){
					
					Criteria c = new Criteria(new Column(GOOGLE_TAG_MANAGER_DETAILS.TABLE, GOOGLE_TAG_MANAGER_DETAILS.PROJECT_ID), projectId, QueryConstants.EQUAL);
					DataObject dobj = getRow(GOOGLE_TAG_MANAGER_DETAILS.TABLE, c);
					if(dobj.containsTable(GOOGLE_TAG_MANAGER_DETAILS.TABLE)) {
						Row row = dobj.getFirstRow(GOOGLE_TAG_MANAGER_DETAILS.TABLE);
						googletagmanager = getGoogleTagManagerFromRow(row);
						deleteResource(row);
						
						googletagmanager.setSuccess(Boolean.TRUE);
						googletagmanager.setProjectId(Long.parseLong(projectId));
						
						Project proj = Project.getProjectByProjectId(Long.parseLong(projectId));
						String project_link_name = proj.getProjectLinkName();
						Integration.deleteIntegration("6", project_link_name, "");
						
						googletagmanager.setSuccess(Boolean.TRUE);
						
					} else {
						throw new ResourceNotFoundException(AudienceConstants.API_MODULE);
					}
					
					
				}
			//}
			httpCon.disconnect();
			
		}  catch (Exception e) {
			googletagmanager = new GoogleTagManager();
			googletagmanager.setSuccess(Boolean.FALSE);
			googletagmanager.setResponseString(e.getMessage());
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return googletagmanager;
	}
}
