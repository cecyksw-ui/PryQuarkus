package com.resolutions.adapters.out.jpa;

import com.resolutions.adapters.out.jpa.entidades.Gencatsenca;
import com.resolutions.adapters.out.jpa.mappers.GencatsencaMapper;
import com.resolutions.application.ports.out.DataPersist;
import com.resolutions.model.Catalogo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class GencatsencaRepositoryImp implements DataPersist<Catalogo> {
    @Inject
    GencatsencaRepository gencatsencaRepo;

    @Inject
    GencatsencaMapper gencatsencaMapper;

    @Override
    public Integer insert(Catalogo dto) {
        Gencatsenca gencatsenca = gencatsencaMapper.toResource(dto);
        gencatsencaRepo.persist(gencatsenca);
        return gencatsenca.getCodCat();
    }

    @Override
    public void update(Catalogo dto) {

    }

    @Override
    public void delete(Catalogo dto) {

    }

    @Override
    public Catalogo get(Integer cod) {
        return new Catalogo();
    }

}
