//$Id$
package com.zoho.abtest.report;

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
import com.zoho.abtest.GOAL_TRIGGER;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.exception.ZABException;
import com.zoho.abtest.goal.Goal;
import com.zoho.abtest.trigger.Trigger;
import com.zoho.abtest.utility.ZABUtil;
import com.zoho.abtest.webhook.Webhook;

public class GoalTrigger extends CustomTrigger {

	private static final Logger LOGGER = Logger.getLogger(GoalTrigger.class.getName());

	private static final long serialVersionUID = 1L;

//	public ArrayList<Webhook> getWebhooks(WebhookWrapper wrapper) throws Exception{
//		 ArrayList<Webhook> al=null;		
//		try{	    
//		     HashMap<String,String> h=wrapper.getWrapper().getExperimentsData().get(0);
//			String portal = h.get(ReportRawDataConstants.PORTAL);
//			 String goalLnName = h.get(ReportRawDataConstants.GOAL_LINK_NAME);
//		     ZABUtil.setDBSpaceByPortal(portal);
//			Long goalid = Goal.getGoalIdForGoal(goalLnName);
//			Criteria c = new Criteria(new Column(GOAL_TRIGGER.TABLE,GOAL_TRIGGER.GOAL_ID),goalid, QueryConstants.EQUAL, Boolean.FALSE);
//			DataObject dobj=getRow(GOAL_TRIGGER.TABLE,c);
//			Iterator it =dobj.getRows(GOAL_TRIGGER.TABLE);
//			while(it.hasNext()){
//				 Row row= (Row)it.next();
//				 Long triggerid=(Long)row.get(GOAL_TRIGGER.TRIGGER_ID);
//				long webhookid=Trigger.getWebhookIdByTriggerId(triggerid);
//				Webhook webhook=Webhook.getWebhookById(webhookid);
//				webhook.setSystem(false);
//				al.add(webhook);
//				 
//			}
//		     }
//		     catch(Exception e){
//		    	 LOGGER.log(Level.SEVERE, e.getMessage(),e);
//		    	 return null;
//		     }
//		     return al;	     
//	}
//	
	



	


	public HashMap<String ,String> getVariables(WebhookWrapper wrapper){
		HashMap<String,String> userAgenths=new HashMap();
		try{
			
			userAgenths.put("PORTAL", wrapper.getWrapper().getExperimentsData().get(0).get(ReportRawDataConstants.PORTAL));

			userAgenths.put("BROWSER", wrapper.getWrapper().getUserAgenths().get(ReportRawDataConstants.BROWSER_VALUE));
	         userAgenths.put("OS",wrapper.getWrapper().getUserAgenths().get(ReportRawDataConstants.OS_VALUE));
	 		userAgenths.put("DEVICE", wrapper.getWrapper().getUserAgenths().get(ReportRawDataConstants.DEVICE_VALUE));

		}
		catch(Exception ex){
			LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
		}
		return userAgenths;
	}


	
	
	
}
