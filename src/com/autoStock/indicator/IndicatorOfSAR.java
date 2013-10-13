/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.indicator.results.ResultsSAR;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.MathTools;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfSAR extends IndicatorBase {
	public ResultsSAR results;
	
	public IndicatorOfSAR(MutableInteger periodLength, int resultLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsSAR analyize(){
		results = new ResultsSAR(periodLength.value - 2);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.sar(0, endIndex, commonAnlaysisData.arrayOfPriceHigh, commonAnlaysisData.arrayOfPriceLow, 0.01, 0.20, new MInteger(), new MInteger(), results.arrayOfSAR); 
				
				//taLibCore.cci(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength.value, new MInteger(), new MInteger(), results.arrayOfCCI);
		
//		Co.println("--> Last result: " + results.arrayOfSAR[27]);
	
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
