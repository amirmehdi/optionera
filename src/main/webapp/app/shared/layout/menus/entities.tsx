import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/instrument">
      <Translate contentKey="global.menu.entities.instrument" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/option">
      <Translate contentKey="global.menu.entities.option" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/option-stats">
      <Translate contentKey="global.menu.entities.optionStats" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
