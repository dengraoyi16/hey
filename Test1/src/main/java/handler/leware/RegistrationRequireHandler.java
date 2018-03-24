package handler.leware;

import info.smartlabs.eda.handler.serviceRequireEventHandler.BasicRegistrationRequireHandler;


public class RegistrationRequireHandler extends BasicRegistrationRequireHandler{
	
	public void run() {
		serviceManager =GrobalData.getServiceManager();
		 super.run();
	}
}
