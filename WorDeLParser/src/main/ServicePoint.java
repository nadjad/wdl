package main;

import generated.WorDeLLexer;
import generated.WorDeLParser;
import generated.WorDeLParser.ContentContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

import representation.nodes.ContentNode;
import support.CustomErrorListener;
import support.Error;
import support.ErrorRepository;

public class ServicePoint {
	public static void main(String[] args) {
		if (args.length == 2) {
			try {
				String ss = readFile(args[0]);
				WorDeLLexer lexer = new WorDeLLexer(new ANTLRInputStream(ss));
				CommonTokenStream tokens = new CommonTokenStream(lexer);

				WorDeLParser parser = new WorDeLParser(tokens);
				parser.addErrorListener(new CustomErrorListener());
				ContentContext result = parser.content();
				ContentNode content = result.c;

				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(new File(args[1])));
				oos.writeObject(content);
				oos.close();
				/* test the error repository */
				List<Error> errors = ErrorRepository.getErrorList();
				for (Error e : errors) {
					System.out.println(e.toString());
				}

			} catch (RecognitionException e) {
				throw new IllegalStateException(
						"Recognition exception is never thrown, only declared.");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			System.out.println("the number of argument provided is wrong");
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
