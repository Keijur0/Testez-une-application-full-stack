import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('Session Api Service', () => {
  let service: SessionApiService;
  let mockHttp: HttpTestingController;

  const mockDate = new Date();

  const mockSession1: Session = {
    id: 1,
    name: 'Yoga Session 1',
    description: 'Yoga Session 1 Description',
    date: mockDate,
    teacher_id: 1,
    users: [1, 2],
    createdAt: mockDate,
    updatedAt: mockDate
  };

  const mockSession2 = {
    id: 1,
    name: 'Yoga Session 2',
    description: 'Yoga Session 2 Description',
    date: mockDate,
    teacher_id: 2,
    users: [],
    createdAt: mockDate,
    updatedAt: mockDate
  };

  const mockSessions: Session[] = [mockSession1, mockSession2];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    mockHttp = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    mockHttp.verify();
  })

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return all sessions', () => {
    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });
    const req = mockHttp.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should return a session details', () => {
    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession1);
    });
    const req = mockHttp.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession1);
  });

  it('should delete a session', () => {
    service.delete('1').subscribe(response => {
      expect(response).toEqual({});
    });
    const req = mockHttp.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a session', () => {
    service.create(mockSession2).subscribe(session => {
      expect(session).toEqual(mockSession2);
    });
    const req = mockHttp.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession2);
  });

  it('should update a session', () => {
    const mockSession1Update: Session = {
      id: 1,
      name: 'Update Yoga Session 1',
      description: 'Update Yoga Session 1 Description',
      date: mockDate,
      teacher_id: 1,
      users: [1, 2],
      createdAt: mockDate,
      updatedAt: mockDate
    };
  
    service.update('1', mockSession1Update).subscribe(session => {
      expect(session).toEqual(mockSession1Update);
    });
    const req = mockHttp.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(mockSession1Update);
  });

  it('should add participation to a session', () => {
    service.participate('2', '1').subscribe(response => {
      expect(response).toEqual({});
    });
    const req = mockHttp.expectOne('api/session/2/participate/1');
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should cancel participation to a session', () => {
    service.unParticipate('1', '1').subscribe(response => {
      expect(response).toEqual({});
    });
    const req = mockHttp.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
