package org.psc.share_food;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Main application configuration class that defines the base path for all REST endpoints
 * and enables JAX-RS functionality for the application.
 */
@ApplicationPath("/")
public class ShareFoodApplication extends Application {
}
