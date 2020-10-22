import './footer.scss';
// import './../../../../content/css/styles-merged.css';
// import './../../../../content/css/style.min.css';
import React from 'react';
import {Col, Row} from 'reactstrap';
import {Translate} from "react-jhipster";

const Footer = props => (
  <div className="">
    <Row>
      <Col md="12" >
        <p style={{margin: 0}}>
          <small>
            © 2020 <a href="#">OptionEra</a>. <Translate contentKey="footer">All Rights Reserved.</Translate> <br />
          </small>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
