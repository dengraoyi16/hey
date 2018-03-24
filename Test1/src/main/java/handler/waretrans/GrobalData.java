package handler.waretrans;

import info.smartlabs.eda.data.ChannelData;
import info.smartlabs.eda.service.messageService.MessageBroker;
import info.smartlabs.eda.serviceManager.ServiceManager;

public class GrobalData {
	private static ChannelData channelData= new ChannelData();
	private static ServiceManager serviceManager; //µ•“ª¿‡
	private static MessageBroker broker;
	public static MessageBroker getBroker() {
		return broker;
	}

	public static void setBroker(MessageBroker broker) {
		GrobalData.broker = broker;
	}

	public static ServiceManager getServiceManager() {
		return serviceManager;
	}

	public  static void setServiceManager(ServiceManager serviceManager) {
		GrobalData.serviceManager = serviceManager;
	}

	public static ChannelData getChannelData() {
		return channelData;
	}

	
}
