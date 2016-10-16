const React = require('react');
const Sidebar = require('./Sidebar');

export class Content extends React.Component{
  constructor(props) {
    super(props);
    window.onresize = () => {
      this.setState({
        dimensions: this.calculateDimensions()
      });
    }

    this.state = {
      dimensions: this.calculateDimensions()
    }
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
        <div id="sidebar-container">
          <Sidebar />
        </div>
        <div className="main-content" style={{
          width: (this.state.dimensions.width - 60) + 'px',
          height: this.state.dimensions.height + 'px'
        }}>
          {React.cloneElement(this.props.children, {dimensions: this.state.dimensions})}
        </div>
      </div>
    );
  }
}

module.exports = Content;
