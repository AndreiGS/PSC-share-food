package org.psc.share_food.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.psc.share_food.dto.DonationRequestDto;
import org.psc.share_food.security.SecurityUtils;
import org.psc.share_food.security.UserDetail;
import org.psc.share_food.service.DonationRequestService;

import java.util.List;
import java.util.Optional;

@Path("/api/v1/donation-requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Donation Requests", description = "Donation Request Management APIs")
public class DonationRequestResource {

    @Inject
    private DonationRequestService donationRequestService;

    @Inject
    private SecurityUtils securityUtils;

    @Inject
    public DonationRequestResource(DonationRequestService donationRequestService, SecurityUtils securityUtils) {
        this.donationRequestService = donationRequestService;
        this.securityUtils = securityUtils;
    }

    @POST
    @RolesAllowed("ROLE_USER")
    @Operation(
        summary = "Create donation request",
        description = "Creates a new donation request for the authenticated user"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "201", 
            description = "Donation request created successfully",
            content = @Content(schema = @Schema(implementation = DonationRequestDto.class))
        ),
        @APIResponse(
            responseCode = "401", 
            description = "Unauthorized, missing or invalid authentication",
            content = @Content(schema = @Schema())
        ),
        @APIResponse(
            responseCode = "400", 
            description = "Bad request, invalid input data",
            content = @Content(schema = @Schema())
        )
    })
    public Response createDonationRequest(DonationRequestDto donationRequestDto) {
        Optional<UserDetail> userOptional = securityUtils.getAuthenticatedUser();
        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Not authenticated\"}")
                    .build();
        }

        try {
            DonationRequestDto createdDonationRequest = donationRequestService.createDonationRequest(donationRequestDto, userOptional.get());
            return Response.status(Response.Status.CREATED)
                    .entity(createdDonationRequest)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ROLE_USER")
    @Operation(
        summary = "Get donation request by ID",
        description = "Retrieves a donation request by its ID"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200", 
            description = "Donation request found and returned successfully",
            content = @Content(schema = @Schema(implementation = DonationRequestDto.class))
        ),
        @APIResponse(
            responseCode = "401", 
            description = "Unauthorized, missing or invalid authentication",
            content = @Content(schema = @Schema())
        ),
        @APIResponse(
            responseCode = "404", 
            description = "Donation request not found",
            content = @Content(schema = @Schema())
        )
    })
    public Response getDonationRequest(
            @Parameter(description = "Donation request ID", required = true)
            @PathParam("id") Long id) {
        Optional<DonationRequestDto> donationRequestOptional = donationRequestService.getDonationRequest(id);
        if (donationRequestOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Donation request not found\"}")
                    .build();
        }

        return Response.ok(donationRequestOptional.get()).build();
    }

    @GET
    @Path("/my-requests")
    @PermitAll
    @Operation(
        summary = "Get my donation requests",
        description = "Retrieves all donation requests for the authenticated user"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200", 
            description = "Donation requests retrieved successfully",
            content = @Content(schema = @Schema(implementation = DonationRequestDto.class))
        ),
        @APIResponse(
            responseCode = "401", 
            description = "Unauthorized, missing or invalid authentication",
            content = @Content(schema = @Schema())
        )
    })
    public Response getMyDonationRequests() {
        Optional<UserDetail> userOptional = securityUtils.getAuthenticatedUser();
        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Not authenticated\"}")
                    .build();
        }

        List<DonationRequestDto> donationRequests = donationRequestService.getDonationRequestsForUser(userOptional.get());
        return Response.ok(donationRequests).build();
    }
}
