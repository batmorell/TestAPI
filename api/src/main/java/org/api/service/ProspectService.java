package org.api.service;

import org.api.bo.Prospect;
import org.api.dto.ProspectDTO;
import org.api.manager.Manager;
import org.api.manager.ProspectManager;
import org.api.utils.TechniqueException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/prospect")
@Produces(MediaType.TEXT_PLAIN)
@Consumes({ "application/xml", "application/json" })
public class ProspectService extends ApiService{

    @Inject
    private ProspectManager mProspectManager ;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void postAProspect(ProspectDTO pProspectDTO) throws TechniqueException {
        try {
            Prospect lProspect = Prospect.createProspectFromDTO(pProspectDTO);
            mProspectManager.insertProspect(lProspect);
        }catch (TechniqueException e) {
            e.printStackTrace();
        }

    }

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateAProspect(ProspectDTO pProspectDTO) throws TechniqueException {
        try {
            Prospect lProspect = Prospect.createProspectFromDTO(pProspectDTO);
            mProspectManager.updateProspect(lProspect);
        }catch (TechniqueException e) {
            e.printStackTrace();
        }

    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProspect(@PathParam("id") int pId) throws TechniqueException {
        Prospect lProspect =  mProspectManager.getProspectFromId(pId);
        return wrapResponse(lProspect);
    }

    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getListOfProspects() throws TechniqueException {
        List<Prospect> lProspect =  mProspectManager.getListOfProspects();
        return wrapJSONAbleResponse(lProspect);
    }


}
