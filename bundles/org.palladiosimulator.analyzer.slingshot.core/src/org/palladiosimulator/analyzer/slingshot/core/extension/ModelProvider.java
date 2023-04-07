package org.palladiosimulator.analyzer.slingshot.core.extension;

import javax.inject.Provider;

import org.eclipse.emf.ecore.EObject;


public interface ModelProvider<T extends EObject> extends Provider<T> {

}
