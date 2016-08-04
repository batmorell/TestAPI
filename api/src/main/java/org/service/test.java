package org.service;

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

import org.utils.TechniqueException;
import org.manager.Manager;

@RequestScoped
@Path("")
@Produces(MediaType.TEXT_PLAIN)
@Consumes({ "application/xml", "application/json" })
public class test extends Manager{

	@GET
	@Path("/")
	public String getAString() throws TechniqueException {
		
		return select("SELECT * FROM users", null, lResultSet -> {
			if (lResultSet.next()){
				return lResultSet.getString("name");
			}
			return "error";
		});

	}
}
