import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionService } from '../../../../services/session.service';
import { expect } from '@jest/globals';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { MatFormFieldModule } from '@angular/material/form-field';
import { HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { AppRoutingModule } from '../../../../app-routing.module'
import { Location } from '@angular/common';

describe('FormComponent Integration Tests', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let location: Location;

  const mockSession: Session = {
    id: 1,
    name: 'Yoga Session 1',
    description: 'Yoga Session 1 Description',
    date: new Date('2024-07-16'),
    teacher_id: 1,
    users: [],
    createdAt: new Date('2024-07-16'),
    updatedAt: new Date('2024-07-16')
  };

  const mockTeacher1: Teacher = {
    id: 1,
    lastName: "Teacher 1",
    firstName: "Yoga 1",
    createdAt: new Date('2024-07-16'),
    updatedAt: new Date('2024-07-16')
  }

  const mockTeacher2: Teacher = {
    id: 2,
    lastName: "Teacher 2",
    firstName: "Yoga 2",
    createdAt: new Date('2024-07-16'),
    updatedAt: new Date('2024-07-16')
  }

  const mockTeachers = [mockTeacher1, mockTeacher2];

  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({ id: '1' })
    }
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn(),
    createUrlTree: jest.fn(),
    navigateByUrl: jest.fn(),
    url: '/sessions/create'
  };

  const mockSessionService = {
    sessionInformation: { 
        admin: true, 
        id: 1 
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        RouterTestingModule.withRoutes([]),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        AppRoutingModule
      ],
      providers: [
        { provide: SessionApiService, useValue: { detail: jest.fn(), create: jest.fn(), update: jest.fn() } },
        { provide: TeacherService, useValue: { all: jest.fn().mockReturnValue(of(mockTeachers)) } },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);

    jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form in create mode', () => {
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: ''
    });
    expect(component.onUpdate).toBe(false);
  });

  it('should submit the form in create mode', () => {
    jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));

    component.sessionForm?.setValue({
      name: 'Test Session',
      date: '2024-07-16',
      teacher_id: 1,
      description: 'Test Description'
    });

    component.submit();

    expect(sessionApiService.create).toBeCalledWith({
      name: 'Test Session',
      date: '2024-07-16',
      teacher_id: 1,
      description: 'Test Description'
    });
    
    expect(matSnackBar.open).toBeCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(router.navigate).toBeCalledWith(['sessions']);
  });

  it('should initialize the form in update mode', async () => {
    mockRouter.url = '/sessions/update/1';
    component.ngOnInit();
    fixture.detectChanges();

    expect(component.onUpdate).toBe(true);
    expect(sessionApiService.detail).toBeCalledWith('1');
    expect(component.sessionForm?.value).toEqual({
      name: mockSession.name,
      date: '2024-07-16',
      teacher_id: mockSession.teacher_id,
      description: mockSession.description
    });
  });

  it('should validate the form fields', () => {
    const nameControl = component.sessionForm?.get('name');
    const dateControl = component.sessionForm?.get('date');
    const teacherIdControl = component.sessionForm?.get('teacher_id');
    const descriptionControl = component.sessionForm?.get('description');

    nameControl?.setValue('');
    dateControl?.setValue('');
    teacherIdControl?.setValue('');
    descriptionControl?.setValue('');

    expect(nameControl?.valid).toBe(false);
    expect(dateControl?.valid).toBe(false);
    expect(teacherIdControl?.valid).toBe(false);
    expect(descriptionControl?.valid).toBe(false);
  });

  it('should submit the form in update mode', () => {
    mockRouter.url = '/sessions/update/1';
    component.ngOnInit();
    fixture.detectChanges();

    jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockSession));

    component.sessionForm?.setValue({
      name: 'Updated Session',
      date: '2024-07-16',
      teacher_id: 1,
      description: 'Updated Description'
    });

    component.submit();

    expect(sessionApiService.update).toBeCalledWith('1', {
      name: 'Updated Session',
      date: '2024-07-16',
      teacher_id: 1,
      description: 'Updated Description'
    });

    expect(matSnackBar.open).toBeCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(router.navigate).toBeCalledWith(['sessions']);
  });

  it('should navigate back when the back button is clicked', () => {
    const backButton = fixture.nativeElement.querySelector('button[routerLink="/sessions"]');
    backButton.click();
    fixture.detectChanges();
    // Session module default route
    expect(location.path()).toBe('');
  });

  it('should redirect non-admin user to /sessions on init', () => {
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    fixture.detectChanges();

    expect(router.navigate).toBeCalledWith(['/sessions']);
  });

});