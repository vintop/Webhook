//$Id$
package com.zoho.abtest.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionManager;

import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.Join;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.ds.query.UpdateQuery;
import com.adventnet.ds.query.UpdateQueryImpl;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;
import com.zoho.abtest.EXPERIMENT;
import com.zoho.abtest.GOAL;
import com.zoho.abtest.GOAL_TRIGGER;
import com.zoho.abtest.PROJECT;
import com.zoho.abtest.TRIGGER;
import com.zoho.abtest.WEBHOOK;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.common.ZABModel;
import com.zoho.abtest.exception.ZABException;
import com.zoho.abtest.experiment.ExperimentConstants;
import com.zoho.abtest.goal.Goal;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.user.ZABUserConstants;
import com.zoho.abtest.utility.ZABUtil;
import com.zoho.abtest.webhook.Webhook;
import com.zoho.abtest.webhook.WebhookConstants;

public class Trigger extends ZABModel{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Webhook.class.getName());
	private int trigger_type;
	private Long webhookid;
	private String webhooklinkname;
	private Long goalid;
	public Long getGoalid() {
		return goalid;
	}

	public void setGoalid(Long goalid) {
		this.goalid = goalid;
	}

	public String getWebhooklinkname() {
		return webhooklinkname;
	}

	public void setWebhooklinkname(String webhooklinkname) {
		this.webhooklinkname = webhooklinkname;
	}


	private Long id;
	
	
	public int getTrigger_type() {
		return trigger_type;
	}

	public void setTrigger_type(int trigger_type) {
		this.trigger_type = trigger_type;
	}

	public Long getWebhookid() {
		return webhookid;
	}

	public void setWebhookid(Long webhookid) {
		this.webhookid = webhookid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public static Long getWebhookIdByTriggerId(Long triggerid){
		Long webhookid=null;
		try{
			
			Criteria c = new Criteria(new Column(TRIGGER.TABLE,TRIGGER.TRIGGER_ID),triggerid, QueryConstants.EQUAL, Boolean.FALSE);
			DataObject dobj = getRow(TRIGGER.TABLE,c);
			Row row=(Row)dobj.getRow(TRIGGER.TABLE);
			webhookid=(Long)row.get(TRIGGER.WEBHOOK_ID);
			
		}
		catch(Exception e){
			
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return webhookid;
	}
	
	
	public static Trigger getTriggerFromRow(Row row){
		Trigger trigger=new Trigger();
		try{
			Long id=(Long)row.get("TRIGGER_ID");
			Long webhookid=(Long)row.get("WEBHOOK_ID");
			int type=Integer.parseInt(row.get("TRIGGER_TYPE").toString());
			Criteria c1 = new Criteria(new Column(GOAL_TRIGGER.TABLE,GOAL_TRIGGER.TRIGGER_ID),id, QueryConstants.EQUAL, Boolean.FALSE);
			DataObject dobj1 = getRow(GOAL_TRIGGER.TABLE,c1);
			Row row1=(Row)dobj1.getRow(GOAL_TRIGGER.TABLE);
			Long goalid=(Long)row1.get("GOAL_ID");
			Criteria c2 = new Criteria(new Column(WEBHOOK.TABLE,WEBHOOK.ID),webhookid, QueryConstants.EQUAL, Boolean.FALSE);
			DataObject dobj2 = getRow(WEBHOOK.TABLE,c2);
			Row row2=(Row)dobj2.getRow(WEBHOOK.TABLE);
			String webhooklinkname=(String)row2.get("WEBHOOK_LINK_NAME");
		trigger.setId(id);
		trigger.setWebhookid(webhookid);
		trigger.setTrigger_type(type);
		trigger.setGoalid(goalid);
		trigger.setWebhooklinkname(webhooklinkname);
		}
		catch(Exception e){
			trigger.setSuccess(Boolean.FALSE);
			trigger.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return trigger;
	}
	
	
	
	public static Trigger getTriggerByTriggerId(Long triggerid){
		LOGGER.log(Level.INFO,"  TRYING TO GET TRIGGER INFORMATION");
		Trigger trigger=new Trigger();
		
		try{
			Criteria c = new Criteria(new Column(TRIGGER.TABLE,TRIGGER.TRIGGER_ID),triggerid, QueryConstants.EQUAL, Boolean.FALSE);
			DataObject dobj = getRow(TRIGGER.TABLE,c);
			Row row=(Row)dobj.getRow(TRIGGER.TABLE);
			trigger=getTriggerFromRow(row);
			trigger.setSuccess(Boolean.TRUE);
		}
		catch(Exception e){
			trigger.setSuccess(Boolean.FALSE);
			trigger.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
	}
		return trigger;
	}
	
	
	
	public static void deleteTrigger(Long triggerid){
		Trigger trigger=null;
		try{
		trigger=Trigger.getTriggerByTriggerId(triggerid);
			Criteria c = new Criteria(new Column(TRIGGER.TABLE,TRIGGER.TRIGGER_ID),triggerid, QueryConstants.EQUAL, Boolean.FALSE);
			deleteResource(c);
			trigger.setSuccess(Boolean.TRUE);
			}
		
		catch(Exception e){
				trigger.setSuccess(Boolean.FALSE);
				trigger.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
				LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
	}
	
	
	public static ArrayList<Trigger> getTriggers(){
		LOGGER.log(Level.INFO,"  TRYING TO GET TRIGGER INFORMATION");
		Trigger trigger=new Trigger();
		ArrayList<Trigger> al=new ArrayList();
		try{
			Criteria c=null;		
			DataObject dj=getRow(TRIGGER.TABLE, c);
			Iterator it=dj.getRows(TRIGGER.TABLE);
			while(it.hasNext())
			{
				Row row = (Row)it.next();
				trigger= getTriggerFromRow(row);		
				al.add(trigger);
				trigger.setSuccess(Boolean.TRUE);

			}
		}
		catch (Exception e) {
			trigger.setSuccess(Boolean.FALSE);
			trigger.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return al;
	}
	
	
	
	public static Trigger createTrigger(HashMap<String,String> hs)
	{
		Trigger trigger = null;
		TransactionManager tMgr = DataAccess.getTransactionManager();
		try {
			tMgr.begin();
			String goallinkname = hs.get(TriggerConstants.TRIGGER_GOAL_LINK_NAME);
			goallinkname = goallinkname.trim();
			if(goallinkname.length() == 0 ){
				throw new ZABException(ZABAction.getMessage(TriggerConstants.TRIGGER_EMPTY_TYPE,null));
			}
			   Long goalid= Goal.getGoalIdByLinkname(goallinkname);

			String webhooklinkname=hs.get(TriggerConstants.WEBHOOK_LINK_NAME);
			Long webhookid = Webhook.getWebhookId(webhooklinkname);
			// TO GET WEBHOOK ID FROM PROJECT TABLE
			hs.put(ZABUserConstants.CREATED_BY, ZABUtil.getCurrentUser().getUserId().toString());
			String time = ZABUtil.getCurrentTimeInMilliSeconds().toString();
			hs.put(ZABUserConstants.CREATED_TIME, time);
			hs.put(ZABUserConstants.MODIFIED_TIME, time);
			Row row = new Row (TRIGGER.TABLE);
			int type = Integer.parseInt(hs.get(TriggerConstants.TRIGGER_TYPE));
			row.set(TRIGGER.TRIGGER_TYPE,type);
			row.set(TRIGGER.WEBHOOK_ID, webhookid);
			//to add the new row in the DataObject
			DataObject d=new WritableDataObject();
			d.addRow(row);
			createResource(d);
			//add the row in trigger database.
			
			Long triggerid = (Long)row.get(TRIGGER.TRIGGER_ID);
			Row row1 = new Row (GOAL_TRIGGER.TABLE);
			row1.set(GOAL_TRIGGER.TRIGGER_ID, triggerid);
			row1.set(GOAL_TRIGGER.GOAL_ID, goalid);
			DataObject d1=new WritableDataObject();
			d1.addRow(row1);
			createResource(d1);
			//add the row in goal trigger database.
			tMgr.commit();
			trigger= getTriggerFromRow(row);
            trigger.setWebhooklinkname(webhooklinkname);
            trigger.setGoalid(goalid);
			trigger.setSuccess(Boolean.TRUE);
		}
		catch (ZABException e) {
			trigger.setSuccess(Boolean.FALSE);
			trigger.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			
			try {
				tMgr.rollback();
			} catch (Exception e1) {
				LOGGER.log(Level.SEVERE, e1.getMessage(),e);
			}
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		} catch (Exception e) {
			trigger.setSuccess(Boolean.FALSE);
			trigger.setResponseString(e.getMessage());
			try {
				tMgr.rollback();
			} catch (Exception e1) {
				LOGGER.log(Level.SEVERE, e1.getMessage(),e);
			}
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		return trigger;
		
		
	}
	
	
	public static Trigger updateTrigger(HashMap<String,String> hs){
		String  goallinkname=hs.get(TriggerConstants.TRIGGER_GOAL_LINK_NAME);
		Long  triggerid=Long.parseLong(hs.get(TriggerConstants.TRIGGER_ID));
		TransactionManager tMgr = DataAccess.getTransactionManager();
		Trigger trigger=null;
		try{
			tMgr.begin();
			Criteria c = new Criteria(new Column(GOAL.TABLE,GOAL.GOAL_LINK_NAME),goallinkname, QueryConstants.EQUAL, Boolean.FALSE);
		        DataObject dobj=getRow(GOAL.TABLE,c);
				Row row=(Row) dobj.getRow("GOAL");
				Long id=(Long)row.get("GOAL_ID");
				Criteria c1 = new Criteria(new Column(TRIGGER.TABLE,TRIGGER.TRIGGER_ID),triggerid, QueryConstants.EQUAL, Boolean.FALSE);
				UpdateQuery u1 = new UpdateQueryImpl(TRIGGER.TABLE);
				u1.setCriteria(c1);
				u1.setUpdateColumn(TRIGGER.TRIGGER_TYPE, Integer.parseInt(hs.get(TriggerConstants.TRIGGER_TYPE)));
				u1.setUpdateColumn(TRIGGER.TRIGGER_ID, triggerid);
				Criteria c2 = new Criteria(new Column(GOAL_TRIGGER.TABLE,GOAL_TRIGGER.TRIGGER_ID),triggerid, QueryConstants.EQUAL, Boolean.FALSE);
				UpdateQuery u2 = new UpdateQueryImpl(GOAL_TRIGGER.TABLE);
				u2.setCriteria(c2);
				u2.setUpdateColumn(GOAL_TRIGGER.GOAL_ID, id);
				u2.setUpdateColumn(GOAL_TRIGGER.TRIGGER_ID, triggerid);
				String webhooklinkname=hs.get(TriggerConstants.WEBHOOK_LINK_NAME);
				Long webhookid = Webhook.getWebhookId(webhooklinkname);
				u1.setUpdateColumn(TRIGGER.WEBHOOK_ID,webhookid);
				ZABModel.updateRow(u1);
				ZABModel.updateRow(u2);
				tMgr.commit();
				trigger=Trigger.getTriggerByTriggerId(triggerid);
				trigger.setSuccess(Boolean.TRUE);
                trigger.setWebhooklinkname(webhooklinkname);
			
		} 	catch (ZABException e) {
			trigger.setSuccess(Boolean.FALSE);
			trigger.setResponseString(e.getMessage());
			try {
				tMgr.rollback();
			} catch (Exception e1) {
				LOGGER.log(Level.SEVERE, e1.getMessage(),e);
			}
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		catch(Exception e) {
			trigger.setSuccess(Boolean.FALSE);
			trigger.setResponseString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE));
			try {
				tMgr.rollback();
			} catch (Exception e1) {
				LOGGER.log(Level.SEVERE, e1.getMessage(),e);
			}
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
		}
		
		return trigger; 
		
	}
}
