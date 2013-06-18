package org.vpac.ndg.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.exceptions.NdgConfigException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class NdgConfigManager {

	final private Logger log = LoggerFactory.getLogger(NdgConfigManager.class);

	private NdgConfig config;

	public NdgConfig getConfig() {
		return config;
	}

	public void setConfig(NdgConfig config) {
		this.config = config;
	}

	public NdgConfigManager() {
	}

	/**
	 * Reads the NDG configuration from the xml file.
	 * @param xmlConfigFile The specified xml file.
	 * @return Returns the NDG configuration.
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unused")
	private void read(String xmlConfigFile) throws NdgConfigException {
		try {
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("cellSize", CellSize.class);
			xstream.processAnnotations(NdgConfig.class);
			
			FileInputStream fis = new FileInputStream(xmlConfigFile);
			config = (NdgConfig) xstream.fromXML(fis);			
		} catch(FileNotFoundException ex) {
			throw new NdgConfigException(ex.getMessage());
		} catch(Exception ex) {
			throw new NdgConfigException(ex.getMessage());			
		}
	}
	
	private void read(InputStream is) throws NdgConfigException {
		try {
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("cellSize", CellSize.class);
			xstream.processAnnotations(NdgConfig.class);
			
			config = (NdgConfig) xstream.fromXML(is);	
		} catch(Exception ex) {
			throw new NdgConfigException(ex.getMessage());			
		}
	}	
	
	public void write(String xmlConfigFile, NdgConfig config) throws FileNotFoundException {
		XStream xstream = new XStream();
		xstream.alias("cellSize", CellSize.class);
		xstream.processAnnotations(NdgConfig.class);
		
		String xml = xstream.toXML(config);
		log.debug("{}", xml);
	}

	public void configure() throws NdgConfigException, IOException {
		if(config == null) {
			String file = "/rsa.xml";
			InputStream inputStream = NdgConfig.class.getResourceAsStream(file);
			read(inputStream);
		}
	}
}
