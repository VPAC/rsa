package org.vpac.web.util;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.Default;
import org.vpac.ndg.common.datamodel.CellSize;

public class ControllerHelper {
	public final static String RESPONSE_ROOT = "Response";
	
	public ControllerHelper() {
	}

	public void BindDateTimeFormatter(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
		    public void setAsText(String value) {
	            setValue(Utils.parseDate(value));
		    }

		    public String getAsText() {
		        return new SimpleDateFormat(Default.MILLISECOND_PATTERN).format((Date) getValue());
		    }        

		});
	}

	public void BindCellSizeFormatter(WebDataBinder binder) {
		binder.registerCustomEditor(CellSize.class, new PropertyEditorSupport() {
					public void setAsText(String value) {
						try {
							setValue(CellSize.fromHumanString(value));
						} catch (IllegalArgumentException e) {
							setValue(CellSize.valueOf(value));
						}
					}

					public String getAsText() {
						return ((CellSize) getValue()).toHumanString();
					}

		});
	}
}
