const React = require('react');
const SetupScreen = require('./SetupScreen');
const Nux = require('./Nux');
const Sidebar = require('./Sidebar');
const $ = require("jquery");
const screens = {
  NONE: -1,
  CONTESTS: 0,
  LOGIN: 1,
  SIGNUP: 2,
  SUBMITTING: 3,
  SUCCESS: 4
}
let startingScreen = screens.CONTESTS
let setupScreenHidden = false

export class Content extends React.Component{
  constructor(props) {
    super(props);
    window.onresize = () => {
      this.setState({
        dimensions: this.calculateDimensions()
      });
    }

    // const contestPayload = {
    //   "contestId": "1",
    //   "contestName": "Duke Programming Contest 2016",
    //   "startTime": "1481562000000", // 12:00PM 11/12/16
    //   "endTime": "1481583600000", // 6:00PM 11/12/16
    // };
    //
    // const createContest = $.ajax({
    //   beforeSend: (request) => {
    //     const authToken = localStorage.getItem('auth-token');
    //     console.log('auth token:', authToken);
    //     request.setRequestHeader('Authorization', authToken);
    //   },
    //   contentType: 'application/json',
    //   url: 'api/contests',
    //   type: 'POST',
    //   data: JSON.stringify(contestPayload)
    // });
    //
    // $.when(createContest).done((response) => {
    //   console.log('contest creation', response);
    // });

    // this.clearSession();
    // this.clearNuxCount();
    const sessionData = this.initSession();

    this.state = {
      dimensions: this.calculateDimensions(),
      tabSelected: 0,
      sessionData: sessionData,
      startingScreen: startingScreen,
      setupScreenHidden: setupScreenHidden
    }
  }

  selectTab(index) {
    this.setState({
      tabSelected: index
    });
  }

  initSession() {
    let sessionData = {};
    let hasContest = false;
    if (localStorage.getItem('contest')) {
      console.log('contest found');
      sessionData.contest = JSON.parse(localStorage.getItem('contest'));
      startingScreen = screens.LOGIN;
      hasContest = true;
    }
    if (localStorage.getItem('auth-token')) {
      console.log('auth-token found');
      const authToken = localStorage.getItem('auth-token');
      sessionData.authToken = authToken;
      if (localStorage.getItem('account')) {
        sessionData.account = JSON.parse(localStorage.getItem('account'));
      } else {
        const accountInfo = $.ajax({
          contentType: 'application/json',
          url: 'api/user',
          type: 'GET',
          beforeSend: (request) => {
            request.setRequestHeader('Authorization', authToken);
          }
        });

        $.when(accountInfo).done((response) => {
          console.log('account info:', response);
          sessionData.account = response.user;
        });
      }

      if (hasContest) {
        setupScreenHidden = true;
      }
    }
    return sessionData;
  }

  updateSession(key, value) {
    console.log('update', key, 'to', value);
    let sessionData = this.state.sessionData;
    if (key != 'auth-token') {
      localStorage.setItem(key, JSON.stringify(value));
    } else {
      localStorage.setItem(key, value);
    }
    sessionData[key] = value;
    this.setState({
      sessionData: sessionData
    });
  }

  clearSession() {
    localStorage.removeItem('contest');
    localStorage.removeItem('auth-token');
    localStorage.removeItem('account');
  }

  clearNuxCount() {
    localStorage.removeItem('accountInfoNux');
  }

  calculateDimensions() {
    const windowDimensions = [window.innerWidth, window.innerHeight];
    return {
      width: Math.max(windowDimensions[0], 800),
      height: Math.max(windowDimensions[1], 600)
    };
  }

  showSetupScreen(screen) {
    this.setState({
      setupScreenHidden: false,
      startingScreen: screen
    });
  }

  closeSetupScreen() {
    console.log('closing setup screen');
    this.setState({
      setupScreenHidden: true
    });
  }

  render() {
    const nuxPosition = {
      left: '65px',
      bottom: '18px'
    }
    const nuxSeen = localStorage.getItem('accountInfoNux');
    const shouldShowNux = !nuxSeen || nuxSeen && parseInt(nuxSeen) < 4;
    return (
      <div>
        {
          this.state.setupScreenHidden == false
          ? (
              <div
                id="setup-screen"
                className="setup-screen"
                style={{
                  width: this.state.dimensions.width + 'px',
                  height: this.state.dimensions.height + 'px'
              }}>
                <SetupScreen
                  dimensions={this.state.dimensions}
                  updateSession={this.updateSession.bind(this)}
                  startingScreen={this.state.startingScreen}
                  closeSetupScreen={this.closeSetupScreen.bind(this)}/>
              </div>
            )
          : null
        }
        <div className="sidebar-container">
          <Sidebar
            tabSelected={this.state.tabSelected}
            selectTab={this.selectTab.bind(this)}
            sessionData={this.state.sessionData}/>
        </div>
        {
          shouldShowNux
          ? (
            <Nux
              message="Click here to see your account info"
              position={nuxPosition}
              id={1} />
          )
          : null
        }
        <div className="main-content" style={{
          width: (this.state.dimensions.width - 60) + 'px',
          height: this.state.dimensions.height + 'px'
        }}>
          {
            React.cloneElement(
              this.props.children,
              {
                dimensions: this.state.dimensions,
                updateSession: this.updateSession.bind(this),
                sessionData: this.state.sessionData,
                selectTab: this.selectTab.bind(this)
              }
            )
          }
        </div>
      </div>
    );
  }
}

module.exports = Content;
