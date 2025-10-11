package com.resolutions.adapters;

import com.resolutions.application.ports.out.LoggerLocal;
import com.resolutions.model.Catalogo;
import com.resolutions.application.ports.in.AddCatalogoUseCase;
import com.resolutions.application.ports.out.DataPersist;
import com.resolutions.application.useCases.AddCatalogo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;


public class QuarkusAppConfig {

    @Inject
    Instance< DataPersist<Catalogo> > catRepository;
    @Inject
    Instance< LoggerLocal > log;

    @Produces
    @ApplicationScoped
    AddCatalogoUseCase getAddCatalogo() {
        return new AddCatalogo(catRepository.get(),log.get());
    }

}
