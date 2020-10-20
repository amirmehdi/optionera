import * as React from "react";
import { useEffect, useState } from "react";
import "./DatePicker.scss";
import moment from 'jalali-moment';
import WeekRow from "./WeekRow";

// eslint-disable-next-line @typescript-eslint/class-name-casing
export interface dateType {
  day: number;
  month: number;
  year: number;
}

export const monthNameConverter = (month: number) => {
  if (month === 1) {
    return "فروردین";
  } else if (month === 2) {
    return "اردیبهشت";
  } else if (month === 3) {
    return "خرداد";
  } else if (month === 4) {
    return "تیر";
  } else if (month === 5) {
    return "مرداد";
  } else if (month === 6) {
    return "شهریور";
  } else if (month === 7) {
    return "مهر";
  } else if (month === 8) {
    return "آبان";
  } else if (month === 9) {
    return "آذر";
  } else if (month === 10) {
    return "دی";
  } else if (month === 11) {
    return "بهمن";
  } else if (month === 12) {
    return "اسفند";
  }
};

const date = moment().format("jYYYY/jM/jD");
const m = moment(date, "jYYYY/jM/jD");
const startDay = m.jDate();
const startMonth = m.jMonth() + 1;
const startYear = m.jYear();

export default function DatePicker({
  twoSelect,
  value,
  getDate,
  enableOlderDate = false,
}: {
  twoSelect: boolean;
  value?: dateType[];
  getDate: (d: dateType[]) => void;
  enableOlderDate?: boolean;
}) {
  const [lastDayDate, setLastDayDate] = useState<number>(moment().endOf("jMonth").jDate());
  const [year, setYear] = useState<number>(startYear);
  const [month, setMonth] = useState<number>(startMonth);
  const [selectedItemIndex1, setSelectedItemIndex1] = useState<dateType>();
  const [selectedItemIndex2, setSelectedItemIndex2] = useState<dateType>();

  useEffect(() => {
    if (value) {
      if (twoSelect) {
        value[0] && setSelectedItemIndex1(value[0]);
        value[1] && setSelectedItemIndex2(value[1]);
      } else {
        setSelectedItemIndex1(value[0]);
        setSelectedItemIndex2(value[0]);
      }
    }
  }, [value]);

  function selectOneItem(x: number) {
    if (!twoSelect) {
      getDate([{ day: x, month, year }]);
      return;
    }
    if (!selectedItemIndex1 || (selectedItemIndex1 && selectedItemIndex2)) {
      const date1 = { day: x, month, year };
      setSelectedItemIndex1(date1);
      setSelectedItemIndex2(undefined);
      getDate([date1]);
      return;
    }
    const date2 = { day: x, month, year };
    // eslint-disable-next-line @typescript-eslint/no-use-before-define
    const cmp = compareDays(selectedItemIndex1, date2);
    if (cmp > 0) {
      getDate([selectedItemIndex1, date2]);
      setSelectedItemIndex2(date2);
    } else {
      getDate([date2, selectedItemIndex1]);
      setSelectedItemIndex2(selectedItemIndex1);
      setSelectedItemIndex1(date2);
    }
  }

  const onPreviousMonth = () => {
    const minusMonth = month - 1;
    const minusYear = year - 1;
    if (month === 1) {
      setMonth(12);
      setYear(year - 1);
      setLastDayDate(
        moment(minusYear + "/" + 12 + "/" + 1, "jYYYY/jM/jD")
          .endOf("jMonth")
          .jDate(),
      );
    } else {
      setMonth(month - 1);
      setLastDayDate(
        moment(year + "/" + minusMonth, "jYYYY/jM")
          .endOf("jMonth")
          .jDate(),
      );
    }
  };

  const onNextMonth = () => {
    const plusMonth = month + 1;
    const plusYear = year + 1;

    if (month === 12) {
      setMonth(1);
      setYear(year + 1);
      setLastDayDate(
        moment(plusYear + "/" + 1 + "/" + 1, "jYYYY/jM/jD")
          .endOf("jMonth")
          .jDate(),
      );
    } else {
      setMonth(month + 1);
      setLastDayDate(
        moment(year + "/" + plusMonth + "/" + 1, "jYYYY/jM/jD")
          .endOf("jMonth")
          .jDate(),
      );
    }
  };

  return (
    <div className="selection date-search-page-one">
      <div className="box">
        <div className="part1">
          <div className="btn">
            <div className="name">{year - 1}</div>
          </div>
          <div
            className="btn"
            onClick={() => {
              setYear(year - 1);
            }}
          >
            <div className="icon-part">
              <img alt="" style={{ height: 15 }} src={require("./right_arrow_2.svg")} />
            </div>
          </div>
          <div className="btn">
            <div className="name">{year}</div>
          </div>
          <div
            className="btn"
            onClick={() => {
              setYear(year + 1);
            }}
          >
            <div className="icon-part">
              <img alt="" style={{ height: 15 }} src={require("./left_arrow_2.svg")} />
            </div>
          </div>
          <div className="btn">
            <div className="name">{year + 1}</div>
          </div>
        </div>
        <div className="part1">
          <div className="btn">
            <div className="name">{monthNameConverter(month > 1 ? month - 1 : 12)}</div>
          </div>
          <div className="btn" onClick={onPreviousMonth}>
            <div className="icon-part">
              <img alt="" style={{ height: 15 }} src={require("./right_arrow_2.svg")} />
            </div>
          </div>
          <div className="btn">
            <div className="name">{monthNameConverter(month)}</div>
          </div>
          <div className="btn" onClick={onNextMonth}>
            <div className="icon-part">
              <img alt="" style={{ height: 15 }} src={require("./left_arrow_2.svg")} />
            </div>
          </div>
          <div className="btn">
            <div className="name">{monthNameConverter(month < 12 ? month + 1 : 1)}</div>
          </div>
        </div>
        <div className="part2">
          <div className="top-box">
            <span>ش</span>
            <span>ی</span>
            <span>د</span>
            <span>س</span>
            <span>چ</span>
            <span>پ</span>
            <span>ج</span>
          </div>
          <div className="main-box">
            <WeekRow
               // eslint-disable-next-line @typescript-eslint/no-use-before-define
              Days={generateDaysOfMonth(lastDayDate, year, month)[0].weekDayDate}
              selectOneItem={(x: number) => selectOneItem(x)}
              selectedItemIndex1={selectedItemIndex1}
              selectedItemIndex2={selectedItemIndex2}
              month={month}
              year={year}
              thisYear={startYear}
              thisMonth={startMonth}
              thisDay={startDay}
              enableOlderDate={enableOlderDate}
            />
            <WeekRow
              // eslint-disable-next-line @typescript-eslint/no-use-before-define
              Days={generateDaysOfMonth(lastDayDate, year, month)[1].weekDayDate}
              selectOneItem={(x: number) => selectOneItem(x)}
              selectedItemIndex1={selectedItemIndex1}
              selectedItemIndex2={selectedItemIndex2}
              month={month}
              year={year}
              thisYear={startYear}
              thisMonth={startMonth}
              thisDay={startDay}
              enableOlderDate={enableOlderDate}
            />
            <WeekRow
              // eslint-disable-next-line @typescript-eslint/no-use-before-define
              Days={generateDaysOfMonth(lastDayDate, year, month)[2].weekDayDate}
              selectOneItem={(x: number) => selectOneItem(x)}
              selectedItemIndex1={selectedItemIndex1}
              selectedItemIndex2={selectedItemIndex2}
              month={month}
              year={year}
              thisYear={startYear}
              thisMonth={startMonth}
              thisDay={startDay}
              enableOlderDate={enableOlderDate}
            />
            <WeekRow
              // eslint-disable-next-line @typescript-eslint/no-use-before-define
              Days={generateDaysOfMonth(lastDayDate, year, month)[3].weekDayDate}
              selectOneItem={(x: number) => selectOneItem(x)}
              selectedItemIndex1={selectedItemIndex1}
              selectedItemIndex2={selectedItemIndex2}
              month={month}
              year={year}
              thisYear={startYear}
              thisMonth={startMonth}
              thisDay={startDay}
              enableOlderDate={enableOlderDate}
            />
            <WeekRow
              // eslint-disable-next-line @typescript-eslint/no-use-before-define
              Days={generateDaysOfMonth(lastDayDate, year, month)[4].weekDayDate}
              selectOneItem={(x: number) => selectOneItem(x)}
              selectedItemIndex1={selectedItemIndex1}
              selectedItemIndex2={selectedItemIndex2}
              month={month}
              year={year}
              thisYear={startYear}
              thisMonth={startMonth}
              thisDay={startDay}
              enableOlderDate={enableOlderDate}
            />
            <WeekRow
              // eslint-disable-next-line @typescript-eslint/no-use-before-define
              Days={generateDaysOfMonth(lastDayDate, year, month)[5].weekDayDate}
              selectOneItem={(x: number) => selectOneItem(x)}
              selectedItemIndex1={selectedItemIndex1}
              selectedItemIndex2={selectedItemIndex2}
              month={month}
              year={year}
              thisYear={startYear}
              thisMonth={startMonth}
              thisDay={startDay}
              enableOlderDate={enableOlderDate}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export function compareDays(date1: dateType, date2: dateType): number {
  if (date1.year < date2.year) return 1;
  if (date2.year < date1.year) return -1;
  if (date1.month < date2.month) return 1;
  if (date2.month < date1.month) return -1;
  if (date1.day < date2.day) return 1;
  if (date2.day < date1.day) return -1;
  return 0;
}

const generateDaysOfMonth = (lastDayDate: number, year: number, month: number) => {
  const A1N = [];
  const A1D = [];

  const A2N = [];
  const A2D = [];

  const A3N = [];
  const A3D = [];

  const A4N = [];
  const A4D = [];

  const A5N = [];
  const A5D = [];

  const A6N = [];
  const A6D = [];

  for (let i = 1; i <= lastDayDate; i++) {
    const weekDayName = moment(year + "/" + month + "/" + i, "jYYYY/jM/jD").format("dddd");
    const weekDayDate = moment(year + "/" + month + "/" + i, "jYYYY/jM/jD").jDate();

    if (A1N.includes("Friday") === false) {
      A1N.push(weekDayName);
      A1D.push(weekDayDate);
    } else {
      if (A2N.includes("Friday") === false) {
        A2N.push(weekDayName);
        A2D.push(weekDayDate);
      } else {
        if (A3N.includes("Friday") === false) {
          A3N.push(weekDayName);
          A3D.push(weekDayDate);
        } else {
          if (A4N.includes("Friday") === false) {
            A4N.push(weekDayName);
            A4D.push(weekDayDate);
          } else {
            if (A5N.includes("Friday") === false) {
              A5N.push(weekDayName);
              A5D.push(weekDayDate);
            } else {
              if (A6N.includes("Friday") === false) {
                A6N.push(weekDayName);
                A6D.push(weekDayDate);
              }
            }
          }
        }
      }
    }
  }

  return [
    { weekDayName: A1N, weekDayDate: A1D },
    { weekDayName: A2N, weekDayDate: A2D },
    { weekDayName: A3N, weekDayDate: A3D },
    { weekDayName: A4N, weekDayDate: A4D },
    { weekDayName: A5N, weekDayDate: A5D },
    { weekDayName: A6N, weekDayDate: A6D },
  ];
};
