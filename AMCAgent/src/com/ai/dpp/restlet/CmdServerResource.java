package com.ai.dpp.restlet;

/**
 * ����ִ��cmd�ķ���������Դ
 * @author guofei
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class CmdServerResource extends ServerResource 
{

	private static final Log log = LogFactory.getLog(CmdServerResource.class);
	
	/**
	 * 
	 * @param instruction
	 *    �ͻ��˴��ݹ�����ָ��
	 * @return Ӧ�÷������ж�ӦӦ�ó���ִ��֮��������ַ�����Ϣ
	 */
	@Post
	public Representation execute(String instruction) 
	{
		// ����Ҫ����instruction
		// �ͻ��˴��ݵ�instruction ��ʽΪ��[CMD| path= /user/bss/data/glov/in , cmd=ls |wc-l]
		// ������Ϊ�����֣� ָ������ path��·���� cmd�����
		// note: ���ݹ�����path���ֿ�ȱʡ
		if (instruction == null || instruction.trim() == "")
		{
			log.warn("Input a command please");
			return new StringRepresentation("Input a command please");
		}

		String instruType = instruction.trim().substring(0,instruction.indexOf('|')).trim();

		// �����жϵ�ǰָ�������Ƿ���cmd
		// Ŀǰ��˵��ֻ����cmd���͵�ָ��
		if (!instruType.equalsIgnoreCase("cmd"))
		{
			log.error("Not supported Command: " + instruType);
			return new StringRepresentation("Not supported Command: " + instruType);
		}

		String restStr = instruction.substring(instruction.indexOf('|') + 1).trim();

		String path = null;
		String cmd = null;

		if(restStr.indexOf(",") > -1)
		{
			//���ʣ���ַ������ж��ţ�˵��path ��  cmd����
			String params[] = restStr.split(",");
			
			path = params[0].split("=")[1].trim();
			if (path == null || path.trim() == "")
			{
				log.error("Format error : lack of path");
				return new StringRepresentation("Format error : lack of path");
			}
				
			cmd = params[1].split("=")[1].trim();
			if (cmd == null || cmd.trim() == ""){
				
				log.error("Format error : lack of cmd");
				return new StringRepresentation("Format error : lack of cmd");
			}
		}else{
			//��������ڶ��ţ���˵��ֻ��cmd
			cmd = restStr.split("=")[1];
		}
		
		// ������һ����������ʹ��java api ����Ӧ�÷����� ~/guofei Ŀ¼�´��� message.sh �ļ�
		// �ļ��ڲ�д��

		String result = "";
		BufferedWriter writer = null;
		//config.properties ����� Linuxϵͳ���� $AIOSS_HOME/conf/�ļ�����
		//config.properties ������  ����˿ں�  �ű��ļ����Ŀ¼
		//���� $AIOSS_HOME ��  System.getenv("AIOSS_HOME") ���
		
		Properties prop = new Properties();
		FileInputStream inStream = null;
		try { 
			inStream = new FileInputStream(new File("./AMCAgent.properties"));
			prop.load(inStream);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		File dest = new File(prop.getProperty("shellPath"));
		
		try {
			if(!dest.exists()){
				dest.createNewFile();
			}
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)));
			
			if(path != null){
				writer.write("cd "+ path + "\n");
				writer.write(cmd);
			}else{
				writer.write(cmd);
			}
			writer.flush();
		 
			String command = "sh " + prop.getProperty("shellPath");
			Process process = Runtime.getRuntime().exec(command);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			BufferedReader reader = new BufferedReader(ir);
			String line;
			while ((line = reader.readLine()) != null) {
				result = result + System.getProperty("line.separator") +line;
			}
			
			// ���  result Ϊ""������Ϊ���ɹ���
			//return new StringRepresentation(result==""?"�ɹ�":result);
		} catch (Exception e) {
			result = result + System.getProperty("line.separator") +e.getMessage();
			e.printStackTrace();
			//return new StringRepresentation(result);
		} finally{
			
			//Ϊ�˱������������ڴ棬�����в�����ɺ�ȷ�����˴β����½���message.sh�ļ�ɾ��
			
			if(dest.exists()){
				dest.delete();
			}
			
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("SERVER SIDE!!!");
		StringRepresentation strRepre = new StringRepresentation(result);
		
		return strRepre;
	}
}
