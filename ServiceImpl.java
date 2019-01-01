//$Id$
package com.zoho.abtest.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Observer;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import kafka.utils.ZKStringSerializer$;

import javax.xml.parsers.DocumentBuilder;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.adventnet.collaboration.Collaboration;
import com.adventnet.iam.security.SecurityUtil;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.mfw.message.MessageListener;
import com.adventnet.mfw.message.Messenger;
import com.adventnet.mfw.service.Service;
import com.adventnet.persistence.DataObject;
import com.zoho.abtest.cdn.ZABCDN;
import com.zoho.abtest.common.ZABConstants;
import com.zoho.abtest.dynamicconf.DynamicConfigurationUtil;
import com.zoho.abtest.elastic.ElasticSearchUtil;
import com.zoho.abtest.license.LicenseDetail;
import com.zoho.abtest.mqueue.rescue.ResQueCoordinator;
import com.zoho.abtest.portal.PortalAction;
import com.zoho.abtest.report.CustomMapping;
import com.zoho.abtest.trigger.Trigger;
import com.zoho.abtest.utility.ApplicationProperty;
import com.zoho.abtest.utility.ListenerXMLErrorHandler;
import com.zoho.abtest.utility.ZABHTTPClient;
import com.zoho.abtest.utility.ZABSchedulerUtil;
import com.zoho.abtest.utility.ZABUtil;
import com.zoho.abtest.webhook.Webhook;
import com.zoho.conf.Configuration;
import com.zoho.ear.common.util.OAuthUtilLite;
import com.zoho.ear.fileencryptagent.FileEncryptAgent;
import com.zoho.mqueue.consumer.MessageConsumer;

public class ServiceImpl implements Service,MessageListener {
	
	private static final Logger LOGGER = Logger.getLogger(ServiceImpl.class.getName());

	public static final String ENGINE_GRID_TYPE = "2"; //NO I18N
	
	public static final String CUSTOM_CLASS=null;

	
	private static final String EAR_URL = ApplicationProperty.getString("com.zoho.abtest.ear.url");
	
	private static final String EAR_KEY = ApplicationProperty.getString("com.zoho.abtest.ear.key");
	
	static {
		
		LOGGER.log(Level.INFO,"Loading proxy credentials");
	    System.setProperty("es.set.netty.runtime.available.processors", "false");
	}
	
	private String localMqueueHost;
	
	
	
	public String getLocalMqueueHost() throws IOException {
		if(localMqueueHost == null) {
			
			Properties mqProps = new Properties();
			mqProps.load(this.getClass().getResource("/../conf/mqclient.properties").openStream()); //NO I18N
			String host = (String) mqProps.get("mqueue.producer.url"),
					defaultPort = "2181";
			
			localMqueueHost = host.contains(":") ? host:host+":"+defaultPort; //NO I18N
		}
		
		return localMqueueHost;
	}

	@Override
	public void onMessage(Object arg0) {
		try {
			LOGGER.log(Level.INFO,"******Service Impl Started****");
			reserveSharedSpace();
			CustomMapping.registerAuths();
			CustomMapping.registerWebhooks();
			CustomMapping.registerTriggers();
			registerListeners();
			setRawDataRequestURL();

			FileEncryptAgent.setIAMFlag(false);
			OAuthUtilLite.setEARDomain(EAR_URL);
			FileEncryptAgent.setAuthToken(EAR_KEY);
			


		} catch (Exception e ) {
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
		}

	}
	
	private void reserveSharedSpace() throws Exception
	{
		try
		{
			Collaboration c = (Collaboration)BeanUtil.lookup("CollaborationBean");
			//Creating shared db space for maintaining common dimensions
			if(!c.dataSpaceExists("sharedspace")){
				synchronized(this){
					if(!c.dataSpaceExists("sharedspace")){
						c.reserveDataSpace("sharedspace");	//No I18N
						String existingDBSpace = ZABUtil.getDBSpace();
						ZABUtil.setDBSpace("sharedspace");	//No I18N
						PortalAction.populateDefaultValuesForCommonStandardDimensions();
						ElasticSearchUtil.populateDefaultIndexes();
						DynamicConfigurationUtil.createDefaultDynamicConfigurations();
						//create a job to monitor elastic search cluster health
						ZABSchedulerUtil.submitESPeriodicRepititiveJob();
						//Job to send mail for app admin console stats
						ZABSchedulerUtil.submitAdminConsoleDailyStatsJob();
						//License Meta population
						LicenseDetail.populateLicenseDetailsInDB();
						//License jobs
						ZABSchedulerUtil.submitLicenseCheckRepititiveJob();
						//ZABSchedulerUtil.submitLicenseAnnualCheckRepititiveJob();
						ZABSchedulerUtil.submitESFailedEntryRepititiveJob();
						ZABSchedulerUtil.submitZCampaignScriptVerificationJob();
						ZABSchedulerUtil.submitSpaceContentDeletionJob();
						ZABSchedulerUtil.submitDataExpiredDeletionJob();
						ZABSchedulerUtil.submitHeatMapEnabledVisitorLimitCheckRepititiveJob();
						ZABSchedulerUtil.submitConsentOptOutJob();
						ZABUtil.setDBSpace(existingDBSpace);
					}
				}
			}
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.SEVERE, "Exception occured during shared space workings: ",ex);
			throw ex;
		}
	}
	
	private void setRawDataRequestURL() {
		// TODO Auto-generated method stub
		FileWriter writer1 = null;
		try{
				String url1 = this.getClass().getResource("/../../web/initializer/scriptLoader.js").getPath(); //No I18N

				String scritpLoaderString = new String(Files.readAllBytes(Paths.get(url1)));
				
				String replacedScriptLoaderString = scritpLoaderString.replace("$ServerURL",ApplicationProperty.getString("com.zoho.https_server")); //NO I18N
				
				writer1 = new FileWriter(url1);
				writer1.write(replacedScriptLoaderString);
				
			}catch(IOException ex){
				LOGGER.log(Level.SEVERE, "Exception occured: ",ex);
			}catch(Exception ex){
				LOGGER.log(Level.SEVERE, "Exception occured: ",ex);
			}finally{
				try {
					if(writer1!=null) {						
						writer1.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	
	
	private void createTopic(String topicName) {
		InputStream in = null;
		try {			
			
			ZkClient zkClient = new ZkClient(getLocalMqueueHost(), 10000, 10000,
					ZKStringSerializer$.MODULE$);
			final boolean isSecure = false;
			final ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(getLocalMqueueHost()), isSecure);
			
			if(!AdminUtils.topicExists(zkUtils, topicName)) {				
				AdminUtils.createTopic(zkUtils, topicName, 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured:", e);
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (Exception e2) {
					LOGGER.log(Level.SEVERE, "Exception occured:", e2);
				}
			}
		}
	}

	// Registers listeners defined in Listener.xml 
	private void registerListeners() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		try {
			LOGGER.log(Level.INFO,"******Register lister start");
			ZABCDN cdn = (ZABCDN)BeanUtil.lookup("ZABCDN");
			cdn.init();
			LOGGER.log(Level.INFO,"**** Flad to start consumer is {0}", ApplicationProperty.getBoolean(ZABConstants.START_MQUEUE_CONSUMER));
				//Read configs and start kafka consumers
				String url = this.getClass().getResource("/../conf/Listener.xml").getPath(); //No I18N
				//File file = new File("."+File.separator+"webapps"+File.separator+"ROOT"+File.separator+"WEB-INF"+File.separator+"conf"+File.separator+"Listener.xml");
				File file = new File(url);
			    DocumentBuilder documentBuilder = SecurityUtil.getDocumentBuilder();
			    documentBuilder.setErrorHandler(new ListenerXMLErrorHandler());
				Document document = documentBuilder.parse(file);
				
				if (document.getDocumentElement().getElementsByTagName("module").getLength()<1) {
					LOGGER.log(Level.INFO,"********** No listeners defined ************");
					return;
				}

				NodeList moduleNodes = document.getElementsByTagName("module");
				Boolean isDevMode = Boolean.parseBoolean(ApplicationProperty.getString("com.abtest.devmode").trim());
				String hostName = "";
				try {
					hostName = InetAddress.getLocalHost().getHostName();
				} catch (UnknownHostException e) {
					LOGGER.log(Level.SEVERE,e.getMessage());
				}
				for (int i = 0; i < moduleNodes.getLength(); i++) {
					ZABNotifier notifier = new ZABNotifier();
					Node node = moduleNodes.item(i);
					String module=node.getAttributes().getNamedItem("name").getNodeValue(); // No I18N
					String topicName=node.getAttributes().getNamedItem("topic-name").getNodeValue(); // No I18N
					String noOfThreads = node.getAttributes().getNamedItem("no-of-threads").getNodeValue(); // No I18N
					Integer not = Integer.parseInt(noOfThreads);
					NodeList listenerNodeList=node.getChildNodes();
					for(int j=0;j<listenerNodeList.getLength();j++) {
						Node listenerNode=listenerNodeList.item(j);
						if(listenerNode.getNodeType()==Document.ELEMENT_NODE) {
							String listenerClass=listenerNode.getAttributes().getNamedItem("class").getNodeValue(); // No I18N
							Class observer = Class.forName(listenerClass);
							Object obj = observer.newInstance();
							ZABNotifier.setObservers(module, (ZABListener)obj);
							if (obj instanceof Observer) {
								notifier.addObserver((Observer) obj);
								if(isDevMode) {
									topicName = hostName +"__"+ topicName; //NO I18N
									createTopic(topicName);
								}
//								if(Configuration.getBoolean(ZABConstants.START_MQUEUE_CONSUMER)) {
								//Mqueue consumers will start only in Schedular type of server for localzoho and IDC
								//For dev production=false so consumer starts by default
								String tagId = System.getProperty("com.adventnet.sas.appserver.mode"); //NO I18N
								
								LOGGER.log(Level.INFO, "GRID TYPE:!!!!!!!!!!!!!!!!!!: {0}", tagId);
								LOGGER.log(Level.INFO, "RO SET UP VALUE - " + ZABConstants.IS_RO_SETUP);
								
								if(!Configuration.getBoolean("production") || (ENGINE_GRID_TYPE.equals(tagId) && !ZABConstants.IS_RO_SETUP)) {
									//Starting consumers
									try {
										
											ResQueCoordinator.registerResQueThread(module);
											MessageConsumer<String, String> consumer = new MessageConsumer<String, String>(topicName, "abtestgroup", (com.zoho.mqueue.consumer.MessageListener<String, String>)obj); //No I18N
											consumer.start(not);
											LOGGER.log(Level.INFO,"Consumer for "+topicName+ " started!!!!!!");
										
									} catch (Exception e) {
										LOGGER.log(Level.SEVERE, "EXCEPTION OCCURED WHILE STARTING MQUEUE CONSUMERS !!!", e);
									}
								}
								LOGGER.log(Level.INFO,"Registered Listener : "+listenerClass+ " under "+module+" module");
							}
						}
					}
					
					notifier.setTopicName(topicName);
					ZABNotifier.setListeners(module, notifier);
				}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occured: ",e);
			LOGGER.log(Level.SEVERE, "TERMINATING SERVER as there is an exception registering mqueue listeners",e);
		}
		
	}
	
	

	
	@Override
	public void create(DataObject arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void start() throws Exception {
		Messenger.subscribe("startupNotification", this, true, null); //No I18N
	}
	@Override
	public void stop() throws Exception {
		
		// Closes all Async HTTP Client requests
		ExecutorService executor=ZABHTTPClient.getHTTPExecutors();
		executor.shutdown();
		while (!executor.isTerminated()) { 
			LOGGER.log(Level.INFO,"Some client request are still under progress....");
		}  
		
	
		}

	
	}


