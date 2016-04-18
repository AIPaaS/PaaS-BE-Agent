package com.ai.dpp.restlet;

/**
 * 用来执行cmd的服务器端资源
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
	 *    客户端传递过来的指令
	 * @return 应用服务器中对应应用程序执行之后输出的字符串信息
	 */
	@Post
	public Representation execute(String instruction) 
	{
		// 首先要解析instruction
		// 客户端传递的instruction 格式为：[CMD| path= /user/bss/data/glov/in , cmd=ls |wc-l]
		// 解析成为三部分： 指令类型 path（路径） cmd（命令）
		// note: 传递过来的path部分可缺省
		if (instruction == null || instruction.trim() == "")
		{
			log.warn("Input a command please");
			return new StringRepresentation("Input a command please");
		}

		String instruType = instruction.trim().substring(0,instruction.indexOf('|')).trim();

		// 首先判断当前指令类型是否是cmd
		// 目前来说先只考虑cmd类型的指令
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
			//如果剩余字符串中有逗号，说明path 和  cmd都有
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
			//如果不存在逗号，则说明只有cmd
			cmd = restStr.split("=")[1];
		}
		
		// 若以上一切正常，则使用java api 到在应用服务器 ~/guofei 目录下创建 message.sh 文件
		// 文件内部写入

		String result = "";
		BufferedWriter writer = null;
		//config.properties 存放在 Linux系统当中 $AIOSS_HOME/conf/文件夹中
		//config.properties 中配置  服务端口号  脚本文件存放目录
		//其中 $AIOSS_HOME 由  System.getenv("AIOSS_HOME") 获得
		
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
			
			// 如果  result 为""，则设为“成功”
			//return new StringRepresentation(result==""?"成功":result);
		} catch (Exception e) {
			result = result + System.getProperty("line.separator") +e.getMessage();
			e.printStackTrace();
			//return new StringRepresentation(result);
		} finally{
			
			//为了保护服务器端内存，在所有操作完成后，确保将此次操作新建的message.sh文件删掉
			
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
