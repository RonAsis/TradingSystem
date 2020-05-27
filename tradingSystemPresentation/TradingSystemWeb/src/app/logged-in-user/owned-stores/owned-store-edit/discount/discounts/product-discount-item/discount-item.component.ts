import {Component, Input, OnInit} from '@angular/core';
import {Discount} from '../../../../../../shared/discount.model';
import {StoreService} from '../../../../../../services/store.service';

@Component({
  selector: 'app-discount-item',
  templateUrl: './app-discount-item.component.html',
  styleUrls: ['./app-discount-item.component.css']
})
export class DiscountItemComponent implements OnInit {

  @Input() discount: Discount;
  endTime: string;

  constructor(private storeService: StoreService) {
  }

  ngOnInit(): void {
    this.endTime = new Date(this.discount.endTime).toISOString().split('T')[0];
  }

  onSelected() {
    this.storeService.discountSelected.emit(this.discount);
  }
}
