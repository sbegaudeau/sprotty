/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * 
 * All rights reserved.
 *******************************************************************************/
package fr.obeo.sprotty.example.capella.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.obeo.sprotty.example.capella.web.diagram.Component;
import fr.obeo.sprotty.example.capella.web.diagram.Edge;
import fr.obeo.sprotty.example.capella.web.diagram.InputPort;
import fr.obeo.sprotty.example.capella.web.diagram.OutputPort;
import fr.obeo.sprotty.example.capella.web.diagram.Service;
import fr.obeo.sprotty.example.capella.web.diagram.System;
import io.typefox.sprotty.api.LayoutOptions;
import io.typefox.sprotty.api.SCompartment;
import io.typefox.sprotty.api.SLabel;

/**
 * This class is used to generate the graphical structure to display in Sprotty.
 * 
 * @author sbegaudeau
 */
public class CapellaDiagramGenerator {

	/**
	 * The main method used to retrieve the {@link System} to display. It will
	 * create the whole graphical structure to be manipulated by ELK and Sprotty.
	 * 
	 * @return The {@link System} to display in Sprotty
	 */
	public System generatorArchitectureDiagram() {
		return this.generateDefaultExample();
	}
	
	private System generateBenchmark() {
		System system = new System();
		system.setId("graph");
		system.setType("graph");
		system.setChildren(new ArrayList<>());
		
		List<Component> components = new ArrayList<>();
		
		for (int i = 0; i < 100;) {
			components.add(this.createComponent(system, "" + i, "" + i));
			i = i + 1;
			
			components.add(this.createComponent(system, "" + i, "" + i));
			i = i + 1;
		}
		
		for (int i = 0; i < 100; i++) {
			int firstNodeNumber = new Random().nextInt(components.size());
			int secondNodeNumber = new Random().nextInt(components.size());
			if (firstNodeNumber != secondNodeNumber) {
				Component source = components.get(firstNodeNumber);
				Component target = components.get(secondNodeNumber);
				
				Edge edge = new Edge();
				edge.setType("edge:straight");
				edge.setId(source.getId() + "->" + target.getId() + new Random().nextInt(50));
				edge.setSourceId(source.getId());
				edge.setTargetId(target.getId());

				system.getChildren().add(edge);
			}
		}
		
		return system;
	}
	
	private System generateDefaultExample() {
		System system = new System();
		system.setId("graph");
		system.setType("graph");
		system.setChildren(new ArrayList<>());
		
		Component passenger = this.createComponent(system, "passenger", "Passenger");
		Service listenToAudioAnnouncement = this.createService(passenger, "listen_to_audio_announcement",
				"Listen to Audio Announcement");
		InputPort listenToAudioAnnouncementInputPort1 = this.createInputPort(listenToAudioAnnouncement);
		InputPort listenToAudioAnnouncementInputPort2 = this.createInputPort(listenToAudioAnnouncement);
		
		this.createService(passenger, "watch_video_on_cabin_screen", "Watch Video on Cabin Screen");
		this.createService(passenger, "watch_video_on_private_screen", "Watch Video on Private Screen");
		this.createService(passenger, "command_vod_service", "Command VOD Service");
		this.createService(passenger, "select_passenger_service", "Select Passenger Service");
		
		Component aircraft = this.createComponent(system, "aircraft", "Aircraft");
		Service playSoundInCabin = this.createService(aircraft, "play_sound_in_cabin", "Play Sound in Cabin");
		InputPort playSoundInCabinInputPort1 = this.createInputPort(playSoundInCabin);
		OutputPort playSoundInCabinOutputPort1 = this.createOutputPort(playSoundInCabin);
		
		Component ifeSystem = this.createComponent(system, "ife_system", "IFE System");
		Service manageVideoAndAudioDiffusion = this.createService(ifeSystem, "manage_video_and_audio_diffusion",
				"Manager View and Audio Diffusion");
		OutputPort manageViewAndAudioDiffusionOutputPort1 = this.createOutputPort(manageVideoAndAudioDiffusion);
		OutputPort manageViewAndAudioDiffusionOutputPort2 = this.createOutputPort(manageVideoAndAudioDiffusion);
		
		this.createComponent(system, "cabin_crew", "Cabin Crew");
		this.createComponent(system, "ground_operator", "Ground Operator");
		
		this.createEdge(system, playSoundInCabinOutputPort1, listenToAudioAnnouncementInputPort1);
		this.createEdge(system, manageViewAndAudioDiffusionOutputPort1, listenToAudioAnnouncementInputPort2);
		this.createEdge(system, manageViewAndAudioDiffusionOutputPort2, playSoundInCabinInputPort1);
		
		return system;
		
	}

	/**
	 * Creates a {@link Component} containing a label and various services.
	 * 
	 * @param system
	 *            The {@link System} which will contain this {@link Component}
	 * @param id
	 *            The identifier of the {@link Component}
	 * @param label
	 *            The label of the {@link Component}
	 * @return The {@link Component} created
	 */
	private Component createComponent(System system, String id, String label) {
		Component component = new Component();
		component.setChildren(new ArrayList<>());
		component.setId(id);
		component.setType("node:capellacomponent");
		//component.setLayout("vbox");

		LayoutOptions componentLayoutOptions = new LayoutOptions();
		componentLayoutOptions.setResizeContainer(true);
		// component.setLayoutOptions(componentLayoutOptions);

		SLabel componentLabel = new SLabel();
		componentLabel.setId(id + "__label");
		componentLabel.setType("label:heading");
		componentLabel.setText(label);

		component.getChildren().add(componentLabel);

		SCompartment compartment = new SCompartment();
		compartment.setId(id + "__compartment");
		compartment.setType("comp:main");
		compartment.setLayout("vbox");
		compartment.setChildren(new ArrayList<>());
		LayoutOptions compartmentLayoutOptions = new LayoutOptions();
		// compartmentLayoutOptions.setResizeContainer(true);
		compartment.setLayoutOptions(compartmentLayoutOptions);

		//component.getChildren().add(compartment);
		system.getChildren().add(component);

		return component;
	}

	/**
	 * Creates a {@link Service} containing a label and various ports.
	 * 
	 * @param component
	 *            The parent {@link Component}
	 * @param id
	 *            The identifier of the {@link Service}
	 * @param label
	 *            The label of the {@link Service}
	 * @return The {@link Service} created
	 */
	private Service createService(Component component, String id, String label) {
		Service service = new Service();
		service.setChildren(new ArrayList<>());
		service.setId(id);
		service.setType("node:capellaservice");
		// service.setLayout("hbox");

		LayoutOptions layoutOptions = new LayoutOptions();
		layoutOptions.setResizeContainer(true);
		// service.setLayoutOptions(layoutOptions);

		SLabel serviceLabel = new SLabel();
		serviceLabel.setId(id + "__label");
		serviceLabel.setType("label:heading");
		serviceLabel.setText(label);

		service.getChildren().add(serviceLabel);
		component.getChildren().add(service);
		//((SCompartment) component.getChildren().get(1)).getChildren().add(service);

		return service;
	}

	/**
	 * Creates an {@link InputPort} in the given {@link Service}.
	 * 
	 * @param service
	 *            The {@link Service} in which the port will be contained
	 * @return The {@link InputPort} created
	 */
	private InputPort createInputPort(Service service) {
		InputPort port = new InputPort();
		port.setType("port:capellainputport");

		service.getChildren().add(port);

		port.setId(service.getId() + ".p" + service.getChildren().indexOf(port));

		return port;
	}

	/**
	 * Creates an {@link OutputPort} in the given {@link Service}.
	 * 
	 * @param service
	 *            The {@link Service} in which the port will be contained
	 * @return The {@link OutputPort} created
	 */
	private OutputPort createOutputPort(Service service) {
		OutputPort port = new OutputPort();
		port.setType("port:capellaoutputport");

		service.getChildren().add(port);

		port.setId(service.getId() + ".p" + service.getChildren().indexOf(port));
		return port;
	}

	/**
	 * Creates an edge from an {@link OutputPort} to an {@link InputPort}.
	 * 
	 * @param system
	 *            The system containing the edge
	 * @param outputPort
	 *            The {@link OutputPort}
	 * @param inputPort
	 *            The {@link InputPort}
	 * @return
	 */
	private Edge createEdge(System system, OutputPort outputPort, InputPort inputPort) {
		Edge edge = new Edge();
		edge.setType("edge:straight");
		edge.setId(outputPort.getId() + "-->" + inputPort.getId());
		edge.setSourceId(outputPort.getId());
		edge.setTargetId(inputPort.getId());

		system.getChildren().add(edge);

		return edge;
	}
}
