package com.ai.dpp.restlet;
/**
 * @author guofei
 */
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class AMCAgent {

	private static final Log log = LogFactory.getLog(AMCAgent.class);

	
	public static void main(String[] args) 
	{
		try {
			
			Component comp = new Component();
			Properties prop = new Properties();
			FileInputStream inStream = null;

			inStream = new FileInputStream(new File("./AMCAgent.properties"));
			
			prop.load(inStream);
			
			int port = Integer.parseInt(prop.get("port").toString());
			String workPath = prop.get("shellPath").toString();
			
			//检查工作目录是否存在
			File file = new File(workPath);
		    if (!file.exists()) {
		    	//工作目录不存在
		    	System.out.println("DIR not exists: workPath = [" + workPath + "]");
		    	return;
		    }

			comp.getServers().add(Protocol.HTTP, port);

			CmdApplication cmdApp = new CmdApplication();
			CacheApplication cacheApp = new CacheApplication();
			FileApplication fileApp = new FileApplication();
			SaveApplication saveApp=new SaveApplication();
			TestApplication testApp = new TestApplication();

			comp.getDefaultHost().attach("/cmd",cmdApp);
			comp.getDefaultHost().attach("/cache",cacheApp);
			comp.getDefaultHost().attach("/file",fileApp);
			comp.getDefaultHost().attach("/save",saveApp);
			comp.getDefaultHost().attach("/test",testApp);

			comp.start();

			log.info("restlet service is already started...");
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}

	}

}
