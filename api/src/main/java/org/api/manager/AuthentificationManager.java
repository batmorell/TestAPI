package org.api.manager;

import java.util.Date;
import java.util.UUID;
import javax.inject.Inject;
import org.api.bo.User;
import org.api.config.Config;
import org.api.utils.Security;
import org.api.utils.TechniqueException;
import org.api.utils.Utils;

public class AuthentificationManager extends Manager {

    private static final long serialVersionUID = 1L;

    @Inject
    private BasicUserManager mBasicUserManger;

    public String logUser(String pEmail, String pPassword) throws TechniqueException {
        User lUser = mBasicUserManger.getForAuthentication(pEmail);
        if (lUser != null) {
            if (!lUser.isActive()) {
                throw new TechniqueException(
                        "Vous n'avez pas confirmé la création de votre compte. Pour le faire, veuillez accéder à l'URL qui vous à été envoyée par email, vous pouvez refaire une demande.");
            }
            if (lUser.getPassword().equals(Security.saltedSHA1(pPassword))) {
                return createSession(lUser);
            }
            throw new TechniqueException("Mot de passe incorrect");
        }
        throw new TechniqueException("Utilisateur non trouvé");
    }

    public void logout(String pAuthToken) throws TechniqueException {
        update("DELETE FROM user_session WHERE session_id = ?;", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pAuthToken);
        });
    }

    public String createSession(User pUser) throws TechniqueException {
        String lUuid = UUID.randomUUID().toString();
        cleanSessions();
        update("INSERT INTO user_session (id_user, session_id, ts_creation, last_seen) VALUES (?, ?, ?,?) ;",
                lPreparedStatement -> {
                    int lIterator = 1;
                    lPreparedStatement.setInt(lIterator++, pUser.getId());
                    lPreparedStatement.setString(lIterator++, lUuid);
                    lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(new Date()));
                    lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(new Date()));
                });
        return lUuid;
    }

    public boolean sessionExists(String pAuthToken) throws TechniqueException {
        cleanSessions();
        return select("SELECT * FROM user_session WHERE session_id = ?;", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pAuthToken);
        }, lResultSet -> {
            if (lResultSet.next()) {
                return true;
            }
            return false;
        });
    }

    private void cleanSessions() throws TechniqueException {
        /*update("DELETE FROM user_session WHERE (EXTRACT(EPOCH FROM current_timestamp - last_seen)/3600) > ?;",
                lPreparedStatement -> {
                    int lIterator = 1;
                    lPreparedStatement.setInt(lIterator++, Config.SESSION_MAX_TIME);
                });*/
    }

}

