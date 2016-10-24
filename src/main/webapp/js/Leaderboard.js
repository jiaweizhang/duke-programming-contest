const React = require('react');

export class Leaderboard extends React.Component{
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.selectTab(4);
  }

  render() {
    return (
      <div>
        Leaderboard
      </div>
    );
  }
}

module.exports = Leaderboard;
