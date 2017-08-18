/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * 
 * All rights reserved.
 *******************************************************************************/
package fr.obeo.sprotty.example.capella.web;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.apache.log4j.Logger;

/**
 * The {@link TestEndpointConfigurator} is used to connect the
 * {@link TestServerEndpoint} to the diagram service.
 * 
 * @author sbegaudeau
 */
public class TestEndpointConfigurator extends Configurator {

	/**
	 * The logger.
	 */
	private static Logger LOG = Logger.getLogger(TestEndpointConfigurator.class);

	/**
	 * The diagram service.
	 */
	private CapellaDiagramService diagramService;

	/**
	 * The constructor.
	 * 
	 * @param diagramService
	 *            The diagram service
	 */
	public TestEndpointConfigurator(CapellaDiagramService diagramService) {
		this.diagramService = diagramService;
	}

	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
		T instance = super.getEndpointInstance(endpointClass);
		TestServerEndpoint testServerEndpoint = (TestServerEndpoint) instance;
		testServerEndpoint.setDiagramServerProvider(this.diagramService);
		testServerEndpoint.setExceptionHandler((exception) -> LOG.warn(exception));
		return instance;
	}
}
