package org.psc.share_food.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.psc.share_food.dto.UserDto;
import org.psc.share_food.constant.OAuthProvider;
import org.psc.share_food.service.AuthenticationService;
import org.psc.share_food.service.OAuthService;

import java.util.Optional;

@Path("/api/v1/oauth")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "OAuth Authentication APIs")
public class OAuthResource {

    @Inject
    private AuthenticationService authService;
    
    @POST
    @Path("/callback")
    @Operation(
        summary = "Handle OAuth callback",
        description = "Processes OAuth callback with authorization code to authenticate user"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200", 
            description = "Authentication successful for existing user",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @APIResponse(
            responseCode = "201", 
            description = "Authentication successful for new user",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @APIResponse(
            responseCode = "400", 
            description = "Invalid OAuth code or authentication failed",
            content = @Content(schema = @Schema())
        )
    })
    public Response handleCallback(
            @Parameter(description = "OAuth provider (e.g., GOOGLE, FACEBOOK)")
            @QueryParam("provider") OAuthProvider provider,
            @Parameter(description = "Authorization code from OAuth provider") 
            @QueryParam("code") String code) {
        System.out.println("OAuth provider: " + provider);
        OAuthService oAuthService = OAuthService.getOAuthServiceByName(provider);
        Optional<UserDto> userOptional = oAuthService.authenticate(code);
        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UserDto user = userOptional.get();

        // Create a session and get the cookie
        NewCookie sessionCookie = authService.createSession(user);

        Response.ResponseBuilder responseBuilder;
        if (user.isNew()) {
            responseBuilder = Response.status(Response.Status.CREATED);
        } else {
            responseBuilder = Response.ok();
        }

        // Return the user info and set the session cookie
        return responseBuilder
                .entity(user)
                .cookie(sessionCookie)
                .build();
    }

    @POST
    @Path("/logout")
    @Operation(
        summary = "Logout user",
        description = "Invalidates the user's session and clears authentication cookie"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200", 
            description = "Logout successful",
            content = @Content(schema = @Schema())
        ),
        @APIResponse(
            responseCode = "401", 
            description = "Unauthorized, no valid session",
            content = @Content(schema = @Schema())
        )
    })
    public Response logout() {
        // Invalidate the session and get an expired cookie
        NewCookie expiredCookie = authService.invalidateSession();

        return Response.ok()
                .cookie(expiredCookie)
                .build();
    }
}
