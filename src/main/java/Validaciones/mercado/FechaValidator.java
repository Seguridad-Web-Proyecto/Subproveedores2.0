/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validaciones.mercado;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Dann
 */
public class FechaValidator implements Validator
{

    private static final String FECHA_PATTERN = "^([0][1-9]|[12][0-9]|3[01])(\\/|-)([0][1-9]|[1][0-2])\\2(\\d{4})(\\s)([0-1][1-9]|[2][0-3])(:)([0-5][0-9])$";

    private final Pattern pattern;
    private Matcher matcher;

    public FechaValidator()
    {
        pattern = Pattern.compile(FECHA_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches())
        {

            FacesMessage msg = new FacesMessage("Error... Fecha no válida", "Invalid email format..");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
