package com.ai.dpp.restlet;
/**
 * 用来处理源文件上传的服务端资源
 * @author guofei
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.data.Form;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
public class SrcCacheServerResource extends ServerResource {
	private static final Log  log = LogFactory.getLog(SrcCacheServerResource.class);
	
	@Post
	public Representation upload(String[] dstFileDir) throws IOException {

		//InputRepresentation in = (InputRepresentation)entity;
		
		// >> a, 获取文件保存的路径， 返回的路径最后没有'/'（不管参数中的最后有没有'/'）
		// 上传文件的目标目录应该在上传页面进行指定
		// 应该不需要采用 日期+UUID 的方式来命名
		
		//Form form = getRequest().getResourceRef().getQueryAsForm();
		
		//String basePath = form.getFirstValue("compileHostDir");
		//String fileName = form.getFirstValue("fileName");
		//String path = basePath + "/" + fileName;
		BufferedWriter writer = null;
		String fileName=dstFileDir[0];  //上传到目的主机的文件名
		String dstDir=dstFileDir[1];    //上传到目的主机的地址和命令类型
        String[] cpDstFileDir=new String[dstFileDir.length-2];
        for(int i=2;i<dstFileDir.length;i++){
        	cpDstFileDir[i-2]=dstFileDir[i];
        }
        if(dstDir==null||dstDir.trim()==""){
        	log.warn("Don't get the address");
        	return new StringRepresentation("please input the address");
        }
        String instruType = dstDir.trim().substring(0,dstDir.indexOf('|')).trim();
        // 首先判断当前命令是否为cache格式，该类只处理cache式的命令
        if(!instruType.equalsIgnoreCase("cache")){
        	log.error("Don't support common: "+ instruType);
        	return new StringRepresentation("Don't support commont: "+instruType);
        }
        //获得实际需要上传的目的地址
        String dstAdres = dstDir.substring(dstDir.indexOf('/')).trim();
        String path=dstAdres + fileName;
        //首先检查是否存在该文件，如果存在需要将该文件删除，再新建一个文件
		// >> b, 如果文件夹不存在，就创建
		File dir = new File(dstAdres);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// >> c, 保存文件
		File dest = new File(path);
		
		try{
			if(dest.exists()){
				dest.delete();
			}
			else{
				dest.createNewFile();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)));
			for(String fileContent : cpDstFileDir){
					writer.write(fileContent + "\n");
					writer.flush();
				}

		
		return new StringRepresentation("success");
	}
	
}
