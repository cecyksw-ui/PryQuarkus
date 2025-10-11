package com.resolutions.adapters.out.jpa;

import com.resolutions.adapters.out.jpa.entidades.Gencatsdeta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GecatsdetaRepository implements PanacheRepository<Gencatsdeta> {
}
