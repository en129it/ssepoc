package com.ddv.test;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ddv.test.model.Event;
import com.ddv.test.model.Transaction;

@RestController
public class TxnController {

	@Autowired
	private TxnRepository txnRepository;
	
	@Autowired
	private SseService sseService;
	
    @RequestMapping(path = "/txn/{txnId}/acquireLock", method = RequestMethod.GET)
	public void acquireLock(@PathVariable("txnId") Integer aTxnId, HttpServletRequest aRequest) {
    	System.out.println("###### AcquireLock " + aTxnId);
    	Transaction txn = txnRepository.getTxn(aTxnId);
    	if (txn!=null) {
    		txn.setLocked(true);
    		sseService.emitEvent(new Event("LOCK_ACQUIRED", aTxnId.toString()));
    	}
	}
	
    @RequestMapping(path = "/txn/{txnId}/releaseLock", method = RequestMethod.GET)
	public void releaseLock(@PathVariable("txnId") Integer aTxnId, HttpServletRequest aRequest) {
    	System.out.println("###### ReleaseLock " + aTxnId);
    	Transaction txn = txnRepository.getTxn(aTxnId);
    	if (txn!=null) {
    		txn.setLocked(false);
    		sseService.emitEvent(new Event("LOCK_RELEASED", aTxnId.toString()));
    	}
	}
	
    @RequestMapping(path = "/txn/{txnId}/txnRepaired", method = RequestMethod.GET)
	public void txnRepaired(@PathVariable("txnId") Integer aTxnId, HttpServletRequest aRequest) {
    	System.out.println("###### TxnRepaired " + aTxnId);
    	if (txnRepository.removeTxn(aTxnId)) {
    		sseService.emitEvent(new Event("TXN_REMOVED", aTxnId.toString()));
    	}
	}
	
    @RequestMapping(path = "/txn", method = RequestMethod.GET)
    public List<Transaction> getTxns() {
    	return txnRepository.getTxns();
    }
    
    private String getSessionId(HttpServletRequest aRequest) {
    	return aRequest.getSession().getId();
    }
}
