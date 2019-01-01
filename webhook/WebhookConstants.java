//$Id$
package com.zoho.abtest.webhook;

import com.zoho.abtest.common.Constants;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.user.ZABUserConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WebhookConstants {
	
	public static final String API_MODULE = "webhook"; //No I18N
	public static final String API_MODULE_PLURAL = "webhooks"; //No I18N
	public static final String API_MODULE_USAGE = "usagestats"; //No I18N
	
	public static final String API_MODULE_USAGE_SINGULAR = "usagestat"; //No I18N
	public static final String API_RESOURCE = "resource.webhook"; //NO I18N
	public static final String API_RESOURCE_INITIAL = "resource.webhook.initial"; //NO I18N
	public static final String PERSONALITY_NAME = "webhookDetails"; //No I18N
	public static final String 	WEBHOOK_NAME = "name";
	public static final String 	WEBHOOK_PROJECT_ID = "projectid";
	public static final String 	WEBHOOK_PROJECT_LINKNAME = "project_linkname";
	public static final String 	WEBHOOK_ID = "id";
	public static final String 	WEBHOOK_URL = "url";
	public static final String 	WEBHOOK_BODY = "body";
	public static final String WEBHOOK_EMPTY_NAME = ZABAction.getMessage(API_RESOURCE_INITIAL);
	public static final String WEBHOOK_ALREADY_EXISTS = ZABAction.getMessage(API_RESOURCE_INITIAL);
	public static final String WEBHOOK_LINK_NAME = "linkname";
	public static final String WEBHOOK_TABLE = ZABAction.getMessage(API_RESOURCE_INITIAL);
	public static final String METHOD = "method"; //NO I18N
	public static final String HEADERS = "header"; //NO I18N
	public static final String HEADER_KEY = "header_key"; //NO I18N
	public static final String HEADER_VALUE = "header_value"; //NO I18N
	public static final String AUTHENTICATION_TYPE = "auth_type"; //NO I18N
	public static final String CALLBACK_URL = "callback_url"; //NO I18N
	public static final String AUTHORIZATION_URL = "authorization_url"; //NO I18N
	public static final String ACCESS_TOKEN_URL = "access_token_url"; //NO I18N
	public static final String ACCESS_TOKEN = "access_token"; //NO I18N
	public static final String REFRESH_TOKEN = "refresh_token"; //NO I18N

	public static enum AuthenticationType {
		NOAUTH(1),
		OAUTH(2);

		private Integer type;
		
		public Integer getType() {
			return this.type;
		}
		
	
		
		private AuthenticationType(Integer statusCode) {
			this.type = statusCode;
		}
		
	}
	
	
	
	
	
}
