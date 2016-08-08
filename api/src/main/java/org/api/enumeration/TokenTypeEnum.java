package org.api.enumeration;

import org.json.JSONException;
import org.api.utils.CustomJSONObject;
import org.api.utils.JSONAble;
import org.api.utils.TechniqueException;

public enum TokenTypeEnum implements JSONAble {
    PASSWORD_SETUP("Configuration de mot de passe"), PASSWORD_RESET("Restauration de mot de passe"),
    UNKNOW("Inconnu"), ACCOUNT_ACTIVATION("Activation du compte");

    private String mUserFriendlyName;

    TokenTypeEnum(String pUserFriendlyName) {
        this.mUserFriendlyName = pUserFriendlyName;
    }

    public String getUserFriendlyName() {
        return mUserFriendlyName;
    }

    public static TokenTypeEnum fromString(String pString) {
        if (pString == null) {
            return TokenTypeEnum.UNKNOW;
        }
        try {
            return TokenTypeEnum.valueOf(pString);
        } catch (IllegalArgumentException iae) {
            return TokenTypeEnum.UNKNOW;
        }
    }

    public CustomJSONObject toJSON() throws TechniqueException {
        CustomJSONObject lJSONEnum = new CustomJSONObject();
        try {
            lJSONEnum.put("name", getUserFriendlyName());
            lJSONEnum.put("value", name());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new TechniqueException();
        }
        return lJSONEnum;
    }

    @Override
    public String getUniqueId() {
        return this.name();
    }
}

