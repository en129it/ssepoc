import { Observable, Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Transaction } from '../model/txn';
import { Injectable } from '@angular/core';
import { Event } from '../model/event';

declare var window;

@Injectable()
export class SSEService {

    constructor(private httpClient: HttpClient) {
    }

    public getTxns(): Observable<Array<Transaction>> {
        return this.httpClient.get<Array<Transaction>>('/txn');
    }

    public acquireLock(txnId: number) {
        const url = '/txn/' + txnId + '/acquireLock';
        console.log('Service AcquireLock ' + url);
        this.httpClient.get(url).subscribe( () => {});
    }

    public releaseLock(txnId: number) {
        const url = '/txn/' + txnId + '/releaseLock';
        console.log('Service ReleaseLock ' + url);
        this.httpClient.get(url).subscribe( () => {});
    }

    public txnRepaired(txnId: number) {
        const url = '/txn/' + txnId + '/txnRepaired';
        console.log('Service TxnRepaired ' + url);
        this.httpClient.get(url).subscribe( () => {});
    }

    public subscribeEvents(): Observable<Event> {
        return new Observable<Event>(observer => {
            const eventSource = new EventSource('/sse/subscribe');
            eventSource.onmessage = function (e) {
                const event: Event = JSON.parse(e.data);
                console.log('>>>>> event received');
                observer.next(event);
            };
            eventSource.onopen = function (e) {
                console.log('>>>>> connection opened');
            };
            eventSource.onerror = function (e: any) {
                console.log('>>>>> on error');
            };
        });
    }
}
