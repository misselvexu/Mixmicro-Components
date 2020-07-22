package xyz.vopen.mixmicro.components.enhance.rpc.json.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Util {
	
	@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
	public static final String DEFAULT_HOSTNAME = "0.0.0.0";
	
	public static boolean hasNonNullObjectData(final ObjectNode node, final String key) {
		return hasNonNullData(node, key) && node.get(key).isObject();
	}
	
	public static boolean hasNonNullData(final ObjectNode node, final String key) {
		return node.has(key) && !node.get(key).isNull();
	}
	
	public static boolean hasNonNullTextualData(final ObjectNode node, final String key) {
		return hasNonNullData(node, key) && node.get(key).isTextual();
	}
}
