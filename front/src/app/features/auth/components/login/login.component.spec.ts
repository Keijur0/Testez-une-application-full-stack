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
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: any;
  let mockRouter: any;
  let mockSessionService: any;

  beforeEach(async () => {
    mockAuthService = {
      login: jest.fn()
    };
    mockRouter = {
      navigate: jest.fn()
    };
    mockSessionService = {
      logIn: jest.fn()
    };
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        {
          provide: AuthService,
          useValue: mockAuthService
        },
        {
          provide: Router,
          useValue: mockRouter
        },
        {
          provide: SessionService,
          useValue: mockSessionService
        }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show error message on failed login', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('error')));
    component.submit();
    expect(component.onError).toBe(true);
  });

  it('should navigate to /sessions on successful login', () => {
    const sessionInformation: SessionInformation = {
      token: '123',
      type: 'user',
      id: 1,
      username: 'johnwick',
      firstName: 'John',
      lastName: 'Wick',
      admin: false
    }

    mockAuthService.login.mockReturnValue(of(sessionInformation));
    component.submit();
    expect(mockSessionService.logIn).toBeCalledWith(sessionInformation);
    expect(mockRouter.navigate).toBeCalledWith(['/sessions']);
  });

  it('should have form controls', () => {
    expect(component.form.contains('email')).toBe(true);
    expect(component.form.contains('password')).toBe(true);
  });

  it('should mark email as invalid if it is empty', () => {
    let email = component.form.get('email');
    email?.setValue('');
    expect(email?.valid).toBe(false);
  });

/*   it('should mark password as invalid if it has less than 3 characters', () => {
    let password = component.form.get('password');
    password?.setValue('qs');
    expect(password?.valid).toBe(false);
  }); */
});
