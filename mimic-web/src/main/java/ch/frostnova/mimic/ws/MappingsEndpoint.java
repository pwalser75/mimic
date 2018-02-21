package ch.frostnova.mimic.ws;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.service.MappingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Mimic mappings web service endpoint <p>
 * Full local path: <a href="https://localhost/api/mappings">https://localhost/api/mappings</a>
 */
@Component
@Path("mappings")
@Api(value = "Mimic Mapping resource", produces = "application/json")
public class MappingsEndpoint {

    @Autowired
    private MappingService mappingService;

    /**
     * List mappings
     *
     * @return list of mappings (never null)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Lists all mappings", response = MimicMapping.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful")
    })
    public List<MimicMapping> list() {
        return mappingService.list();
    }

    /**
     * Get a record by id. If the record was not found, a NoSuchElementException will be thrown (resulting in a 404 NOT
     * FOUND).
     *
     * @param id id of the record
     * @return record
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MimicMapping get(@PathParam("id") String id) {
        MimicMapping result = mappingService.get(id);
        if (result != null) {
            return result;
        }
        throw new NoSuchElementException();
    }

    /**
     * Create a new record (or updates an existing record, when the id is set).
     *
     * @param mapping record to create
     * @return created record
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MimicMapping save(MimicMapping mapping) {
        return mappingService.save(mapping);
    }

    /**
     * Update a record
     *
     * @param id      id of the record to update
     * @param mapping new data to set
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") String id, MimicMapping mapping) {
        mapping.setId(id);
        mappingService.save(mapping);
    }

    /**
     * Delete a record
     *
     * @param id id of the record
     */
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        mappingService.delete(id);
    }
}
