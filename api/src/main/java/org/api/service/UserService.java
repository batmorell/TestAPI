package org.api.service;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import org.api.bo.Token;
import org.api.bo.User;
import org.api.dto.UserDTO;
import org.api.enumeration.TokenTypeEnum;
import org.api.manager.AuthentificationManager;
import org.api.manager.BasicUserManager;
import org.api.manager.TokenManager;
import org.api.utils.TechniqueException;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class UserService extends ApiService {

    @Inject
    private BasicUserManager mBasicUserManager;
    @Inject
    private AuthentificationManager mAuthenticationManager;
    @Inject
    private TokenManager mTokenManager;

    @GET
    @Path("/fromAuthToken/{authToken}")
    public Response getUserFromAuthToken(@Context HttpHeaders pHeaders,
                                         @PathParam(value = "authToken") String pAuthToken) throws TechniqueException, JSONException {
        if (mAuthenticationManager.sessionExists(pAuthToken)) {
            User lUser = mBasicUserManager.getUserFromExistingAuthToken(pAuthToken);

            return wrapResponse(lUser);

        } else {
            return wrapResponse(new TechniqueException("Votre session a expirée, veuillez vous reconnecter"),
                    Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/")
    public Response insertUser(@Context HttpHeaders pHeaders, UserDTO pUserDTO) throws JSONException {
        User lUser;
        String lToken;
        try {
            int lIdInserted = mBasicUserManager.createFromDTO(pUserDTO);
            lUser = mBasicUserManager.getUserByEmail(pUserDTO.email);
            lToken = mTokenManager.create(lUser.getId(), TokenTypeEnum.ACCOUNT_ACTIVATION);
            return wrapResponse(new JSONObject().put("insertedId", lIdInserted));
        } catch (TechniqueException ex) {
            return internalError(ex);
        }
    }

    @POST
    @Path("/activate/{token}")
    public Response insertUser(@Context HttpHeaders pHeaders, @PathParam(value = "token") String pToken)
            throws JSONException {
        Token lToken;
        String lNewToken;
        User lUser;
        try {
            lToken = mTokenManager.get(pToken);
            if (lToken == null) {
                return internalError(new TechniqueException("Ce token n'exite pas"));
            }if (lToken.hasExpired()) {
                mTokenManager.delete(lToken.getValue());
                lUser = mBasicUserManager.getUserById(lToken.getIdUser());
                lNewToken = mTokenManager.create(lUser.getId(), TokenTypeEnum.ACCOUNT_ACTIVATION);
                return internalError(new TechniqueException("Ce token a expiré. Un nouvel email d'activaion va vous être envoyé."));
            }
            mBasicUserManager.setAccountActive(mBasicUserManager.getUserById(lToken.getIdUser()));
            mTokenManager.delete(pToken);
            return wrapResponse();
        } catch (TechniqueException ex) {
            return internalError(ex);
        }
    }
}
