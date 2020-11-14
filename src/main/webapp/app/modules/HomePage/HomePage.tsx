import React from 'react';
import {connect} from 'react-redux';
import OptionStats from 'app/entities/option-stats/option-stats';
import {RouteComponentProps} from 'react-router-dom';

export type IHomeProp = StateProps;
export interface IOptionStatsProps extends StateProps, RouteComponentProps<{ url: string }> {
}

export const HomePage = (props: IOptionStatsProps) => {
  const { account } = props;
  return (
    <div style={{padding: 10}}>
      {/* {account.authorities && account.authorities.includes("ROLE_USER") ?
        !account.authorities.includes("ROLE_ADMIN") ?
        <OptionStats  history={props.history}
                      location={props.location}
                      match={props.match} /> : "" : ""
      }*/ }
      <OptionStats  history={props.history}
                    location={props.location}
                    match={props.match} />
    </div>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});
type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(HomePage);
