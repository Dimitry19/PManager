import { Component } from '@angular/core';
import { Task } from './task.model';

@Component({
  selector: 'app-options',
  templateUrl: './options.component.html',
  styleUrls: ['./options.component.css']
})
export class OptionsComponent {


  constructor() { }

  remainingTasks = 0;
  toggleAllTask = false;
  tasks : Task[] = [];

  removeTask(index: number){
    this.tasks.splice(index,1);
  }
  removeAllTask(){
    this.tasks.splice(0,this.tasks.length);
  }
  addTask(task: string){
    if(task.length > 0){
      this.tasks.push({
        label:task,
        completed: false
      });
    }
    
  }
  toggleAll(){
    this.toggleAllTask = !this.toggleAllTask;
    this.tasks.forEach(element => {
      element.completed = this.toggleAllTask;
    });
  }
  toggleTask(i: number){
    this.tasks[i].completed = !this.tasks[i].completed;
  }
  count(): number{
    let c = 0;
    this.tasks.forEach(elt =>{
      if(!elt.completed){
        c++;
      }
    })
    return c;
  }

}
