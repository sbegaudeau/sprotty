/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * 
 * All rights reserved.
 *******************************************************************************/
package fr.obeo.sprotty.example.capella.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import io.typefox.sprotty.api.IDiagramServer;

/**
 * The CapellaDiagramService is used to create the {@link CapellaDiagramServer}.
 * 
 * @author sbegaudeau
 */
public class CapellaDiagramService implements HttpSessionListener, IDiagramServer.Provider {

	/**
	 * The logger.
	 */
	private static Logger LOG = Logger.getLogger(CapellaDiagramService.class);
	
	/**
	 * The map of all the {@link CapellaDiagramServer} and their identifier.
	 */
	private Map<String, CapellaDiagramServer> diagramServers = new HashMap<>();

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		LOG.info("Session created");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		LOG.info("Session destroyed");
	}
	
	@Override
	public IDiagramServer getDiagramServer(String clientId) {
		synchronized (this.diagramServers) {
			CapellaDiagramServer result = this.diagramServers.get(clientId);
			if (result == null) {
				result = new CapellaDiagramServer();
				result.setClientId(clientId);
				this.diagramServers.put(clientId, result);
			}
			return result;
		}
	}

}
