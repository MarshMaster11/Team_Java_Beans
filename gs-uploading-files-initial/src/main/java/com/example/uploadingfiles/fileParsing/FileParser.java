package com.example.uploadingfiles.fileParsing;

import java.io.FileReader;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.xml.crypto.Data;


/**
 * Class: FileParser
 * @author Cameron Ventimiglia, Michelle Watson, Puen Xie, Constance Yang
 * @author Christopher Wilder, David Marshall, Ephrem Engida, Matteo Kitic
 * Version: 1.1
 * Course: ITEC 3870
 * Written: September 16th, 2021
 * Modified: February 16th, 2022
 * 
 * Purpose: The purpose of this class is to have the ability to parse a JSON file
 * in order to create Parameter objects. These Parameter objects will hold the name of the
 * input parameter in the file and an ArrayList<String> of the equivalence classes for that input parameter.
 * This class is also responsible for creating all possible combinations of equivalence classes. The goal is
 * to take this class and use it in the front-end in order to process files input by the user.
 */
@Service
public class FileParser {
	/**
	 * Constructor: FileParser()
	 * This constructor is used to create FileParser objects in order to call the methods from this class.
	 */
	public FileParser() {
		
	}
	
	/**
	 * method: createCombos
	 * creates a matrix that holds all of the combinations of equivalence classes. The matrix is 
	 * made by setting the rows to the amount of combinations and columns to the amount of parameters. The algorithm populates 
	 * the matrix from bottom to top. The parameter arrList represents an ArrayList that holds all the parameters. The parameter
	 * count represents the total amount of combinations.
	 * @param arrList
	 * @param count
	 * @return returns the populated matrix
	 */
	public String[][] createCombos(ArrayList<Parameter> arrList, int count) {
        ArrayList<ArrayList<String>> paramList = new ArrayList<>(); // stores the strings of each parameters equiv classes
		
		//populate the paramList ArrayList with the equivalence Classes
	    for (Parameter temp : arrList) {
	    	paramList.add(temp.getEquivalenceClasses());
	    }
		
		int totalParams = arrList.size(); // amount of parameters
		int currentParam = paramList.get(totalParams-1).size(); // the equivclass
		int change = 1; // use this to show when to change the equivClass
		int checkChange = change * currentParam; // use this to exit the change loop and reset
		String[][] paramArray = new String[count][totalParams]; // array with the size of the total amount of params
		int temp = count-1; //temp variable used to represent the total number of combinations - 1 (rows)
		int check = totalParams; // use this as a decision maker in the if statement
		
		while (totalParams > 0) { // keep going until every parameter is ran through
			while (temp > -1) { //keep going until every row is populated 
					for (int j = 0; j < currentParam; j++) { // use j to populate the matrix 
						if ((check != totalParams) && (totalParams > 0)) { // this is used to decide which logic to use
							change = 0; // reset change so it can iterate again
							while (change < checkChange) { 
								paramArray[temp][totalParams-1] = paramList.get(totalParams-1).get(j);
								temp--;
								change++;
							}
						} else {
							paramArray[temp][totalParams-1] = paramList.get(totalParams-1).get(j);
							temp--;
						}
					}
			}
			temp = count-1; // reset temp
			totalParams--; //change the parameter
			checkChange = change * currentParam; // update checkChange
			if (totalParams > 0) { // use this to update currentParam while totalParams > 1
				currentParam = paramList.get(totalParams-1).size();
			}
		}
		
		return paramArray;
	}

	/**
	 * method: parseFile
	 * This method parses JSON files. It will add the input parameter name and equivalence classes and create Parameter objects
	 * using these. Once one Parameter object is made, it will add it to an ArrayList of type Parameter.
	 * @param fileName
	 * @return returns an ArrayList of Parameter objects
	 * @throws Exception
	 */
	public DataObject parseFile(String fileName) throws Exception {
		DataObject data = new DataObject();
		JSONObject obj = (JSONObject)new JSONParser().parse(new FileReader(fileName));
		parseObject(data, obj);
		
		return data;
	}

	/**
	 * method: parseParameters
	 * takes a DataObject and returns an ArrayList of Parameters for generating test cases.
	 * finds a parameter's name and its equivalence classes.
	 * generate a Parameter object based on them.
	 * adds the generated Parameter object to an ArrayList
	 * @param data
	 * @return an ArrayList of generated Parameter objects
	 */
	public ArrayList<Parameter> parseParameters(DataObject data) {
		ArrayList<String> paramNameList = new ArrayList<>();
		ArrayList<Parameter> pList = new ArrayList<>();

		DataObject inputParameters = data.find("InputParameters");
		for (Map.Entry<String, DataObject> entry : inputParameters.getChildObject().entrySet())
		{
			String paramName = entry.getKey();
			paramNameList.add(paramName);

			Parameter param = new Parameter();
			param.setName(paramName);

			DataObject equivClass = entry.getValue().find("EquivalenceClasses");

			for (Map.Entry<String, DataObject> subentry : equivClass.getChildObject().entrySet())
				param.addParam(subentry.getKey());

			pList.add(param);

		}
		return pList;
	}

	/**
	 * method: parseParameters
	 * takes a DataObject and returns an ArrayList of expected results for test cases.
	 * finds an expected result's name and the condition under which it applies to a test case.
	 * generate an ExpectedResult object based on them.
	 * adds the generated ExpectedResult object to an ArrayList
	 * @param data
	 * @return an ArrayList of generated ExpectedResult objects
	 */
	public ArrayList<ExpectedResult> parseExpectedResults(DataObject data) {
		ArrayList<ExpectedResult> expectedResultsList = new ArrayList<>();
		DataObject expResults = data.find("ExpectedResults");
		for (Map.Entry<String, DataObject> subentry : expResults.getChildObject().entrySet()) {
			String name = subentry.getKey();
			String condition = subentry.getValue().find("Condition");
			ExpectedResult expResult = new ExpectedResult(name, condition);
			expectedResultsList.add(expResult);
		}
		return expectedResultsList;
	}
	
	public void parseArray(JSONObject json, String key, DataObject data)
	{
		JSONArray jsonArray = (JSONArray)json.get(key);
		// sanity checks
		if (jsonArray == null)
			return;
		
		ArrayList<DataObject> objectArray = new ArrayList<DataObject>();
		ArrayList<String> stringArray = new ArrayList<String>();
		
		for (int i = 0; i < jsonArray.size(); i++)
		{
			Object object = jsonArray.get(i);
			
			if (object instanceof JSONObject)
			{
				// json array contains an jsonobject
				// create a new data object and let parseObject handle it
				DataObject arrayObject = new DataObject();
				parseObject(arrayObject, (JSONObject)object);
				objectArray.add(arrayObject);
				continue;
			}
			
			stringArray.add((String)object);
		}
		
		// sanity checks
		if (objectArray.size() > 0)
			data.addDataArrayObject(key, objectArray);
		
		if (stringArray.size() > 0)
			data.addDataArray(key, stringArray);
		
	}

		/**
	 * method: resultsList
	 * Returns a list of all possible results for a specific test case
	 * @return returns list of expected results.
	 * Author: Matteo, Ephrem, David, Chris: Group coding sessions

	 *SHUNTING YARD ALGORITHM GUIDE

		While there are tokens to be read:
2.        Read a token
3.        If it's a number add it to queue
4.        If it's an operator
5.               While there's an operator on the top of the stack with greater precedence:
6.                       Pop operators from the stack onto the output queue
7.               Push the current operator onto the stack
8.        If it's a left bracket push it onto the stack
9.        If it's a right bracket
10.            While there's not a left bracket at the top of the stack:
11.                     Pop operators from the stack onto the output queue.
12.             Pop the left bracket from the stack and discard it
13. While there are operators on the stack, pop them to the queue
		 */

	public Queue<String> resultsList(String condition) {
		//split condition into array of words
		String[] words = condition.split("\\s+");
		Stack<String> stack = new Stack<>();
		Queue<String> queue = new LinkedList<>();

		//Greater value in map = higher precedence
		HashMap<String, Integer> precedence = new HashMap<>();
		precedence.put("AND", 3);
		precedence.put("OR", 2);
		precedence.put("(", 4);
		precedence.put(")", 4);

		for (String word : words) {
			//System.out.println("Word: " + word + "<- Word");
			//push word at start of parenthesis
			if (word.charAt(0) == '(') {
				queue.add(word.substring(word.indexOf('(') + 1));
				stack.push("(");
			}
			//push word at end of parenthesis
			else if (word.endsWith(")")) {
	
				//while there is no left bracket at the top of the stack pop values from the stack onto the output queue.
				do {
					queue.add(stack.pop());

				} while (!stack.peek().equals("("));
				stack.pop(); //discard left bracket from stack

			}
			//account for AND/OR/=/!= operators
			else if (word.equalsIgnoreCase("AND") || word.equalsIgnoreCase("OR") || word.equals("=") || word.equals("!=")) {

				stack.push(word);
			}
			else {
				//add normal words to queue
				queue.add(word);
			}
		}
		while (!stack.isEmpty()) {
			queue.add(stack.pop());
		}
		return queue;
	}

	public void parseObject(DataObject data, JSONObject json)
	{
		// loop through the whole json
		Iterator<String> iter = json.keySet().iterator();
		while (iter.hasNext()) {
			
			String key = iter.next();
			
			if (json.get(key) instanceof JSONObject) {
				// if its an object (surrounded by '{}')
				// create a new DataObject and do parseObject again
				DataObject child = new DataObject();
				data.addChild(key, child);
				parseObject(child, (JSONObject)json.get(key));
				continue;
			}
			
			if (json.get(key) instanceof JSONArray) {
				// if its an array (surrounded by '[]')
				// let parseArray handle it
				parseArray(json, key, data);
				continue;
			}
			
			String value = (String)json.get(key);
			data.addDataField(key, value);
			
		}
		
	}
/**
 * method: compareVariables
 * IMPORTANT: This method is dated and needs to be modified to meet the project's needs
 * Returns a list of parameter names that appear in any ExpectedResult Conditions for a given Json file.
 * @return returns list of valid parameter names that appear in expectedResultsList
 */
	public ArrayList<String> compareVariable(ArrayList<Parameter> parametersList , ArrayList<ExpectedResult> expectedResultList) {
		ArrayList<String> matchingVariables = new ArrayList<>();
		for (Parameter parameter: parametersList) {
			for (ExpectedResult expectedResult: expectedResultList) {
				if (expectedResult.getCondition().contains(parameter.getName()) && !matchingVariables.contains(parameter.getName())) {
					matchingVariables.add(parameter.getName());
				}

			}
		} return matchingVariables;
	}
}
