import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {StoreService} from '../../services/store.service';
import {WebSocketAPI} from '../../shared/apis/webSocketApi.model';
import {UserService} from '../../services/user.service';
import {ShareService} from '../../services/share.service';

@Component({
  selector: 'app-header-logging-user',
  templateUrl: './header-logging-user.component.html',
  styleUrls: ['./header-logging-user.component.css']
})
export class HeaderLoggedInUserComponent implements OnInit {
  @Output() featureSelectedLogging = new EventEmitter<string>();
  private isAdmin: boolean;

  constructor(private storeService: StoreService, private userService: UserService,
              private shareService: ShareService) {
    this.isAdmin = userService.getIsAdmin();
  }

  ngOnInit(): void {
    this.shareService.featureSelected.subscribe(feature => {
      this.teraAll();
      if (feature === 'Owned-stores'){
        this.storeService.setOwnerStores(true);
      }else if (feature === 'Managed-stores'){
        this.storeService.setManagerStores(true);
      }
    });
  }

  onSelect(feature: string) {
    this.teraAll();
    this.storeService.setOwnerStores(feature === 'Owned-stores');
    this.storeService.setManagerStores(feature === 'Managed-stores');
    this.featureSelectedLogging.emit(feature);
  }

  private teraAll() {
    this.storeService.setOwnerStores(false);
    this.storeService.setManagerStores(false);
  }

  getIsAdmin() {
     return this.isAdmin;
  }
}
