package org.psc.share_food.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
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
import org.psc.share_food.service.impl.GenericOAuthService;

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
    @PermitAll
    @Path("/callback")
    public Response handleCallback(
            @Parameter(description = "OAuth provider (e.g., GOOGLE, FACEBOOK)")
            @QueryParam("provider") OAuthProvider provider,
            @Parameter(description = "Authorization code from OAuth provider")
            @QueryParam("code") String code,
            @Context HttpServletRequest request) {

        System.out.println("OAuth provider: " + provider);
        OAuthService oAuthService = OAuthService.getOAuthServiceByName(provider);
        Optional<UserDto> userOptional = oAuthService.authenticate(code, provider);
        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UserDto user = userOptional.get();

        // Create a session and get the cookie
        NewCookie sessionCookie = authService.createSession(user, isSecure(request));

        Response.ResponseBuilder responseBuilder;
        if (user.isNew()) {
            responseBuilder = Response.status(Response.Status.CREATED);
        } else {
            responseBuilder = Response.ok();
        }

        return responseBuilder
                .entity(user)
                .cookie(sessionCookie)
                .build();
    }

    @POST
    @Path("/logout")
    @PermitAll
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
    public Response logout(@Context HttpServletRequest request) {
        // Invalidate the session and get an expired cookie
        NewCookie expiredCookie = authService.invalidateSession(isSecure(request));

        return Response.ok()
                .cookie(expiredCookie)
                .build();
    }

    private boolean isSecure(HttpServletRequest request) {
        String proto = request.getHeader("X-Forwarded-Proto");
        return "https".equalsIgnoreCase(proto) || request.isSecure();
    }
}
