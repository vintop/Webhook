//$Id$
package com.zoho.abtest.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.zoho.abtest.GOOGLE_ANALYTICS_DETAILS;
import com.zoho.abtest.PROJECT;
import com.zoho.abtest.PROJECT_INTEGRATION;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.exception.ZABException;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.project.ProjectConstants;

public class GoogleAnalytics extends ZABModel {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(GoogleAnalytics.class.getName());

	private Long experimentProjectIntegrationId;
	private int customDimension;
	private String customTracker;

	public int getCustomDimension() {
		return customDimension;
	}
	
	public void setCustomDimension(int customDimension) {
		this.customDimension = customDimension;
	}
	
	public Long getExperimentProjectIntegrationId() {
		return experimentProjectIntegrationId;
	}
	
	public void setExperimentProjectIntegrationId(
			Long experimentProjectIntegrationId) {
		this.experimentProjectIntegrationId = experimentProjectIntegrationId;
	}
	
	public String getCustomTracker() {
		return customTracker;
	}
	
	public void setCustomTracker(String customTracker) {
		this.customTracker = customTracker;
	}
	
	public static GoogleAnalytics getGoogleAnalyticsFromRow(Row row)
	{
		GoogleAnalytics googleanalytics=new GoogleAnalytics();
		googleanalytics.setSuccess(Boolean.TRUE);
		googleanalytics.setCustomDimension((int)row.get(GOOGLE_ANALYTICS_DETAILS.CUSTOM_DIMENSION));
		googleanalytics.setCustomTracker((String)row.get(GOOGLE_ANALYTICS_DETAILS.CUSTOM_TRACKER));
		googleanalytics.setExperimentProjectIntegrationId((Long)row.get(GOOGLE_ANALYTICS_DETAILS.EXPERIMENT_PROJECT_INTEGRATION_ID));
		return googleanalytics;
	}
	
	public static ArrayList<GoogleAnalytics> getGoogleAnalyticsFromDobj(DataObject dobj) throws DataAccessException
	{
		ArrayList<GoogleAnalytics> googleanalytics=new ArrayList<GoogleAnalytics>();
		if(dobj.containsTable(GOOGLE_ANALYTICS_DETAILS.TABLE)) {
			Iterator it = dobj.getRows(GOOGLE_ANALYTICS_DETAILS.TABLE);
			while(it.hasNext()) {
				Row row = (Row)it.next();
				GoogleAnalytics g = getGoogleAnalyticsFromRow(row);
				googleanalytics.add(g);
			}
		}
		return googleanalytics;
	}

	public static GoogleAnalytics getGoogleAnalyticsFromExpProjIntegrationId(DataObject dobj,Long expProjIntegId) throws DataAccessException
	{
		GoogleAnalytics googleanalytics = null;
		if(dobj.containsTable(GOOGLE_ANALYTICS_DETAILS.TABLE)) {
			Criteria c = new Criteria(new Column(GOOGLE_ANALYTICS_DETAILS.TABLE, GOOGLE_ANALYTICS_DETAILS.EXPERIMENT_PROJECT_INTEGRATION_ID),expProjIntegId, QueryConstants.EQUAL);
			Iterator it = dobj.getRows(GOOGLE_ANALYTICS_DETAILS.TABLE,c);
			if(it.hasNext()) {
				Row row = (Row)it.next();
				googleanalytics = getGoogleAnalyticsFromRow(row);
			}
		}
		return googleanalytics;
	}
	public static GoogleAnalytics createGoogleAnalytics(HashMap<String, String> hs) {
		// TODO Auto-generated method stub
		GoogleAnalytics googleanalytics=null;
		try {
			
			DataObject dobj = createRow(IntegrationConstants.GOOGLE_ANALYTICS_TABLE, GOOGLE_ANALYTICS_DETAILS.TABLE, hs);
			ArrayList<GoogleAnalytics> googleAnalyticsRes =getGoogleAnalyticsFromDobj(dobj);
			if(googleAnalyticsRes!=null && !googleAnalyticsRes.isEmpty()) {
				googleanalytics=googleAnalyticsRes.get(0);
			}
			
			
		} catch (ZABException e) {
			googleanalytics = new GoogleAnalytics();
			googleanalytics.setSuccess(Boolean.FALSE);
			googleanalytics.setResponseString(e.getMessage());
		} catch (Exception e) {
			googleanalytics = new GoogleAnalytics();
			googleanalytics.setSuccess(Boolean.FALSE);
			googleanalytics.setResponseString(e.getMessage());
		}
		return googleanalytics;
	}
	
	public static GoogleAnalytics getGoogleAnalytics(Long experiment_project_integration_id)
	{
		//ArrayList<GoogleAnalytics> googleanalytics=null;
		GoogleAnalytics google=null;

		
		try{
			Criteria c=new Criteria(new Column(GOOGLE_ANALYTICS_DETAILS.TABLE,GOOGLE_ANALYTICS_DETAILS.EXPERIMENT_PROJECT_INTEGRATION_ID),experiment_project_integration_id,QueryConstants.EQUAL);
			DataObject dobj = ZABModel.getRow(GOOGLE_ANALYTICS_DETAILS.TABLE, c);
			ArrayList<GoogleAnalytics> googleAnalyticsRes =getGoogleAnalyticsFromDobj(dobj);
			if(googleAnalyticsRes!=null && !googleAnalyticsRes.isEmpty()) {
				google=googleAnalyticsRes.get(0);
			}
		   }
			
	        catch(Exception e){
	        	google=new GoogleAnalytics();
	        	google.setSuccess(Boolean.FALSE);
	        	google.setResponseString(e.getMessage());
	        	LOGGER.log(Level.SEVERE,e.getMessage(),e);
	        }
			return google;
		
	}
	
	public static GoogleAnalytics updateGoogleAnalytics(HashMap<String,String> hs)
	{
		GoogleAnalytics googleanalytics=null;
		try
		{
		String experiment_project_integration_id=hs.get("experiment_project_integration_id");
		Criteria c = new Criteria(new Column(GOOGLE_ANALYTICS_DETAILS.TABLE,GOOGLE_ANALYTICS_DETAILS.EXPERIMENT_PROJECT_INTEGRATION_ID),experiment_project_integration_id, QueryConstants.EQUAL);
		ZABModel.updateRow(IntegrationConstants.GOOGLE_ANALYTICS_TABLE, GOOGLE_ANALYTICS_DETAILS.TABLE, hs, c, IntegrationConstants.API_MODULE_GOOGLEANALYTICS);
		googleanalytics = new GoogleAnalytics();
		googleanalytics.setSuccess(Boolean.TRUE);
		googleanalytics.setCustomDimension(Integer.parseInt(hs.get("custom_dimension")));
		googleanalytics.setCustomTracker(hs.get("custom_tracker"));
		googleanalytics.setExperimentProjectIntegrationId(Long.parseLong(hs.get("experiment_project_integration_id")));
		}catch(Exception e){
			googleanalytics.setSuccess(Boolean.FALSE);
		}
		return googleanalytics;
		
	}

}
