import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        AuthService
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        RouterTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],

    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disable submit button if form is invalid', () => {
    component.form.patchValue({
        firstName: '',
        lastName: '',
        email: '',
        password: ''
      });
      fixture.detectChanges();

    expect(component.form.valid).toBeFalsy();
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBeTruthy();
  });

  it('should enable submit button if form is valid', () => {
    component.form.patchValue({
      firstName: 'John',
      lastName: 'Wick',
      email: 'john.wick@test.com',
      password: 'test!1234'
    });
    fixture.detectChanges();

    expect(component.form.valid).toBeTruthy();
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBeFalsy();
  });

  it('should call authService.register() on form submission', () => {
    const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    const registerRequest = {
      firstName: 'John',
      lastName: 'Wick',
      email: 'john.wick@test.com',
      password: 'test!1234'
    };
    component.form.setValue(registerRequest);
    fixture.detectChanges();

    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    submitButton.click();
    expect(registerSpy).toBeCalledWith(registerRequest);
  });

  it('should navigate to /login on successful registration', () => {
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    const navigateSpy = jest.spyOn(router, 'navigate');
    const registerRequest = {
      firstName: 'John',
      lastName: 'Wick',
      email: 'john.wick@test.com',
      password: 'test!1234'
    };
    component.form.setValue(registerRequest);
    fixture.detectChanges();

    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    submitButton.click();
    fixture.detectChanges();

    expect(navigateSpy).toBeCalledWith(['/login']);
  });

  it('should show error message on failed registration', () => {
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('Error')));
    const registerRequest = {
      firstName: 'John',
      lastName: 'Wick',
      email: 'john.wick@test.com',
      password: 'test!1234'
    };
    component.form.setValue(registerRequest);
    fixture.detectChanges();

    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    submitButton.click();
    fixture.detectChanges();

    expect(component.onError).toBeTruthy();
  });
});