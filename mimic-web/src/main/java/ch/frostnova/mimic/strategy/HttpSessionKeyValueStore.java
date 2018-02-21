package ch.frostnova.mimic.strategy;

import ch.frostnova.mimic.api.KeyValueStore;
import ch.frostnova.util.check.Check;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Key/value store adapter for the HTTPSession, stores value in session scope.
 *
 * @author pwalser
 * @since 28.01.2018.
 */
public class HttpSessionKeyValueStore implements KeyValueStore {

    private final HttpSession session;

    public HttpSessionKeyValueStore(HttpSession session) {
        this.session = Check.required(session, "session");
    }


    @Override
    public void put(String key, String value) {
        Check.required("key", key);
        if (value == null) {
            remove(key);
        } else {
            session.setAttribute(key, value);
        }
    }

    @Override
    public String get(String key) {
        Check.required("key", key);
        Object attribute = session.getAttribute(key);
        return attribute != null ? attribute.toString() : null;
    }

    @Override
    public void remove(String key) {
        Check.required("key", key);
        session.removeAttribute(key);
    }

    @Override
    public Set<String> getKeys() {
        return new HashSet<>(Collections.list(session.getAttributeNames()));
    }

    @Override
    public void clear() {
        for (String key : getKeys()) {
            remove(key);
        }
    }
}
