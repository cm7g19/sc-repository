import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class BBInterpreter{
	
	Map<String, Integer> varMap = new HashMap<String, Integer>(); //Store variables and their values.
	String[] lines; //List of lines in BB code.
	
	int pointer = 0; //Track position in BB code.
	
	public static void main(String[] args) throws IOException{
		BBInterpreter bbi = new BBInterpreter();
		bbi.go();
	}
	
	void go() throws IOException{
		
		//Get the text from the file to be interpreted.
		FileReader fr = getFR(GetInput("Name of file to interpret: "));		
		String wholeText = new String();				
		int ch;
		while ((ch=fr.read())!=-1){
			wholeText += (char)ch;
		}
		
		//Split the text into its individual lines.
		lines = Splitter(wholeText, ";");
		
		goThroughLines(0); //Go through the lines, starting from the beggining.
		
		fr.close(); //Close the filereader, since we're done using it.
    }
	
	//Finds, decodes and then executes the instruction on the given line
	//Returns if it is time for the current loop to end.
	Boolean getInstruction(String line){
		
		//Remove unnecessary whitespace.
		line = line.trim();
		line = line.replaceAll("\\s+", " ");
		
		//Seperate words, allowing us to find individual instructions and data.
		String[] commands = Splitter(line, " ");
		
		//Output line.
		System.out.println("");
		for(String c: commands)
			System.out.print(c + " ");
				
		Boolean end = false;
		
		//switch statement on instruction, to decide what it will execute.
		//commands[0] = instruction, commands[1] = variable
		switch(commands[0]){
			case "clear":
				clear(commands[1]);
				break;
			case "incr":
				incr(commands[1]);
				break;
			case "decr":
				decr(commands[1]);
				break;
			case "while":
				end = true;					
				loop(pointer,commands[1]);
				
				//loop(int i, lines);
			case "end":
				end = true;
		}
					
		//if (end) {/*System.out.println("Break");*/ break;}
		return end;
		
	}
	
	//Goes through the lines from a starting point, until it is told to stop.
	void goThroughLines(int starting){
		
		//if(condition){
		
		for (int i = starting; i < lines.length; i++) {
			pointer = i;	
			Boolean end = getInstruction(lines[i]);
			if (end) {break;}
			printVariables();
			
        }
	}
	
	//Output variables to see how they've changed.
	void printVariables(){
		System.out.println("");
		for(String variable : varMap.keySet()){
			System.out.print(":" + variable + " = " + varMap.get(variable) + ":");
		}
	}

	//Creates and/or makes variables value 0.
	void clear(String variable){
		varMap.put(variable,0);		
	}
	
	//increases the value of the variable by 1.
	void incr(String variable){
		checkVar(variable); 
		varMap.put(variable,varMap.get(variable) + 1);
	}
	
	//decreases the value of the variable by 1.
	void decr(String variable){
		if(varMap.get(variable) > 0)
			varMap.put(variable,varMap.get(variable) - 1);
	}
	
	//loops until the variable = 0. Once the loop is complete, the program goes through lines like normal.
	void loop(int starting , String variable){
		
		starting++;
		checkVar(variable);

		while(varMap.get(variable) != 0){
			goThroughLines(starting);			
		}
		goThroughLines(pointer + 1);
	}
	
	//Checks a variable exists. If not, it creates one with a value of 0.
	void checkVar(String variable){
		if(varMap.get(variable) == null)
			varMap.put(variable,0);
	}
	
	//Get a file within the users directory with the name filename
	FileReader getFR(String filename){
		FileReader fr=null; 
        try
        { 
			fr = new FileReader(System.getProperty("user.dir") + "\\" + filename);
			
        } 
        catch (FileNotFoundException fe) 
        { 
            System.out.println("File not found"); 
        }
		
		return fr;
	}
	
	//Get users input
	String GetInput(String message){
		String input = new String();
		try{  
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br= new BufferedReader(isr);
			System.out.print(message);
			input = br.readLine(); 
		}
		catch(IOException ioe){ 
			System.out.println("IO exception");
		}
		return input;
	}
	
	//SPlit a given string wherever the string REGEX exists
	String[] Splitter(String input, String REGEX){
		
		Pattern p = Pattern.compile(REGEX);
		String[] lines = p.split(input);
		return lines;
	}

}