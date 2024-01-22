import cx from 'classnames';
import React, { Fragment } from 'react';
import { SubscriptionMenu } from './../../AppNav/NavItems';
import MetisMenu from 'react-metismenu';
import ClickAwayListener from 'react-click-away-listener';

class SearchBox extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      activeSearch: true,
      lastSearchMatchId: '',
      searchText: '',
      result: []
    };
    this.findFromNestedItems = this.findFromNestedItems.bind(this);
    this.searchItems = this.searchItems.bind(this);
  }

  removeSearchHighlights(arr, nestingKey) {
    if (arr) {
      arr.map(item => {
        if (item.to != 'header') {
          let searchObj = document.getElementById(item.label.replace(' ', '_'));
          searchObj.classList.remove('searchActive');
          searchObj.classList.remove('opacity-25');
          if (!searchObj.classList.contains('active')) {
            searchObj.closest('ul').classList.remove('visible');
          } else {

          }
        }

        if (item[nestingKey]) this.removeSearchHighlights(item[nestingKey], nestingKey);
      });
      return;
    }
  }

  findFromNestedItems(arr, itemId, nestingKey) {
    return arr.reduce((a, item) => {
      if (itemId !== '' && item.label.toLowerCase().includes(itemId)) {
        a.push(item);
        return a;
      } else if (item.to != 'header') {
        let searchObj = document.getElementById(item.label.replace(' ', '_'));
        searchObj.classList.remove('searchActive');
        searchObj.classList.add('opacity-25');
      }

      if (item[nestingKey])
        return a.concat(
          this.findFromNestedItems(
            item[nestingKey].map(e => {
              e.icon = item.icon;
              return e;
            }),
            itemId,
            nestingKey
          )
        );
      return a;
    }, []);
  }

  searchItems(e) {
    if (e.target.value === '') {
      this.setState({ searchText: '' });
      this.removeSearchHighlights(SubscriptionMenu, 'content');
      return;
    }

    SubscriptionMenu.map(menu => (menu.highlightClass = 'bg-transparent'));
    const res = this.findFromNestedItems(SubscriptionMenu, e.target.value.toLowerCase(), 'content').reduce((filtered, item) => {
      if (item.to != 'header') {
        item.highlightClass = 'searchActive';
        filtered.push(item);
      }
      return filtered;
    }, []);

    this.setState({ result: res });
    if (res.length) {
      res.map((obj, i) => {
        let searchObj = document.getElementById(obj.label.replace(' ', '_'));
        if (i === 0)
          searchObj.scrollIntoView({
            behavior: 'auto',
            block: 'center',
            inline: 'center'
          });
        searchObj.classList.remove('bg-transparent');
        searchObj.classList.remove('opacity-25');
        searchObj.classList.add(obj.highlightClass);
        searchObj.closest('ul').classList.add('visible');
      });
    }
    this.setState({ searchText: e.target.value });
  }

  handleClickAway() {
    if (this.state.searchText !== '') {
      this.setState({ searchText: '' });
      this.removeSearchHighlights(SubscriptionMenu, 'content');
    }
    return;
  }

  removeActiveMenu(arr, nestingKey, title) {
    if (arr) {
      arr.map(item => {
        if (item.to != 'header') {
          let searchObj = document.getElementById(item.label.replace(' ', '_'));
          searchObj.classList.remove('active');
          if (item.label === title) {
            searchObj.classList.add('active');
            searchObj.closest('ul').classList.add('visible');
          }
        }

        if (item[nestingKey]) this.removeActiveMenu(item[nestingKey], nestingKey, title);
      });
      return;
    }
  }

  render() {
    return (
      <Fragment>
        <ClickAwayListener onClickAway={() => this.handleClickAway()}>
          <div
            className={cx('search-wrapper', {
              active: this.state.activeSearch
            })}
          >
            <div className="input-holder">
              <input
                type="text"
                className="search-input"
                placeholder="Type to search"
                onChange={this.searchItems}
                value={this.state.searchText}
              />
              <button className="search-icon">
                <span />
              </button>
            </div>

            <div
              style={{ position: 'absolute', backgroundColor: '#fafbfc', display: this.state.searchText ? '' : 'none', maxHeight: '250px' }}
              className="w-100 overflow-auto"
            >
              <MetisMenu
                content={this.state.result}
                activeLinkFromLocation
                LinkComponent={CustomLink}
                onSelected={async (e) => {
                  await this.handleClickAway();
                  this.removeActiveMenu(SubscriptionMenu, 'content', e.selectedMenuLabel);

                }}
                className="vertical-nav-menu"
                iconNamePrefix=""
                classNameStateIcon="pe-7s-angle-down"
              />
            </div>
          </div>
        </ClickAwayListener>
      </Fragment>
    );
  }
}

export default SearchBox;

class CustomLink extends React.Component {
  constructor() {
    super();
    this.onClick = this.onClick.bind(this);
  }



  onClick(e) {

    if (this.props.label === 'Become an affiliate') {
      let appUrl = 'https://appstle.firstpromoter.com/';
      if (window.app) {
        Redirect.create(app).dispatch(Redirect.Action.REMOTE, {
          url: appUrl,
          newContext: true
        });
      } else {
        window.open(appUrl, '_blank');
      }
    } else {
      if (this.props.hasSubMenu) this.props.toggleSubMenu(e);
      else {
        /*
         * your own operation using "to"
         * myGotoFunc(this.props.to);
         * or
         * this.props.activateMe(/* Parameters to pass "onSelected" event listener
         */
        this.props.activateMe({
          newLocation: this.props.to,
          selectedMenuLabel: this.props.label
        });
      }
    }
    return;
  }

  render() {
    if (this.props?.to == 'header') {
      return <h5 className="app-sidebar__heading">{this.props.label}</h5>;
    } else {
      return (
        <a
          className={`metismenu-link ${this.props.to == window.location.hash ? 'active' : ''}`}
          href={this.props.label !== 'Become an affiliate' ? this.props.to : null}
          onClick={this.onClick}
        >
          {this.props.children}
        </a>
      );
    }
  }
}
