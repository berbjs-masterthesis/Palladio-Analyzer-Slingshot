package org.palladiosimulator.analyzer.slingshot.common.events;

import com.google.common.reflect.TypeToken;

/**
 * This abstract event is used for the special case that an event uses generics
 * which need to be distinguished by it. For example, if
 * {@code SampleEvent<TypeA>} and {@code SampleEvent<TypeB>} are published, then
 * the event dispatcher must be able to distinguish them, since otherwise the
 * generic types are erased at runtime and the dispatcher would call all event
 * handlers listening to {@code SampleEvent} regardless of their specific type.
 * <p>
 * To distinguish the events, a {@link TypeToken} is used which can be accessed
 * by the event dispatcher.
 * <p>
 * An example event could be:
 *
 * <pre>
 * {@code
 * public final class SomeGenericEvent<T> extends AbstractGenericEvent<T, EntityType>
 * }
 * </pre>
 *
 * @author Julijan Katic
 *
 * @param <G> The generic type of the event onto which to distinguish
 * @param <T> The entity type for {@link AbstractEntityChangedEvent}.
 */
public abstract class AbstractGenericEvent<G, T> extends AbstractEntityChangedEvent<T> implements ReifiedEvent<G> {

	private final TypeToken<G> genericTypeToken;

	/**
	 * Constructs a AbstractGenericEvent without a concrete Class instance. Here, a
	 * {@link TypeToken} is generated.
	 * <p>
	 * Note that if the type is not known at compile-time, the TypeToken will
	 * contain the type of the known super-class. For example, if a class {@code A}
	 * has three sub-classes {@code B, C, D}, but the concrete class is not known
	 * and this constructor is used (i.e. {@code new GenericEvent<A, A>(b, 0);}),
	 * then {@link #getTypeToken()} will return {@code A}, even though {@code b} is
	 * of instance {@code B}. In this case, use
	 * {@link #AbstractGenericEvent(Class, T, double)} instead.
	 *
	 * @param entity The entity for {@link AbstractEntityChangedEvent}.
	 * @param delay  The delay of the event.
	 */
	protected AbstractGenericEvent(final T entity, final double delay) {
		super(entity, delay);
		this.genericTypeToken = new TypeToken<G>(this.getClass()) {};
	}

	/**
	 * Constructs an AbstractGenericEvent with a concrete Class information. The
	 * {@link TypeToken} is generated by using {@code concreteClass}, hence
	 * {@link #getTypeToken()} will return the type from {@code concreteClass}.
	 * <p>
	 * This constructor is useful if the concrete class information is not known at
	 * compile time. Otherwise, if it is possible to instantiate as follows:
	 * {@code new GenericEvent<A, Entity>(entity, 0);}, then use
	 * {@link #AbstractGenericEvent(T, double)} instead.
	 *
	 * @param concreteClass The concrete class of the type {@code G}.
	 * @param entity        The entity for {@link AbstractEntityChangedEvent}.
	 * @param delay         the delay of the event.
	 */
	protected AbstractGenericEvent(final Class<G> concreteClass,
			final T entity, final double delay) {
		super(entity, delay);
		this.genericTypeToken = TypeToken.of(concreteClass);
	}

	@Override
	public TypeToken<G> getTypeToken() {
		return this.genericTypeToken;
	}

	public Class<?> getGenericType() {
		return this.genericTypeToken.getRawType();
	}


}
