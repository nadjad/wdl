package main;

import generated.WorDeLLexer;
import generated.WorDeLParser;
import generated.WorDeLParser.ContentContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

import representation.nodes.ContentNode;
import representation.nodes.FlowNode;
import support.CustomErrorListener;
import support.Error;
import support.ErrorRepository;

public class MainClass {
	public static void main(String[] args) {
		try {
			// lexer splits input into tokens
			// ANTLRStringStream input = new ANTLRStringStream("hello fucker");

			// CharStream cs = (CharStream) new
			// ANTLRStringStream("hello");

			// String s = "flow: frt[a,b,c] op [d][e,f] opp [g]end";
			String ss = readFile("src\\Input.txt");
			WorDeLLexer lexer = new WorDeLLexer(new ANTLRInputStream(ss));
			// lexer.reset();
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			WorDeLParser parser = new WorDeLParser(tokens);
			parser.addErrorListener(new CustomErrorListener());
			ContentContext result = parser.content();
			ContentNode content = result.c;
			Map<String, FlowNode> aa = content.getFlows();
			// System.out.println("oooooooooooooooooo" + aa.size());
			// result.
			System.out.println(result.toStringTree());

			System.out.println("------------");
			System.out.println(content.getSimulation());
			/* test the error repository */
			List<Error> errors = ErrorRepository.getErrorList();
			for (Error e : errors) {
				System.out.println(e.toString());
			}
			// ///////////**************** second ********************/

			/*******************************************************/
			// TokenStream tokens = new CommonTokenStream( (TokenSource) new
			// FirstLexer( input ) );

			// //parser generates abstract syntax tree
			// S001HelloWordParser parser = new S001HelloWordParser(tokens);
			// S001HelloWordParser.expression_return ret = parser.expression();;
			// return ast;
		} catch (RecognitionException e) {
			throw new IllegalStateException(
					"Recognition exception is never thrown, only declared.");
		}
	}

	static String readFile(String path) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded);
	}
}
