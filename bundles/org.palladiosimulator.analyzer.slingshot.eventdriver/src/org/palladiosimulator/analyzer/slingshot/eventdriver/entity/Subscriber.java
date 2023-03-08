package org.palladiosimulator.analyzer.slingshot.eventdriver.entity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.entity.interceptors.IPostInterceptor;
import org.palladiosimulator.analyzer.slingshot.eventdriver.entity.interceptors.IPreInterceptor;
import org.palladiosimulator.analyzer.slingshot.eventdriver.entity.interceptors.InterceptorInformation;
import org.palladiosimulator.analyzer.slingshot.eventdriver.returntypes.InterceptionResult;
import org.palladiosimulator.analyzer.slingshot.eventdriver.returntypes.Result;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import java.util.Collections;

public class Subscriber<T> implements Consumer<T>, Disposable, Comparable<Subscriber<T>>, InterceptorInformation {
	
	private boolean disposed = false;
	
	private final int priority;
	private final Class<?>[] reifiedClasses;
	private final String name;
	private final EventHandler<? super T> handler;
	private final Class<?> handlerType;
	private final Optional<Class<?>> enclosingType;
	private final Class<T> forEvent;
	private final Optional<IPreInterceptor> preInterceptor;
	private final Optional<IPostInterceptor> postInterceptor;
	private final List<EventContract> associatedContracts;

	private Subscriber(final Builder<T> builder) {
		this.priority = builder.priority;
		this.reifiedClasses = builder.reifiedClasses;
		this.name = builder.name;
		this.handler = builder.handler;
		this.handlerType = builder.handlerType;
		this.enclosingType = builder.enclosingType;
		this.forEvent = builder.forEvent;
		this.preInterceptor = builder.preInterceptor;
		this.postInterceptor = builder.postInterceptor;
		this.associatedContracts = builder.associatedContracts;
	}
	
	@Override
	public void accept(final T event) throws Exception {
		final InterceptorInformation preInterceptionInformation = this;
		
		final InterceptionResult preInterceptionResult = this.preInterceptor
				.map(preInterceptor -> preInterceptor.apply(preInterceptionInformation, event))
				.orElseGet(() -> InterceptionResult.success());
		
		if (!this.checkIfCorrectlyReified(event) || !preInterceptionResult.wasSuccessful()) {
			return;
		}
		
		final Result<?> result = this.handler.acceptEvent(event);
		

		final InterceptionResult postInterceptionResult = this.postInterceptor
				.map(postInterceptor -> postInterceptor.apply(preInterceptionInformation, event, result))
				.orElseGet(() -> InterceptionResult.success());
		
	}
	
	@Override
	public void dispose() {
		if (!disposed) {
			this.disposed = true;
		}
	}
	
	@Override
	public boolean isDisposed() {
		return this.disposed;
	}
	
	@Override
	public int compareTo(final Subscriber<T> other) {
		return Integer.compare(other.priority, priority);
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public Class<?>[] getReifiedClasses() {
		return this.reifiedClasses;
	}

	public String getName() {
		return name;
	}

	public Class<?> getHandlerType() {
		return Function.class;
	}

	public Optional<Class<?>> getEnclosingType() {
		return enclosingType;
	}

	public Class<T> getEventType() {
		return forEvent;
	}

	public List<EventContract> getAssociatedContracts() {
		return associatedContracts;
	}
	
	private boolean checkIfCorrectlyReified(final Object event) {
		if (event instanceof ReifiedEvent<?>) {
			if (this.getReifiedClasses() == null || this.getReifiedClasses().length == 0) {
				// We always accept then
				return true;
			}
			
			final ReifiedEvent<?> reifiedEvent = (ReifiedEvent<?>) event;
			return this.getReifiedClasses()[0].isAssignableFrom(reifiedEvent.getTypeToken().getRawType());
		}
		// Since it's not a reified event, check is not needed.
		return true;
	}

	/**
	 * Creates builder to build {@link Subscriber}.
	 * @return created builder
	 */
	public static <T> Builder<T> builder(final Class<T> forEvent) {
		return new Builder<>(forEvent);
	}

	public static final class Builder<T> {
		private int priority;
		private Class<?>[] reifiedClasses;
		private String name;
		private EventHandler<? super T> handler;
		private Class<?> handlerType = EventHandler.class;
		private Optional<Class<?>> enclosingType = Optional.empty();
		private Class<T> forEvent;
		private Optional<IPreInterceptor> preInterceptor = Optional.empty();
		private Optional<IPostInterceptor> postInterceptor = Optional.empty();
		private List<EventContract> associatedContracts = Collections.emptyList();

		private Builder(final Class<T> forEvent) {
			this.forEvent = forEvent;
		}

		public Builder<T> priority(int priority) {
			this.priority = priority;
			return this;
		}

		public Builder<T> reifiedClasses(Class<?>[] reifiedClasses) {
			this.reifiedClasses = reifiedClasses;
			return this;
		}

		public Builder<T> name(String name) {
			this.name = name;
			return this;
		}

		public Builder<T> handler(EventHandler<? super T> handler) {
			this.handler = handler;
			return this;
		}
		
		public Builder<T> handlerType(final Class<?> handlerType) {
			this.handlerType = handlerType;
			return this;
		}

		public Builder<T> enclosingType(Class<?> enclosingType) {
			this.enclosingType = Optional.ofNullable(enclosingType);
			return this;
		}

		public Builder<T> preInterceptor(IPreInterceptor preInterceptor) {
			this.preInterceptor = Optional.ofNullable(preInterceptor);
			return this;
		}

		public Builder<T> postInterceptor(IPostInterceptor postInterceptor) {
			this.postInterceptor = Optional.ofNullable(postInterceptor);
			return this;
		}

		public Builder<T> associatedContracts(List<EventContract> associatedContracts) {
			this.associatedContracts = associatedContracts;
			return this;
		}

		public Subscriber<T> build() {
			return new Subscriber<>(this);
		}
	}
}
