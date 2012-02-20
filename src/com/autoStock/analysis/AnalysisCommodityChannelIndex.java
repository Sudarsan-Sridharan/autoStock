/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.Co;
import com.autoStock.analysis.results.ResultsBollingerBands;
import com.autoStock.analysis.results.ResultsCommodityChannelIndex;
import com.autoStock.analysis.tools.DataExtractor;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisCommodityChannelIndex extends AnalysisBase {
	public ResultsCommodityChannelIndex results;
	
	public ResultsCommodityChannelIndex analyize(){
		super.initializeTypicalAnalys(128, ((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		
		results = new ResultsCommodityChannelIndex(datasetLength+periodLength);
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));

		valuesPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceOpen").toArray(new Float[0]));
		valuesPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		valuesPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceLow").toArray(new Float[0]));
		valuesPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
	
		preceedDataSetWithPeriod();
		
		RetCode returnCode = getTaLibCore().cci(periodLength+1, periodLength+datasetLength-1, valuesPriceHigh, valuesPriceLow, valuesPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
