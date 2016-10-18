package testingWSN;

public class TestPackageData {
	public int num; 		// numberOfRecivedPackage
	public int oflo; 		// numberOfDetectOverflow
	public int seq; 		// packSeqNumber
	public int per; 		// packErrorRate
	public int err; 		// numberOfCorrelatedPack
	public int lqi; 		// linkQualityIndic
	public int rssi; 		// signalStrengthIndicator
	public int ed; 			// edSigStren
	public int gain; 		// rxGainSettings
	public int status; 		// status
	public int time; 		// time
	public int fp; 			// framePendingStatus
	public int length; 		// length
	
	
	public TestPackageData() {
		this.num = 0;	
		this.oflo = 0;
		this.seq = 0;
		this.per = 0;
		this.err = 0;
		this.lqi = 0;
		this.rssi = 0;
		this.ed = 0;
		this.gain = 0;
		this.status = 0;
		this.time = 0;
		this.fp = 0;
		this.length = 0;
	}
	
	public void copyData(int[] data) {
		this.num = data[0];	
		this.oflo = data[1];
		this.seq = data[2];
		this.per = data[3];
		this.err = data[4];
		this.lqi = data[5];
		this.rssi = data[6];
		this.ed = data[7];
		this.gain = data[8];
		this.status = data[9];
		this.time = data[10];
		this.fp = data[11];
		this.length = data[12];
	}
	
	public int[] getIntArrOfData() {
		int[] data = new int[13];
		
		data[0] = this.num ;	
		data[1] = this.oflo;
		data[2] = this.seq;
		data[3] = this.per;
		data[4] = this.err;
		data[5] = this.lqi;
		data[6] = this.rssi;
		data[7] = this.ed;
		data[8] = this.gain;
		data[9] = this.status;
		data[10] = this.time;
		data[11] = this.fp;
		data[12] = this.length;
		
		return data;
	}
	
	public void insertData(String[] data){
		this.num = Integer.valueOf(data[0]);
		this.oflo = Integer.valueOf(data[1]);
		this.seq = Integer.valueOf(data[2]);
		this.per = Integer.valueOf(data[3]);
		this.err = Integer.valueOf(data[4]);
		this.lqi = changeHex(data[5]);
		this.rssi = (-1) * Integer.valueOf(data[6]);
		this.ed = changeHex(data[7]);
		this.gain = changeHex(data[8]);
		this.status = changeHex(data[9]);
		this.time = changeHex(data[10]);
		this.fp = Integer.valueOf(data[11]);
		this.length = changeHex(data[12]);
	}
	
	public void printVariables() {
		System.out.print(this.num + "\t");
		System.out.print(this.oflo + "\t");
		System.out.print(this.seq + "\t");
		System.out.print(this.per + "\t");
		System.out.print(this.err + "\t");
		System.out.print(this.lqi + "\t");
		System.out.print(this.rssi + "\t");
		System.out.print(this.ed + "\t");
		System.out.print(this.gain + "\t");
		System.out.print(this.status + "\t");
		System.out.print(this.time + "\t");
		System.out.print(this.fp + "\t");
		System.out.print(this.length + "\t");
		System.out.println();
	}
	
	public String retPackageData()
	{
		String retMsg = "";
		
		retMsg += this.num + "\t";
		retMsg += this.oflo + "\t";
		retMsg += this.seq + "\t";
		retMsg += this.per + "\t";
		retMsg += this.err + "\t";
		retMsg += this.lqi + "\t";
		retMsg += this.rssi + "\t";
		retMsg += this.ed + "\t";
		retMsg += this.gain + "\t";
		retMsg += this.status + "\t";
		retMsg += this.time + "\t";
		retMsg += this.fp + "\t";
		retMsg += this.length;
		
		return retMsg;
	}
	
	public int changeHex(String hex){
		String strHex;
		int retHex;
		
		int findX = hex.indexOf("0x");
		strHex = hex.substring(findX+2);
		retHex = Integer.parseInt(strHex, 16);

		return retHex;
	}
}
