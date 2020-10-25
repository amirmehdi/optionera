import React, {useState} from 'react';
import {connect} from 'react-redux';
import {IRootState} from 'app/shared/reducers';
import {getEntities, reset} from './../instrument/instrument.reducer';
import './style.scss';
import {Radio, Select} from 'antd';
import axios from 'axios';
import 'moment/locale/fa';
import moment from 'moment';
import {CloseCircleOutlined} from '@ant-design/icons';
import 'react-modern-calendar-datepicker/lib/DatePicker.css';
import DatePicker from 'react-modern-calendar-datepicker';
import Persian from 'persian-info';
import {Translate} from "react-jhipster";

moment.locale('fa_IR');

const { Option } = Select;


export const SearchOptionStats = (props) => {
  const [instrument, setInstrument] = useState([]);
  const [selectedDayRange, setSelectedDayRange] = useState({
    from:  undefined,
    to: undefined
  });

  function onChange(value, val) {
    props.instrumentId(val);
  }

  function onChangeRadio(e: any) {
    props.switch(e.target.value === 'undefined' ? undefined : e.target.value);
  }

  function onSearch(val) {
    // eslint-disable-next-line no-irregular-whitespace
    const apiUrlSearch = 'api/instruments/search';
    const requestUrl = `${apiUrlSearch}/${val}`;
    axios.get(requestUrl).then((res) => {
      setInstrument(res.data);
    });
  }

  function onChangeDate(e) {
    setSelectedDayRange(e);

    if (e.from) {
      const dateSplitFrom = Persian.date.convertJalaliToGregorian({
        year: e.from.year,
        month: e.from.month,
        day: e.from.day
      });
      const getMonthFrom = dateSplitFrom.getMonth() + 1;
      const outDateFrom = dateSplitFrom.getFullYear() + '-' + getMonthFrom.toString().padStart(2, '0') + '-' + dateSplitFrom.getDate().toString().padStart(2, '0');
      props.fromDateRange(outDateFrom);
    }

    if (e.to) {
      const dateSplitTo = Persian.date.convertJalaliToGregorian({
        year: e.to.year,
        month: e.to.month,
        day: e.to.day
      });
      const getMonthTo = dateSplitTo.getMonth() + 1;
      const outDateTO = dateSplitTo.getFullYear() + '-' + getMonthTo.toString().padStart(2, '0') + '-' + dateSplitTo.getDate().toString().padStart(2, '0');
      e.to && props.toDateRange(outDateTO);
    }
  }


  return (
    <>
      <div style={{ marginLeft: 20 }}>
        <Select
          showSearch
          style={{ width: 200 }}
          placeholder="جستجو"
          optionFilterProp="children"
          value={props.instrumentValue?.children}
          onChange={(e, val) => onChange(e, val)}
          onSearch={onSearch}
          filterOption={(input, option) =>
            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
          }
        >
          {instrument.map((i, ix) => {
            return <Option key={ix} value={i.isin}>{i.name}</Option>;
          })}
        </Select>
        {props.instrumentValue ? <CloseCircleOutlined
          onClick={() => props.instrumentId(undefined)}
          style={{ fontSize: 20, marginLeft: 5 }}/> : null}
      </div>

      <div style={{ marginLeft: 20 }}>

        <DatePicker
          value={selectedDayRange}
          onChange={onChangeDate}
          inputPlaceholder="انتخاب تاریخ"
          shouldHighlightWeekends
          locale="fa"
        />

        {props.toDateValue ? <CloseCircleOutlined
          onClick={() => {
            props.toDateRange(undefined);
            props.fromDateRange(undefined);
            setSelectedDayRange({to : undefined , from: undefined})
          }}
          style={{ fontSize: 20, marginLeft: 5 }}/> : null}
      </div>

      <div style={{ marginLeft: 20 }}>
        <Radio.Group onChange={onChangeRadio} defaultValue={!props.switchValue ? 'undefined' : props.switchValue}
                     value={!props.switchValue ? 'undefined' : props.switchValue}>
          <Radio.Button value="true"><Translate contentKey="eTradeApp.optionStats.inTheMoney">call ITM</Translate></Radio.Button>
          <Radio.Button value="undefined"><Translate contentKey="eTradeApp.optionStats.all">all</Translate></Radio.Button>
          <Radio.Button value="false"><Translate contentKey="eTradeApp.optionStats.outOfMoney">call OTM</Translate></Radio.Button>
        </Radio.Group>
      </div>

    </>
  );
};

const mapStateToProps = ({ optionStats }: IRootState) => ({
  optionStatsList: optionStats.entities,
  loading: optionStats.loading,
  totalItems: optionStats.totalItems,
  links: optionStats.links,
  entity: optionStats.entity,
  updateSuccess: optionStats.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchOptionStats);
