package handler.letoysupplychain;

import info.smartlabs.eda.serviceManager.ServiceManager;

public class GrobalData {
	private static ServiceManager serviceManager; //��һ��

	public static ServiceManager getServiceManager() {
		return serviceManager;
	}

	public  static void setServiceManager(ServiceManager serviceManager) {
		GrobalData.serviceManager = serviceManager;
	}
}
