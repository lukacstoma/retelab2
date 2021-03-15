package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

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
		ArrayList<String> traps = new ArrayList<String>();
		ArrayList<State> noNames = new ArrayList<State>();

		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if(state.getName()== "" || state.getName()==null) {
					System.out.println("A state has no name. Suggested name: UnknownState" + (noNames.size()+1));
					noNames.add(state);
				}
				else {				
					System.out.println(state.getName());
				}
				if(state.getOutgoingTransitions().size() == 0)
					traps.add(state.getName());
			}
			else if(content instanceof Transition) {
				Transition t = (Transition) content;
				if(!(t.getSource().getName()=="")) {
					System.out.println(t.getSource().getName() + " -> "+ t.getTarget().getName());
				}
				else {
					System.out.println("Entry point -> "+ t.getTarget().getName());
				}
			}
		}
		
		System.out.println("\nTraps:");
		for(String t : traps ) {
			System.out.println(t);
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
