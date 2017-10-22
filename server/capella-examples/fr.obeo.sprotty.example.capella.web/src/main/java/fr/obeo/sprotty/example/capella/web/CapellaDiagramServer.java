/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * 
 * All rights reserved.
 *******************************************************************************/
package fr.obeo.sprotty.example.capella.web;

import fr.obeo.sprotty.example.capella.web.diagram.System;
import io.typefox.sprotty.api.ActionMessage;
import io.typefox.sprotty.api.DefaultDiagramServer;
import io.typefox.sprotty.api.ILayoutEngine;
import io.typefox.sprotty.api.RequestModelAction;
import io.typefox.sprotty.api.SModelRoot;

/**
 * The {@link CapellaDiagramServer} is used to communicate with the clients.
 * 
 * @author sbegaudeau
 */
public class CapellaDiagramServer extends DefaultDiagramServer {
	/**
	 * The diagram generator.
	 */
	private CapellaDiagramGenerator diagramGenerator = new CapellaDiagramGenerator();
	
	@Override
	public void accept(ActionMessage message) {
		/*
		 * When we will receive an action from the client requesting the model, we will also
		 * return an action indicating that the client should update its model with the one
		 * that has been computed by the CapellaDiagramGenerator.
		 **/
		if (message.getAction() instanceof RequestModelAction) {
			System architectureDiagram = this.diagramGenerator.generatorArchitectureDiagram();
			this.updateModel(architectureDiagram);
		} else {
			super.accept(message);
		}
	}
	
	@Override
	protected boolean needsClientLayout(SModelRoot root) {
		/*
		 * The client layout is necessary in order to have Sprotty compute some information which
		 * are necessary for ELK to to its job. As an example, the internal layout of a node needs
		 * to be computed by Sprotty in order to know the real size of the various elements after
		 * the use of CSS. Sprotty can then communicate this information to ELK which can thus start
		 * to compute a meaningful layout which will be then returned to Sprotty.
		 **/
		return true;
	}
	
	@Override
	protected boolean needsServerLayout(SModelRoot root) {
		switch (root.getType()) {
			case "graph":
				return true;
			default:
				return false;
		}
	}
	
	@Override
	protected ILayoutEngine getLayoutEngine() {
		return new CapellaLayoutEngine();
	}
}
