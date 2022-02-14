export interface Task {
  label: string;
  completed: boolean;
}

export class TaskUtils {

  static createTask(label: string, completed: boolean = false): Task {
    return {label, completed};
  }

}

/**
 * Typescript exemple useful if you don't like enum !
 * and mostly used when typescript was not proposing string-enums.
 * So don't use it in this exercise.
 */
export type Status = 'ALL' | 'COMPLETED' | 'ACTIVE';

/**
 * enum using strings as values
 */
export enum TaskCompleteEnum {
  ALL = 'ALL', ACTIVE = 'ACTIVE', COMPLETED = 'COMPLETED'
}

// enum use example
const myString: Status = 'ALL';
const myEnumValue: TaskCompleteEnum = TaskCompleteEnum.ACTIVE;
