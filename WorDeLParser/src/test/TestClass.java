package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import representation.Operator;
import representation.Type;
import representation.types.BaseType;
import representation.types.CompositeType;
import utilities.OpReader;

public class TestClass {

	public static void main(String[] args) {
		// typeTest();
		testOperatorRead();
	}

	private static void testOperatorRead() {
		Map<String, Operator> ops = OpReader.readOperators("src//Operators.txt");
		Collection<Operator> es = ops.values();
		for (Operator o : es)
			System.out.println(o.toString());
	}

	private static void typeTest() {
		/*
		 * test list - type equality
		 */
		// first list
		Type associated = BaseType.getInstance("Integer");
		CompositeType ct = CompositeType.getInstance("List", associated);

		// second list
		Type associated1 = BaseType.getInstance("Integer");
		CompositeType ct1 = CompositeType.getInstance("List", associated1);
		test(ct, ct1);

		test(ct, associated);

		/*
		 * test tuple-type equality
		 */

		// first tuple
		Type associated2 = BaseType.getInstance("Integer");
		Type associated3 = BaseType.getInstance("String");
		List<Type> associates = new ArrayList<Type>();
		associates.add(associated2);
		associates.add(associated3);
		CompositeType ct21 = CompositeType.getInstance("List", associates);
		CompositeType ct2 = CompositeType.getInstance("Tuple", ct21);

		// second tuple
		Type associated4 = BaseType.getInstance("Integer");
		Type associated5 = BaseType.getInstance("String");
		List<Type> associates1 = new ArrayList<Type>();
		associates1.add(associated4);
		associates1.add(associated5);
		CompositeType ct31 = CompositeType.getInstance("List", associates1);
		CompositeType ct3 = CompositeType.getInstance("Tuple", ct31);
		test(ct2, ct3);
	}

	private static void test(Type t1, Type t2) {
		System.out.println("************************************");
		System.out.println("t1 equal to t2:" + t1.equals(t2));
		System.out.println(t1.toString() + "\n" + t2.toString());
	}
}
