package com.ai.dpp.restlet;
/**
 * 用来编译源代码以及收集编译结果的服务端资源
 * @author guofei
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.restlet.data.Form;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class CompFileServerResource extends ServerResource {

	//下载编译结果
	@Get
	public InputRepresentation download(){
		
		FileInputStream fileInputStream = null;
		try {
			
			Form form = getRequest().getResourceRef().getQueryAsForm();
			String type = form.getFirstValue("type");
			String path = "";
			if(type.equals("exe")){
				
				String exeFileName = form.getFirstValue("exeFileName");
				path = System.getenv("AIOSS_HOME") + "/bin/" + exeFileName;
			}else if(type.equals("lib")){
				
				String libFileName = form.getFirstValue("libFileName");
				path = System.getenv("AIOSS_HOME") + "/lib/" + libFileName;
			}
			
			File file = new File(path);
			fileInputStream = new FileInputStream(file);
		
			InputRepresentation inputRepre = new InputRepresentation(fileInputStream);
			return inputRepre;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
			
		}
	}
	
	//编译源代码
	@Post
	public Representation post(String shellContent){
		
		File dest = new File("/unibss/devusers/devrt01/guofei/message.sh");
		BufferedWriter writer = null;
		
		try {
			
			if(!dest.exists()){
				
				dest.createNewFile();
			}
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)));
			
			writer.write(shellContent);
				
			writer.flush();
		
			String command = "sh /unibss/devusers/devrt01/guofei/message.sh";
			Runtime.getRuntime().exec(command);
			
//			String command = "sh " + prop.getProperty("shellPath");
//			InputStreamReader ir = new InputStreamReader(
//					process.getInputStream());
//			BufferedReader reader = new BufferedReader(ir);
//			String line;
//			while ((line = reader.readLine()) != null) {
//				result = result + System.getProperty("line.separator") +line;
//			}
			
			return new StringRepresentation("success");
		} catch (Exception e) {
			
			//result = result + System.getProperty("line.separator") +e.getMessage();
			e.printStackTrace();
			return null;
		} finally{
			
			//为了保护服务器端内存，在所有操作完成后，确保将此次操作新建的message.sh文件删掉
			if(dest.exists()){
				
				dest.delete();
			}
			
			if(writer!=null){
				
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
