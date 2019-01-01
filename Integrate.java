//$Id$
package com.zoho.abtest.report;

import com.zoho.abtest.listener.ZABNotifier;
import com.zoho.abtest.trigger.TriggerConstants.TriggerType;

public class Integrate {
	
	public static void integrate(TriggerType type,Object obj){
		WebhookWrapper wrapper=new WebhookWrapper();
		wrapper.setWrapper((VisitorRawDataWrapper)obj);
		wrapper.setType(type);
		ZABNotifier.notifyListeners(ReportRawDataConstants.API_MODULE_WEBHOOK, wrapper);
	}

}
