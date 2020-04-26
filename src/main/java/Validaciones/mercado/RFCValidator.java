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
public class RFCValidator implements Validator       
{
private static final String RFC_PATTERN = "^(([ÑA-Z|ña-z|&]{3}|[A-Z|a-z]{4})\\d{2}((0[1-9]|1[012])(0[1-9]|1\\d|2[0-8])|(0[13456789]|1[012])(29|30)|(0[13578]|1[02])31)(\\w{2})([A|a|0-9]{1}))$|^(([ÑA-Z|ña-z|&]{3}|[A-Z|a-z]{4})([02468][048]|[13579][26])0229)(\\w{2})([A|a|0-9]{1})$\n" +
"";

    private final Pattern pattern;
    private Matcher matcher;

    public RFCValidator()
    {
        pattern = Pattern.compile(RFC_PATTERN);
    }
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException
    {
        matcher= pattern.matcher(value.toString());
	if(!matcher.matches()) {

        FacesMessage msg=new FacesMessage("Error... RFC inválido...", "Invalid  format..");
	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	throw new ValidatorException(msg);
    }
    
}
}
