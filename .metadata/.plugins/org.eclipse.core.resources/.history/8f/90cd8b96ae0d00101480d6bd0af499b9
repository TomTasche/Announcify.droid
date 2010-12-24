package com.announcify.util;

import android.content.Context;

public class Money {
	private static Boolean paid;

	public static boolean isPaid(final Context context) {
		if (paid != null) {
			return paid;
		}
		try {
			context.createPackageContext("com.announcify.paid", 0); // TODO:
																	// change
																	// package?

			return true;
		} catch (final Exception e) {
			// bad boy... you didn't donate!
			return false;
		}
	}
}