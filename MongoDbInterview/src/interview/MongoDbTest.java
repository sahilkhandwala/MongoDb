package interview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MongoDbTest {
	
	private Mongodb mongodb;
	
	@Before
	public void setup() {
		mongodb = new Mongodb();
	}

	@Test
	public void testJsonParsingSuccess() throws JSONException {
		String input = "{ \"name\":\"John\" }";
		JSONObject obj = mongodb.parseJson(input);
		Assert.assertTrue(obj.get("name").equals("John"));
	}
	
	@Test
	public void testJsonParsingFailure_EmptyString() {
		String input = "";
		JSONObject obj = mongodb.parseJson(input);
		Assert.assertNull(obj);
	}
	
	@Test
	public void testJsonParsingFailure_IncorrectJson() {
		String input = "\"name\":\"John\" }";;
		JSONObject obj = mongodb.parseJson(input);
		Assert.assertNull(obj);
	}
	
	@Test
	public void testFlatteningJsonSuccess() throws JSONException {
		JSONObject obj = new JSONObject(); JSONObject obj2 = new JSONObject();
		
		obj.put("a", "1"); obj.put("b", "true");
		obj2.put("d", 3); obj2.put("e", "test");
		obj.put("c", obj2);
		
		 Map<String, String> map = new HashMap<>();
		 
		 Map<String, String> expectedOutput = new HashMap<>();
		 expectedOutput.put("a", "1");
		 expectedOutput.put("b", "true");
		 expectedOutput.put("c.d", "3");
		 expectedOutput.put("c.e", "test");
		 
		 mongodb.flattenJson(obj, map, "");
		 Assert.assertNotEquals(0, map.size());
		 Assert.assertEquals(expectedOutput, map);
	}
	
	@Test
	public void testFlatteningNestedJsonSuccess() throws JSONException {
		 JSONObject obj = new JSONObject(); JSONObject obj2 = new JSONObject(); JSONObject obj3 = new JSONObject();
		
		 obj3.put("z", 100); 
		 obj2.put("d", 3); obj2.put("e", "test"); obj2.put("x", obj3);
		
		 obj.put("a",1); obj.put("b",true); obj.put("c",obj2);
		
		 JSONObject obj4 = new JSONObject();
		 JSONObject obj5 = new JSONObject();
		
		 obj4.put("president", obj5);
		 obj5.put("name", "biden");
		
		 obj.put("vote", obj4);
		 
		 Map<String, String> map = new HashMap<>();
		 
		 Map<String, String> expectedOutput = new HashMap<>();
		 expectedOutput.put("c.x.z", "100");
		 expectedOutput.put("a", "1");
		 expectedOutput.put("vote.president.name", "biden");
		 expectedOutput.put("b", "true");
		 expectedOutput.put("c.d", "3");
		 expectedOutput.put("c.e", "test");
		 
		 mongodb.flattenJson(obj, map, "");
		 Assert.assertNotEquals(0, map.size());
		 Assert.assertEquals(expectedOutput, map);
	}
}
