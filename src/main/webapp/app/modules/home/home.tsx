import './home.scss';
import './../../../content/css/styles-merged.css'
import React, {useState} from 'react';
import {connect} from 'react-redux';
import {Alert, Col, Row} from 'reactstrap';
import {Link} from "react-router-dom";
import {Translate} from "react-jhipster";
import {Button, Modal} from 'antd';

export type IHomeProp = StateProps;
export const Home = (props: IHomeProp) => {
  const {account} = props;
  const [active , setActive] = useState<number>(1);
  const [visible , setVisible] = useState<boolean>(false);
  const [price , setPrice] = useState<string>("0");

  const visibleModal = (e) => {
    setVisible(true);
    setPrice(e)
  };

  return (
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
                className="col-md-8 col-md-offset-2 col-sm-8 col-sm-offset-2 text-center probootstrap-hero-text pb0 flex-center"
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
                  <li onClick={() => setActive(1)} className={active === 1 ? "active" : ""}>
                    <a><Translate contentKey="home.slide.title1">All data in your screen</Translate></a>
                    <p><Translate contentKey="home.slide.subtitle2">All data in your screen</Translate></p>
                  </li>
                  <li onClick={() => setActive(2)} className={active === 2 ? "active" : ""}>
                    <a><Translate contentKey="home.slide.title2">best signals in your watchlist</Translate></a>
                    <p><Translate contentKey="home.slide.subtitle2">best signals in your watchlist</Translate></p>
                  </li>
                  <li onClick={() => setActive(3)} className={active === 3 ? "active" : ""}>
                    <a><Translate contentKey="home.slide.title3">api for writing your own strategy</Translate></a>
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
                        <li className={active === 1 ? "active" : ""}>
                          <img
                            src="content/img/1.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li className={active === 2 ? "active" : ""}>
                          <img
                            src="content/img/2.jpg"
                            alt="Image"
                            className="img-responsive"
                          />
                        </li>
                        <li className={active === 3 ? "active" : ""}>
                          <img
                            src="content/img/3.jpg"
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
        <section id="features" className="probootstrap-section probootstrap-bg-white probootstrap-zindex-above-showcase">
          <div className="container">
            <div className="row">
              <div
                className="col-md-6 col-md-offset-3 text-center section-heading flex-center"
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
        <section id="pricing" className="probootstrap-section probootstrap-zindex-above-showcase">
          <div className="container">
            <div className="row">
              <div
                className="col-md-6 col-md-offset-3 text-center section-heading flex-center">
                <h2><Translate contentKey="home.pricing.title">Pricing</Translate></h2>
                <p className="lead"><Translate contentKey="home.pricing.subtitle">Subscribe to the site for instant access to the options market board and use the site features</Translate></p>
              </div>
            </div>
            {/* END row */}
            <div className="container-pricing">
              <div className="card-price">
                <h2><Translate contentKey="home.pricing.cards.title1">Starter</Translate></h2>
                <div className="price-green">
                  <p><Translate contentKey="home.pricing.cards.price1">free</Translate></p>
                  <span>.</span>
                </div>
                <Link to="/account/register" role="button">
                  <p className="btn-price"><Translate contentKey="global.menu.account.register">register</Translate></p>
                </Link>
              </div>
              <div className="card-price">
                <h2><Translate contentKey="home.pricing.cards.title2"></Translate></h2>
                <div className="price-green">
                  <p><Translate contentKey="home.pricing.cards.price2"></Translate></p>
                  <span><Translate contentKey="home.pricing.perMonth"></Translate></span>
                </div>
                <p onClick={() => visibleModal("500,000")} className="btn-price"><Translate contentKey="home.pricing.select">select plan</Translate></p>
              </div>
              <div className="card-price">
                <h2><Translate contentKey="home.pricing.cards.title3"></Translate></h2>
                <div className="price-green">
                  <p><Translate contentKey="home.pricing.cards.price3"></Translate></p>
                  <span><Translate contentKey="home.pricing.perMonth"></Translate></span>
                </div>
                <p onClick={() => visibleModal("1,300,000")} className="btn-price"><Translate contentKey="home.pricing.select">select plan</Translate></p>
              </div>
            </div>
          </div>
          <Modal
            title="خرید اشتراک"
            visible={visible}
            footer={null}
            onCancel={() => setVisible(false)}
          >
            <h2 className="price">{price}</h2>
           <ul className="content-list-warning">
             <li>قبل از خرید اشتراک در سایت ثبت نام کنید</li>
             <li>دقت کنید ایمیل هنگام پرداخت با ایمیل حساب کاربری یکسان باشد</li>
             <li>اشتراک شما تا حداکثر ۲۴ ساعت پس از پرداخت فعال میگردد</li>
             <li>در صورت بروز مشکلی به ادمین کانال تلگرام به ادرس @optionera_admin پیام دهید</li>
           </ul>
            <div className="container-btn-modal">
              <Button onClick={() => {
                const value = price.replace(/,/g, '');
                window.open(`https://idpay.ir/optionera-ir/${value}`)
              }

              } className="btn-green">خرید اشتراک</Button>
              <Button onClick={() => setVisible(false)}>انصراف</Button>
            </div>
          </Modal>
        </section>
      </div>

      <footer className="probootstrap-footer">
        <div className="container">
          <Row>
            <Col md="12">
              <Row>
                <Col md="6">
                  <div className="probootstrap-footer-widget">
                    <h3><Translate contentKey="home.footer.aboutApp">ABOUT Optionera</Translate></h3>
                    <p style={{fontSize: 14}}><Translate contentKey="home.footer.about"></Translate></p>
                    <ul className="probootstrap-footer-social">
                      <li>
                        <a href="https://t.me/optionera">
                          <i className="icon-telegram"/>
                        </a>
                      </li>
                      <li>
                        <a href="https://t.me/Optionera_admin">
                          <i className="icon-chat"/>
                        </a>
                      </li>
                    </ul>
                  </div>
                </Col>
                <Col md="2">
                  <div className="probootstrap-footer-widget">
                    <h3><Translate contentKey="home.footer.info">INFORMATION</Translate></h3>
                    <ul className="footer-information">
                      <li>
                        <a href="#"> <i className="icon-check"/> <Translate contentKey="global.menu.home"> Home</Translate></a>
                      </li>
                      <li>
                        <a href="#features"> <i className="icon-check"/> <Translate contentKey="home.features.title">Platform Features</Translate></a>
                      </li>
                      <li>
                        <a href="#pricing"> <i className="icon-check"/> <Translate contentKey="home.pricing.title">Pricing</Translate></a>
                      </li>
                    </ul>
                  </div>
                </Col>
                {/* <Col md="4">
                  <div className="probootstrap-footer-widget">
                    <h3>RECENT BLOG</h3>
                    <ul className="footer-information">
                      <li>
                        <a className="footer-blog" href="#">
                          <img  src="content/img/blog-1.jpg" />
                          <div>
                            <p>Photoshoot Technique</p>
                            <span>30 March 2018</span>
                          </div>
                        </a>
                      </li>
                      <li>
                        <a className="footer-blog" href="#">
                          <img  src="content/img/blog-2.jpg" />
                          <div>
                            <p>Photoshoot Technique</p>
                            <span>30 March 2018</span>
                          </div>
                        </a>
                      </li>
                      <li>
                        <a className="footer-blog" href="#">
                          <img  src="content/img/blog-3.jpg" />
                          <div>
                            <p>Photoshoot Technique</p>
                            <span>30 March 2018</span>
                          </div>
                        </a>
                      </li>
                    </ul>
                  </div>
                </Col>*/}
                <Col md="4">
                  <div className="probootstrap-footer-widget">
                    <h3><Translate contentKey="home.footer.contactInfo">CONTACT INFO</Translate></h3>
                    <ul className="footer-information">
                      {/* <li>
                        291 South 21th Street,
                        Suite 721 New York NY 10016
                      </li>*/}
                      {/* <li>
                        <a className="footer-blog footer-contact" href="#">
                          <i style={{marginRight: 5}} className="icon-phone"/> 1235 2355 98 </a>
                      </li>*/}
                      <li>
                        <a className="footer-blog footer-contact" href="#">
                          <i style={{marginRight: 5}} className="icon-mail"/> optionera@gmail.com
                        </a>
                      </li>
                      <li>
                        <a className="footer-blog footer-contact" href="#">
                          <i style={{marginRight: 5}} className="icon-location"/> optionera.ir
                        </a>
                      </li>
                    </ul>
                  </div>
                </Col>
              </Row>
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
