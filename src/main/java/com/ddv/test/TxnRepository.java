package com.ddv.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ddv.test.model.Transaction;

@Repository
public class TxnRepository {

	private ArrayList<Transaction> data = new ArrayList<Transaction>();  
	
	
	public TxnRepository() {
		data.add(createTxn(1, "Securities Lending", 123.45));
		data.add(createTxn(2, "Securities Lending", 5764.85));
		data.add(createTxn(3, "Loan Deposit", 175.753));
		data.add(createTxn(4, "Purchase Sale", 8276.875));
	}
	
	public Transaction getTxn(Integer aTxnId) {
		for (Transaction txn : data) {
			if (txn.getId().equals(aTxnId)) {
				return txn;
			}
		}
		return null;
	}
	
	public boolean removeTxn(Integer aTxnId) {
		Transaction txn = null;
		Iterator<Transaction> iter = data.iterator();
		while (iter.hasNext()) {
			txn = iter.next();
			if (txn.getId().equals(aTxnId)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
	public List<Transaction> getTxns() {
		return new ArrayList<Transaction>(data);
	}
	
	private Transaction createTxn(Integer anId, String aType, Double anAmount) {
		Transaction txn = new Transaction();
		txn.setId(anId);
		txn.setType(aType);
		txn.setAmount(anAmount);
		return txn;
	}
}
