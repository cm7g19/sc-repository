import java.io.*; //Used for BufferReader and InputStreamReader objects
import java.net.*; //Used for URL object

public class SSChallengeEmail{
	

	public static void main(String[] args) throws Exception{
		SSChallengeEmail obj = new SSChallengeEmail(); //Create new SSChallengeEmail object
		obj.Version2(); 
	}
	
	//Used to run non-static methods which cannot be run in main.
	public void Run() throws Exception{
		
		String emailName = GetInput("Email name: "); //Get name user wants
		String address = "https://www.ecs.soton.ac.uk/people/" + emailName; //Create the address to the website.
		
		URL websiteURL = new URL(address);	 //Create url object	
		BufferedReader br = new BufferedReader(new InputStreamReader(websiteURL.openStream())); //Extract html data from url
		
		String inputLine; //Set up string to store line the desired name is on
		while ((inputLine = br.readLine()) != null) { //Go through every line in the HTML
			inputLine.toString(); //Convert the entire line to string (otherwise it can't find 'property=name', only property and name seperatly)
			if(inputLine.contains("property=\"name\"")){ //If line contains 'property=name'...
				break;} //Found the desired line, end the for loop.
			
		}
		
		
		//Narrow down the line until it includes only the desired name
		inputLine = NarrowDownString(inputLine, '1', '1'); 
		//The name lies between the first uses of H1 and H1. Character 1 isn't used before this, so it is used here to narrow down the line.
		inputLine = NarrowDownString(inputLine, '>', '<'); 
		//after the line is narrowed down the name is between first instances of > and <
		
		System.out.println(inputLine); //Output the proper name
		
        br.close(); //No longer using the BufferReader so it is closed.
		
	}
	

	
	//Get users input from the cmd.
	String GetInput(String message){
		String input = new String();
		try{  
			InputStreamReader isr = new InputStreamReader(System.in); //Used to get users input from the cmd
			BufferedReader br= new BufferedReader(isr); //Used to read contents of input.
			System.out.print(message);
			input = br.readLine(); //Get the users input - the email name they want to convert to a proper name.
		}
		catch(IOException ioe){ //In case of IO exception...
			System.out.println("IO exception"); //Print error message
		}
		return input;
	}
	
	//Narrows down a string, creating a substring between two characters.
	String NarrowDownString(String string, char target1, char target2){
		
		int partBegin = string.indexOf(target1); //Index of first instace of target1 in string.
		int partEnd = string.indexOf(target2,partBegin + 1); //Index of first instance of target2 after target1.
		
		return string.substring(partBegin + 1,partEnd);
	}
	
}