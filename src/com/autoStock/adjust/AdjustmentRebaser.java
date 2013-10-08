package com.autoStock.adjust;

import java.util.ArrayList;
import java.util.List;

import com.autoStock.Co;
import com.autoStock.backtest.BacktestContainer;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalRangeLimit;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentRebaser {
	private AdjustmentCampaign adjustmentCampaign;
	private BacktestContainer backtestContainer;
	
	public AdjustmentRebaser(AdjustmentCampaign adjustmentCampaign, BacktestContainer backtestContainer) {
		this.adjustmentCampaign = adjustmentCampaign;
		this.backtestContainer = backtestContainer;
	}

	public void rebase(){	
		Co.println("--> Rebasing");
		for (AdjustmentBase adjustmentBase : adjustmentCampaign.listOfAdjustmentBase){
			if (adjustmentBase instanceof AdjustmentOfSignalMetric){
				AdjustmentOfSignalMetric adjustmentOfSignalMetric = (AdjustmentOfSignalMetric) adjustmentBase;
				

				for (SignalBase signalBase : backtestContainer.hashOfSignalRangeLimit.keySet()){
					if (adjustmentOfSignalMetric.signalBase.getClass() == signalBase.getClass()){
						IterableOfInteger iterableOfInteger = ((IterableOfInteger)adjustmentOfSignalMetric.iterableBase);
						
						Co.println("--> Rebasing adjustment: " + adjustmentOfSignalMetric.description);
						
//						if (iterableOfInteger.rebaseRequired){
							int min = backtestContainer.hashOfSignalRangeLimit.get(signalBase).getMin();
							int max = backtestContainer.hashOfSignalRangeLimit.get(signalBase).getMax();
						
							iterableOfInteger.rebase(min, max);
						
							Co.println("--> " + adjustmentOfSignalMetric.description + ", " + min + ", " + max + " rebase adjusted to: " + iterableOfInteger.getMin() + ", " + iterableOfInteger.getMax());
//						}
					}
				}
			}
		}
		
		adjustmentCampaign.rebased();
		
//		throw new IllegalStateException();
	}
	
	public static SignalRangeLimit getRangeLimit(List<SignalRangeLimit> list){
		SignalRangeLimit signalRangeLimit = new SignalRangeLimit();
		
		for (SignalRangeLimit limit : list){
			signalRangeLimit.addValue(limit.getMin());
			signalRangeLimit.addValue(limit.getMax());
		}
		return signalRangeLimit;
	}
}
