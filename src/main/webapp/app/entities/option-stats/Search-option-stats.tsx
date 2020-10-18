import React, { useState } from 'react';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import {getEntities, reset } from './../instrument/instrument.reducer';
import './style.scss';
import { Select , DatePicker , Radio } from 'antd';
import axios from 'axios';
const { RangePicker } = DatePicker;
const { Option } = Select;


export const SearchOptionStats = (props) => {
  const [instrument, setInstrument] = useState([]);


  function onChange(value) {
    props.instrumentId(value)
  }

  function onChangeRadio(e) {

  }

  function onSearch(val) {
    const apiUrlSearch = 'api/instruments/search'​
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
        <RangePicker />

      </div>

      <div style={{marginLeft: 20}}>
        <Radio.Group onChange={onChangeRadio} defaultValue="a">
          <Radio.Button value="a">Active</Radio.Button>
          <Radio.Button value="b">In between</Radio.Button>
          <Radio.Button value="c">Inactive</Radio.Button>
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
