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
public class URLValidator implements Validator
{

    private static final String URL_PATTERN = "^(http://|https://)(([a-z0-9]([-a-z0-9]*[a-z0-9]+)?){1,63}\\.)+[a-z]{2,6}";

    private final Pattern pattern;
    private Matcher matcher;

    public URLValidator()
    {
        pattern = Pattern.compile(URL_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches())
        {

            FacesMessage msg = new FacesMessage("Error... URL no válido", "Invalid format..");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
