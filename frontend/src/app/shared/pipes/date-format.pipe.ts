import { Pipe, PipeTransform } from '@angular/core';

import { format, sub } from 'date-fns';
import { ptBR } from 'date-fns/locale';

@Pipe({
  name: 'dateFormat',
  standalone: true,
})
export class DateFormatPipe implements PipeTransform {
  readonly DATE_PATTERN = "dd 'de' MMMM 'de' yyyy 'às' HH:mm";

  transform(dateString: string): string {
    return format(this.getDateInBrasiliaTime(dateString), this.DATE_PATTERN, { locale: ptBR });
  }

  private getDateInBrasiliaTime(dateString: string): Date {
    return sub(new Date(dateString), { hours: 3 });
  }
}
