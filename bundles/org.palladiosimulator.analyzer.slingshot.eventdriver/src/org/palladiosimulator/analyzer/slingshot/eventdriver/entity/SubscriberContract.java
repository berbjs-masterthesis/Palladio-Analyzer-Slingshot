package org.palladiosimulator.analyzer.slingshot.eventdriver.entity;

import java.util.Arrays;
import java.util.List;

import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.EventCardinality;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;

/**
 * Holds all the necessary contract for subscribers of a particular event of the
 * type returned by {@link #getWhen()}.
 * <p>
 * It tells that only the events listed by {@link #getThen()} are allowed to be returned
 * in the result set. Furthermore, the number of events returned is specified by 
 * {@link #getCardinality()}.
 * <p>
 * This contract only makes sense if the result of an event handler contains events that
 * should be published afterwards.
 * <p>
 * If an annotation {@link OnEvent} was used to specify the contract, then the information
 * can be transformed using {@link #fromAnnotation(OnEvent)}.
 * 
 * @author Julijan Katic
 */
public class SubscriberContract {

	private final Class<?> when;
	private final List<Class<?>> then;
	private final EventCardinality cardinality;
	
	public SubscriberContract(Class<?> when, List<Class<?>> then, EventCardinality cardinality) {
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
	
	/**
	 * Returns a new SubscriberContract from the {@link OnEvent} annotation.
	 * 
	 * @param onEvent The instance of the OnEvent annotation to a class.
	 * @return A new SubscriberContract containing the specified information from the annotation.
	 */
	public static SubscriberContract fromAnnotation(final OnEvent onEvent) {
		return new SubscriberContract(onEvent.when(), Arrays.asList(onEvent.then()), onEvent.cardinality());
	}
}
