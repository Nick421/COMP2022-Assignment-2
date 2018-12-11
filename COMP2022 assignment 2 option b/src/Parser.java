import java.util.*;
import java.nio.file.*;

public class Parser{
	static String[] terminals = {"a","b","c","d","0","1","2","3","(",")","+","-","*","if","print"};
	static String[] vars = {"L","G","E","B","C","K","F","V","T"};
	static String[] actionTokens = {"{L->EG}","{G->EG}","{G->ep}","{E->(B}","{E->V}","{E->T}",
			"{B->C)}","{B->F)}", "{C->ifEEK}","{K->E}", "{K->ep}", "{F->+L}","{F->-L}",
			"{F->*L}","{F->printL}","{V->a}","{V->b}","{V->c}","{V->d}","{T->0}","{T->1}",
			"{T->2}","{T->3}"};
	static HashMap<String, Integer> actionTokensMap = new HashMap<>();
	static HashMap<String, String> actionToVar = new HashMap<>();
	
	static int numVar = 9;
	static int numTerm = 15;
	
	static Stack<String> ruleStack = new Stack<String>();
	static Stack<String> inputStack = new Stack<String>();
	static Stack<SimpleTree> exprStack = new Stack<SimpleTree>();
	
	static String formatted;
	static Boolean evalMode = false;
	
	/*@param
	 * t is current terminal
	 * nextT is next terminal
	 * var is the current varibale on top of variable stack
	 * rules is the rule stack
	 * This method is the hard coded version of the parse table
	*/
	public static Stack<String> parseTable(String t, String nextT, String var, Stack<String> rules){
		
		Stack<String> returnRule = rules;
		
		//if both stack matches
		if(isATerminal(t) && isATerminal(var) && t.compareTo(var) == 0) {
			SimpleTree<String> root = new SimpleTree<>(returnRule.peek());
			exprStack.push(root);
			returnRule.pop();
			return returnRule;
		}
		
		// if found end of string on rule stack but still left over on input stack
		if(isATerminal(t) && var.compareTo("$") == 0){
			
			returnRule = null;
			return returnRule;
		}
		// if a terminal is on top of rule stack but the top of input is not the same
		if(isATerminal(var) && t.compareTo(var) != 0){
			 
			returnRule = null;
			return returnRule;
		}
		
		//implement L -> EG
		if(var.compareTo("L") == 0) {
			if(t.compareTo("a") == 0 || t.compareTo("b") == 0 || t.compareTo("c") == 0 ||
			  t.compareTo("d") == 0 || t.compareTo("0") == 0 || t.compareTo("1") == 0 ||
			  t.compareTo("2") == 0 || t.compareTo("3") == 0 || t.compareTo("(") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[0]);
					
				}
				returnRule.push("G");
				returnRule.push("E");
				return returnRule;
			}
			else{
				returnRule = null;
				return returnRule;
			}
		}
		
		
		if(var.compareTo("G") == 0) {
			//implement G -> EG
			if(t.compareTo("a") == 0 || t.compareTo("b") == 0 || t.compareTo("c") == 0 ||
			  t.compareTo("d") == 0 || t.compareTo("0") == 0 || t.compareTo("1") == 0 ||
			  t.compareTo("2") == 0 || t.compareTo("3") == 0 || t.compareTo("(") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[1]);

				}
				returnRule.push("G");
				returnRule.push("E");
				return returnRule;
			}
			//G -> epsilon
			else if((t.compareTo("$") == 0) || (t.compareTo(")") == 0) ){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[2]);

				}
				
				return returnRule;
			} 
			else{
			
				returnRule = null;
				return returnRule;
			}
		}
		
		if(var.compareTo("E") == 0) {
			// E -> V
			if(t.compareTo("a") == 0 || t.compareTo("b") == 0 || t.compareTo("c") == 0 ||
			  t.compareTo("d") == 0 ){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[4]);

				}
				returnRule.push("V");
				return returnRule;
			}
			//E -> T
			else if(t.compareTo("0") == 0 || t.compareTo("1") == 0 ||
			  t.compareTo("2") == 0 || t.compareTo("3") == 0 ){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[5]);

				}
				returnRule.push("T");
				return returnRule;
			}
			//E -> (B
			else if(t.compareTo("(") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[3]);

				}
				returnRule.push("B");
				returnRule.push("(");
				return returnRule;
			}
			else{
			
				returnRule = null;
				return returnRule;
			
			}
		}
		
		if(var.compareTo("B") == 0) {
			//B -> F)
			if(t.compareTo("+") == 0 || t.compareTo("-") == 0 || t.compareTo("*") == 0 ||
			  t.compareTo("print") == 0 ){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[7]);

				}
				returnRule.push(")");
				returnRule.push("F");
				return returnRule;
			}
			//B -> C)
			else if(t.compareTo("if") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[6]);

				}
				returnRule.push(")");
				returnRule.push("C");
				return returnRule;
			}
			else{
				
				returnRule = null;
				return returnRule;
			}
		}
		
		//C -> ifEEK
		if(var.compareTo("C") == 0) {
			
			if(t.compareTo("if") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[8]);

				}
				returnRule.push("K");
				returnRule.push("E");
				returnRule.push("E");
				returnRule.push("if");
				return returnRule;
			}
			else{
				
				returnRule = null;
				return returnRule;
			}
		}
		
		if(var.compareTo("K") == 0) {
			//K -> E
			if(t.compareTo("a") == 0 || t.compareTo("b") == 0 || t.compareTo("c") == 0 ||
			  t.compareTo("d") == 0 || t.compareTo("0") == 0 || t.compareTo("1") == 0 ||
			  t.compareTo("2") == 0 || t.compareTo("3") == 0 || t.compareTo("(") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[9]);

				}
				returnRule.push("E");
				return returnRule;
			}
			//K -> epsilon
			else if(t.compareTo(")") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[10]);

				}
				
				return returnRule;
			}
			else{
			
				returnRule = null;
				return returnRule;
			}
		}
		
		if(var.compareTo("F") == 0) {
			//F -> +L
			if(t.compareTo("+") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[11]);

				}
				returnRule.push("L");
				returnRule.push("+");
				return returnRule;
			}
			//F -> -L
			else if (t.compareTo("-") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[12]);

				}
				returnRule.push("L");
				returnRule.push("-");
				return returnRule;
			}
			//F -> *L
			else if(t.compareTo("*") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[13]);

				}
				returnRule.push("L");
				returnRule.push("*");
				return returnRule;
			}
			//F -> printL
			else if(t.compareTo("print") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[14]);

				}
				returnRule.push("L");
				returnRule.push("print");
				return returnRule;
			}
			else{
				returnRule = null;
				return returnRule;
			}
		}
		
		if(var.compareTo("V") == 0) {
			//V -> a
			if(t.compareTo("a") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[15]);

				}
				returnRule.push("a");
				return returnRule;
			}
			//V -> b
			else if (t.compareTo("b") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[16]);

				}
				returnRule.push("b");
				return returnRule;
			}
			//V -> c
			else if(t.compareTo("c") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[17]);

				}
				returnRule.push("c");
				return returnRule;
			}
			//V -> d
			else if(t.compareTo("d") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[18]);

				}
				returnRule.push("d");
				return returnRule;
			}
			else{
				
				returnRule = null;
				return returnRule;
			}
		}
		
		if(var.compareTo("T") == 0) {
			//T -> 0
			if(t.compareTo("0") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[19]);

				}
				returnRule.push("0");
				return returnRule;
			}
			//T -> 1
			else if (t.compareTo("1") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[20]);

				}
				returnRule.push("1");
				return returnRule;
			}
			//T -> 2
			else if(t.compareTo("2") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[21]);

				}
				returnRule.push("2");
				return returnRule;
			}
			//T -> 3
			else if(t.compareTo("3") == 0){
				returnRule.pop();
				if(evalMode == true){
					returnRule.push(actionTokens[22]);

				}
				returnRule.push("3");
				return returnRule;
			}
			else{
				
				returnRule = null;
				return returnRule;
			}
		}
		
		
		//check duplicate like -- , ++
		if(isATerminal(t) && isATerminal(nextT) && t.compareTo(nextT) == 0
				&& (t.compareTo("+") == 0 || t.compareTo("-") == 0 || t.compareTo("*") == 0
				|| t.compareTo("if") == 0 || t.compareTo("print") == 0)){
			
			returnRule = null;
			return returnRule;
		}
	
		return returnRule;
		
	}
	//This method check if the string is a terminal or not
	public static boolean isATerminal(String x){
		for(int i = 0; i < numTerm; i++) {
				if(x.compareTo(terminals[i]) == 0) {
					return true;
				}
			}
		return false;
	}
	//goes through the input string and initialise the input stack with terminals
	public static int parser(String formatted){
		ruleStack.push("$");
		ruleStack.push(vars[0]);
		inputStack.push("$");
		
		//add the chars in string to stack
		for(int i = formatted.length() - 1; i >= 0; i--) {
			//need to push "if" and "print" as 1 string to stack
			String token = Character.toString(formatted.charAt(i));
			//System.out.println(token);
			String token1 = null;
			String token2 = null;
			String token3 = null;
			String token4 = null;
			if(i >= 4){
				token1 = Character.toString(formatted.charAt(i-1));
				token2 = Character.toString(formatted.charAt(i-2));
				token3 = Character.toString(formatted.charAt(i-3));
				token4 = Character.toString(formatted.charAt(i-4));
				if(token.compareTo("t") == 0 && token1.compareTo("n") == 0
						&& token2.compareTo("i") == 0 && token3.compareTo("r") == 0
						&& token4.compareTo("p") == 0){
					token = "print";
					i-= 4;
				}
				
			}
			if(i >= 1){
				token1 = Character.toString(formatted.charAt(i-1));
				if(token.compareTo("f") == 0 && token1.compareTo("i") == 0){
					token = "if";
					i--;
				}
			}
			
			
			//System.out.println(token);
			if(isATerminal(token) == false){
				System.out.println("ERROR_INVALID_SYMBOL");
				return 1;
			}
			inputStack.push(token);
		}
		return 0;
	}
	//main part of the program, loops till rule stack is empty
	public static void parsing(){
		boolean end = false;
		//int debug = 0;
		
		while(end == false){
			
			//when in evalMode, don't print stack trace
			if(evalMode == false){
				print_stacks(inputStack,ruleStack,exprStack);
			}
			
			String currentT = inputStack.get(inputStack.size()-1);
			String nextT = null;
			
			if(inputStack.size() - 2 >= 0) {
				nextT = inputStack.get(inputStack.size() - 2);
			}
			
			if(currentT.compareTo(ruleStack.get(ruleStack.size() - 1)) == 0 && currentT.compareTo("$") == 0) {
				end = true;
				//now that we got the root (the whole tree)
				//we evaluate it and output
				if(evalMode == true){
					SimpleTree<?> t = exprStack.pop();
					t.evaluate(null);
					break;
				}else{
					System.out.println("ACCEPTED");
					break;
				}
				
			}
			
			//check if both input and rule is the same
			//if yes pop the char in input stack
			String check = ruleStack.get(ruleStack.size() - 1);
			if(isATerminal(check) && isATerminal(inputStack.peek())){
				inputStack.pop();
			}
			//if top of rule stack is an action token, make tree/node
			if(isactionTokens(check) && evalMode == true){
				makeSubtree(check);
				
			}
			//parse it through the table
			ruleStack = parseTable(currentT, nextT, check , ruleStack);
			
			if(ruleStack == null) {
			    System.out.println("REJECTED");
			    break;
			}
			//debug++;
		}
	}
	/*@param string for the tree node data
	 * make a sub-tree
	 * i.e if action token is T -> 1 
	 * produce this tree
	 *         T
	 *        /
	 *       1
	 */
	public static void makeSubtree(String t){
		//pop the action token
		ruleStack.pop();
		//get corresponding node data to the action token
		String reminder = actionToVar.get(t);
		//get the length of production rule ie L-> EG would be 2
		int n = actionTokensMap.get(t);
		//make new node with the variable/terminal
		SimpleTree<String> newRoot = new SimpleTree<>(reminder);
		//temp stack so I can make the children in correct order, 0 index is left most child
		Stack<SimpleTree> temp = new Stack<SimpleTree>();
		//get the nodes in exprStack and push to temp, reverse order 
		for(int i = 0; i < n; i++){
			temp.push(exprStack.pop());
		}
		//add child to the new root
		for(int i = 0; i < n; i++){
			newRoot.addChild(temp.pop());
		}
		//push the new subtree back
		exprStack.push(newRoot);
	}
	//this method simply hardcoded initialise the maps 
	//I use to map tokens to number and token to variable
	public static void initialiseTokens(){
		
		actionTokensMap.put("{L->EG}", 2);
		actionTokensMap.put("{G->EG}", 2);
		actionTokensMap.put("{G->ep}", 0);
		actionTokensMap.put("{E->(B}", 2);
		actionTokensMap.put("{E->V}", 1);
		actionTokensMap.put("{E->T}", 1);
		actionTokensMap.put("{B->C)}", 2);
		actionTokensMap.put("{B->F)}", 2);
		actionTokensMap.put("{C->ifEEK}", 4);
		actionTokensMap.put("{K->E}", 1);
		actionTokensMap.put("{K->ep}", 0);
		actionTokensMap.put("{F->+L}", 2);
		actionTokensMap.put("{F->-L}", 2);
		actionTokensMap.put("{F->*L}", 2);
		actionTokensMap.put("{F->printL}", 2);
		actionTokensMap.put("{V->a}", 1);
		actionTokensMap.put("{V->b}", 1);
		actionTokensMap.put("{V->c}", 1);
		actionTokensMap.put("{V->d}", 1);
		actionTokensMap.put("{T->0}", 1);
		actionTokensMap.put("{T->1}", 1);
		actionTokensMap.put("{T->2}", 1);
		actionTokensMap.put("{T->3}", 1);
		
		actionToVar.put("{L->EG}", "L");
		actionToVar.put("{G->EG}", "G");
		actionToVar.put("{G->ep}", "G");
		actionToVar.put("{E->(B}", "E");
		actionToVar.put("{E->V}", "E");
		actionToVar.put("{E->T}", "E");
		actionToVar.put("{B->C)}", "B");
		actionToVar.put("{B->F)}", "B");
		actionToVar.put("{C->ifEEK}", "C");
		actionToVar.put("{K->E}", "K");
		actionToVar.put("{K->ep}", "K");
		actionToVar.put("{F->+L}", "F");
		actionToVar.put("{F->-L}", "F");
		actionToVar.put("{F->*L}", "F");
		actionToVar.put("{F->printL}", "F");
		actionToVar.put("{V->a}", "V");
		actionToVar.put("{V->b}", "V");
		actionToVar.put("{V->c}", "V");
		actionToVar.put("{V->d}", "V");
		actionToVar.put("{T->0}", "T");
		actionToVar.put("{T->1}", "T");
		actionToVar.put("{T->2}", "T");
		actionToVar.put("{T->3}", "T");
		
	}
	//check if A is an action token
	public static boolean isactionTokens(String A){
		if(actionTokensMap.containsKey(A)) {
				return true;
		}
		return false;
	}
	
	public static void print_stacks(Stack<String> in, Stack<String> ru , Stack<SimpleTree> e) {
		
		for(int i = in.size() - 1; i >= 0; i--) {
			System.out.print(in.get(i));
		}
		
		System.out.print("\t");
		
		for(int j = ru.size() - 1; j >= 0; j--) {
			System.out.print(ru.get(j));
		}
		
		if(evalMode == true){
			System.out.print("\t");
			for(int k = e.size() - 1; k >= 0; k--) {
				System.out.print(e.get(k).toString());
			}
		}
		System.out.println();

	}
	//from https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
	public static String readFileAsString(String fileName)throws Exception 
  	{ 
		String data = ""; 
		data = new String(Files.readAllBytes(Paths.get(fileName))); 
		return data; 
  	} 
	
	public static void main(String[] args) throws Exception {
		initialiseTokens();
		String data = readFileAsString(args[0]);
		if(args.length > 2){
			System.out.println("Parser only accepts 2 args");
			return;
		}
		//check if extension arg is pass
		if(args.length == 2 && args[1] != null){
			String eval = args[1];
			if(eval.equals("eval")){
	   			evalMode = true;
	   		}
		}
		//delete all types of white spaces
		formatted = data.replaceAll("\\s","");
   		//System.out.println(formatted); 
   		if (parser(formatted) == 1){
   			return;
   		}
		parsing();
	}
}