/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsADX;
import com.autoStock.indicator.results.ResultsAR;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfAR extends IndicatorBase {
	public ResultsAR results;
	
	public IndicatorOfAR(ImmutableInteger periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsAR analyize(){
		results = new ResultsAR(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.aroon(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, periodLength.value/2, new MInteger(), new MInteger(), results.arrayOfARUp, results.arrayOfARDown);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}