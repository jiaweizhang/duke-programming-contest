const React = require('react');
const SIDEBAR_WIDTH = 60;
const TITLE_HEIGHT = 100;
const CONTAINER_WIDTH = 500;
const CONTAINER_HEIGHT = 220;

export class Home extends React.Component{
  constructor(props) {
    super(props);
  }

  render() {
    const width = this.props.dimensions.width - 160;
    const height = this.props.dimensions.height - 100;

    return (
      <div className="home-tab">
        <div
          className="home-intro card"
          style={{
            left: '50px',
            top: '50px',
            width: width,
            height: height
          }}>
          <div className="tab-title">
            Welcome!
          </div>
          <div
            className="home-tab-getting-started"
            style={{
              left: (width - CONTAINER_WIDTH)/2 + 'px',
              top: (height - CONTAINER_HEIGHT - TITLE_HEIGHT)/2 + TITLE_HEIGHT + 'px',
              width: CONTAINER_WIDTH + 'px',
              height: CONTAINER_HEIGHT + 'px',
              border: '1px solid gray',
              borderRadius: '10px',
              boxShadow: '0 0 2px black'
            }}>
            <div className="home-tab-header">
              > Getting Started
            </div>
            <div className="home-tab-getting-started-items unselectable">
              <div className="home-tab-getting-started-item">
                <div className="link-button register">
                  <i className="fa fa-clipboard" aria-hidden="true"></i>
                  &nbsp; Register
                </div>
                <div style={{display: 'inline-block', marginLeft: '10px'}}>
                  for the programming contest
                </div>
              </div>
              <div className="home-tab-getting-started-item">
                <div className="link-button groups">
                  <i className="fa fa-users" aria-hidden="true"></i>
                  &nbsp; Find a group
                </div>
                <div style={{display: 'inline-block', marginLeft: '10px'}}>
                  and invite your friends
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

module.exports = Home;
