package com.ai.dpp.restlet;
/**
 * ��������Դ�ļ��ϴ��ķ������Դ
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
		
		// >> a, ��ȡ�ļ������·���� ���ص�·�����û��'/'�����ܲ����е������û��'/'��
		// �ϴ��ļ���Ŀ��Ŀ¼Ӧ�����ϴ�ҳ�����ָ��
		// Ӧ�ò���Ҫ���� ����+UUID �ķ�ʽ������
		
		//Form form = getRequest().getResourceRef().getQueryAsForm();
		
		//String basePath = form.getFirstValue("compileHostDir");
		//String fileName = form.getFirstValue("fileName");
		//String path = basePath + "/" + fileName;
		BufferedWriter writer = null;
		String fileName=dstFileDir[0];  //�ϴ���Ŀ���������ļ���
		String dstDir=dstFileDir[1];    //�ϴ���Ŀ�������ĵ�ַ����������
        String[] cpDstFileDir=new String[dstFileDir.length-2];
        for(int i=2;i<dstFileDir.length;i++){
        	cpDstFileDir[i-2]=dstFileDir[i];
        }
        if(dstDir==null||dstDir.trim()==""){
        	log.warn("Don't get the address");
        	return new StringRepresentation("please input the address");
        }
        String instruType = dstDir.trim().substring(0,dstDir.indexOf('|')).trim();
        // �����жϵ�ǰ�����Ƿ�Ϊcache��ʽ������ֻ����cacheʽ������
        if(!instruType.equalsIgnoreCase("cache")){
        	log.error("Don't support common: "+ instruType);
        	return new StringRepresentation("Don't support commont: "+instruType);
        }
        //���ʵ����Ҫ�ϴ���Ŀ�ĵ�ַ
        String dstAdres = dstDir.substring(dstDir.indexOf('/')).trim();
        String path=dstAdres + fileName;
        //���ȼ���Ƿ���ڸ��ļ������������Ҫ�����ļ�ɾ�������½�һ���ļ�
		// >> b, ����ļ��в����ڣ��ʹ���
		File dir = new File(dstAdres);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// >> c, �����ļ�
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
