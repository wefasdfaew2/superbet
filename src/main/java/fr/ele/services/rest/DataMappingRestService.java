package fr.ele.services.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import fr.ele.model.DataMapping;
import fr.ele.model.search.DataMappingSearch;

@Path(DataMappingRestService.PATH)
public interface DataMappingRestService {

    public static final String PATH = "datamappings";

    static final String SERVER = "DataMappingRestService";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Iterable<DataMapping> findAll();

    @POST
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Iterable<DataMapping> search(DataMappingSearch search);

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    DataMapping get(@PathParam("id") long id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DataMapping create(DataMapping dataMapping);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") long id);

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("upload")
    List<DataMapping> insertCsv(
            @Multipart(value = "content", type = MediaType.WILDCARD) Attachment file);
}
