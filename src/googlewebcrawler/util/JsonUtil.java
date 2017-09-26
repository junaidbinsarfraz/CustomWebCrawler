package googlewebcrawler.util;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonParsingException;

/**
 * The JsonUtil class provides JSON utility operations.
 * 
 * @author Junaid Sarfraz
 */
public class JsonUtil {

	private static final String STATUS_CODE_SUCCESS = "100";
	public static final String STATUS_CODE_FAILURE = "200";
	public static final String STATUS_CODE_BUSINESS_VALIDATION_ERROR = "201";
	public static final String STATUS_CODE_EXCEPTION = "202";
	public static final String STATUS_CODE_REQUEST_VALIDATION_ERROR = "203";

	/**
	 * Default private constructor.
	 */
	private JsonUtil() {

	}

	/**
	 * The getFieldValue() method is used to get string value from json object
	 * 
	 * @param json
	 *            Specifies the JsonObject instance
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	public static String getFieldValue(JsonObject json, String fieldName) {
		
		String result = null;
		
		try {
			result = json.getString(fieldName);
			
		} catch (Exception e) {
			System.out.println("Invalid Field Value for " + fieldName);
		}
		
		return result;
	}

	/**
	 * The getFieldValue() method is used to get integer value from json object
	 * 
	 * @param json
	 *            Specifies the JsonObject instance
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	public static Integer getIntegerFieldValue(JsonObject json, String fieldName) {
		
		Integer result = null;
		
		try {
			result = json.getInt(fieldName);
			
		} catch (Exception e) {
			System.out.println("Invalid Field Value for " + fieldName);
		}
		
		return result;
	}

	/**
	 * The getFieldValue() method is used to get double value from json object
	 * 
	 * @param json
	 *            Specifies the JsonObject instance
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	public static Double getDoubleFieldValue(JsonObject json, String fieldName) {
		
		Double result = null;
		
		try {
			result = json.getJsonNumber(fieldName).doubleValue();
			
		} catch (Exception e) {
			System.out.println("Invalid Field Value for " + fieldName);
		}
		
		return result;
	}

	/**
	 * The getBooleanFieldValue() method is used to get boolean value from json
	 * object
	 * 
	 * @param json
	 *            Specifies the JsonObject instance
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	public static Boolean getBooleanFieldValue(JsonObject json, String fieldName) {
		
		Boolean result = null;
		
		try {
			result = json.getBoolean(fieldName);
			
		} catch (Exception e) {
			System.out.println("Invalid Field Value for " + fieldName);
			
		}
		return result;
	}

	// TODO: Import DateUtil
	/**
	 * The getFieldValue() method is used to get date value from json object
	 * 
	 * @param json
	 *            Specifies the JsonObject instance
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	/*public static Date getDateFieldValue(JsonObject json, String fieldName) {
		
		String dateStr = getFieldValue(json, fieldName);
		Date date = null;
		
		try {
			date = DateUtil.getDateFromMobileDate(dateStr);
		} catch (Exception ex) {
			System.out.println("Warning: Invalid Date passed to getDateFieldValue function, returning default date...");
		}
		
		return date;
	}*/

	// TODO: Check this
	/**
	 * The getCalendarDateFieldValue() method is used to get calendar date value
	 * from json object
	 * 
	 * @param json
	 *            Specifies the JsonObject instance
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	/*public static Calendar getCalendarDateFieldValue(JsonObject json, String fieldName) {
		
		Calendar calendar = null;

		try {
			Date date = getDateFieldValue(json, fieldName);
			
			if(date!= null) {
				calendar = Calendar.getInstance();
				calendar.setTime(date);
			}
			
		} catch (Exception ex) {
			System.out.println("Warning: Invalid Date passed to getCalendarDateFieldValue function, returning default date...");
		}

		return calendar;
	}*/

	/**
	 * The getJsonValueFromJsonString() method is used to get json value of
	 * given key from given json detail string
	 * 
	 * @param json
	 *            Specifies the json detail string
	 * @param key
	 *            Specifies the key name of json
	 * @return Return value of given json key
	 */
	public static String getJsonValueFromJsonString(String json, String key) {
		
		System.out.println("getJsonValueFromJsonString() method starts");
		System.out.println("key: " + key);

		JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
		String detailString = null;

		if (jsonObject.containsKey(key) && !jsonObject.isNull(key)) {

			JsonObject detail = jsonObject.getJsonObject(key);

			if (detail != null) {
				detailString = detail.toString();
			}
		}

		System.out.println("getJsonValueFromJsonString() method ends");

		return detailString;
	}

	/**
	 * The getJsonArrayFromJsonString() method is used to get json array from
	 * given json detail string
	 * 
	 * @param json
	 *            Specifies the json detail string
	 * @param key
	 *            Specifies the key name of json
	 * @return Return array of given json key
	 */
	public static String[] getJsonArrayFromJsonString(String json, String key) {
		
		System.out.println("getJsonArrayFromJsonString() method starts");
		System.out.println("key: " + key);

		JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();

		String[] jsonStringArray = null;

		if (!jsonObject.isNull(key)) {

			JsonArray jsonArray = jsonObject.getJsonArray(key);

			if (jsonArray != null) {
				jsonStringArray = new String[jsonArray.size()];

				for (int i = 0; i < jsonArray.size(); i++) {
					
					jsonStringArray[i] = jsonArray.getJsonObject(i).toString();
//					System.out.println("Json for " + key + " [" + i + "]: " + jsonStringArray[i]);
				}
			}
		}

		System.out.println("getJsonArrayFromJsonString() method ends");

		return jsonStringArray;
	}

	/**
	 * The getJsonRequestFromRequestMap() method is used to get json request
	 * string from request parameters map
	 * 
	 * @param requestParam
	 *            Specifies the request map
	 * @return Return request json string
	 */
	public static String getJsonRequestFromRequestMap(Map<String, Object> requestParam) {
		
		System.out.println("getJsonRequestFromRequestMap() method starts");

		String requestJsonString = null;

		if (Util.isNotNullAndEmpty(requestParam)) {
			
			JsonObjectBuilder requestJsonBuilder = Json.createObjectBuilder();

			for (String key : requestParam.keySet()) {
				
				Object requestValue = requestParam.get(key);

				if (requestValue instanceof JsonObjectBuilder) {
					
					JsonObjectBuilder requestObject = (JsonObjectBuilder) requestParam.get(key);
					requestJsonBuilder.add(key, requestObject);

				} else if (requestValue instanceof JsonArrayBuilder) {
					
					JsonArrayBuilder requestArray = (JsonArrayBuilder) requestParam.get(key);
					requestJsonBuilder.add(key, requestArray);

				} else if (requestValue instanceof JsonObject) {
					
					JsonObject requestObject = (JsonObject) requestParam.get(key);
					requestJsonBuilder.add(key, requestObject);

				}  else if (requestValue instanceof JsonArray) {
					
					JsonArray requestObject = (JsonArray) requestParam.get(key);
					requestJsonBuilder.add(key, requestObject);

				} else if (requestValue instanceof Boolean) {
					Boolean requestBoolean = Boolean.parseBoolean(requestParam.get(key).toString());
					requestJsonBuilder.add(key, requestBoolean);

				} else if (requestValue instanceof Integer) {
					Integer requestInteger = Integer.parseInt(requestParam.get(key).toString());
					requestJsonBuilder.add(key, requestInteger);

				} else if (requestValue instanceof Double) {
					Double requestDouble = Double.parseDouble(requestParam.get(key).toString());
					requestJsonBuilder.add(key, requestDouble);

				} else {
					requestJsonBuilder.add(key, requestValue.toString());
				}
			}

			requestJsonString = requestJsonBuilder.build().toString();
		}
		
		System.out.println("getJsonRequestFromRequestMap() method ends");

		return requestJsonString;
	}

	/**
	 * The getResponseObjectsFromJson() method is used to get response objects
	 * from response json string
	 * 
	 * @param responseJson
	 *            Specifies the response json string
	 * @return Return response map
	 */
	public static Map<String, String> getResponseObjectsFromJson(String responseJson) {
		
		System.out.println("getResponseObjectsFromJson() method starts");

		JsonObject responseJsonObject = Json.createReader(new StringReader(responseJson)).readObject();

		Map<String, String> responseObjects = new HashMap<String, String>();

		responseObjects.put("requestCode", JsonUtil.getFieldValue(responseJsonObject, "requestCode"));
		responseObjects.put("statusCode", JsonUtil.getFieldValue(responseJsonObject, "statusCode"));
		responseObjects.put("statusMessage", JsonUtil.getFieldValue(responseJsonObject, "statusMessage"));

		String detailJson = JsonUtil.getJsonValueFromJsonString(responseJson, "detail");

		if (Util.isNotNullAndEmpty(detailJson)) {
			responseObjects.put("detail", detailJson);
		}

		System.out.println("getResponseObjectsFromJson() method ends");

		return responseObjects;
	}

	/**
	 * The isRequestSuccessful() method is used to check request response
	 * received successfully
	 * 
	 * @param responseJson
	 *            Specifies the response json string
	 * @return Return true if response received successfully
	 */
	public static Boolean isRequestSuccessful(String responseJson) {

		System.out.println("isRequestSuccessful() method starts");

		Boolean isSuccessful = false;

		if (Util.isNotNullAndEmpty(responseJson)) {

			JsonObject responseJsonObject = Json.createReader(new StringReader(responseJson)).readObject();
			String statusCode = JsonUtil.getFieldValue(responseJsonObject, "statusCode");

			if (STATUS_CODE_SUCCESS.equals(statusCode)) {
				isSuccessful = true;
			}
		}

		System.out.println("isRequestSuccessful() method ends");

		return isSuccessful;
	}
	
	public static Boolean isRequestFailure(JsonObject responseJson) {

		System.out.println("isRequestFailure() method starts");

		Boolean isFailure = false;

		if (responseJson != null) {
			String statusCode = JsonUtil.getFieldValue(responseJson, "statusCode");

			if (STATUS_CODE_FAILURE.equals(statusCode) || STATUS_CODE_EXCEPTION.equals(statusCode)) {
				isFailure = true;
			}
		}

		System.out.println("isRequestFailure() method ends");

		return isFailure;
	}
	
	/**
	 * The isRequestSuccessful() method is used to check request response
	 * received successfully
	 * 
	 * @param responseJson Specifies the response json object
	 * @return Return true if response received successfully
	 */
	public static Boolean isRequestSuccessful(JsonObject responseJson) {

		System.out.println("isRequestSuccessful() method starts");

		Boolean isSuccessful = false;

		if (responseJson != null) {
			String statusCode = JsonUtil.getFieldValue(responseJson, "statusCode");

			if (STATUS_CODE_SUCCESS.equals(statusCode)) {
				isSuccessful = true;
			}
		}

		System.out.println("isRequestSuccessful() method ends");

		return isSuccessful;
	}

	/**
	 * The getResponseDetailFromMap() method is used to get response detail from
	 * response json
	 * 
	 * @param responseJson
	 *            Specifies the response json string
	 * @return Return if response json contains detail then return detail string
	 *         otherwise return null
	 */
	public static String getResponseDetailFromJson(String responseJson) {
		
		System.out.println("getResponseDetailFromJson() method starts");

		String detail = null;

		if (Util.isNotNullAndEmpty(responseJson)) {

			Map<String, String> responseMap = JsonUtil.getResponseObjectsFromJson(responseJson);

			if (Util.isNotNullAndEmpty(responseMap)) {
				String statusCode = responseMap.get("statusCode");

				if (STATUS_CODE_SUCCESS.equals(statusCode)) {
					detail = responseMap.get("detail");
				}
			}
		}

		System.out.println("getResponseDetailFromJson() method ends");

		return detail;
	}
	
	/**
	 * The getStringFieldValueFromJson() method is used to get string value from
	 * json string
	 * 
	 * @param json
	 *            Specifies the json string
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	public static String getStringFieldValueFromJson(String json, String fieldName) {
		
		JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
		String result = getFieldValue(jsonObject, fieldName);

		return result;
	}

	/**
	 * The getBooleanFieldValueFromJson() method is used to get boolean value
	 * from json string
	 * 
	 * @param json
	 *            Specifies the json string
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return boolean field value of given field name
	 */
	public static Boolean getBooleanFieldValueFromJson(String json, String fieldName) {
		
		JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
		Boolean result = getBooleanFieldValue(jsonObject, fieldName);

		return result;
	}
	
	/**
	 * The getJsonArrayFromJsonString() method is used to get json array from
	 * given json detail string
	 * 
	 * @param json
	 *            Specifies the json detail string
	 * @param key
	 *            Specifies the key name of json
	 * @return Return array of given json key
	 */
	public static String[] getJsonStringArrayFromJsonString(String json, String key) {
		JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
		return getJsonStringArrayFromJsonObject(jsonObject, key);
	}
	
	public static String[] getJsonStringArrayFromJsonObject(JsonObject jsonObject, String key) {
		
		System.out.println("getJsonStringArrayFromJsonString() method starts");
		System.out.println("key: " + key);
		String[] jsonStringArray = null;

		if (!jsonObject.isNull(key)) {

			JsonArray jsonArray = jsonObject.getJsonArray(key);

			if (jsonArray != null) {
				jsonStringArray = new String[jsonArray.size()];

				for (int i = 0; i < jsonArray.size(); i++) {
					
					jsonStringArray[i] = jsonArray.get(i).toString();
				}
			}
		}

		System.out.println("getJsonStringArrayFromJsonString() method ends");

		return jsonStringArray;
	}
	
	/**
	 * The getIntegerFieldValueFromString() method is used to get integer value from json object
	 * 
	 * @param json
	 *            Specifies the JsonObject instance
	 * @param fieldName
	 *            Specifies the field name
	 * @return Return field value of given field name
	 */
	// TODO: We need to merge getIntegerFieldValueFromString and getIntegerFieldValue
	public static Integer getIntegerFieldValueFromString(JsonObject json, String fieldName) {
		
		Integer result = null;
		
		try {
			JsonValue jsonValue = json.get(fieldName);
			if(jsonValue != null) {
			
				ValueType valueType = jsonValue.getValueType();
				
				if(valueType == ValueType.STRING) {
					result = Integer.parseInt(json.getString(fieldName));
				} 
				else if(valueType == ValueType.NUMBER) {
					result = json.getInt(fieldName);
				}
			}
		} catch (Exception e) {
			System.out.println("Invalid Field Value for " + fieldName);
		}
		
		return result;
	}
	
	public static Boolean isJSONValid (String json) {
    	try {
			Json.createReader(new StringReader(json)).readObject();
			return true;
		} catch (JsonParsingException e) {
			return false;
		}
    }
	
	public static Boolean containsKey(String json, String key) {
		JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
		
		return jsonObject.containsKey(key);
	}
}
