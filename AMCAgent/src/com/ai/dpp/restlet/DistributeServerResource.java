package com.ai.dpp.restlet;
/**
 * 用于想各个目标主机分发编译结果的服务端资源
 * @author guofei
 */
import java.io.File;
import java.io.FileOutputStream;

import org.restlet.data.Form;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class DistributeServerResource extends ServerResource {

	@Post
	public Representation upload(Representation	entity) {

		InputRepresentation in = (InputRepresentation)entity;
		
		// >> a, 获取文件保存的路径， 返回的路径最后没有'/'（不管参数中的最后有没有'/'）
		// 上传文件的目标目录应该在上传页面进行指定
		
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String type = form.getFirstValue("type");
		String fileName = form.getFirstValue("fileName");
		String basePath = System.getenv("AIOSS_HOME");
		String path = "";
		if("exe".equals(type)){
			
			path = basePath + "/bin/" + fileName;
		}else if("lib".equals(type)){
			
			path = basePath + "/lib/" + fileName;
		}
		
		// >> b, 如果文件夹不存在，就创建  貌似用不到
		File dir = new File(basePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// >> c, 保存文件
		File dest = new File(path);
		try {
			in.write(new FileOutputStream(dest));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return new StringRepresentation("success");
	}
	
}
