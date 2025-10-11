package com.resolutions.adapters.out.jpa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.resolutions.adapters.out.jpa.entidades.Gencatsenca;
import com.resolutions.model.Catalogo;

@Mapper (componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface GencatsencaMapper {
    @Mapping(target = "fechCrea", source = "fecha")
    @Mapping(target = "fechMod", source = "fecha")
    Gencatsenca toResource(Catalogo catalogo);

    // De entidad JPA a modelo de dominio
    @Mapping(target = "fecha", source = "fechMod") // o fechCrea, según tu lógica
    Catalogo toDomain(Gencatsenca entity);
}
