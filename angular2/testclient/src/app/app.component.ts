import { Component, NgZone } from '@angular/core';
import { SSEService } from './service/sse.service';
import { Transaction } from './model/txn';
import { Event } from './model/event';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  public txns = new Array<Transaction>();

  constructor(private sseService: SSEService, private zone: NgZone) {
    this.sseService.getTxns().subscribe( (data) => {
      this.txns = data;
    });
    this.sseService.subscribeEvents().subscribe( (event: Event) => {
      if (event.eventType === 'LOCK_ACQUIRED') {
        this.zone.run(() => {
            const txnId = parseInt(event.eventValue, 10);
            console.log('Event lock acquired ' + txnId);
            this.txns.forEach( (item) => {
              console.log('   ' + (item.id === txnId) + '   ' + item.id + '<>' + txnId);
              if (item.id === txnId) {
                item.locked = true;
              }
            });
        });
      } else if (event.eventType === 'LOCK_RELEASED') {
        this.zone.run(() => {
          const txnId = parseInt(event.eventValue, 10);
          console.log('Event lock released');
          this.txns.forEach( (item) => {
            if (item.id === txnId) {
              item.locked = false;
            }
          });
        });
      } else if (event.eventType === 'TXN_REMOVED') {
        this.zone.run(() => {
          const txnId = parseInt(event.eventValue, 10);
          console.log('Event txn removed');
          for (let i = 0; i < this.txns.length; i++) {
            if (this.txns[i].id === txnId) {
              this.txns.splice(i, 1);
              break;
            }
          }
        });
      }
    });
  }

  public acquireLock(aTxnId: number) {
    console.log('AcquireLock ' + aTxnId);
    this.sseService.acquireLock(aTxnId);
  }

  public releaseLock(aTxnId: number) {
    console.log('ReleaseLock ' + aTxnId);
    this.sseService.releaseLock(aTxnId);
  }

  public repaired(aTxnId: number) {
    console.log('Repaired ' + aTxnId);
    this.sseService.txnRepaired(aTxnId);
  }
}
