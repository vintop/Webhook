//$Id$
package com.zoho.abtest.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.Join;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.iam.IAMUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;
import com.adventnet.sas.ds.SASThreadLocal;
import com.zoho.abtest.GOOGLE_ANALYTICS_DETAILS;
import com.zoho.abtest.INTEGRATION;
import com.zoho.abtest.PROJECT_INTEGRATION;
import com.zoho.abtest.EXPERIMENT_PROJECT_INTEGRATION;
import com.zoho.abtest.audience.AudienceConstants;
import com.zoho.abtest.audience.AudienceAttributeConstants.AudienceAttributes;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.datamigration.AudienceFeatureUpgradeHandler;
import com.zoho.abtest.datamigration.IntegrationFeatureUpgradeHandler;
import com.zoho.abtest.dimension.Dimension;
import com.zoho.abtest.eventactivity.EventActivityConstants;
import com.zoho.abtest.eventactivity.EventActivityWrapper;
import com.zoho.abtest.exception.ResourceNotFoundException;
import com.zoho.abtest.exception.ZABException;
import com.zoho.abtest.experiment.Experiment;
import com.zoho.abtest.listener.ZABNotifier;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.project.ProjectTreeEventConstants;
import com.zoho.abtest.project.ProjectTreeEventConstants.Module;
import com.zoho.abtest.project.ProjectTreeEventWrapper;
import com.zoho.abtest.project.ProjectTreeEventConstants.OperationType;
import com.zoho.abtest.utility.ApplicationProperty;
import com.zoho.abtest.utility.ZABSchedulerUtil;
import com.zoho.abtest.utility.ZABUtil;
import com.zoho.conf.Configuration;
import com.zoho.abtest.integration.IntegrationConstants.Integ;


public class Integration extends ZABModel{
	
	/**
	 * 
	 */
	
	private static final Logger LOGGER = Logger.getLogger(Integration.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	private Integer integrationId;
	
	private String integrationName;
	
	private String integrationDescription;

	private Long projectIntegrationId;
	
	private Long experimentProjectIntegrationId;
	
	private Long projectId;
	
	private Long experimentId;
	
	private String iconURL;
	
	private String termURL;
	
	private String privacyURL;
	
	private String authenticationURL;

	private String emailId;
	
	private Integer customDimension;
	
	private String customTracker;
	
	private Boolean isDimension;
	
	private Boolean isAuthenticated;
	
	private Boolean isGAdwordsEnabled;

	public Boolean getIsAuthenticated() {
		return isAuthenticated;
	}

	public void setIsAuthenticated(Boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public Boolean getIsDimension() {
		return isDimension;
	}

	public void setIsDimension(Boolean isDimension) {
		this.isDimension = isDimension;
	}

	public Integer getCustomDimension() {
		return customDimension;
	}

	public void setCustomDimension(Integer customDimension) {
		this.customDimension = customDimension;
	}

	public String getCustomTracker() {
		return customTracker;
	}

	public void setCustomTracker(String customTracker) {
		this.customTracker = customTracker;
	}
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public Integer getIntegrationId() {
		return integrationId;
	}

	public void setIntegrationId(Integer integrationId) {
		this.integrationId = integrationId;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String adwordsURL) {
		this.iconURL = adwordsURL;
	}
	
	public String getTermURL() {
		return termURL;
	}

	public void setTermURL(String termURL) {
		this.termURL = termURL;
	}
	
	public String getPrivacyURL() {
		return privacyURL;
	}

	public void setPrivacyURL(String privacyURL) {
		this.privacyURL = privacyURL;
	}
	
	public String getAuthenticationURL() {
		return authenticationURL;
	}

	public void setAuthenticationURL(String authenticationUrl) {
		this.authenticationURL = authenticationUrl;
	}

	public String getIntegrationName() {
		return integrationName;
	}

	public void setIntegrationName(String integrationName) {
		this.integrationName = integrationName;
	}

	public String getIntegrationDescription() {
		return integrationDescription;
	}

	public void setIntegrationDescription(String integrationDescription) {
		this.integrationDescription = integrationDescription;
	}
	
	public Long getProjectIntegrationId() {
		return projectIntegrationId;
	}

	public void setProjectIntegrationId(Long projectIntegrationId) {
		this.projectIntegrationId = projectIntegrationId;
	}
	
	public Long getExperimentProjectIntegrationId() {
		return experimentProjectIntegrationId;
	}

	public void setExperimentProjectIntegrationId(Long experimentProjectIntegrationId) {
		this.experimentProjectIntegrationId = experimentProjectIntegrationId;
	}
	

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	public Boolean getGAdwords() {
		return isGAdwordsEnabled;
	}

	public void setGAdwords(Boolean isGAdwordsEnabled) {
		this.isGAdwordsEnabled = isGAdwordsEnabled;
	}
	
	public Long getExperimentId() {
		return experimentId;
	}


	public static String getIntegrationName(Integer integrationId){
		String integrationName=null;
		try{
//			Criteria c = new Criteria(new Column(INTEGRATION.TABLE, INTEGRATION.INTEGRATION_ID),integrationId, QueryConstants.EQUAL);
//			DataObject dobj = getRow(INTEGRATION.TABLE,c);
//			if(dobj.containsTable(INTEGRATION.TABLE)) {
//				Iterator<?> it = dobj.getRows(INTEGRATION.TABLE);
//				Row row = (Row)it.next();
//				integrationName = (String)row.get(INTEGRATION.INTEGRATION_NAME);
//			}
			
			for(Integ integ: Integ.values()) {
				if(integ.getIntegrationId() == integrationId){
					integrationName = integ.getDisplayName();
				}
			}
			
		}catch(Exception e){
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
		}
		return integrationName;
	}
	
	public static ArrayList<Integration> getIntegration(String projectId,String project_link_name,String experiment_link_name,String integration_id,HttpServletRequest request) {
		
		//String a = IAMUtil.getCurrentTicket();
		
//		try {
//			ZABSchedulerUtil.submitAdwordsGClidJob();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		AudienceFeatureUpgradeHandler h = new AudienceFeatureUpgradeHandler();
//		try{
//			h.updateAudience();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		IntegrationFeatureUpgradeHandler h1 = new IntegrationFeatureUpgradeHandler();
//		try{
//			h1.locationDataPopulation();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
		
		//String a = Dimension.getAdwordsAdGroup();
		//String a = IAMUtil.getCurrentTicket();
		
		//GoogleAdwords.addGClidJobWithDate("20180526");
		
		ArrayList<Integration> integration = new ArrayList<Integration>();
		try {
			if(projectId!= null){
				
				if(integration_id != null){
					for(Integ integ: Integ.values()) {
						if(Integer.parseInt(integration_id) == integ.getIntegrationId()){
							integration.add(getIntegrationFromRow(integ,request,projectId));
						}
					}
					
				}else{
					for(Integ integ: Integ.values()) {
						integration.add(getIntegrationFromRow(integ,request,projectId));
					}
				}
				
//				DataObject dobj = getRow(INTEGRATION.TABLE, null);
//				if(dobj.containsTable(INTEGRATION.TABLE)) {
//					Iterator<?> it = dobj.getRows(INTEGRATION.TABLE);
//					while(it.hasNext()) {
//						Row row = (Row)it.next();
//						integration.add(getIntegrationFromRow(row,request,projectId));
//					}
//				}


			}else if(project_link_name != null){
				Long project_id = Project.getProjectId(project_link_name);
				//String integration_id = request.getParameter(IntegrationConstants.INTEGRATION_ID);
				
				DataObject dobj = null;
				if(integration_id == null){
					Criteria c = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),project_id, QueryConstants.EQUAL);
					dobj = getRow(PROJECT_INTEGRATION.TABLE, c);
				}else{
					Criteria c = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),project_id, QueryConstants.EQUAL);
					Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),integration_id, QueryConstants.EQUAL);
					dobj = getRow(PROJECT_INTEGRATION.TABLE, c.and(c1));
				}
				if(dobj.containsTable(PROJECT_INTEGRATION.TABLE)) {
					Iterator<?> it = dobj.getRows(PROJECT_INTEGRATION.TABLE);
					while(it.hasNext()) {
						Row row = (Row)it.next();
						Integer integrationId = (Integer)row.get(PROJECT_INTEGRATION.INTEGRATION_ID);
 						integration.add(getProjectIntegrationFromRow(row,integrationId,project_link_name));
 					}
				}
				
			}else if(experiment_link_name != null){	
				Long experimentId = Experiment.getExperimentId(experiment_link_name);
				Criteria c = new Criteria(new Column(EXPERIMENT_PROJECT_INTEGRATION.TABLE, EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_ID),experimentId, QueryConstants.EQUAL);
				
				Join join=new Join(PROJECT_INTEGRATION.TABLE,EXPERIMENT_PROJECT_INTEGRATION.TABLE,new String[]{PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID},new String[]{EXPERIMENT_PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID},Join.INNER_JOIN);
				DataObject dobj =getRow(PROJECT_INTEGRATION.TABLE, c, new Join[]{join});
			
				if(dobj.containsTable(EXPERIMENT_PROJECT_INTEGRATION.TABLE)) {
					Iterator<?> it = dobj.getRows(EXPERIMENT_PROJECT_INTEGRATION.TABLE);
					Iterator<?> it1 = dobj.getRows(PROJECT_INTEGRATION.TABLE);
					while(it1.hasNext()){
						Row row1 = (Row)it1.next();
						Integer integrationId = (Integer)row1.get(PROJECT_INTEGRATION.INTEGRATION_ID);
						if(it.hasNext()) {
							Row row = (Row)it.next();
							integration.add(getExperimentProjectIntegrationFromRow(row,integrationId));
						}
					}
					
				}
			}
			
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
		}
		
		return integration;
	}
	

	public static Integration getIntegrationFromRow(Integ integ,HttpServletRequest request,String projectId) throws JSONException {
		
		Integration integration = new Integration();
		try{
			
//			integration.setIntegrationId((Integer)row.get(INTEGRATION.INTEGRATION_ID));
//			integration.setIntegrationName((String)row.get(INTEGRATION.INTEGRATION_NAME));
//			integration.setIntegrationDescription((String)row.get(INTEGRATION.INTEGRATION_DESCRIPTION));
			integration.setIntegrationId((Integer)integ.getIntegrationId());
			integration.setIntegrationName((String)integ.getDisplayName());
			integration.setIntegrationDescription((String)integ.getDescription());
			integration.setSuccess(Boolean.TRUE);
//			Integer integration_id = (Integer)row.get(INTEGRATION.INTEGRATION_ID);
			Integer integration_id = (Integer)integ.getIntegrationId();
			String iconURL;
			String termsURL=null;
			String privacyURL=null;
			if(integration_id != null && integration_id==1){
				iconURL = ApplicationProperty.getString("com.abtest.integration.analyticsiconurl");
				termsURL = ApplicationProperty.getString("com.abtest.integration.analyticstermurl");
				privacyURL = ApplicationProperty.getString("com.abtest.integration.analyticsprivacyurl");
				integration.setIconURL((String)iconURL);
				integration.setTermURL((String)termsURL);
				integration.setPrivacyURL((String)privacyURL);
			}else if(integration_id != null && integration_id==2){
				iconURL = ApplicationProperty.getString("com.abtest.integration.kissmetricsiconurl");
				termsURL = ApplicationProperty.getString("com.abtest.integration.kissmetricstermurl");
				privacyURL = ApplicationProperty.getString("com.abtest.integration.kissmetricsprivacyurl");
				integration.setIconURL((String)iconURL);
				integration.setTermURL((String)termsURL);
				integration.setPrivacyURL((String)privacyURL);
			}else if(integration_id != null && integration_id==3){
				iconURL = ApplicationProperty.getString("com.abtest.integration.mixpaneliconurl");
				termsURL = ApplicationProperty.getString("com.abtest.integration.mixpaneltermurl");
				privacyURL = ApplicationProperty.getString("com.abtest.integration.mixpanelprivacyurl");
				integration.setIconURL((String)iconURL);
				integration.setTermURL((String)termsURL);
				integration.setPrivacyURL((String)privacyURL);
			}else if(integration_id != null && integration_id==4){
				iconURL = ApplicationProperty.getString("com.abtest.integration.adwordsiconurl");
				termsURL = ApplicationProperty.getString("com.abtest.integration.adwordstermurl");
				privacyURL = ApplicationProperty.getString("com.abtest.integration.adwordsprivacyurl");
				String GadgetsURL = ApplicationProperty.getString("com.abtest.gadwords.serverurl");
				
				//HttpServletRequest request = null;
				String scheme = request.getScheme();
				String serverName = request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
				if (request.getServerPort() == 443 || request.getServerPort() == 80) {
					 serverName = request.getServerName() + request.getContextPath();
				}
				
				String portalName = ZABUtil.getPortalName(); 
			    String serverURL =  scheme + "://" +serverName+"/pagesense/tpauthentication.jsp?projectId="+projectId+"%26portalName="+portalName+"%26mode=adwords";
				String adwordsURL = GadgetsURL+"/auth/google?zservice=ZohoPageSense&authScopes=GADWORDS.ALL&serURL="+serverURL;  //No I18N
				ArrayList<GoogleAdwords> ga=GoogleAdwords.getGoogleAdwords(projectId);
				if(!ga.isEmpty()) {					
					integration.setEmailId(ga.get(0).getEmailId());
				}
				integration.setIsAuthenticated((!ga.isEmpty()  && ga.get(0).getSuccess()));
				integration.setIconURL((String)iconURL);
				integration.setTermURL((String)termsURL);
				integration.setPrivacyURL((String)privacyURL);
				integration.setAuthenticationURL((String)adwordsURL);
			}else if(integration_id != null && integration_id==5){
				iconURL = ApplicationProperty.getString("com.abtest.integration.clickyiconurl");
				termsURL = ApplicationProperty.getString("com.abtest.integration.clickytermurl");
				privacyURL = ApplicationProperty.getString("com.abtest.integration.clickyprivacyurl");
				integration.setIconURL((String)iconURL);
				integration.setTermURL((String)termsURL);
				integration.setPrivacyURL((String)privacyURL);
			}else if(integration_id != null && integration_id==6){
				iconURL = ApplicationProperty.getString("com.abtest.integration.tagmanagericonurl");
				termsURL = ApplicationProperty.getString("com.abtest.integration.tagmanagertermurl");
				privacyURL = ApplicationProperty.getString("com.abtest.integration.tagmanagerprivacyurl");
				String GadgetsURL = ApplicationProperty.getString("com.abtest.gtm.serverurl");
				
				//HttpServletRequest request = null;
				String scheme = request.getScheme();
				String serverName = request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
				if (request.getServerPort() == 443 || request.getServerPort() == 80) {
					 serverName = request.getServerName() + request.getContextPath();
				}
				
				String portalName = IAMUtil.getCurrentServiceOrg().getDomains().get(0).getDomain(); 
			    String serverURL =  scheme + "://" +serverName+"/pagesense/tpauthentication.jsp?projectId="+projectId+"%26portalName="+portalName+"%26mode=gtm";
				String gtmURL = GadgetsURL+"/auth/google?zservice=ZohoPageSense&authScopes=GTM.ALL&serURL="+serverURL;  //No I18N
				
				GoogleTagManager gtm = GoogleTagManager.getGoogleTagManagerForProject(Long.parseLong(projectId));
				if(gtm!=null) {					
					integration.setEmailId(gtm.getEmailId());
				}
				integration.setIsAuthenticated((gtm!=null  && gtm.getSuccess()));
				integration.setIconURL((String)iconURL);
				integration.setTermURL((String)termsURL);
				integration.setPrivacyURL((String)privacyURL);
				integration.setAuthenticationURL((String)gtmURL);
			}
		}catch(Exception ex){
			LOGGER.log(Level.SEVERE, "Exception occured: ",ex);
		}
		return integration;
		
	}
	
	public static Integration getProjectIntegrationFromRow(Row row,Integer integration_id,String project_linkname) throws JSONException {
		
		Integration integration = new Integration();
		String integrationName = getIntegrationName(integration_id);
		String iconURL=null;
		String termsURL=null;
		String privacyURL=null;
		integration.setIntegrationId((Integer)row.get(PROJECT_INTEGRATION.INTEGRATION_ID));
		integration.setIntegrationName((String)integrationName);
		if(integration_id==1){
			integration.setIsDimension(Boolean.TRUE);
		}
		integration.setSuccess(Boolean.TRUE);
		if(integration_id != null && integration_id==1){
			iconURL = ApplicationProperty.getString("com.abtest.integration.analyticsiconurl");
			termsURL = ApplicationProperty.getString("com.abtest.integration.analyticstermurl");
			privacyURL = ApplicationProperty.getString("com.abtest.integration.analyticsprivacyurl");
			integration.setIconURL((String)iconURL);
			integration.setTermURL((String)termsURL);
			integration.setPrivacyURL((String)privacyURL);
		}else if(integration_id != null && integration_id==2){
			iconURL = ApplicationProperty.getString("com.abtest.integration.kissmetricsiconurl");
			termsURL = ApplicationProperty.getString("com.abtest.integration.kissmetricstermurl");
			privacyURL = ApplicationProperty.getString("com.abtest.integration.kissmetricsprivacyurl");
			integration.setIconURL((String)iconURL);
			integration.setTermURL((String)termsURL);
			integration.setPrivacyURL((String)privacyURL);
		}else if(integration_id != null && integration_id==3){
			iconURL = ApplicationProperty.getString("com.abtest.integration.mixpaneliconurl");
			termsURL = ApplicationProperty.getString("com.abtest.integration.mixpaneltermurl");
			privacyURL = ApplicationProperty.getString("com.abtest.integration.mixpanelprivacyurl");
			integration.setIconURL((String)iconURL);
			integration.setTermURL((String)termsURL);
			integration.setPrivacyURL((String)privacyURL);
		}else if(integration_id != null && integration_id==4){
			iconURL = ApplicationProperty.getString("com.abtest.integration.adwordsiconurl");
			termsURL = ApplicationProperty.getString("com.abtest.integration.adwordstermurl");
			privacyURL = ApplicationProperty.getString("com.abtest.integration.adwordsprivacyurl");
			integration.setIconURL((String)iconURL);
			integration.setTermURL((String)termsURL);
			integration.setPrivacyURL((String)privacyURL);
		}else if(integration_id != null && integration_id==5){
			iconURL = ApplicationProperty.getString("com.abtest.integration.clickyiconurl");
			termsURL = ApplicationProperty.getString("com.abtest.integration.clickytermurl");
			privacyURL = ApplicationProperty.getString("com.abtest.integration.clickyprivacyurl");
			integration.setIconURL((String)iconURL);
			integration.setTermURL((String)termsURL);
			integration.setPrivacyURL((String)privacyURL);
		}else if(integration_id != null && integration_id==6){
			iconURL = ApplicationProperty.getString("com.abtest.integration.tagmanagericonurl");
			termsURL = ApplicationProperty.getString("com.abtest.integration.tagmanagertermurl");
			privacyURL = ApplicationProperty.getString("com.abtest.integration.tagmanagerprivacyurl");
			integration.setIconURL((String)iconURL);
			integration.setTermURL((String)termsURL);
			integration.setPrivacyURL((String)privacyURL);
		}
		if(integration_id==4)
		{
			String projectId = Project.getProjectId(project_linkname).toString();
			ArrayList<GoogleAdwords> ga=GoogleAdwords.getGoogleAdwords(projectId);
			if(ga!=null){
				integration.setEmailId(ga.get(0).getEmailId());
			}
			integration.setIsAuthenticated((ga!=null  && ga.get(0).getSuccess()));
			integration.setSuccess(Boolean.TRUE);
		}
		if(integration_id==6)
		{
			String projectId = Project.getProjectId(project_linkname).toString();
			GoogleTagManager gtm = GoogleTagManager.getGoogleTagManagerForProject(Long.parseLong(projectId));
			if(gtm!=null) {					
				integration.setEmailId(gtm.getEmailId());
			}
			integration.setIsAuthenticated((gtm!=null  && gtm.getSuccess()));
			integration.setSuccess(Boolean.TRUE);
		}
		return integration;
		
	}
	
	public static Integration getExperimentProjectIntegrationFromRow(Row row, Integer integrationId) throws JSONException {
		
		Integration integration = new Integration();
		String integrationName = getIntegrationName(integrationId);
		integration.setIntegrationName((String)integrationName);

		integration.setIntegrationId((Integer)integrationId);
		integration.setExperimentProjectIntegrationId((Long)row.get(EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_PROJECT_INTEGRATION_ID));
		integration.setSuccess(Boolean.TRUE);
		if(integrationId==1)
		{
			Long experiment_project_integration_id=(Long)row.get(EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_PROJECT_INTEGRATION_ID);
			GoogleAnalytics ga=GoogleAnalytics.getGoogleAnalytics(experiment_project_integration_id);
			integration.setCustomDimension(ga.getCustomDimension());
			integration.setCustomTracker(ga.getCustomTracker());
			integration.setExperimentProjectIntegrationId(experiment_project_integration_id);
			integration.setIsDimension(Boolean.TRUE);
			integration.setSuccess(Boolean.TRUE);
		}
		return integration;
	}
	
	public static ArrayList<Integration> createIntegration(HashMap<String, String> hs) {
		
		ArrayList<Integration> integrations = new ArrayList<Integration>();
		GoogleAnalytics ga=null;
		Integration integration = new Integration();
		try {
			String projectLinkname = hs.get(IntegrationConstants.PROJECT_LINK_NAME);
			String experimentLinkname = hs.get(IntegrationConstants.EXPERIMENT_LINK_NAME);
			String integration_id = hs.get(IntegrationConstants.INTEGRATION_ID);
			
			if(projectLinkname!=null) {
				Long projectId = Project.getProjectId(projectLinkname);
				hs.put(IntegrationConstants.PROJECT_ID, projectId.toString());
				
				DataObject dobj = null;
				Criteria c = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),projectId, QueryConstants.EQUAL);
				Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),integration_id, QueryConstants.EQUAL);
				dobj = getRow(PROJECT_INTEGRATION.TABLE, c.and(c1));
				int size = dobj.size(PROJECT_INTEGRATION.TABLE);
				if(size==-1){
					createRow(IntegrationConstants.PROJECT_INTEGRATION_TABLE, PROJECT_INTEGRATION.TABLE, hs);
				}
				
				Boolean isGAdwordsEnabled;
				isGAdwordsEnabled = GoogleAdwords.isGAdwordsEnabled(projectLinkname);
				if(isGAdwordsEnabled){
					integration.setGAdwords(Boolean.TRUE);
				}else{
					integration.setGAdwords(Boolean.FALSE);
				}
				
				integration.setSuccess(Boolean.TRUE);
				integration.setProjectId(projectId);
				integrations.add(integration);
			} else if(experimentLinkname!=null){
				Long projectIntegrationId = null;
				Long experimentProjectIntegrationId = null;
				Long experimentId = Experiment.getExperimentId(experimentLinkname);
				Long projectId = Project.getProjectIdFromExperimentLinkName(experimentLinkname);
				String integrationId = hs.get(IntegrationConstants.INTEGRATION_ID);
				
				Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),projectId, QueryConstants.EQUAL);
				Criteria c2 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),integrationId, QueryConstants.EQUAL);
				
				DataObject dobj = getRow(PROJECT_INTEGRATION.TABLE, c1.and(c2));
				if(dobj.containsTable(PROJECT_INTEGRATION.TABLE)) {
					Row row = dobj.getFirstRow(PROJECT_INTEGRATION.TABLE);
					projectIntegrationId = (Long)row.get(PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID);
				}
				hs.put(IntegrationConstants.EXPERIMENT_ID, experimentId.toString());
				hs.put(IntegrationConstants.PROJECT_INTEGRATION_ID, projectIntegrationId.toString());
				
				
				DataObject dobj1 = null;
				Criteria c4 = new Criteria(new Column(EXPERIMENT_PROJECT_INTEGRATION.TABLE, EXPERIMENT_PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID),projectIntegrationId, QueryConstants.EQUAL);
				Criteria c5 = new Criteria(new Column(EXPERIMENT_PROJECT_INTEGRATION.TABLE, EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_ID),experimentId, QueryConstants.EQUAL);
				dobj1 = getRow(EXPERIMENT_PROJECT_INTEGRATION.TABLE, c4.and(c5));
				int size = dobj1.size(EXPERIMENT_PROJECT_INTEGRATION.TABLE);
				if(size==-1){
					dobj1 = createRow(IntegrationConstants.EXPERIMENT_PROJECT_INTEGRATION_TABLE, EXPERIMENT_PROJECT_INTEGRATION.TABLE, hs);
					
					if(dobj1.containsTable(EXPERIMENT_PROJECT_INTEGRATION.TABLE)) {
						Row row = dobj1.getFirstRow(EXPERIMENT_PROJECT_INTEGRATION.TABLE);
						experimentProjectIntegrationId = (Long)row.get(EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_PROJECT_INTEGRATION_ID);
						HashMap<String,String> hs1=new HashMap<String,String>();
						hs1.put(IntegrationConstants.EXPERIMENT_PROJECT_INTEGRATION_ID,experimentProjectIntegrationId.toString());
						hs1.put(IntegrationConstants.CUSTOM_TRACKER,"");
						hs1.put(IntegrationConstants.INTEGRATION_ID, integrationId);
						LOGGER.log(Level.SEVERE,"Exception occured ",integrationId);
						if(integrationId!=null && integrationId.equals("1")){
							ga=GoogleAnalytics.createGoogleAnalytics(hs1);
							integration.setCustomDimension(ga.getCustomDimension());
							integration.setCustomTracker(ga.getCustomTracker());
							integration.setExperimentProjectIntegrationId(experimentProjectIntegrationId);
							integration.setIsDimension(Boolean.TRUE);
						}
					}
					
					//Push Event activity Log start
					HashMap<String, String> updatedValues = new HashMap<String, String>();
					updatedValues.put(IntegrationConstants.INTEGRATION_ID, integrationId);
					updatedValues.put(EventActivityConstants.EXPERIMENT_ID, experimentId.toString());
					updatedValues.put(EventActivityConstants.PROJECT_ID, projectId.toString());
					if(ZABUtil.getCurrentUser() != null)
					{
						updatedValues.put(EventActivityConstants.USER_ID, ZABUtil.getCurrentUser().getUserId().toString());
					}
					updatedValues.put(EventActivityConstants.TIME, ZABUtil.getCurrentTimeInMilliSeconds().toString());
					EventActivityWrapper activityWrapper = new EventActivityWrapper();
					activityWrapper.setModule(com.zoho.abtest.eventactivity.EventActivityConstants.Module.INTEGRATION);
					activityWrapper.setOperationType(com.zoho.abtest.eventactivity.EventActivityConstants.OperationType.CREATE);
					activityWrapper.setDbSpace(ZABUtil.getCurrentUserDbSpace());
					activityWrapper.setUpdatedValues(updatedValues);
					ZABNotifier.notifyListeners(EventActivityConstants.EVENT_MODULE_NAME, activityWrapper);
					//Push Event activity Log end
				}
				
				
				integration.setSuccess(Boolean.TRUE);
				integration.setIntegrationId(Integer.parseInt(integrationId));
				//integration.setExperimentProjectIntegrationId(experimentProjectIntegrationId);
				integrations.add(integration);
				ProjectTreeEventWrapper wrapper = new ProjectTreeEventWrapper();
				wrapper.setModel(integration);
				wrapper.setModule(Module.PROJECT_INTEGRATION);
				wrapper.setDbspace(ZABUtil.getCurrentUserDbSpace());
				wrapper.setType(OperationType.CREATE);
				ZABNotifier.notifyListeners(ProjectTreeEventConstants.EVENT_MODULE_NAME, wrapper);
			}else {
				integration.setSuccess(Boolean.FALSE);
				integration.setResponseString(ZABAction.getMessage(AudienceConstants.AUDIENCE_NOT_EXISTS));
				integrations.add(integration);
			}
			
			if(!integrations.isEmpty() && integrations.get(0).getSuccess()) {				
				ProjectTreeEventWrapper wrapper = new ProjectTreeEventWrapper();
				wrapper.setModel(integrations.get(0));
				wrapper.setDbspace(ZABUtil.getCurrentUserDbSpace());
				wrapper.setType(OperationType.CREATE);
				ZABNotifier.notifyListeners(ProjectTreeEventConstants.EVENT_MODULE_NAME, wrapper);
			}
		}
		catch (Exception e) {
			integration.setSuccess(Boolean.FALSE);
			integration.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			integrations.add(integration);
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
		}
		return integrations;
	}
	
	public static ArrayList<Integration> deleteIntegration(String integration_id,String project_link_name,String experiment_link_name) {
		ArrayList<Integration> integrations = new ArrayList<Integration>();
		Integration integration = new Integration();
		try {
			
			if(project_link_name!=null) {
				Long projectId = Project.getProjectId(project_link_name);
				
				Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),projectId, QueryConstants.EQUAL);
				Criteria c2 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),integration_id, QueryConstants.EQUAL);
				
				DataObject dobj = getRow(PROJECT_INTEGRATION.TABLE, c1.and(c2));
				if(dobj.containsTable(PROJECT_INTEGRATION.TABLE)) {
					Row row = dobj.getFirstRow(PROJECT_INTEGRATION.TABLE);
					deleteResource(row);
				}
				integration.setSuccess(Boolean.TRUE);
				integration.setProjectId(projectId);
				integrations.add(integration);
				ProjectTreeEventWrapper wrapper = new ProjectTreeEventWrapper();
				wrapper.setModel(integration);
				wrapper.setModule(Module.PROJECT_INTEGRATION);
				wrapper.setDbspace(ZABUtil.getCurrentUserDbSpace());
				wrapper.setType(OperationType.DELETE);
				ZABNotifier.notifyListeners(ProjectTreeEventConstants.EVENT_MODULE_NAME, wrapper);
			} else if(experiment_link_name!=null){
				
				Long experimentId = Experiment.getExperimentId(experiment_link_name);
				Long projectIntegrationId = null;
				Long projectId =Project.getProjectIdFromExperimentLinkName(experiment_link_name);
				
				Criteria c1 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.INTEGRATION_ID),integration_id, QueryConstants.EQUAL);
				Criteria c4 = new Criteria(new Column(PROJECT_INTEGRATION.TABLE, PROJECT_INTEGRATION.PROJECT_ID),projectId, QueryConstants.EQUAL);
				
				DataObject dobj = getRow(PROJECT_INTEGRATION.TABLE, c1.and(c4));
				if(dobj.containsTable(PROJECT_INTEGRATION.TABLE)) {
					Row row = dobj.getFirstRow(PROJECT_INTEGRATION.TABLE);
					projectIntegrationId = (Long)row.get(PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID);
					projectId = (Long)row.get(PROJECT_INTEGRATION.PROJECT_ID);
				}
				
				Criteria c2 = new Criteria(new Column(EXPERIMENT_PROJECT_INTEGRATION.TABLE, EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_ID),experimentId, QueryConstants.EQUAL);
				Criteria c3 = new Criteria(new Column(EXPERIMENT_PROJECT_INTEGRATION.TABLE, EXPERIMENT_PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID),projectIntegrationId, QueryConstants.EQUAL);
				
				DataObject dobj1 = getRow(EXPERIMENT_PROJECT_INTEGRATION.TABLE, c2.and(c3));
				if(dobj1.containsTable(EXPERIMENT_PROJECT_INTEGRATION.TABLE)) {
					Row row = dobj1.getFirstRow(EXPERIMENT_PROJECT_INTEGRATION.TABLE);
					deleteResource(row);
					
					//Push Event activity Log start
					HashMap<String, String> updatedValues = new HashMap<String, String>();
					updatedValues.put(IntegrationConstants.INTEGRATION_ID, integration_id);
					updatedValues.put(EventActivityConstants.EXPERIMENT_ID, experimentId.toString());
					updatedValues.put(EventActivityConstants.PROJECT_ID, projectId.toString());
					if(ZABUtil.getCurrentUser() != null)
					{
						updatedValues.put(EventActivityConstants.USER_ID, ZABUtil.getCurrentUser().getUserId().toString());
					}
					updatedValues.put(EventActivityConstants.TIME, ZABUtil.getCurrentTimeInMilliSeconds().toString());
					EventActivityWrapper activityWrapper = new EventActivityWrapper();
					activityWrapper.setModule(com.zoho.abtest.eventactivity.EventActivityConstants.Module.INTEGRATION);
					activityWrapper.setOperationType(com.zoho.abtest.eventactivity.EventActivityConstants.OperationType.DELETE);
					activityWrapper.setDbSpace(ZABUtil.getCurrentUserDbSpace());
					activityWrapper.setUpdatedValues(updatedValues);
					ZABNotifier.notifyListeners(EventActivityConstants.EVENT_MODULE_NAME, activityWrapper);
					//Push Event activity Log end
				}
				
				integration.setSuccess(Boolean.TRUE);
				integration.setProjectId(projectId);
				integrations.add(integration);
				ProjectTreeEventWrapper wrapper = new ProjectTreeEventWrapper();
				wrapper.setModel(integration);
				wrapper.setModule(Module.EXPERIMENT_INTEGRATION);
				wrapper.setDbspace(ZABUtil.getCurrentUserDbSpace());
				wrapper.setType(OperationType.DELETE);
				ZABNotifier.notifyListeners(ProjectTreeEventConstants.EVENT_MODULE_NAME, wrapper);
			}else {
				integration.setSuccess(Boolean.FALSE);
				integration.setResponseString(ZABAction.getMessage(AudienceConstants.AUDIENCE_NOT_EXISTS));
				integrations.add(integration);
			}
		}
		catch (Exception e) {
			integration.setSuccess(Boolean.FALSE);
			integration.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			integrations.add(integration);
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
		}
		return integrations;
	}
	
	public static Long duplicateExperimentIntegrationMapping(Row eiarow, Long experimentId) throws Exception {
		Row dupRow = new Row(EXPERIMENT_PROJECT_INTEGRATION.TABLE);
		dupRow.set(EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_ID, experimentId);
		dupRow.set(EXPERIMENT_PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID, (Long)eiarow.get(EXPERIMENT_PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID));
		DataObject dobj = new WritableDataObject();
		dobj.addRow(dupRow);
		updateDataObject(dobj);
		return (Long)dupRow.get(EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_PROJECT_INTEGRATION_ID);
	}
	
	public static void duplicateGoogleAnalyticsMapping(Row garow,Long exp_pro_integrationid) throws Exception {
		Row dupRow = new Row(GOOGLE_ANALYTICS_DETAILS.TABLE);
		dupRow.set(GOOGLE_ANALYTICS_DETAILS.EXPERIMENT_PROJECT_INTEGRATION_ID, exp_pro_integrationid);
		dupRow.set(GOOGLE_ANALYTICS_DETAILS.CUSTOM_DIMENSION, garow.get(GOOGLE_ANALYTICS_DETAILS.CUSTOM_DIMENSION));
		dupRow.set(GOOGLE_ANALYTICS_DETAILS.CUSTOM_TRACKER, garow.get(GOOGLE_ANALYTICS_DETAILS.CUSTOM_TRACKER));
		DataObject dobj = new WritableDataObject();
		dobj.addRow(dupRow);
		updateDataObject(dobj);
	}
	
}
