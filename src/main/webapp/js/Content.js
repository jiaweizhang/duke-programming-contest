const React = require('react');
const SetupScreen = require('./SetupScreen');
const Sidebar = require('./Sidebar');

export class Content extends React.Component{
  constructor(props) {
    super(props);
    console.log('content init');
    window.onresize = () => {
      this.setState({
        dimensions: this.calculateDimensions()
      });
    }

    this.state = {
      dimensions: this.calculateDimensions(),
      tabSelected: 0,
      sessionData: this.initSession()
    }
  }

  selectTab(index) {
    this.setState({
      tabSelected: index
    });
  }

  initSession() {
    let sessionData = {};
    console.log('initializing session');
    if (localStorage.getItem('account')) {
      console.log('account loaded');
    }
    if (localStorage.getItem('contest')) {
      console.log('contest loaded');
    }
    return sessionData;
  }

  updateSession(key, value) {

  }

  calculateDimensions() {
    const windowDimensions = [window.innerWidth, window.innerHeight];
    return {
      width: Math.max(windowDimensions[0], 800),
      height: Math.max(windowDimensions[1], 600)
    };
  }

  render() {
    return (
      <div>
        <div className="setup-screen" style={{
          width: this.state.dimensions.width + 'px',
          height: this.state.dimensions.height + 'px'
        }}>
          <SetupScreen dimensions={this.state.dimensions} />
        </div>
        <div className="sidebar-container">
          <Sidebar
            tabSelected={this.state.tabSelected}
            selectTab={this.selectTab.bind(this)}/>
        </div>
        <div className="main-content" style={{
          width: (this.state.dimensions.width - 60) + 'px',
          height: this.state.dimensions.height + 'px'
        }}>
          {
            React.cloneElement(
              this.props.children,
              {
                dimensions: this.state.dimensions,
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
