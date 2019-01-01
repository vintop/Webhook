//$Id$
package com.zoho.abtest.trigger;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONException;

import com.adventnet.iam.IAMUtil;
import com.adventnet.iam.xss.IAMEncoder;
import com.opensymphony.xwork2.ActionSupport;
import com.zoho.abtest.common.ZABAction;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.elastic.ElasticSearchIndexConstants;
import com.zoho.abtest.license.LicenseConstants;
import com.zoho.abtest.license.PortalLicenseMapping;
import com.zoho.abtest.project.Project;
import com.zoho.abtest.project.ProjectAction;
import com.zoho.abtest.project.ProjectConstants;
import com.zoho.abtest.user.ZABUserConstants;
import com.zoho.abtest.utility.ZABUtil;


public class TriggerAction extends ActionSupport implements ServletResponseAware, ServletRequestAware{

private static final Logger LOGGER = Logger.getLogger(TriggerAction.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String linkname;
	private Long triggerid;
	public Long getTriggerid() {
		return triggerid;
	}

	public void setTriggerid(Long triggerid) {
		this.triggerid = triggerid;
	}

	public String getLinkname() {
		return linkname;
	}

	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request = arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		response = arg0;
	}

	public String execute() throws IOException, JSONException {
		LOGGER.log(Level.INFO, "Trigger Action Start");
		ArrayList<Trigger> triggers = new ArrayList<Trigger>();
		try {			
			switch(ZABAction.getHTTPMethod(request)) {
			case POST:	
				HashMap<String,String> hs = ZABAction.getRequestParser(request).parseTrigger(request);
				if(hs.containsKey(ZABConstants.SUCCESS)&&!ZABUtil.parseBoolean(hs.get(ZABConstants.SUCCESS),ZABConstants.SUCCESS)){
					Trigger trigger = new Trigger();
					trigger.setSuccess(Boolean.FALSE);
					trigger.setResponseString(hs.get(ZABConstants.RESPONSE_STRING));
					triggers.add(trigger);
				}else{
	                  
					triggers.add(Trigger.createTrigger(hs));
				}
				break;
			case GET:
				if(triggerid==null ) {	
					triggers.addAll(Trigger.getTriggers());
				} else {
					Trigger trigger = Trigger.getTriggerByTriggerId(triggerid);
					triggers.add(trigger);
				}
				break;
			case DELETE:
				Trigger.deleteTrigger(triggerid);
				break;
			case PUT:
				HashMap<String,String> hs1 = ZABAction.getRequestParser(request).parseTrigger(request);
				if(hs1.containsKey(ZABConstants.SUCCESS)&&!ZABUtil.parseBoolean(hs1.get(ZABConstants.SUCCESS),ZABConstants.SUCCESS)){
					Trigger trigger = new Trigger();
					trigger.setSuccess(Boolean.FALSE);
					trigger.setResponseString(hs1.get(ZABConstants.RESPONSE_STRING));
					triggers.add(trigger);
				}else{					
					triggers.add(Trigger.updateTrigger(hs1));
				}
				break;
			}
		}
          catch(JSONException ex){	
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getInvalidInputFormatException(TriggerConstants.API_MODULE_PLURAL));
			return null; 	
		}  catch(Exception ex){
			ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getExceptionString(ZABAction.getMessage(ZABConstants.RESOURCE_PROCESSING_FAILURE), TriggerConstants.API_MODULE_PLURAL));
			return null; 	
		}		
		LOGGER.log(Level.INFO, "Trigger Action Ends"); //No I18N
	    ZABAction.sendResponse(request,response,ZABAction.getResponseProvider(request).getTriggerResponse(request, triggers));	
	    return null;
}
}