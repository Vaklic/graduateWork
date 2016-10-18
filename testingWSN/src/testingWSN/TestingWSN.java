package testingWSN;

public class TestingWSN extends Thread {
	private String serialPort; 
	private String mode;
	private int numOfPackage;
	private String testMode;	
	private String typeOfCheckError;
	
	private String retMsg;
	private String bufMsg;
	
	private boolean isParametersRecived;
	private boolean checkEnd;
	
	private boolean stopWork;

	public void run(){
		try {
			analizeCommingData();
		} catch(NullPointerException e) {
			this.checkEnd = true;
			e.printStackTrace();
		} catch(StringIndexOutOfBoundsException e) {
			this.checkEnd = true;
			this.retMsg = "Ошибка чтения с Serial Port";
			e.printStackTrace();
		}
		
		try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            return;
        }
	}
	
	public TestingWSN() {
		this.retMsg = "Данные не обработались";
		this.serialPort = "";
		this.mode = "";
		this.numOfPackage = -1;
		this.isParametersRecived = false;
		this.typeOfCheckError = "";
		this.testMode = "";
		this.checkEnd = false;
		this.stopWork = false;
	}
	
	public TestingWSN(String _serialPort, String _mode, String _numOfPackage, String _typeOfErrorCheck, String _testMode) {
		this.retMsg = "Данные не обработались";
		this.serialPort = _serialPort;
		this.mode = _mode;
		
		if(this.mode == "TX") 
			this.numOfPackage = Integer.parseInt(_numOfPackage);	
		else if(this.mode == "RX") this.numOfPackage = 0;
		
		this.testMode = _testMode; 
		this.isParametersRecived = true;
		this.typeOfCheckError = _typeOfErrorCheck;  
		this.checkEnd = false;
		this.stopWork = false;
	}
	
	public void stopWork() {
		this.stopWork = true;
	}
	
	public boolean getCheckEndLabel() { return checkEnd; }
	
	public void setParameters(String _serialPort, String _mode, String _numOfPackage, String _typeOfErrorCheck, String _testMode) {
		this.serialPort = _serialPort;
		this.mode = _mode;
		
		if(this.mode == "TX") 
			this.numOfPackage = Integer.parseInt(_numOfPackage);	
		else if(this.mode == "RX") this.numOfPackage = 0;
		
		this.isParametersRecived = true;
		this.typeOfCheckError = _typeOfErrorCheck;
		this.testMode = _testMode; 
	}
	
	public void analizeCommingData() {
		if(this.isParametersRecived){
			if(this.mode == "RX") {
				startRXmodeOnDevice();
				ListenSerialThread.sendCommand(this.serialPort, "e");
				
				if(this.bufMsg != "") {
					TestPackageDataArray parseValues = new TestPackageDataArray();
					parseValues.insertOtherArr(ConnectAndParse.parseMsgFromRx(this.bufMsg));
			
					if(parseValues.length <= 0) {
						this.retMsg = "Ошибка при распозновании полученных данных";
						return;
					}
				
					CheckError checkArr = new CheckError(parseValues);
					if(typeOfCheckError == "FULL") {
						this.retMsg = checkArr.retFullResult();
					} else if(typeOfCheckError == "SHORT") {
						if(testMode == "ALL") {
							this.retMsg = "Проверка на PER\n-----------------\n\n" + checkArr.checkPERShortResult() +
									      "\nПроверка на RSSI\n-----------------\n\n" + checkArr.checkRSSIShortResult() +
									      "\nПроверка на SEQ\n-----------------\n\n" + checkArr.checkSequenseNumberShortResult();
						} else if (testMode == "RSSI") {
							this.retMsg = "\nПроверка на RSSI\n-----------------\n\n" + checkArr.checkRSSIShortResult();
						} else if (testMode == "PER") {
							this.retMsg = "Проверка на PER\n-----------------\n\n" + checkArr.checkPERShortResult();
						} else if (testMode == "SEQ") {
							this.retMsg = "\nПроверка на SEQ\n-----------------\n\n" + checkArr.checkSequenseNumberShortResult();
						} else {
							this.retMsg = "Неудача при анализе полученных данных";
						}
					} else {
						this.retMsg = "Неизвестный формат вывода";
					}
				} else {
					this.retMsg = "Данные не поступали";
				}
			} else if(this.mode == "TX") {
				ListenSerialThread.sendCommand(this.serialPort, "tx 0x" + Integer.toHexString(this.numOfPackage));
				
				try{
					 Thread.sleep(this.numOfPackage / 30 * 1000);
				} catch(InterruptedException e) {};
				this.retMsg = "Пакеты отправлены";
			} else this.retMsg = "Некорректный режим работы модуля!";
		}
		this.checkEnd = true;
	}
	
	public String getMsg() { return this.retMsg; }
	
	public void startRXmodeOnDevice() {
		ListenSerialThread newPort = new ListenSerialThread(this.serialPort);
		Thread threadForListen = new Thread(newPort);
		try{
            Thread.sleep(100);
        }catch(InterruptedException e) {}
		threadForListen.start();
		
		while(true) {
			if(this.stopWork){
				threadForListen.interrupt();
				this.bufMsg = newPort.getMsg();
				break;
			}
			try{
	            Thread.sleep(150);
	        }catch(InterruptedException e) {}
		}
		//System.out.println(this.bufMsg);
	}
	
	public static void startTestProcedure() {
		String Msg;
		ListenSerialThread newPort;
		int numOfPack = Integer.parseInt("512");
		
		
		newPort = new ListenSerialThread("/dev/ttyUSB3");//, numOfPack);
		try{
            Thread.sleep(100);
        }catch(InterruptedException e) {}
		newPort.start();
		
		try{
			 Thread.sleep(500);
		} catch(InterruptedException e) {};
		
		ListenSerialThread.sendCommand("/dev/ttyUSB1", "tx 0x" + Integer.toHexString(numOfPack));
		
		try{
			 Thread.sleep(numOfPack / 30 * 1000);
		} catch(InterruptedException e) {};
		newPort.interrupt();
		
		ListenSerialThread.sendCommand("/dev/ttyUSB3", "e");
		Msg = newPort.getMsg(); 
	
		System.out.println("---Stage1---");
		
		TestPackageDataArray parseValues = new TestPackageDataArray();
		parseValues.insertOtherArr(ConnectAndParse.parseMsgFromRx(Msg));
		parseValues.printData();
		
		CheckError checkArr = new CheckError(parseValues);
		
		System.out.println("\n---Проверка полученных результатов на наличие ошибок---\n");
		
		System.out.println("Проверка на PER\n");
		System.out.println(checkArr.checkPERShortResult());
		
		System.out.println("Проверка на RSSI\n");
		System.out.println(checkArr.checkRSSIShortResult());
		
		System.out.println("Проверка на SEQ\n");
		System.out.println(checkArr.checkSequenseNumberShortResult());
		

	}
}
