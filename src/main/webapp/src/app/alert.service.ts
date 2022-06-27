import {Injectable} from '@angular/core';
import {ToastrService} from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AlertService {


  constructor(private toastr: ToastrService) { }

  showSuccess(message, title, options?){
      this.toastr.success(message, title, options)
  }

  showError(message, title, options?){
      this.toastr.error(message, title, options);
  }

  showInfo(message, title, options?){
      this.toastr.info(message, title, options)
  }

  showWarning(message, title, options?){
      this.toastr.warning(message, title, options)
  }
}
