package handler.xlogistic;

import info.smartlabs.eda.handler.serviceResponseEventHandler.BasicDeregistrationResponseHandler;


public class DeregistrationResponseHandler extends BasicDeregistrationResponseHandler{
	public DeregistrationResponseHandler(){
		channelData=GrobalData.getChannelData();
	}
	public void run(){
		
		channelData.printSubsribeQueue();
		start();
		channelData.printSubsribeQueue();
	
	}
}
