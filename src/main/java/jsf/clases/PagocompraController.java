package jsf.clases;

import entidades.Pagocompra;
import jsf.clases.util.JsfUtil;
import jsf.clases.util.PaginationHelper;
import beans.sessions.PagocompraFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("pagocompraController")
@SessionScoped
public class PagocompraController implements Serializable {

    private Pagocompra current;
    private DataModel items = null;
    @EJB
    private beans.sessions.PagocompraFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PagocompraController() {
    }

    public Pagocompra getSelected() {
        if (current == null) {
            current = new Pagocompra();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PagocompraFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Pagocompra) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Pagocompra();
        selectedItemIndex = -1;
        return "List";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("¡Pago de compra creado con exito!");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addSuccessMessage("¡Lo sentimos la operación no pudo completarse intente mas tarde!");
            return "List";
        }
    }

    public String prepareEdit() {
        current = (Pagocompra) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("¡Pago de compra editada con exito!");
            return "List";
        } catch (Exception e) {
            JsfUtil.addSuccessMessage("¡Lo sentimos la operación no pudo completarse intente mas tarde!");
            return "List";
        }
    }

    public String destroy() {
        current = (Pagocompra) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
             JsfUtil.addSuccessMessage("¡Pago de compra eliminado con exito!");
        } catch (Exception e) {
            JsfUtil.addSuccessMessage("¡Lo sentimos la operación no pudo completarse intente mas tarde!");
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Pagocompra getPagocompra(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Pagocompra.class)
    public static class PagocompraControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PagocompraController controller = (PagocompraController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "pagocompraController");
            return controller.getPagocompra(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Pagocompra) {
                Pagocompra o = (Pagocompra) object;
                return getStringKey(o.getPagocompraid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Pagocompra.class.getName());
            }
        }

    }

}
