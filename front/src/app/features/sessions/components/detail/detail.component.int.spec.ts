import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionService } from '../../../../services/session.service';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { expect } from '@jest/globals';

describe('DetailComponent Integration Tests', () => {
    let component: DetailComponent;
    let fixture: ComponentFixture<DetailComponent>;
    let sessionApiService: SessionApiService;
    let teacherService: TeacherService;
    let sessionService: SessionService;
    let matSnackBar: MatSnackBar;
    let router: Router;

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
  
    const mockSession = {
      id: 1,
      name: 'Yoga Session 1',
      description: 'Yoga Session 1 Description',
      date: new Date('2024-07-10'),
      teacher_id: 1,
      users: [],
      createdAt: new Date('2024-07-10'),
      updatedAt: new Date('2024-07-10')
    };
  
    const mockTeacher = {
      id: 1,
      firstName: 'Yoga',
      lastName: 'Teacher',
      createdAt: new Date('2024-07-10'),
      updatedAt: new Date('2024-07-10')
    };
  
    const mockActivatedRoute = {
      snapshot: {
        paramMap: convertToParamMap({ id: '1' })
      }
    };
  
    const mockMatSnackBar = {
      open: jest.fn()
    };

    const mockRouter = {
        navigate: jest.fn()
    }
  
    describe('Admin User', () => {

        beforeEach(async () => {
            await TestBed.configureTestingModule({
              declarations: [DetailComponent],
              imports: [
                ReactiveFormsModule,
                RouterTestingModule,
                MatCardModule,
                MatButtonModule,
                MatIconModule,
                MatProgressSpinnerModule,
                BrowserAnimationsModule
              ],
              providers: [
                { provide: SessionApiService, useValue: { detail: jest.fn(), delete: jest.fn(), participate: jest.fn(), unParticipate: jest.fn() } },
                { provide: TeacherService, useValue: { detail: jest.fn().mockReturnValue(of(mockTeacher)) } },
                { provide: SessionService, useValue: mockSessionServiceAdmin },
                { provide: ActivatedRoute, useValue: mockActivatedRoute },
                { provide: MatSnackBar, useValue: mockMatSnackBar },
                { provide: Router, useValue: mockRouter }
              ]
            }).compileComponents();
        
            fixture = TestBed.createComponent(DetailComponent);
            component = fixture.componentInstance;
            sessionApiService = TestBed.inject(SessionApiService);
            teacherService = TestBed.inject(TeacherService);
            sessionService = TestBed.inject(SessionService);
            matSnackBar = TestBed.inject(MatSnackBar);
            router = TestBed.inject(Router);
        
            jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
            fixture.detectChanges();
          });
      it('should create', () => {
        expect(component).toBeTruthy();
      });
  
      it('should fetch session details on init', () => {
        expect(sessionApiService.detail).toBeCalledWith('1');
        expect(component.session).toEqual(mockSession);
        expect(teacherService.detail).toBeCalledWith('1');
        expect(component.teacher).toEqual(mockTeacher);
      });
  
      it('should delete session', () => {
        const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of({}));
        const snackBarOpenSpy = jest.spyOn(matSnackBar, 'open');
  
        component.delete();
  
        expect(deleteSpy).toBeCalledWith('1');
        expect(snackBarOpenSpy).toBeCalledWith('Session deleted !', 'Close', { duration: 3000 });
      });
  
      it('should navigate back', () => {
        jest.spyOn(window.history, 'back');
        component.back();
        expect(window.history.back).toBeCalled();
      });
  
    });
  
    describe('Non Admin User', () => {
  
        beforeEach(async () => {
            await TestBed.configureTestingModule({
                declarations: [DetailComponent],
                imports: [
                    ReactiveFormsModule,
                    RouterTestingModule,
                    MatCardModule,
                    MatButtonModule,
                    MatIconModule,
                    MatProgressSpinnerModule,
                    BrowserAnimationsModule
                ],
                providers: [
                    { provide: SessionApiService, useValue: { detail: jest.fn(), delete: jest.fn(), participate: jest.fn(), unParticipate: jest.fn() } },
                    { provide: TeacherService, useValue: { detail: jest.fn().mockReturnValue(of(mockTeacher)) } },
                    { provide: SessionService, useValue: mockSessionServiceNonAdmin },
                    { provide: ActivatedRoute, useValue: mockActivatedRoute },
                    { provide: MatSnackBar, useValue: mockMatSnackBar }
                ]
            }).compileComponents();
        
            fixture = TestBed.createComponent(DetailComponent);
            component = fixture.componentInstance;
            sessionApiService = TestBed.inject(SessionApiService);
            teacherService = TestBed.inject(TeacherService);
            sessionService = TestBed.inject(SessionService);
            matSnackBar = TestBed.inject(MatSnackBar);
        
            jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
            fixture.detectChanges();
        });

        it('should create', () => {
        expect(component).toBeTruthy();
        });

        it('should fetch session details on init', () => {
        expect(sessionApiService.detail).toBeCalledWith('1');
        expect(component.session).toEqual(mockSession);
        expect(teacherService.detail).toBeCalledWith('1');
        expect(component.teacher).toEqual(mockTeacher);
        });

        it('should participate in session', () => {
        const participateSpy = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(undefined));
        component.participate();

        expect(participateSpy).toBeCalledWith('1', '1');
        expect(sessionApiService.detail).toBeCalled();
        });

        it('should unparticipate in session', () => {
        const unParticipateSpy = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(undefined));
        component.unParticipate();

        expect(unParticipateSpy).toBeCalledWith('1', '1');
        expect(sessionApiService.detail).toBeCalled();
        });

        it('should navigate back', () => {
        jest.spyOn(window.history, 'back');
        component.back();
        expect(window.history.back).toBeCalled();
        });
  
    });
  
  });