package jsf.clases;

import entidades.Tarjetacreditoventa;
import jsf.clases.util.JsfUtil;
import jsf.clases.util.PaginationHelper;
import beans.sessions.TarjetacreditoventaFacade;

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

@Named("tarjetacreditoventaController")
@SessionScoped
public class TarjetacreditoventaController implements Serializable {

    private Tarjetacreditoventa current;
    private DataModel items = null;
    @EJB
    private beans.sessions.TarjetacreditoventaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public TarjetacreditoventaController() {
    }

    public Tarjetacreditoventa getSelected() {
        if (current == null) {
            current = new Tarjetacreditoventa();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TarjetacreditoventaFacade getFacade() {
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
        current = (Tarjetacreditoventa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Tarjetacreditoventa();
        selectedItemIndex = -1;
        return "List";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("¡Tarjeta de credito venta creada con exito!");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addSuccessMessage("¡Lo sentimos la operación no pudo completarse intente mas tarde!");
            return "List";
        }
    }

    public String prepareEdit() {
        current = (Tarjetacreditoventa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("¡Tarjeta de credito venta editada con exito!");
            return "List";
        } catch (Exception e) {
            JsfUtil.addSuccessMessage("¡Lo sentimos la operación no pudo completarse intente mas tarde!");
            return "List";
        }
    }

    public String destroy() {
        current = (Tarjetacreditoventa) getItems().getRowData();
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
            JsfUtil.addSuccessMessage("¡Tarjeta de credito venta eliminada con exito!");
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

    public Tarjetacreditoventa getTarjetacreditoventa(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Tarjetacreditoventa.class)
    public static class TarjetacreditoventaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TarjetacreditoventaController controller = (TarjetacreditoventaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tarjetacreditoventaController");
            return controller.getTarjetacreditoventa(getKey(value));
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
            if (object instanceof Tarjetacreditoventa) {
                Tarjetacreditoventa o = (Tarjetacreditoventa) object;
                return getStringKey(o.getTarjetacreditoventaid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tarjetacreditoventa.class.getName());
            }
        }

    }

}
