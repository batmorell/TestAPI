package org.api.service;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.api.utils.TechniqueException;
import org.api.manager.Manager;

@Path("/route")
@Produces(MediaType.TEXT_PLAIN)
@Consumes({ "application/xml", "application/json" })
public class test extends Manager{

	@GET
	@Path("/")
	public String getAString() throws TechniqueException {
			return "Success";
	}

	@GET
	@Path("/route")
	public String getARoute() throws TechniqueException {
		return "route 2";
	}
}
