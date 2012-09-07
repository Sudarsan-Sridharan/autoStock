/**
 * 
 */
package com.autoStock.trading.platform.ib;

import java.text.SimpleDateFormat;

import com.autoStock.exchange.ExchangeHelper.ExchangeDesignation;
import com.autoStock.exchange.request.RequestMarketScanner.MarketScannerType;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.internal.Config;
import com.autoStock.trading.platform.ib.core.Contract;
import com.autoStock.trading.platform.ib.core.EClientSocket;
import com.autoStock.trading.platform.ib.core.Order;
import com.autoStock.trading.platform.ib.core.ScannerSubscription;
import com.autoStock.trading.platform.ib.subset.SubsetOfScannerSubscription;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.Position;
import com.autoStock.trading.types.RealtimeData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class IbExchangeInstance {
	public IbExchangeWrapper ibExchangeWrapper;
	public IbExchangeClientSocket ibExchangeClientSocket;
	
	public void init(){
		ibExchangeWrapper = new IbExchangeWrapper();
		ibExchangeClientSocket = new IbExchangeClientSocket();
		
		try {
			ibExchangeClientSocket.init(ibExchangeWrapper);
			ibExchangeClientSocket.connect();
		}catch(Exception e){} //e.printStackTrace();
	}
	
	public EClientSocket getEclientSocket(){
		return ibExchangeClientSocket.eClientSocket;
	}
	
	public void getAccountUpdates(){
		ibExchangeClientSocket.eClientSocket.reqAccountUpdates(true, Config.plIbUsername);
	}

	public void getOpenOrders(){
		ibExchangeClientSocket.eClientSocket.reqOpenOrders();
	}
	
	public void placeLongEntry(Position typePosition, RequestHolder requestHolder, Exchange exchange){
		Contract contract = new Contract();
		Order order = new Order();
		contract.m_exchange = exchange.name;
		contract.m_symbol = typePosition.symbol;
		contract.m_secType = "STK";
		contract.m_currency = exchange.currency.name();
		order.m_action = "BUY";
		order.m_orderType = "MKT";
		order.m_auxPrice = 0;
		order.m_totalQuantity = typePosition.units;
		
		ibExchangeClientSocket.eClientSocket.placeOrder(requestHolder.requestId, contract, order);
	}
	
	public void placeLongExit(Position typePosition, RequestHolder requestHolder, Exchange exchange){
		Contract contract = new Contract();
		Order order = new Order();
		contract.m_exchange = exchange.name;
		contract.m_symbol = typePosition.symbol;
		contract.m_secType = "STK";
		contract.m_currency = exchange.currency.name();
		order.m_action = "SELL";
		order.m_orderType = "MKT";
		order.m_auxPrice = 0;
		order.m_totalQuantity = typePosition.units;
		
		ibExchangeClientSocket.eClientSocket.placeOrder(requestHolder.requestId, contract, order);
	}
	
	public void placeShortEntry(Position typePosition, RequestHolder requestHolder, Exchange exchange){
		Contract contract = new Contract();
		Order order = new Order();
		contract.m_exchange = exchange.name;
		contract.m_symbol = typePosition.symbol;
		contract.m_secType = "STK";
		contract.m_currency = exchange.currency.name();
		order.m_action = "SELL";
		order.m_orderType = "MKT";
		order.m_auxPrice = 0;
		order.m_totalQuantity = typePosition.units;
		
		ibExchangeClientSocket.eClientSocket.placeOrder(requestHolder.requestId, contract, order);
	}
	
	public void placeShortExit(Position typePosition, RequestHolder requestHolder, Exchange exchange){
		Contract contract = new Contract();
		Order order = new Order();
		contract.m_exchange = exchange.name;
		contract.m_symbol = typePosition.symbol;
		contract.m_secType = "STK";
		contract.m_currency = exchange.currency.name();
		order.m_action = "BUY";
		order.m_orderType = "MKT";
		order.m_auxPrice = 0;
		order.m_totalQuantity = typePosition.units;
		
		ibExchangeClientSocket.eClientSocket.placeOrder(requestHolder.requestId, contract, order);
	}
	
	public void getScanner(RequestHolder requestHolder, Exchange exchange, MarketScannerType marketScannerType){
		ScannerSubscription scanner = new SubsetOfScannerSubscription().getScanner(exchange, marketScannerType);
		ibExchangeClientSocket.eClientSocket.reqScannerSubscription(requestHolder.requestId, scanner);
	}
	
	public void getRealtimeData(RealtimeData typeRealtimeData, RequestHolder requestHolder){
		//Co.println("Request id: " + requestHolder.requestId);
		Contract contract = new Contract();
		contract.m_exchange = "ASX";
		contract.m_symbol = typeRealtimeData.symbol;
		contract.m_secType = typeRealtimeData.securityType;
		contract.m_currency = "AUD";
		ibExchangeClientSocket.eClientSocket.reqRealTimeBars(requestHolder.requestId, contract, 5, "TRADES", false);
	}
	
	public void getMarketData(Exchange exchange, Symbol symbol, RequestHolder requestHolder){
		//Co.println("Request id: " + requestHolder.requestId);
		Contract contract = new Contract();
		contract.m_exchange = exchange.name;
		contract.m_symbol = symbol.symbolName;
		contract.m_secType = "STK";
		contract.m_currency = exchange.currency.name();
		contract.m_includeExpired = true;
		ibExchangeClientSocket.eClientSocket.reqMktData(requestHolder.requestId, contract, "100,101,104,105,106,107,165,236,293,294,295,411", false);
	}
	
	public void getHistoricalPrice(HistoricalData historicalData, RequestHolder requestHolder){
		//Co.println("Request id: " + requestHolder.requestId);
		Contract contract = new Contract();
		contract.m_exchange = historicalData.exchange.name;
		contract.m_symbol = historicalData.symbol.symbolName;
		contract.m_secType = historicalData.securityType;
		contract.m_currency = historicalData.exchange.currency.name();
		String endDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(historicalData.endDate) + " est";
		String duration = String.valueOf(historicalData.duration) + " S";
		ibExchangeClientSocket.eClientSocket.reqHistoricalData(requestHolder.requestId, contract, endDate, duration, "1 day", "TRADES", 0, 2);
	}
	
	public void cancelScanner(RequestHolder requestHolder){
		ibExchangeClientSocket.eClientSocket.cancelScannerSubscription(requestHolder.requestId);
	}
	
	public void cancelMarketData(RequestHolder requestHolder){
		ibExchangeClientSocket.eClientSocket.cancelMktData(requestHolder.requestId);
	}
}
