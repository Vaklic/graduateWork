package testingWSN;


public class TestPackageDataArray {
	TestPackageData outStruct[];
	int length;
	
	public TestPackageDataArray() {
		this.length = 0;
	}
	
	public void setLength(int _length) {
		this.length = _length;
	}
	
	public void TestPackageDataArray(TestPackageData _outStruct[], int _length) {
		this.length = _length;
		this.outStruct = new TestPackageData[this.length];
		
		for(int ix = 0; ix < this.length; ++ix) {
			this.outStruct[ix].copyData(_outStruct[ix].getIntArrOfData());
			++ix;
		}
	}
	
	public void TestPackageDataArray(TestPackageDataArray _out){
		this.length = _out.length;
		this.outStruct = new TestPackageData[this.length];
		
		for(int ix = 0; ix < this.length; ++ix) {
			this.outStruct[ix].copyData(_out.outStruct[ix].getIntArrOfData());
			++ix;
		}
	}
	
	public void insertArrOfOutData(TestPackageData _outStruct[], int _length) {
		this.length = _length;
		this.outStruct = new TestPackageData[this.length];
		
		System.out.println(this.length);
		for(int ix = 0; ix < this.length; ++ix) {
			this.outStruct[ix] = new TestPackageData();
			
			this.outStruct[ix].copyData(_outStruct[ix].getIntArrOfData());
		}
	}
	
	public void insertOtherArr(TestPackageDataArray _out){
		this.length = _out.length;
		this.outStruct = new TestPackageData[this.length];

		System.out.println(this.length);
		for(int ix = 0; ix < this.length; ++ix) {
			this.outStruct[ix] = new TestPackageData();
			this.outStruct[ix].copyData(_out.outStruct[ix].getIntArrOfData());
		}
	}
	
	public void printData(){
		System.out.println("num\toflo\tseq\tper\terr\tlqi\trssi\ted\tgain\tstatus\ttime\tfp\tlength");
		
		for(int ix = 0; ix < this.length; ++ix) {
			this.outStruct[ix].printVariables();
		}
	}
	
	public String retArrayData(){
		String retMsg = "";
		
		for(int ix = 0; ix < this.length; ++ix) {
			retMsg += this.outStruct[ix].retPackageData();
			retMsg += "\n";
		}
		
		return retMsg;
	}
	
	public void insertToArr(TestPackageData _out){
		TestPackageData _outData[];
		this.length = _out.length+1;
		
		if(this.length > 1) {
			_outData = new TestPackageData[this.length];
			
			for(int ix = 0; ix < this.length-1; ++ix) {
				_outData[ix].copyData(this.outStruct[ix].getIntArrOfData());
			}
			_outData[this.length-1] = _out;
			 
			this.outStruct = new TestPackageData[this.length];
			
			for(int ix = 0; ix < this.length; ++ix) {
				this.outStruct[ix].copyData(_outData[ix].getIntArrOfData());
			}
		}
		this.outStruct = new TestPackageData[length];
		
		this.outStruct[this.length-1] = _out;
	}
}
