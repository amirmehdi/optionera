import "./SelectDate.scss";
import * as React from "react";
import {useMemo, useState} from "react";
// eslint-disable-next-line @typescript-eslint/ban-ts-ignore
// @ts-ignore
import LeftArrowIcon from "./left-arrow.svg";
import DatePicker, {dateType} from "./datepicker/DatePicker";
// eslint-disable-next-line @typescript-eslint/ban-ts-ignore
// @ts-ignore
import moment from "jalali-moment";

// eslint-disable-next-line @typescript-eslint/class-name-casing
export interface selectDateType {
  date: dateType[];
  dateEnString: string;
  titleString?: string;
  startDate?: string;
  endDate?: string;
}

export default function SelectDate({
  placeHolder,
  titleString,
  selection,
  setSelection,
  twoSelect = false,
  style = {},
  enableOlderDate,
  isOpen,
  closeOnSelect = false,
}: {
  placeHolder: string;
  titleString?: string;
  selection?: selectDateType;
  setSelection?: (e: any) => void;
  style?: object;
  twoSelect?: boolean;
  enableOlderDate?: boolean;
  isOpen?: (e: boolean) => any;
  closeOnSelect?: boolean;
}) {
  const [showDateModal, setShowDateModal] = useState(false);
  const [titleStringState, setTitleStringState] = useState(titleString);

  useMemo(() => {
    isOpen && isOpen(showDateModal);
  }, [showDateModal]);

  useMemo(() => {
    setTitleStringState(titleString);
  }, [titleString]);

  function persianToEn(date: string) {
    // 1398/1/12
    // 2019-02-15
    return moment.from(date, "fa", "YYYY/M/D").format("YYYY-MM-DD");
  }

  return (
    <div className="custom-date-selection" style={{ ...style }}>
      <div
        className="custom-date-top-box"
        onClick={() => {
          setShowDateModal(!showDateModal);
        }}
      >
        {/* <img alt="" style={{height: 25}} src={require('./ic-calendar.svg')}/>*/}
        {/* <hr className="custom-date-internal-hotel-hr"/>*/}
        <div
          className="custom-date-text"
          style={!titleStringState || showDateModal ? { color: "#8f909e" } : { color: "#585962" }}
        >
          {titleStringState && titleStringState.length > 0 && !showDateModal
            ? titleStringState
            : titleStringState
            ? placeHolder + ": " + titleStringState
            : placeHolder}
        </div>
        <div className="custom-date-left-icon-parent">
          <img
            src={LeftArrowIcon}
            alt=""
            className={
              showDateModal ? "custom-date-left-icon custom-date-rotate" : "custom-date-left-icon"
            }
          />
        </div>
      </div>
      {showDateModal && (
        <div className="custom-date-bot-box">
          <DatePicker
            enableOlderDate={enableOlderDate}
            twoSelect={twoSelect}
            value={selection ? selection.date : undefined}
            getDate={(x: dateType[]) => {
              if (x) {
                if (!twoSelect) {
                  setSelection &&
                    setSelection({
                      dateEnString: persianToEn(x[0].year + "-" + x[0].month + "-" + x[0].day),
                      date: x,
                    });
                  setShowDateModal(false);
                } else {
                  const newDate = {
                    ...selection,
                    startDate: persianToEn(x[0].year + "-" + x[0].month + "-" + x[0].day),
                    endDate: x[1]
                      ? persianToEn(x[1].year + "-" + x[1].month + "-" + x[1].day)
                      : undefined,
                    date: x,
                  };
                  setSelection && setSelection(newDate);
                  if (newDate.endDate && closeOnSelect) {
                    setTimeout(() => {
                      setShowDateModal(false);
                    }, 1500);
                  }
                }
              } else {
                setSelection &&
                  setSelection({
                    ...selection,
                    startDate: undefined,
                    endDate: undefined,
                  });
              }
            }}
          />
        </div>
      )}
    </div>
  );
}
