import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let mockHttp: HttpTestingController;

  const mockDate = new Date();

  const mockTeachers: Teacher[] = [
  {
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: mockDate,
    updatedAt: mockDate
  },
  {
    id: 2,
    lastName: 'Doe',
    firstName: 'Jane',
    createdAt: mockDate,
    updatedAt: mockDate
  }
]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    mockHttp = TestBed.inject(HttpTestingController);
    
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('it should list all teachers', () => {
    service.all().subscribe(teachers => {
      expect(teachers.length).toBe(2);
      expect(teachers).toEqual(mockTeachers);
    });

    const req = mockHttp.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should show teacher details by id', () => {
    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeachers[1])
    });

    const req = mockHttp.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers[1]);
  });

});
