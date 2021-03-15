package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.stext.stext.impl.EventDefinitionImpl;
import org.yakindu.sct.model.stext.stext.impl.VariableDefinitionImpl;
import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<VariableDefinitionImpl> variables = new ArrayList<VariableDefinitionImpl>();
		ArrayList<EventDefinitionImpl> events = new ArrayList<EventDefinitionImpl>();

		
		while (iterator.hasNext()) {
			EObject content = iterator.next();

			if(content instanceof VariableDefinitionImpl) {
				VariableDefinitionImpl variable = (VariableDefinitionImpl)content;
				variables.add(variable);
			}
			else if (content instanceof EventDefinitionImpl) {
				EventDefinitionImpl event = (EventDefinitionImpl)content;
				events.add(event);
			}
		}

		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
				"\r\n" + 
				"import java.io.IOException;\r\n" + 
				"import java.util.Scanner;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"public class RunStatechart {"
				+ "\r\n" +
				"public static void main(String[] args) throws IOException {\n" +
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		\r\n" + 
				"		\r\n" + 
				"		Scanner scnr = new Scanner(System.in);\r\n" + 
				"		String cmd = scnr.next();\r\n" + 
				"		while(!cmd.equals(\"exit\")) {\r\n" + 
				"			switch(cmd) {");
		
		for(EventDefinitionImpl e : events) {
			System.out.println("				case \""+ e.getName()+"\":\r\n" + 
					"					s.raise" + e.getName().substring(0, 1).toUpperCase() + e.getName().substring(1) + "();\r\n" + 
					"					break;");
		}
		
		System.out.println("				default:\r\n" + 
				"					System.out.println(\"Bad command!\");\r\n" + 
				"			}\r\n" + 
				"			s.runCycle();\r\n" + 
				"			print(s);\r\n" + 
				"			cmd = scnr.next();\r\n" + 
				"		}\r\n" + 
				"		\r\n" + 
				"		System.exit(0);\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public static void print(IExampleStatemachine s) {");

		for(VariableDefinitionImpl var : variables) {
			System.out.println("		System.out.println(\""+ var.getName().substring(0,1).toUpperCase() +
					" = \" + s.getSCInterface().get" + var.getName().substring(0,1).toUpperCase() + var.getName().substring(1) + "());");
		}
		
		System.out.println("	} \n}");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
