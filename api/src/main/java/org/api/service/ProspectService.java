package org.api.service;

import org.api.bo.Prospect;
import org.api.dto.ProspectDTO;
import org.api.manager.ProspectManager;
import org.api.utils.TechniqueException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("")
@Produces(MediaType.TEXT_PLAIN)
@Consumes({ "application/xml", "application/json" })
public class ProspectService extends ApiService{

    ProspectManager mProspectManager = new ProspectManager();

    @POST
    @Path("/prospect")
    @Consumes(MediaType.APPLICATION_JSON)
    public void postAProspect(ProspectDTO pProspectDTO) throws TechniqueException {
        try {
            Prospect lProspect = Prospect.createProspectFromDTO(pProspectDTO);
            mProspectManager.insertProspect(lProspect);
        }catch (TechniqueException e) {
            e.printStackTrace();
        }

    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProspect(@PathParam("id") int pId) throws TechniqueException {
        try {
            Prospect lProspect = mProspectManager.getProspectFromId(pId);

            return wrapResponse(lProspect);
        } catch (TechniqueException e) {
            e.printStackTrace();
            return internalError(e);
        }

    }
}
