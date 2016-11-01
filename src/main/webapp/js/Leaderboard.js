const React = require('react');

export class Leaderboard extends React.Component{
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.selectTab(3);
  }

  render() {
    const width = this.props.dimensions.width - 160;
    const height = this.props.dimensions.height - 100;

    return (
      <div>
        <div className="leaderboard-tab card" style={{
          width: width,
          height: height
        }}>
          <div className="tab-title">
            Leaderboard
          </div>
          <div>
            asdf
          </div>
        </div>
      </div>
    );
  }
}

module.exports = Leaderboard;
