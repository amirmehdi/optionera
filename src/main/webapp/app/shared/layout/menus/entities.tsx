import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import {Translate, translate} from 'react-jhipster';
import {NavDropdown} from './menu-components';

const adminMenus = (
  <>
    <MenuItem icon="asterisk" to="/algorithm">
      <Translate contentKey="global.menu.entities.algorithm" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/signal">
      <Translate contentKey="global.menu.entities.signal" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/board">
      <Translate contentKey="global.menu.entities.board" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/instrument">
      <Translate contentKey="global.menu.entities.instrument" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/option">
      <Translate contentKey="global.menu.entities.option" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/order">
      <Translate contentKey="global.menu.entities.order" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/token">
      <Translate contentKey="global.menu.entities.token" />
    </MenuItem>
  </>
);

export const EntitiesMenu = ({ isAdmin }) => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/option-stats">
      <Translate contentKey="global.menu.entities.optionStats" />
    </MenuItem>
    {isAdmin && adminMenus}
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
