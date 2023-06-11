import { DateFormatPipe } from './date-format.pipe';

describe('DateFormatPipe', () => {
  it('create an instance', () => {
    const pipe = new DateFormatPipe();
    expect(pipe).toBeTruthy();
  });

  it('should return a formatted date string in Brasília time', () => {
    const dateString = '2023-06-10T15:00:00.000000';
    const expectedResult = '10 de junho de 2023 às 12:00';

    const pipe = new DateFormatPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform(dateString)).toEqual(expectedResult);
  });
});
