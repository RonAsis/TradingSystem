import {EventEmitter, Injectable, Input} from '@angular/core';
import {Store} from '../shared/store.model';
import {HttpClient} from '@angular/common/http';
import {Product} from '../shared/product.model';
import {Receipt} from '../shared/receipt.model';
import {HttpService} from './http.service';
import {UserService} from './user.service';
import {Discount} from '../shared/discount.model';
import {Policy} from '../shared/policy.model';

@Injectable({
  providedIn: 'root'
})
export class StoreService {
  storeSelected = new EventEmitter<Store>();

  // for know which stores get
  private ownerStore = false;
  private mangerStore = false;
  discountSelected = new EventEmitter<Discount>();
  discountAdded = new EventEmitter<Discount>();
  policySelected = new EventEmitter<Policy>();
  policyAdded = new EventEmitter<Policy>();
  constructor(private httpService: HttpService, private userService: UserService) {
  }

  public setOwnerStores(ownerStore: boolean) {
    this.ownerStore = ownerStore;
  }

  public getOwnerStores() {
    return this.ownerStore;
  }

  public setManagerStores(mangerStore: boolean) {
    this.mangerStore = mangerStore;
  }

  public getManagerStores() {
    return this.mangerStore;
  }

  getStores(username: string, uuid: string) {
    if (this.ownerStore) {
      console.log('owner stores');
      return this.httpService.getOwnerStores(username, uuid);
    } else if (this.mangerStore) {
      console.log('manager stores');
      return this.httpService.getManageStores(username, uuid);
    } else {
      console.log('get stores');
      return this.httpService.getStores();
    }
  }

  viewPurchaseHistory(storeId: number) {
      return this.httpService
        .viewPurchaseHistoryOfOwner(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  openStore(storeName: string, description: string) {
    return this.httpService.openStore(this.userService.getUsername(), storeName, description, this.userService.getUuid());
  }

  addProduct(storeId: number, productName: string, category: string, amount: number, cost: number) {
    return this.httpService.addProduct(this.userService.getUsername(), storeId, productName, category,
      amount, cost, this.userService.getUuid());
  }

  deleteProductFromStore(productSn: number, storeId: number) {
    return this.httpService.deleteProductFromStore(this.userService.getUsername(),
      storeId, productSn, this.userService.getUuid());
  }

  editProduct(storeId: any, productSn: number, productName: string, category: string, amount: number, cost: number) {
    return this.httpService.editProduct(this.userService.getUsername(), storeId, productSn, productName, category,
      amount, cost, this.userService.getUuid());
  }

  public getAllUsernameNotOwnerNotManger( storeId: number) {
    return this.httpService.getAllUsernameNotOwnerNotManger(this.userService.getUsername(),
      storeId, this.userService.getUuid());
  }

  addOwner(storeId: number, selectedNewOwner: string) {
    return this.httpService.addOwner(this.userService.getUsername(),
      storeId, selectedNewOwner, this.userService.getUuid());
  }

  getMySubOwners(storeId: number) {
    return this.httpService.getMySubOwners(this.userService.getUsername(),
      storeId, this.userService.getUuid());
  }

  addManager(storeId: number, selectedNewManager: string) {
    return this.httpService.addManager(this.userService.getUsername(),
      storeId, selectedNewManager, this.userService.getUuid());
  }

  getMySubMangers(storeId: number) {
    return this.httpService.getMySubMangers(this.userService.getUsername(),
      storeId, this.userService.getUuid());
  }

  removePermission(managerUsername: string, storeId: number, permission: string) {
    return this.httpService.removePermission(this.userService.getUsername(),
      storeId, managerUsername, permission, this.userService.getUuid());
  }

  removeManager(manager: string, storeId: number) {
    return this.httpService.removeManager(this.userService.getUsername(), storeId, manager, this.userService.getUuid());

  }

  addPermission(storeId: number, managerUsername: string, permission: string) {
    return this.httpService.addPermission(this.userService.getUsername(), storeId, managerUsername, permission, this.userService.getUuid());
  }

  getPermissionsCanAdd(managerUsername: string, storeId: number) {
    return this.httpService.getPermissionCantDo(this.userService.getUsername(), storeId, managerUsername, this.userService.getUuid());
  }

  getMyPermissions(storeId: number) {
    return this.httpService.getMyPermissions(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  getIsOwner(storeId: number) {
    return this.httpService.isOwner(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  getStoreDiscounts(storeId: number) {
    return this.httpService.getStoreDiscounts(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  removeDiscount(discountId: number, storeId: number) {
    return this.httpService.removeDiscount(this.userService.getUsername(), storeId, discountId, this.userService.getUuid());
  }

  getCompositeOperators(storeId: number) {
    return this.httpService.getCompositeOperators(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  getDiscounts(storeId: number) {
    return this.httpService.getAllDiscounts(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  getSimpleDiscounts(storeId: number) {
    return this.httpService.getSimpleDiscounts(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  getAllPurchasePolicies(storeId: number) {
    return this.httpService.getAllPurchasePolicies(this.userService.getUsername(), storeId, this.userService.getUuid());
  }

  addDiscount(storeId: number, discount: Discount) {
    return this.httpService.addDiscount(this.userService.getUsername(), storeId, discount, this.userService.getUuid());
  }

  addPolicy(storeId: number, policy: Policy) {
    return this.httpService.addPolicy(this.userService.getUsername(), storeId, policy, this.userService.getUuid());
  }

  removeOwner(storeId: number, ownerRemove: string){
    return this.httpService.removeOwner(this.userService.getUsername(), storeId, ownerRemove, this.userService.getUuid());

  }


}
