package ch.frostnova.mimic.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * JPA type converter for {@link LocalDate} (mapped as {@link Date})
 *
 * @author pwalser
 * @since 27.06.2017
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate dateTime) {
        return dateTime == null ? null : Date.valueOf(dateTime);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return date == null ? null : date.toLocalDate();
    }
}
