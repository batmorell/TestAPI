package org.api.bo;

import org.api.dto.ProspectDTO;
import org.api.utils.CustomJSONObject;
import org.api.utils.JSONAble;
import org.api.utils.TechniqueException;
import org.json.JSONException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectOption implements JSONAble {
    public int id;
    public String value;

    public static SelectOption mapIn(ResultSet pResultSet) throws SQLException {
        SelectOption lSelectOption = new SelectOption();
        lSelectOption.id = pResultSet.getInt("id");
        lSelectOption.value = pResultSet.getString("value");

        return lSelectOption;
    }

    public CustomJSONObject toJSON() throws TechniqueException {
        CustomJSONObject lJSONObject = new CustomJSONObject();
        try {
            lJSONObject.put("id", this.id);
            lJSONObject.put("value", this.value);
        }   catch (JSONException ex) {
                ex.printStackTrace();
                throw new TechniqueException();
        }
        return lJSONObject;
    }

    @Override
    public String getUniqueId() {
        return this.id + "";
    }


}
