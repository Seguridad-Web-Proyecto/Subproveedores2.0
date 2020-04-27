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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Dann
 */
@FacesConverter("dateConverter")
public class DateConverter implements Converter
{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string)
    {
        Date d = null;
    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
    try {
      d = s.parse(string);
    } catch (ParseException ex) {
      Logger.getLogger(DateConverter.class.getName()).log(Level.SEVERE, null, ex);
    }
    return d;
  }
    

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o)
    {
        return String.valueOf(o);
    }
    
}
