import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalizadoComponent } from './finalizado.component';

describe('FinalizadoComponent', () => {
  let component: FinalizadoComponent;
  let fixture: ComponentFixture<FinalizadoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FinalizadoComponent]
    });
    fixture = TestBed.createComponent(FinalizadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
