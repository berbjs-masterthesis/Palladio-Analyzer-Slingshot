package org.palladiosimulator.analyzer.slingshot.eventdriver.entity;

import java.util.Arrays;
import java.util.List;

import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.EventCardinality;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;

public class EventContract {

	private final Class<?> when;
	private final List<Class<?>> then;
	private final EventCardinality cardinality;
	
	public EventContract(Class<?> when, List<Class<?>> then, EventCardinality cardinality) {
		this.when = when;
		this.then = then;
		this.cardinality = cardinality;
	}

	public Class<?> getWhen() {
		return when;
	}

	public List<Class<?>> getThen() {
		return then;
	}

	public EventCardinality getCardinality() {
		return cardinality;
	}
	
	public static EventContract fromAnnotation(final OnEvent onEvent) {
		return new EventContract(onEvent.when(), Arrays.asList(onEvent.then()), onEvent.cardinality());
	}
}
