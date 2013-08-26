package fr.ele.services.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.ele.model.ref.BookMaker;

@Path(BookMakerRestService.PATH)
public interface BookMakerRestService {

    public static final String PATH = "bookmakers";

    public static final String SERVER = "BookMakerRestService";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<BookMaker> findAll();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    BookMaker get(@PathParam("id") long id);

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    BookMaker findByCode(@QueryParam("code") String code);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    BookMaker create(BookMaker bookmaker);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);
}
