import React from 'react';
import {shallow} from 'enzyme';

import sinon from 'sinon';

import LoadingBar from 'react-redux-loading-bar';
import {Nav, Navbar} from 'reactstrap';

import {Brand, Home} from 'app/shared/layout/header/header-components';
import {AccountMenu, AdminMenu, EntitiesMenu, LocaleMenu} from 'app/shared/layout/menus';
import Header from 'app/shared/layout/header/header';
import {AUTHORITIES} from "app/config/constants";

describe('Header', () => {
  let mountedWrapper;

  const localeSpy = sinon.spy();

  const devProps = {
    isAuthenticated: true,
    isAdmin: true,
    currentLocale: 'en',
    onLocaleChange: localeSpy,
    ribbonEnv: 'dev',
    isInProduction: false,
    isSwaggerEnabled: true,
    account:[AUTHORITIES.ADMIN,AUTHORITIES.USER]
  };
  const prodProps = {
    ...devProps,
    ribbonEnv: 'prod',
    isInProduction: true,
    isSwaggerEnabled: false
  };
  const userProps = {
    ...prodProps,
    isAdmin: false,
    account:[AUTHORITIES.USER]
  };
  const guestProps = {
    ...prodProps,
    isAdmin: false,
    isAuthenticated: false,
    account:[AUTHORITIES.USER]
  };

  const wrapper = (props = devProps) => {
    if (!mountedWrapper) {
      mountedWrapper = shallow(<Header {...props} />);
    }
    return mountedWrapper;
  };

  beforeEach(() => {
    mountedWrapper = undefined;
  });

  // All tests will go here
  it('Renders a Header component in dev profile with LoadingBar, Navbar, Nav and dev ribbon.', () => {
    const component = wrapper();
    // the created snapshot must be committed to source control
    expect(component).toMatchSnapshot();
    expect(component.find(LoadingBar).length).toEqual(1);
    const navbar = component.find(Navbar);
    expect(navbar.length).toEqual(1);
    expect(navbar.find(Brand).length).toEqual(1);
    const nav = component.find(Nav);
    expect(nav.length).toEqual(1);
    expect(nav.find(Home).length).toEqual(1);
    expect(nav.find(AdminMenu).length).toEqual(1);
    expect(nav.find(EntitiesMenu).length).toEqual(1);
    expect(nav.find(LocaleMenu).length).toEqual(1);

    expect(nav.find(AccountMenu).length).toEqual(1);
    const ribbon = component.find('.ribbon.dev');
    expect(ribbon.length).toEqual(1);
  });

  it('Renders a Header component in prod profile with LoadingBar, Navbar, Nav.', () => {
    const component = wrapper(prodProps);
    // the created snapshot must be committed to source control
    expect(component).toMatchSnapshot();
    const navbar = component.find(Navbar);
    expect(navbar.length).toEqual(1);
    expect(navbar.find(Brand).length).toEqual(1);
    const nav = component.find(Nav);
    expect(nav.length).toEqual(1);
    expect(nav.find(Home).length).toEqual(1);
    expect(nav.find(AdminMenu).length).toEqual(1);
    expect(nav.find(EntitiesMenu).length).toEqual(1);
    expect(nav.find(LocaleMenu).length).toEqual(1);

    expect(nav.find(AccountMenu).length).toEqual(1);
    const ribbon = component.find('.ribbon.dev');
    expect(ribbon.length).toEqual(0);
  });

  it('Renders a Header component in prod profile with logged in User', () => {
    const nav = wrapper(userProps).find(Nav);
    expect(nav.find(AdminMenu).length).toEqual(0);
    expect(nav.find(EntitiesMenu).length).toEqual(1);
    const account = nav.find(AccountMenu);
    expect(account.first().props().isAuthenticated).toEqual(true);
  });

  it('Renders a Header component in prod profile with no logged in User', () => {
    const nav = wrapper(guestProps).find(Nav);
    expect(nav.find(AdminMenu).length).toEqual(0);
    expect(nav.find(EntitiesMenu).length).toEqual(0);
    const account = nav.find(AccountMenu);
    expect(account.length).toEqual(1);
    expect(account.first().props().isAuthenticated).toEqual(false);
  });
});
