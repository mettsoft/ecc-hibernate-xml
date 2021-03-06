package com.ecc.hibernate_xml.util.app;

import java.text.ParseException;
import java.util.Scanner;

import com.ecc.hibernate_xml.util.function.CheckedFunction;

public class InputHandler {
	private static final Scanner SCANNER = new Scanner(System.in);

	public static <R> R getNextLine(String message, CheckedFunction<String, R> function) throws Exception {
		System.out.print(message);
		try {
			return function.apply(SCANNER.nextLine());				
		}
		catch (NumberFormatException|ParseException exception) {
			throw new InputException(exception);
		}
		catch (Exception exception) {
			throw exception;
		}
	}

	public static <R> R getNextLineREPL(String message, CheckedFunction<String, R> function) {
		try {
			return getNextLine(message, function);
		}
		catch (Exception exception) {
			ExceptionHandler.printException(exception);
			return getNextLineREPL(message, function);
		}
	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}