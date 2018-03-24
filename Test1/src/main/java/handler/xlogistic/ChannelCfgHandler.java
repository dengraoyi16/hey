package handler.xlogistic;

import info.smartlabs.eda.handler.instructEventHandler.cfgInstructEventHandler.BasicChannelCfgHandler;

public class ChannelCfgHandler extends BasicChannelCfgHandler {
	
	public void run() {
		// TODO Auto-generated method stub
		channelData = GrobalData.getChannelData();
		projectName = "xlogistic";
		broker= GrobalData.getBroker();
		super.run();
	}

}
