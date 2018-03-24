package handler.letoysupplychain;

import info.smartlabs.eda.handler.reportEventHandler.cfgReportHandler.BasicDeregistrationReportHandler;


public class DeregistrationReportHandler extends BasicDeregistrationReportHandler{

	 public void run() {
		 serviceManager=GrobalData.getServiceManager();
		 super.run();
	}
}
