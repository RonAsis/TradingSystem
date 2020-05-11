import {Component, Input, OnInit} from '@angular/core';
import {Store} from '../../../../shared/store.model';
import {StoreService} from '../../../../services/store.service';

@Component({
  selector: 'app-owners',
  templateUrl: './owners.component.html',
  styleUrls: ['./owners.component.css']
})
export class OwnersComponent implements OnInit {
  @Input() store: Store;
  owners: string[];
  constructor(private storeService: StoreService) {
    this.owners = [];
  }

  ngOnInit(): void {
    this.storeService.getMySubOwner(this.store.storeId).subscribe(
      response => {
        if (response !== undefined && response !== null){
          this.owners = response;
        }
      }
    );
  }

  onOwnerAdded(newOwner: string) {
    console.log(newOwner);
    this.owners.push(newOwner);
  }

}
