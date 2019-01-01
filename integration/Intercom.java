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
import org.json.JSONException;
import org.json.JSONObject;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.UpdateQuery;
import com.adventnet.ds.query.UpdateQueryImpl;
import com.adventnet.iam.IAMUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;
import com.zoho.abtest.GOOGLE_ADWORDS_DETAILS;
import com.zoho.abtest.GOOGLE_TAG_MANAGER_DETAILS;
import com.zoho.abtest.INTERCOM;
import com.zoho.abtest.INTERCOM_MAPPING;

import com.zoho.abtest.PROJECT_INTEGRATION;
<<<<<<< HEAD
import com.zoho.abtest.TRIGGER;
import com.zoho.abtest.USER_IDENTITY_DETAILS;
=======

>>>>>>> 0673152e9364e51b6181c09a88e62aaeabf034a2
import com.zoho.abtest.audience.AudienceConstants;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.exception.ResourceNotFoundException;
import com.zoho.abtest.identity.IdentityConstants;
import com.zoho.abtest.project.Project;

import com.zoho.abtest.utility.ApplicationProperty;
import com.zoho.abtest.utility.ZABServiceOrgUtil;
import com.zoho.abtest.utility.ZABUtil;

public class Intercom extends ZABModel {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(Intercom.class.getName());
	
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}


	private String accesstoken;
	private Long projectId;
	private Long userId;
	private String emailId;
	private String workSpace;
	private String appId;
	public String getWorkSpace() {
		return workSpace;
	}
	public void setWorkSpace(String workSpace) {
		this.workSpace = workSpace;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getIntercom_user_email() {
		return intercom_user_email;
	}
	public String getIntercom_user_id() {
		return intercom_user_id;
	}


	private String intercom_user_email=null;
	private String intercom_user_id=null;

	public String getIntercomUserEmail() {
		return intercom_user_email;
	}
	public void setIntercom_user_email(String intercom_user_email) {
		this.intercom_user_email = intercom_user_email;
	}
	public String getIntercomUserId() {
		return intercom_user_id;
	}
	public void setIntercom_user_id(String intercom_user_id) {
		this.intercom_user_id = intercom_user_id;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public static String getAccesstokenByProjectId(Long projectid){
		String accesstoken=null;
		try{
		Criteria c = new Criteria(new Column(INTERCOM.TABLE,INTERCOM.PROJECT_ID), projectid, QueryConstants.EQUAL);
		DataObject dobj = ZABModel.getRow(INTERCOM.TABLE,c);
		if(dobj.containsTable(INTERCOM.TABLE)){
			Row row=dobj.getRow(INTERCOM.TABLE);
			accesstoken=(String)row.get(INTERCOM.ACCESS_TOKEN);
		}
	}
		catch(Exception e) {
			LOGGER.log(Level.SEVERE,e.getMessage(), e);
		}
		return accesstoken;
	}
	public static String getEmail(String accesstoken,String type,String user_id) throws JSONException{
		StringBuffer response = new StringBuffer();
		String s=null;
		String email=null;
		String url=null;
		try {
			if(type.equals("users")){
			    url=ApplicationProperty.getString("com.abtest.intercom.users");}
			else{
				url=ApplicationProperty.getString("com.abtest.intercom.leads");
			}
			url=url+"/"+user_id;
			URL obj = new URL(url);
			s=obj.getProtocol();
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET"); //NO I18N
	    	con.setRequestProperty("Authorization","Bearer "+accesstoken);//NO I18N
	    	con.setRequestProperty("Accept","application/json");//NO I18N
	    	con.setRequestProperty("Content-Type","application/json");//NO I18N
			con.setConnectTimeout(3000);
			int responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured", e);
		}
		 if(response.toString().contains("email")){
		 String tokens=response.toString();
		 JSONObject json=new JSONObject(tokens);
	      if(json.has("email")){
	    	  email=json.getString("email");			  
	      }}
	         return email;
	}
	
	private static String getIntercom(String accesstoken) throws JSONException{
		StringBuffer response = new StringBuffer();
		String s=null;
		String email=null;
		try {
			
			String adminurl=ApplicationProperty.getString("com.abtest.intercom.admin");
			URL obj = new URL(adminurl);
			s=obj.getProtocol();
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET"); //NO I18N
	    	con.setRequestProperty("Authorization","Bearer "+accesstoken);//NO I18N
	    	con.setRequestProperty("Accept","application/json");//NO I18N
	    	con.setRequestProperty("Content-Type","application/json");//NO I18N
			con.setConnectTimeout(3000);
			int responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Exception occured", e);
				return null;
		}
		 String tokens=null;
		 tokens=response.toString();
		// JSONObject json=new JSONObject(tokens);
//		  for(int i=0;i<json.getJSONArray("admins").length();i++){
//				JSONObject json1=(JSONObject) json.getJSONArray("admins").get(i);
//				if(json1.get("type").equals("admin")){
//		        email=json1.get("email").toString();}
//		  }
	         return tokens;
	}
	
	
	 public static Intercom getIntercomDetails(String accesstoken,String user_id,String type,String email) throws JSONException{
		StringBuffer response = new StringBuffer();
		String s=null,id=null,url=null;
		Intercom intercom=new Intercom();
		if(user_id==null && email==null){
			return null;
		}
		try {
			if(type.equals("leads")){
			  url=ApplicationProperty.getString("com.abtest.intercom.leads");}
			else{
				  url=ApplicationProperty.getString("com.abtest.intercom.users");
			}
			if(user_id==null){
				url=url+"?email="+email;//NO I18N
			}
			else{
			url=url+"?user_id="+user_id;//NO I18N
			}
			URL obj = new URL(url);
			s=obj.getProtocol();
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET"); //NO I18N
	    	con.setRequestProperty("Authorization","Bearer "+accesstoken);//NO I18N
	    	con.setRequestProperty("Accept","application/json");//NO I18N
	    	con.setRequestProperty("Content-Type","application/json");//NO I18N
			con.setConnectTimeout(3000);
			int responseCode = con.getResponseCode();			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		
		 String tokens=response.toString();
		 JSONObject json=new JSONObject(tokens);
		    if(json.has("id")){
		    	id=json.getString("id");
		    	intercom.setIntercom_user_id(id);
		    } 
		    if(json.has("email")){
		    	email=json.getString("email");
		    	intercom.setIntercom_user_email(email);
		    }
		}
		    catch(Exception e) {
		    	LOGGER.log(Level.SEVERE, "Exception occured", e);
				return null;
		}
	         return intercom;
	}

	private static StringBuffer getAccessToken(String code){
		StringBuffer response = new StringBuffer();
		String s=null;
		try {
			String accessurl=ApplicationProperty.getString("com.abtest.intercom.accessurl");
			String clientid=ApplicationProperty.getString("com.abtest.intercom.clientid");
			String clientsecret=ApplicationProperty.getString("com.abtest.intercom.clientsecret");
             accessurl=accessurl+"?client_id="+clientid+"&client_secret="+clientsecret+"&code="+code;//NO I18N
			URL obj = new URL(accessurl);
			s=obj.getProtocol();
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST"); //NO I18N
	        con.setDoOutput(true);
			con.setConnectTimeout(3000);
			int responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured", e);
		}
	return response;
	}
	
public static Intercom setAccessToken(String code,String projectid, String portalDomain){
	String str=null;
	Intercom intercom=new Intercom();
	try {
		Long zsoid = ZABServiceOrgUtil.getZSOIDFromDomain(portalDomain);
		ZABUtil.setDBSpace(zsoid.toString());
	   	Long project_id=Long.parseLong(projectid);
		  String tokens=getAccessToken(code).toString();
		  JSONObject json=new JSONObject(tokens);
		  String accesstoken=json.get("access_token").toString();
		  tokens=getIntercom(accesstoken);
		  if(tokens!=null){
			   json=new JSONObject(tokens);
			  String email=json.getString("email");
			  String space=json.getJSONObject("app").getString("name");
			  String id_code=json.getJSONObject("app").getString("id_code");
		     Row row = new Row (INTERCOM.TABLE);
		     Long userid=IAMUtil.getCurrentUser().getZUID();
		     intercom.setAccesstoken(accesstoken);
				intercom.setAppId(id_code);
				intercom.setEmailId(email);
				intercom.setProjectId(project_id);
				intercom.setWorkSpace(space);	
			row.set(INTERCOM.USER_ID,userid);
			row.set(INTERCOM.PROJECT_ID, project_id);
			row.set(INTERCOM.ACCESS_TOKEN, accesstoken);
			row.set(INTERCOM.EMAIL_ID, email);
			row.set(INTERCOM.APP_ID, id_code);
			row.set(INTERCOM.WORKSPACE, space);
			DataObject d=new WritableDataObject();
			d.addRow(row);
			createResource(d);			
			}		
	}
	catch (Exception ex) {
		LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		intercom=null;
	}
	return intercom;
}

	
	
	public static Intercom create(HashMap<String,String> hs) {
		// TODO Auto-generated method stub
		Intercom intercom = new Intercom();
		try {
			
			String projectId = hs.get(IntegrationConstants.PROJECT_ID);
			Project project = Project.getProjectByProjectId(Long.parseLong(projectId));
	
	            hs.put(IntegrationConstants.INTEGRATION_ID, "7"); //No I18N
	            hs.put(IntegrationConstants.PROJECT_ID, projectId);
	            createRow(IntegrationConstants.PROJECT_INTEGRATION_TABLE, PROJECT_INTEGRATION.TABLE, hs);
				createRow(IntegrationConstants.INTERCOM_TABLE, INTERCOM.TABLE, hs);
                intercom .setSuccess(Boolean.TRUE);
			
			
		} catch (Exception ex) {
			
			intercom.setSuccess(Boolean.FALSE);
			intercom.setResponseString(ex.getMessage());
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		}
		return intercom;
	}
	
	
	
	public static ArrayList<Intercom> getIntercomFromDobj(DataObject dobj) throws DataAccessException
	{
		ArrayList<Intercom> intercom=new ArrayList<Intercom>();
		if(dobj.containsTable(INTERCOM.TABLE)) {
			Iterator it = dobj.getRows(INTERCOM.TABLE);
			while(it.hasNext()) {
				Row row = (Row)it.next();
				Intercom g = getIntercomFromRow(row);
				intercom.add(g);
			}
		}
		return intercom;
	}
	
	public static Intercom getIntercomFromRow(Row row)
	{
		Intercom intercom=new Intercom();
		intercom.setSuccess(Boolean.TRUE);
		intercom.setProjectId((Long)row.get(INTERCOM.PROJECT_ID));
		intercom.setUserId((Long)row.get(INTERCOM.USER_ID));
		intercom.setEmailId((String)row.get(INTERCOM.EMAIL_ID));
		intercom.setAppId((String)row.get(INTERCOM.APP_ID));
		intercom.setWorkSpace((String)row.get(INTERCOM.WORKSPACE));

		return intercom;
	}
	
	
	public static  Intercom getIntercom(Long projectId)
	{
		Intercom intercom = new Intercom();
	
		try{
			Criteria c=new Criteria(new Column(INTERCOM.TABLE,INTERCOM.PROJECT_ID),projectId,QueryConstants.EQUAL);
			DataObject dobj = ZABModel.getRow(INTERCOM.TABLE, c);
			ArrayList<Intercom> intercoms =getIntercomFromDobj(dobj);
			if(intercoms!=null && !intercoms.isEmpty()) {
				intercom=intercoms.get(0);
			}
			else{
				 intercom.setSuccess(Boolean.FALSE);
			}
	   }
	   catch(Exception e){
		   intercom.setSuccess(Boolean.FALSE);
		   intercom.setResponseString(e.getMessage());
        	LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
		return intercom;
	}

	
	public static Intercom delete(String projectId) {
		// TODO Auto-generated method stub
		Intercom intercom=null;
		try {
			        Criteria c = new Criteria(new Column(INTERCOM.TABLE, INTERCOM.PROJECT_ID), projectId, QueryConstants.EQUAL);
					DataObject dobj = getRow(INTERCOM.TABLE, c);
					if(dobj.containsTable(INTERCOM.TABLE)) {
						Row row = dobj.getFirstRow(INTERCOM.TABLE);
						intercom = getIntercomFromRow(row);
						deleteResource(row);
						
						intercom.setSuccess(Boolean.TRUE);
						intercom.setProjectId(Long.parseLong(projectId));
						
						Project proj = Project.getProjectByProjectId(Long.parseLong(projectId));
						String project_link_name = proj.getProjectLinkName();
	//					Integration.deleteIntegration("7", project_link_name, "");
						
						intercom.setSuccess(Boolean.TRUE);
						
					} else {
						throw new ResourceNotFoundException(AudienceConstants.API_MODULE);
					}
					

		}  catch (Exception e) {
			intercom.setSuccess(Boolean.FALSE);
			intercom.setResponseString(e.getMessage());
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return intercom;
	}
	
	public static void intercomWebhook(HashMap<String,String> hs) {
		// TODO Auto-generated method stub
		String id=null;String uuid=null;String project_key=null;
		Long project_id=null;
		HashMap<String,String> hashMap = new HashMap<String,String>();
		try {
			id=hs.get(IntegrationConstants.INTERCOM_ID);
			 Criteria c = new Criteria(new Column(INTERCOM_MAPPING.TABLE, INTERCOM_MAPPING.VISITOR_ID), id, QueryConstants.EQUAL);
			 DataObject dobj=ZABModel.getRow(INTERCOM_MAPPING.TABLE, c);
					if(dobj.containsTable(INTERCOM_MAPPING.TABLE)) {
						Row row=(Row) dobj.getRow(INTERCOM_MAPPING.TABLE);
						uuid = (String)row.get(INTERCOM_MAPPING.UUID);
						project_id = (Long)row.get(INTERCOM_MAPPING.PROJECT_ID);
						project_key=Project.getProjectByProjectId(project_id).getProjectKey();
					hashMap.put(IdentityConstants.IDENTITY_DETAILS, hs.get("email"));
					hashMap.put(IdentityConstants.UUID,uuid);
					hashMap.put(IdentityConstants.PROJECT_KEY, project_key);
					hashMap.put(IdentityConstants.TIME, hs.get("created_at"));					
					createRow(IdentityConstants.IDENTITY_DETAILS_TABLE,USER_IDENTITY_DETAILS.TABLE,hashMap);	
					}			

		}  catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return ;
	}
	
}
