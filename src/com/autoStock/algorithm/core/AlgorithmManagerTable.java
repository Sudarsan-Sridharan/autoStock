package com.autoStock.algorithm.core;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionManager;
import com.autoStock.position.PositionValue;
import com.autoStock.signal.SignalMetric;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmManagerTable {
	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	
	public void addRow(AlgorithmTest algorithm, ArrayList<QuoteSlice> listOfQuoteSlice){
		ArrayList<String> columnValues = new ArrayList<String>();
		
		Position position = PositionManager.getInstance().getPosition(algorithm.symbol);
		PositionValue positionValue = position == null ? null : position.getPositionValue();
		
		double percentGainFromAlgorithm = 0;
		double percentGainFromPosition = 0;
		
		if (algorithm.firstQuoteSlice != null && algorithm.getCurrentQuoteSlice() != null){
			if (algorithm.firstQuoteSlice.priceClose != 0 && algorithm.getCurrentQuoteSlice().priceClose != 0){
				percentGainFromAlgorithm = ((algorithm.getCurrentQuoteSlice().priceClose / algorithm.firstQuoteSlice.priceClose) -1) * 100;
				if (Double.isNaN(percentGainFromAlgorithm)){
					percentGainFromAlgorithm = 0;
				}
			}
		}
		
		if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
			if (positionValue.unitPriceCurrent != 0 && positionValue.unitPriceFilled != 0){
				percentGainFromPosition = position.getCurrentPercentGainLoss(false);
				if (Double.isNaN(percentGainFromPosition)){
					percentGainFromPosition = 0;
				}
			}
		}
		
		columnValues.add(algorithm.getCurrentQuoteSlice() != null && algorithm.getCurrentQuoteSlice().dateTime != null ? DateTools.getPrettyDate(algorithm.getCurrentQuoteSlice().dateTime) : "?"); 
		columnValues.add(algorithm.symbol.symbolName);
		columnValues.add(algorithm.strategy.lastStrategyResponse == null ? "-" : (algorithm.strategy.lastStrategyResponse.positionGovernorResponse.signalPoint.signalPointType.name() + ", " + algorithm.strategy.lastStrategyResponse.positionGovernorResponse.signalPoint.signalMetricType.name()));
		columnValues.add(algorithm.strategy.currentStrategyResponse == null ? "-" : (algorithm.strategy.currentStrategyResponse.strategyActionCause.name()));
		columnValues.add(position == null ? "-" : position.positionType.name());
		columnValues.add(String.valueOf(algorithm.getFirstQuoteSlice() == null ? 0 : MathTools.round(algorithm.getFirstQuoteSlice().priceClose)));
		columnValues.add(String.valueOf(position == null ? "-" : positionValue.unitPriceIntrinsic));
		columnValues.add(String.valueOf(algorithm.getCurrentQuoteSlice() == null ? 0 : MathTools.round(algorithm.getCurrentQuoteSlice().priceClose)));
		columnValues.add(String.valueOf(new DecimalFormat("#.###").format(percentGainFromAlgorithm)));
		columnValues.add(String.valueOf(new DecimalFormat("#.###").format(percentGainFromPosition)));
		columnValues.add(String.valueOf(position == null ? "-" : ("P&L: " + StringTools.addPlusToPositiveNumbers(position.getPositionProfitLossBeforeComission()) + " / " + StringTools.addPlusToPositiveNumbers(position.getPositionProfitLossAfterComission(false)))));
		//columnValues.add(String.valueOf(position == null ? "-" : (position.getFirstKnownUnitPrice() + ", " +  position.getLastKnownUnitPrice() + ", " + position.getPositionValue().valueCurrent + ", " + position.getPositionValue().valueIntrinsic + ", " + position.getPositionValue().unitPriceFilled + ", " + position.positionUtils.getOrderUnitsFilled() + ", " + + position.positionUtils.getOrderUnitsIntrinsic())));
		
		columnValues.add(String.valueOf(algorithm.algorithmState.isDisabled));
		
		String stringForSignalMetrics = new String();
		
		if (algorithm.strategy.signal != null){
			for (SignalMetric signalMetric : algorithm.strategy.signal.getListOfSignalMetric()){
				//signalMetrics += " (" + signalMetric.signalMetricType.name() + ":" + signalMetric.strength + ":" + signalMetric.getSignalPoint(position == null ? false : true, position == null ? PositionType.position_none : position.positionType).signalPointType.name() + ")";
				stringForSignalMetrics = "Replace this";
			}
		}else{
			stringForSignalMetrics = "?";
		}
		
		columnValues.add(stringForSignalMetrics);
		
		listOfDisplayRows.add(columnValues);
	}
	
	public ArrayList<ArrayList<String>> getListOfDisplayRows(){
		return listOfDisplayRows;
	}

	public void clear() {
		listOfDisplayRows.clear();
	}
}
