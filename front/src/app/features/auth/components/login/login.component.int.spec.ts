import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent Integration Tests', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  const mockRouter = {
    navigate: jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        AuthService,
        SessionService,
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show error message on failed login', () => {
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('error')));
    component.submit();
    fixture.detectChanges();
    expect(component.onError).toBe(true);
    const errorMsg = fixture.nativeElement.querySelector('.error');
    expect(errorMsg).toBeTruthy();
  });

  it('should navigate to /sessions on successful login', () => {
    const sessionInformation: SessionInformation = {
      token: '123',
      type: 'Bearer ',
      id: 1,
      username: 'johnwick',
      firstName: 'John',
      lastName: 'Wick',
      admin: false
    };

    jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformation));
    const navigateSpy = jest.spyOn(router, 'navigate');
    const logInSpy = jest.spyOn(sessionService, 'logIn');
    component.submit();
    expect(logInSpy).toBeCalledWith(sessionInformation);
    expect(navigateSpy).toBeCalledWith(['/sessions']);
  });

  it('should have form controls', () => {
    expect(component.form.contains('email')).toBe(true);
    expect(component.form.contains('password')).toBe(true);
  });

  it('should disable submit button if email is empty', () => {
    const email = component.form.get('email');
    const password = component.form.get('password');
    email?.setValue('');
    password?.setValue('test!1234');
    fixture.detectChanges();
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBe(true);
  });

  it('should disable submit button if password is empty', () => {
    const email = component.form.get('email');
    const password = component.form.get('password');
    email?.setValue('test@test.com');
    password?.setValue('');
    fixture.detectChanges();
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBe(true);
  });
});