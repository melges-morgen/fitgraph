package ru.fitgraph.rest.elements;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by melges on 19.01.15.
 */
public class DateParameter {
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private Date date;

    public DateParameter() {
        this.date = new Date();
    }

    public DateParameter(String date) {
        try {
            this.date = dateTimeFormat.parse(date);
        } catch (ParseException e) {
            throw new WebApplicationException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("InvalidParameterException", e.getLocalizedMessage())).build());
        }
    }

    public Date getDate() {
        return date;
    }
}
