//$Id$
package com.zoho.abtest.report;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zoho.abtest.exception.ZABException;
import com.zoho.abtest.listener.IPRestrictionData;
import com.zoho.abtest.listener.ZABListener;
import com.zoho.mqueue.consumer.MessageListener;

public class WebhookListener extends ZABListener implements MessageListener<String, String>{
	private static final Logger LOGGER = Logger.getLogger(WebhookListener.class.getName());
	
	/* (non-Javadoc)
	 * @see com.zoho.abtest.listener.ZABListener#consumeMessage(java.lang.Object)
	 */
	@Override
	public void consumeMessage(Object obj) throws Exception {
		try {			
 			if(obj!=null) {
				WebhookWrapper wrapper = (WebhookWrapper)obj;
				 String customclass=CustomMapping.getTriggertype().get(String.valueOf(wrapper.getType().getCode()));
				   Class cls = Class.forName(customclass);
				   Object obj1 = cls.newInstance();
				   ((CustomTrigger) obj1).trigger(wrapper);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(),e);
			throw new Exception(e);
		}
	}

	@Override
	public IPRestrictionData getIPRestrictionData(Object message)
			throws ZABException {
	
		return null;
	}

}
