package org.vpac.ndg.cli.smadaptor;

import org.springframework.context.ApplicationContext;
import org.vpac.ndg.cli.smadaptor.local.LocalStorageManager;
import org.vpac.ndg.cli.smadaptor.remote.RemoteStorageManager;


public class Factory {
	public static StorageManager create(String uri, ApplicationContext appContext) {
		if (uri.equals("")) {
			return new LocalStorageManager(appContext);
		} else if(!uri.equals("")) {
			return new RemoteStorageManager(uri, appContext);
		} else {
			throw new UnsupportedOperationException("Non-local storage managers not supported yet.");
		}
	}
}
