package org.utils;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomJSONObject extends JSONObject implements Cloneable {

    public CustomJSONObject() {
        super();
    }

    public CustomJSONObject(String pString) throws JSONException {
        super(pString);
    }

    public CustomJSONObject put(String pKey, Date pValue) throws JSONException {
        if (pValue != null) {
            return (CustomJSONObject) this.put(pKey, Utils.americanDateTimeFormater().format(pValue));
        }
        return (CustomJSONObject) this.put(pKey, JSONObject.NULL);
    }

//	public CustomJSONObject put(String pKey, Object pValue) throws JSONException {
//		if (pValue != null) {
//			if (pValue instanceof JSONAble) {
//				try {
//					return put(pKey, (JSONAble) pValue);
//				} catch (TechniqueException ex) {
//					ex.printStackTrace();
//				}
//			}
//			return (CustomJSONObject) super.put(pKey, pValue);
//		}
//		return (CustomJSONObject) this.put(pKey, JSONObject.NULL);
//	}

    public <T extends JSONAble> CustomJSONObject put(String pKey, T pValue)throws JSONException, TechniqueException {
        if (pValue != null) {
            return (CustomJSONObject) super.put(pKey, pValue.toJSON());
        }
        return (CustomJSONObject) this.put(pKey, JSONObject.NULL);
    }

    public CustomJSONObject put(String pKey, List<? extends JSONAble> pValue)throws JSONException, TechniqueException {
        if (pValue != null) {
            return (CustomJSONObject) super.put(pKey, new CustomJSONArray(pValue));
        }
        return (CustomJSONObject) this.put(pKey, JSONObject.NULL);
    }

    @Override
    public CustomJSONObject clone() throws CloneNotSupportedException {
        try {
            return new CustomJSONObject(this.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}

