//$Id$
package com.zoho.abtest.trigger;

import com.zoho.abtest.common.Constants;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.user.ZABUserConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TriggerConstants implements Serializable{
	
	public static final String API_MODULE = "trigger"; //No I18N
	public static final String API_MODULE_PLURAL = "triggers"; //No I18N
	public static final String API_MODULE_USAGE = "usagestats"; //No I18N
	
	public static final String API_MODULE_USAGE_SINGULAR = "usagestat"; //No I18N
	public static final String API_RESOURCE = "resource.trigger"; //NO I18N
	public static final String API_RESOURCE_INITIAL = "resource.trigger.initial"; //NO I18N
	public static final String PERSONALITY_NAME = "triggerDetails"; //No I18N
	public static final String 	TRIGGER_TYPE = "type";
	public static final String 	GOAL_ID = "goal_id";
	public static final String 	WEBHOOK_ID = "webhook_id";
	public static final String 	TRIGGER_ID = "triggerid";
	public static final String  TRIGGER_GOAL_LINK_NAME="goal_link_name";
	public static final String TRIGGER_EMPTY_TYPE = ZABAction.getMessage(API_RESOURCE_INITIAL);
	public static final String TRIGGER_ALREADY_EXISTS = ZABAction.getMessage(API_RESOURCE_INITIAL);
	public static final String WEBHOOK_LINK_NAME = "webhook_link_name";

	public static enum TriggerType {
		NONE(0),
		GOAL(1),
		EXPERIMENT(2);

		private Integer code;
		
		public Integer getCode() {
			return this.code;
		}
		
	
		
		private TriggerType(Integer statusCode) {
			this.code = statusCode;
		}
		
	}

	
	
	
	
}
