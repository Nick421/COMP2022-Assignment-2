import java.util.*;

public class SimpleTree<T> {
	static String[] vars = {"L","G","E","B","C","K","F","V","T"};
	static int numVar = 9;

	public String data;
	public ArrayList<SimpleTree> children = new ArrayList<>();
	public SimpleTree<T> parent;
	
	public SimpleTree(String data){
		this.data = data;
	}
	
	public void addChild(SimpleTree child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void setParent(SimpleTree parent) {
        this.parent = parent;
    }
    public String evaluate(ArrayList<Integer> a){
    	//if it is a terminal return back
    	if(isVar(data) == false){
    		return data;
    	}
    	//recurse down each child
    	switch(data){
    		case "L":
    			return L(a);
    		case "G":
    			return G(a);
    		case "E":
    			return E(a);
    		case "B":
    			return B(a);
    		case "C":
    			return C(a);
    		case "K":
    			return K(a);
    		case "F":
    			return F(a);
    		case "V":
    			return V(a);
    		case "T":
    			return T(a);
    	}
    	return null;
    }
    public boolean isVar(String x){
    	for(int i = 0; i < numVar; i++) {
			if(x.compareTo(vars[i]) == 0) {
				return true;
			}
		}
	return false;
    }
    public String L(ArrayList<Integer> a){
    	//L -> EG
    	
		String ret = children.get(0).evaluate(a);
		children.get(1).evaluate(a);
    	return ret;
    }
    public String G(ArrayList<Integer> a){
    	//G -> EG
    	
    	if (children.size() == 2) {
    		children.get(0).evaluate(a);
    		children.get(1).evaluate(a);
			
			
		}
    	//G -> epsilon
    	return null;
    }
    public String E(ArrayList<Integer> a){
    	//E->(B
    	//E->V
    	//E->T
    	if(children.size() == 2){
    		return children.get(1).evaluate(a);
    	}
		return children.get(0).evaluate(a);
    }
    public String B(ArrayList<Integer> a){
    	//B->C)
    	//B->F)
  
		return children.get(0).evaluate(a);
    }
   
	public String C(ArrayList<Integer> a){
    	//C->ifEEK
    	
    	String check = children.get(1).evaluate(null);
    	//check if first E is zero or not
    	//else return second E
    	if(check.equals("0")){
    		//if zero check if K is null or not
    		String k = children.get(3).evaluate(a);
    		//if k is not null return k else return 0
    		if(k != null){
    			return k;
    		}else{
    			return "0";
    		}	
    	}

		return children.get(2).evaluate(a);	
    }
    public String K(ArrayList<Integer> a){
    	//K->E
    	if (children.size() == 1) {
			return children.get(0).evaluate(a);
		}
    	//K-> epsilon
    	return null;
    }
    public String F(ArrayList<Integer> a){
    	//F->+L
    	//F->-L
    	//F->*L
    	//F->printL
    	ArrayList<Integer> nums = new ArrayList<Integer>();
    	if(a != null){
    		nums = a;
    	}
    	
    	if (children.size() == 2) {
    		String operation = children.get(0).evaluate(a);
    		
    		if (operation.equals("+")) {
    			
    			//pass down a new list to get number for adding
    			ArrayList<Integer> adds = new ArrayList<Integer>();
    			
    			children.get(1).evaluate(adds);
    			//sum all number in the list that we pass down
    			
    			int sum = 0;
    			for(int d : adds){
    				sum += d;
    			}
    			
    			//add it to nums list in case there is more operation up the tree
    			nums.add(sum);
				return Integer.toString(sum);
				
			} else if(operation.equals("-")) {
				//pass down a new list to get number for calculation
				ArrayList<Integer> minus = new ArrayList<Integer>();
				
    			children.get(1).evaluate(minus);
    			//get the first number in the list we pass down
    			
    			int diff = minus.get(0);
    			//if list has 1 number then return the negative of that
    			//else we just go through the list and minus all of them in order
    			
    			if(minus.size() == 1){
    				diff = -minus.get(0); 
    			}else{
    			for(int d = 1; d < minus.size() ; d++){
    				diff -= minus.get(d);
    				}
    			}
    			
    			//add it to nums list in case there is more operation up the tree
    			nums.add(diff);
				return Integer.toString(diff);
				
			} else if(operation.equals("*")){
				
				//same thing here with the list
				ArrayList<Integer> times = new ArrayList<Integer>();
    			
    			children.get(1).evaluate(times);
    			
    			//we use 1 because of 0 times a number gives 0 when there's only 1 number 
    			int product = 1;
    			for(int d : times){
    				product *= d;
    			}
    			
    			//add it to nums list in case there is more operation up the tree
    			nums.add(product);
				return Integer.toString(product);
				
			}else if(operation.equals("print")){
				
				// just print what is return from the child
				String ret = children.get(1).evaluate(a);
				System.out.println(ret);
				return ret;
				
			}
    		
    	}
    	return null;
    }
    public String V(ArrayList<Integer> a){
    	//V -> a|b|c|d
    	return children.get(0).evaluate(a);
    }
    public String T(ArrayList<Integer> a){
    	//T -> 0|1|2|3
    	if(a != null){
    		a.add(Integer.parseInt(children.get(0).evaluate(a)));
    	}
    	
    	return children.get(0).evaluate(a);
    }
}
