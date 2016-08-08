package org.api.enumeration;

import org.json.JSONException;
import org.api.utils.CustomJSONObject;
import org.api.utils.JSONAble;
import org.api.utils.TechniqueException;

public enum AccessLevelEnum implements JSONAble {

    //The order is very important !! It defines the grant hierarchi
    UNKNOWN("Inconnu"), USER("Utilisateur"), ADMIN("Administrateur");

    private String mName;

    private AccessLevelEnum(String pName) {
        this.mName = pName;
    }

    public static AccessLevelEnum fromString(String pFrom) {
        if (pFrom == null) {
            return AccessLevelEnum.UNKNOWN;
        }
        try {
            return AccessLevelEnum.valueOf(pFrom.trim());
        } catch (IllegalArgumentException iae) {
            return AccessLevelEnum.UNKNOWN;
        }
    }

    @Override
    public CustomJSONObject toJSON() throws TechniqueException {
        CustomJSONObject lJSONObject = new CustomJSONObject();
        try {
            lJSONObject.put("value", this.name());
            lJSONObject.put("ordinal", this.ordinal());
            lJSONObject.put("name", mName);
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new TechniqueException();
        }
        return lJSONObject;
    }

    @Override
    public String getUniqueId() {
        return this.name();
    }
}
