/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package io.narayana.lra.coordinator.api;

import io.narayana.lra.coordinator.domain.model.LRAStatusHolder;
import io.narayana.lra.coordinator.domain.service.LRAService;
import io.narayana.lra.logging.LRALogger;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static io.narayana.lra.LRAConstants.RECOVERY_COORDINATOR_PATH_NAME;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@ApplicationScoped
@Path(RECOVERY_COORDINATOR_PATH_NAME)
@Tag(name = "LRA Recovery")
public class RecoveryCoordinator {

    private final Logger logger = Logger.getLogger(RecoveryCoordinator.class.getName());

    @Context
    private UriInfo context;

    @Inject
    LRAService lraService;

    // Performing a GET on the recovery URL (return from a join request) will return the original <participant URL>
    @GET
    @Path("{LRAId}/{RecCoordId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Lookup the participant URL", description = "Performing a GET on the recovery URL " +
        "(returned from a join request) will return the original participant URL(s)")
    @APIResponses({
        @APIResponse(responseCode = "404", description = "The coordinator has no knowledge of this participant"),
        @APIResponse(responseCode = "200", description = "The participant associated with this recovery id is returned",
            content = @Content(schema = @Schema(title = "The original participant URI")))
    })
    public String getCompensator(
            @Parameter(name = "LRAId", description = "Identifies the LRAId that the participant joined", required = true)
            @PathParam("LRAId") String lraId,
            @Parameter(name = "RecCoordId",
                description = "An identifier that was returned by the coordinator when a participant joined the LRA",
                required = true)
            @PathParam("RecCoordId") String rcvCoordId) throws NotFoundException {

        String compensatorUrl = lraService.getParticipant(rcvCoordId);

        if (compensatorUrl == null) {
            LRALogger.i18NLogger.error_cannotFoundCompensatorUrl(compensatorUrl, lraId);
            throw new NotFoundException(rcvCoordId);
        }

        return compensatorUrl;
    }

    // Performing a PUT on the recovery URL will overwrite the old <compensor URL> with the new one supplied
    // and return the old url
    @PUT
    @Path("{LRAId}/{RecCoordId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update the endpoint that a participant is prepared to accept requests on.",
        description = "Performing a PUT on the recovery URL will overwrite the old <compensor URL> with the new one supplied" +
            " and return the old url. The old value is returned." +
            "The full URL was returned when the participant first joined the LRA.")
    @APIResponses({
        @APIResponse(responseCode = "404", description = "The coordinator has no knowledge of this participant"),
        @APIResponse(responseCode = "200", description = "The coordinator has replaced the old participant with the new one")
    })
    public String replaceCompensator(
            @Parameter(name = "LRAId",
                description = "Identifies the LRAId that the participant joined",
                required = true)
            @PathParam("LRAId") String lraId,
            @Parameter(name = "RecCoordId",
                description = "An identifier that was returned by the coordinator when a participant joined the LRA",
                required = true)
            @PathParam("RecCoordId") String rcvCoordId,
            String newCompensatorUrl) throws NotFoundException {
        String compensatorUrl = lraService.getParticipant(rcvCoordId);

        if (compensatorUrl != null) {
            URI lra = null;

            try {
                lra = new URI(lraId);
            } catch (URISyntaxException e) {
                LRALogger.i18NLogger.error_invalidFormatOfLraIdReplacingCompensatorURI(lraId, compensatorUrl, e);

                throw new WebApplicationException(Response.status(INTERNAL_SERVER_ERROR.getStatusCode())
                            .entity(String.format("%s: %s", lraId, e.getMessage())).build());
            }

            lraService.updateRecoveryURI(lra, newCompensatorUrl, rcvCoordId, true);

            return context.getRequestUri().toASCIIString();
        }

        LRALogger.i18NLogger.error_cannotFoundCompensatorUrl(compensatorUrl, lraId);
        throw new NotFoundException(rcvCoordId);
    }

    @GET
    @Path("recovery")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List recovering Long Running Actions",
        description = "Returns LRAs that are recovering (ie some compensators still need to be ran)")
    @APIResponse(responseCode = "200",
        content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = LRAStatusHolder.class)))
    public List<LRAStatusHolder> getRecoveringLRAs() {
        return lraService.getAllRecovering(true);
    }
}
