import './footer.scss';
// import './../../../../content/css/styles-merged.css';
// import './../../../../content/css/style.min.css';

import React from 'react';
import {Col, Row} from 'reactstrap';

const Footer = props => (
  <div className="">
    <Row>
      <Col md="12" >
        <p className="copy">
          <small>
            © 2020 <a href="#">OptionEra</a>. All Rights Reserved. <br />
          </small>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
