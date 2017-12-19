package com.odysseusinc.arachne.jcr.config;

import com.odysseusinc.arachne.jcr.service.ContentIntegrityService;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.DeleteEventListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateListenerConfigurer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ContentIntegrityService contentIntegrityService;

    @PostConstruct
    public void registerListeners() {

        EventListenerRegistry registry = getEventRegistry();

        DeleteEventListener deleteEntityListener = contentIntegrityService.getHibernateDeleteEventListener();
        registry.getEventListenerGroup(EventType.DELETE).appendListener(deleteEntityListener);
    }

    private EventListenerRegistry getEventRegistry() {

        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) this.entityManagerFactory;
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
        return sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
    }
}
