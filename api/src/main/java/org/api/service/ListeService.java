package org.api.service;

import org.api.bo.Prospect;
import org.api.bo.SelectOption;
import org.api.dto.ProspectDTO;
import org.api.manager.ListeManager;
import org.api.manager.Manager;
import org.api.manager.ProspectManager;
import org.api.utils.TechniqueException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/liste")
@Produces(MediaType.TEXT_PLAIN)
@Consumes({ "application/xml", "application/json" })
public class ListeService extends ApiService{

    @Inject
    private ListeManager mListeManager;


    @GET
    @Path("/{table}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProspect(@PathParam("table") String pTable) throws TechniqueException {
        List<SelectOption> lSelectOptions=  mListeManager.getListForTable(pTable);
        return wrapJSONAbleResponse(lSelectOptions);
    }

}
