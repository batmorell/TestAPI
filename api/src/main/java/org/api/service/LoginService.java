package org.api.service;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.api.dto.LoginRequestDTO;
import org.api.manager.AuthentificationManager;
import org.api.utils.TechniqueException;

@Path("/session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class LoginService extends ApiService {

    @Inject
    private AuthentificationManager mLoginManager;

    @POST
    @Path("/login")
    public Response loginRecruiter(@Context HttpHeaders pHeaders, LoginRequestDTO loginRequest) throws JSONException {
        try {
            String authToken = mLoginManager.logUser(loginRequest.email, loginRequest.password);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("authToken", authToken);
            return wrapResponse(responseJSON);
        } catch (TechniqueException ex) {
            return internalError(ex);
        }
    }

    @GET
    @Path("/logout")
    public Response logout(@Context HttpHeaders pHeaders, @Context HttpHeaders httpHeaders) throws JSONException {
        try {
            String authToken = httpHeaders.getHeaderString("authToken");
            mLoginManager.logout(authToken);
            return wrapResponse();
        } catch (TechniqueException ex) {
            return internalError(ex);
        }
    }

}
