import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionServiceAdmin = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionServiceNonAdmin = {
    sessionInformation: {
      admin: false
    }
  };

  const mockRouterCreate = {
    navigate: jest.fn(),
    url: '/sessions/create'
  };

  const mockRouterUpdate = {
    navigate: jest.fn(),
    url: '/sessions/update/1'
  };


  const mockSessionDetail = {
    name: 'Yoga Session 1',
    date: '2024-07-10',
    teacher_id: '1',
    description: 'Yoga Session 1 Description'
  };

  const mockActivatedRouteCreate = {
    snapshot: {
      paramMap: {
        get: jest.fn()
      }
    }
  };

  const mockActivatedRouteUpdate = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([]))
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of({})),
    detail: jest.fn().mockReturnValue(of(mockSessionDetail)),
    update: jest.fn().mockReturnValue(of({}))
  };

  describe('Update form', () => {
    const mockSessionUpdate = {
      name: 'Yoga Session 1 Update',
      date: '2024-07-11',
      teacher_id: '1',
      description: 'Yoga Session 1 Description Update'      
    };
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [FormComponent],
        imports: [
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          ReactiveFormsModule,
          MatSnackBarModule,
          MatSelectModule,
          BrowserAnimationsModule
        ],
        providers: [
          { provide: SessionService, useValue: mockSessionServiceAdmin },
          { provide: SessionApiService, useValue: mockSessionApiService },
          { provide: ActivatedRoute, useValue: mockActivatedRouteUpdate },
          { provide: Router, useValue: mockRouterUpdate },
          { provide: TeacherService, useValue: mockTeacherService },
          { provide: MatSnackBar, useValue: mockMatSnackBar}
        ]
      })
      .compileComponents();
  
      fixture = TestBed.createComponent(FormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  
    it('should load current session data when updating', () => {
      expect(mockSessionApiService.detail).toBeCalledWith('1');
      expect(component.sessionForm?.get('name')?.value).toBe(mockSessionDetail.name);
      expect(component.sessionForm?.get('date')?.value).toBe(mockSessionDetail.date);
      expect(component.sessionForm?.get('description')?.value).toBe(mockSessionDetail.description);
    });
  
    it('should call update API when the form is submitted for update', () => {
      component.onUpdate = true;
      component.sessionForm?.patchValue(mockSessionUpdate);
      component.submit();
      expect(mockSessionApiService.update).toBeCalledWith('1', mockSessionUpdate);
      expect(mockMatSnackBar.open).toBeCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(mockRouterUpdate.navigate).toBeCalledWith(['sessions']);
    });
  });

  describe('Create Form', () => {
    const mockSessionCreation = {
      name: 'Yoga Session 1',
      date: '2024-07-10',
      teacher_id: '1',
      description: 'Yoga Session 1 Description'
    };
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [FormComponent],
        imports: [
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          ReactiveFormsModule,
          MatSnackBarModule,
          MatSelectModule,
          BrowserAnimationsModule
        ],
        providers: [
          { provide: SessionService, useValue: mockSessionServiceAdmin },
          { provide: SessionApiService, useValue: mockSessionApiService },
          { provide: ActivatedRoute, useValue: mockActivatedRouteCreate },
          { provide: Router, useValue: mockRouterCreate },
          { provide: TeacherService, useValue: mockTeacherService },
          { provide: MatSnackBar, useValue: mockMatSnackBar}
        ]
      })
      .compileComponents();
  
      fixture = TestBed.createComponent(FormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should load an empty form when creating', () => {
      expect(component.sessionForm?.get('name')?.value).toBe('');
      expect(component.sessionForm?.get('date')?.value).toBe('')
      expect(component.sessionForm?.get('description')?.value).toBe('');
    });

    it('should call create API when the form is submitted for creation', () => {
      component.sessionForm?.patchValue(mockSessionCreation);
      component.submit();
      expect(mockSessionApiService.create).toBeCalledWith(mockSessionCreation);
      expect(mockMatSnackBar.open).toBeCalledWith('Session created !', 'Close', { duration: 3000});
      expect(mockRouterCreate.navigate).toBeCalledWith(['sessions']);
    });
  });

  describe('Non Admin user', () => {
    describe('Update Form', () => {
      beforeEach(async () => {
        await TestBed.configureTestingModule({
    
          imports: [
            RouterTestingModule,
            HttpClientModule,
            MatCardModule,
            MatIconModule,
            MatFormFieldModule,
            MatInputModule,
            ReactiveFormsModule, 
            MatSnackBarModule,
            MatSelectModule,
            BrowserAnimationsModule
          ],
          providers: [
            { provide: SessionService, useValue: mockSessionServiceNonAdmin },
            { provide: Router, useValue: mockRouterUpdate },
            { provide: SessionApiService, useValue: mockSessionApiService },
            { provide: ActivatedRoute, useValue: mockActivatedRouteUpdate }
          ],
          declarations: [FormComponent]
        })
          .compileComponents();
    
        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
      });
    
      it('should create', () => {
        expect(component).toBeTruthy();
      });
  
      it('should redirect to sessions page on init', () => {
        expect(mockRouterUpdate.navigate).toBeCalledWith(['/sessions']);
      });
    });

    describe('Create Form', () => {
      beforeEach(async () => {
        await TestBed.configureTestingModule({
    
          imports: [
            RouterTestingModule,
            HttpClientModule,
            MatCardModule,
            MatIconModule,
            MatFormFieldModule,
            MatInputModule,
            ReactiveFormsModule, 
            MatSnackBarModule,
            MatSelectModule,
            BrowserAnimationsModule
          ],
          providers: [
            { provide: SessionService, useValue: mockSessionServiceNonAdmin },
            { provide: Router, useValue: mockRouterCreate },
            { provide: SessionApiService, useValue: mockSessionApiService },
            { provide: ActivatedRoute, useValue: mockActivatedRouteCreate }
          ],
          declarations: [FormComponent]
        })
          .compileComponents();
    
        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
      });
    
      it('should create', () => {
        expect(component).toBeTruthy();
      });
  
      it('should redirect to sessions page on init', () => {
        expect(mockRouterCreate.navigate).toBeCalledWith(['/sessions']);
      });
    });
  });
});
