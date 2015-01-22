package ru.fitgraph.database.marshals;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for serialize weights date from database to json in specified format.
 */
public class WeightDateJsonSerializer extends JsonSerializer<Date> {
    private static final SimpleDateFormat weightDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(weightDateFormat.format(date));
    }
}
