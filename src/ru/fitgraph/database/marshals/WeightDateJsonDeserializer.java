package ru.fitgraph.database.marshals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.JsonDeserializer;;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Deserializer class for weight point date field.
 */
public class WeightDateJsonDeserializer extends JsonDeserializer<Date> {
    private final static Logger logger = Logger.getLogger(WeightDateJsonDeserializer.class);

    private static final SimpleDateFormat weightDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        Date parsed = null;
        try {
            parsed = weightDateFormat.parse(jsonParser.getText());
        } catch (ParseException e) {
            logger.warn("Can't parse weight date.", e);
        }

        return parsed;
    }
}
