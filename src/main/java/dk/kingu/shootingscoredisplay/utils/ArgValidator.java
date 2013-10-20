package dk.kingu.shootingscoredisplay.utils;

import java.util.List;

public class ArgValidator {

	/**
	 * Asserts that a given object is not null 
	 */
	public static void checkNotNull(Object obj, String name) {
		if(obj == null) {
			throw new IllegalStateException("The object '" + name + "' is not allowed to be null");
		}
	}
	
	/**
	 * Assert that a given string is not:
	 * - Null
	 * - Empty 
	 */
	public static void checkNotNullOrEmpty(String obj, String name) {
		checkNotNull(obj, name);
		if(obj.isEmpty()) {
			throw new IllegalStateException("The string '" + name + "' is not allowed to be empty");
		}
	}
	
	public static void checkNotNullAndLength(List<?> obj, int length, String name) {
		checkNotNull(obj, name);
		if(obj.size() != length) {
			throw new IllegalStateException("The list '" + name + "' have the size " + obj.size() 
					+ " but is required to be: " + length);
		}
	}
}
