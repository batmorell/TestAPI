package org.api.manager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.api.bo.Token;
import org.api.bo.User;
import org.api.enumeration.TokenTypeEnum;
import org.api.utils.TechniqueException;
import org.api.utils.Utils;

public class TokenManager extends Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    public String create(int pIdUser, TokenTypeEnum pType) throws TechniqueException {
        String lToken = UUID.randomUUID().toString();

        Calendar cal = Calendar.getInstance();
        Date lCreationDate = new Date(cal.getTimeInMillis());
        cal.add(Calendar.DATE, 1);
        Date lExpirationDate = new Date(cal.getTimeInMillis());
        update("INSERT INTO token ( id_user, value, type, ts_creation, ts_expiration) VALUES (?, ?, ?, ?, ?);",
                lPreparedStatement -> {
                    int lIterator = 1;
                    lPreparedStatement.setInt(lIterator++, pIdUser);
                    lPreparedStatement.setString(lIterator++, lToken);
                    lPreparedStatement.setString(lIterator++, pType.name());
                    lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(lCreationDate));
                    lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(lExpirationDate));
                });
        return lToken;
    }

    public Token get(String pToken) throws TechniqueException {
        return select("SELECT * FROM token WHERE value = ?", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pToken);
        }, lResultSet -> {
            if (lResultSet.next()) {
                return Token.mapIn(lResultSet);
            }
            return null;
        });
    }

    public void delete(String pToken) throws TechniqueException {
        update("DELETE FROM token WHERE value = ?", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pToken);
        });
    }

    public void delete(User pUser, TokenTypeEnum pType) throws TechniqueException {
        update("DELETE FROM token WHERE id_user = ? AND type = ?;", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setInt(lIterator++, pUser.getId());
            lPreparedStatement.setString(lIterator++, pType.name());
        });
    }
}

