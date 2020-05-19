import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Store} from '../../../../../shared/store.model';
import {Options} from 'ng5-slider';
import {StoreService} from '../../../../../services/store.service';
import {UserService} from '../../../../../services/user.service';
import { Discount } from 'src/app/shared/discount.model';

@Component({
  selector: 'app-discounts',
  templateUrl: './discounts.component.html',
  styleUrls: ['./discounts.component.css']
})
export class DiscountsComponent implements OnInit {

  @Output() discountWasSelected = new EventEmitter<Discount>();
  @Input() store: Store;
  discounts: Discount [] ;

  constructor(private storeService: StoreService, private userService: UserService) {
    this.discounts = [];
  }

  ngOnInit(): void {
    this.storeService.getDiscounts(this.store.storeId)
      .subscribe(discounts => {
        if (discounts !== null && discounts !== undefined) {
          this.discounts = discounts;
          console.log(this.discounts);
        }
      });
  }


}
