package ru.fitgraph.engine.secure;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by melges on 18.12.14.
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException, WebApplicationException {
        // Get method which will be invoked by jersey if we don't
        Method resourceMethod = resourceInfo.getResourceMethod();

        if(resourceMethod.isAnnotationPresent(PermitAll.class))
            return; // Simply return and continue work if resource available for all

        if(resourceMethod.isAnnotationPresent(DenyAll.class))
            throw new ForbiddenException("Forbidden");

        if(resourceMethod.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowed = resourceMethod.getAnnotation(RolesAllowed.class);
            String[] allowedRoles = rolesAllowed.value();
        }


    }
}
