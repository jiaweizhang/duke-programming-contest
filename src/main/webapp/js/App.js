const React = require('react');
const Sidebar = require('./Sidebar');
const Content = require('./Content');
const Home = require('./Home');
const Registration = require('./Registration');
const Problems = require('./Problems');
const Groups = require('./Groups');
const Leaderboard = require('./Leaderboard');
const Help = require('./Help');
import { Router, Route, Link, hashHistory } from 'react-router'

export class App extends React.Component{
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <div id="content-container">
          <Router history={hashHistory}>
            <Route path="/~kevinhe/dpc" component={Content}>
              <Route path="/" component={Home}/>
              <Route path="/register" component={Registration}/>
              <Route path="/problems" component={Problems}/>
              <Route path="/groups" component={Groups}/>
              <Route path="/leaderboard" component={Leaderboard}/>
              <Route path="/help" component={Help}/>
            </Route>
          </Router>
        </div>
      </div>
    );
  }
}

module.exports = App;
