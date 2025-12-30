package com.quetoquenana.pedalpal.repository.impl;

import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.Bike;
import com.quetoquenana.pedalpal.model.BikeComponent;
import com.quetoquenana.pedalpal.repository.BikeRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class BikeRepositoryImpl implements BikeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public BikeComponent createComponent(UUID bikeId, BikeComponent component) {
        Bike bike = em.find(Bike.class, bikeId);
        if (bike == null) throw new RecordNotFoundException("bike.not.found", bikeId);
        component.setBike(bike);
        em.persist(component);
        return component;
    }

    @Override
    public BikeComponent updateComponent(BikeComponent component) {
        return em.merge(component);
    }

    @Override
    public void deleteComponent(UUID componentId) {
        BikeComponent c = em.find(BikeComponent.class, componentId);
        if (c != null) em.remove(c);
    }

    @Override
    public List<BikeComponent> findComponentsByBikeId(UUID bikeId) {
        return em.createQuery("select c from BikeComponent c where c.bike.id = :bikeId", BikeComponent.class)
                .setParameter("bikeId", bikeId).getResultList();
    }
}
