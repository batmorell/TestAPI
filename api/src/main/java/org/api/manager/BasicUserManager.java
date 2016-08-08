package org.api.manager;


import java.io.Serializable;
import org.api.bo.User;
import org.api.dto.UserDTO;
import org.api.utils.TechniqueException;

public class BasicUserManager extends UserManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public User getUserById(int pId) throws TechniqueException {

        return super.getUserById(pId);
    }

    @Override
    public User getUserByEmail(String pEmail) throws TechniqueException {

        return super.getUserByEmail(pEmail);
    }

    @Override
    protected boolean isEmailExisting(String pEmail, int pId) throws TechniqueException {
        return super.isEmailExisting(pEmail, pId);
    }

    @Override
    protected boolean isEmailExisting(String pEmail) throws TechniqueException {
        return super.isEmailExisting(pEmail);
    }

    @Override
    public User getUserFromExistingAuthToken(String pAuthToken) throws TechniqueException {

        return super.getUserFromExistingAuthToken(pAuthToken);
    }

    @Override
    public User getUserByAuthToken(String pAuthToken) throws TechniqueException {

        return super.getUserByAuthToken(pAuthToken);
    }

    @Override
    public void updateSessionTimeOut(String pToken) throws TechniqueException {

        super.updateSessionTimeOut(pToken);
    }

    @Override
    public int createFromDTO(UserDTO pUserDTO) throws TechniqueException {

        return super.createFromDTO(pUserDTO);
    }

    @Override
    public void updateFromDTO(UserDTO pUserDTO) throws TechniqueException {

        super.updateFromDTO(pUserDTO);
    }

    @Override
    public void deleteUser(int pIdUser) throws TechniqueException {

        super.deleteUser(pIdUser);
    }

    @Override
    public User getForAuthentication(String pEmail) throws TechniqueException {

        return super.getForAuthentication(pEmail);
    }

    @Override
    public void setPassword(int pId, String pPassword) throws TechniqueException {

        super.setPassword(pId, pPassword);
    }

    @Override
    public void setAccountActive(User pUser) throws TechniqueException {

        super.setAccountActive(pUser);
    }

}

