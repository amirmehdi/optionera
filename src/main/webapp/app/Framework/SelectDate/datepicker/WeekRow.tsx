import * as React from "react";
import "./DatePicker.scss";
import { compareDays, dateType } from "./DatePicker";

// eslint-disable-next-line @typescript-eslint/class-name-casing
export interface props {
  Days: number[];
  selectOneItem: (item: number) => any;
  selectedItemIndex1?: dateType;
  selectedItemIndex2?: dateType;
  year: number;
  month: number;
  thisYear: number;
  thisMonth: number;
  thisDay: number;
  enableOlderDate: boolean;
}

function checkDay(year: number, month: number, day: number, i1?: dateType, i2?: dateType) {
  if (!i1) return false;
  if (!i2) return compareDays({ day, month, year }, i1) === 0;
  if (compareDays(i2, i1) >= 0) {
    return compareDays({ day, month, year }, i1) >= 0 && compareDays(i2, { day, month, year }) >= 0;
  } else {
    return compareDays({ day, month, year }, i2) >= 0 && compareDays(i1, { day, month, year }) >= 0;
  }
}

function checkFirst(year: number, month: number, day: number, i1?: dateType, i2?: dateType) {
  if (!i1 || !i2) return false;
  if (compareDays(i2, i1) >= 0) {
    return i1.day === day && i1.month === month && i1.year === year;
  } else {
    return i2.day === day && i2.month === month && i2.year === year;
  }
}

function checkLast(year: number, month: number, day: number, i1?: dateType, i2?: dateType) {
  if (!i1 || !i2) return false;
  if (compareDays(i2, i1) >= 0) {
    return i2.day === day && i2.month === month && i2.year === year;
  } else {
    return i1.day === day && i1.month === month && i1.year === year;
  }
}

const WeekRow = (Props: props) => {
  const {
    Days,
    selectOneItem,
    selectedItemIndex1,
    selectedItemIndex2,
    year,
    month,
    thisYear,
    thisMonth,
    thisDay,
    enableOlderDate,
  } = Props;
  return (
    <div className="week">
      {Days.map((item) => {
        const isPassed = !enableOlderDate
          ? year < thisYear ||
            (year === thisYear && month < thisMonth) ||
            (year === thisYear && month === thisMonth && item < thisDay)
          : false;
        const chDay = isPassed
          ? false
          : checkDay(year, month, item, selectedItemIndex1, selectedItemIndex2);
        const chFirst = isPassed
          ? false
          : checkFirst(year, month, item, selectedItemIndex1, selectedItemIndex2);
        const chLast = isPassed
          ? false
          : checkLast(year, month, item, selectedItemIndex1, selectedItemIndex2);
        return (
          <div
            className={isPassed ? "weekDay-container disabled-date" : "weekDay-container"}
            key={Days.indexOf(item) + 1}
          >
            <div
              style={{
                borderBottomLeftRadius: chFirst ? 25 : 0,
                borderBottomRightRadius: chLast ? 25 : 0,
                borderTopLeftRadius: chFirst ? 25 : 0,
                borderTopRightRadius: chLast ? 25 : 0,
                backgroundColor: chDay ? "#6B92D7" : undefined,
                color: chDay ? "#fff" : "#000",
              }}
              className="weekDay"
              onClick={() => selectOneItem(item)}
            >
              {item}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default WeekRow;
