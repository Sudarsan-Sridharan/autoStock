/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCCI{
	private SignalMetricType signalMetricType = SignalMetricType.metric_cci;
	private ArrayList<Double> listOfDouble = new ArrayList<Double>();
	
	public void addInput(double cciValue){
		listOfDouble.add(new Double(cciValue));
	}
	
	public ArrayList<Double> getListOfValue(){
		return listOfDouble;
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(listOfDouble.get(listOfDouble.size()-1)), signalMetricType);
	}
}
