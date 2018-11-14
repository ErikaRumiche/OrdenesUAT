package pe.com.nextel.form;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

import pe.com.nextel.util.GenericObject;


public abstract class GenericForm extends GenericObject implements Serializable {
    private static final long serialVersionUID = 1L;
	public GenericForm populateForm(HttpServletRequest request) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        try {
            HashMap hshMapForm = new HashMap();
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
				if (fields[i].getType().isArray()) {
                    hshMapForm.put(fields[i].getName(), request.getParameterValues(fields[i].getName()));
                } else {
                    hshMapForm.put(fields[i].getName(), request.getParameter(fields[i].getName()));
                }
            }

            BeanUtils.populate(this, hshMapForm);
        } catch (IllegalAccessException iae) {
            logger.error(formatException(iae));
        } catch (InvocationTargetException ite) {
            logger.error(formatException(ite));
        }
        if (logger.isDebugEnabled())
            logger.debug(this);
        return this;
    }

}