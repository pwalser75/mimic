package ch.frostnova.mimic.impl;

import ch.frostnova.mimic.api.KeyValueStore;
import ch.frostnova.util.check.Check;

import javax.servlet.http.HttpSession;

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
            clear(key);
        } else {
            session.setAttribute(key, value);
        }
    }

    @Override
    public String get(String key) {
        Check.required("key", key);
        return String.valueOf(session.getAttribute(key));
    }

    @Override
    public void clear(String key) {
        Check.required("key", key);
        session.removeAttribute(key);
    }
}
