package com.resolutions.adapters.in.rest;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import com.resolutions.application.ports.in.AddCatalogoUseCase;
import com.resolutions.model.Catalogo;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/catalogos")
public class CatalogoResources{
    private final AddCatalogoUseCase addCatalogo;
    private final Logger logger;

    public CatalogoResources(AddCatalogoUseCase addCatalogo, Logger logger) {
        this.addCatalogo = addCatalogo;
        this.logger = logger;
    }

    @POST
    public RestResponse<Void> crearCatalogo(Catalogo catalogo, @Context UriInfo uriInfo) throws Exception {

            Integer id = addCatalogo.run(catalogo);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(id.toString());
            logger.debug("Se creó catálogo con URI" + builder.build().toString());
            return RestResponse.created(builder.build());
    }
}
