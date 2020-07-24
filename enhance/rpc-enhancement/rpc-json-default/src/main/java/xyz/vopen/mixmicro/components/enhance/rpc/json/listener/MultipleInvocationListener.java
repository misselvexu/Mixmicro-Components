package xyz.vopen.mixmicro.components.enhance.rpc.json.listener;

import com.fasterxml.jackson.databind.JsonNode;
import xyz.vopen.mixmicro.components.enhance.rpc.json.InvocationListener;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link InvocationListener} that supports the use
 * of multiple {@link InvocationListener}s called one after another.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class MultipleInvocationListener implements InvocationListener {
	
	private final List<InvocationListener> invocationListeners;
	
	/**
	 * Creates with the given {@link InvocationListener}s,
	 * {@link #addInvocationListener(InvocationListener)} can be called to
	 * add additional {@link InvocationListener}s.
	 *
	 * @param invocationListeners the {@link InvocationListener}s
	 */
	public MultipleInvocationListener(InvocationListener... invocationListeners) {
		this.invocationListeners = new LinkedList<>();
		Collections.addAll(this.invocationListeners, invocationListeners);
	}
	
	/**
	 * Adds an {@link InvocationListener} to the end of the
	 * list of invocation listeners.
	 *
	 * @param invocationListener the {@link InvocationListener} to add
	 */
	public void addInvocationListener(InvocationListener invocationListener) {
		this.invocationListeners.add(invocationListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
  public void preInvoke(Method method, List<JsonNode> arguments) {
		for (InvocationListener invocationListener : invocationListeners) {
			invocationListener.preInvoke(method, arguments);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
  public void postInvoke(Method method, List<JsonNode> arguments, Object result, Throwable t, long duration) {
		for (InvocationListener invocationListener : invocationListeners) {
			invocationListener.postInvoke(method, arguments, result, t, duration);
		}
	}
	
}
