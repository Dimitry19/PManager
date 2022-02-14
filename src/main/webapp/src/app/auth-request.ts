import {RxStomp} from '@stomp/rx-stomp';
import SockJS from 'sockjs-client';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, finalize, catchError} from 'rxjs/operators';
import { NgxSpinnerService } from "ngx-spinner";
import { Urlconstances } from './constancesURL';
import { HttpClient, HttpHeaders } from  '@angular/common/http'; 

export interface notif {
  id: number;
  message: string;
  title?: string;
};


@Injectable({
  providedIn: 'root'
})

export class AuthRequest {
 
  sessionUser: string = '';
  
  constructor(private spinner: NgxSpinnerService,private httpClient: HttpClient) {

  }

// HttpErrorResponse
  handleError(error) {
    let errorMessage;
    // if (error.error instanceof ErrorEvent) {
    //   // Client-side errors
    //   errorMessage = `Error: ${error.error.message}`;
    // } else {
    //   // Server-side errors
    //     errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    // }
    // window.alert(errorMessage); 

    if(typeof error._body === "object"){
      errorMessage = JSON.parse(error._body);
      return of(errorMessage);
    }else {
      //   // Server-side errors
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      return errorMessage;
      //window.alert(errorMessage); 
    }
      
    
    // return throwError(errorMessage);
    
  }

  setOptions() {
    const headers = { headers: new HttpHeaders({
      "AUTH_API_KEY":"abcd123456",
      "session-user": this.sessionUser,
      'content-type': 'application/json;text/plain;multipart/form-data;image/*'
  })};
// loader de la page
    this.spinner.show();

    return headers;
  }


  //method authentication
  //   isAuthenticated(): {
  //     var options = this.setOptions();
  //     var timestamp = new Date().getTime();
  //   return this.httpClient.get('url.BASEURL + 'ws/user/login'+ timestamp, options).pipe(map((res: any)=> res).
  //   catchError((error: any) => {
  //     this.handleError(error);
  //     return Observable.throw(error.statusText);
  //   }));
  //
  // }
  //method login without social network
isLoggedIn(data){
    this.sessionUser = data.username;
    var options = this.setOptions();
    // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/user/ulogin', JSON.stringify(data),options).pipe(map((res: any)=> res
    ),
   catchError((this.handleError)),
   finalize(() => {
    this.spinner.hide();
  }));
}

Registration(data){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/user/register', JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

forgotPwd(data){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/user/password', JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
      this.spinner.hide();
    }));
}

annonceAdd(data){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/announce/create', JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
      this.spinner.hide();
    }));
}

annonceDel(id){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.delete(Urlconstances.BASEURL + 'ws/announce/delete?id='+id,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

annonceUpd(data){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.put(Urlconstances.BASEURL + 'ws/announce/update/'+data.id, JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

annoncesList(numPage){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/announce/announces?page='+numPage,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}


annonceLoggedUser(idUser){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/user/info/'+idUser,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
          this.spinner.hide();
      }));
  }

annonceId(idAnnonce){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/announce/announce?id='+idAnnonce,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

annonceUserId(idUser){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/announce/user'+idUser,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

addComment(data){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/message/add', JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}
deleteComment(idcomment){
    // console.log(JSON.stringify(data));
    var options = this.setOptions();
    // var timestamp = new Date().getTime();
  return this.httpClient.delete(Urlconstances.BASEURL + 'ws/message/delete?id='+idcomment,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
          this.spinner.hide();
        }));
}
updateComment(idcomment,data){
    // console.log(JSON.stringify(data));
    var options = this.setOptions();
    // var timestamp = new Date().getTime();
  return this.httpClient.put(Urlconstances.BASEURL + 'ws/message/update/'+idcomment.id, JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
          this.spinner.hide();
        }));
}
filter(filter){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/announce/find', JSON.stringify(filter),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

userNotifications(bool,idUser){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/user/notification?enable='+bool+'&userId='+idUser,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}
userUpd(idUser){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.put(Urlconstances.BASEURL + 'ws/user/update/'+idUser.id,JSON.stringify(idUser), options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

userUpdPwd(pwd,idUser){
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.put(Urlconstances.BASEURL + 'ws/user/password/'+idUser,JSON.stringify(pwd), options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

usrImg(id,type,file){

  const ToUpload = new FormData();
  ToUpload.append('imageFile',file,file.name);
  var options = this.setOptions();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/image/upload?id='+id+'&type='+type,ToUpload).pipe(map((res: any) => res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

  getUsrImg(imgName){
    // console.log(JSON.stringify(data));
    var options = this.setOptions();
    // var timestamp = new Date().getTime();
    return this.httpClient.get(Urlconstances.BASEURL + 'ws/image/'+imgName,options).pipe(map((res: any)=> res),
    catchError((this.handleError)),finalize(() => {
          this.spinner.hide();
        }));
  }


addReserve(data){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/reservation/add', JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}

updateReserve(idres,data){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.put(Urlconstances.BASEURL + 'ws/reservation/update/'+idres, JSON.stringify(data),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
  }


deleteReserve(idres){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.delete(Urlconstances.BASEURL + 'ws/reservation/delete?id='+idres,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
  }

  validReserve(data){
    // console.log(JSON.stringify(data));
    var options = this.setOptions();
    // var timestamp = new Date().getTime();
    return this.httpClient.post(Urlconstances.BASEURL + 'ws/reservation/validate', JSON.stringify(data),options).pipe(map((res: any)=> res),
    catchError((this.handleError)),finalize(() => {
          this.spinner.hide();
        }));
  }

getReserveUser(idUser){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/reservation/user?page=0&size=12&userId='+idUser+'&type=CREATED',options).pipe(map((res: any)=> res),
  catchError((this.handleError)),
  finalize(() => {
        this.spinner.hide();
      }));
}
getReserveAnn(idAnnonce){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/reservation/announce?announceId='+idAnnonce+'&page=0&size=12',options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
  }

notificationId(idnotification){
    // console.log(JSON.stringify(data));
    var options = this.setOptions();
    // var timestamp = new Date().getTime();
  return this.httpClient.get(Urlconstances.BASEURL + 'ws/notification/notification?id='+idnotification,options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
          this.spinner.hide();
        }));
   }

addRatingAnnounce(rating){
   // console.log(JSON.stringify(data));
   var options = this.setOptions();
   // var timestamp = new Date().getTime();
   return this.httpClient.post(Urlconstances.BASEURL + 'ws/review/add', JSON.stringify(rating),options).pipe(map((res: any)=> res),
   catchError((this.handleError)),finalize(() => {
         this.spinner.hide();
       }));
  }
followUser(idUser){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/user/subscribers/add', JSON.stringify(idUser),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}
unFollowUser(subscription){
  // console.log(JSON.stringify(data));
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
  return this.httpClient.post(Urlconstances.BASEURL + 'ws/user/unsubscribe', JSON.stringify(subscription),options).pipe(map((res: any)=> res),
  catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}
//ceux que je suis
mesAbonnements(idUser){
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
return this.httpClient.get(Urlconstances.BASEURL + 'ws/user/subscriptions/'+idUser,options).pipe(map((res: any)=> res),
catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}
//ceux qui me suivent
mesAbonnes(idUser){
  var options = this.setOptions();
  // var timestamp = new Date().getTime();
return this.httpClient.get(Urlconstances.BASEURL + 'ws/user/subscribers/'+idUser,options).pipe(map((res: any)=> res),
catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
}
    //authentication
    // get isAuthenticated(): boolean{
    //   let authtoken = sessionStorage.getItem('access_token');
    //   return (authtoken !== null) ? true : false;
    // }
  //method logout without social network/pmanager
  islogout(username) {
      // console.log(JSON.stringify(data));
      var options = this.setOptions();
      // var timestamp = new Date().getTime();
      return this.httpClient.get(Urlconstances.BASEURL + 'ws/user/logout?username='+username,options).pipe(map((res: any)=> res),
      catchError((this.handleError)),finalize(() => {
        this.spinner.hide();
      }));
  }

   /**SOCKECT API */
   /*source stomp: https://ngdeveloper.com/spring-boot-2-angular-11-websocket-2-3-sockjs-1-0-2/ */
   /**soure rxstomp: https://www.codesandnotes.be/2020/03/31/websocket-based-notification-system-using-spring/ */

private client: RxStomp;
public notifications: notif[] = [];

connectClicked(user: string) {
  if (!this.client || this.client.connected) {
    this.client = new RxStomp();
    this.client.configure({
      webSocketFactory: () => new SockJS(Urlconstances.webSocketEndPoint),
      connectHeaders:{'user': user},
      debug: (msg: string) => console.log("debug",msg)
    });
    this.client.activate();

    this.watchForNotifications();

    console.info('connected!');
  }
}

watchForNotifications() {
  this.client.watch('/user/notification/item')
    .pipe(
      map(response => {
        //const text: string = JSON.parse(response.body).message;
        const text: notif = {"id": JSON.parse(response.body).id, "message": JSON.parse(response.body).message,"title": JSON.parse(response.body).title};
        return text;
      }))
    .subscribe((notification) => this.notifications.push(notification));
}

disconnectClicked() {
  if (this.client && this.client.connected) {
    this.client.deactivate();
    this.client = null;
    console.info("disconnected :-/");
  }
}

startClicked() {
  if (this.client && this.client.connected) {
    this.client.publish({destination: '/swns/start'});
  }
}

stopClicked() {
  if (this.client && this.client.connected) {
    this.client.publish({destination: '/swns/stop'});
  }
}

}
