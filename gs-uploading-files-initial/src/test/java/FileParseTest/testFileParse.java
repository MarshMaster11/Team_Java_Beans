package FileParseTest;

import static org.junit.jupiter.api.Assertions.*;
import java.security.InvalidParameterException;
import java.beans.Transient;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;

import com.example.uploadingfiles.fileParsing.DataObject;
import com.example.uploadingfiles.fileParsing.ExpectedResult;
import org.junit.jupiter.api.Test;

//import com.example.uploadingfiles.fileParsing.FileParser;
import com.example.uploadingfiles.fileParsing.Parameter;
import com.example.uploadingfiles.storage.StorageException;
import com.example.uploadingfiles.fileParsing.FileParser;

/**
 * Class: testFileParse
 * @author Cameron Ventimiglia
 * Version: 1.0
 * Written: October 31, 2021
 * Course: ITEC 3870
 * 
 * Purpose: To test the methods inside of the FileParse Class
 */
class testFileParse {

	@Test
	void testParseFile() {

		FileParser fileParser = new FileParser();

		try {
			DataObject dataObject = fileParser.parseFile("ExecutionQueueOnSave.json");
			ArrayList<Parameter> arrList = fileParser.parseParameters(dataObject);
			assertTrue(arrList.size() > 0, "error parsing the file");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testCreateCombos() {
		FileParser fileParser = new FileParser();

		try {
			int count = 1;
			DataObject dataObject = fileParser.parseFile("ExecutionQueueOnSave.json");
			ArrayList<Parameter> arrList = fileParser.parseParameters(dataObject);

			for (Parameter temp : arrList) {
				count = count * temp.getEquivalenceClasses().size();
			}

			String[][] combos = fileParser.createCombos(arrList, count);
			assertTrue(combos.length > 0, "error creating matrix");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testCountParameter() {
		FileParser fileParser = new FileParser();

		try {
			int count = 1;
			DataObject dataObject = fileParser.parseFile("ExecutionQueueOnSave.json");
			ArrayList<Parameter> arrList = fileParser.parseParameters(dataObject);

			for (Parameter temp : arrList) {
				count = count * temp.getEquivalenceClasses().size();
			}

			assertTrue(count == 16, "error updating parameter: count");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void checkFileTypeLimitationError() {
		String filename = "testFile.txt";
		int lastIndext = filename.lastIndexOf('.');
		String extension = filename.substring(lastIndext, filename.length());
		
		assertThrows(StorageException.class, () -> {
			if (!(extension).equalsIgnoreCase(".json")) {
				throw new StorageException("Failed to store non-json file");
			}
		});
	}

	/**
	 * method: testPrepareCondition()
	 * Preliminary testing method that prints the returned lists of the prepareCondition method when applied to a given Json file.
	 */
	@Test
	void testPrepareCondition() {
		FileParser fileParser = new FileParser();
		try {
			DataObject dataObject = fileParser.parseFile("ExecutionQueueOnSave.json");
			//DataObject dataObject = fileParser.parseFile("OrgLevelUnits.json");
			//DataObject dataObject = fileParser.parseFile("QuadraticEquationSolver.json");

			ArrayList<ExpectedResult> arrList = fileParser.parseExpectedResults(dataObject);
			for (ExpectedResult expectedResult: arrList) {
				fileParser.prepareCondition(expectedResult.getCondition());
				System.out.println(fileParser.prepareCondition(expectedResult.getCondition()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	Tests for prepare condition method
	    that checks if it functions as expected
	*/

	@Test
	void testPrepareCondition1(){
		FileParser fileParser = new FileParser();
		Queue<String> condition = fileParser.prepareCondition("A = 0");
		     //A=

		assertEquals(3,condition.size());
		assertEquals("A",condition.poll());
		assertEquals("0",condition.poll());
		assertEquals("=",condition.poll());

	}
	@Test
	void testPrepareCondition2(){
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = Ready OR InterfaceX-RunState = Running)");

		//A=

		assertNotEquals(1,condition.size());
		assertNotEquals("A",condition.poll());
		assertNotEquals("0",condition.poll());
		assertNotEquals("!=",condition.poll());
		assertEquals("InterfaceX-RunState",condition.poll());

	}



	@Test
	void testConditionisValid5(){
		FileParser fileParser = new FileParser();
		Queue<String> condition = fileParser.prepareCondition("A = 0 and Discriminant = GreaterThanZero");
		String[] testCase = {"Positive", "GreaterThanZero"};
		Map<String, Integer> parameters = Map.of("A", 0, "Discriminant", 1);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertFalse(result);

	}

	/*
	@Test
	void testIsConditionValid4() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState == Saved AND ((InterfaceX-RunState = NeverRun) OR (InterfaceX-RunState = Completed))");
		if(condition.contains(")") || condition.contains("(")){
			throw new InvalidParameterException("Check for extra bracket in the condition");
		}
		String[] testCase = {"Saved", "WebExecution", "Completed"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertTrue(result);
	}
	*/


	@Test
	void testPrepareCondition3(){
		FileParser fileParser = new FileParser();
		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState = Completed)");
		String[] stringArray = {"InterfaceX-EditState", "Saved", "=", "InterfaceX-RunState", "NeverRun", "=", "InterfaceX-RunState", "Completed", "=", "OR", "AND"};
		for (int i = 0; i < stringArray.length; i++) {
			assertEquals(stringArray[i],condition.poll());
		}

	}


	@Test
	void testIsConditionValid() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState = Completed)");
		String[] testCase = {"Saved", "WebExecution", "Completed"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertTrue(result);
	}

	@Test
	void testIsConditionValidInvalid() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState = Completed)");
		String[] testCase = {"Saved", "WebExecution", "Ready"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertFalse(result);
	}
    //more tests

	@Test
	void testIsConditionValid2() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState = Completed)");
		String[] testCase = {"Saved", "WebExecution", "NeverRun"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertTrue(result);
	}

	@Test
	void testIsConditionValidInvalid2() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState = Completed)");
		String[] testCase = {"NotYetSaved", "WebExecution", "Ready"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertFalse(result);
	}

	@Test
	void testIsConditionValid3() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = NotSaved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState = Completed)");
		String[] testCase = {"NotSaved", "WebExecution", "NeverRun"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertTrue(result);
	}

	@Test
	void testIsConditionValidInvalid3() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState != Completed)");
		String[] testCase = {"NotYetSaved", "WebExecution", "NotCompletedYet"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertFalse(result);
	}


	//unit test for prepareCondition





	@Test
	void testIsConditionValid4() {
		FileParser fileParser = new FileParser();

		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = NotYetSaved AND (InterfaceX-RunState = NeverRun OR InterfaceX-RunState = Running)");
		String[] testCase = {"NotYetSaved", "WebExecution", "Running"};
		Map<String, Integer> parameters = Map.of("InterfaceX-EditState", 0, "ExecutionType", 1, "InterfaceX-RunState", 2);
		boolean result = fileParser.isConditionValid(condition, testCase, parameters);
		assertTrue(result);
	}


	@Test
	void testConditionQueue() {
		FileParser fileParser = new FileParser();
		Queue<String> condition = fileParser.prepareCondition("InterfaceX-EditState = Saved AND (InterfaceX-RunState = Ready OR InterfaceX-RunState = Running))");
		
		/*assertNotEquals(1,condition.size());
		assertNotEquals("A",condition.poll());
		assertNotEquals("0",condition.poll());
		assertNotEquals("!=",condition.poll());
		assertEquals("InterfaceX-RunState",condition.poll());
		*/
	}

}
