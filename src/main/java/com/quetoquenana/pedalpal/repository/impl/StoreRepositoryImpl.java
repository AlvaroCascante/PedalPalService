package com.quetoquenana.pedalpal.repository.impl;

import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.Store;
import com.quetoquenana.pedalpal.model.StoreLocation;
import com.quetoquenana.pedalpal.repository.StoreRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public StoreLocation createLocation(UUID storeId, StoreLocation location) {
        Store store = em.find(Store.class, storeId);
        if (store == null) throw new RecordNotFoundException("store.not.found", storeId);
        location.setStore(store);
        em.persist(location);
        return location;
    }

    @Override
    public StoreLocation updateLocation(StoreLocation location) {
        return em.merge(location);
    }

    @Override
    public void deleteLocation(UUID locationId) {
        StoreLocation l = em.find(StoreLocation.class, locationId);
        if (l != null) em.remove(l);
    }

    @Override
    public List<StoreLocation> findLocationsByStoreId(UUID storeId) {
        return em.createQuery("select s from StoreLocation s where s.store.id = :storeId", StoreLocation.class)
                .setParameter("storeId", storeId).getResultList();
    }
}
