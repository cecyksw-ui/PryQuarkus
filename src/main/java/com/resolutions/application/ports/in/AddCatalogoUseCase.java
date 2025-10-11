package com.resolutions.application.ports.in;
import com.resolutions.model.Catalogo;

public interface AddCatalogoUseCase {
    Integer run(Catalogo catalogo) throws Exception ;
}
