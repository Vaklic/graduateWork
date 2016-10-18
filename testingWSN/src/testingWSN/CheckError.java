package testingWSN;


public class CheckError {
	TestPackageDataArray checkArray;
	
	public CheckError() {
		checkArray = new TestPackageDataArray();
	}
	
	public CheckError(TestPackageDataArray _out) {
		checkArray = new TestPackageDataArray();
		checkArray.insertOtherArr(_out);
	}
	
	public void getArray(TestPackageDataArray _out) {
		checkArray.insertOtherArr(_out);
	}
	
	public void formArray(TestPackageData _outStruct[], int _length) {
		checkArray.insertArrOfOutData(_outStruct, _length);
	}
	
	
	public String checkSequenseNumberFullResult() {
		String _retError = "";
		
		for(int ix = 1; ix < checkArray.length - 1; ++ix) {
			if((checkArray.outStruct[ix+1].seq != checkArray.outStruct[ix].seq + 1) || 
			(checkArray.outStruct[ix-1].seq != checkArray.outStruct[ix].seq - 1)) {
				_retError += (checkArray.outStruct[ix-1].num + "\t" 
						   + checkArray.outStruct[ix-1].seq + "\n" 
						   + checkArray.outStruct[ix].num + "\t"
						   + checkArray.outStruct[ix].seq + "\n"
						   + checkArray.outStruct[ix+1].num + "\t"
						   + checkArray.outStruct[ix+1].seq + "\n"
						   + "----------------------------------------------\n");
			}
		}
		
		return _retError;
	}
	
	
	public String checkSequenseNumberShortResult(){
		String _retError = "Нет ошибок\n";
		int packWithSeqErr = 0;
		int biggestPack = 0;
		int smallestPack;
		
		smallestPack = checkArray.outStruct[0].seq;
		
		for(int ix = 1; ix < checkArray.length - 1; ++ix) {
			if((checkArray.outStruct[ix+1].seq != checkArray.outStruct[ix].seq + 1) &&
			   (checkArray.outStruct[ix-1].seq != checkArray.outStruct[ix].seq - 1)) {
				++ packWithSeqErr;
			}
			if(biggestPack < checkArray.outStruct[ix-1].seq) biggestPack = checkArray.outStruct[ix-1].seq;
			if(smallestPack > checkArray.outStruct[ix-1].seq) smallestPack = checkArray.outStruct[ix-1].seq;
			if(biggestPack < checkArray.outStruct[ix].seq) biggestPack = checkArray.outStruct[ix].seq;
			if(smallestPack > checkArray.outStruct[ix].seq) smallestPack = checkArray.outStruct[ix].seq;
			if(biggestPack < checkArray.outStruct[ix+1].seq) biggestPack = checkArray.outStruct[ix+1].seq;
			if(smallestPack > checkArray.outStruct[ix+1].seq) smallestPack = checkArray.outStruct[ix+1].seq;
		}
		
		double persentRecivedPack = (double)(checkArray.length) / (double)biggestPack * 100;
		//System.out.println("\n" + biggestPack + "\t" + packWithSeqErr + "\t" + checkArray.length +"\t" + persentRecivedPack + "\n");
		double persentRecivedPackFromFirstPackage = (double)(checkArray.length-1) / (double)(biggestPack - smallestPack) * 100;
		if(packWithSeqErr != 0 || (int)persentRecivedPack < 100){
			_retError = "Количество пакетов, принятых с нарушением порядка доставки: " + packWithSeqErr
					  + ".\nПроцент доставленных пакетов, относительно пакета с самым высоким порядковым номером\nдоставки: " 
					  + (int)persentRecivedPack + "%"
					  + "\nПроцент доставленных пакетов, относительно кол-ва пакетов, пришедших, начиная с первого\nдоставленного пакета и заканчивая последним: "
					  + (int)persentRecivedPackFromFirstPackage + "%";
		}
		
		return _retError;
	}
	
	
	public String checkPERFullResult() {
		String _retError = "";
	
		for(int ix = 0; ix < checkArray.length; ++ix) {
			if(checkArray.outStruct[ix].per > 0 && checkArray.outStruct[ix].err > 0){
				_retError += (checkArray.outStruct[ix].num + "\t" 
						    + checkArray.outStruct[ix].seq + "\t" 
						    + checkArray.outStruct[ix].per + "\t"
						    + checkArray.outStruct[ix].err +"\n");
			}
		}
		
		return _retError;
	}
	
	
	public String checkPERShortResult() {
		String _retError = "Нет ошибок\n";
		int countHighBER = 0,
			countMidBER = 0,
			countLowBER = 0,
			countLowerBER = 0;
		
		for(int ix = 0; ix < checkArray.length; ++ix) {
			if(checkArray.outStruct[ix].err >= 40){
				++ countHighBER;
			} else
			if(checkArray.outStruct[ix].err < 40 && checkArray.outStruct[ix].err >= 15){
				++ countMidBER;
			} else
			if(checkArray.outStruct[ix].err < 15 && checkArray.outStruct[ix].err > 5){
				++ countLowBER;
			} else
			if(checkArray.outStruct[ix].err < 5 && checkArray.outStruct[ix].err != 0){
				++ countLowerBER;
			}
		}
		
		if(countHighBER != 0 || countMidBER != 0 ||  countLowBER != 0 || countLowerBER != 0){
			_retError = "Большое количество ошибок в " + countHighBER + " пакетах\n" +
						"Среднее количество ошибок в " + countMidBER + " пакетах\n" +
						"Малое количество ошибок в " + countLowBER + " пакетах\n" +
						"Незначительное количество ошибок в " + countLowerBER + " пакетах\n\n" +
						"Процент пакетов поступивших с ошибкой: " + checkArray.outStruct[checkArray.length-1].per + "%\n";
		}
		
		return _retError;
	}
	
	
	public String checkRSSIFullResult() {
		String _retError = "";
		
		for(int ix = 0; ix < checkArray.length; ++ix) {
			if(checkArray.outStruct[ix].rssi >= 55 || checkArray.outStruct[ix].lqi < 255) {
				_retError += (checkArray.outStruct[ix].num + "\t" 
						    + checkArray.outStruct[ix].seq + "\t" 
						    + checkArray.outStruct[ix].rssi + "\t"
						    + checkArray.outStruct[ix].lqi + "\n");
			}
		}
		
		return _retError;
	}
	
	
	public String checkRSSIShortResult() {
		String _retError = "Нет ошибок\n";
		double rssiMidValue = 0,
			   lqiMidValue = 0;
		
		for(int ix = 0; ix < checkArray.length; ++ix) {
			rssiMidValue += checkArray.outStruct[ix].rssi;
			lqiMidValue += checkArray.outStruct[ix].lqi;
		}
		
		int convertRSSI = (int)(rssiMidValue/checkArray.length);
		int convertLQI = (int)(lqiMidValue/checkArray.length);
		
		if(convertRSSI <= 50 && convertLQI == 255) {
			_retError = "Нет ошибок\n";
		} else if(convertRSSI >= 60 && convertLQI <= 160) {
			_retError = "Слабый сигнал с высоким уровнем 'зашумления' диапазона!\n"
					  + "Среднее значение показателя мощности сигнала: " + convertRSSI  + " dBm"
					  + "\nСреднее значение показателя качества сигнала: " + convertLQI + "\n";
		} else if(convertRSSI >= 60 && convertLQI >= 160) {
			_retError = "Слабый сигнал с низким уровнем 'зашумления' диапазона!\n"
					  + "Среднее значение показателя мощности сигнала: " + convertRSSI  + " dBm"
					  + "\nСреднее значение показателя качества сигнала: " + convertLQI + "\n";
		} else if(convertRSSI <= 60 && convertLQI <= 160) {
			_retError = "Сильный сигнал с высоким уровнем 'зашумления' диапазона!\n"
					  + "Среднее значение показателя мощности сигнала: " + convertRSSI  + " dBm"
					  + "\nСреднее значение показателя качества сигнала: " + convertLQI + "\n";
		} else if(convertRSSI <= 60 && convertLQI >= 160) {
			_retError = "Сильный сигнал с низким уровнем 'зашумления' диапазона!\n"
					  + "Среднее значение показателя мощности сигнала: " + convertRSSI + " dBm" 
					  + "\nСреднее значение показателя качества сигнала: " + convertLQI + "\n";
		}
		
		return _retError;
	}
	
	public String retFullResult() {	
		return ("num\toflo\tseq\tper\terr\tlqi\trssi\ted\tgain\tstatus\ttime\tfp\tlength\n" + checkArray.retArrayData());
	}
}
