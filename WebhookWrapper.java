//$Id$
package com.zoho.abtest.report;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zoho.abtest.trigger.TriggerConstants.TriggerType;

public class WebhookWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(ReportRawDataAction.class.getName());

	private String webhooklinkname;
     public String getWebhooklinkname() {
		return webhooklinkname;
	}



	public void setWebhooklinkname(String webhooklinkname) {
		this.webhooklinkname = webhooklinkname;
	}

	private TriggerType type;

		public TriggerType getType() {
		return type;
	}



	public void setType(TriggerType type) {
		this.type = type;
	}


	private VisitorRawDataWrapper wrapper;
	
	public VisitorRawDataWrapper getWrapper() {

		return wrapper;
	}
	public void setWrapper(VisitorRawDataWrapper wrapper) {
		this.wrapper = wrapper;
	}
	
}
