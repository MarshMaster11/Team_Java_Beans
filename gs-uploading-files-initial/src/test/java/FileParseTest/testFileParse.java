package FileParseTest;

import static org.junit.jupiter.api.Assertions.*;

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
	 * Preliminary test case that prints the returned lists of the prepareCondition method when applied to a given Json file.
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
	void testCompareVariables() {
		FileParser fileParser = new FileParser();
		try {
			DataObject dataObject = fileParser.parseFile("ExecutionQueueOnSave.json");
			//DataObject dataObject = fileParser.parseFile("OrgLevelUnits.json");
			//DataObject dataObject = fileParser.parseFile("QuadraticEquationSolver.json");

			ArrayList<Parameter> parametersList = fileParser.parseParameters(dataObject);
			ArrayList<ExpectedResult> expectedResultsList = fileParser.parseExpectedResults(dataObject);
			ArrayList<String> variablesInExpectedResults = fileParser.compareVariable(parametersList,expectedResultsList);
			System.out.println(variablesInExpectedResults);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
