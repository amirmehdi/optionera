const DateTime = {

     monthNames : ['فروردین', 'اردیبهشت', 'خرداد', 'تیر', 'مرداد', 'شهریور', 'مهر', 'آبان', 'آذر', 'دی', 'بهمن', 'اسفند'],
     weekNames : ['شنبه', 'یکشنبه', 'دوشنبه', 'سه‌شنبه', 'چهارشنبه', 'پنجشنبه', 'جمعه'],

    JalaliDate : {
      // eslint-disable-next-line @typescript-eslint/camelcase
        g_days_in_month : [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
      // eslint-disable-next-line @typescript-eslint/camelcase
        j_days_in_month : [31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29]
    },

    getJalaliMonthFirstWeekDay(y,m,d) {
      // eslint-disable-next-line prefer-const
        const kabisCount = Math.floor((y - 1392) / 4),
            newDay = (y - 1392 + kabisCount + 5) % 7;

        return   (31 * (m - 1) + (m - 7 > 0 ? 7 - m : 0) + newDay + d - 1) % 7;
    },

  // eslint-disable-next-line @typescript-eslint/camelcase
    gregorianToJalali(g_y, g_m, g_d) {
      // eslint-disable-next-line @typescript-eslint/camelcase
        const g_days_in_month = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
      // eslint-disable-next-line @typescript-eslint/camelcase
        const j_days_in_month = [31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29];

      // eslint-disable-next-line @typescript-eslint/camelcase
        const gy = g_y - 1600;
      // eslint-disable-next-line @typescript-eslint/camelcase
        const gm = g_m - 1;
      // eslint-disable-next-line @typescript-eslint/camelcase
        const gd = g_d - 1;

      // eslint-disable-next-line @typescript-eslint/camelcase
        let g_day_no = 365 * gy + Math.floor((gy + 3) / 4) - Math.floor((gy + 99) / 100) + Math.floor((gy + 399) / 400);

        for (let i = 0; i < gm; ++i)
          // eslint-disable-next-line @typescript-eslint/camelcase
          g_day_no += g_days_in_month[i];
        if (gm > 1 && ((gy % 4 === 0 && gy % 100 !== 0) || (gy % 400 === 0)))
        /* leap and after Feb */
          // eslint-disable-next-line @typescript-eslint/camelcase
          ++g_day_no;
      // eslint-disable-next-line @typescript-eslint/camelcase
      g_day_no += gd;
      // eslint-disable-next-line @typescript-eslint/camelcase
        let j_day_no = g_day_no - 79;
      // eslint-disable-next-line @typescript-eslint/camelcase
        const j_np = Math.floor(j_day_no / 12053);
      // eslint-disable-next-line @typescript-eslint/camelcase
      j_day_no %= 12053;
      // eslint-disable-next-line @typescript-eslint/camelcase
        let jy = 979 + 33 * j_np + 4 * Math.floor(j_day_no / 1461);
      // eslint-disable-next-line @typescript-eslint/camelcase
        j_day_no %= 1461;
      // eslint-disable-next-line @typescript-eslint/camelcase
        if (j_day_no >= 366) {
          // eslint-disable-next-line @typescript-eslint/camelcase
          jy += Math.floor((j_day_no - 1) / 365);
          // eslint-disable-next-line @typescript-eslint/camelcase
          j_day_no = (j_day_no - 1) % 365;
        }
      // eslint-disable-next-line @typescript-eslint/camelcase,no-var
        for (var i = 0; i < 11 && j_day_no >= j_days_in_month[i]; ++i) {
          // eslint-disable-next-line @typescript-eslint/camelcase
          j_day_no -= j_days_in_month[i];
        }
        const jm = i + 1;
      // eslint-disable-next-line @typescript-eslint/camelcase
      const jd = j_day_no + 1;

        return { day: jd ,month: jm,year: jy};
    },
    stringToDate(dateTime) {
        const [date, time] = dateTime.split('T');
      // eslint-disable-next-line radix
        const sp1 = date.split('-').map(v => parseInt(v));
      // eslint-disable-next-line radix
        const sp2 = time ? time.split('.')[0].split(':').map(v => parseInt(v)) : [0, 0, 0];
        return { year: sp1[0], month: sp1[1], day: sp1[2],hour: sp2[0], minute: sp2[1] , second: sp2[2] }
    },



};

export default DateTime



