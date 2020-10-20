package interview;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Mongodb {
	public static void main(String[] args) {
		// Step 1: Read the input file
		String jsonString = readFromFile(System.in);
		
		if(!jsonString.isEmpty()) {
			// Step 2: parse the file contents to json object
			JSONObject jsonObject = parseJson(jsonString.trim());	
			
			// Step 3: flatten and print the json
			if (jsonObject != null) {
				Map<String, String> flattenedMap = new HashMap<>();
				try {
					flattenJson(jsonObject, flattenedMap, "");
					System.out.println(flattenedMap);
				} catch (JSONException e) {
					System.out.println("Error while flattening json string data " + e.getMessage());
				}
			}
		}

		System.out.println("------ End of Processing ------");
	}
	
	static String readFromFile(InputStream in) {
		Scanner reader = new Scanner(in);

		StringBuilder jsonString = new StringBuilder();
		while (reader.hasNextLine()) {
			String data = reader.nextLine();
			jsonString.append(data);
		}
		reader.close();
		return jsonString.toString();
	}

	static JSONObject parseJson(String inputData) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(inputData);
		} catch (JSONException e) {
			System.out.println("Error while parsing input json string " + e.getMessage());
		}
		return jsonObject;
	}

	static void flattenJson(JSONObject jo, Map<String, String> map, String prefix) throws JSONException {
		@SuppressWarnings("unchecked")
		Iterator<String> keys = jo.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			// recurse through the nested json object
			if (jo.get(key) instanceof JSONObject) {
				if (prefix.isEmpty())
					flattenJson(jo.getJSONObject(key), map, key);
				else
					flattenJson(jo.getJSONObject(key), map, prefix + "." + key);
			} else {
				if (prefix.isEmpty()) {
					map.put(key, jo.get(key).toString());
				} else {
					map.put(prefix + "." + key, jo.get(key).toString());
				}
			}
		}
	}
}
