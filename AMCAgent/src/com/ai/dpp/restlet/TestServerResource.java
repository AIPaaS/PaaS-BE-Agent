package com.ai.dpp.restlet;
/**
 * @author guofei
 */
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class TestServerResource extends ServerResource {
	
	@Put
	public Representation upload(JsonRepresentation requestRepre) {
		
		System.out.println("·þÎñ¶Ë");
		
		JsonRepresentation responseRepre = null;
		try {
			JSONObject jsonObj = requestRepre.getJsonObject();
			
			System.out.println(jsonObj.get("aaa"));
//			
//			JsonObjectµÄ²âÊÔ
//			JSONObject responseJsonObj = new JSONObject();
//			responseJsonObj.put("xxx", "xxx");
//			
//			responseRepre = new JsonRepresentation(responseJsonObj);
			
			JSONArray item = new JSONArray();
			item.put("{xxx:_xxx,yyy:_yyy}");
			responseRepre = new JsonRepresentation(item);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return responseRepre;
		
	}
}
