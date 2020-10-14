import './footer.scss';

import React from 'react';
import {Col, Row} from 'reactstrap';

const Footer = props => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>
          <small>
            © 2020 <a href="#">OptionEra</a>. All Rights Reserved. <br/>
          </small>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
