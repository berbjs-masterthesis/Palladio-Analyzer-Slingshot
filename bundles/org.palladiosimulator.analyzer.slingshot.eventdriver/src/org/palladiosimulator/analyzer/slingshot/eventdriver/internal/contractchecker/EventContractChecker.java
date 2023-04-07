package org.palladiosimulator.analyzer.slingshot.eventdriver.internal.contractchecker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.PostIntercept;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.eventdriver.entity.interceptors.InterceptorInformation;
import org.palladiosimulator.analyzer.slingshot.eventdriver.internal.contractchecker.exception.NoContractFoundException;
import org.palladiosimulator.analyzer.slingshot.eventdriver.returntypes.InterceptionResult;
import org.palladiosimulator.analyzer.slingshot.eventdriver.returntypes.Result;

public final class EventContractChecker {

	private static final Logger LOGGER = Logger.getLogger(EventContractChecker.class);

	public static void checkEventContract(final Method method, final Object target, final Class<?> eventCls) throws NoContractFoundException {
		final Class<?> cls = target.getClass();

		getOnEventContract(target, eventCls)
			.findFirst()
			.orElseThrow(() -> new NoContractFoundException(method.getName(), cls.getCanonicalName(), eventCls.getName()));

		LOGGER.debug("Contract found!");
	}

	@PostIntercept
	public InterceptionResult postEventContractChecker(final InterceptorInformation information, final Object event, final Result<?> result) {
		return information.getAssociatedContracts().stream()
			.flatMap(ctr -> ctr.getThen().stream())
			.filter(cls -> result.getResultEvents().stream()
					.map(r -> r.getClass())
					.anyMatch(rcls -> rcls.isAssignableFrom(cls)))
			.findAny()
			.map(cls -> (InterceptionResult) InterceptionResult.success())
			.orElseGet(() -> InterceptionResult.error(null));
	}
	
	public static Stream<OnEvent> getOnEventContract(final Object target, final Class<?> eventClass) {
		return Arrays.stream(target.getClass().getAnnotationsByType(OnEvent.class))
				.filter(contract -> contract.when().isAssignableFrom(eventClass));
	}
}
