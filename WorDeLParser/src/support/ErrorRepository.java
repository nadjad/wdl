package support;

import java.util.ArrayList;
import java.util.List;

public class ErrorRepository {
	private static List<Error> errorList = new ArrayList<Error>();

	public static void addError(Error error) {
		error.setId(errorList.size() + 1);
		errorList.add(error);
	}

	public static void reset() {
		errorList.clear();
	}

	public static List<Error> getErrorList() {
		List<Error> errors = new ArrayList<Error>();
		errors.addAll(errorList);
		return errors;
	}

	public static int getNumberOfErrors() {
		return errorList.size();
	}
}
