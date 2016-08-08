package org.api.manager;

import org.api.bo.Prospect;
import org.api.utils.TechniqueException;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.api.manager.Manager;

public class ProspectManager extends Manager {

    public int insertProspect(Prospect pProspect) throws TechniqueException {
        return update(
                "INSERT INTO prospects(name, firstname, mail, company, companysecteur, companyadress, prospectfunction, phone, origin, description, other, statut)"
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                lPreparedStatement -> {
                    pProspect.mapOut(lPreparedStatement);
                }, Statement.RETURN_GENERATED_KEYS);
    }

    public List<Prospect> getListOfProspects() throws TechniqueException{
        return select("SELECT * FROM prospects",
                null , lResultSet -> {
                        List<Prospect> lProspects = new ArrayList<>();
                        while (lResultSet.next()) {
                            lProspects.add(Prospect.mapIn(lResultSet));
                        }
                        return lProspects;
                });
    }

    public int updateProspect(Prospect pProspect) throws TechniqueException {
        return update(
                "UPDATE prospects SET name=?, firstname=?, mail=?, company=?, companysecteur=?, companyadress=?, prospectfunction=?, phone=?, origin=?, description=?, other=?, statut=?"
                        + "WHERE id=?;",
                lPreparedStatement -> {
                    pProspect.mapOutUpdate(lPreparedStatement);
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
