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
public class NumerosValidator implements Validator
{

    private static final String NUMEROS_PATTERN = "([0-9]+.[0-9]+)|([0-9]+)$";

    private final Pattern pattern;
    private Matcher matcher;

    public NumerosValidator()
    {
        pattern = Pattern.compile(NUMEROS_PATTERN);
    }
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
         matcher= pattern.matcher(value.toString());
	if(!matcher.matches()) {

        FacesMessage msg=new FacesMessage("Solo acepta números", "Invalid  format..");
	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	throw new ValidatorException(msg);
        }
    }

}
