//$Id$
package com.zoho.abtest.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.zoho.abtest.GOOGLE_ANALYTICS_DETAILS;
import com.zoho.abtest.GOOGLE_ADWORDS_DETAILS;
import com.zoho.abtest.GOOGLE_TAG_MANAGER_DETAILS;
import com.zoho.abtest.INTEGRATION;
import com.zoho.abtest.PROJECT_INTEGRATION;
import com.zoho.abtest.EXPERIMENT_PROJECT_INTEGRATION;
import com.zoho.abtest.common.Constants;
import com.zoho.abtest.common.ZABConstants;

public class IntegrationConstants {
	
	public static final String API_MODULE = "integration"; //No I18N
	
	public static final String API_MODULE_GOOGLEANALYTICS = "googleanalytics"; //No I18N

	public static final String API_MODULE_GOOGLEADWORDS = "googleadwords"; //No I18N
	
	public static final String API_MODULE_INTERCOM = "intercom"; //No I18N
	
	public static final String API_MODULE_INTERCOM_WEBHOOK = "data"; //No I18N
	
	public static final String API_MODULE_GOOGLETAGMANAGER = "googletagmanager"; //No I18N
	
	public static final String API_RESOURCE = "resource.integration"; //NO I18N
	
	public static final String API_RESOURCE_INITIAL = "resource.integration.initial"; //NO I18N
	
	public static final String INTEGRATION_ID = "integration_id"; //No I18N
	
	public static final String INTEGRATION_NAME = "integration_name"; //NO I18N
	
	public static final String ICON_URL = "icon_url"; //NO I18N
	
	public static final String TERM_URL = "term_url"; //NO I18N
	
	public static final String PRIVACY_URL = "privacy_url"; //NO I18N
	
	public static final String AUTHENTICATION_URL = "authentication_url"; //NO I18N
	
	public static final String INTEGRATION_DESCRIPTION = "integration_description"; //NO I18N
	
	public static final String PROJECT_ID = "project_id"; //No I18N
	
	public static final String USER_ID = "user_id"; //No I18N
	
	public static final String ACCESS_TOKEN = "access_token"; //No I18N

	public static final String VARIATION_ID = "variation_id"; //No I18N
	
	public static final String PROJECT_LINK_NAME = "project_link_name"; //No I18N
		
	public static final String EXPERIMENT_ID = "experiment_id"; //No I18N
	
	public static final String EXPERIMENT_LINK_NAME = "experiment_link_name"; //No I18N
	
	public static final String PROJECT_INTEGRATION_ID = "project_integration_id"; //No I18N
	
	public static final String EXPERIMENT_PROJECT_INTEGRATION_ID = "experiment_project_integration_id"; //No I18N
	
	public static final String CUSTOM_DIMENSION = "custom_dimension"; //No I18N

	public static final String CUSTOM_TRACKER = "custom_tracker"; //No I18N
	
	public static final String IS_DIMENSION="is_dimension"; //No I18N
	
	public static final String IS_AUTHENTICATED="is_authenticated"; //No I18N

	public static final String EMAIL_ID = "email_id"; //No I18N
	
	public static final String WORKSPACE = "workspace"; //No I18N
	
	public static final String MODE = "mode"; //No I18N
	
	public static final String CLIENT_ID = "client_id"; //No I18N
	
	public static final String IS_MCC = "is_mcc"; //No I18N
	
	public static final String CUSTOMER_NAME = "customer_name"; //No I18N
	
	public static final String ISMCC = "ismcc"; //No I18N
	
	public static final String OAUTH_TOKEN = "oauth_token"; //No I18N
	
	public static final String OAUTH_ZUID = "oauth_zuid"; //No I18N
	
	public static final String ACCOUNT_NAME = "account_name"; //No I18N
	
	public static final String WORKSPACE_NAME = "workspace_name"; //No I18N
	
	public static final String CONTAINER_NAME = "container_name"; //No I18N
	
	public static final String TAG_ID = "tag_id"; //No I18N
	
	//public static final String NAME = "client_id"; //No I18N
	
	public static final String ACCOUNT_ID = "account_id"; //No I18N
	
	public static final String CONTAINER_ID = "container_id"; //No I18N

	public static final String WORKSPACE_ID = "workspace_id"; //No I18N
	
	public static final String TAG_NAME = "tag_name"; //No I18N
	
	public static final String TAG_VALUE = "tag_value"; //No I18N
	
	public static final String GOOGLETAGMANAGER = "Google Tag Manager"; //No I18N
	
	public static final String GOOGLEANALYTICS = "Google Analytics"; //No I18N

	public static final String KISSMETRICS = "Kissmetrics"; //No I18N

	public static final String MIXPANEL = "Mixpanel"; //No I18N

	public static final String GOOGLEADWORDS = "Google Ads"; //No I18N

	public static final String CLICKY = "Clicky"; //No I18N
	
	public static final String INTERCOM = "Intercom"; //No I18N
	
	public static final String INTERCOM_ID = "id"; //No I18N
	
	public static final String ADWORDS_URL = "url"; //No I18N
	
	public static final String CAMPAIGN_NAME = "campaign_name"; //No I18N
	
	public static final String ADGROUP_NAME = "adgroup_name"; //No I18N
	
	public static final String ADGROUP_ARRAY = "adgroup"; //No I18N
	
	public static final String CLICKS = "clicks"; //No I18N
	
	public static final String GOOGLECLICKS = "googleclicks"; //No I18N
	
	public static final String COSTS = "costs"; //No I18N
	
	public static final String IMPRESSIONS = "impressions"; //No I18N
	
	public static final String CONVERSIONS = "conversions"; //No I18N
	
	public static final String GOOGLECONVERSIONS = "googleconversions"; //No I18N
	
	public static final String CURRENCY = "currency"; //No I18N
	
	public static final String CTR = "ctr"; //No I18N
	
	public static final String CPC = "cpc"; //No I18N
	
	public static final String START_DATE = "start_date"; //No I18N
	
	public static final String END_DATE = "end_date"; //No I18N
	
	public static final String START_DATE_ADWORDS = "start_date_adwords"; //No I18N
	
	public static final String END_DATE_ADWORDS = "end_date_adwords"; //No I18N
	
	public static final String EXPERIMENT_TYPE_NO = "experiment_type_no"; //No I18N
	
	public static final String MULTIPLE_SEGMENT = "multisegment_criteria"; //No I18N
	
	public static final String URL = "url"; //No I18N
	
	public static final String NO_ACCOUNTS = "no accounts"; //No I18N
	
	public static final String MATCH_TYPE_ID = "match_type_id"; //No I18N
	
	public static final String REPORT_TYPE = "report_type"; //No I18N
	
	public static final String MODULE_TYPE = "module_type"; //No I18N
	
	public static final String ARGENTINE_PESO = "AR$"; //No I18N
	
	public static final String AUSTRALIAN_DOLLARS = "$"; //No I18N

	public static final String BRUNEI_DOLLAR = "$"; //No I18N

	public static final String BRAZILIAN_REAL = "R$"; //No I18N

	public static final String CANADIAN_DOLLARS = "$"; //No I18N

	public static final String CHILEAN_PESO = "CL$"; //No I18N

	public static final String YUAN_RENMINBI = "¥"; //No I18N

	public static final String EUROS = "€"; //No I18N

	public static final String FIJI_DOLLARS = "$"; //No I18N

	public static final String BRITISH_POUND_STERLING = "£"; //No I18N

	public static final String HONGKONG_DOLLARS = "$"; //No I18N

	public static final String ISREALI_SHEKEL = "₪"; //No I18N

	public static final String JAPANESE_YEN = "¥"; //No I18N

	public static final String SOUTHKOREAN_WON = "₩"; //No I18N

	public static final String NIGERIAN_NAIRA = "₦"; //No I18N

	public static final String NEWZEALAND_DOLLARS = "$"; //No I18N

	public static final String SINGAPORE_DOLLARS = "$"; //No I18N

	public static final String THAI_BHAT = "฿"; //No I18N

	public static final String NEWTAIWAN_DOLLAR = "$"; //No I18N

	public static final String US_DOLLARS = "$"; //No I18N

	public static final String URUGUARYAN_PESO = "$U"; //No I18N
	
	public static final String VIETNAMESE_DONG = "₫"; //No I18N
	
	public static final String SOUTHAFRICAN_RAND = "R"; //No I18N

	
	

	
	public static enum Integ{

		GOOGLETAGMANAGER(6, IntegrationConstants.GOOGLETAGMANAGER,""), //NO I18N
		GOOGLEANALYTICS(1, IntegrationConstants.GOOGLEANALYTICS,""), //NO I18N
		KISSMETRICS(2, IntegrationConstants.KISSMETRICS,""), //NO I18N
		MIXPANEL(3, IntegrationConstants.MIXPANEL,""), //NO I18N
		GOOGLEADWORDS(4, IntegrationConstants.GOOGLEADWORDS,""), //NO I18N
		CLICKY(5, IntegrationConstants.CLICKY,""), //NO I18N
        INTERCOM(7, IntegrationConstants.INTERCOM,""); //NO I18N

		private Integer integrationId;
		
		private String displayName;
		
		private String description;
		
		Integ(Integer integrationId, String displayName,String description) {
			this.integrationId = integrationId;
			this.displayName = displayName;
			this.description = description;
		}

		public Integer getIntegrationId() {
			return integrationId;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public String getDescription() {
			return description;
		}
		
		public static Integ getIntegrationById(Integer integId) {
			for(Integ integObj: Integ.values()) { 
				if(integId!=null && integObj.getIntegrationId().equals(integId)) {
					return integObj;
				}
			}
			return null;
		}
	}
	
	public final static List<Constants> INTEGRATION_TABLE;
	static{
		ArrayList<Constants> list = new ArrayList<Constants>();
		list.add(new Constants(INTEGRATION_ID,INTEGRATION.INTEGRATION_ID,ZABConstants.INTEGER,Boolean.TRUE));
		list.add(new Constants(INTEGRATION_NAME,INTEGRATION.INTEGRATION_NAME,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(INTEGRATION_DESCRIPTION,INTEGRATION.INTEGRATION_DESCRIPTION,ZABConstants.STRING,Boolean.TRUE));
		INTEGRATION_TABLE = (List<Constants>) Collections.unmodifiableList(list);
	}

	public final static List<Constants> PROJECT_INTEGRATION_TABLE;
	static{
		ArrayList<Constants> list = new ArrayList<Constants>();
		list.add(new Constants(PROJECT_INTEGRATION_ID,PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(PROJECT_ID,PROJECT_INTEGRATION.PROJECT_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(INTEGRATION_ID,PROJECT_INTEGRATION.INTEGRATION_ID,ZABConstants.INTEGER,Boolean.TRUE));
		PROJECT_INTEGRATION_TABLE = (List<Constants>) Collections.unmodifiableList(list);
	}
	
	public final static List<Constants> EXPERIMENT_PROJECT_INTEGRATION_TABLE;
	static{
		ArrayList<Constants> list = new ArrayList<Constants>();
		list.add(new Constants(EXPERIMENT_PROJECT_INTEGRATION_ID,EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_PROJECT_INTEGRATION_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(PROJECT_INTEGRATION_ID,EXPERIMENT_PROJECT_INTEGRATION.PROJECT_INTEGRATION_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(EXPERIMENT_ID,EXPERIMENT_PROJECT_INTEGRATION.EXPERIMENT_ID,ZABConstants.LONG,Boolean.TRUE));
		EXPERIMENT_PROJECT_INTEGRATION_TABLE = (List<Constants>) Collections.unmodifiableList(list);
	}
	
	public final static List<Constants> GOOGLE_ANALYTICS_TABLE;
	static{
		ArrayList<Constants> list = new ArrayList<Constants>();
		list.add(new Constants(EXPERIMENT_PROJECT_INTEGRATION_ID,GOOGLE_ANALYTICS_DETAILS.EXPERIMENT_PROJECT_INTEGRATION_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(CUSTOM_DIMENSION,GOOGLE_ANALYTICS_DETAILS.CUSTOM_DIMENSION,ZABConstants.INTEGER,Boolean.TRUE));
		list.add(new Constants(CUSTOM_TRACKER,GOOGLE_ANALYTICS_DETAILS.CUSTOM_TRACKER,ZABConstants.STRING,Boolean.TRUE));
		GOOGLE_ANALYTICS_TABLE = (List<Constants>) Collections.unmodifiableList(list);
	}
	
	public final static List<Constants> GOOGLE_ADWORDS_TABLE;
	static{
		ArrayList<Constants> list = new ArrayList<Constants>();
		list.add(new Constants(PROJECT_ID,GOOGLE_ADWORDS_DETAILS.PROJECT_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(EMAIL_ID,GOOGLE_ADWORDS_DETAILS.EMAIL_ID,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(CLIENT_ID,GOOGLE_ADWORDS_DETAILS.CLIENT_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(OAUTH_TOKEN,GOOGLE_ADWORDS_DETAILS.OAUTH_TOKEN,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(OAUTH_ZUID,GOOGLE_ADWORDS_DETAILS.OAUTH_ZUID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(ACCOUNT_NAME,GOOGLE_ADWORDS_DETAILS.ACCOUNT_NAME,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(IS_MCC,GOOGLE_ADWORDS_DETAILS.IS_MCC,ZABConstants.BOOLEAN,Boolean.TRUE));
		GOOGLE_ADWORDS_TABLE = (List<Constants>) Collections.unmodifiableList(list);
	}
	
	public final static List<Constants> GOOGLE_TAG_MANAGER_TABLE;
	static{
		ArrayList<Constants> list = new ArrayList<Constants>();
		list.add(new Constants(PROJECT_ID,GOOGLE_TAG_MANAGER_DETAILS.PROJECT_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(EMAIL_ID,GOOGLE_TAG_MANAGER_DETAILS.EMAIL_ID,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(ACCOUNT_NAME,GOOGLE_TAG_MANAGER_DETAILS.ACCOUNT_NAME,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(CONTAINER_NAME,GOOGLE_TAG_MANAGER_DETAILS.CONTAINER_NAME,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(WORKSPACE_NAME,GOOGLE_TAG_MANAGER_DETAILS.WORKSPACE_NAME,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(TAG_ID,GOOGLE_TAG_MANAGER_DETAILS.TAG_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(ACCOUNT_ID,GOOGLE_TAG_MANAGER_DETAILS.ACCOUNT_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(CONTAINER_ID,GOOGLE_TAG_MANAGER_DETAILS.CONTAINER_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(WORKSPACE_ID,GOOGLE_TAG_MANAGER_DETAILS.WORKSPACE_ID,ZABConstants.LONG,Boolean.TRUE));
		GOOGLE_TAG_MANAGER_TABLE = (List<Constants>) Collections.unmodifiableList(list);
	}
	public final static List<Constants> INTERCOM_TABLE;
	static{
		ArrayList<Constants> list = new ArrayList<Constants>();
		list.add(new Constants(PROJECT_ID,com.zoho.abtest.INTERCOM.PROJECT_ID,ZABConstants.LONG,Boolean.FALSE));
		list.add(new Constants(USER_ID,com.zoho.abtest.INTERCOM.USER_ID,ZABConstants.LONG,Boolean.TRUE));
		list.add(new Constants(ACCESS_TOKEN,com.zoho.abtest.INTERCOM.ACCESS_TOKEN,ZABConstants.STRING,Boolean.TRUE));
		list.add(new Constants(EMAIL_ID,com.zoho.abtest.INTERCOM.EMAIL_ID,ZABConstants.STRING,Boolean.TRUE));

		INTERCOM_TABLE = (List<Constants>) Collections.unmodifiableList(list);
	}
}
