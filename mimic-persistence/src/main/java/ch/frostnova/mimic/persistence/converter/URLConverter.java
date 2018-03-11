package ch.frostnova.mimic.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * JPA type converter for {@link URL} (mapped as {@link String})
 *
 * @author pwalser
 * @since 27.06.2017
 */
@Converter(autoApply = true)
public class URLConverter implements AttributeConverter<URL, String> {

    @Override
    public String convertToDatabaseColumn(URL url) {
        return url == null ? null : url.toExternalForm();
    }

    @Override
    public URL convertToEntityAttribute(String url) {
        if (url == null) {
            return null;
        }
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
