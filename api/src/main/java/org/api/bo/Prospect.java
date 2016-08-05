package org.api.bo;

import org.api.dto.ProspectDTO;
import org.json.JSONException;
import org.api.utils.CustomJSONObject;
import org.api.utils.JSONAble;
import org.api.utils.TechniqueException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Prospect implements JSONAble {
    public int id;
    public String name;
    public String firstname;
    public String email;

    public int mapOut(PreparedStatement pPreparedStatement) throws SQLException {
        int lIterator = 1;
        pPreparedStatement.setString(lIterator++, this.name);
        pPreparedStatement.setString(lIterator++, this.firstname);
        pPreparedStatement.setString(lIterator++, this.email);

        return lIterator;
    }

    public static Prospect mapIn(ResultSet pResultSet) throws SQLException {
        Prospect lProspect = new Prospect();
        lProspect.id = pResultSet.getInt("id");
        lProspect.name = pResultSet.getString("name");
        lProspect.firstname = pResultSet.getString("firstname");
        lProspect.email = pResultSet.getString("mail");

        return lProspect;
    }

    public CustomJSONObject toJSON() throws TechniqueException {
        CustomJSONObject lJSONObject = new CustomJSONObject();
        try {
            lJSONObject.put("id", this.id);
            lJSONObject.put("nom", this.name);
            lJSONObject.put("prenom", this.firstname);
            lJSONObject.put("email", this.email);
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

    public static Prospect createProspectFromDTO(ProspectDTO pProspectDTO) {
        Prospect lProspect = new Prospect();
        lProspect.name = pProspectDTO.name;
        lProspect.firstname = pProspectDTO.firstname;
        lProspect.email = pProspectDTO.email;

        return lProspect;
    }

}
