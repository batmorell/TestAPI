package org.api.manager;

import java.io.Serializable;
import java.sql.Statement;
import java.util.Date;
import org.api.bo.User;
import org.api.dto.UserDTO;
import org.api.enumeration.AccessLevelEnum;
import org.api.utils.Security;
import org.api.utils.TechniqueException;
import org.api.utils.Utils;

public class UserManager extends Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    protected User getUserById(int pId) throws TechniqueException {
        String lSqlQuery = "SELECT * FROM users WHERE users.id = ?;";
        return select(lSqlQuery, lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setInt(lIterator++, pId);
        }, lResultSet -> {
            if (lResultSet.next()) {
                return User.mapIn(lResultSet);
            }
            throw new TechniqueException("Utilisateur non trouvé");
        });
    }

    protected User getUserByEmail(String pEmail) throws TechniqueException {
        String lSqlQuery = "SELECT * FROM users WHERE users.email = ?;";
        return select(lSqlQuery, lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pEmail);
        }, lResultSet -> {
            if (lResultSet.next()) {
                return User.mapIn(lResultSet);
            }
            throw new TechniqueException("Utilisateur non trouvé");
        });
    }

    protected boolean isEmailExisting(String pEmail, int pId) throws TechniqueException {
        return select("SELECT * FROM users WHERE email = ? AND id != ? AND  users.active = 1", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pEmail);
            lPreparedStatement.setInt(lIterator++, pId);
        }, lResultSet -> {
            if (lResultSet.next())
                return true;
            return false;
        });
    }

    protected boolean isEmailExisting(String pEmail) throws TechniqueException {
        return select("SELECT * FROM users WHERE email = ?", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pEmail);
        }, lResultSet -> {
            if (lResultSet.next())
                return true;
            return false;
        });
    }

    protected User getUserFromExistingAuthToken(String pAuthToken) throws TechniqueException {
        return getUserByAuthToken(pAuthToken);
    }

    protected User getUserByAuthToken(String pAuthToken) throws TechniqueException {
        String lSqlQuery = "SELECT * FROM users LEFT JOIN user_session ON users.id = user_session.id_user WHERE session_id = ? AND  users.active = 1;";
        return select(lSqlQuery, lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pAuthToken);
        }, lResultSet -> {
            if (lResultSet.next()) {
                return User.mapIn(lResultSet);
            }
            throw new TechniqueException("Utilisateur non trouvé");
        });
    }

    protected void updateSessionTimeOut(String pToken) throws TechniqueException {
        update("UPDATE user_session SET last_seen = ? WHERE session_id = ?", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(new Date()));
            lPreparedStatement.setString(lIterator++, pToken);
        });
    }

    protected int createFromDTO(UserDTO pUserDTO) throws TechniqueException {
        if (pUserDTO.email == null || pUserDTO.email.trim().isEmpty()) {
            throw new TechniqueException("L'email ne peut pas être vide.");
        } else if (pUserDTO.firstName == null || pUserDTO.firstName.trim().isEmpty()) {
            throw new TechniqueException("Le prénom ne peut pas être vide.");
        } else if (pUserDTO.lastName == null || pUserDTO.lastName.trim().isEmpty()) {
            throw new TechniqueException("Le nom ne peut pas être vide.");
        } else if (pUserDTO.profil == null || pUserDTO.profil.trim().isEmpty()) {
            throw new TechniqueException("Le profil ne peut pas être vide.");
        } else if (pUserDTO.company == null || pUserDTO.company.trim().isEmpty()) {
            throw new TechniqueException("L'entreprise ne peut pas être vide.");
        } else if (pUserDTO.password == null || pUserDTO.password.trim().isEmpty()) {
            throw new TechniqueException("Le mot de passe ne peut pas être vide.");
        } else if (isEmailExisting(pUserDTO.email)) {
            throw new TechniqueException(pUserDTO.email + " est déjà utilisé par un autre utilisateur.");
        }
        String lQueryUser = "INSERT INTO users(email, first_name, last_name, profil, company, password, ts_creation, access_level) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        int lInsertedId = update(lQueryUser, lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pUserDTO.email);
            lPreparedStatement.setString(lIterator++, pUserDTO.firstName);
            lPreparedStatement.setString(lIterator++, pUserDTO.lastName);
            lPreparedStatement.setString(lIterator++, pUserDTO.profil);
            lPreparedStatement.setString(lIterator++, pUserDTO.company);
            lPreparedStatement.setString(lIterator++, AccessLevelEnum.USER.name());
            try {
                lPreparedStatement.setString(lIterator++, Security.saltedSHA1(pUserDTO.password));
            } catch (TechniqueException ex) {
                ex.printStackTrace();
            }
            lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(new Date()));
        }, Statement.RETURN_GENERATED_KEYS);

        return lInsertedId;
    }

    protected void updateFromDTO(UserDTO pUserDTO) throws TechniqueException {
        if (pUserDTO.email == null || pUserDTO.email.trim().isEmpty()) {
            throw new TechniqueException("L'email ne peut pas être vide.");
        } else if (pUserDTO.firstName == null || pUserDTO.firstName.trim().isEmpty()) {
            throw new TechniqueException("Le prénom ne peut pas être vide.");
        } else if (pUserDTO.lastName == null || pUserDTO.lastName.trim().isEmpty()) {
            throw new TechniqueException("Le nom ne peut pas être vide.");
        } else if (isEmailExisting(pUserDTO.email, pUserDTO.id)) {
            throw new TechniqueException(pUserDTO.email + " est déjà utilisé par un autre utilisateur.");
        }

        update("UPDATE users SET email= ?,first_name= ?,last_name= ?, ts_update = ? WHERE id = ?;",
                lPreparedStatement -> {
                    int lIterator = 1;
                    lPreparedStatement.setString(lIterator++, pUserDTO.email);
                    lPreparedStatement.setString(lIterator++, pUserDTO.firstName);
                    lPreparedStatement.setString(lIterator++, pUserDTO.lastName);
                    lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(new Date()));
                    lPreparedStatement.setInt(lIterator++, pUserDTO.id);
                }, Statement.RETURN_GENERATED_KEYS);
    }

    protected void deleteUser(int pIdUser) throws TechniqueException {
        update("UPDATE users SET active = 0, ts_update = ? WHERE id = ?", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setTimestamp(lIterator++, Utils.convertDateToTimestamp(new Date()));
            lPreparedStatement.setInt(lIterator++, pIdUser);
        });
    }

    protected User getForAuthentication(String pEmail) throws TechniqueException {
        String lSqlQuery = "SELECT * FROM users WHERE email = ?";
        return select(lSqlQuery, lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setString(lIterator++, pEmail);
        }, lResultSet -> {
            if (lResultSet.next()) {
                return User.mapInWithPassword(lResultSet);
            }
            throw new TechniqueException("Utilisateur non trouvé");
        });
    }

    protected void setPassword(int pId, String pPassword) throws TechniqueException {
        update("UPDATE users SET password = ? WHERE id = ?", lPreparedStatement -> {
            int lIterator = 1;
            try {
                lPreparedStatement.setString(lIterator++, Security.saltedSHA1(pPassword));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            lPreparedStatement.setInt(lIterator++, pId);
        });
    }

    protected void setAccountActive(User pUser) throws TechniqueException {
        update("UPDATE users SET active = 1 WHERE id = ?", lPreparedStatement -> {
            int lIterator = 1;
            lPreparedStatement.setInt(lIterator++, pUser.getId());
        });
    }
}
