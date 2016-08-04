package org.api.manager;

import org.api.bo.Prospect;
import org.api.utils.TechniqueException;

import javax.enterprise.inject.Model;
import java.io.Serializable;
import java.sql.Statement;

@Model
public class ProspectManager extends Manager {

     public int insertProspect(Prospect pProspect) throws TechniqueException {
        return update(
                "INSERT INTO prospects(name, firstname, mail)"
                        + "VALUES (?,?,?)",
                lPreparedStatement -> {
                    pProspect.mapOut(lPreparedStatement);
                }, Statement.RETURN_GENERATED_KEYS);
    }

    public Prospect getProspectFromId(int pId) throws TechniqueException {
        return select("SELECT * FROM prospects WHERE id = ?", lPreparedStatement -> {
            lPreparedStatement.setInt(1, pId);
        }, lResultSet -> {
            if (lResultSet.next()) {
                return Prospect.mapIn(lResultSet);
            }
            throw new TechniqueException("Prospect utilisateur non trouvé");
        });
    }
}