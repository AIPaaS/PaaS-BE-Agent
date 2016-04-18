package com.ai.dpp.restlet;
/**
 * 用来处理源文件上传的服务端资源
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
public class SrcFileServerResource extends ServerResource {

	@Post
	public Representation upload(Representation	entity) {

		InputRepresentation in = (InputRepresentation)entity;
		
		// >> a, 获取文件保存的路径， 返回的路径最后没有'/'（不管参数中的最后有没有'/'）
		// 上传文件的目标目录应该在上传页面进行指定
		// 应该不需要采用 日期+UUID 的方式来命名
		
		Form form = getRequest().getResourceRef().getQueryAsForm();
		
		String basePath = form.getFirstValue("compileHostDir");
		String fileName = form.getFirstValue("fileName");
		String path = basePath + "/" + fileName;

		// >> b, 如果文件夹不存在，就创建
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
