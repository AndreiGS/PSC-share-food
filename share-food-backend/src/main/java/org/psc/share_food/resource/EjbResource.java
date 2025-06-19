package org.psc.share_food.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.psc.share_food.ejb.DonationProcessorRemoteClient;

@Path("/api/v1/ejb")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "EJB", description = "EJB APIs")
public class EjbResource {

    @Inject
    public DonationProcessorRemoteClient donationProcessorRemoteClient;

    @POST
    @Path("/{id}")
    @PermitAll
    public Response postStatus(
            @Parameter(description = "Donation request ID", required = true)
            @PathParam("id") Long id) {
        boolean result = donationProcessorRemoteClient.processDonationRequest(id);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}/status")
    @PermitAll
    public Response getStatus(
            @Parameter(description = "Donation request ID", required = true)
            @PathParam("id") Long id) {
        String result = donationProcessorRemoteClient.getDonationRequestStatus(id);
        return Response.ok(result).build();
    }

}
