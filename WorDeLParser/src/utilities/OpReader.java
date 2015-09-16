package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import representation.Operator;
import representation.Port;
import representation.Type;

public class OpReader {
	private static String[] TYPES = { "Integer", "String", "Float", "Boolean" };
	private static List<String> TYPES_LIST = Arrays.asList(TYPES);

	public static Map<String, Operator> readOperators(String filepath) {
		Map<String, Operator> operators = new HashMap<String, Operator>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					filepath)));
			String line;
			List<String> lines = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();

			for (int i = 0; i < lines.size(); i += 4) {

				// extract operator name
				String[] identifiers = lines.get(i).split(":");

				// extract the operator's path
				String opPath = lines.get(i + 1).trim();

				// extract input parameters
				List<Port> inParameters = new ArrayList<Port>();
				String[] params = lines.get(i + 2).split("\\s+");
				for (int j = 0; j < params.length; j++) {
					String components[] = params[j].split(":");
					inParameters.add(new Port(components[0],
							extractType(components[1])));
				}

				// extract output params
				List<Port> outParameters = new ArrayList<Port>();
				String[] params1 = lines.get(i + 3).split("\\s+");
				for (int j = 0; j < params1.length; j++) {
					String components[] = params1[j].split(":");
					outParameters.add(new Port(components[0],
							extractType(components[1])));
				}
				// System.out.println("&&&&&&&&&&&&&&&&&&:" + identifiers[1]);
				operators.put(identifiers[1].trim(), new Operator(
						identifiers[1], opPath, inParameters, outParameters));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return operators;
	}

	private static Type extractType(String text) {
		if (text.indexOf("List") == 0) {
			String subtype = text.substring(text.indexOf('<') + 1,
					text.lastIndexOf('>'));
			Type sbtp = extractType(subtype);
			return representation.types.CompositeType.getInstance("List", sbtp);
		} else if (text.indexOf("Tuple") == 0) {
			String subtypesString = text.substring(text.indexOf('(') + 1,
					text.lastIndexOf(')'));
			String subtypes[] = subtypesString.split(",");
			List<Type> subtpList = new ArrayList<Type>();
			for (int i = 0; i < subtypes.length; i++) {
				subtpList.add(extractType(subtypes[i]));
			}
			return representation.types.CompositeType.getInstance("Tuple",
					subtpList);
		} else if (TYPES_LIST.contains(text)) {
			return representation.types.BaseType.getInstance(text);
		} else {
			return null;
		}
	}
}
