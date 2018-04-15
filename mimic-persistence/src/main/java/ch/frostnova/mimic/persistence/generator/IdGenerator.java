package ch.frostnova.mimic.persistence.generator;

import ch.frostnova.keygen.KeyGenerator;
import ch.frostnova.keygen.model.KeyLengthUnit;
import ch.frostnova.keygen.model.KeySpec;
import ch.frostnova.keygen.model.KeyType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Identity generator (UUID strings)
 *
 * @author pwalser
 * @since 25.12.2017.
 */
public class IdGenerator implements IdentifierGenerator {

    private final static KeySpec KEY_SPEC = new KeySpec(KeyType.AlphaNumeric, 128, KeyLengthUnit.Bits);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return KeyGenerator.generate(KEY_SPEC);
    }
}