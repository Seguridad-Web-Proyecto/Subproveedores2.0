/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validaciones.mercado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final String FECHA_PATTERN = "^([0-2][0-9]|3[0-1])(\\/|-)(0[1-9]|1[0-2])\\2(\\d{4})$";

    private final Pattern pattern;
    private Matcher matcher;

    public FechaValidator()
    {
        pattern = Pattern.compile(FECHA_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        String date = s.format(value);
        matcher = pattern.matcher(date);
        if (!matcher.matches())
        {
            FacesMessage msg = new FacesMessage("Error... Fecha no válida", "Invalid email format..");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        Date d1 = new Date();
        try
        {
            d1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException ex)
        {
            Logger.getLogger(FechaValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date d2 = new Date(System.currentTimeMillis());
        if (d1.compareTo(d2) < 0)
        {
            FacesMessage msg = new FacesMessage("Error... La fecha no debe ser menor a la actual", "Invalid date...");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
