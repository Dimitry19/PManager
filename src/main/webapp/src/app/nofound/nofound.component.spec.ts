import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { NofoundComponent } from './nofound.component';

describe('NofoundComponent', () => {
  let component: NofoundComponent;
  let fixture: ComponentFixture<NofoundComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ NofoundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NofoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});