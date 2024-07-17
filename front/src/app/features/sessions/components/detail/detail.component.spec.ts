import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockSessionServiceAdmin = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockSessionServiceNonAdmin = {
    sessionInformation: {
      admin: false,
      id: 1
    }
  };

  const mockSessionUserDoesNotParticipate = {
    id: 1,
    name: 'Yoga Session 1',
    description: 'Yoga Session 1 Description',
    date: '2024-07-10',
    teacher_id: 1,
    users: [],
    createdAt: '2024-07-10',
    updatedAt: '2024-07-10'
  };

  const mockSessionUserParticipates = {
    id: 1,
    name: 'Yoga Session 1',
    description: 'Yoga Session 1 Description',
    date: '2024-07-10',
    teacher_id: 1,
    users: [1],
    createdAt: '2024-07-10',
    updatedAt: '2024-07-10'
  };

  const mockTeacher = {
    id: 1,
    lastName: 'Teacher',
    firstName: 'Yoga',
    createdAt: '2024-07-10',
    updatedAt: '2024-07-10'
  }

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  }

  describe('Admin User', () => {

    const mockSessionApiService = {
      delete: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(of(mockSessionUserDoesNotParticipate)),
      participate: jest.fn().mockReturnValue(of()),
      unparticipate: jest.fn().mockReturnValue(of()),
    };

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatButtonModule,
          MatIconModule,
          MatProgressSpinnerModule,
          BrowserAnimationsModule
        ],
        declarations: [DetailComponent], 
        providers: [
          { provide: SessionService, useValue: mockSessionServiceAdmin },
          { provide: ActivatedRoute, useValue: mockActivatedRoute },
          { provide: SessionApiService, useValue: mockSessionApiService },
          { provide: TeacherService, useValue: mockTeacherService },
          { provide: MatSnackBar, useValue: mockMatSnackBar },
          { provide: Router, useValue: mockRouter }
        ],
      })
        .compileComponents();
      service = TestBed.inject(SessionService);
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should fetch session on init', () => {
      expect(mockSessionApiService.detail).toBeCalledWith('1');
      expect(component.session).toEqual(mockSessionUserDoesNotParticipate);
      expect(mockTeacherService.detail).toBeCalledWith('1');
      expect(component.teacher).toEqual(mockTeacher);
    });

    it('should delete session', () => {
      component.delete();
      expect(mockSessionApiService.delete).toBeCalledWith('1');
      expect(mockMatSnackBar.open).toBeCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toBeCalledWith(['sessions']);
    });

    it('should go back to previous page', () => {
      jest.spyOn(window.history, 'back');
      component.back();
      expect(window.history.back).toBeCalled();
    });

  });

  describe('Non Admin User Does Not Already Participate', () => {

    const mockSessionApiService = {
      delete: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(of(mockSessionUserDoesNotParticipate)),
      participate: jest.fn().mockReturnValue(of()),
      unparticipate: jest.fn().mockReturnValue(of()),
    };

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatButtonModule,
          MatIconModule,
          MatProgressSpinnerModule,
          BrowserAnimationsModule
        ],
        declarations: [DetailComponent], 
        providers: [
          { provide: SessionService, useValue: mockSessionServiceNonAdmin },
          { provide: ActivatedRoute, useValue: mockActivatedRoute },
          { provide: SessionApiService, useValue: mockSessionApiService },
          { provide: TeacherService, useValue: mockTeacherService },
          { provide: MatSnackBar, useValue: mockMatSnackBar },
          { provide: Router, useValue: mockRouter }
        ],
      })
        .compileComponents();
      service = TestBed.inject(SessionService);
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should fetch session on init', () => {
      expect(mockSessionApiService.detail).toBeCalledWith('1');
      expect(component.session).toEqual(mockSessionUserDoesNotParticipate);
      expect(mockTeacherService.detail).toBeCalledWith('1');
      expect(component.teacher).toEqual(mockTeacher);
    });

    it('should participate in session', () => {
      expect(component.isParticipate).toEqual(false);
      component.participate();
      expect(mockSessionApiService.participate).toBeCalledWith('1', '1');
      expect(mockSessionApiService.detail).toBeCalled();
    });

    it('should navigate back to previous page', () => {
      jest.spyOn(window.history, 'back');
      component.back();
      expect(window.history.back).toBeCalled();
    })

  });

  describe('Non Admin User Already Participates', () => {

    const mockSessionApiService = {
      delete: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(of(mockSessionUserParticipates)),
      participate: jest.fn().mockReturnValue(of()),
      unParticipate: jest.fn().mockReturnValue(of()),
    };

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatButtonModule,
          MatIconModule,
          MatProgressSpinnerModule,
          BrowserAnimationsModule
        ],
        declarations: [DetailComponent], 
        providers: [
          { provide: SessionService, useValue: mockSessionServiceNonAdmin },
          { provide: ActivatedRoute, useValue: mockActivatedRoute },
          { provide: SessionApiService, useValue: mockSessionApiService },
          { provide: TeacherService, useValue: mockTeacherService },
          { provide: MatSnackBar, useValue: mockMatSnackBar },
          { provide: Router, useValue: mockRouter }
        ],
      })
        .compileComponents();
      service = TestBed.inject(SessionService);
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should fetch session on init', () => {
      expect(mockSessionApiService.detail).toBeCalledWith('1');
      expect(component.session).toEqual(mockSessionUserParticipates);
      expect(mockTeacherService.detail).toBeCalledWith('1');
      expect(component.teacher).toEqual(mockTeacher);
    });

    it('should cancel participation in session', () => {
      expect(component.isParticipate).toEqual(true);
      component.unParticipate();
      expect(mockSessionApiService.unParticipate).toBeCalledWith('1', '1');
      expect(mockSessionApiService.detail).toBeCalled();
    });

    it('should navigate back to previous page', () => {
      jest.spyOn(window.history, 'back');
      component.back();
      expect(window.history.back).toBeCalled();
    })

  });

});

