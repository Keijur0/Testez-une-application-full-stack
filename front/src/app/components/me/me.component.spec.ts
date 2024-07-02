import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  }
  const mockDate = new Date();
  const mockAdmin = {
    id: 1,
    email: 'john.wick@test.com',
    lastName: 'Wick',
    firstName: 'John',
    admin: true,
    password: 'test!1234',
    createdAt: mockDate,
    updatedAt: mockDate
  };

  const mockUser = {
    id: 1,
    email: 'john.wick@test.com',
    lastName: 'Wick',
    firstName: 'John',
    admin: false,
    password: 'test!1234',
    createdAt: mockDate,
    updatedAt: mockDate
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { 
          provide: SessionService, 
          useValue: mockSessionService
        },
        {
          provide: UserService,
          useValue: {
            getById: jest.fn().mockReturnValue(of(mockAdmin)),
            delete: jest.fn().mockReturnValue(of({}))
          }
        },
        {
          provide: MatSnackBar,
          useValue: {
            open: jest.fn()
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jest.fn()
          }
        }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display user information', () => {
    component.user = mockAdmin;
    fixture.detectChanges();

    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('p').textContent).toContain('Name: John WICK');
    expect(compiled.querySelector('p:nth-child(2)').textContent).toContain('Email: john.wick@test.com');
    expect(compiled.querySelector('p:nth-child(3)').textContent).toContain('You are admin');
  });

  it('should display the delete button for non-admin user', () => {
    component.user = mockUser;
    fixture.detectChanges();

    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('button')).toBeTruthy();
  });

  it('should call back method when back button is clicked', () => {
    jest.spyOn(component, 'back');
    const button = fixture.debugElement.nativeElement.querySelector('button[mat-icon-button]');
    button.click();
    expect(component.back).toBeCalled();
  });

  it('should call the delete method when delete button is clicked', () => {
    component.user = mockUser;
    fixture.detectChanges();
    
    jest.spyOn(component, 'delete');
    const button = fixture.debugElement.nativeElement.querySelector('button[mat-raised-button]');
    button.click();
    expect(component.delete).toBeCalled();
  });

  it('should fetch user data on initialization', () => {
    component.ngOnInit();
    fixture.detectChanges();

    expect(component.user).toEqual(mockAdmin)
  });

  it('should delete user and navigate to homepage on delete', () => {
    component.delete();
    fixture.detectChanges();

    expect(userService.delete).toBeCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(matSnackBar.open).toBeCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(sessionService.logOut).toBeCalled();
    expect(router.navigate).toBeCalledWith(['/']);
  });

});
