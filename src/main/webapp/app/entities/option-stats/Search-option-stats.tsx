import React, {useState} from 'react';
import {connect} from 'react-redux';
import {IRootState} from 'app/shared/reducers';
import {getEntities, reset} from './../instrument/instrument.reducer';
import './style.scss';
import {DatePicker, Radio, Select} from 'antd';
import axios from 'axios';
import 'moment/locale/fa';
import moment from 'moment';
import {selectDateType} from 'app/Framework/SelectDate/SelectDate';

moment.locale('fa_IR');

const {RangePicker} = DatePicker;
const {Option} = Select;


export const SearchOptionStats = (props) => {
  const [instrument, setInstrument] = useState([]);
  const [selectDateIsOpen, setSelectDateIsOpen] = useState<boolean>();
  const [selectDate, setSelectDate] = useState<selectDateType>();

  function onChange(value) {
    props.instrumentId(value)
  }

  function onChangeRadio(e:any) {
    props.switch(e.target.value)

  }

  // eslint-disable-next-line no-shadow
  // function getDateTitleString(selectDate: selectDateType) {
  //   if (!(selectDate && selectDate.date && selectDate.date[0])) return undefined;
  //   const first =
  //     selectDate.date[0].day +
  //     " " +
  //     monthNameConverter(selectDate.date[0].month) +
  //     " " +
  //     selectDate.date[0].year +
  //     " " +
  //     "الی" +
  //     " ";
  //   if (!selectDate.date[1]) return first;
  //   return (
  //     first +
  //     selectDate.date[1].day +
  //     " " +
  //     monthNameConverter(selectDate.date[1].month) +
  //     " " +
  //     selectDate.date[1].year
  //   );
  // }

  function onSearch(val) {
    // eslint-disable-next-line no-irregular-whitespace
    const apiUrlSearch = "api/instruments/search";
    const requestUrl = `${apiUrlSearch}/${val}`;
    axios.get(requestUrl).then((res) => {
      setInstrument(res.data)
    });
  }

  return (
    <div className="container-search">
      <div style={{marginLeft: 20}}>
        <Select
          showSearch
          style={{ width: 200 }}
          placeholder="جستجو"
          optionFilterProp="children"
          onChange={onChange}
          onSearch={onSearch}
          filterOption={(input, option) =>
            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
          }
        >
          {instrument.map((i ,ix) => {
            return <Option key={ix} value={i.isin}>{i.name}</Option>
          })}
        </Select>
      </div>

      <div style={{marginLeft: 20}}>
        {/* <SelectDate*/}
        {/*  enableOlderDate*/}
        {/*  twoSelect*/}
        {/*  closeOnSelect*/}
        {/*  isOpen={(is) => {*/}
        {/*    setSelectDateIsOpen(is);*/}
        {/*  }}*/}
        {/*  placeHolder={"زمان"}*/}
        {/*  titleString={selectDate && selectDate.titleString}*/}
        {/*  selection={selectDate}*/}
        {/*  setSelection={(e) => {*/}
        {/*    setSelectDate({ ...e, titleString: getDateTitleString(e) });*/}
        {/*    onFilter &&*/}
        {/*    onFilter({*/}
        {/*      selectBuySell,*/}
        {/*      selectStatus,*/}
        {/*      selectInstrument,*/}
        {/*      selectDate: { ...e, titleString: getDateTitleString(e) },*/}
        {/*    });*/}
        {/*  }}*/}
        {/* />*/}

        <RangePicker />



      </div>

      <div style={{marginLeft: 20}}>
        <Radio.Group onChange={onChangeRadio} defaultValue="a">
          <Radio.Button value="true">Active</Radio.Button>
          <Radio.Button value="undefined">In between</Radio.Button>
          <Radio.Button value="false">Inactive</Radio.Button>
        </Radio.Group>
      </div>

    </div>
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

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SearchOptionStats);
