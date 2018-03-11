package ch.frostnova.mimic.persistence.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * Identity generator (UUID strings)
 *
 * @author pwalser
 * @since 25.12.2017.
 */
public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        String generatedId = UUID.randomUUID().toString();
        System.out.println("ID: " + generatedId);
        return generatedId;
    }
}