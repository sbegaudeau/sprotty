/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * 
 * All rights reserved.
 *******************************************************************************/
package fr.obeo.sprotty.example.capella.web;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import io.typefox.sprotty.api.ActionMessage;
import io.typefox.sprotty.server.websocket.DiagramServerEndpoint;

/**
 * The {@link TestServerEndpoint} is used to log the various interactions
 * between the client and the server.
 * 
 * @author sbegaudeau
 */
public class TestServerEndpoint extends DiagramServerEndpoint {
	/**
	 * The logger.
	 */
	private static Logger LOG = Logger.getLogger(TestServerEndpoint.class);

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		LOG.info("Opened connection " + session.getId());
		super.onOpen(session, config);
	}

	@Override
	public void onClose(Session session, CloseReason closeReason) {
		LOG.info("Closed connection " + session.getId());
		super.onClose(session, closeReason);
	}

	@Override
	public void accept(ActionMessage message) {
		LOG.info("SERVER " + message);
		super.accept(message);
	}

	@Override
	protected void fireMessageReceived(ActionMessage message) {
		LOG.info("CLIENT " + message);
		super.fireMessageReceived(message);
	}
}
