const React = require('react');

export class Groups extends React.Component{
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.selectTab(3);
  }

  render() {
    return (
      <div>
        Groups
      </div>
    );
  }
}

module.exports = Groups;
