import { TestBed } from '@angular/core/testing';

import { StorageService } from './storage.service';
import { PLATFORM_ID } from '@angular/core';

describe('StorageService', () => {
  let service: StorageService;

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      providers: [{ provide: PLATFORM_ID, useFactory: () => 'browser' }],
    }).compileComponents();
  });

  it('should be created', () => {
    service = TestBed.inject(StorageService);
    expect(service).toBeTruthy();
  });

  it('should call localStorage clear when platform is browser', () => {
    service = TestBed.inject(StorageService);
    const localStorageClearSpy = spyOn(window.localStorage, 'clear').and.callThrough();

    const [key, value] = ['some_key', 'some_value'];
    localStorage.setItem(key, value);

    service.clear();

    expect(localStorageClearSpy).toHaveBeenCalledTimes(1);
    expect(localStorage.getItem(key)).toBeNull();
  });

  it('should not call localStorage clear when platform is server', () => {
    TestBed.overrideProvider(PLATFORM_ID, { useFactory: () => 'server' });
    service = TestBed.inject(StorageService);
    const localStorageClearSpy = spyOn(window.localStorage, 'clear');

    service.clear();

    expect(localStorageClearSpy).toHaveBeenCalledTimes(0);
  });

  it('should call localStorage getItem when platform is browser', () => {
    service = TestBed.inject(StorageService);
    const localStorageGetItemSpy = spyOn(window.localStorage, 'getItem').and.callThrough();

    const [key, value] = ['some_key', 'some_value'];
    localStorage.setItem(key, value);

    const expectedValue = service.getItem(key);

    expect(localStorageGetItemSpy).toHaveBeenCalledOnceWith(key);
    expect(expectedValue).toBe(value);
  });

  it('should call localStorage getItem when platform is browser and get null return', () => {
    service = TestBed.inject(StorageService);
    const localStorageGetItemSpy = spyOn(window.localStorage, 'getItem').and.callThrough();

    const key = 'some_key';

    const expectedValue = service.getItem(key);

    expect(localStorageGetItemSpy).toHaveBeenCalledOnceWith(key);
    expect(expectedValue).toBeNull();
  });

  it('should not call localStorage getItem when platform is server and get null return', () => {
    TestBed.overrideProvider(PLATFORM_ID, { useFactory: () => 'server' });
    service = TestBed.inject(StorageService);
    const localStorageGetItemSpy = spyOn(window.localStorage, 'getItem').and.callThrough();

    const key = 'some_key';

    const expectedValue = service.getItem(key);

    expect(localStorageGetItemSpy).toHaveBeenCalledTimes(0);
    expect(expectedValue).toBeNull();
  });

  it('should call localStorage removeItem when platform is browser', () => {
    service = TestBed.inject(StorageService);
    const localStorageRemoveItemSpy = spyOn(window.localStorage, 'removeItem').and.callThrough();

    const key = 'some_key';
    localStorage.setItem(key, 'some_value');

    service.removeItem(key);

    expect(localStorageRemoveItemSpy).toHaveBeenCalledOnceWith(key);
    expect(localStorage.getItem(key)).toBeNull();
  });

  it('should not call localStorage removeItem when platform is server', () => {
    TestBed.overrideProvider(PLATFORM_ID, { useFactory: () => 'server' });
    service = TestBed.inject(StorageService);
    const localStorageRemoveItemSpy = spyOn(window.localStorage, 'removeItem').and.callThrough();

    const key = 'some_key';

    service.removeItem(key);

    expect(localStorageRemoveItemSpy).toHaveBeenCalledTimes(0);
  });

  it('should call localStorage setItem when platform is browser', () => {
    service = TestBed.inject(StorageService);
    const localStorageSetItemSpy = spyOn(window.localStorage, 'setItem').and.callThrough();

    const [key, value] = ['some_key', 'some_value'];

    service.setItem(key, value);

    expect(localStorageSetItemSpy).toHaveBeenCalledOnceWith(key, value);
    expect(localStorage.getItem(key)).toBe(value);
  });

  it('should not call localStorage setItem when platform is server', () => {
    TestBed.overrideProvider(PLATFORM_ID, { useFactory: () => 'server' });
    service = TestBed.inject(StorageService);
    const localStorageSetItemSpy = spyOn(window.localStorage, 'setItem').and.callThrough();

    const [key, value] = ['some_key', 'some_value'];

    service.setItem(key, value);

    expect(localStorageSetItemSpy).toHaveBeenCalledTimes(0);
    expect(localStorage.getItem(key)).toBeNull();
  });
});
