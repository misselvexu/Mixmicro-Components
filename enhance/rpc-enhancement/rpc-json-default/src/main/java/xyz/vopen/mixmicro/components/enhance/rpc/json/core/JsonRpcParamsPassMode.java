package xyz.vopen.mixmicro.components.enhance.rpc.json.core;

/**
 * The Mixmicro RPC specification allows either passing parameters as an Array, for by-position arguments, or as an Object,
 * for by-name arguments.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public enum JsonRpcParamsPassMode {
	AUTO,
	ARRAY,
	OBJECT
}
