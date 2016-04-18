package com.ai.dpp.restlet;
/**
 * ��������Դ�ļ��ϴ��ķ������Դ
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
		
		// >> a, ��ȡ�ļ������·���� ���ص�·�����û��'/'�����ܲ����е������û��'/'��
		// �ϴ��ļ���Ŀ��Ŀ¼Ӧ�����ϴ�ҳ�����ָ��
		// Ӧ�ò���Ҫ���� ����+UUID �ķ�ʽ������
		
		Form form = getRequest().getResourceRef().getQueryAsForm();
		
		String basePath = form.getFirstValue("compileHostDir");
		String fileName = form.getFirstValue("fileName");
		String path = basePath + "/" + fileName;

		// >> b, ����ļ��в����ڣ��ʹ���
		File dir = new File(basePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// >> c, �����ļ�
		File dest = new File(path);
		try {
			in.write(new FileOutputStream(dest));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return new StringRepresentation("success");
	}
	
}
