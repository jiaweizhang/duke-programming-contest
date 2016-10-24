const React = require('react');

export class Help extends React.Component{
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.selectTab(5);
  }

  render() {
    return (
      <div>
        Help
      </div>
    );
  }
}

module.exports = Help;
