//$Id$
package com.zoho.abtest.integration;


import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.Join;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.SortColumn;
import com.adventnet.iam.IAMProxy;
import com.adventnet.iam.IAMUtil;
import com.adventnet.iam.OAuthAPI;
import com.adventnet.iam.ServiceOrg;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.zoho.abtest.AC_PORTAL;
import com.zoho.abtest.EXPERIMENT;
import com.zoho.abtest.FUNNEL_STEPS;
import com.zoho.abtest.GOOGLE_ADWORDS_DETAILS;
import com.zoho.abtest.PROJECT;
import com.zoho.abtest.PROJECT_INTEGRATION;
import com.zoho.abtest.adminconsole.AdminConsoleConstants;
import com.zoho.abtest.audience.AudienceConstants;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.dimension.Dimension;
import com.zoho.abtest.dynamicconf.DynamicConfigurationConstants;
import com.zoho.abtest.dynamicconf.DynamicConfigurationUtil;
import com.zoho.abtest.elastic.ElasticSearchStatistics;
import com.zoho.abtest.elastic.ElasticSearchUtil;
import com.zoho.abtest.exception.ResourceNotFoundException;
import com.zoho.abtest.exception.ZABException;
import com.zoho.abtest.experiment.Experiment;
import com.zoho.abtest.experiment.ExperimentConstants;
import com.zoho.abtest.experiment.ExperimentConstants.ExperimentStatus;
import com.zoho.abtest.experiment.ExperimentConstants.ExperimentType;
import com.zoho.abtest.forms.FormRawDataConstants;
import com.zoho.abtest.forms.FormReportConstants;
import com.zoho.abtest.funnel.FunnelAnalysis;
import com.zoho.abtest.funnel.report.FunnelFlexible;
import com.zoho.abtest.funnel.report.FunnelReportConstants;
import com.zoho.abtest.goal.GoalConstants;
import com.zoho.abtest.heatmaps.HeatmapConstants;
import com.zoho.abtest.integration.IntegrationConstants.Integ;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.project.ProjectConstants;
import com.zoho.abtest.projectgoals.GoalAdwords;
import com.zoho.abtest.report.ElasticSearchConstants;
import com.zoho.abtest.report.ReportConstants;
import com.zoho.abtest.utility.ApplicationProperty;
import com.zoho.abtest.utility.ZABServiceOrgUtil;
import com.zoho.abtest.utility.ZABUtil;


public class GoogleAdwords extends ZABModel {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(GoogleAdwords.class.getName());

	private Long projectId;
	private String emailId;
	private Long clientId;
	private String customername;
	private Boolean ismcc;
	private String oauthtoken;
	private Long oauthzuid;
	private String campaignName;
	private String adGroupName;
	private Integer clicks;
	private Integer googleclicks;
	private Double costs;
	private Integer impressions;
	private Integer conversions;
	private Double googleconversions;
	private String currency;
	private String ctr = "0";
	private Double cpc;
	private JSONArray jarray = new JSONArray();
	

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
	
	public Long getClientId() {
		return clientId;
	}
	
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
	public String getCustomerName() {
		return customername;
	}
	
	public void setCustomerName(String customername) {
		this.customername = customername;
	}
	
	public Boolean getIsMCC() {
		return ismcc;
	}
	
	public void setIsMCC(Boolean ismcc) {
		this.ismcc = ismcc;
	}
	
	public String getOAuthToken() {
		return oauthtoken;
	}
	
	public void setOAuthToken(String oauthtoken) {
		this.oauthtoken = oauthtoken;
	}
	
	public Long getOAuthZUID() {
		return oauthzuid;
	}
	
	public void setOAuthZUID(Long oauthzuid) {
		this.oauthzuid = oauthzuid;
	}
	
	public String getCampaignName() {
		return campaignName;
	}
	
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	
	public JSONArray getAdGroupArray() {
		return jarray;
	}
	
	public void setAdGroupArray(JSONArray jarray) {
		this.jarray = jarray;
	}
	
	public String getAdGroupName() {
		return adGroupName;
	}
	
	public void setAdGroupName(String adGroupName) {
		this.adGroupName = adGroupName;
	}
	
	public Integer getClicks() {
		return clicks;
	}
	
	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}
	
	public Integer getGoogleClicks() {
		return googleclicks;
	}
	
	public void setGoogleClicks(Integer googleclicks) {
		this.googleclicks = googleclicks;
	}
	
	public Double getCosts() {
		return costs;
	}
	
	public void setCosts(Double costs) {
		this.costs = costs;
	}
	
	public Integer getImpressions() {
		return impressions;
	}
	
	public void setImpressions(Integer impressions) {
		this.impressions = impressions;
	}
	
	public Integer getConversions() {
		return conversions;
	}
	
	public void setConversions(Integer conversions) {
		this.conversions = conversions;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Double getGoogleConversions() {
		return googleconversions;
	}
	
	public void setGoogleConversions(Double googleconversions) {
		this.googleconversions = googleconversions;
	}
	
	public String getCTR() {
		return ctr;
	}
	
	public void setCTR(String ctr) {
		this.ctr = ctr;
	}
	
	public Double getCPC() {
		return cpc;
	}
	
	public void setCPC(Double cpc) {
		this.cpc = cpc;
	}
	
	
	//public static GoogleAdwords createGoogleAdwords(String emailId, String projectId,String portalName) {
	public static GoogleAdwords createGoogleAdwords(HashMap<String, String> hs) {
		// TODO Auto-generated method stub
		GoogleAdwords googleadwords=null;
		try {
			
			String emailId = hs.get(IntegrationConstants.EMAIL_ID);
			String projectId = hs.get(IntegrationConstants.PROJECT_ID);
			String clientId =  hs.get(IntegrationConstants.CLIENT_ID);
			String accountName = hs.get(IntegrationConstants.CUSTOMER_NAME);
			String is_mcc = hs.get(IntegrationConstants.IS_MCC);
			
			String OAuthToken = null;
			OAuthToken = createRefreshToken();
			Long OAuthZUID = IAMUtil.getCurrentUser().getZUID();
			
			hs.put(IntegrationConstants.PROJECT_ID, projectId);
			hs.put(IntegrationConstants.EMAIL_ID, emailId);
			hs.put(IntegrationConstants.CLIENT_ID, clientId);
			
			hs.put(IntegrationConstants.OAUTH_TOKEN, OAuthToken);
			hs.put(IntegrationConstants.OAUTH_ZUID, OAuthZUID.toString());
			hs.put(IntegrationConstants.ACCOUNT_NAME, accountName);
			hs.put(IntegrationConstants.IS_MCC, is_mcc);

			DataObject dobj = createRow(IntegrationConstants.GOOGLE_ADWORDS_TABLE, GOOGLE_ADWORDS_DETAILS.TABLE, hs);
			
			Project proj = Project.getProjectByProjectId(Long.parseLong(projectId));
			String project_link_name = proj.getProjectLinkName();
			HashMap<String, String> hs1 = new HashMap<String, String>();
			hs1.put(IntegrationConstants.INTEGRATION_ID, "4");
			hs1.put(IntegrationConstants.PROJECT_LINK_NAME, project_link_name);
			Integration.createIntegration(hs1);
			
			ArrayList<GoogleAdwords> googleAdwordsRes =getGoogleAdwordsFromDobj(dobj);
			if(googleAdwordsRes!=null && !googleAdwordsRes.isEmpty()) {
				googleadwords=googleAdwordsRes.get(0);
			}
			
		}  catch (Exception ex) {
			googleadwords = new GoogleAdwords();
			googleadwords.setSuccess(Boolean.FALSE);
			googleadwords.setResponseString(ex.getMessage());
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		}
		return googleadwords;
	}
	
	public static ArrayList<GoogleAdwords> getGoogleAdwordsFromDobj(DataObject dobj) throws DataAccessException
	{
		ArrayList<GoogleAdwords> googleadwords=new ArrayList<GoogleAdwords>();
		if(dobj.containsTable(GOOGLE_ADWORDS_DETAILS.TABLE)) {
			Iterator it = dobj.getRows(GOOGLE_ADWORDS_DETAILS.TABLE);
			while(it.hasNext()) {
				Row row = (Row)it.next();
				GoogleAdwords g = getGoogleAdwordsFromRow(row);
				googleadwords.add(g);
			}
		}
		return googleadwords;
	}
	
	public static GoogleAdwords getGoogleAdwordsFromRow(Row row)
	{
		GoogleAdwords googleadwords=new GoogleAdwords();
		googleadwords.setSuccess(Boolean.TRUE);
		googleadwords.setProjectId((Long)row.get(GOOGLE_ADWORDS_DETAILS.PROJECT_ID));
		googleadwords.setEmailId((String)row.get(GOOGLE_ADWORDS_DETAILS.EMAIL_ID));
		googleadwords.setClientId((Long)row.get(GOOGLE_ADWORDS_DETAILS.CLIENT_ID));
		googleadwords.setOAuthToken((String)row.get(GOOGLE_ADWORDS_DETAILS.OAUTH_TOKEN));
		googleadwords.setOAuthZUID((Long)row.get(GOOGLE_ADWORDS_DETAILS.OAUTH_ZUID));
		googleadwords.setCustomerName((String)row.get(GOOGLE_ADWORDS_DETAILS.ACCOUNT_NAME));
		googleadwords.setIsMCC((Boolean)row.get(GOOGLE_ADWORDS_DETAILS.IS_MCC));
		return googleadwords;
	}
	

	public static String getAdwordsClientId(String emailId){
		String clientId = null;
		
		String adwordsInfoURL = null;
		try{
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gadwords.serverurl");
			adwordsInfoURL = GadgetsURL+"/api/google/v1/adwords/getcustomerinfo?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&response_type=gadgets&mailId="+URLEncoder.encode(emailId,"UTF-8"); //No I18N
			String responseJsonStr = ZABUtil.getResponseStrFromURL(adwordsInfoURL);
			JSONObject responseJson = new JSONObject(responseJsonStr);
			JSONObject clientObj = new JSONObject(responseJson.getString("response"));  
			JSONObject clientResObj = new JSONObject(clientObj.getString("result"));
			JSONArray jar1 = new JSONArray(clientResObj.getString("rows")); 
			JSONArray jobj = new JSONArray(jar1.get(0).toString());
			clientId = jobj.getString(0);
		}catch(Exception ex){
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		}
		return clientId;
	}
	
	public static ArrayList<GoogleAdwords> getCustomerInfo(String emailId){
		
		Long clientId = 0L;
		String customername = null;
		Boolean ismcc = false;
		ArrayList<GoogleAdwords> ga = new ArrayList<GoogleAdwords>();
		String adwordsInfoURL = null;
		try{
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gadwords.serverurl");
			adwordsInfoURL = GadgetsURL+"/api/google/v1/adwords/getcustomerinfo?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&response_type=gadgets&mailId="+URLEncoder.encode(emailId,"UTF-8"); //No I18N
			String responseJsonStr = ZABUtil.getResponseStrFromURL(adwordsInfoURL);
			JSONObject responseJson = new JSONObject(responseJsonStr);
			JSONObject clientObj = new JSONObject(responseJson.getString("response"));  
			JSONObject clientResObj = new JSONObject(clientObj.getString("result"));
			JSONArray jar1 = new JSONArray(clientResObj.getString("rows"));
			for(int i=0; i<jar1.length(); i++){
				GoogleAdwords ga1 = new GoogleAdwords();
				JSONArray jobj = new JSONArray(jar1.get(i).toString());
				clientId = Long.parseLong(jobj.getString(0));
				customername = jobj.getString(1);
				ismcc = Boolean.parseBoolean(jobj.getString(2));
				ga1.setClientId(clientId);
				ga1.setCustomerName(customername);
				ga1.setIsMCC(ismcc);
				ga1.setSuccess(Boolean.TRUE);
				ga.add(ga1);
			}

		}catch(Exception ex){
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		}
		return ga;
	}
	
	public static ArrayList<GoogleAdwords> getAccountInfo(String emailId, String client_id){
		
		Long clientId = 0L;
		String customername = null;
		Boolean ismcc = false;
		ArrayList<GoogleAdwords> ga = new ArrayList<GoogleAdwords>();
		String adwordsInfoURL = null;
		try{
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gadwords.serverurl");
			adwordsInfoURL = GadgetsURL+"/api/google/v1/adwords/getaccountinfo?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&response_type=gadgets&clientId="+client_id+"&mailId="+URLEncoder.encode(emailId,"UTF-8"); //No I18N
			String responseJsonStr = ZABUtil.getResponseStrFromURL(adwordsInfoURL);
			JSONObject responseJson = new JSONObject(responseJsonStr);
			JSONObject clientObj = new JSONObject(responseJson.getString("response"));  
			JSONObject clientResObj = new JSONObject(clientObj.getString("result"));
			JSONArray jar1 = new JSONArray(clientResObj.getString("rows"));
			for(int i=0; i<jar1.length(); i++){
				GoogleAdwords ga1 = new GoogleAdwords();
				JSONArray jobj = new JSONArray(jar1.get(i).toString());
				clientId = Long.parseLong(jobj.getString(0));
				customername = jobj.getString(1);
				ismcc = Boolean.parseBoolean(jobj.getString(2));
				ga1.setClientId(clientId);
				ga1.setCustomerName(customername);
				ga1.setIsMCC(ismcc);
				ga1.setSuccess(Boolean.TRUE);
				ga.add(ga1);
			}

		}catch(Exception ex){
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		}
		return ga;
	}
	
	

	public static ArrayList<GoogleAdwords> getGoogleAdwords(String projectId)
	{
		ArrayList<GoogleAdwords> google = new ArrayList<GoogleAdwords>();
	
		try{
			Criteria c=new Criteria(new Column(GOOGLE_ADWORDS_DETAILS.TABLE,GOOGLE_ADWORDS_DETAILS.PROJECT_ID),projectId,QueryConstants.EQUAL);
			DataObject dobj = ZABModel.getRow(GOOGLE_ADWORDS_DETAILS.TABLE, c);
			ArrayList<GoogleAdwords> googleAdwordsRes =getGoogleAdwordsFromDobj(dobj);
			if(googleAdwordsRes!=null && !googleAdwordsRes.isEmpty()) {
				google.add(googleAdwordsRes.get(0));
			}
	   }
	   catch(Exception e){
        	GoogleAdwords google1=new GoogleAdwords();
        	google1.setSuccess(Boolean.FALSE);
        	google1.setResponseString(e.getMessage());
        	google.add(google1);
        	LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
		return google;
	}
	
	
	public static void addGClidJob() throws Exception{
		
		
			LOGGER.info("addGClidJob STARTS ");
			DataObject dataObj = ZABModel.getRow(AC_PORTAL.TABLE, null);
			if(!dataObj.isEmpty())
			{
				Iterator it = dataObj.getRows(AC_PORTAL.TABLE);
				while(it.hasNext()) {
					try{
					Row row1 = (Row)it.next();
					Long zsoid = ((Long)row1.get(AC_PORTAL.ZSOID));
					ServiceOrg serviceOrg = ZABServiceOrgUtil.getServiceOrg(zsoid);
					String domainName = serviceOrg.getDomains().get(0).getDomain();
					ZABUtil.setDBSpace(zsoid.toString());
					
//					String domainName = "gfcxhp9l"; //No I18N
//					Long zsoid = 455L;
//			
					ArrayList<Project> proj = Project.getProjects(); 
					for(int i = 0; i<proj.size(); i++){
						Long projectId = proj.get(i).getProjectId();
						Criteria c = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),projectId, QueryConstants.EQUAL);
						Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),Integ.GOOGLEADWORDS.getIntegrationId(), QueryConstants.EQUAL);
						DataObject dobj = getRow(PROJECT_INTEGRATION.TABLE, c.and(c1));
						if(!dobj.isEmpty()){
							
							ArrayList<GoogleAdwords> gadwords = GoogleAdwords.getGoogleAdwords(projectId.toString());
							Long clientId = gadwords.get(0).getClientId();
							String emailId = gadwords.get(0).getEmailId();
							String oauthtoken = gadwords.get(0).getOAuthToken();
							Long oauthzuid = gadwords.get(0).getOAuthZUID();
							Boolean is_mcc = gadwords.get(0).getIsMCC();
//							if(oauthtoken==null){
//								oauthtoken = createRefreshToken();
//							}
							
							String accessToken = getAccessTokenfromRefreshToken(oauthtoken,oauthzuid);
						
							
							JSONArray fieldArray = new JSONArray();
							fieldArray.put("CampaignName");
							fieldArray.put("AdGroupName");
							fieldArray.put("GclId");
							
							final Calendar cal = Calendar.getInstance();
						    cal.add(Calendar.DATE, -1);
						    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");  //No I18N
						    String start_date= formatter.format(cal.getTime());  
							//emailId = URLEncoder.encode(emailId,"UTF-8"); //No I18N
							
							// Check for MCC account having more than one client_id
							if(accessToken != null && is_mcc != null){
								if(is_mcc){
									ArrayList<GoogleAdwords> ga = getAccountInfo(emailId,clientId.toString());
									for(int j=0; j<ga.size(); j++){
										clientId = ga.get(j).getClientId();
										pushgcliddata(emailId,clientId,accessToken,projectId,domainName,zsoid,start_date,fieldArray);
									}
								}else{
									pushgcliddata(emailId,clientId,accessToken,projectId,domainName,zsoid,start_date,fieldArray);
								}
							}
						}
					}
				}catch(Exception ex){
					LOGGER.log(Level.SEVERE, "!!!!!!!!!!!  addGClidJob Exception "+ex.getMessage(),ex);
				}
				}
		}
		
	}
	
	public static void pushgcliddata(String email_id, Long client_id, String accessToken, Long project_id, String domainName, Long zsoid, String start_date, JSONArray fieldArray){
		
		try{
			
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gadwords.serverurl");
			email_id = URLEncoder.encode(email_id,"UTF-8"); //No I18N
			String adwordsReportURL = GadgetsURL+"/api/google/v1/adwords/downloadreport?zeroimpression=false&zservice=ZohoPageSense&type=clpr&response_type=gadgets&clientId="+client_id+"&mailId="+email_id+"&sdate="+start_date+"&edate="+start_date+"&fields="+URLEncoder.encode(fieldArray.toString(),"UTF-8"); //No I18N
			String responseStr = ZABUtil.getResponseStrFromURL(adwordsReportURL,accessToken);
			String report[] = responseStr.split("\n");
			int strLen = report.length;
			
			String indexName = ElasticSearchUtil.getIndexByPortal(domainName);
			String type = ElasticSearchConstants.ADWORDS_RAW_TYPE;
			int count = 0;
			for(int j=2;j<strLen;j++){
				count++;
				String row[] = report[j].split(",");
				JSONObject obj = new JSONObject();
				obj.put("campaignname", row[0]);
				obj.put("adgroupname", row[1]);
				obj.put("gclid", row[2]);
				obj.put("projectid", project_id);
				obj.put("portal",domainName);
				obj.put("zsoid", zsoid);
				obj.put("time", ZABUtil.getCurrentTimeInMilliSeconds());
				ElasticSearchUtil.createIndex(indexName, type,obj.toString());
			}
			LOGGER.info("!!!!!!!!!!! addGClidJobWithDate END ProjectId = "+project_id+" ClientId = "+client_id+" DATE = "+ZABUtil.getCurrentDate()+" Count = "+count);
			
		}catch(Exception ex){
			LOGGER.log(Level.SEVERE, "!!!!!!!!!!!  addGClidJob Exception "+ex.getMessage(),ex);
		}
	}
	
	public static ArrayList<GoogleAdwords> getGoogleAdwordsReport(HashMap<String, String> hs,HttpServletRequest request,String projectId)
	{
		
		HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();
		String start_date_adwords = hs.get(IntegrationConstants.START_DATE_ADWORDS);
		String end_date_adwords = hs.get(IntegrationConstants.END_DATE_ADWORDS);
		//String experiment_type_no = hs.get(IntegrationConstants.EXPERIMENT_TYPE_NO);
		String url = hs.get(IntegrationConstants.URL);
		Integer matchTypeId = (Integer.parseInt(hs.get(IntegrationConstants.MATCH_TYPE_ID)));
		//Integer matchTypeId = 1;
		String hostName = "";
		hostName = request.getRequestURL().toString();
		if(hostName != null && (hostName.contains("localzoho") || hostName.contains("zohocorpin"))){
			url = "https://www.zoho.com/pagesense/"; //No I18N
		}
		HashMap<String,HashMap<String,JSONObject>> hashMap = new HashMap<String,HashMap<String,JSONObject>>();
		hashMap = getGclidVisitConversion(hs,projectId);
		//addGClidJob();
		
		ArrayList<GoogleAdwords> googleAdword = new ArrayList<GoogleAdwords>();
		
		String adwordsReportURL = "";
		try{
			
			Criteria c = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),projectId, QueryConstants.EQUAL);
			Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),Integ.GOOGLEADWORDS.getIntegrationId(), QueryConstants.EQUAL);
			DataObject dobj = getRow(PROJECT_INTEGRATION.TABLE, c.and(c1));
			
			if(!dobj.isEmpty()){	
			
				ArrayList<GoogleAdwords> gadwords = GoogleAdwords.getGoogleAdwords(projectId);
				Long clientId = gadwords.get(0).getClientId();
				String emailId = gadwords.get(0).getEmailId();
				String oauthtoken = gadwords.get(0).getOAuthToken();
				Long oauthzuid = gadwords.get(0).getOAuthZUID();
				Boolean is_mcc = gadwords.get(0).getIsMCC();

				int flag = 0;
				if(oauthtoken==null){
					oauthtoken = createRefreshToken();
					oauthzuid = IAMUtil.getCurrentUser().getZUID();
					flag = 1;
				}
				String accessToken = getAccessTokenfromRefreshToken(oauthtoken, oauthzuid);
				
				// Existing user who haven't oauthtoken
				if(accessToken!=null && accessToken!="" && flag ==1){
					HashMap<String, String> hs1 = new HashMap<String, String>();
					hs1.put(IntegrationConstants.OAUTH_TOKEN, oauthtoken);
					hs1.put(IntegrationConstants.OAUTH_ZUID, oauthzuid.toString());
					Criteria c2 = new Criteria(new Column(GOOGLE_ADWORDS_DETAILS.TABLE, GOOGLE_ADWORDS_DETAILS.PROJECT_ID), projectId, QueryConstants.EQUAL);
					ZABModel.updateRow(IntegrationConstants.GOOGLE_ADWORDS_TABLE, GOOGLE_ADWORDS_DETAILS.TABLE, hs1,c2,IntegrationConstants.API_MODULE);
				}
				
				String GadgetsURL = ApplicationProperty.getString("com.abtest.gadwords.serverurl");
				JSONArray fieldArray = new JSONArray();
				fieldArray.put("CampaignName");
				fieldArray.put("CampaignId");
				fieldArray.put("Clicks");
				fieldArray.put("Cost");
				fieldArray.put("Impressions");
				fieldArray.put("Conversions");
				fieldArray.put("Ctr");
				fieldArray.put("AverageCpc");
				fieldArray.put("CreativeFinalUrls");
				fieldArray.put("AccountCurrencyCode");
				fieldArray.put("AdGroupId");
				fieldArray.put("AdGroupName");
				
				String rowString = "";
				String campaignname = "";
				String adgroupname = "";
				int click = 0 ;
				long cost = 0;
				int impression = 0;
				double conversion = 0.0;
				double ctr = 0.0;
				int cpc = 0;
				DecimalFormat df = new DecimalFormat(".##");
				String Currency = null;
	
				
				if(is_mcc){ // Check for MCC account having more than one client_id
					ArrayList<GoogleAdwords> ga = getAccountInfo(emailId,clientId.toString());
					for(int j=0; j<ga.size(); j++){
						clientId = ga.get(j).getClientId();
						adwordsReportURL = GadgetsURL+"/api/google/v1/adwords/downloadreport?zeroimpression=false&zservice=ZohoPageSense&type=adpr&response_type=gadgets&clientId="+clientId+"&mailId="+URLEncoder.encode(emailId.toString(),"UTF-8")+"&sdate="+start_date_adwords+"&edate="+end_date_adwords+"&fields="+URLEncoder.encode(fieldArray.toString(),"UTF-8"); //No I18N
						String responseStr = ZABUtil.getResponseStrFromURL(adwordsReportURL,accessToken);
						String report[] = responseStr.split("\n");
						int strLen = report.length;
				
						for(int i=2;i<strLen;i++){
							String row[] = report[i].split(",");
							if(i==2){
								Currency = row[9];
							}
							
							url = URLDecoder.decode(url,"UTF-8"); //No I18N
							
							String currentURL = row[8];
							if(currentURL.contains("www.")){
								String currentURL1[] = currentURL.split("\"\\[\"\"");
								currentURL = currentURL1[1];
								String currentURL2[] = currentURL.split("\"\"\\]\"");
								currentURL = currentURL2[0];
							}
							
							Boolean urlMatch = Experiment.urlMatches(url, currentURL, matchTypeId);
							
							/*if(row[8] != null && row[8].contains(url)){ */
							if(urlMatch){
								
								HashMap hm_construct = new HashMap<String,String>();	
							    if(hm.containsKey(row[0])){
							    	
							    	hm_construct = hm.get(row[0]);  
							    	if(hm_construct.containsKey(row[11])){
							    		
							    		rowString = (String)hm_construct.get(row[11]);
								    	String dataString[] = rowString.split("_");
								    	campaignname = dataString[0];
								    	adgroupname = dataString[7];
								    	click = Integer.parseInt(dataString[1])+Integer.parseInt(row[2]); 
								    	cost = Long.parseLong(dataString[2])+Long.parseLong(row[3]); 
								    	impression = Integer.parseInt(dataString[3])+Integer.parseInt(row[4]); 
								    	conversion = Double.parseDouble(dataString[4])+Double.parseDouble(row[5]); 
								    	ctr = Double.parseDouble(dataString[5])+Double.parseDouble(row[6].substring(0, row[6].length() - 1)); 
								    	cpc = Integer.parseInt(dataString[6])+Integer.parseInt(row[7]); 
								    	
								    	rowString = campaignname+"_"+click+"_"+cost+"_"+impression+"_"+conversion+"_"+ctr+"_"+cpc+"_"+adgroupname;
								    	hm_construct.put(row[11], rowString);
								    	hm.put(row[0], hm_construct);
								    	
							    	}else{
							    		
								    	//rowString = campaignname+"_"+click+"_"+cost+"_"+impression+"_"+conversion+"_"+ctr+"_"+cpc+"_"+adgroupname;
							    		rowString = row[0]+"_"+row[2]+"_"+row[3]+"_"+row[4]+"_"+row[5]+"_"+row[6].substring(0, row[6].length() - 1)+"_"+row[7]+"_"+row[11];
								    	hm_construct.put(row[11], rowString);
								    	hm.put(row[0], hm_construct);
							    	}
							    	
							    	
							    }else{
							    	
							    	rowString = row[0]+"_"+row[2]+"_"+row[3]+"_"+row[4]+"_"+row[5]+"_"+row[6].substring(0, row[6].length() - 1)+"_"+row[7]+"_"+row[11];
							    	hm_construct.put(row[11], rowString);
							    	hm.put(row[0], hm_construct);
							    	
							    }
							}
						}
					}
				}else{
					emailId = URLEncoder.encode(emailId,"UTF-8"); //No I18N
					adwordsReportURL = GadgetsURL+"/api/google/v1/adwords/downloadreport?zeroimpression=false&zservice=ZohoPageSense&type=adpr&response_type=gadgets&clientId="+clientId+"&mailId="+emailId+"&sdate="+start_date_adwords+"&edate="+end_date_adwords+"&fields="+URLEncoder.encode(fieldArray.toString(),"UTF-8"); //No I18N
					String responseStr = ZABUtil.getResponseStrFromURL(adwordsReportURL,accessToken);
					String report[] = responseStr.split("\n");
					int strLen = report.length;
			
					for(int i=2;i<strLen;i++){
						String row[] = report[i].split(",");
						if(i==2){
							Currency = row[9];
							Currency = getCurrencySymbol(Currency);
						}
						
						url = URLDecoder.decode(url,"UTF-8"); //No I18N
						
						String currentURL = row[8];
						if(currentURL.contains("www.")){
							String currentURL1[] = currentURL.split("\"\\[\"\"");
							currentURL = currentURL1[1];
							String currentURL2[] = currentURL.split("\"\"\\]\"");
							currentURL = currentURL2[0];
						}else{
							currentURL = "http://google.com"; //No I18N
						}
						
						
						Boolean urlMatch = Experiment.urlMatches(url, currentURL, matchTypeId);
						
						/*if(row[8] != null && row[8].contains(url)){ */
						if(urlMatch){
							
							HashMap hm_construct = new HashMap<String,String>();
						    if(hm.containsKey(row[0])){
						    	
						    	hm_construct = hm.get(row[0]);  
						    	if(hm_construct.containsKey(row[11])){
						    		
						    		rowString = (String)hm_construct.get(row[11]);
							    	String dataString[] = rowString.split("_");
							    	campaignname = dataString[0];
							    	adgroupname = dataString[7];
							    	click = Integer.parseInt(dataString[1])+Integer.parseInt(row[2]); 
							    	cost = Long.parseLong(dataString[2])+Long.parseLong(row[3]); 
							    	impression = Integer.parseInt(dataString[3])+Integer.parseInt(row[4]); 
							    	conversion = Double.parseDouble(dataString[4])+Double.parseDouble(row[5]); 
							    	ctr = Double.parseDouble(dataString[5])+Double.parseDouble(row[6].substring(0, row[6].length() - 1)); 
							    	cpc = Integer.parseInt(dataString[6])+Integer.parseInt(row[7]); 
							    	
							    	rowString = campaignname+"_"+click+"_"+cost+"_"+impression+"_"+conversion+"_"+ctr+"_"+cpc+"_"+adgroupname;
							    	hm_construct.put(row[11], rowString);
							    	hm.put(row[0], hm_construct);
							    	
						    	}else{
						    		
							    	//rowString = campaignname+"_"+click+"_"+cost+"_"+impression+"_"+conversion+"_"+ctr+"_"+cpc+"_"+adgroupname;
						    		rowString = row[0]+"_"+row[2]+"_"+row[3]+"_"+row[4]+"_"+row[5]+"_"+row[6].substring(0, row[6].length() - 1)+"_"+row[7]+"_"+row[11];
							    	hm_construct.put(row[11], rowString);
							    	hm.put(row[0], hm_construct);
						    	}
						    	
						    	
						    }else{
						    	
						    	rowString = row[0]+"_"+row[2]+"_"+row[3]+"_"+row[4]+"_"+row[5]+"_"+row[6].substring(0, row[6].length() - 1)+"_"+row[7]+"_"+row[11];
						    	hm_construct.put(row[11], rowString);
						    	hm.put(row[0], hm_construct);
						    	
						    }
						}
					}
				}
				
				
				for(Map.Entry<String, HashMap<String, String>> campaignEntry:hm.entrySet())
				{
					GoogleAdwords google = new GoogleAdwords();
					HashMap<String, String> adgroupEntry = campaignEntry.getValue();
					Iterator it = adgroupEntry.entrySet().iterator();
					String campaignName = campaignEntry.getKey();
					HashMap hm_adg = new HashMap<String,JSONObject>();
					JSONArray jarray = new JSONArray();
					JSONObject json_adgroup = new JSONObject();
					
					
					Integer campaign_clicks = 0;
					Integer campaign_googleclicks = 0;
					Integer campaign_conversions = 0;
					Double campaign_googleconversions = 0.0;
					Integer campaign_impressions = 0;
					Double campaign_costs = 0.0;
					Double campaign_ctr = 0.0;
					Double campaign_cpc = 0.0;
					
					while (it.hasNext()) {
						json_adgroup = new JSONObject();
				        Map.Entry pair = (Map.Entry)it.next();
				        String dataString[] = pair.getValue().toString().split("_");
				        
				        campaign_googleclicks += Integer.parseInt(dataString[1]);
	        			campaign_googleconversions += Double.parseDouble(dataString[4]);
	        			campaign_impressions += Integer.parseInt(dataString[3]);
				        
				        json_adgroup.put(IntegrationConstants.GOOGLECLICKS, dataString[1]);
	        			json_adgroup.put(IntegrationConstants.GOOGLECONVERSIONS, dataString[4]);
	        			json_adgroup.put(IntegrationConstants.CAMPAIGN_NAME, dataString[7]);
	        			json_adgroup.put(IntegrationConstants.IMPRESSIONS, dataString[3]);
	        			if(dataString[2] != null && !dataString[2].equals("0")){
	        				json_adgroup.put(IntegrationConstants.COSTS, String.format("%.2f",((Double.parseDouble(dataString[2]))/1000000)));
	        				campaign_costs += ((Double.parseDouble(dataString[2]))/1000000);
	        			}else{
	        				json_adgroup.put(IntegrationConstants.COSTS, 0);
	        			}
	        			if(dataString[3] != null && !dataString[3].equals("0")){
	        				json_adgroup.put(IntegrationConstants.CTR, String.format("%.2f",(Double.parseDouble(dataString[1])/Double.parseDouble(dataString[3]))*100));
	        				campaign_ctr += ((Double.parseDouble(dataString[1])/Double.parseDouble(dataString[3]))*100);
	        			}else{
	        				json_adgroup.put(IntegrationConstants.CTR, 0);
	        			}
				        
	        			json_adgroup.put(IntegrationConstants.CLICKS, 0);
	        			json_adgroup.put(IntegrationConstants.CONVERSIONS, 0);
	        			json_adgroup.put(IntegrationConstants.CPC, 0);
				        if(hashMap.get(dataString[0]) != null ){				        	
				        	hm_adg = hashMap.get(dataString[0]);
			        		if(hm_adg.get(dataString[7]) != null){
			        			JSONObject json = (JSONObject)hm_adg.get(dataString[7]);
			        			
			        			
			        			campaign_clicks += Integer.parseInt(json.getString("clicks"));
//			        			campaign_googleclicks += Integer.parseInt(dataString[1]);
			        			campaign_conversions += Integer.parseInt(json.getString("conversions"));
//			        			campaign_googleconversions += Double.parseDouble(dataString[4]);
//			        			campaign_impressions += Integer.parseInt(dataString[3]);
			        			
			        			
//			        			json_adgroup.put(IntegrationConstants.GOOGLECLICKS, dataString[1]);
//			        			json_adgroup.put(IntegrationConstants.GOOGLECONVERSIONS, dataString[4]);
			        			json_adgroup.put(IntegrationConstants.CLICKS, Integer.parseInt(json.getString("clicks")));
			        			json_adgroup.put(IntegrationConstants.CONVERSIONS, Integer.parseInt(json.getString("conversions")));
//			        			json_adgroup.put(IntegrationConstants.CAMPAIGN_NAME, dataString[7]);
//			        			json_adgroup.put(IntegrationConstants.IMPRESSIONS, dataString[3]);
//			        			if(dataString[2] != null && !dataString[2].equals("0")){
//			        				json_adgroup.put(IntegrationConstants.COSTS, String.format("%.2f",((Double.parseDouble(dataString[2]))/1000000)));
//			        				campaign_costs += ((Double.parseDouble(dataString[2]))/1000000);
//			        			}else{
//			        				json_adgroup.put(IntegrationConstants.COSTS, "0");
//			        			}
//			        			if(dataString[3] != null && !dataString[3].equals("0")){
//			        				json_adgroup.put(IntegrationConstants.CTR, String.format("%.2f",(Double.parseDouble(dataString[1])/Double.parseDouble(dataString[3]))*100));
//			        				campaign_ctr += ((Double.parseDouble(dataString[1])/Double.parseDouble(dataString[3]))*100);
//			        			}else{
//			        				json_adgroup.put(IntegrationConstants.CTR, "0");
//			        			}
			        			if(!dataString[1].equals("0") && !json.getString("conversions").equals("0")){
			        				json_adgroup.put(IntegrationConstants.CPC, String.format("%.2f",(Double.parseDouble(dataString[2])/Double.parseDouble(json.getString("conversions")))/1000000));
			        				campaign_cpc += ((Double.parseDouble(dataString[2])/Double.parseDouble(json.getString("conversions")))/1000000);
			        			}else{
			        				json_adgroup.put(IntegrationConstants.CPC, 0);
			        			}
				        	}
				        }
				        if(json_adgroup.length() > 0){
				        	jarray.put(json_adgroup);
				        }
					}
					
					if(jarray.length() > 0){
						google.setCampaignName(campaignName);
						google.setClicks(campaign_clicks);
						google.setGoogleClicks(campaign_googleclicks);
						google.setConversions(campaign_conversions);
						google.setGoogleConversions(campaign_googleconversions);
						//google.setCosts(String.format("%.2f",(campaign_costs))); //No I18N
						google.setCosts(campaign_costs);
						google.setCTR(String.format("%.2f",(campaign_ctr))); //No I18N
						if(campaign_conversions != 0 && campaign_costs != 0){
							DecimalFormat df2 = new DecimalFormat(".##");
					        String numberAsString = df2.format(Double.parseDouble(campaign_costs.toString())/(Double.parseDouble(campaign_conversions.toString())));
							Double costperclick = Double.parseDouble(numberAsString);
							google.setCPC(costperclick);
						}else{
							google.setCPC(0.00);
						}
						google.setImpressions(campaign_impressions);
						google.setCurrency(Currency);
						google.setSuccess(Boolean.TRUE);
						google.setAdGroupArray(jarray);
						googleAdword.add(google);
					}
					
			    }
				
			}
		}catch(Exception ex){
			LOGGER.log(Level.SEVERE, "getGoogleAdwordsReport "+ex.getMessage(),ex);
		}
		
		return googleAdword;
	}
	
     
	public static GoogleAdwords deleteGoogleAdwords(String projectId) {
		// TODO Auto-generated method stub
		GoogleAdwords googleadwords=null;
		HttpURLConnection httpCon = null;
		try {
			
			//Long projectId = (Long.parseLong(hs.get(IntegrationConstants.PROJECT_ID)));
			Project proj = Project.getProjectByProjectId(Long.parseLong(projectId));
			String project_link_name = proj.getProjectLinkName();
			ArrayList<GoogleAdwords> gadwords = GoogleAdwords.getGoogleAdwords(projectId.toString());
			String emailId = gadwords.get(0).getEmailId();
			emailId = URLEncoder.encode(emailId,"UTF-8"); //No I18N
			String GadgetsURL = ApplicationProperty.getString("com.abtest.gadwords.serverurl");
			URL url = new URL(GadgetsURL+"/api/google/v1/action/auth/revoke?ticket="+IAMUtil.getCurrentTicket()+"&zservice=ZohoPageSense&authScopes=GADWORDS.ALL&mailId="+emailId);  //No I18N
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
			httpCon.setRequestMethod("DELETE");
			httpCon.connect();
			int responseCode = httpCon.getResponseCode();
			
			if(responseCode == 200){
				
				Criteria c = new Criteria(new Column(GOOGLE_ADWORDS_DETAILS.TABLE, GOOGLE_ADWORDS_DETAILS.PROJECT_ID), projectId, QueryConstants.EQUAL);
				DataObject dobj = getRow(GOOGLE_ADWORDS_DETAILS.TABLE, c);
				if(dobj.containsTable(GOOGLE_ADWORDS_DETAILS.TABLE)) {
					Row row = dobj.getFirstRow(GOOGLE_ADWORDS_DETAILS.TABLE);
					googleadwords = getGoogleAdwordsFromRow(row);
					String refreshToken = (String)row.get(GOOGLE_ADWORDS_DETAILS.OAUTH_TOKEN);
					String accountsURL = IAMProxy.getIAMServerURL();
					URL url1 = new URL(accountsURL+"/oauth/v2/token/revoke?token="+refreshToken);  //No I18N
					httpCon = (HttpURLConnection) url1.openConnection();
					httpCon.setDoOutput(true);
					httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" ); //No I18N
					httpCon.setRequestMethod("POST"); //No I18N
					httpCon.connect();
					int responseCode1 = httpCon.getResponseCode();
					if(responseCode1 == 200){
						deleteResource(row);
						Integration.deleteIntegration("4", project_link_name, "");
					}
					
					
				} else {
					throw new ResourceNotFoundException(AudienceConstants.API_MODULE);
				}
				
				
			}
			
		}  catch (Exception e) {
			googleadwords = new GoogleAdwords();
			googleadwords.setSuccess(Boolean.FALSE);
			googleadwords.setResponseString(e.getMessage());
			LOGGER.log(Level.SEVERE, "!!!!!!!!!!!  deleteGoogleAdwords "+e.getMessage(),e);
		}
		finally{
			try {
				if(httpCon!=null) {					
					httpCon.disconnect();				
				}
			} catch (Exception e2) {
				LOGGER.log(Level.SEVERE, "!!!!!!!!!!!  deleteGoogleAdwords "+e2.getMessage(),e2);
			}
		}
		return googleadwords;
	}
	
	public static Boolean isGAdwordsEnabled(String project_link_name){
		Boolean isGAdwordsEnabled = true;
		Long projectId = Project.getProjectId(project_link_name);
		try{
			Criteria c=new Criteria(new Column(GOOGLE_ADWORDS_DETAILS.TABLE,GOOGLE_ADWORDS_DETAILS.PROJECT_ID),projectId,QueryConstants.EQUAL);
			DataObject dobj = ZABModel.getRow(GOOGLE_ADWORDS_DETAILS.TABLE, c);
			ArrayList<GoogleAdwords> googleAdwordsRes =getGoogleAdwordsFromDobj(dobj);
			if(googleAdwordsRes!=null && !googleAdwordsRes.isEmpty()) {
				isGAdwordsEnabled = true;
			}else{
				isGAdwordsEnabled = false;
			}
	   }
	   catch(Exception e){
        	LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
		return isGAdwordsEnabled;
	}
	
	public static HashMap<String,HashMap<String,JSONObject>> populateGCLIDValues(List<String> gclidList, String domainName)
	{
		
		HashMap<String,HashMap<String,JSONObject>> hashMap = new HashMap<String,HashMap<String,JSONObject>>();
		try
		{
			String indexName = ElasticSearchUtil.getIndexByPortal(domainName);
			
			//Query
			BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();
			finalQuery.filter().add(QueryBuilders.termsQuery("gclid", gclidList)); //No I18N
			
			//Aggregations
			TermsAggregationBuilder campaignNameAggr = AggregationBuilders.terms("campaignnames").field(ElasticSearchConstants.CAMPAIGN_NAME).size(ElasticSearchConstants.INTEGER_MAX_COUNT); //No I18N
			TermsAggregationBuilder adgroupNameAggr = AggregationBuilders.terms("adgroupnames").field(ElasticSearchConstants.ADGROUP_NAME).size(ElasticSearchConstants.INTEGER_MAX_COUNT); //No I18N
			int precisionThreshold = DynamicConfigurationConstants.ES_CARDINALITY_THRESHOLD_VALUE;
			String thresholdStr = DynamicConfigurationUtil.getDynamicPropertyValueByName(DynamicConfigurationConstants.ES_CARDINALITY_THRESHOLD);
			if(StringUtils.isNotEmpty(thresholdStr))
			{
				precisionThreshold = Integer.parseInt(thresholdStr);
			}
			CardinalityAggregationBuilder cardinalityAggr = AggregationBuilders.cardinality("gclidcount").field(ElasticSearchConstants.GCLID).precisionThreshold(precisionThreshold); //No I18N
			AggregationBuilder finalAggr = campaignNameAggr.subAggregation(adgroupNameAggr.subAggregation(cardinalityAggr).size(Integer.MAX_VALUE));
			
			SearchResponse response = ElasticSearchUtil.getData(indexName, ElasticSearchConstants.ADWORDS_RAW_TYPE, 0, finalQuery, finalAggr);
			
			Aggregations aggrResponse = response.getAggregations();
			Terms terms = aggrResponse.get("campaignnames");
			List<? extends Bucket> buckets = terms.getBuckets();
			for(Bucket bucket:buckets)
			{
				String campaingname = (String)bucket.getKey();
				Aggregations buckaggrResponse = bucket.getAggregations();
				InternalCardinality cardinality = buckaggrResponse.get("gclidcount");
				Terms expterms = buckaggrResponse.get("adgroupnames");
				List<? extends Bucket> expbuckets = expterms.getBuckets();
				HashMap<String,JSONObject> hashMap1 = new HashMap<String,JSONObject>();
				for(Bucket expbucket:expbuckets)
				{
					String adgroupname = (String)expbucket.getKey();
					Aggregations expaggrResponse = expbucket.getAggregations();
					InternalCardinality cardinality1 = expaggrResponse.get("gclidcount");
					Long gclidcount = cardinality1.getValue();
					JSONObject jb = new JSONObject();
					jb.put("count",gclidcount);
					hashMap1.put(adgroupname, jb);
				}
				hashMap.put(campaingname, hashMap1);
			}
			
		}
		catch(Exception e){
        	LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
		return hashMap;
	}
	
	public static HashMap<String,HashMap<String,JSONObject>> getGclidVisitConversion(HashMap<String,String> hs,String projectId) {
		
		HashMap<String,HashMap<String,JSONObject>> hashMap = new HashMap<String,HashMap<String,JSONObject>>();
		try{
			
			String portalName = ZABUtil.getPortalName();
			
			String indexName = ElasticSearchUtil.getIndexByPortal(portalName);
			HashMap<String,String> hs1 = new HashMap<String,String>();
			
			String startTime = null;
			String endTime = null;
			
			if(hs.containsKey(HeatmapConstants.START_DATE.toLowerCase())){
				startTime = hs.get(HeatmapConstants.START_DATE.toLowerCase());
			}
			
			if(hs.containsKey(HeatmapConstants.END_DATE.toLowerCase())){
				endTime  = hs.get(HeatmapConstants.END_DATE.toLowerCase());
			}
			
			String moduleType = hs.get(IntegrationConstants.MODULE_TYPE);
			String reportType = hs.get(IntegrationConstants.REPORT_TYPE);
			Long startTimeInMillis = null;
			if(startTime!=null&&!startTime.isEmpty()){
				 startTimeInMillis = ZABUtil.getTimeInLongFromDateFormStr(startTime, "yyyy-MM-dd");		// NO I18N
			}
			
			Long endTimeInMillis = null;
			if(endTime!=null&&!endTime.isEmpty()){
				endTimeInMillis = ZABUtil.getTimeInLongFromDateFormStr(endTime, "yyyy-MM-dd");		// NO I18N
				endTimeInMillis = ZABUtil.getNthDayDateInLong(endTimeInMillis, 1);
			}else{
				endTimeInMillis = ZABUtil.getCurrentTimeInMilliSeconds();
			}
			
			Long experimentId = 0L;
			String experimentLinkName = "";
			
			String queryParamKey = ElasticSearchConstants.GCLID;
			String multiSegmentCriteria = hs.get(FunnelReportConstants.MULTISEGMENT_CRITERIA); 
			if(moduleType != null && (moduleType.equals("6") || (moduleType.equals("7")))){
				experimentId=Long.parseLong(hs.get(IntegrationConstants.EXPERIMENT_ID));
				experimentLinkName = Experiment.getExperimentLinkname(experimentId);
			}
			
			String goalLinkName = hs.get(GoalConstants.GOAL_LINK_NAME);
			
			String domainName = ZABUtil.getPortaldomain();
			
			ArrayList<String> all_gclids = new ArrayList<String>();
			ArrayList<String> converted_gclids = new ArrayList<String>();
			
			if(moduleType != null && moduleType.equals("6")){
				
				all_gclids = FunnelFlexible.getAllVisitorswithQPKey(queryParamKey, multiSegmentCriteria, startTimeInMillis, endTimeInMillis, experimentLinkName);
				converted_gclids = FunnelFlexible.getConvertedWithQPKey(queryParamKey, multiSegmentCriteria, startTimeInMillis, endTimeInMillis, experimentLinkName);
			
			}else if(moduleType != null && moduleType.equals("7")){
				
				all_gclids = getAllFormsVisitors(multiSegmentCriteria, startTimeInMillis, endTimeInMillis, experimentLinkName, indexName, portalName, projectId, reportType);
				converted_gclids = getConvertedFormsVisitors(multiSegmentCriteria, startTimeInMillis, endTimeInMillis, experimentLinkName, indexName, portalName, projectId, reportType);
			
			}else if(moduleType != null && moduleType.equals("10")){
				
				all_gclids = GoalAdwords.getAllGoalVisitors(multiSegmentCriteria, startTime, endTime, goalLinkName, indexName, portalName, projectId);
				converted_gclids = GoalAdwords.getAllGoalConversions(multiSegmentCriteria, startTime, endTime, goalLinkName, indexName, portalName, projectId);
			
			}
			
			if(all_gclids!=null){
				int all_gclids_count = all_gclids.size();
				LOGGER.info("!!!!!!!! ALL GCLID Count "+all_gclids_count);
			}else{
				LOGGER.info("!!!!!!!! ALL GCLID Count ZERO");
			}
			if(converted_gclids!=null){
				int converted_gclids_count = converted_gclids.size();
				LOGGER.info("!!!!!!!! CONVERTED GCLID Count "+converted_gclids_count);
			}else{
				LOGGER.info("!!!!!!!! ALL GCLID Count ZERO");
			}
			
			LOGGER.info("!!!!!!!! DOMAINNAME "+domainName);
			
			if(all_gclids!=null){
				HashMap<String,HashMap<String,JSONObject>> hashMap_allgclids = populateGCLIDValues(all_gclids, domainName);
				HashMap<String,HashMap<String,JSONObject>> hashMap_convertedgclids = populateGCLIDValues(converted_gclids, domainName);
				
				hashMap = mergeClickConversionHashMap(hashMap_allgclids,hashMap_convertedgclids);
				
				LOGGER.info("!!!!!!!! populateGCLIDValues_hashMap_allgclids "+hashMap_allgclids);
				LOGGER.info("!!!!!!!! populateGCLIDValues_hashMap_convertedgclids "+hashMap_convertedgclids);
				LOGGER.info("!!!!!!!! mergeClickConversionHashMap "+hashMap);
			}
			
			
//			BoolQueryBuilder query = null;
//			String experimentLinkName = Experiment.getExperimentLinkname(experimentId);
//			FunnelAnalysis analysis = FunnelAnalysis.getFunnelAnalysisWithSteps(experimentLinkName);
//			if(hs.containsKey(FunnelReportConstants.MULTISEGMENT_CRITERIA)) {
//				 query = (BoolQueryBuilder)FunnelFlexible.generateMultiSegmentCriteria(hs.get(FunnelReportConstants.MULTISEGMENT_CRITERIA), startTimeInMillis, endTimeInMillis, analysis);
//			}else{
//				query =  (BoolQueryBuilder)FunnelFlexible.generateMultiSegmentCriteria("{\"condition_type\":1,\"conditions\":[]}", startTimeInMillis, endTimeInMillis, analysis); //No I18N
//			}
//			
//			List<AggregationBuilder> aggregation = generateFunnelGclidAggregationJson();
//			
//			BoolQueryBuilder bool = null;
//			if(hs.containsKey(FunnelReportConstants.MULTISEGMENT_CRITERIA)) {
//				bool = FunnelFlexible.getGCLIDVisitors(hs.get(FunnelReportConstants.MULTISEGMENT_CRITERIA), startTimeInMillis, endTimeInMillis, analysis); 
//			}else{
//				bool = FunnelFlexible.getGCLIDVisitors("{\"condition_type\":1,\"conditions\":[]}", startTimeInMillis, endTimeInMillis, analysis); //No I18N
//			}
//			
//
//			SearchResponse response = ElasticSearchUtil.getData(indexName,type, 0, bool, aggregation);
//			hashMap =  readGclidResponseData(response,hs1,projectId,experimentId, startTimeInMillis, endTimeInMillis, analysis,hs);
			} 
		catch(Exception e){
			LOGGER.log(Level.SEVERE,e.getMessage(),e);
			
		}
		return hashMap;
	}
	
		public static HashMap<String,HashMap<String,JSONObject>> mergeClickConversionHashMap(HashMap<String,HashMap<String,JSONObject>> hashMap_allgclids, HashMap<String,HashMap<String,JSONObject>> hashMap_convertedgclids) throws JSONException{
			
			HashMap<String,HashMap<String,JSONObject>> hashMap = new HashMap<String,HashMap<String,JSONObject>>();
			
			try{
				if(!hashMap_allgclids.isEmpty()){
					
					Iterator it = hashMap_allgclids.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        String campaignName = (String) pair.getKey();
				        if(hashMap_convertedgclids.get(campaignName) != null){
				        	
				        	HashMap<String,JSONObject> hm_conv = hashMap_convertedgclids.get(campaignName);
				        	HashMap<String,JSONObject> hm_all = hashMap_allgclids.get(campaignName);
				        	Iterator it1 = hm_all.entrySet().iterator();
						    while (it1.hasNext()) {
						    	Map.Entry pair1 = (Map.Entry)it1.next();
						    	String adgroupname = (String) pair1.getKey();
						    	
						    	if(hm_conv.get(adgroupname) != null){
						    		
						    		JSONObject jb_conv = hm_conv.get(adgroupname);
						    		JSONObject jb_all = hm_all.get(adgroupname);
							    	jb_all.put("clicks",jb_all.get("count"));
							    	jb_all.put("conversions", jb_conv.get("count"));
							    	jb_all.remove("count"); //No I18N
							    	
							    	hm_all.put(adgroupname, jb_all);	
						    	}else{	
						    		JSONObject jb_all = hm_all.get(adgroupname);
							    	jb_all.put("clicks",jb_all.get("count"));
							    	jb_all.put("conversions", "0");
							    	jb_all.remove("count"); //No I18N
							    	
							    	hm_all.put(adgroupname, jb_all);
						    	}
						    	
						    }
						    hashMap.put(campaignName, hm_all);
						    
				        }else{
				        	
				        	HashMap<String,JSONObject> hm_all = hashMap_allgclids.get(campaignName);
				        	Iterator it1 = hm_all.entrySet().iterator();
				        	while (it1.hasNext()) {
				        		Map.Entry pair1 = (Map.Entry)it1.next();
				        		String adgroupname = (String) pair1.getKey();
				        		JSONObject jb_all = hm_all.get(adgroupname);
				        		jb_all.put("clicks",jb_all.get("count"));
						    	jb_all.put("conversions", "0");
						    	jb_all.remove("count"); //No I18N
						    	hm_all.put(adgroupname, jb_all);
				        	}
				        	hashMap.put(campaignName, hm_all);
				        }
				        
				    }
				}
			}catch(Exception e){
				LOGGER.log(Level.SEVERE,e.getMessage(),e);
			}
			
			return hashMap;
		}
		

		public static List<AggregationBuilder> generateGclidAggregationJson() {
			List<AggregationBuilder>  ab = new ArrayList<AggregationBuilder>();
			AggregationBuilder  gclid = AggregationBuilders.terms(ElasticSearchConstants.GCLID).field(ElasticSearchConstants.GCLID).size(ElasticSearchConstants.INTEGER_MAX_COUNT);
			ab.add(gclid);
			return ab;
		}
		
		public static List<AggregationBuilder> generateFunnelGclidAggregationJson() {
			List<AggregationBuilder>  ab = new ArrayList<AggregationBuilder>();
			AggregationBuilder  gclid = AggregationBuilders.nested("gclid", "steps.nurlparameter") //No I18N
				.subAggregation(
						AggregationBuilders.terms("param_gclid").field("steps.nurlparameter.value").size(ElasticSearchConstants.SEGMENT_MAX_COUNT)); //No I18N
		
			ab.add(gclid);
			return ab;
		}
		
		public static HashMap<String,HashMap<String,JSONObject>> readGclidResponseData(SearchResponse response, HashMap<String, String> gclidHs,String projectId,Long experimentId, Long startTimeInMillis, Long endTimeInMillis, FunnelAnalysis analysis, HashMap<String,String> hs) {
			//TODO: Modified only to support funnel
			//Please check it.
			
			Nested gclidAggr = response.getAggregations().get("gclid"); //No I18N
			Terms gclid = gclidAggr.getAggregations().get("param_gclid"); //No I18N
			for(Bucket bucket : gclid.getBuckets())
			{
			String gclidValue = (String)bucket.getKey();
			String count = Long.toString(bucket.getDocCount());
			gclidHs.put(gclidValue, count);
			}
			HashMap<String,HashMap<String,JSONObject>> hashMap = new HashMap<String,HashMap<String,JSONObject>>();
			try{
			String portalName = ZABUtil.getPortalName();
			String indexName = ElasticSearchUtil.getIndexByPortal(portalName);
			List<AggregationBuilder> aggregation = null;	
			Iterator it = gclidHs.entrySet().iterator();
			
			
			// loop for all gclids in funnel exp
		    while (it.hasNext()) {
		    	HashMap<String,JSONObject> hm = new HashMap<String,JSONObject>();
		    	Map.Entry entry = (Map.Entry) it.next();
		    	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		    	MatchQueryBuilder portal = QueryBuilders.matchQuery("portal",portalName); //No I18N
		    	MatchQueryBuilder proId = QueryBuilders.matchQuery("projectid",projectId);//No I18N
//		    	MatchQueryBuilder gclidBuilder = QueryBuilders.matchQuery("gclid",entry.getKey());//No I18N
		    	
		    	MatchQueryBuilder gclidBuilder = QueryBuilders.matchQuery("gclid",entry.getKey());//No I18N
		    	boolQuery.must(proId);
		    	boolQuery.must(gclidBuilder);
		    	boolQuery.must(portal);
		    	// get corresponding campaign name for gclid
		    	AggregationBuilder aggr = null;
				SearchResponse response1 = ElasticSearchUtil.getData(indexName,"adwordsrawdata",0,(QueryBuilder)boolQuery,aggr);//No I18N
				JSONObject json = new JSONObject();
				SearchHits sHits = response1.getHits();
				SearchHit hitArr[] = sHits.getHits();
				String campaignname = null;
				String adgroupname = null;
				if(hitArr.length > 0)  //VISIT OR CLICK
				{
					SearchHit hit = hitArr[0];
					JSONObject value = new JSONObject(hit.getSource());
						campaignname = value.getString("campaignname");
						adgroupname = value.getString("adgroupname");
//					docId = hit.getId();
						
				if(hashMap.get(campaignname) != null){
					hm = hashMap.get(campaignname);
					if(hm.get(adgroupname) != null){
						
						json = hm.get(adgroupname);
						Integer clk = (Integer.parseInt(json.getString("clicks")));
						Integer conv = (Integer.parseInt(json.getString("conversions")));
						clk++;
						
						json.put("clicks",clk);	//gclidHs.get(entry.getKey())
						BoolQueryBuilder bool1 = null;
						 
						if(hs.containsKey(FunnelReportConstants.MULTISEGMENT_CRITERIA)) {
							bool1 = FunnelFlexible.getGCLIDConversions(hs.get(FunnelReportConstants.MULTISEGMENT_CRITERIA), startTimeInMillis, endTimeInMillis, analysis, (String) entry.getKey()); 
						}else{
							bool1 = FunnelFlexible.getGCLIDConversions("{\"condition_type\":1,\"conditions\":[]}", startTimeInMillis, endTimeInMillis, analysis, (String) entry.getKey()); //No I18N
						}
						//bool1.must(gclidBuilder);
						SearchResponse response2 = ElasticSearchUtil.getData(indexName,ElasticSearchConstants.FUNNEL_RAW_TYPE, bool1);
						SearchHits conversionsHits = response2.getHits();
						SearchHit conhitArr[] = conversionsHits.getHits();
						if(conhitArr.length > 0)  //CONVERSION 
						{
							conv++;
							json.put("conversions",conv);
						}else{
							json.put("conversions",conv);
						}
						hm.put(adgroupname, json);
						hashMap.put(campaignname, hm);
					}else{
						json.put("clicks","1");	//gclidHs.get(entry.getKey())
						BoolQueryBuilder bool1 = null;
						
						if(hs.containsKey(FunnelReportConstants.MULTISEGMENT_CRITERIA)) {
							bool1 = FunnelFlexible.getGCLIDConversions(hs.get(FunnelReportConstants.MULTISEGMENT_CRITERIA), startTimeInMillis, endTimeInMillis, analysis, (String) entry.getKey());
						}else{
							bool1 = FunnelFlexible.getGCLIDConversions("{\"condition_type\":1,\"conditions\":[]}", startTimeInMillis, endTimeInMillis, analysis, (String) entry.getKey()); //No I18N
						}

						//bool1.must(gclidBuilder);
						SearchResponse response2 = ElasticSearchUtil.getData(indexName,ElasticSearchConstants.FUNNEL_RAW_TYPE, bool1);
						SearchHits conversionsHits = response2.getHits();
						SearchHit conhitArr[] = conversionsHits.getHits();
						if(conhitArr.length > 0)
						{
							json.put("conversions","1");
						}else{
							json.put("conversions","0");
						}
						
						hm.put(adgroupname, json);
						hashMap.put(campaignname, hm);
					}
					
				}else{
					json.put("clicks","1");	//gclidHs.get(entry.getKey())
					
					BoolQueryBuilder bool1 = null;
					
					if(hs.containsKey(FunnelReportConstants.MULTISEGMENT_CRITERIA)) {
						bool1 = FunnelFlexible.getGCLIDConversions(hs.get(FunnelReportConstants.MULTISEGMENT_CRITERIA), startTimeInMillis, endTimeInMillis, analysis, (String) entry.getKey());
					}else{
						bool1 = FunnelFlexible.getGCLIDConversions("{\"condition_type\":1,\"conditions\":[]}", startTimeInMillis, endTimeInMillis, analysis, (String) entry.getKey()); //No I18N
					}
					

//					bool1.must(gclidBuilder);
					SearchResponse response2 = ElasticSearchUtil.getData(indexName,ElasticSearchConstants.FUNNEL_RAW_TYPE, bool1);
					SearchHits conversionsHits = response2.getHits();
					SearchHit conhitArr[] = conversionsHits.getHits();
					if(conhitArr.length > 0)
					{
						json.put("conversions","1");
					}else{
						json.put("conversions","0");
					}
					hm.put(adgroupname,json);
					hashMap.put(campaignname, hm);
				}
						 	
					}
			    }
			    
				}
				catch(Exception e) {
					LOGGER.log(Level.SEVERE,"readGclidResponseData "+e.getMessage(),e);
				}
				return hashMap;
			}
		
		public static ArrayList<Long> getFunnelAnalysisMeta(String experimentLinkname) {
			
			FunnelAnalysis funnelAnalysis = new FunnelAnalysis();
			ArrayList<Long> order = null;
			try {			
				
				
				Integer stepCount = 0;
				Criteria c1 = new Criteria(new Column(EXPERIMENT.TABLE, EXPERIMENT.EXPERIMENT_LINK_NAME), experimentLinkname, QueryConstants.EQUAL);
				Join join1 = new Join(EXPERIMENT.TABLE,FUNNEL_STEPS.TABLE,new String[]{EXPERIMENT.EXPERIMENT_ID},new String[]{FUNNEL_STEPS.EXPERIMENT_ID},Join.INNER_JOIN);
				SortColumn sort = new SortColumn(new Column(FUNNEL_STEPS.TABLE, FUNNEL_STEPS.STEP_ORDER), Boolean.TRUE);
				DataObject dobj = getRow(EXPERIMENT.TABLE, c1, sort, new Join[]{join1});
				
				if(dobj.containsTable(FUNNEL_STEPS.TABLE)) {
					 order = new ArrayList<Long>();
					Iterator it = dobj.getRows(FUNNEL_STEPS.TABLE);
					
					Integer experimentStatus = (Integer) dobj.getFirstRow(EXPERIMENT.TABLE).get(EXPERIMENT.EXPERIMENT_STATUS);
					Boolean mayHaveReport = (!experimentStatus.equals(ExperimentStatus.DRAFT.getStatusCode()));
					
					
					while(it.hasNext()) {
						Row row = (Row)it.next();
						Long stepId = (Long)row.get(FUNNEL_STEPS.STEP_ID);
						if(stepCount == 0 && mayHaveReport) {						
							//Setting conversion rate
							funnelAnalysis.setVisitorCount(FunnelFlexible.getTotalSession(stepId));
						}
						order.add(stepId);
						stepCount++;
					}
				}
					
				
				
			} catch (ZABException e) {
				funnelAnalysis = new FunnelAnalysis();
				funnelAnalysis.setSuccess(Boolean.FALSE);
				funnelAnalysis.setResponseString(e.getMessage());
				LOGGER.log(Level.SEVERE,"Exception Occurred",e);
			}
			catch (Exception e) {
				funnelAnalysis = new FunnelAnalysis();
				funnelAnalysis.setSuccess(Boolean.FALSE);
				funnelAnalysis.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
				LOGGER.log(Level.SEVERE,"Exception Occurred",e);
			}
			return order;
		}

		public static GoogleAdwords addGClidJobWithDate(String start_date){
			
			GoogleAdwords googleadwords = new GoogleAdwords();
			try{
				
			LOGGER.info("addGClidJobWithDate STARTS");
			int count = 0;
			ArrayList<Project> proj = Project.getProjects(); 
			String domainName = ZABUtil.getPortaldomain();
			ServiceOrg currentServiceOrg = IAMUtil.getCurrentServiceOrg();
			Long zsoid = currentServiceOrg.getZSOID();
			for(int i = 0; i<proj.size(); i++){
				Long projectId = proj.get(i).getProjectId();
				Criteria c = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),projectId, QueryConstants.EQUAL);
				Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),Integ.GOOGLEADWORDS.getIntegrationId(), QueryConstants.EQUAL);
				DataObject dobj = getRow(PROJECT_INTEGRATION.TABLE, c.and(c1));
				if(!dobj.isEmpty()){
					
					ArrayList<GoogleAdwords> gadwords = GoogleAdwords.getGoogleAdwords(projectId.toString());
					Long clientId = gadwords.get(0).getClientId();
					String emailId = gadwords.get(0).getEmailId();
					String oauthtoken = gadwords.get(0).getOAuthToken();
					Long oauthzuid = gadwords.get(0).getOAuthZUID();
					Boolean is_mcc = gadwords.get(0).getIsMCC();

					if(oauthtoken==null){
						oauthtoken = createRefreshToken();
						oauthzuid = IAMUtil.getCurrentUser().getZUID();
					}
					String accessToken = getAccessTokenfromRefreshToken(oauthtoken, oauthzuid);
					
					JSONArray fieldArray = new JSONArray();
					fieldArray.put("CampaignName");
					fieldArray.put("AdGroupName");
					fieldArray.put("GclId");
					//emailId = URLEncoder.encode(emailId,"UTF-8"); //NO I18N
					

					// Check for MCC account having more than one client_id
					if(accessToken != null && is_mcc != null){
						if(is_mcc){
							ArrayList<GoogleAdwords> ga = getAccountInfo(emailId,clientId.toString());
							for(int j=0; j<ga.size(); j++){
								clientId = ga.get(j).getClientId();
								pushgcliddata(emailId,clientId,accessToken,projectId,domainName,zsoid,start_date,fieldArray);
							}
						}else{
							pushgcliddata(emailId,clientId,accessToken,projectId,domainName,zsoid,start_date,fieldArray);
						}
					}
				}
				googleadwords.setSuccess(Boolean.TRUE);
			}
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "!!!!!!!!!!! addGClidJobWithDate "+ex.getMessage(),ex);
				googleadwords.setSuccess(Boolean.FALSE);
			} 
			return googleadwords;
		}
		
		public static String createRefreshToken(){
			
			String refreshToken = null;
			try{
				 String ipAddress = InetAddress.getLocalHost().getHostAddress();
				 String userAgent = IAMUtil.getCurrentRequest().getHeader("user-agent");
				 OAuthAPI oauthApi = IAMProxy.getInstance().getOAuthAPI();
				 String oauthClientId = ApplicationProperty.getString("com.abtest.authtoken.clientid");
				 Long zuid = IAMUtil.getCurrentUser().getZUID();
				 String ticket = IAMUtil.getCurrentTicket(); 
				 String[] scopes = {ApplicationProperty.getString("com.abtest.authtoken.scopename")};
				 String serviceName = ApplicationProperty.getString("com.abtest.authtoken.servicename");
				 refreshToken = oauthApi.generateUserOAuthRefreshTokenbyTicket(oauthClientId, zuid, ticket, scopes, serviceName, ipAddress, userAgent); 
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "!!!!!!!!! createRefreshToken "+ex.getMessage(),ex);
			}
			
			return refreshToken;
		}
		
		public static String getAccessTokenfromRefreshToken(String refreshtoken, Long oauthzuid){
				
				String accessToken = null;
				try{
					 String ipAddress = InetAddress.getLocalHost().getHostAddress();
					 String userAgent = null;
					 OAuthAPI oauthApi = IAMProxy.getInstance().getOAuthAPI();
					 String oauthClientId = ApplicationProperty.getString("com.abtest.authtoken.clientid");
					 String[] scopes = {ApplicationProperty.getString("com.abtest.authtoken.scopename")};
					 String serviceName = ApplicationProperty.getString("com.abtest.authtoken.servicename");
					 accessToken = oauthApi.generateInternalOAuthAccessToken(oauthClientId, oauthzuid, refreshtoken, serviceName, ipAddress, userAgent); 
				}catch(Exception ex){
					LOGGER.log(Level.SEVERE, "!!!!!!!!! getAccessTokenfromRefreshToken "+ex.getMessage(),ex);
				}
				
				return accessToken;
		}
		
		public static ArrayList<String> getAllFormsVisitors(String multiSegmentCriteria, Long startTimeInMillis, Long endTimeInMillis, String experimentLinkName, String indexName, String portal, String projectId, String reportType){
			
			SearchResponse response = null;
			Long expId = 0L;
			try{
				expId = Experiment.getExperimentId(experimentLinkName);
				QueryBuilder query = generateGCLIDQuery(portal,reportType,expId,startTimeInMillis,endTimeInMillis,multiSegmentCriteria);
				List<AggregationBuilder> cardinalityAggregation = generateGCLIDAggregation();
				response = ElasticSearchUtil.getData(indexName, ElasticSearchConstants.FORM_ANALYTICS_RAW_TYPE, 0, query, cardinalityAggregation);

			}
			catch(Exception ex){
				LOGGER.log(Level.SEVERE, "!!!!!!!!! getAllFormsVisitors "+ex.getMessage(),ex);
			}
			return readGCLIDresponse(response);
		}
		
		
		public static ArrayList<String> getConvertedFormsVisitors(String multiSegmentCriteria, Long startTimeInMillis, Long endTimeInMillis, String experimentLinkName, String indexName, String portal, String projectId, String reportType){
			
			SearchResponse response = null;
			Long expId = 0L;
			try{
				expId = Experiment.getExperimentId(experimentLinkName);
				BoolQueryBuilder query = generateGCLIDConversionQuery(portal,reportType,expId,startTimeInMillis,endTimeInMillis,multiSegmentCriteria);
				List<AggregationBuilder> cardinalityAggregation = generateGCLIDAggregation();
				response = ElasticSearchUtil.getData(indexName, ElasticSearchConstants.FORM_ANALYTICS_RAW_TYPE, 0, query, cardinalityAggregation);

			}
			catch(Exception ex){
				LOGGER.log(Level.SEVERE, "!!!!!!!!! getAllFormsVisitors "+ex.getMessage(),ex);
			}
			return readGCLIDresponse(response);
		}
		
		public static BoolQueryBuilder generateGCLIDQuery(String portal, String reportType, Long expId, Long startTime, Long endTime, String multisegmentCriteria)
		{
			BoolQueryBuilder boolQuery = null;
			
			try {
				
				if (reportType.equals(ReportConstants.SUMMARY)) {

					boolQuery = ElasticSearchStatistics.generateSourceQueryJson(
							portal, expId, startTime, endTime, false, null, false,
							null, null);

				}

				else if (reportType.equals(ReportConstants.MULTISEGMENT))

				{

					boolQuery = ElasticSearchStatistics
							.generateSourceMultiSegmentQueryJson(portal, expId,
									startTime, endTime, multisegmentCriteria,
									false, null, null, null);

				}

				MatchQueryBuilder paramNameQuery = QueryBuilders.matchQuery(
						"nurlparameter.name", "gclid"); // No I18N

				List<QueryBuilder> paramList = new ArrayList<QueryBuilder>();

				paramList.add(paramNameQuery);

				BoolQueryBuilder conditionQueries = QueryBuilders.boolQuery();

				conditionQueries.must().addAll(paramList);

				NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery(
						"nurlparameter", conditionQueries, ScoreMode.None);    //No I18N
																			

				boolQuery.must(nestedQuery);

			}

			catch (Exception ex)

			{

				LOGGER.log(Level.SEVERE, ex.getMessage(), ex);

			}

			return boolQuery;

		}

		public static BoolQueryBuilder generateGCLIDConversionQuery(String portal,
				String reportType, Long expId, Long startTime, Long endTime,
				String multisegmentCriteria)

		{

			BoolQueryBuilder boolQuery = null;

			try

			{

				if (reportType.equals(ReportConstants.SUMMARY))

				{

					boolQuery = ElasticSearchStatistics.generateSourceQueryJson(
							portal, expId, startTime, endTime, false, null, false,
							null, null);

				}

				else if (reportType.equals(ReportConstants.MULTISEGMENT))

				{

					boolQuery = ElasticSearchStatistics
							.generateSourceMultiSegmentQueryJson(portal, expId,
									startTime, endTime, multisegmentCriteria,
									false, null, null, null);

				}

				MatchQueryBuilder paramNameQuery = QueryBuilders.matchQuery(
						"nurlparameter.name", "gclid"); // No I18N

				MatchQueryBuilder paramNameQuery2 = QueryBuilders.matchQuery(
						"form_conversion", 1); // No I18N

				List<QueryBuilder> paramNameQueries = new ArrayList<QueryBuilder>();

				paramNameQueries.add(paramNameQuery);


				List<QueryBuilder> paramList = new ArrayList<QueryBuilder>();

				paramList.addAll(paramNameQueries);

				BoolQueryBuilder conditionQueries = QueryBuilders.boolQuery();

				conditionQueries.must().addAll(paramList);

				NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery(
						"nurlparameter", conditionQueries, ScoreMode.None);  //No I18N

				boolQuery.must(paramNameQuery2).must(nestedQuery);

			}

			catch (Exception ex)

			{

				LOGGER.log(Level.SEVERE, ex.getMessage(), ex);

			}

			return boolQuery;

		}

		public static List<AggregationBuilder> generateGCLIDAggregation() {

			List<AggregationBuilder> ab = new ArrayList<AggregationBuilder>();

			AggregationBuilder aggr = AggregationBuilders.nested("aggr",     //No I18N
					"nurlparameter") // No I18N

					.subAggregation(

							AggregationBuilders
									.terms("name")  //No I18N
									.field("nurlparameter.name")   //No I18N
									.subAggregation(
											AggregationBuilders.terms("value")   //No I18N
													.field("nurlparameter.value").size(Integer.MAX_VALUE)));   //No I18N
																						

			ab.add(aggr);

			return ab;

		}

		public static ArrayList<String> readGCLIDresponse(SearchResponse response) {

			ArrayList<String> array = new ArrayList<String>();
			Nested nested = response.getAggregations().get("aggr"); // No I18N
			Terms nameTerms = nested.getAggregations().get("name"); // No I18N
			HashMap<String,HashMap<String,JSONObject>> hashMap = new HashMap<String,HashMap<String,JSONObject>>();
			if (nameTerms.getBuckets().size() == 0) {
				return null;
			} else {
				List<? extends Bucket> nameBuckets = nameTerms.getBuckets();
				for (Bucket nameBucket : nameBuckets)
				{
					String name = (String) nameBucket.getKey();
					if (name.equals("gclid")) { // No I18N
						Terms valueTerms = nameBucket.getAggregations().get("value"); // No I18N
						List<? extends Bucket> valueBuckets = valueTerms.getBuckets();
						for (Bucket valueBucket : valueBuckets)
						{
							array.add(valueBucket.getKey().toString());
						}
					}
				}
			}

			
			return array;
		}

		public static List<AggregationBuilder> generateConversionAggregation() {

			List<AggregationBuilder> ab = new ArrayList<AggregationBuilder>();

			AggregationBuilder formConversionCount = AggregationBuilders.sum(
					FormReportConstants.FORM_CONVERSION_COUNT).field(
					FormRawDataConstants.FORM_CONVERSION);

			ab.add(formConversionCount);

			return ab;

		}
		
		public static String getCurrencySymbol(String currency){
			
			switch(currency){
				
				case "USD":
					currency = IntegrationConstants.US_DOLLARS;
					break;
				case "ARS":
					currency = IntegrationConstants.ARGENTINE_PESO;
					break;
				case "AUD":
					currency = IntegrationConstants.AUSTRALIAN_DOLLARS;
					break;
				case "BND":
					currency = IntegrationConstants.BRUNEI_DOLLAR;
					break;
				case "BRL":
					currency = IntegrationConstants.BRAZILIAN_REAL;
					break;
				case "CAD":
					currency = IntegrationConstants.CANADIAN_DOLLARS;
					break;
				case "CLP":
					currency = IntegrationConstants.CHILEAN_PESO;
					break;
				case "CNY":
					currency = IntegrationConstants.YUAN_RENMINBI;
					break;
				case "EUR":
					currency = IntegrationConstants.EUROS;
					break;
				case "FJD":
					currency = IntegrationConstants.FIJI_DOLLARS;
					break;
				case "GBP":
					currency = IntegrationConstants.BRITISH_POUND_STERLING;
					break;
				case "HKD":
					currency = IntegrationConstants.HONGKONG_DOLLARS;
					break;
				case "ILS":
					currency = IntegrationConstants.ISREALI_SHEKEL;
					break;
				case "JPY":
					currency = IntegrationConstants.JAPANESE_YEN;
					break;
				case "KRW":
					currency = IntegrationConstants.SOUTHKOREAN_WON;
					break;
				case "NGN":
					currency = IntegrationConstants.NIGERIAN_NAIRA;
					break;
				case "NZD":
					currency = IntegrationConstants.NEWZEALAND_DOLLARS;
					break;
				case "SGD":
					currency = IntegrationConstants.SINGAPORE_DOLLARS;
					break;
				case "THB":
					currency = IntegrationConstants.THAI_BHAT;
					break;
				case "TWD":
					currency = IntegrationConstants.NEWTAIWAN_DOLLAR;
					break;
				case "UYU":
					currency = IntegrationConstants.URUGUARYAN_PESO;
					break;
				case "VND":
					currency = IntegrationConstants.VIETNAMESE_DONG;
					break;
				case "ZAR":
					currency = IntegrationConstants.SOUTHAFRICAN_RAND;
					break;
			
			}
			
			return currency;
		}
		
		
}
