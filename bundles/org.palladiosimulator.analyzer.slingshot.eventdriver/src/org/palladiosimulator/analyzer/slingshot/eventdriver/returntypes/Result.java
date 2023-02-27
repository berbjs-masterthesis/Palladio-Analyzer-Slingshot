package org.palladiosimulator.analyzer.slingshot.eventdriver.returntypes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A result is a container that holds any type of information of an event handler's result.
 * This could be, for example, new events that should be scheduled afterwards, especially
 * within the simulation phase.
 *
 * The container itself is a set, that means there is no guarantee of order.
 *
 * @author Julijan Katic, Sarah Stieß
 *
 * @param <T> The containers type
 */
public final class Result<T> {

	private Set<T> resultEvents;

	private Result(final Collection<T> resultEvents) {
		if (resultEvents != null) {
			this.resultEvents = new HashSet<>(resultEvents);
			// Remove potentially nulls
			this.resultEvents.remove(null);
		} else {
			this.resultEvents = Collections.emptySet();
		}
	}

	public Set<Object> getResultEvents() {
		return Collections.unmodifiableSet(this.resultEvents);
	}

	public static <T> Result<T> of(final T... events) {
		return new Result<>(List.of(events));
	}

	public static <T, S extends T> Result<T> of(final Collection<S> resultEvents) {
		return new Result<>(List.copyOf(resultEvents));
	}

	public static <T, S extends T> Result<T> of(final Optional<S> resultEvent) {
		if (resultEvent.isPresent()) {
			return new Result<>(List.of(resultEvent.get()));
		}
		return new Result<>(List.of());
	}

	@Deprecated
	public static <T> Result<T> from(final Collection<T> resultEvents) {
		return new Result<>(resultEvents);
	}

	@Deprecated
	public static <T> Result<T> empty() {
		return new Result<>(List.of());
	}
}
