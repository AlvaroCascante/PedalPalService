package com.quetoquenana.pedalpal.repository.impl;

import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.Appointment;
import com.quetoquenana.pedalpal.model.AppointmentTask;
import com.quetoquenana.pedalpal.repository.AppointmentRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class AppointmentRepositoryImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public AppointmentTask createTask(UUID appointmentId, AppointmentTask task) {
        Appointment app = em.find(Appointment.class, appointmentId);
        if (app == null) throw new RecordNotFoundException("appointment.not.found", appointmentId);
        task.setAppointment(app);
        em.persist(task);
        return task;
    }

    @Override
    public AppointmentTask updateTask(AppointmentTask task) {
        return em.merge(task);
    }

    @Override
    public void deleteTask(UUID taskId) {
        AppointmentTask t = em.find(AppointmentTask.class, taskId);
        if (t != null) em.remove(t);
    }

    @Override
    public List<AppointmentTask> findTasksByAppointmentId(UUID appointmentId) {
        return em.createQuery("select t from AppointmentTask t where t.appointment.id = :appointmentId", AppointmentTask.class)
                .setParameter("appointmentId", appointmentId).getResultList();
    }
}
