/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * 
 * All rights reserved.
 *******************************************************************************/
package fr.obeo.sprotty.example.capella.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.alg.layered.options.LayeringStrategy;
import org.eclipse.elk.core.math.KVector;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.HierarchyHandling;
import org.eclipse.elk.core.options.SizeConstraint;
import org.eclipse.elk.graph.ElkGraphElement;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import fr.obeo.sprotty.example.capella.web.diagram.Component;
import io.typefox.sprotty.api.SGraph;
import io.typefox.sprotty.api.SLabel;
import io.typefox.sprotty.api.SModelElement;
import io.typefox.sprotty.api.SModelRoot;
import io.typefox.sprotty.layout.ElkLayoutEngine;
import io.typefox.sprotty.layout.SprottyLayoutConfigurator;

/**
 * The CapellaLayoutEngine defines the configuration of the layout that ELK
 * should apply to a given Sprotty model. This configuration can be highly
 * complex and any kind of modification can have dramatic result.
 * 
 * Here be dragons!!!
 * 
 * @author sbegaudeau
 */
public class CapellaLayoutEngine extends ElkLayoutEngine {
	
	/**
	 * The logger.
	 */
	private static Logger LOG = Logger.getLogger(CapellaLayoutEngine.class);
	
	@Override
	public void layout(SModelRoot root) {
		if (root instanceof SGraph) {
			SprottyLayoutConfigurator configurator = new SprottyLayoutConfigurator();
			configurator.configureByType("graph")
				.setProperty(CoreOptions.ALGORITHM, LayeredOptions.ALGORITHM_ID)
				.setProperty(CoreOptions.HIERARCHY_HANDLING, HierarchyHandling.INCLUDE_CHILDREN)
				.setProperty(LayeredOptions.LAYERING_STRATEGY, LayeringStrategy.NETWORK_SIMPLEX)
				//.setProperty(LayeredOptions.SPACING_NODE_NODE, 50.0)
				//.setProperty(LayeredOptions.CROSSING_MINIMIZATION_GREEDY_SWITCH_TYPE, GreedySwitchType.OFF)
				//.setProperty(LayeredOptions.CROSSING_MINIMIZATION_STRATEGY, CrossingMinimizationStrategy.INTERACTIVE)
				//.setProperty(LayeredOptions.CYCLE_BREAKING_STRATEGY, CycleBreakingStrategy.GREEDY)
				//.setProperty(LayeredOptions.EDGE_ROUTING, EdgeRouting.ORTHOGONAL)
				//.setProperty(LayeredOptions.DIRECTION, Direction.RIGHT)
				;
			
			configurator.configureByType("node:capellacomponent")
				.setProperty(CoreOptions.NODE_SIZE_CONSTRAINTS, EnumSet.of(SizeConstraint.NODE_LABELS, SizeConstraint.MINIMUM_SIZE))
				.setProperty(CoreOptions.NODE_SIZE_MINIMUM, new KVector(250,  200))
				//.setProperty(CoreOptions.NODE_LABELS_PLACEMENT, NodeLabelPlacement.insideBottomRight())
				//.setProperty(LayeredOptions.CROSSING_MINIMIZATION_GREEDY_SWITCH_TYPE, GreedySwitchType.OFF)
				;
			
			configurator.configureByType("comp:main");
			
			configurator.configureByType("node:capellaservice")
				.setProperty(CoreOptions.NODE_SIZE_CONSTRAINTS, EnumSet.of(SizeConstraint.NODE_LABELS, SizeConstraint.MINIMUM_SIZE, SizeConstraint.PORTS))
				.setProperty(CoreOptions.NODE_SIZE_MINIMUM, new KVector(100, 60))
				//.setProperty(LayeredOptions.PORT_CONSTRAINTS, PortConstraints.FIXED_SIDE)
				;
			
			configurator.configureByType("port:capellainputport")
				//.setProperty(LayeredOptions.PORT_SIDE, PortSide.EAST)
				;
			
			configurator.configureByType("port:capellaoutputport")
				//.setProperty(LayeredOptions.PORT_SIDE, PortSide.WEST)
				;
			
			//configurator.configureByType("label:heading");
			
			//configurator.configureByType("edge:straight");
			
			this.layout((SGraph) root, configurator);
		}
		super.layout(root);
	}
	
	@Override
	protected boolean shouldInclude(SModelElement element, SModelElement sParent, ElkGraphElement elkParent, LayoutContext context) {
		if (sParent instanceof Component && element instanceof SLabel) {
			return true;
		}
		return super.shouldInclude(element, sParent, elkParent, context);
	}
	
	@Override
	protected void applyEngine(ElkNode elkGraph) {
		/*
		 * Transform the ELK model into XMI in order to log it. This code allow us to
		 * have some information in order to be able to debug the behavior of ELK. It
		 * does not give us a full understanding of the behavior of ELK but it give us
		 * the shape of the constraints that ELK has understood from our code above.
		 **/
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createFileURI("output.elkg"));
		resource.getContents().add(elkGraph);
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			resource.save(outputStream, new HashMap<>());
			LOG.info(outputStream.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		super.applyEngine(elkGraph);
	}
}
