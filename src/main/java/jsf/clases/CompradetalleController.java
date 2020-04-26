package jsf.clases;

import entidades.Compradetalle;
import jsf.clases.util.JsfUtil;
import jsf.clases.util.PaginationHelper;
import beans.sessions.CompradetalleFacade;

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

@Named("compradetalleController")
@SessionScoped
public class CompradetalleController implements Serializable {

    private Compradetalle current;
    private DataModel items = null;
    @EJB
    private beans.sessions.CompradetalleFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public CompradetalleController() {
    }

    public Compradetalle getSelected() {
        if (current == null) {
            current = new Compradetalle();
            current.setCompradetallePK(new entidades.CompradetallePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private CompradetalleFacade getFacade() {
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
        current = (Compradetalle) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Compradetalle();
        current.setCompradetallePK(new entidades.CompradetallePK());
        selectedItemIndex = -1;
        return "List";
    }

    public String create() {
        try {
            current.getCompradetallePK().setCompraid(current.getOrdencompra().getOrdencompraid());
            current.getCompradetallePK().setProductoid(current.getProducto().getProductoid());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("¡Detalle de compra creado con exito!");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addSuccessMessage("¡Lo sentimos la operación no pudo completarse intente mas tarde!");
            return "List";
        }
    }

    public String prepareEdit() {
        current = (Compradetalle) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getCompradetallePK().setCompraid(current.getOrdencompra().getOrdencompraid());
            current.getCompradetallePK().setProductoid(current.getProducto().getProductoid());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("¡Detalle de compra editado con exito!");
            return "List";
        } catch (Exception e) {
            JsfUtil.addSuccessMessage("¡Lo sentimos la operación no pudo completarse intente mas tarde!");
            return "List";
        }
    }

    public String destroy() {
        current = (Compradetalle) getItems().getRowData();
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
            JsfUtil.addSuccessMessage("¡Detalle de compra eliminado con exito!");
           
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

    public Compradetalle getCompradetalle(entidades.CompradetallePK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Compradetalle.class)
    public static class CompradetalleControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CompradetalleController controller = (CompradetalleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "compradetalleController");
            return controller.getCompradetalle(getKey(value));
        }

        entidades.CompradetallePK getKey(String value) {
            entidades.CompradetallePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entidades.CompradetallePK();
            key.setCompraid(Long.parseLong(values[0]));
            key.setProductoid(Long.parseLong(values[1]));
            return key;
        }

        String getStringKey(entidades.CompradetallePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCompraid());
            sb.append(SEPARATOR);
            sb.append(value.getProductoid());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Compradetalle) {
                Compradetalle o = (Compradetalle) object;
                return getStringKey(o.getCompradetallePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Compradetalle.class.getName());
            }
        }

    }

}
