package handler.xexpress;

import info.smartlabs.eda.handler.serviceRequireEventHandler.BasicDeregistrationRequireHandler;

public class DeregistrationRequireHandler extends BasicDeregistrationRequireHandler{
	
	public void run() {
		serviceManager =GrobalData.getServiceManager();
		 super.run();
	}
}
