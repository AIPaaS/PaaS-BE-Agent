package com.ai.dpp.restlet;
/**
 * ��������Դ�����Լ��ռ��������ķ������Դ
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

	//���ر�����
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
	
	//����Դ����
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
			
			//Ϊ�˱������������ڴ棬�����в�����ɺ�ȷ�����˴β����½���message.sh�ļ�ɾ��
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
