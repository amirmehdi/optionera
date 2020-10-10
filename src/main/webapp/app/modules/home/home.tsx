import './home.scss';
import './../../../content/css/styles-merged.css'
import './../../../content/css/style.min.css'

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account } = props;

  return (
    /*    <Row>
          <Col md="9">
            <h2>
              <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
            </h2>
            <p className="lead">
              <Translate contentKey="home.subtitle">This is your homepage</Translate>
            </p>
            {account && account.login ? (
              <div>
                <Alert color="success">
                  <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                    You are logged in as user {account.login}.
                  </Translate>
                </Alert>
              </div>
            ) : (
              <div>

                <Alert color="warning">
                  <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
                  <Link to="/account/register" className="alert-link">
                    <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                  </Link>
                </Alert>
              </div>
            )}
            <p>
              <Translate contentKey="home.question">If you have any question on JHipster:</Translate>
            </p>

            <ul>
              <li>
                <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
                  <Translate contentKey="home.link.homepage">JHipster homepage</Translate>
                </a>
              </li>
              <li>
                <a href="http://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
                  <Translate contentKey="home.link.stackoverflow">JHipster on Stack Overflow</Translate>
                </a>
              </li>
              <li>
                <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
                  <Translate contentKey="home.link.bugtracker">JHipster bug tracker</Translate>
                </a>
              </li>
              <li>
                <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
                  <Translate contentKey="home.link.chat">JHipster public chat room</Translate>
                </a>
              </li>
              <li>
                <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
                  <Translate contentKey="home.link.follow">follow @jhipster on Twitter</Translate>
                </a>
              </li>
            </ul>

            <p>
              <Translate contentKey="home.like">If you like JHipster, do not forget to give us a star on</Translate>{' '}
              <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
                Github
              </a>
              !
            </p>
          </Col>
          <Col md="3" className="pad">
            <span className="hipster rounded" />
          </Col>
        </Row>*/
    <div>
      <div>
        <section className="probootstrap-hero">
          <div className="container">
            <div className="row">
              <div
                className="col-md-8 col-md-offset-2 col-sm-8 col-sm-offset-2 text-center probootstrap-hero-text pb0 probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <h1>Launch your awesome startup now!</h1>
                <p>
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                  provident qui tempore natus quos quibusdam soluta at.
                </p>
                <p>
                  <a href="#" className="btn btn-primary btn-lg" role="button">
                    Get This App
                  </a>
                  <a
                    href="#"
                    className="btn btn-primary btn-ghost btn-lg"
                    role="button"
                  >
                    Try it for free
                  </a>
                </p>
                {/* <p><a href="#"><i class="icon-play2"></i> Watch the video</a></p> */}
              </div>
            </div>
            <div className="row probootstrap-feature-showcase">
              <div className="col-md-4 col-md-push-8 probootstrap-showcase-nav probootstrap-animate">
                <ul>
                  <li className="active">
                    <a href="#">Responsive Design</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Business Solution</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Brand Identity</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Creative Ideas</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Search Engine Friendly</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Easy Customization</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                </ul>
              </div>
              <div
                className="col-md-8 col-md-pull-4 probootstrap-animate"
                style={{ position: "relative" }}
              >
                <div className="probootstrap-home-showcase-wrap">
                  <div className="probootstrap-home-showcase-inner">
                    <div className="probootstrap-chrome">
                      <div>
                        <span />
                        <span />
                        <span />
                      </div>
                    </div>
                    <div className="probootstrap-image-showcase">
                      <ul className="probootstrap-images-list">
                        <li className="active">
                          <img
                            src="content/img/img_showcase_2.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_1.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_2.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_1.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_2.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_1.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
        <section className="probootstrap-section probootstrap-bg-white probootstrap-zindex-above-showcase">
          <div className="container">
            <div className="row">
              <div
                className="col-md-6 col-md-offset-3 text-center section-heading probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <h2>Platform Features</h2>
                <p className="lead">
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                  provident qui tempore natus quos quibusdam soluta at.
                </p>
              </div>
            </div>
            {/* END row */}
            <div className="row probootstrap-gutter60">
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeInLeft"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-mobile3" />
                  </div>
                  <div className="text">
                    <h3>Responsive Design</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-presentation" />
                  </div>
                  <div className="text">
                    <h3>Business Solutions</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeInRight"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-circle-compass" />
                  </div>
                  <div className="text">
                    <h3>Brand Identity</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeInLeft"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-lightbulb" />
                  </div>
                  <div className="text">
                    <h3>Creative Ideas</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </div>
                </div>
              </div>
              <div className="col-md-4 probootstrap-animate">
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-magnifying-glass2" />
                  </div>
                  <div className="text">
                    <h3>Search Engine Friendly</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeInRight"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-browser2" />
                  </div>
                  <div className="text">
                    <h3>Easy Customization</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
        <section className="probootstrap-section pb0">
          <div className="container">
            <div className="row probootstrap-feature-showcase probootstrap-animate">
              <div className="col-md-4 probootstrap-showcase-nav">
                <ul>
                  <li className="active">
                    <a href="#">Responsive Design</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Business Solution</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Brand Identity</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Creative Ideas</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Search Engine Friendly</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                  <li>
                    <a href="#">Easy Customization</a>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                      provident qui tempore natus quos quibusdam soluta at.
                    </p>
                  </li>
                </ul>
              </div>
              <div
                className="col-md-8 probootstrap-animate"
                style={{ position: "relative" }}
              >
                <div className="probootstrap-home-showcase-wrap">
                  <div className="probootstrap-home-showcase-inner">
                    <div className="probootstrap-chrome">
                      <div>
                        <span />
                        <span />
                        <span />
                      </div>
                    </div>
                    <div className="probootstrap-image-showcase">
                      <ul className="probootstrap-images-list">
                        <li className="active">
                          <img
                            src="content/img/img_showcase_2.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_1.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_2.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_1.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_2.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li>
                          <img
                            src="content/img/img_showcase_1.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
        <section className="probootstrap-section probootstrap-bg-white probootstrap-zindex-above-showcase">
          <div className="container">
            <div className="row">
              <div className="col-md-6 col-md-offset-3 text-center section-heading probootstrap-animate">
                <h2>More Features</h2>
                <p className="lead">
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                  provident qui tempore natus quos quibusdam soluta at.
                </p>
              </div>
            </div>
            {/* END row */}
            <div className="row">
              <div
                className="col-md-7 col-md-push-5 probootstrap-animate"
                data-animate-effect="fadeInRight"
              >
                <div className="owl-carousel owl-carousel-fullwidth border-rounded">
                  <div className="item">
                    <img
                      src="content/img/img_showcase_1.jpg"
                      alt="Free HTML5 Bootstrap Template by GetTemplates.co"
                    />
                  </div>
                  <div className="item">
                    <img
                      src="content/img/img_showcase_2.jpg"
                      alt="Free HTML5 Bootstrap Template by GetTemplates.co"
                    />
                  </div>
                  <div className="item">
                    <img
                      src="content/img/img_showcase_1.jpg"
                      alt="Free HTML5 Bootstrap Template by GetTemplates.co"
                    />
                  </div>
                  <div className="item">
                    <img
                      src="content/img/img_showcase_2.jpg"
                      alt="Free HTML5 Bootstrap Template by GetTemplates.co"
                    />
                  </div>
                </div>
              </div>
              <div className="col-md-5 col-md-pull-7">
                <div
                  className="service left-icon probootstrap-animate"
                  data-animate-effect="fadeInLeft"
                >
                  <div className="icon">
                    <i className="icon-mobile3" />
                  </div>
                  <div className="text">
                    <h3>Responsive Design</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit iusto
                      provident.
                    </p>
                  </div>
                </div>
                <div
                  className="service left-icon probootstrap-animate"
                  data-animate-effect="fadeInLeft"
                >
                  <div className="icon">
                    <i className="icon-presentation" />
                  </div>
                  <div className="text">
                    <h3>Business Solutions</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit iusto
                      provident.
                    </p>
                  </div>
                </div>
                <div
                  className="service left-icon probootstrap-animate"
                  data-animate-effect="fadeInLeft"
                >
                  <div className="icon">
                    <i className="icon-circle-compass" />
                  </div>
                  <div className="text">
                    <h3>Brand Identity</h3>
                    <p>
                      Lorem ipsum dolor sit amet consectetur adipisicing elit iusto
                      provident.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
        <section className="probootstrap-section probootstrap-border-top probootstrap-bg-white">
          <div className="container">
            <div className="row">
              <div className="col-md-6 col-md-offset-3 text-center section-heading probootstrap-animate">
                <h2>What People Says</h2>
                <p className="lead">
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                  provident qui tempore natus quos quibusdam soluta at.
                </p>
              </div>
            </div>
            <div className="row">
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <div className="probootstrap-testimony-wrap text-center">
                  <figure>
                    <img
                      src="content/img/person_1.jpg"
                      alt="Free Bootstrap Template by uicookies.com"
                    />
                  </figure>
                  <blockquote className="quote">
                    “Design must be functional and functionality must be translated
                    into visual aesthetics, without any reliance on gimmicks that have
                    to be explained.”{" "}
                    <cite className="author">
                      — Ferdinand A. Porsche <br /> <span>Design Lead at AirBNB</span>
                    </cite>
                  </blockquote>
                </div>
              </div>
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <div className="probootstrap-testimony-wrap text-center">
                  <figure>
                    <img
                      src="content/img/person_2.jpg"
                      alt="Free Bootstrap Template by uicookies.com"
                    />
                  </figure>
                  <blockquote className="quote">
                    “Creativity is just connecting things. When you ask creative
                    people how they did something, they feel a little guilty because
                    they didn’t really do it, they just saw something. It seemed
                    obvious to them after a while.”{" "}
                    <cite className="author">
                      — Steve Jobs <br /> <span>Co-Founder Square</span>
                    </cite>
                  </blockquote>
                </div>
              </div>
              <div
                className="col-md-4 probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <div className="probootstrap-testimony-wrap text-center">
                  <figure>
                    <img
                      src="content/img/person_3.jpg"
                      alt="Free Bootstrap Template by uicookies.com"
                    />
                  </figure>
                  <blockquote className="quote">
                    “I think design would be better if designers were much more
                    skeptical about its applications. If you believe in the potency of
                    your craft, where you choose to dole it out is not something to
                    take lightly.”{" "}
                    <cite className="author">
                      — Frank Chimero <br /> <span>Creative Director at Twitter</span>
                    </cite>
                  </blockquote>
                </div>
              </div>
            </div>
          </div>
        </section>
        <section className="probootstrap-section proboostrap-clients probootstrap-bg-white probootstrap-border-top">
          <div className="container">
            <div className="row">
              <div className="col-md-6 section-heading probootstrap-animate">
                <h2>Our Clients</h2>
                <p className="lead">
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                  provident qui tempore natus quos quibusdam soluta at.
                </p>
                <p>
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                  provident qui tempore natus quos quibusdam soluta at.
                </p>
              </div>
            </div>
            {/* END row */}
            <div className="row">
              <div
                className="col-md-3 col-sm-6 col-xs-6 text-center client-logo probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <img
                  src="content/img/client_1.png"
                  className="img-responsive"
                  alt="Free Bootstrap Template by uicookies.com"
                />
              </div>
              <div
                className="col-md-3 col-sm-6 col-xs-6 text-center client-logo probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <img
                  src="content/img/client_2.png"
                  className="img-responsive"
                  alt="Free Bootstrap Template by uicookies.com"
                />
              </div>
              <div className="clearfix visible-sm-block visible-xs-block" />
              <div
                className="col-md-3 col-sm-6 col-xs-6 text-center client-logo probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <img
                  src="content/img/client_3.png"
                  className="img-responsive"
                  alt="Free Bootstrap Template by uicookies.com"
                />
              </div>
              <div
                className="col-md-3 col-sm-6 col-xs-6 text-center client-logo probootstrap-animate"
                data-animate-effect="fadeIn"
              >
                <img
                  src="content/img/client_4.png"
                  className="img-responsive"
                  alt="Free Bootstrap Template by uicookies.com"
                />
              </div>
            </div>
          </div>
        </section>
        <section className="probootstrap-cta">
          <div className="container">
            <div className="row">
              <div className="col-md-12">
                <h2
                  className="probootstrap-animate"
                  data-animate-effect="fadeInRight"
                >
                  We had like to help and talk with you
                </h2>
                <a
                  href="#"
                  role="button"
                  className="btn btn-primary btn-lg btn-ghost probootstrap-animate"
                  data-animate-effect="fadeInLeft"
                >
                  Contact Us
                </a>
              </div>
            </div>
          </div>
        </section>
      </div>

      <footer className="probootstrap-footer">
        <div className="container">
          <Row>
            <Col md="6">
              <Row>
                <Col md="4" className="probootstrap-animate">
                  <div className="probootstrap-footer-widget">
                    <h3>Links</h3>
                    <ul>
                      <li>
                        <a href="#">Knowledge Base</a>
                      </li>
                      <li>
                        <a href="#">Career</a>
                      </li>
                      <li>
                        <a href="#">Press Releases</a>
                      </li>
                      <li>
                        <a href="#">Terms of services</a>
                      </li>
                      <li>
                        <a href="#">Privacy Policy</a>
                      </li>
                    </ul>
                  </div>
                </Col>
                <Col md="4" className="probootstrap-animate">
                  <div className="probootstrap-footer-widget">
                    <h3>Links</h3>
                    <ul>
                      <li>
                        <a href="#">Knowledge Base</a>
                      </li>
                      <li>
                        <a href="#">Career</a>
                      </li>
                      <li>
                        <a href="#">Press Releases</a>
                      </li>
                      <li>
                        <a href="#">Terms of services</a>
                      </li>
                      <li>
                        <a href="#">Privacy Policy</a>
                      </li>
                    </ul>
                  </div>
                </Col>
                <Col md="4" className="probootstrap-animate">
                  <div className="probootstrap-footer-widget">
                    <h3>Links</h3>
                    <ul>
                      <li>
                        <a href="#">Knowledge Base</a>
                      </li>
                      <li>
                        <a href="#">Career</a>
                      </li>
                      <li>
                        <a href="#">Press Releases</a>
                      </li>
                      <li>
                        <a href="#">Terms of services</a>
                      </li>
                      <li>
                        <a href="#">Privacy Policy</a>
                      </li>
                    </ul>
                  </div>
                </Col>
              </Row>
            </Col>
            <Col md="6" className="probootstrap-animate">
              <div className="probootstrap-footer-widget">
                <h3>Paragraph</h3>
                <p>
                  Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto
                  provident qui tempore natus quos quibusdam soluta at.
                </p>
                <ul className="probootstrap-footer-social">
                  <li>
                    <a href="#">
                      <i className="icon-twitter"/>
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i className="icon-facebook"/>
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i className="icon-github"/>
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i className="icon-dribbble"/>
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i className="icon-linkedin"/>
                    </a>
                  </li>
                  <li>
                    <a href="#">
                      <i className="icon-youtube"/>
                    </a>
                  </li>
                </ul>
              </div>

            </Col>
          </Row>
          {/* END row */}
        </div>
      </footer>

    </div>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
