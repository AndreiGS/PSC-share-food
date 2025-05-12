package org.psc.share_food.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.security.SecurityUtils;
import org.psc.share_food.security.UserDetail;
import org.psc.share_food.service.UserService;

import java.util.Optional;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "User Management APIs")
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private SecurityUtils securityUtils;

    @Inject
    public UserResource(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves detailed user information by user ID. Requires admin privileges."
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200", 
            description = "User found and returned successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @APIResponse(
            responseCode = "401", 
            description = "Unauthorized, missing or invalid authentication",
            content = @Content(schema = @Schema())
        ),
        @APIResponse(
            responseCode = "403", 
            description = "Forbidden, user doesn't have admin role",
            content = @Content(schema = @Schema())
        ),
        @APIResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema())
        )
    })
    public Response getUser(
            @Parameter(description = "User ID", required = true)
            @PathParam("id") Long id) {
        Optional<UserDto> userOptional = userService.getUser(id);
        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"User not found\"}")
                    .build();
        }

        return Response.ok(userOptional.get()).build();
    }

    @GET
    @Path("/me")
    @PermitAll
    @Operation(
        summary = "Get current user",
        description = "Retrieves information about the currently authenticated user"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200", 
            description = "Current user information retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserDetail.class))
        ),
        @APIResponse(
            responseCode = "401", 
            description = "Unauthorized, missing or invalid authentication",
            content = @Content(schema = @Schema())
        )
    })
    public Response getCurrentUser(@Context HttpServletRequest httpServletRequest) {
        Optional<UserDetail> userOptional = securityUtils.getAuthenticatedUser();

        System.out.println("Caller roles: " + httpServletRequest.isUserInRole("ADMIN"));
        System.out.println("Caller roles: " + httpServletRequest.isUserInRole("ROLE_ADMIN"));

        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Not authenticated\"}")
                    .build();
        }

        return Response.ok(userOptional.get()).build();
    }
}
