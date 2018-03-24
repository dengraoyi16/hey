package handler.waretrans;

import info.smartlabs.eda.handler.instructEventHandler.cfgInstructEventHandler.BasicChannelCfgHandler;

public class ChannelCfgHandler extends BasicChannelCfgHandler {
	
	public void run() {
		// TODO Auto-generated method stub
		channelData = GrobalData.getChannelData();
		projectName = "waretrans";
		broker= GrobalData.getBroker();
		super.run();
	}

}
