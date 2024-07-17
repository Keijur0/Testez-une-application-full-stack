import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ListComponent } from './list.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { FormComponent } from '../form/form.component';
import { DetailComponent } from '../detail/detail.component';

describe('ListComponent Integration', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let router: Router;

  const mockSession1 = {
    id: 1,
    name: 'Yoga Session 1',
    description: 'Description 1',
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSession2 = {
    id: 2,
    name: 'Yoga Session 2',
    description: 'Description 2',
    date: new Date(),
    teacher_id: 2,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };
  
  const mockSessions: Session[] = [mockSession1, mockSession2];

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
        declarations: [ListComponent, FormComponent, DetailComponent],
        imports: [
          RouterTestingModule.withRoutes([
            { path: 'create', component: FormComponent },
            { path: 'detail/:id', component: DetailComponent },
            { path: 'update/:id', component: FormComponent }
          ]),
          MatCardModule,
          MatIconModule,
          MatButtonModule,
          BrowserAnimationsModule
        ],
        providers: [
          { provide: SessionApiService, useValue: { all: jest.fn().mockReturnValue(of(mockSessions)) } },
          { provide: SessionService, useValue: mockSessionService }
        ]
      }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render the sessions', () => {
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements.length).toBe(mockSessions.length);
    expect(sessionElements[0].querySelector('mat-card-title').textContent).toContain(mockSessions[0].name);
    expect(sessionElements[1].querySelector('mat-card-title').textContent).toContain(mockSessions[1].name);
  });

  it('should display Create button if user is admin', () => {
    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeTruthy();
  });

  it('should navigate to create page when "Create" button is clicked', async () => {
    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    createButton.click();

    await fixture.whenStable();

    expect(router.url).toBe('/create');
  });

  it('should display Edit buttons if user is admin', () => {
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements[0].querySelectorAll('span.ml1')[1].textContent).toContain("Edit");
    expect(sessionElements[1].querySelectorAll('span.ml1')[1].textContent).toContain("Edit");
  });

  it('should navigate to session edit when Edit button is clicked', async () => {
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    const editButton = sessionElements[0].querySelectorAll('button')[1];
    editButton.click();

    await fixture.whenStable();

    expect(router.url).toBe(`/update/${mockSessions[0].id}`);
  });

  it('should not display Edit buttons if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements[0].querySelectorAll('span.ml1')[1]).toBeUndefined();
    expect(sessionElements[1].querySelectorAll('span.ml1')[1]).toBeUndefined();
  });

  it('should not display Create button if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();

    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeNull();
  });

  it('should navigate to session details when Detail button is clicked', async () => {
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    const detailButton = sessionElements[0].querySelectorAll('button')[0];
    detailButton.click();

    await fixture.whenStable();

    expect(router.url).toBe(`/detail/${mockSessions[0].id}`);
  });
});