const React = require('react');

export class Groups extends React.Component{
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.selectTab(2);
  }

  render() {
    const width = this.props.dimensions.width - 160;
    const height = this.props.dimensions.height - 100;

    return (
      <div>
        <div className="groups-tab card" style={{
          width: width,
          height: height
        }}>
          <div className="tab-title">
            Groups
          </div>
          <div>
            asdf
          </div>
        </div>
      </div>
    );
  }
}

module.exports = Groups;
