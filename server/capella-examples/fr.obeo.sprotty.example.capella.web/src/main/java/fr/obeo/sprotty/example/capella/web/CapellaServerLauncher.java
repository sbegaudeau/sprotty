/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * 
 * All rights reserved.
 *******************************************************************************/
package fr.obeo.sprotty.example.capella.web;

import java.net.InetSocketAddress;

import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Builder;

import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.core.util.persistence.ElkGraphResourceFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import io.typefox.sprotty.layout.ElkLayoutEngine;

/**
 * The {@link CapellaServerLauncher} is used to start the whole Capella server.
 * 
 * @author sbegaudeau
 */
public class CapellaServerLauncher {

	/**
	 * Entry point of the Capella server.
	 */
	public static void main(String[] args) {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("elkg", new ElkGraphResourceFactory());
		ElkLayoutEngine.initialize(new LayeredOptions());
		CapellaDiagramService capellaDiagramService = new CapellaDiagramService();
		
		Server server = new Server(new InetSocketAddress(8080));
		
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setResourceBase("src/main/webapp");
		webAppContext.setWelcomeFiles(new String[] {"index.html"});
		webAppContext.setContextPath("/");
		webAppContext.setConfigurations(new Configuration[] {
				new AnnotationConfiguration(),
				new WebXmlConfiguration(),
				new WebInfConfiguration(),
				new MetaInfConfiguration()
		});
		webAppContext.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, ".*/fr\\\\.obeo\\\\.sprotty\\\\.example\\\\.capella\\\\.web/.*,.*\\\\.jar");
		webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
		webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
		webAppContext.addEventListener(capellaDiagramService);
		
		HandlerList handlerList = new HandlerList();
		ResourceHandler handler = new ResourceHandler();
		handler.setResourceBase("../../../client");
		handler.setWelcomeFiles(new String[] {"examples/index.html"});
		handler.setDirAllowed(false);
		
		handlerList.addHandler(handler);
		handlerList.addHandler(webAppContext);
		server.setHandler(handlerList);
		
		Slf4jLog log = new Slf4jLog(CapellaServerLauncher.class.getName());

		try {
			TestEndpointConfigurator testEndpointConfigurator = new TestEndpointConfigurator(capellaDiagramService);
			
			ServerContainer container = WebSocketServerContainerInitializer.configureContext(webAppContext);
			Builder endpointConfigBuilder = ServerEndpointConfig.Builder.create(TestServerEndpoint.class, "/diagram");
			endpointConfigBuilder.configurator(testEndpointConfigurator);
			container.addEndpoint(endpointConfigBuilder.build());
			
			server.start();
			log.info("Server started " + server.getURI());
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					log.info("Press enter to stop the server...");
				}
			};
			thread.start();
			server.join();
		} catch (Exception exception) {
			log.warn(exception.getMessage());
			System.exit(1);
		}
	}
}
