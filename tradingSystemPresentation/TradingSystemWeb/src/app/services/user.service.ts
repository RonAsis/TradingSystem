import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserSystem} from '../shared/userSystem.model';
import {Product} from '../shared/product.model';
import {ShoppingCart} from '../shared/shoppingCart.model';
import {ShoppingBag} from '../shared/shoppingBag.model';
import {formatNumber} from '@angular/common';
import {Receipt} from '../shared/receipt.model';
import {Store} from '../shared/store.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly guestUrl: string;
  private readonly buyerUrl: string;
  private shoppingCart: ShoppingCart;
  private isAdmin: boolean;
  private uuid: string;
  private username: string;
  private usernameWantSeeHistory = null;

  userLoggingEvent = new EventEmitter<boolean>();
  logoutNoEvent = new EventEmitter<boolean>();
  userSelectedEvent = new EventEmitter<UserSystem>();

  constructor(private http: HttpClient) {
    this.guestUrl = 'http://localhost:8080/guest';
    this.buyerUrl = 'http://localhost:8080/buyer-reg';
    this.shoppingCart = new ShoppingCart(new Map<number, ShoppingBag>());
  }

  public getUsername() {
    console.log(this.username);
    return this.username;
  }

  public getIsAdmin() {
   // return this.isAdmin;
    return true;
  }


  public getUuid() {
    return this.uuid;
  }

  public register(username: string,
                  password: string,
                  firstName: string,
                  lastName: string) {
    const urlRegister = `${this.guestUrl}/` +
      'register-user/' +
      `${username}/` +
      `${password}/` +
      `${firstName}/` +
      `${lastName}`;
    return this.http.post<boolean>(
      urlRegister, null);
  }

  login(username: string, password: string) {
    this.usernameWantSeeHistory = null;
    const urlLogin = `${this.guestUrl}/` +
      'login/' +
      `${username}/` +
      `${password}`;
    this.http.put<{ key: string; value: boolean }>(
      urlLogin, null).subscribe(
      response => {
        if (response !== null && response.key !== null) {
          this.uuid = response.key;
          this.isAdmin = response.value;
          this.userLoggingEvent.emit(true);
          this.username = username;
          console.log(this.username);
        }
      });
    return this.uuid !== null;
  }

  addToShoppingCart(product: Product, amountProducts: number) {
    const storeId = product.storeId;
    let shoppingBag = this.shoppingCart.shoppingBags.get(storeId);
    if (shoppingBag === undefined) {
      const products = new Map<Product, number>();
      products.set(product, amountProducts);
      shoppingBag = new ShoppingBag(products);
    }
    this.shoppingCart.shoppingBags.set(storeId, shoppingBag);
  }

  logout() {
    const urlLogout = `${this.buyerUrl}/` +
      'logout/' +
      `${(this.username)}/` +
      `${(this.uuid)}`;
    this.http.put<boolean>(
      urlLogout, null).subscribe(
      response => {
        if (response) {
          this.uuid = null;
          this.isAdmin = false;
          this.userLoggingEvent.emit(false);
        }
      });
  }

  viewPurchaseHistory() {
    let receipts: Receipt[] = [];
    const purchaseHistory = `${this.buyerUrl}/` +
      'view-purchase-history/' +
      `${(this.username)}/` +
      `${(this.uuid)}`;
    this.http.get<Receipt[]>(
      purchaseHistory).subscribe(res => receipts = res);
    const map = new Map();
    map.set(new Product(1, '2', 'sds', 1, 3232, 323, 1, 1, 'sdsd'), 3);
    map.set(new Product(1, '2', 'sdsdsdds', 1, 232, 323, 1, 1, 'sdsd'), 3);

    return [new Receipt(1, 1, 'sds', new Date(), 2, map)];
    // return receipts;
  }

  getUsers() {
    console.log(this.usernameWantSeeHistory);
    if (this.usernameWantSeeHistory !== null){
      return [new UserSystem('ron', 'ron', 'asis'),
        new UserSystem('ron1', 'ro1n11', 'asis1')];
    }else{
      return [new UserSystem('null', 'null', 'null'),
        new UserSystem('null', 'null', 'null')];
    }
  }

  wantViewPurchaseHistory(user: UserSystem) {
    this.usernameWantSeeHistory = user.username;
  }
}
