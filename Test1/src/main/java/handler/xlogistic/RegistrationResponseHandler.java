package handler.xlogistic;

import java.util.ArrayList;
import java.util.List;

import info.smartlabs.eda.data.ProjectData;
import info.smartlabs.eda.event.serviceResponse.RegistrationResponse;
import info.smartlabs.eda.handler.serviceResponseEventHandler.BasicRegistrationResponseHandler;

public class RegistrationResponseHandler extends BasicRegistrationResponseHandler {
	public RegistrationResponseHandler(){
		this.setBroker(GrobalData.getBroker());
	}
	public void run() {
		RegistrationResponse registrationResponse = (RegistrationResponse) this.event;
		logger.info("接收到注册回复消息,是否注册成功："+registrationResponse.isConfirm());
		if (registrationResponse.isConfirm()) {
			ProjectData data = new ProjectData();
			data.setProjectName("xlogistics");
			data.setProjectURI("http://ontology.smart-labs.info/edo/xexpress#ProjectXLogistics");
			data.setManagedByURI("http://ontology.smart-labs.info/edo/xexpress#ServiceManagerOfXExpress");
			List<String> individualURIList = new ArrayList<String>();
			individualURIList.add("http://ontology.smart-labs.info/edo/xexpress#xLogistics");
			data.setIndividualURIList(individualURIList);
			openIndividualListener(data, ChannelCfgHandler.class);
			
		} else {
			logger.info("xlogistics"+ "注册失败");
			// 关闭自己
			// 调用karaf关闭自己吗？
		}
		this.closeConsumer();
	}

}
