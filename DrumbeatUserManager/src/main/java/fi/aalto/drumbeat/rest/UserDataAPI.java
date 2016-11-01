package fi.aalto.drumbeat.rest;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import fi.aalto.drumbeat.DrumbeatUserManager.DrumbeatGraphDatabase;
 

@Path("data")
public class UserDataAPI{
	
	@GET
	@Path("/setname/{id}.{name}")
    public Response settName(@PathParam("id") String id,@PathParam("name") String name) {
		/*GraphDatabase g=GraphDatabase.getInstance();
		Graph graph=g.getGraph();
		
		RDFTermFactory rft = new RDFTermFactoryJena() ;
	        graph.add(rft.createIRI("http://drumbeat/company/"+id),
	                  rft.createIRI("http://drumbeat/name"),
	                  rft.createLiteral(name)) ;
	    g.write_triples();
	    g.save();
		return Response.status(200).entity(g.toString()).build();*/
		return Response.status(200).entity("Just OK now").build();
    }
	
	@GET
	@Path("")
    public Response getAll() {
		DrumbeatGraphDatabase db =DrumbeatGraphDatabase.getInstance();
		
		return Response.status(200).entity(db.getJSON_LDContent()).build();
    }
	
	
	@POST
	@Path("/checkClaim")
    public Response checkClaim(@PathParam("id") String id,@PathParam("name") String name) {
		//WebID
		// Rule
		// rule params: company jne
		// transitivity
		/*GraphDatabase g=GraphDatabase.getInstance();
		Graph graph=g.getGraph();
		
		RDFTermFactory rft = new RDFTermFactoryJena() ;
	        graph.add(rft.createIRI("http://drumbeat/company/"+id),
	                  rft.createIRI("http://drumbeat/name"),
	                  rft.createLiteral(name)) ;
	    g.write_triples();
	    g.save();
		return Response.status(200).entity(g.toString()).build();*/
		return Response.status(200).entity("Just OK now").build();
    }
}