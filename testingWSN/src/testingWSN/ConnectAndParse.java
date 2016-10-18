package testingWSN;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jssc.SerialPort;
import jssc.SerialPortException;

public class ConnectAndParse {	
	public static TestPackageDataArray parseMsgFromRx(String msg){
		String buffer, bufferStr;
		TestPackageData retStruct[];
		int ix = 0;
		
		int lastInputNumber;
		lastInputNumber = msg.indexOf("length");
		System.out.println(msg);
		buffer = msg.substring(lastInputNumber+9);
		
		int numberStrings = getNumberOfStrings(buffer)-1;
		retStruct = new TestPackageData[numberStrings];
		
		buffer = removeSpaces(buffer);		
		
		lastInputNumber = buffer.indexOf("}}");
		
		TestPackageDataArray retArr = new TestPackageDataArray();
		retArr.setLength(numberStrings);
		while(lastInputNumber != -1) {
			bufferStr =	buffer.substring(2,lastInputNumber+1);
			
			retStruct[ix] = getOutStruct(bufferStr);
			buffer = buffer.substring(lastInputNumber+3);
			
			lastInputNumber = buffer.indexOf("}}");
			
			++ix;
		}
		
		retArr.insertArrOfOutData(retStruct, numberStrings);
				
		retArr.printData();
		
		return retArr;
	}
	
	public static int getNumberOfStrings(String msg) {
		int retValue = 0;
		String buffer = msg;
		int lastInputNumber = 0;
		
		while(lastInputNumber != -1) {
			lastInputNumber = buffer.indexOf("\n");
			++retValue;
			buffer = buffer.substring(lastInputNumber+1);
		}
		return retValue-1;
	}
	
	public static TestPackageData getOutStruct(String str) {
		int ix = 0;
		String buffer = str, bufStr = "";
		int firstNumber = 0, lastNumber = 0;
		
		String data[] = new String[13];
		
		while(ix < 13) {	
			firstNumber = buffer.indexOf("{");
			lastNumber = buffer.indexOf("}");
			bufStr = buffer.substring(firstNumber+1, lastNumber);
			buffer = buffer.substring(lastNumber+1);
			data[ix] = bufStr;
			++ix;
		}
		
		TestPackageData retStruct = new TestPackageData();
		retStruct.insertData(data);
		
		return retStruct;
	}
	
	public static String removeSpaces(String text){
		String retData = text;
		int spaceChar = 0;
		
		while(true){
			spaceChar = retData.indexOf(" ");
			if(spaceChar == -1) break;
			retData = retData.substring(0, spaceChar) + retData.substring(spaceChar+1);
		}
		
		return retData;
	}
	
	public static String getExampleString(String path) {
		String retMsg = "";
		Path file = Paths.get(path);
		
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        retMsg += line;
		        retMsg += "\n";
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		return retMsg;
	}
}
