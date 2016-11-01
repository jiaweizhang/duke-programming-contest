const React = require('react');

export class Help extends React.Component{
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.selectTab(4);
  }

  render() {
    const width = this.props.dimensions.width - 160;
    const height = this.props.dimensions.height - 100;

    return (
      <div>
        <div className="help-tab card" style={{
          width: width,
          height: height
        }}>
          <div className="tab-title">
            Help
          </div>
          <div>
            asdf
          </div>
        </div>
      </div>
    );
  }
}

module.exports = Help;
