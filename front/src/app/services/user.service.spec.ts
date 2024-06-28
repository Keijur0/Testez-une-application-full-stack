import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let mockHttp: HttpTestingController;

  const mockDate = new Date();

  const mockUser = {
    id: 1,
    email: 'john.wick@test.com',
    lastName: 'Wick',
    firstName: 'John',
    admin: true,
    password: 'test!1234',
    createdAt: mockDate,
    updatedAt: mockDate
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers:[
        UserService
      ]
    });
    service = TestBed.inject(UserService);
    mockHttp = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    mockHttp.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get a user by id', () => {
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = mockHttp.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should delete a user by id', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull();
    });

    const req = mockHttp.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

});
