import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { HeadComponent } from './head.component';

describe('HeadComponent', () => {
  let component: HeadComponent;
  let fixture: ComponentFixture<HeadComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ HeadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
