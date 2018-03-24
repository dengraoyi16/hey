package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import info.smartlabs.eda.data.ConstantInterface;
import info.smartlabs.eda.ontology.PropertyURI;
import info.smartlabs.eda.service.messageService.MessageBroker;
import info.smartlabs.eda.serviceManager.BasicReasoner;
import info.smartlabs.eda.serviceManager.ResultOfChannelReasoning;
import info.smartlabs.eda.serviceManager.ServiceManager;
import info.smartlabs.eda.tool.CurrentTrace;
import info.smartlabs.eda.tool.hashCodeTool;

public class LowCasteReasoningTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MessageBroker broker16 = new MessageBroker("tcp://192.168.1.100:61616");
		
		ServiceManager serviceManager1 = new ServiceManager("file:D:\\wwwroot\\conf\\letoysupplychain.owl",
				"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain", null, broker16);
		serviceManager1.getRawModel().read("file:D:\\wwwroot\\conf\\xexpress.owl");
		serviceManager1.getRawModel().read("file:D:\\wwwroot\\conf\\letoy.owl");
		serviceManager1.getRawModel().read("file:D:\\wwwroot\\conf\\leware.owl");
		serviceManager1.getRawModel().read("file:D:\\wwwroot\\conf\\xlogistics.owl");
		
		
		
		serviceManager1.getRawModel().read("file:D:\\wwwroot\\conf\\waretrans.owl");
		
		//OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, serviceManager1.getRawModel());
		//printIterator(model.listIndividuals(),"individual");
		List<String> individualURIList2 = new ArrayList<String>();
		individualURIList2.add("http://ontology.smart-labs.info/edo/letoy#wareTrans");
		BasicReasoner reasoner =new BasicReasoner();
		//reasoner.channelReasoning(serviceManager1.getRawModel(), individualURIList2);
		reasoner.lowCasteChannelReasoning(serviceManager1.getRawModel(),"http://ontology.smart-labs.info/edo/letoysc#ServiceManagerOfLeToySupplyChain",individualURIList2);
	
		
	}
	public static void printIterator(Iterator<?> i, String header) {
        System.out.println(header);
        for(int c = 0; c < header.length(); c++)
            System.out.print("=");
        System.out.println();
        
        if(i.hasNext()) {
	        while (i.hasNext()) 
	            System.out.println( i.next() );
        }       
        else
            System.out.println("<EMPTY>");
        
        System.out.println();
    }
	
	public static  List<ResultOfChannelReasoning> channelReasoning(Model rawModel, List<String> aimURIs) {
		Logger logger = Logger.getLogger(LowCasteReasoningTest.class); 
		List<ResultOfChannelReasoning> resultList = new ArrayList<ResultOfChannelReasoning>();
		OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, rawModel);
		
		printIterator(model.listIndividuals(),"individual");
		PelletInfGraph pellet = (PelletInfGraph) model.getGraph();
		// load data to pellet
		model.rebind();
		// check consistency
		boolean consistent = pellet.isConsistent();
		// trigger classification
		if (consistent == true) {
			pellet.classify();
			pellet.validate();
			Iterator<String> it = aimURIs.iterator();
			while (it.hasNext()) {
				
				String x = it.next();
				logger.info("serviceManager现在正在推理与"+x+"有关的通道");
				Resource sourceOfNewEntrant = model.getResource(x);
				Property event = model.getProperty(PropertyURI.event);
				Property source = model.getProperty(PropertyURI.source);
				Property destination = model.getProperty(PropertyURI.destination);
				
				StmtIterator beDeliveredToResultIter = model.listStatements(null,
						model.getProperty(PropertyURI.beDeliveredTo), sourceOfNewEntrant);
				printStmtIterator(beDeliveredToResultIter,"bedeliveredto result");
				Statement stmt;
				Resource publish;
				if (beDeliveredToResultIter.hasNext()) {
					while (beDeliveredToResultIter.hasNext()) {
						stmt = beDeliveredToResultIter.nextStatement(); // get
																		// next
																		// //
																		// statement
						publish = stmt.getSubject(); // get the subject

						String channel;
						// 发送配置事件给当事人
						// 生成queueName
						if (publish.getPropertyResourceValue(source) != null
								&& publish.getPropertyResourceValue(event) != null) {

							channel = "deng"+hashCodeTool.queueNameHash(publish.getPropertyResourceValue(source).getURI(),
									publish.getPropertyResourceValue(event).getURI(), sourceOfNewEntrant.getURI());
							resultList.add(new ResultOfChannelReasoning(channel, ConstantInterface.publish,
									publish.getPropertyResourceValue(source).getURI(), publish.getPropertyResourceValue(event).getURI()));
							resultList.add(new ResultOfChannelReasoning(channel, ConstantInterface.subscribe,
									sourceOfNewEntrant.getURI(),publish.getPropertyResourceValue(event).getURI()));
							logger.info("找到与"+x+"有关的事件通道，"+x+"为subscribe方，对方是"+publish.getPropertyResourceValue(source).getURI());

						} else {
							System.out.println(ConstantInterface.class.getName() + "publish 出错");
						}

					}
				}

				/*
				 * 收集和新的成员有关的receiveFrom结果，将结果发送给双方当事人
				 */
				StmtIterator receiveFromResultIter = model.listStatements(null,
						model.getProperty(PropertyURI.receiveFrom), sourceOfNewEntrant);
				
				Resource subscibe;
				if (receiveFromResultIter.hasNext()) {
					while (receiveFromResultIter.hasNext()) {
						stmt = receiveFromResultIter.nextStatement(); // get
																		// next
						// statement
						String channel;
						subscibe = stmt.getSubject(); // get the subject
						System.out.println("subscribe"+subscibe.toString());
						// 发送配置事件给当事人
						// 生成queueName
						if (subscibe.getPropertyResourceValue(destination) != null
								&& subscibe.getPropertyResourceValue(event) != null) {
							System.out.println("subscribe 正常"+ " destination: "+subscibe.getPropertyResourceValue(destination)+ " event"+subscibe.getPropertyResourceValue(event));
							channel ="deng"+ hashCodeTool.queueNameHash(sourceOfNewEntrant.getURI(),
									subscibe.getPropertyResourceValue(event).getURI(),
									subscibe.getPropertyResourceValue(destination).getURI());

							resultList.add(new ResultOfChannelReasoning(channel, ConstantInterface.publish,
									sourceOfNewEntrant.getURI(),subscibe.getPropertyResourceValue(event).getURI()));
							resultList.add(new ResultOfChannelReasoning(channel, ConstantInterface.subscribe,
									subscibe.getPropertyResourceValue(destination).getURI(),subscibe.getPropertyResourceValue(event).getURI()));
							logger.info("找到与"+x+"有关的事件通道，"+x+"为publish方，对方是"+subscibe.getPropertyResourceValue(destination).getURI());


						} else {
							System.out.println("subscribe 出错");
						}
					}
				}

			}
		}
		return resultList;
	}
	public static void printStmtIterator(StmtIterator iter , String header) {
        System.out.println(header);
        for(int c = 0; c < header.length(); c++)
            System.out.print("=");
        System.out.println();
        
        if(iter.hasNext()) {
        	while ( iter.hasNext( ) ) {
        		
        	    Statement stmt      = iter.nextStatement();  // get next statement
        	    Resource  subject   = stmt.getSubject();     // get the subject
        	    Property  predicate = stmt.getPredicate();   // get the predicate
        	    RDFNode   object    = stmt.getObject();      // get the object这里要注意下类型
        	    System.out.print(subject.toString());
        	    System.out.print(" " + predicate.toString() + " ");
        	    if (object instanceof Resource) {
        	       System.out.print(object.toString());
        	    } else {
        	        // object is a literal
        	        System.out.print(" \"" + object.toString() + "\"");
        	    }
        	    System.out.println(" ."); 
        	}
        }
      
        else{
        	System.out.println("<EMPTY>");
        }
        System.out.println();
    }
}
