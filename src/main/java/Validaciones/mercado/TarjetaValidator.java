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
public class TarjetaValidator implements Validator
{

    private static final String VISA_PATTERN = "^4[0-9]{3}-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}$";
    private static final String MASTERCARD_PATTERN = "^5[1-5][0-9]{2}-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}$";

    private final Pattern pattern;
    private final Pattern pattern1;
    private Matcher matcher;

    public TarjetaValidator()
    {
        pattern = Pattern.compile(VISA_PATTERN);
        pattern1 = Pattern.compile(MASTERCARD_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches())
        {

            FacesMessage msg = new FacesMessage("Error...TARJETA no válida", "Invalid format..");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
        matcher = pattern1.matcher(value.toString());
        if (!matcher.matches())
        {

            FacesMessage msg = new FacesMessage("Error... TARJETA no válida", "Invalid email format..");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
    }

}
