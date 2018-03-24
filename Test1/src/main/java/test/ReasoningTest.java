package test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import info.smartlabs.eda.event.serviceRequire.DeregistrationRequire;
import info.smartlabs.eda.event.serviceRequire.RegistrationRequire;
import info.smartlabs.eda.listener.ListenerGenerator;
import info.smartlabs.eda.ontology.EventURI;
import info.smartlabs.eda.service.messageService.MessageBroker;
import info.smartlabs.eda.service.messageService.PublisherGenerator;
import info.smartlabs.eda.serviceManager.ServiceManager;

public class ReasoningTest {
	public static void main(String[] args) {
		// 先生成几个servicemanager
		MessageBroker broker16 = new MessageBroker("tcp://192.168.1.100:61616");
		MessageBroker broker17 = new MessageBroker("tcp://192.168.1.100:61617");
		ServiceManager serviceManager1 = new ServiceManager("file:D:\\wwwroot\\conf\\letoysupplychain.owl",
				"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain", null, broker16);
		handler.letoysupplychain.GrobalData.setServiceManager(serviceManager1);
		ListenerGenerator.generateAndOpenAListner(broker16.createConnection(), EventURI.RegistrationRequire,
				"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain",
				handler.letoysupplychain.RegistrationRequireHandler.class);
		ListenerGenerator.generateAndOpenAListner(broker16.createConnection(), EventURI.RegisterationReport,
				"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain",
				handler.letoysupplychain.RegistrationReportHandler.class);
		ListenerGenerator.generateAndOpenAListner(broker16.createConnection(), EventURI.DeregisterationReport,
				"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain",
				handler.letoysupplychain.DeregistrationReportHandler.class);
		
		
		
		
		ServiceManager serviceManager12 = new ServiceManager("file:D:\\wwwroot\\conf\\xexpress.owl",
				"http://ontology.smart-labs.info/edo/xexpress#ServiceManagerOfXExpress",
				"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain", broker17);
		handler.xexpress.GrobalData.setServiceManager(serviceManager12);
		ListenerGenerator.generateAndOpenAListner(broker17.createConnection(), EventURI.RegistrationRequire,
				"http://ontology.smart-labs.info/edo/xexpress#ServiceManagerOfXExpress",
				handler.xexpress.RegistrationRequireHandler.class);
		ListenerGenerator.generateAndOpenAListner(broker17.createConnection(), EventURI.DeregistrarionRequire,
				"http://ontology.smart-labs.info/edo/xexpress#ServiceManagerOfXExpress",
				handler.xexpress.DeregistrationRequireHandler.class);

		
		
		ServiceManager serviceManager11 = new ServiceManager("file:D:\\wwwroot\\conf\\letoy.owl",
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeToy",
				"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain", broker16);
		handler.letoy.GrobalData.setServiceManager(serviceManager11);
		ListenerGenerator.generateAndOpenAListner(broker16.createConnection(), EventURI.RegisterationReport,
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeToy",
				handler.letoy.RegistrationReportHandler.class);
		
		ListenerGenerator.generateAndOpenAListner(broker16.createConnection(), EventURI.RegistrationRequire,
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeToy",
				handler.letoy.RegistrationRequireHandler.class);

		ServiceManager serviceManager112 = new ServiceManager("file:D:\\wwwroot\\conf\\leware.owl",
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeWare",
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeToy", broker16);
		handler.leware.GrobalData.setServiceManager(serviceManager112);
		ListenerGenerator.generateAndOpenAListner(broker16.createConnection(), EventURI.RegistrationRequire,
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeWare",
				handler.leware.RegistrationRequireHandler.class);

		// 现在是注册部分的代码，分别注册xlogistic 和 waretrans
		handler.xlogistic.GrobalData.setBroker(broker17);
		Logger logger = Logger.getLogger(ReasoningTest.class);
					// 先发送注册事件
		RegistrationRequire event = new RegistrationRequire();
		event.setRegistrantURI("http://ontology.smart-labs.info/edo/xexpress#ProjectXLogistics");
		event.setOwlFilePath("file:D:\\wwwroot\\conf\\xlogistics.owl");
		List<String> individualURIList = new ArrayList<String>();
		individualURIList.add("http://ontology.smart-labs.info/edo/xexpress#xLogistics");
		event.setIndividualURIListOfRegistrant(individualURIList);
		logger.info("xlogistics" + "发送了注册请求到" + "service manager of xexpress" + "和" + EventURI.RegistrationRequire
				+ "组成的queue");
		logger.info("注册信息为: registrantURI-" + event.getRegistrantURI() + " OwlFilePath-" + event.getOwlFilePath());
		PublisherGenerator.generateAndPublishAnEvent(broker17.createConnection(), event, EventURI.RegistrationRequire,
				"http://ontology.smart-labs.info/edo/xexpress#ServiceManagerOfXExpress");

		// 再监听注册回复事件

		ListenerGenerator.generateAndOpenAOneoffListener(broker17.createConnection(), EventURI.RegistrationResponse,
				"http://ontology.smart-labs.info/edo/xexpress#ServiceManagerOfXExpress",
				"http://ontology.smart-labs.info/edo/xexpress#ProjectXLogistics",
				new handler.xlogistic.RegistrationResponseHandler());
		

			
		// waretrans
		handler.waretrans.GrobalData.setBroker(broker16);
		// 先发送注册事件
		RegistrationRequire event2 = new RegistrationRequire();
		event2.setRegistrantURI("http://ontology.smart-labs.info/edo/letoy#ProjectWareTrans");
		event2.setOwlFilePath("file:D:\\wwwroot\\conf\\waretrans.owl");
		List<String> individualURIList2 = new ArrayList<String>();
		individualURIList2.add("http://ontology.smart-labs.info/edo/letoy#wareTrans");
		event2.setIndividualURIListOfRegistrant(individualURIList2);
		logger.info("waretrans" + "发送了注册请求到" + "service manager of leware" + "和" + EventURI.RegistrationRequire
				+ "组成的queue");
		logger.info("注册信息为: registrantURI-" + event2.getRegistrantURI() + " OwlFilePath-" + event2.getOwlFilePath());
		PublisherGenerator.generateAndPublishAnEvent(broker16.createConnection(), event2, EventURI.RegistrationRequire,
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeWare");

		// 再监听注册回复事件

		ListenerGenerator.generateAndOpenAOneoffListener(broker16.createConnection(), EventURI.RegistrationResponse,
				"http://ontology.smart-labs.info/edo/letoy#ServiceManagerOfLeWare",
				"http://ontology.smart-labs.info/edo/letoy#ProjectWareTrans",
				new handler.waretrans.RegistrationResponseHandler());

		
			
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DeregistrationRequire eventDeregistrationRequire = new DeregistrationRequire();
		eventDeregistrationRequire.setDeletedURI("http://ontology.smart-labs.info/edo/xexpress#ProjectXLogistics");
		eventDeregistrationRequire.setOwlFilePath("file:D:\\wwwroot\\conf\\xlogistics.owl");
		eventDeregistrationRequire.setIndividualURIListOfRegistrant(individualURIList);
		logger.info("xlogistics"+"发送了注销请求到"+"service manager of express" +"和"+ EventURI.DeregistrarionRequire+"组成的queue");
		PublisherGenerator.generateAndPublishAnEvent(broker17.createConnection(), eventDeregistrationRequire, EventURI.DeregistrarionRequire, 
				"http://ontology.smart-labs.info/edo/xexpress#ServiceManagerOfXExpress");
		
		// 再监听注销回复事件
		
		ListenerGenerator.generateAndOpenAOneoffListener(broker17.createConnection(),
				EventURI.DeregistrarionResponse, 
				"http://ontology.smart-labs.info/edo/xexpress#ProjectXLogistics",
				new handler.xlogistic.DeregistrationResponseHandler());

	}
}
