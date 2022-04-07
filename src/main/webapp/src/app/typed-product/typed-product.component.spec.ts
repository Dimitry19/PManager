import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypedProductComponent } from './typed-product.component';

describe('TypedProductComponent', () => {
  let component: TypedProductComponent;
  let fixture: ComponentFixture<TypedProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TypedProductComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TypedProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
