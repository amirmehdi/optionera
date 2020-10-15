import React from 'react';
import { connect } from 'react-redux';
import { Row} from 'reactstrap';
import  OptionStats  from 'app/entities/option-stats/option-stats';
import { RouteComponentProps } from 'react-router-dom';

export type IHomeProp = StateProps;
export interface IOptionStatsProps extends StateProps, RouteComponentProps<{ url: string }> {
}

export const HomePage = (props: IOptionStatsProps) => {
  const { account } = props;
  return (
    <Row>

      {account.authorities && account.authorities[0] === "ROLE_USER" ?
        account.authorities[1] !== "ROLE_ADMIN" ?
        <OptionStats  history={props.history}
                      location={props.location}
                      match={props.match} /> : "" : ""
      }

    </Row>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});
type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(HomePage);
