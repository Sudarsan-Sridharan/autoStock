/**
 * 
 */
package com.autoStock;

import java.util.Date;

import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.RequestMarketScanner;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.request.listener.RequestMarketScannerListener;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultRowMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;
import com.autoStock.position.PositionManager;
import com.autoStock.trading.types.MarketData;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainCampaign {
	
	private ExResultSetMarketScanner exResultSetMarketScanner;
	
	public void init(){
//		while (true){
//			//Co.println("Waiting for date... " + getDate().getTime() + ", " + getDate().getHours());
//			if (getDate().getHours() >= 9 && getDate().getMinutes() >= 35){
//				break;
//			}
//			try {Thread.sleep(1000);}catch(InterruptedException e){return;}
//		}
		
		dayStart();
	}
	
	public void dayStart(){
		new RequestMarketScanner(new RequestHolder(new RequestMarketScannerListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketScanner exResultSetMarketScanner) {
				ExchangeController.getIbExchangeInstance().ibExchangeClientSocket.eClientSocket.cancelScannerSubscription(requestHolder.requestId);
				for (ExResultRowMarketScanner result : exResultSetMarketScanner.listOfExResultRowMarketScanner){
					Co.println("Should run algorithm for symbol: " + result.symbol);
				}
				
				MainCampaign.this.exResultSetMarketScanner = exResultSetMarketScanner;
				handleCompletedMarketScanner();
			}
		}));
	}
	
	public void handleCompletedMarketScanner(){
		for (ExResultRowMarketScanner result : exResultSetMarketScanner.listOfExResultRowMarketScanner){
			Co.println("Should run algorithm for symbol: " + result.symbol);
			new RequestMarketData(new RequestHolder(null), new RequestMarketDataListener() {
				@Override
				public void receiveQuoteSlice(RequestHolder requestHolder, QuoteSlice typeQuoteSlice) {
					Co.println("Received quote slice: " + typeQuoteSlice.symbol + ", " + typeQuoteSlice.priceClose);
				}
				
				@Override
				public void failed(RequestHolder requestHolder) {
					
				}
				
				@Override
				public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData) {
					
				}
			}, new MarketData(result.symbol, "STK"), 5000);
		}
	}
	
	public void dayEnd(){
		Co.println("End of day reached, sell all...");
		PositionManager.instance.executeSellAll();
	}
	
	public Date getDate(){
		return new Date();
	}
}
