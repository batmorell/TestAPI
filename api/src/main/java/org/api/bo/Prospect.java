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
    public String company;
    public String companySecteur;
    public String companyAdress;
    public String prospectFunction;
    public String phone;
    public String origin;
    public String description;
    public String other;
    public String statut;

    public int mapOut(PreparedStatement pPreparedStatement) throws SQLException {
        int lIterator = 1;
        pPreparedStatement.setString(lIterator++, this.name);
        pPreparedStatement.setString(lIterator++, this.firstname);
        pPreparedStatement.setString(lIterator++, this.email);
        pPreparedStatement.setString(lIterator++, this.company);
        pPreparedStatement.setString(lIterator++, this.companySecteur);
        pPreparedStatement.setString(lIterator++, this.companyAdress);
        pPreparedStatement.setString(lIterator++, this.prospectFunction);
        pPreparedStatement.setString(lIterator++, this.phone);
        pPreparedStatement.setString(lIterator++, this.origin);
        pPreparedStatement.setString(lIterator++, this.description);
        pPreparedStatement.setString(lIterator++, this.other);
        pPreparedStatement.setString(lIterator++, this.statut);

        return lIterator;
    }

    public void mapOutUpdate(PreparedStatement pPreparedStatement) throws SQLException {
        int lIterator = mapOut(pPreparedStatement);
        pPreparedStatement.setInt(lIterator++, this.id);
    }

    public static Prospect mapIn(ResultSet pResultSet) throws SQLException {
        Prospect lProspect = new Prospect();
        lProspect.id = pResultSet.getInt("id");
        lProspect.name = pResultSet.getString("name");
        lProspect.firstname = pResultSet.getString("firstname");
        lProspect.email = pResultSet.getString("mail");
        lProspect.company = pResultSet.getString("company");
        lProspect.companySecteur = pResultSet.getString("companysecteur");
        lProspect.companyAdress = pResultSet.getString("companyadress");
        lProspect.prospectFunction = pResultSet.getString("prospectfunction");
        lProspect.phone = pResultSet.getString("phone");
        lProspect.origin = pResultSet.getString("origin");
        lProspect.description = pResultSet.getString("description");
        lProspect.other = pResultSet.getString("other");
        lProspect.statut = pResultSet.getString("statut");

        return lProspect;
    }

    public CustomJSONObject toJSON() throws TechniqueException {
        CustomJSONObject lJSONObject = new CustomJSONObject();
        try {
            lJSONObject.put("id", this.id);
            lJSONObject.put("nom", this.name);
            lJSONObject.put("prenom", this.firstname);
            lJSONObject.put("email", this.email);
            lJSONObject.put("entreprise", this.company);
            lJSONObject.put("secteur", this.companySecteur);
            lJSONObject.put("adresse", this.companyAdress);
            lJSONObject.put("fonction", this.prospectFunction);
            lJSONObject.put("telephone", this.phone);
            lJSONObject.put("provenance", this.origin);
            lJSONObject.put("description", this.description);
            lJSONObject.put("autre", this.other);
            lJSONObject.put("statut", this.statut);
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
        lProspect.id = pProspectDTO.id;
        lProspect.name = pProspectDTO.nom;
        lProspect.firstname = pProspectDTO.prenom;
        lProspect.email = pProspectDTO.email;
        lProspect.company = pProspectDTO.entreprise;
        lProspect.companySecteur = pProspectDTO.secteur;
        lProspect.companyAdress = pProspectDTO.adresse;
        lProspect.prospectFunction = pProspectDTO.fonction;
        lProspect.phone = pProspectDTO.telephone;
        lProspect.origin = pProspectDTO.provenance;
        lProspect.description = pProspectDTO.description;
        lProspect.other = pProspectDTO.autre;
        lProspect.statut = pProspectDTO.statut;
        return lProspect;
    }

}
