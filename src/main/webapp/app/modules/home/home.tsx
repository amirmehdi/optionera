import './home.scss';
import './../../../content/css/styles-merged.css'
import React from 'react';
import {connect} from 'react-redux';
import {Alert, Col, Row} from 'reactstrap';
import {Link} from "react-router-dom";
import {Translate} from "react-jhipster";

export type IHomeProp = StateProps;
export const Home = (props: IHomeProp) => {
  const {account} = props;

  return (
    /*    <Row>
          <Col md="9">
            <h2>
              <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
            </h2>
            <p className="lead">
              <Translate contentKey="home.subtitle">This is your homepage</Translate>
            </p>
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
        {account && account.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{username: account.login}}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>

            <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account
                yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>
          </div>
        )}
        <section className="probootstrap-hero">
          <div className="container">
            <div className="row">
              <div
                className="col-md-8 col-md-offset-2 col-sm-8 col-sm-offset-2 text-center probootstrap-hero-text pb0 "
              >
                <h1><Translate contentKey="home.title">Trading Options using a realtime data dashboard</Translate></h1>
                <p><Translate contentKey="home.subtitle">Instant review of the entire option market and find the best
                  option to trade</Translate>
                </p>
                <p>
                  <a href="#pricing" className="btn btn-primary btn-lg" role="button">
                    <Translate contentKey="home.getApp">Get This App</Translate>
                  </a>
                  <Link
                    to="/account/register"
                    className="btn btn-primary btn-ghost btn-lg"
                    role="button">
                    <Translate contentKey="home.tryForFree">Try it for free</Translate>
                  </Link>
                </p>
                {/* <p><a href="#"><i class="icon-play2"></i> Watch the video</a></p> */}
              </div>
            </div>
            <div dir="ltr" className="row probootstrap-feature-showcase">
              <div className="col-md-4 col-md-push-8 probootstrap-showcase-nav ">
                <ul>
                  <li className="active">
                    <a href="#"><Translate contentKey="home.slide.title1">All data in your screen</Translate></a>
                    <p><Translate contentKey="home.slide.subtitle2">All data in your screen</Translate></p>
                  </li>
                  <li>
                    <a href="#"><Translate contentKey="home.slide.title2">best signals in your watchlist</Translate></a>
                    <p><Translate contentKey="home.slide.subtitle2">best signals in your watchlist</Translate></p>
                  </li>
                  <li>
                    <a href="#"><Translate contentKey="home.slide.title3">api for writing your own strategy</Translate></a>
                    <p><Translate contentKey="home.slide.subtitle3">api for writing your own strategy</Translate></p>
                  </li>
                </ul>
              </div>
              <div
                className="col-md-8 col-md-pull-4 "
                style={{position: "relative"}}>
                <div className="probootstrap-home-showcase-wrap">
                  <div className="probootstrap-home-showcase-inner">
                    <div className="probootstrap-chrome">
                      <div>
                        <span/>
                        <span/>
                        <span/>
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
                className="col-md-6 col-md-offset-3 text-center section-heading "
              >
                <h2><Translate contentKey="home.features.title">Platform Features</Translate></h2>
                <p className="lead">
                  <Translate contentKey="home.features.subtitle">Platform Features</Translate>
                </p>
              </div>
            </div>
            {/* END row */}
            <div className="row probootstrap-gutter60">
              <div
                className="col-md-4 "
                data-animate-effect="fadeInLeft"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-mobile3" />
                  </div>
                  <div className="text">
                    <h3><Translate contentKey="home.features.f1title"></Translate></h3>
                    <p>
                      <Translate contentKey="home.features.f1subtitle"></Translate>
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 "
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-presentation" />
                  </div>
                  <div className="text">
                    <h3><Translate contentKey="home.features.f2title"></Translate></h3>
                    <p>
                      <Translate contentKey="home.features.f2subtitle"></Translate>
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 "
                data-animate-effect="fadeInRight"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-circle-compass" />
                  </div>
                  <div className="text">
                    <h3><Translate contentKey="home.features.f3title"></Translate></h3>
                    <p>
                      <Translate contentKey="home.features.f3subtitle"></Translate>
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 "
                data-animate-effect="fadeInLeft"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-lightbulb" />
                  </div>
                  <div className="text">
                    <h3><Translate contentKey="home.features.f4title"></Translate></h3>
                    <p>
                      <Translate contentKey="home.features.f4subtitle"></Translate>
                    </p>
                  </div>
                </div>
              </div>
              <div className="col-md-4 ">
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-magnifying-glass2" />
                  </div>
                  <div className="text">
                    <h3><Translate contentKey="home.features.f5title"></Translate></h3>
                    <p>
                      <Translate contentKey="home.features.f5subtitle"></Translate>
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-md-4 "
                data-animate-effect="fadeInRight"
              >
                <div className="service text-center">
                  <div className="icon">
                    <i className="icon-browser2" />
                  </div>
                  <div className="text">
                    <h3><Translate contentKey="home.features.f6title"></Translate></h3>
                    <p>
                      <Translate contentKey="home.features.f6subtitle"></Translate>
                    </p>
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
                className="col-md-6 col-md-offset-3 text-center section-heading "
              >
                <h2>Pricing</h2>
                <p className="lead">
                  Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name
                </p>
              </div>
            </div>
            {/* END row */}
            <div className="container-pricing">
              <div className="card-price">
                <h2>Starter</h2>
                <div className="price-green">
                  <p>$9</p>
                  <span>per month</span>
                </div>
                <p>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts.</p>
                <p className="btn-price">selected plan</p>
              </div>
              <div className="card-price">
                <h2>Starter</h2>
                <div className="price-green">
                  <p>$9</p>
                  <span>per month</span>
                </div>
                <p>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts.</p>
                <p className="btn-price">selected plan</p>
              </div>
              <div className="card-price">
                <h2>Starter</h2>
                <div className="price-green">
                  <p>$9</p>
                  <span>per month</span>
                </div>
                <p>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts.</p>
                <p className="btn-price">selected plan</p>
              </div>
              <div className="card-price">
                <h2>Starter</h2>
                <div className="price-green">
                  <p>$9</p>
                  <span>per month</span>
                </div>
                <p>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts.</p>
                <p className="btn-price">selected plan</p>
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
                <Col md="4">
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
                <Col md="4">
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
                <Col md="4">
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
            <Col md="6">
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
