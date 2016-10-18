package testingWSN;

import jssc.SerialPort;
import jssc.SerialPortException;

public class ListenSerialThread extends Thread {
	String _port;
	String _msg;

	public void run(){
		sendCommand(_port, "rx");
		listenSerial();
		try{
            Thread.sleep(300);		
        }catch(InterruptedException e){
            return;	
        }
	}
	
	public ListenSerialThread() {
		_msg = "";
		_port = "/dev/ttyUSB0";
	}
	
	public ListenSerialThread(String port) {//, int numOfPack) {
		_msg = "";
		_port = port;
	}
	
	public String getMsg() { return _msg; }
	
	public void setPort(String port) { _port = port; }
	
	public static void sendCommand(String port,String msg) {
		SerialPort serialPort = new SerialPort(port);
	    try {
	        serialPort.openPort();
	        serialPort.writeString(msg);
	        serialPort.writeInt(0x0D);
	        serialPort.closePort();
	    }
	    catch (SerialPortException ex) {
	        System.out.println(ex);
	    }
	}
	
	public void listenSerial() {
		String buffer = null;
	    
	    SerialPort serialPort = new SerialPort(_port);
	    
	    try {
	    	serialPort.openPort();
	    	serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	    	
	    	while(true){
	    		if(this.isInterrupted()) break;
	    		buffer = serialPort.readString();
	    		if(buffer != null){
	    			_msg += buffer;
	    		}
	    		Thread.sleep(50);
	        } 
	    }
	    catch (SerialPortException ex) {
	    	System.out.println(ex);
	    } catch(InterruptedException e) {
	    	System.out.println(e);
	    };
	}
}
